/*
 * Copyright (C) 2016 normal
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package epgtools.dumpepgfromts.dataextractor.channel;

import epgtools.dumpepgfromts.dataextractor.AbstractDataExtractor;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.SectionBody;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableBody;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableRepeatingPart;
import libepg.epg.section.descriptor.DESCRIPTOR_TAG;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.servicedescriptor.SERVICE_TYPE;
import libepg.epg.section.descriptor.servicedescriptor.ServiceDescriptor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.keyvalue.MultiKey;

/**
 * SDTからチャンネル情報を抽出し、重複を除去してリスト化する。 取り出す情報は以下の通り。
 * transport_stream_id=ServiceDescriptionTableBody.getTransport_stream_id<br>
 * original_network_id=ServiceDescriptionTableBody.getOriginal_network_id<br>
 * service_id=ServiceDescriptionTableRepeatingPart.getService_id<br>
 * ServiceDescriptor.getService_name_String<br>
 *
 * @author normal
 */
public final class ChannelDataExtractor extends AbstractDataExtractor<Channel> {

    /**
     * @param source 処理対象のセクション
     * @throws IllegalArgumentException　<br>
     * 1:テーブル識別値がSDTのそれではない<br>
     * 2:CRCエラーが起きている場合。<br>
     */
    public ChannelDataExtractor(Section source) {
        super(source, TABLE_ID.SDT);
    }

    /**
     * @return チャンネル情報をまとめたオブジェクト
     * @throws IllegalStateException セクションのデータ部分の型がテーブルIdの指定と異なる場合。
     */
    @Override
    public Map<MultiKey<Integer>, Channel> getDataList() throws IllegalStateException {

        final boolean isPutMaeesgage = false;

        final SectionBody b = this.getSource().getSectionBody();

        if (b.getClass() != this.getSource().getTable_id_const().getDataType()) {
            //まずありえないのでテストケースにはしない。
            throw new IllegalStateException("セクションとセクション本体のデータ型が異なっています。セクション = " + this.getSource().getClass() + " 本体 = " + b.getClass() + " セクションのデータ = " + Hex.encodeHexString(this.getSource().getData()));
        }

        final ServiceDescriptionTableBody body = (ServiceDescriptionTableBody) b;

        final int transport_stream_id = body.getTransport_stream_id();
        final int original_network_id = body.getOriginal_network_id();
        int service_id = -1;
        String service_name_String;

        final Set<Channel> ret = Collections.synchronizedSet(new HashSet<>());
        if (LOG.isInfoEnabled() && isPutMaeesgage) {
            LOG.info("重複排除用セット作製");
        }

        final List<ServiceDescriptionTableRepeatingPart> rep = body.getSDTRepeatingPartList();

        if (LOG.isInfoEnabled() && isPutMaeesgage) {
            LOG.info("繰り返し項目数 = " + rep.size());
        }

        ServiceDescriptor d = null;
        for (ServiceDescriptionTableRepeatingPart rp : rep) {
            service_id = rp.getService_id();
            //とりあえず、TV用の局情報があるサービス記述子は1個だけだと思うことにする。
            final List<Descriptor> ld = rp.getDescriptors_loop().getDescriptors_loopList();

            if (LOG.isInfoEnabled() && isPutMaeesgage) {
                LOG.info("記述子数 = " + ld.size());
                for (Descriptor desc : ld) {
                    LOG.info("\n*********************************************************************************************************************************************************\n"
                            + "記述子 = " + desc
                            + "*********************************************************************************************************************************************************");
                }
            }

            for (Descriptor desc : ld) {
                if (desc.getDescriptor_tag_const() == DESCRIPTOR_TAG.SERVICE_DESCRIPTOR) {
                    d = (ServiceDescriptor) desc;
                    if (d.getService_type_Enum() == SERVICE_TYPE.DIGITAL_TV_SERVICE) {
                        if (LOG.isInfoEnabled() && isPutMaeesgage) {
                            LOG.info("TV用の記述子を発見。内容 = " + d.toString());
                        }
                        break;
                    }
                }
            }
            //たまにサービス名を書いていない記述子があるので、その場合は事業者名を入れる。それもなければダミーを入れる。空白が入っていたりした場合はそのまま。
            POST_PROCESS:
            {
                //まずないとは思うが記述子が無いケース
                if (d == null) {
                    LOG.warn("サービス記述子が見つかりません。ダミーで代用します。 セクション = " + Hex.encodeHexString(this.getSource().getData()));
                    service_name_String = "unknown-display-name";
                    break POST_PROCESS;
                }
                //正常系
                if ((d.getService_name_String() != null) && !("".equals(d.getService_name_String()))) {
                    LOG.debug("サービス名を設定します。");
                    service_name_String = d.getService_name_String();
                    break POST_PROCESS;
                }
                //サービス事業者名があるならそれで代用する。
                if ((d.getService_provider_name_String() != null) && !("".equals(d.getService_provider_name_String()))) {
                    LOG.warn("事業者名で代用します。 セクション = " + Hex.encodeHexString(this.getSource().getData()));
                    service_name_String = d.getService_provider_name_String();
                    break POST_PROCESS;
                }
                //どれもないならダミーで代用。
                LOG.warn("サービス名、サービス事業差名が記載されたサービス記述子が見つかりません。ダミーで代用します。 セクション = " + Hex.encodeHexString(this.getSource().getData()));
                service_name_String = "unknown-display-name";
            }

            LOG.debug("サービス名 = " + service_name_String);
            final Channel ch = new Channel(transport_stream_id, original_network_id, service_id, service_name_String);
            boolean addret = ret.add(ch);
            if (addret = false) {
                LOG.info("重複しました。 データ = " + ch);
            }
        }
        if (LOG.isInfoEnabled() && isPutMaeesgage) {
            LOG.info("サービス情報の件数 = " + ret.size());
        }
        return this.SetToMap(ret);

    }

}
