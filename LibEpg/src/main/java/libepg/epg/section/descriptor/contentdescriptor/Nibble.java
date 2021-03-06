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
package libepg.epg.section.descriptor.contentdescriptor;

import libepg.util.bytearray.ByteConverter;
import java.text.MessageFormat;
import java.util.Objects;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.codec.binary.Hex;

/**
 * ジャンルコード(2バイト)
 */
public final class Nibble {

    private final ByteDataBlock data;

    Nibble(byte[] data) {
        this.data = new ByteDataBlock(data);
        int lengthFromData = this.data.getData().length;
        if (lengthFromData != 2) {
            MessageFormat msg1 = new MessageFormat("渡されたデータの長さが想定と異なります。データから算出した配列長={0} 想定される配列長={1}");
            Object[] parameters1 = {lengthFromData, 2};
            throw new IllegalArgumentException(msg1.format(parameters1));

        }
    }

    /**
     * バイト長配列を取得する。
     *
     * @return 参照。
     */
    public synchronized byte[] getData() {
        return data.getData();
    }

    /**
     * content_nibble_level_1（ジャンル1）：この4 ビットのフィールドは、コンテント識別の
     * 第一段階分類を表す。符号化については別途規定する。（付録H 参照）
     *
     * @return 上記の値
     * @throws IllegalStateException 当てはまるジャンルが見つからないとき。
     */
    public synchronized NIBBLE_LEVEL_1 getContent_nibble_level_1() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[0]);
        temp = temp >>> 4;
        NIBBLE_LEVEL_1 n1 = NIBBLE_LEVEL_1.reverseLookUp(temp);
        if (n1 == null) {
            throw new IllegalStateException("ジャンルが見つかりません  検索値 = " + Integer.toHexString(temp) + " バイト列 = " + this.data);
        }
        return n1;
    }

    /**
     * content_nibble_level_2（ジャンル2）：この4 ビットのフィールドは、コンテント識別の
     * 第二段階分類を表す。符号化については別途規定する。（付録H 参照）
     *
     * @return 上記の値
     * @throws IllegalStateException 当てはまるジャンルが見つからないとき。
     */
    public synchronized NIBBLE_LEVEL_2 getContent_nibble_level_2() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[0]);
        temp = temp & 0xf;
        NIBBLE_LEVEL_1 n1 = this.getContent_nibble_level_1();
        NIBBLE_LEVEL_2 n2 = NIBBLE_LEVEL_2.reverseLookUp(n1.getCode(), temp);
        if (n2 == null) {
            throw new IllegalStateException("ジャンルが見つかりません 上位ジャンル = " + n1 + "\n検索値 = " + Integer.toHexString(temp) + "\nバイト列 = " + this.data);
        }
        return n2;
    }

    /**
     * user_nibble（ユーザジャンル）：この4 ビットのフィールドは、放送事業者により定義される。
     *
     * @return 上記の値
     */
    public synchronized int getUser_nibble1() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[1]);
        temp = temp >>> 4;
        return temp;
    }

    /**
     * user_nibble（ユーザジャンル）：この4 ビットのフィールドは、放送事業者により定義される。
     *
     * @return 上記の値
     */
    public synchronized int getUser_nibble2() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[1]);
        temp = temp & 0xf;
        return temp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Nibble other = (Nibble) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    private static final String TITLE = "ジャンルコード";
    private static final MessageFormat NIBBLE_DESC = new MessageFormat(
            TITLE + " バイト列 = {0}\n"
            + TITLE + " ジャンル1 = {1}\n"
            + TITLE + " ジャンル2 = {2}\n"
            + TITLE + " ユーザジャンル1 = {3}\n"
            + TITLE + " ユーザジャンル2 = {4}\n"
    );

    @Override
    public String toString() {
        Object[] parameters = {this.data, this.getContent_nibble_level_1(), this.getContent_nibble_level_2(), this.getUser_nibble1(), this.getUser_nibble2()};
        return NIBBLE_DESC.format(parameters);
    }

}
