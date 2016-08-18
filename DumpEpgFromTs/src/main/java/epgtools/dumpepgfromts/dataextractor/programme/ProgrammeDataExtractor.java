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
package epgtools.dumpepgfromts.dataextractor.programme;

import epgtools.dumpepgfromts.dataextractor.AbstractDataExtractor;
import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.eventinformationtable.EventInformationTableBody;
import libepg.epg.section.body.eventinformationtable.EventInformationTableRepeatingPart;
import libepg.epg.section.descriptor.DESCRIPTOR_TAG;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.contentdescriptor.ContentDescriptor;
import libepg.epg.section.descriptor.contentdescriptor.Nibble;
import libepg.epg.section.descriptor.shorteventdescriptor.ShortEventDescriptor;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.keyvalue.MultiKey;

/**
 * EITから番組情報を取り出し、重複を除去してリスト化する。取り出す情報は以下の通り。
 * EventInformationTableBody.getTransport_stream_id
 * EventInformationTableBody.getOriginal_network_id
 * EventInformationTableBody.getService_id
 * EventInformationTableRepeatingPart.getEvent_id
 * EventInformationTableRepeatingPart.getStart_time_Object
 * EventInformationTableRepeatingPart.getStopTime_Object
 * ShortEventDescriptor.getEvent_name_String ShortEventDescriptor.getText_String
 * ExtendedEventDescriptor.getStoredText ContentDescriptor.getNibbles
 *
 * @author normal
 */
public class ProgrammeDataExtractor extends AbstractDataExtractor<Programme> {

    public ProgrammeDataExtractor(Section source) throws IllegalArgumentException {
        super(source, TABLE_ID.EIT_OTHER_STREAM_8_DAYS, TABLE_ID.EIT_OTHER_STREAM_NOW_AND_NEXT, TABLE_ID.EIT_THIS_STREAM_8_DAYS, TABLE_ID.EIT_THIS_STREAM_NOW_AND_NEXT);
    }

    @Override
    public Map<MultiKey<Integer>, Programme> getDataList() throws IllegalStateException {

        Set<Programme> pSet = this.initiarizeSet();

        this.checkSectionBodyType();
        EventInformationTableBody b = (EventInformationTableBody) this.getSource().getSectionBody();

        final TABLE_ID tid = this.getSource().getTable_id_const();

        boolean this_or_other = false;
        if (tid == TABLE_ID.EIT_THIS_STREAM_8_DAYS || tid == TABLE_ID.EIT_OTHER_STREAM_NOW_AND_NEXT) {
            this_or_other = true;
        } else if (tid == TABLE_ID.EIT_THIS_STREAM_8_DAYS || tid == TABLE_ID.EIT_THIS_STREAM_NOW_AND_NEXT) {
            this_or_other = false;
        }

        int transport_stream_id = b.getTransport_stream_id();

        int original_network_id = b.getOriginal_network_id();

        int service_id = b.getService_id();

        int event_id = Integer.MIN_VALUE;

        Timestamp start_Time = null;

        Timestamp stop_Time = null;

        String event_name = null;

        String description = null;

        String additional_description = null;

        List<Nibble> nibbles = null;

        final List<EventInformationTableRepeatingPart> rp = b.getEITRepeatingPartList();

        for (EventInformationTableRepeatingPart rpart : rp) {
            REPEATING_PART:
            {
                event_id = rpart.getEvent_id();

                try {
                    start_Time = rpart.getStart_time_Object();

                } catch (Exception ex) {
                    LOG.warn("開始時刻のタイムスタンプ生成に失敗しました。 ダミーを入力します。　セクション = " + Hex.encodeHexString(this.getSource().getData()), ex);
                    //タイムスタンプが取得できなかったので、ダミーで代用。(3億年ばかり過去の日付)
                    start_Time = new Timestamp(Long.MIN_VALUE);
                }

                try {

                    stop_Time = rpart.getStop_Time_Object();

                } catch (Exception ex) {
                    LOG.warn("終了時刻のタイムスタンプ生成に失敗しました。ダミーを入力します。　 セクション = " + Hex.encodeHexString(this.getSource().getData()), ex);
                    //タイムスタンプが取得できなかったので、ダミーで代用。(3億年ばかり未来の日付)
                    stop_Time = new Timestamp(Long.MAX_VALUE);
                }

                final StringBuilder sb_extendedEd = new StringBuilder();
                for (Descriptor desc : rpart.getDescriptors_loop().getDescriptors_loopList()) {

                    //短形イベント記述子の処理(複数は存在しない想定)
                    if (desc.getDescriptor_tag_const() == DESCRIPTOR_TAG.SHORT_EVENT_DESCRIPTOR) {

                        ShortEventDescriptor sedesc = (ShortEventDescriptor) desc;

                        event_name = sedesc.getEvent_name_String();

                        description = sedesc.getText_String();
                    }

                    //拡張イベント記述子の処理(複数ある場合は文字列を連結する。)
                    if (desc.getDescriptor_tag_const() == DESCRIPTOR_TAG.CONTENT_DESCRIPTOR) {

                        ContentDescriptor cdesc = (ContentDescriptor) desc;

                        nibbles = cdesc.getNibbles();
                    }

                    //コンテント記述子の処理
                }
                additional_description = sb_extendedEd.toString();

                //番組情報を生成。
                final Programme p = new Programme(
                        event_id,
                        start_Time,
                        stop_Time,
                        event_name,
                        description,
                        additional_description,
                        nibbles,
                        transport_stream_id,
                        original_network_id,
                        service_id,
                        this_or_other
                );
                if (p != null) {
                    pSet.add(p);
                } else {
                    LOG.error("番組情報がnullです。 セクション = " + Hex.encodeHexString(this.getSource().getData()));
                }
            }

        }
        return this.SetToMap(pSet);
    }

}