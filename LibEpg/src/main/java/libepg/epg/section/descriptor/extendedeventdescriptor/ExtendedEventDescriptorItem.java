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
package libepg.epg.section.descriptor.extendedeventdescriptor;

import libepg.util.bytearray.ByteConverter;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.Objects;
import libepg.epg.section.Section;
import libepg.epg.util.Aribstr;
import epgtools.loggerfactory.LoggerFactory;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import libepg.util.bytearray.ByteDataBlock;

/**
 * 拡張形式イベント記述子の項目
 */
public class ExtendedEventDescriptorItem {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Section.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final ByteDataBlock data;

    public ExtendedEventDescriptorItem(byte[] data) {
        this.data = new ByteDataBlock(data);
    }

    /**
     * バイト長配列を取得する。
     *
     * @return 参照。
     */
    public final synchronized byte[] getData() {
        return data.getData();
    }

    /**
     * item_description_length(項目名長):これは8 ビットのフィールドで、項目名のバイト長を示す。
     *
     * @return 上記の値
     */
    public final synchronized int getItem_description_length() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[0]);
        return temp;
    }

    /**
     * item_description_char(項目名(文字符号)):これは8ビットのフィールドで、<br>
     * 一連の項目名のフィールドは、項目名の文字記述を規定する。 文字情報の符号化に関しては、付録A を参照。
     *
     * @return 上記の値
     */
    public final synchronized byte[] getiItem_description() {
        byte[] t = new byte[this.getItem_description_length()];
        if (t.length > 0) {
            System.arraycopy(this.getData(), 1, t, 0, t.length);
        }
        return t;
    }

    /**
     * item_description_char(項目名(文字符号))を変換した文字列。
     *
     * @return 上記の値
     */
    public final synchronized String getiItem_description_String() {
        return Aribstr.AribToString(this.getiItem_description());
    }

    /**
     * item_length(項目記述長):これは8 ビットのフィールドで、項目記述のバイト長を示す。
     *
     * @return 上記の値
     */
    public final synchronized int getItem_length() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[0 + this.getItem_description_length() + 1]);
        return temp;
    }

    /**
     * item_char(項目記述(文字符号)):これは8 ビットのフィールドで、一連の項目記述のフィールドは、項目の文字記述を規定する。
     * 文字情報の符号化に関しては、付録A を参照。
     *
     * @return 上記の値
     */
    public final synchronized byte[] getItem() {
        byte[] t = new byte[this.getItem_length()];
        if (t.length > 0) {
            System.arraycopy(this.getData(), 0 + this.getItem_description_length() + 1 + 1, t, 0, t.length);
        }
        return t;
    }

    /**
     * item_char(項目記述(文字符号))を変換した文字列。
     *
     * @return 上記の値
     */
    public final synchronized String getItem_String() {
        return Aribstr.AribToString(this.getItem());
    }

    /**
     * 項目名と項目を文字列に連結して返す。
     *
     * @return 項目名 項目の内容。
     */
    public final synchronized String getStoredString() {
        return this.getItem_description_length() + "  " + this.getItem_String();
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 41 * hash + Objects.hashCode(this.data);
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
        final ExtendedEventDescriptorItem other = (ExtendedEventDescriptorItem) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    private static final MessageFormat ITEM_DESC = new MessageFormat(
            "拡張形式イベント記述子の項目 バイト列 = {0}\n"
            + "拡張形式イベント記述子の項目 項目名長 = {1}\n"
            + "拡張形式イベント記述子の項目 項目名 = {2}\n"
            + "拡張形式イベント記述子の項目 項目名(テキスト) = {3}\n"
            + "拡張形式イベント記述子の項目 項目記述長 = {4}\n"
            + "拡張形式イベント記述子の項目 項目記述 = {5}\n"
            + "拡張形式イベント記述子の項目 項目記述(テキスト) = {6}\n"
    );

    @Override
    public String toString() {
        Object[] parameters = {
            this.data,
            this.getItem_description_length(),
            Hex.encodeHexString(this.getiItem_description()),
            this.getiItem_description_String(),
            this.getItem_length(),
            Hex.encodeHexString(this.getItem()),
            this.getItem_String()};
        return ITEM_DESC.format(parameters);
    }

}
