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

import epgtools.dumpepgfromts.dataextractor.DataObject;

/**
 * チャンネル情報を保持するクラス。 保持する情報は以下の通り。<br>
 * transport_stream_id<br>
 * original_network_id<br>
 * service_id<br>
 * display-name(service_name)<br>
 *
 * @author normal
 */
public final class Channel extends DataObject {

    private final String display_name;

    /**
     * @param transport_stream_id トランスポートストリーム識別 (16bit符号なし
     * ただし、0x0000と0xffffは使用禁止。)
     * @param original_network_id オリジナルネットワーク識別 (16bit16bit符号なし)
     * @param service_id サービス識別 (16bit16bit符号なし)
     * @param display_name サービス名 (null 空文字禁止)
     * @throws IllegalArgumentException 各フィールドに不正な値がセットされたとき。
     */
    public Channel(int transport_stream_id, int original_network_id, int service_id, String display_name) throws IllegalArgumentException {

        super(transport_stream_id, original_network_id, service_id);

        this.display_name = Channel.replaceNull(display_name);

    }

    public String getDisplay_name() {
        return display_name;
    }

}
