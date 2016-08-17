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
import libepg.epg.section.Section;
import libepg.epg.section.Section.CRC_STATUS;
import libepg.epg.section.SectionBody;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableBody;
import org.apache.commons.codec.binary.Hex;
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
     * @throws IllegalArgumentException　テーブル識別値がSDTのそれではないか、CRCエラーが起きている場合。
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

    public void getChannels() {
        SectionBody body = this.getSource().getSectionBody();
    }

    public Section getSource() {
        return source;
    }

}
