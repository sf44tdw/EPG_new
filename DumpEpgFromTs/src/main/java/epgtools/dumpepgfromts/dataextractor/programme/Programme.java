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

import epgtools.dumpepgfromts.dataextractor.DataObject;
import java.sql.Timestamp;
import java.util.List;
import libepg.epg.section.descriptor.contentdescriptor.NIBBLE_LEVEL_2;
import libepg.epg.section.descriptor.contentdescriptor.Nibble;

/**
 * 番組情報を保持するクラス。 保持する情報は以下の通り。<br>
 * transport_stream_id <br>
 * original_network_id<br>
 * service_id<br>
 * Start_time<br>
 * StopTime<br>
 * Event_id<br>
 * Event_name_String<br>
 * Text_String<br>
 * Nibble<br>
 *
 * @author normal
 */
public final class Programme extends DataObject {

    private final int event_id;
    private final long start_Time;
    private final long stop_Time;
    private final String event_name;
    private final String description;
    private final String additional_description;
    private final List<Nibble> nibbles;
    private final boolean this_or_other;

    /**
     *
     * @param event_id 番組ID
     * @param start_Time 開始時刻
     * @param stop_Time 終了時刻
     * @param event_name 番組名
     * @param description 番組内容
     * @param additional_description 拡張形式イベント記述子の内容
     * @param nibbles ジャンルコード
     * @param transport_stream_id 補足参照
     * @param original_network_id 補足参照
     * @param service_id 補足参照
     * @param this_or_other 自局、他局フラグ
     *
     * @see DataObject {
     *
     */
    public Programme(int event_id, Timestamp start_Time, Timestamp stop_Time, String event_name, String description, String additional_description, List<Nibble> nibbles, int transport_stream_id, int original_network_id, int service_id, boolean this_or_other) {
        super(transport_stream_id, original_network_id, service_id);
        this.event_id = event_id;
        this.start_Time = start_Time.getTime();
        this.stop_Time = stop_Time.getTime();
        this.event_name = replaceNull(event_name);
        this.description = replaceNull(description);
        this.additional_description = replaceNull(additional_description);
        this.nibbles = nibbles;
        this.this_or_other = this_or_other;

    }

    /**
     * @return 番組ID
     */
    public int getEvent_id() {
        return event_id;
    }

    /**
     * @return 開始時刻
     */
    public Timestamp getStart_Time() {
        return new Timestamp(this.start_Time);
    }

    /**
     * @return 終了時刻
     */
    public Timestamp getStop_Time() {
        return new Timestamp(this.stop_Time);
    }

    /**
     * @return 番組名
     */
    public String getEvent_name() {
        return event_name;
    }

    /**
     * @return 番組内容
     */
    public String getDescription() {
        return description;
    }

    /**
     * @return 拡張形式イベント記述子の内容
     */
    public String getAdditional_description() {
        return additional_description;
    }

    /**
     * @return 自局の番組ならtrue そうでないならfalse
     */
    public boolean isThis_or_other() {
        return this_or_other;
    }

    /**
     * @return ジャンルコード
     */
    public List<Nibble> getNibble() {
        return nibbles;
    }

    /**
     * ToStringの若干見やすい版
     *
     * @return 中身
     */
    public String getString() {
        StringBuilder sb = new StringBuilder();
        sb.append("TSID = ").append(this.getTransport_stream_id()).append("\n");
        sb.append("ONID = ").append(this.getOriginal_network_id()).append("\n");
        sb.append("SID = ").append(this.getService_id()).append("\n");
        sb.append("番組ID = ").append(this.getEvent_id()).append("\n");
        sb.append("開始時刻 = ").append(this.getStart_Time()).append("\n");
        sb.append("終了時刻 = ").append(this.getStop_Time()).append("\n");
        sb.append("番組名 = ").append(this.getEvent_name()).append("\n");
        sb.append("番組内容 = ").append(this.getDescription()).append("\n");
        sb.append("補足 = ").append(this.getAdditional_description()).append("\n");
        sb.append("自局フラグ = ").append(this.isThis_or_other()).append("\n");
        sb.append("ジャンル = \n");
        if (this.getNibble() == null) {
        } else {
            for (Nibble n : this.getNibble()) {
                if (n == null) {
                } else {
                    NIBBLE_LEVEL_2 nb = n.getContent_nibble_level_2();
                    if (nb == null) {
                    } else {
                        sb.append("ジャンル(en) = ").append(n.getContent_nibble_level_2().getNibble_en()).append("\n");
                        sb.append("ジャンル(jp) = ").append(n.getContent_nibble_level_2().getNibble_jp()).append("\n");
                    }
                }
            }
        }
        return sb.toString();
    }
}
