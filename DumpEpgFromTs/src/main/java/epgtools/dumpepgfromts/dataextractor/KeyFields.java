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
package epgtools.dumpepgfromts.dataextractor;

import static libepg.epg.section.range.SectionValueRangeChecker.TRANSPORT_STREAM_ID_RANGE;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * 検索に使用する項目を保持し、マップ用の複合キーを生成する。
 *
 * @author dosdiaopfhj
 */
public final  class KeyFields{

    /**
     * トランスポートストリーム識別
     */
    private final int transport_stream_id;

    /**
     * オリジナルネットワーク識別
     */
    private final int original_network_id;

    /**
     * サービス識別
     */
    private final int service_id;

    /**
     * @param transport_stream_id トランスポートストリーム識別 (16bit符号なし
     * ただし、0x0000と0xffffは使用禁止。)
     * @param original_network_id オリジナルネットワーク識別 (16bit符号なし)
     * @param service_id サービス識別 (16bit符号なし)
     * @throws IllegalArgumentException
     *  各フィールドに不正な値がセットされたとき。
     */
    public KeyFields(int transport_stream_id, int original_network_id, int service_id) throws IllegalArgumentException {
        this.transport_stream_id = transport_stream_id;
        this.original_network_id = original_network_id;
        this.service_id = service_id;

        String errorFieldName = null;
        ID_CHECK:
        {
            if (!TRANSPORT_STREAM_ID_RANGE.contains(this.transport_stream_id)) {
                errorFieldName = "トランスポートストリーム識別 値　= "+Integer.toHexString(this.transport_stream_id);
                break ID_CHECK;
            }
            if (!TRANSPORT_STREAM_ID_RANGE.contains(this.original_network_id)) {
                errorFieldName = "オリジナルネットワーク識別 値　= "+Integer.toHexString(this.original_network_id);
                break ID_CHECK;
            }
            if (!TRANSPORT_STREAM_ID_RANGE.contains(this.service_id)) {
                errorFieldName = "サービス識別 値　= "+Integer.toHexString(this.service_id);
                break ID_CHECK;
            }
        }
        if (errorFieldName != null) {
            throw new IllegalArgumentException("範囲外の値がセットされました。フィールドと値 = " + errorFieldName);
        }
    }

    public final int getTransport_stream_id() {
        return transport_stream_id;
    }

    public final int getOriginal_network_id() {
        return original_network_id;
    }

    public final  int getService_id() {
        return service_id;
    }

    /**
     * トランスポートストリーム識別、オリジナルネットワーク識別、サービス識別から、Map用の複合キーを生成する。
     *
     * @see java.util.Map
     * @return 複合キー
     */
    public final MultiKey<Integer> getMuiltiKey() {
        return new MultiKey<>(this.getTransport_stream_id(), this.getOriginal_network_id(), this.getService_id());
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(3, 11, this);
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
