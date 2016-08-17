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

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * チャンネル情報を保持するクラス。 保持する情報は以下の通り。<br>
 * transport_stream_id<br>
 * original_network_id<br>
 * service_id<br>
 * display-name(service_name)<br>
 *
 * @author normal
 */
public final class Channel {

    private final KeyFields kf;
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
        this.kf = new KeyFields(transport_stream_id, original_network_id, service_id);
        this.display_name = display_name;

        String errorMessage = null;
        CHECK:
        {
            if (this.display_name == null || "".equals(this.display_name)) {
                errorMessage = "サービス名がnullもしくは空文字列です。";
                break CHECK;
            }
        }
        if (errorMessage != null) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public int getTransport_stream_id() {
        return this.kf.getTransport_stream_id();
    }

    public int getOriginal_network_id() {
        return this.kf.getOriginal_network_id();
    }

    public int getService_id() {
        return this.kf.getService_id();
    }

    public String getDisplay_name() {
        return display_name;
    }

    public MultiKey<Integer> getMuiltiKey() {
        return kf.getMuiltiKey();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 31, this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
