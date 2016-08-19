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

import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * EPGデータの保持オブジェクトのひな型。トランスポートストリーム識別、オリジナルネットワーク識別、サービス識別の各フィールドの保持と、それらからのキーの生成を強制する。
 * KeyFieldsのラッパー
 *
 * @author normal
 */
public class DataObject {

    protected static String replaceNull(String src) {
        if (src == null) {
            return "";
        } else {
            return src;
        }
    }

    private final KeyFields keyfFields;

    public DataObject(int transport_stream_id, int original_network_id, int service_id) throws IllegalArgumentException {
        this.keyfFields = new KeyFields(transport_stream_id, original_network_id, service_id);
    }

    /**
     * @return トランスポートストリーム識別
     */
    public final int getTransport_stream_id() {
        return this.keyfFields.getTransport_stream_id();
    }

    /**
     * @return オリジナルネットワーク識別
     */
    public final int getOriginal_network_id() {
        return this.keyfFields.getOriginal_network_id();
    }

    /**
     * @return サービス識別
     */
    public final int getService_id() {
        return this.keyfFields.getService_id();
    }

    public KeyFields getKeyfFields() {
        return keyfFields;
    }

    /**
     * 複合キー生成用オブジェクトを利用して、トランスポートストリーム識別、オリジナルネットワーク識別、サービス識別から、Map用の複合キーを生成する。
     *
     * @see java.util.Map
     * @return 複合キー
     */
    public final MultiKey<Integer> getMuiltiKey() {
        return this.keyfFields.getMuiltiKey();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(5, 67, this);
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
