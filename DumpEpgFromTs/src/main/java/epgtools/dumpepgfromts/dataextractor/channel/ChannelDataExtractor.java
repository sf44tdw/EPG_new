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
package epgtools.dumpchannelfromts.dataextractor.channel;

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import libepg.epg.section.Section;
import libepg.epg.section.Section.CRC_STATUS;
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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SDTからチャンネル情報を抽出し、重複を除去してリスト化する。 取り出す情報は以下の通り。
 * transport_stream_id=ServiceDescriptionTableBody.getTransport_stream_id
 * original_network_id=ServiceDescriptionTableBody.getOriginal_network_id
 * service_id=ServiceDescriptionTableRepeatingPart.getService_id
 * ServiceDescriptor.getService_name_String
 *
 * @author normal
 */
public final class ChannelDataExtractor {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }
    private final Section source;

    /**
     * @throws IllegalArgumentException　<br>
     * 1:テーブル識別値がSDTのそれではない<br>
     * 2:CRCエラーが起きている場合。<br>
     */
    public ChannelDataExtractor(Section source) throws IllegalArgumentException {
        this.source = source;
        final String hexDump = Hex.encodeHexString(this.source.getData());
        if (this.source.checkCRC() != CRC_STATUS.NO_CRC_ERROR) {
            throw new IllegalArgumentException("CRCエラーです。 セクション = " + hexDump);
        }
        if (this.source.getTable_id_const() != TABLE_ID.SDT) {
            throw new IllegalArgumentException("テーブル識別がSDTではありません。 値 = " + Integer.toHexString(this.source.getTable_id()) + " 定数 = " + this.source.getTable_id_const() + " セクション = " + hexDump);
        }

    }

    public Section getSource() {
        return source;
    }

    /**
     * @return チャンネル情報をまとめたマップ。
     * @throws IllegalStateException セクションのデータ部分の型がテーブルIdの指定と異なる場合。
     */
    public Map<MultiKey<Integer>, Channel> getChannels() throws IllegalStateException {

        final SectionBody b = this.getSource().getSectionBody();

        if (b.getClass() != this.source.getTable_id_const().getDataType()) {
            //まずありえないのでテストケースにはしない。
            throw new IllegalStateException("セクションとセクション本体のデータ型が異なっています。セクション = " + this.source.getClass() + " 本体 = " + b.getClass() + " セクションのデータ = " + Hex.encodeHexString(this.source.getData()));
        }

        final ServiceDescriptionTableBody body = (ServiceDescriptionTableBody) b;

        final int transport_stream_id = body.getTransport_stream_id();
        final int original_network_id = body.getOriginal_network_id();
        int service_id = -1;
        String service_name_String;
        final Map<MultiKey<Integer>, Channel> ret = new ConcurrentHashMap<>();

        final List<ServiceDescriptionTableRepeatingPart> rep = body.getSDTRepeatingPartList();

        if (LOG.isDebugEnabled()) {
            LOG.debug("繰り返し項目数 = " + rep.size());
        }

        ServiceDescriptor d = null;
        for (ServiceDescriptionTableRepeatingPart rp : rep) {
            service_id = rp.getService_id();
            //とりあえず、TV用の局情報があるサービス記述子は1個だけだと思うことにする。
            final List<Descriptor> ld = rp.getDescriptors_loop().getDescriptors_loopList();

            if (LOG.isDebugEnabled()) {
                LOG.debug("記述子数 = " + ld.size());
            }

            for (Descriptor desc : ld) {
                if (desc.getDescriptor_tag_const() == DESCRIPTOR_TAG.SERVICE_DESCRIPTOR) {
                    d = (ServiceDescriptor) desc;
                    if (d.getService_type_Enum() == SERVICE_TYPE.DIGITAL_TV_SERVICE) {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("TV用の記述子を発見。内容 = " + d.toString());
                        }
                        break;
                    }
                }
            }
            if ((d == null) || (d.getService_name_String() == null || "".equals(d.getService_provider_name_String()))) {
                service_name_String = "unknown-display-name";
            } else {
                service_name_String = d.getService_name_String();
            }
            final Channel ch = new Channel(transport_stream_id, original_network_id, service_id, service_name_String);
            ret.put(ch.getMuiltiKey(), ch);
        }
       return  Collections.unmodifiableMap(ret);

    }

}
