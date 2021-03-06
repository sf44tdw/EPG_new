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
package libepg.epg.section.descriptor.servicedescriptor;

import libepg.util.bytearray.ByteConverter;
import java.text.MessageFormat;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.util.Aribstr;
import org.apache.commons.codec.binary.Hex;

/**
 * サービス記述子（Service descriptor） 編成チャンネル名とその事業者名をサービス形式種別とともに文字符号で表す。
 *
 * @author normal
 */
public final class ServiceDescriptor extends Descriptor {

    public ServiceDescriptor(Descriptor discriptor) {
        super(discriptor);
    }

    /**
     * service_type(サービス形式種別):これは8ビットのフィールドで、サービスの種類を表す。 <br>
     *
     * @return サービス形式種別。 <br>
     */
    public synchronized int getService_type() {
        return ByteConverter.byteToInt(this.getDescriptor_Body()[0]);
    }

    /**
     * service_type(サービス形式種別)に対応する定数を表示する。 <br>
     *
     * @return サービス形式種別。 <br>
     */
    public synchronized SERVICE_TYPE getService_type_Enum() {
        return SERVICE_TYPE.reverseLookUp(this.getService_type());
    }

    /**
     * service_provider_name_length(事業者名長):この8 ビットのフィールドは、後続の事業者名のバイト長を表す。
     *
     * @return 上記の値
     */
    public synchronized int getService_provider_name_length() {
        return ByteConverter.byteToInt(this.getDescriptor_Body()[1]);
    }

    /**
     * 事業者名(記述子のデータ部分の3バイト目から記述子長バイト分)
     *
     * @return 上記の値
     */
    public synchronized byte[] getService_provider_name() {
        byte[] t = new byte[this.getService_provider_name_length()];
        if (t.length > 0) {
            System.arraycopy(this.getDescriptor_Body(), 2, t, 0, t.length);
        }
        return t;
    }

    /**
     * 事業者名の文字列
     *
     * @return 変換した文字列
     */
    public synchronized String getService_provider_name_String() {
        byte[] t = this.getService_provider_name();
        return Aribstr.AribToString(t);
    }

    /**
     * service_name_length(サービス名長):この8 ビットのフィールドは、後続のサービス名のバイト長を表す。
     *
     * @return 上記の値
     */
    public synchronized int getService_name_length() {
        //このフィールドの位置=サービス形式種別長+事業者名長フィールドの長さ+事業者名長
        return ByteConverter.byteToInt(this.getDescriptor_Body()[1 + 1 + this.getService_provider_name_length()]);
    }

    /**
     * サービス名
     *
     * @return 上記の値
     */
    public synchronized byte[] getService_name() {
        int l = this.getService_name_length();
        byte[] t = new byte[l];
        //このフィールドの開始位置=サービス形式種別長+事業者名長フィールドの長さ+事業社名長+サービス名長フィールドの長さ
        if (t.length > 0) {
            int pos = 1 + 1 + this.getService_provider_name_length() + 1;
            System.arraycopy(this.getDescriptor_Body(), pos, t, 0, t.length);
        }
        return t;
    }

    /**
     * サービス名の文字列
     *
     * @return 変換した文字列
     */
    public synchronized String getService_name_String() {
        byte[] t;
        t = this.getService_name();
        return Aribstr.AribToString(t);
    }

    private static final String TITLE = "サービス記述子";
    private static final MessageFormat SD_DESC = new MessageFormat(
            TITLE + " {0}\n"
            + TITLE + " サービス形式種別 = {1}\n"
            + TITLE + " サービス形式種別定数 = {2}\n"
            + TITLE + " 事業者名長 = {3}\n"
            + TITLE + " 事業者名 = {4}\n"
            + TITLE + " 事業者名文字列 = {5}\n"
            + TITLE + " サービス名長 = {6}\n"
            + TITLE + " サービス名 = {7}\n"
            + TITLE + " サービス名文字列 = {8}\n"
    );

    @Override
    public String toString() {
        Object[] parameters = {super.toString(),
            Integer.toHexString(this.getService_type()),
            this.getService_type_Enum(),
            this.getService_provider_name_length(),
            Hex.encodeHexString(this.getService_provider_name()),
            this.getService_provider_name_String(),
            this.getService_name_length(),
            Hex.encodeHexString(this.getService_name()),
            this.getService_name_String()};
        return SD_DESC.format(parameters);
    }
}
