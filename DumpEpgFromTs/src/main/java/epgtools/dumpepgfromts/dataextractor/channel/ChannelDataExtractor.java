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
import java.util.List;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableBody;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableRepeatingPart;
import libepg.epg.section.descriptor.DESCRIPTOR_TAG;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.servicedescriptor.SERVICE_TYPE;
import libepg.epg.section.descriptor.servicedescriptor.ServiceDescriptor;
import org.apache.commons.codec.binary.Hex;

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

    public ChannelDataExtractor() {
        super(TABLE_ID.SDT);
    }

    /**
     * @throws IllegalStateException セクションのデータ部分の型がテーブルIdの指定と異なる場合。
     */
    @Override
    public void makeDataSet(Section s) throws IllegalStateException {
        this.checkSection(s);
        this.checkSectionBodyType(s);

        final boolean isPutMaeesgage = false;

        final ServiceDescriptionTableBody body = (ServiceDescriptionTableBody) s.getSectionBody();

        final int transport_stream_id = body.getTransport_stream_id();
        final int original_network_id = body.getOriginal_network_id();
        int service_id = -1;
        String service_name_String;

        final List<ServiceDescriptionTableRepeatingPart> rep = body.getSDTRepeatingPartList();

        if (LOG.isInfoEnabled() && isPutMaeesgage) {
            LOG.info("繰り返し項目数 = " + rep.size());
        }

        ServiceDescriptor d = null;
        for (ServiceDescriptionTableRepeatingPart rp : rep) {
            service_id = rp.getService_id();
            //とりあえず、TV用の局情報があるサービス記述子は1個だけだと思うことにする。
            final List<Descriptor> ld = rp.getDescriptors_loop().getDescriptors_loopList();

            for (Descriptor desc : ld) {
                PROCESS_SDT:
                {
                    if (desc.getDescriptor_tag_const() != DESCRIPTOR_TAG.SERVICE_DESCRIPTOR) {
                        break PROCESS_SDT;
                    }
                    d = (ServiceDescriptor) desc;
                    if (d.getService_type_Enum() != SERVICE_TYPE.DIGITAL_TV_SERVICE) {
                        break PROCESS_SDT;
                    }
                    if (LOG.isInfoEnabled() && isPutMaeesgage) {
                        LOG.info("TV用の記述子を発見。内容 = " + d.toString());
                    }
                    SERVICE_NAME:
                    {
                        service_name_String = "";
                        //正常系
                        if ((d.getService_name_String() != null) && !("".equals(d.getService_name_String()))) {
                            service_name_String = d.getService_name_String();
                            break SERVICE_NAME;
                        }

                        //サービス事業者名があるならそれで代用する。
                        if ((d.getService_provider_name_String() != null) && !("".equals(d.getService_provider_name_String()))) {
                            LOG.warn("事業者名で代用します。 セクション = " + Hex.encodeHexString(s.getData()));
                            service_name_String = d.getService_provider_name_String();
                            break SERVICE_NAME;
                        }

                        //たまにサービス名を書いていない記述子があるので、その場合は事業者名を入れる。それもなければダミーを入れる。空白が入っていたりした場合はそのまま。
                        //まずないとは思うが記述子が無いケース
                        if (d == null) {
                            LOG.warn("サービス記述子が見つかりません。ダミーで代用します。 セクション = " + Hex.encodeHexString(s.getData()));
                            service_name_String = "unknown-display-name";
                            break SERVICE_NAME;
                        }
                    }

                    Channel ch = new Channel(transport_stream_id, original_network_id, service_id, service_name_String);
//                    LOG.info("追加前のチャンネル情報 = " + ch);
                    boolean addret = this.getDataSet().add(ch);
                }
            }
        }
        if (LOG.isInfoEnabled() && isPutMaeesgage) {
            LOG.info("サービス情報の件数 = " + this.getDataSet().size());
        }
    }

}
