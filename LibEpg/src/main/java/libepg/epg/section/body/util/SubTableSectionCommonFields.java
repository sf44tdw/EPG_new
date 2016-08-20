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
package libepg.epg.section.body.util;

import libepg.epg.section.body.SectionBody;
import libepg.util.bytearray.ByteConverter;

/**
 * セクション本体の最初(セクションのセクション長フィールド直後)から下記のように続くフィールドを取得するためのクラス。<br>
 * 16 uimsbf<br>
 * 2 bslbf<br>
 * 5 uimsbf<br>
 * 1 bslbf<br>
 * 8 uimsbf<br>
 * 8 uimsbf<br>
 *
 * @author normal
 */
public final class SubTableSectionCommonFields {

    private final SectionBody source;

    public SubTableSectionCommonFields(SectionBody source) {
        this.source = source;
    }

    private byte[] getData() {
        return this.source.getData();
    }

    /**
     * 1個めの16ビット
     *
     * @return 上記の値
     */
    public final synchronized int getFirst16Bit() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 0, t, 0, t.length);
        int temp = ByteConverter.bytesToInt(t);
        return temp;
    }

    /**
     * 2個めの2ビット
     *
     * @return 3
     */
    public final synchronized int getSecond2Bit() {
        byte t = this.getData()[2];
        int temp = ByteConverter.byteToInt(t);
        temp = temp >>> 6;
        return temp;
    }

    /**
     * 3個目の5ビット
     *
     * @return 上記の値
     */
    public final synchronized int getTherd5Bit() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[2]);
        temp = temp & 0x3E;
        temp = temp >>> 1;
        return temp;
    }

    /**
     * 4個めの1ビット
     *
     * @return 上記の値
     */
    public final synchronized int getFourth1Bit() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[2]);
        temp = temp & 0x1;
        return temp;
    }

    /**
     * 5個めの8ビット
     *
     * @return 上記の値
     */
    public final synchronized int getFifth8Bit() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[3]);
        return temp;
    }

    /**
     * 6個めの8ビット
     *
     * @return 上記の値
     */
    public final synchronized int getSixth8Bit() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[4]);
        return temp;
    }
}
