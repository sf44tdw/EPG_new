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
package libepg.ts.packet;

import libepg.util.bytearray.ByteConverter;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import loggingsupport.loggerfactory.LoggerFactory;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * TSパケットのバイト列からパケットの内容を取得するクラス。
 */
public class TsPacket {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TsPacket.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    /**
     * 同期ワード(TSパケットの最初の1バイトを示す)
     */
    public static final int TS_SYNC_BYTE = 0x47;

    private static final Range<Integer> ZERO_TO_ONE = Range.between(0x0000, 0x0001);

    /**
     * トランスポートエラーインジケータの取りうる値の範囲。
     */
    public static final Range<Integer> TEI_RANGE = ZERO_TO_ONE;

    /**
     * トランスポートエラーインジケータの取りうる値の範囲。
     */
    public static final Range<Integer> PES_RANGE = ZERO_TO_ONE;

    /**
     * トランスポート優先度の取りうる値の範囲。
     */
    public static final Range<Integer> TP_RANGE = ZERO_TO_ONE;

    /**
     * PIDの取りうる値の範囲。
     */
    public static final Range<Integer> PID_RANGE = Range.between(0x0000, 0x1FFF);

    private static final Range<Integer> ZERO_TO_THREE = Range.between(0, 3);

    /**
     * トランスポートスクランブルコントロールの取りうる値の範囲。
     */
    public static final Range<Integer> TSC_RANGE = ZERO_TO_THREE;

    /**
     * アダプテーションフィールドコントロールの取りうる値の範囲。
     */
    public static final Range<Integer> AFC_RANGE = ZERO_TO_THREE;

    /**
     * 巡回カウンターの取りうる値の範囲。
     */
    public static final Range<Integer> CC_RANGE = Range.between(0, 15);

    /**
     * アダプテーションフィールド長の取りうる値の範囲。
     */
    public static final Range<Integer> AFL_RANGE = Range.between(0, 183);

    /**
     * ペイロード長の取りうる値の範囲。
     */
    public static final Range<Integer> P_RANGE = Range.between(0, 184);

    /**
     * TSパケットで使うバイト長
     */
    public static enum TS_PACKET_BYTE_LENGTH {
        /**
         * TSパケット長
         */
        PACKET_LENGTH(188),
        /**
         * TSパケットヘッダ長
         */
        HEADER_LENGTH(4);
        private final int byteLength;

        private TS_PACKET_BYTE_LENGTH(int length) {
            this.byteLength = length;
        }

        /**
         * @return TSパケットで使うバイト長
         */
        public int getByteLength() {
            return byteLength;
        }
    }

    private final ByteDataBlock data;

    /**
     * TSパケットを受け取る。
     *
     * @param data TSパケットのデータ
     * @throws IllegalArgumentException
     * 渡されたバイト列の最初が動機ワードではない場合か、パケット長が188バイトではない場合。
     */
    public TsPacket(final byte[] data) throws IllegalArgumentException {
        byte[] temp = Arrays.copyOf(data, data.length);

        PRE_CHECK:
        {
            MessageFormat fmt = new MessageFormat("異常なパケットです。メッセージ={0} バイト列={1}");
            String msg;
            String hexDump;
            int syncbyte = ByteConverter.byteToInt(temp[0]);
            if (syncbyte != TsPacket.TS_SYNC_BYTE) {
                MessageFormat fmt_sync = new MessageFormat("同期ワードが不正です。想定={0} 検出={1}");
                Object[] parameters_sync = {Integer.toHexString(TsPacket.TS_SYNC_BYTE), Integer.toHexString(syncbyte)};
                msg = fmt_sync.format(parameters_sync);
            } else if (temp.length != TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength()) {
                MessageFormat fmt_length = new MessageFormat("バイト列の長さがパケット長と一致しません。想定={0} 検出={1}");
                Object[] parameters_length = {Integer.toHexString(TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength()), Integer.toHexString(temp.length)};
                msg = fmt_length.format(parameters_length);
            } else {
                break PRE_CHECK;
            }
            hexDump = Hex.encodeHexString(temp);
            Object[] parameters2 = {msg, hexDump};
            throw new IllegalArgumentException(fmt.format(parameters2));
        }
        this.data = new ByteDataBlock(temp);
    }

    /**
     * バイト列を取得する。
     *
     * @return バイト列。
     */
    public synchronized byte[] getData() {
        return data.getData();
    }

    /**
     * sync_byte(同期ワード)
     *
     * @return 上記の値
     * @throws IllegalStateException ありえない値(0x47以外)を取得した場合。
     */
    public synchronized int getSync_byte() throws IllegalStateException {
        int temp = ByteConverter.byteToInt(this.data.getData()[0]);
        if (temp == TsPacket.TS_SYNC_BYTE) {
            return temp;
        } else {
            MessageFormat msg = new MessageFormat("同期ワードが不正です。値={0}");
            Object[] parameters = {Integer.toHexString(temp)};
            throw new IllegalStateException(msg.format(parameters));
        }
    }

    /**
     * transport_error_indicator(トランスポートエラーインジケータ)
     * 1’の場合、少なくとも1ビットの訂正不可能なエラーがTSパケットに存在することを示す。
     *
     * @return トランスポートエラーインジケータ。
     * @throws IllegalStateException ありえない値(0,1以外)を取得した場合。
     */
    public synchronized int getTransport_error_indicator() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[1]);
        temp = temp >>> 7;
        if (TEI_RANGE.contains(temp)) {
            return temp;
        } else {
            MessageFormat msg = new MessageFormat("トランスポートエラーインジケータが不正です。値={0}");
            Object[] parameters = {temp};
            throw new IllegalStateException(msg.format(parameters));
        }
    }

    /**
     * ペイロードユニットスタートインジケータの値
     */
    public static enum PAYLOAD_UNIT_START_INDICATOR {
        /**
         * ペイロードユニットスタートインジケータの値=1<br>
         * このパケットのペイロードがPESの先頭であるか、PISのセクションの先端を含む<br>
         */
        START_PES_OR_START_SECTION(1),
        /**
         * ペイロードユニットスタートインジケータの値=0 <br>
         * このパケットのペイロードがPESの先頭ではないし、PISのセクションの先端も含まない<br>
         */
        NOT_START_POINT(0);

        private final int value;

        private PAYLOAD_UNIT_START_INDICATOR(int value) {
            this.value = value;
        }

        /**
         * @return ペイロードユニットスタートインジケータの値
         */
        public int getValue() {
            return value;
        }

    }

    /**
     * payload_unit_start_indicator(ペイロードユニットスタートインジケータ)<br>
     * 1だとこのパケットのペイロードがPESの先頭であるか、PISのセクションを含むパケットであればペイロードの先頭バイトがセクションの先頭位置を示す(ポインタフィールド)。<br>
     * ポインタフィールドが0の場合、ポインタフィールドの直後が少なくとも1つのセクションの先頭になる。<br>
     * セクションの先頭バイト(テーブルID)は0xFFにならない。<br>
     *
     * @return ペイロードユニットスタートインジケータの値に基づく定数。
     * @throws IllegalStateException ありえない値(0,1以外)を取得した場合。
     */
    public synchronized PAYLOAD_UNIT_START_INDICATOR getPayload_unit_start_indicator() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[1]);
        temp = temp & 0x40;
        temp = temp >>> 6;
        if (!PES_RANGE.contains(temp)) {
            MessageFormat msg = new MessageFormat("ペイロードユニットスタートインジケータが不正な値です。値={0}");
            Object[] parameters = {temp};
            throw new IllegalStateException(msg.format(parameters));
        }
        switch (temp) {
            case 0:
                return TsPacket.PAYLOAD_UNIT_START_INDICATOR.NOT_START_POINT;
            case 1:
                return TsPacket.PAYLOAD_UNIT_START_INDICATOR.START_PES_OR_START_SECTION;
        }
        return null;
    }

    /**
     * transport_priority(トランスポート優先度) 同一のPIDを持つパケットの中での優先度を示すフラグで、‘1’が優先を示す
     *
     * @return 上記の値
     * @throws IllegalStateException ありえない値(0,1以外)を取得した場合。
     */
    public synchronized int getTransport_priority() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[1]);
        temp = temp & 0x20;
        temp = temp >>> 5;
        if ((temp == 0) || (temp == 1)) {
            return temp;
        } else {
            MessageFormat msg = new MessageFormat("トランスポート優先度が不正な値です。値={0}");
            Object[] parameters = {temp};
            throw new IllegalStateException(msg.format(parameters));
        }
    }

    /**
     * PID(プログラムID)
     *
     * @return 上記の値
     * @throws IllegalStateException ありえない値(0x0000以上0x1FFF以下の値以外)を取得した場合。
     */
    public synchronized int getPid() throws IllegalStateException {
        byte[] t = new byte[2];
        System.arraycopy(this.data.getData(), 1, t, 0, t.length);
        int temp = ByteConverter.bytesToInt(t);
        temp = temp & 0x1FFF;
        if (PID_RANGE.contains(temp)) {
            return temp;
        } else {
            MessageFormat msg = new MessageFormat("PID(プログラムID)が不正な値です。値={0}");
            Object[] parameters = {temp};
            throw new IllegalStateException(msg.format(parameters));
        }
    }

    /**
     * @return 定数として定義されたpidがあれば、それを返す。無い場合はnull
     */
    public synchronized RESERVED_PROGRAM_ID getPid_Const() {
        return RESERVED_PROGRAM_ID.reverseLookUp(this.getPid());
    }

    /**
     * transport_scrambling_control(トランスポートスクランブルコントロール)
     *
     * @return 上記の値
     * @throws IllegalStateException ありえない値(0以上3以下の整数以外)のとき
     */
    public synchronized int getTransport_scrambling_control() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[3]);
        temp = temp >>> 6;
        if (!TSC_RANGE.contains(temp)) {
            MessageFormat msg = new MessageFormat("トランスポートスクランブルコントロールが不正な値です。値={0}");
            Object[] parameters = {temp};
            throw new IllegalStateException(msg.format(parameters));
        }
        return temp;
    }

    /**
     * トランスポートスクランブルコントロール
     */
    public static enum TRANSPORT_SCRAMBLING_CONTROL {
        /**
         * スクランブルなし。
         */
        NOT_SCRAMBLED(0),
        /**
         * リザーブ。
         */
        RESERVED(1),
        /**
         * 偶数鍵スクランブル
         */
        SCRAMBLED_BY_EVEN_KEY(2),
        /**
         * 奇数鍵スクランブル
         */
        SCRAMBLED_BY_ODD_KEY(3);
        private final int value;

        private TRANSPORT_SCRAMBLING_CONTROL(int value) {
            this.value = value;
        }

        /**
         * @return トランスポートスクランブルコントロールの値
         */
        public int getValue() {
            return value;
        }

    }

    /**
     * transport_scrambling_control(トランスポートスクランブルコントロール)の定数。無い場合はnull
     *
     * @return 上記の値
     * @throws IllegalStateException ありえない値(0以上3以下の整数以外)のとき
     */
    public synchronized TRANSPORT_SCRAMBLING_CONTROL getTransport_scrambling_control_Const() throws IllegalStateException {
        switch (this.getTransport_scrambling_control()) {
            case 0:
                return TsPacket.TRANSPORT_SCRAMBLING_CONTROL.NOT_SCRAMBLED;
            case 1:
                return TsPacket.TRANSPORT_SCRAMBLING_CONTROL.RESERVED;
            case 2:
                return TsPacket.TRANSPORT_SCRAMBLING_CONTROL.SCRAMBLED_BY_EVEN_KEY;
            case 3:
                return TsPacket.TRANSPORT_SCRAMBLING_CONTROL.SCRAMBLED_BY_ODD_KEY;
        }
        return null;
    }

    /**
     * アダプテーションフィールドコントロールの定義
     */
    public static enum ADAPTATION_FIELD_CONTROL {

        /**
         * 0:定義されていない。
         */
        RESERVED(0),
        /**
         * 1:このパケットにはペイロードのみ。
         */
        ONLY_PAYLOAD(1),
        /**
         * 2:このパケットにはアダプテーションフィールドのみ。 アダプテーションフィールドコントロールの定義
         */
        ONLY_ADAPTATION_FIELD(2),
        /**
         * 3:両方ある。(アダプテーションフィールドの次にペイロード)
         */
        BOTH_EXIST(3);

        private final int fieldControl;

        private ADAPTATION_FIELD_CONTROL(int fieldControl) {
            this.fieldControl = fieldControl;
        }

        /**
         * Get the value of fieldControl
         *
         * @return the value of fieldControl
         */
        public int getFieldControl() {
            return fieldControl;
        }

    }

    /**
     * adaptation_field_control(アダプテーションフィールドコントロール)
     *
     * @return アダプテーションフィールドコントロールの値に基づく定数。
     * @throws IllegalStateException ありえない値(0,1,2,3以外)を取得した場合。
     */
    public synchronized ADAPTATION_FIELD_CONTROL getAdaptation_field_control() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[3]);
        temp = temp & 0x30;
        temp = temp >>> 4;

        if (!AFC_RANGE.contains(temp)) {
            MessageFormat msg = new MessageFormat("アダプテーションフィールドコントロールが不正な値です。値={0}");
            Object[] parameters = {temp};
            throw new IllegalStateException(msg.format(parameters));
        }

        switch (temp) {
            case 0:
                return TsPacket.ADAPTATION_FIELD_CONTROL.RESERVED;
            case 1:
                return TsPacket.ADAPTATION_FIELD_CONTROL.ONLY_PAYLOAD;
            case 2:
                return TsPacket.ADAPTATION_FIELD_CONTROL.ONLY_ADAPTATION_FIELD;
            case 3:
                return TsPacket.ADAPTATION_FIELD_CONTROL.BOTH_EXIST;
        }
        return null;
    }

    /**
     * continuity_control(巡回カウンター) MPEG-2 TSパケットに欠落が無いか検査する為のカウンター。<br>
     * 0～15の値を取り、MPEG-2 TSパケット毎に1ずつ増えていく。 <br>
     * ただしアダプテーションフィールドのみのペイロードが存在しない MPEG-2 TSパケットの場合はこのカウンターは常に0となる。<br>
     * また、TSパケットが同じ内容となる「連送パケット」の場合、巡回カウンターは増加せず、前回と同じカウンター値となる。<br>
     * 連送パケットは2回まで許可されている。<br>
     *
     * @return 巡回カウンターの値
     * @throws IllegalStateException ありえない値(0以上15以下の整数以外)を取得した場合。
     */
    public synchronized int getContinuity_counter() throws IllegalStateException {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[3]);
        temp = temp & 0x0F;
        if (CC_RANGE.contains(temp)) {
            return temp;
        } else {
            MessageFormat msg = new MessageFormat("巡回カウンターが不正な値です。値={0}");
            Object[] parameters = {temp};
            throw new IllegalStateException(msg.format(parameters));
        }
    }

    /**
     * アダプテーションフィールドがあれば、フィールド長の直後からのアダプテーションフィールドの長さ(バイト)を返す。<br>
     *
     * @return フィールド長。フィールドがない場合は0。
     * @throws IllegalStateException ありえない値(0以上183以下の整数以外)を取得した場合。
     *
     */
    private int getAdaptation_length() throws IllegalStateException {
        switch (this.getAdaptation_field_control()) {
            case ONLY_ADAPTATION_FIELD:
            case BOTH_EXIST:
                //フィールド長を取得 
                int temp = ByteConverter.byteToInt(this.data.getData()[4]);
//                LOG.debug(temp);
//                LOG.debug(TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength() - TsPacket.TS_PACKET_BYTE_LENGTH.HEADER_LENGTH.getByteLength());
//                LOG.debug(TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength() - temp);
                if ((temp < (TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength() - TsPacket.TS_PACKET_BYTE_LENGTH.HEADER_LENGTH.getByteLength()))
                        && (TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength() - temp) > 6) {
                } else {
                    //理由がわからないがこうしないと正しい長さにならない。
                    //異常に大きな値がセットされているケースがあるが、その場合は8で割るらしい。(FFばかり続くので、パディングの類。)
                    temp = temp / 8;
                }
                if (!AFL_RANGE.contains(temp)) {
                    MessageFormat msg = new MessageFormat("アダプテーションフィールド長が不正な値です。値={0}");
                    Object[] parameters = {temp};
                    throw new IllegalStateException(msg.format(parameters));
                }
                return temp;
            default:
                return 0;
        }

    }

    /**
     * アダプテーションフィールド (あれば)パケットの5バイト目から始まっている可変長フィールド。<br>
     * 5バイト目がフィールド長(バイト)、6バイト目がフラグ郡。それ以降がフィールド本体。<br>
     *
     * @return アダプテーションフィールド。 ない場合はbyte[] ={0};
     */
    public synchronized byte[] getAdaptation_field() {

        byte dummy[] = {0};

        int field_length = this.getAdaptation_length();
        if (field_length > 0) {
            //フィールド長項目を含めてコピーする。
            field_length++;
            byte[] buf = new byte[field_length];
            System.arraycopy(this.data.getData(), 4, buf, 0, buf.length);
            return buf;
        } else {
            return dummy;
        }

    }

    /**
     * ペイロード長を取得する。ペイロード無しの場合は0を返す。
     * @return ペイロード長
     * @throws IllegalStateException ありえない値(0以上184以下の整数以外)を取得した場合。
     */
    private int getPayload_length()  throws IllegalStateException{

        //ペイロードなしのケースでは0を返す。
        if (getAdaptation_field_control() == TsPacket.ADAPTATION_FIELD_CONTROL.ONLY_ADAPTATION_FIELD) {
            return 0;
        } else {
            int length = 0;
            switch (this.getAdaptation_field_control()) {
                case ONLY_PAYLOAD:
                    //ペイロードのみ
                    length = this.data.length() - TsPacket.TS_PACKET_BYTE_LENGTH.HEADER_LENGTH.getByteLength();
                    break;
                case BOTH_EXIST:
                    //アダプテーションの後にペイロード
                    length = this.data.length() - TsPacket.TS_PACKET_BYTE_LENGTH.HEADER_LENGTH.getByteLength() - this.getAdaptation_length();
                    break;
            }
            if (!P_RANGE.contains(length)) {
                MessageFormat msg = new MessageFormat("ペイロード長が不正な値です。値={0}");
                Object[] parameters = {length};
                throw new IllegalStateException(msg.format(parameters));
            }
            return length;
        }
    }

    /**
     *
     * @return ペイロード 取得できないときはbyte [] ={0};
     */
    public synchronized byte[] getPayload() {

        byte dummy[] = {0};

        int field_length = this.getPayload_length();
        if (field_length > 0) {
            byte[] buf = new byte[field_length];
            switch (this.getAdaptation_field_control()) {
                case ONLY_PAYLOAD:
                    //ペイロードのみ
                    System.arraycopy(this.data.getData(), 4, buf, 0, buf.length);
                    break;
                case BOTH_EXIST:
                    //アダプテーションの後にペイロード
                    System.arraycopy(this.data.getData(), 4 + this.getAdaptation_length(), buf, 0, buf.length);
                    break;
            }
            return buf;
        } else {
            return dummy;
        }

    }

    private static final String TITLE = "パケット";
    private static final MessageFormat PACKET_DESC = new MessageFormat(
            TITLE + " 内容 = {0}\n"
            + TITLE + " 同期ワード = {1}\n"
            + TITLE + " トランスポートエラーインジケータ = {2}\n"
            + TITLE + " ペイロードユニットスタートインジケータ = {3}\n"
            + TITLE + " トランスポート優先度 = {4}\n"
            + TITLE + " プログラムID = {5}\n"
            + TITLE + " プログラムID(定数) = {6}\n"
            + TITLE + " トランスポートスクランブルコントロール = {7}\n"
            + TITLE + " トランスポートスクランブルコントロール(定数) = {8}\n"
            + TITLE + " アダプテーションフィールドコントロール = {9}\n"
            + TITLE + " 巡回カウンター = {10}\n"
            + TITLE + " アダプテーションフィールド長 = {11}\n"
            + TITLE + " アダプテーションフィールド = {12}\n"
            + TITLE + " ペイロード = {13}\n"
            + TITLE + " ハッシュ = {14}\n"
    );

    /**
     * ヘッダの解析結果とパケットのダンプを返す。
     *
     * @return このパケットの内容
     */
    @Override
    public String toString() {
        Object[] parameters = {this.data.toString(),
            Integer.toHexString(this.getSync_byte()),
            this.getTransport_error_indicator(),
            this.getPayload_unit_start_indicator(),
            this.getTransport_priority(),
            Integer.toHexString(this.getPid()),
            this.getPid_Const(),
            this.getTransport_scrambling_control(),
            this.getTransport_scrambling_control_Const(),
            this.getAdaptation_field_control(),
            this.getContinuity_counter(),
            this.getAdaptation_length(),
            Hex.encodeHexString(this.getAdaptation_field()),
            Hex.encodeHexString(this.getPayload()),
            this.hashCode()};
        return PACKET_DESC.format(parameters);
    }

    /**
     * @return
     * 比較対象のオブジェクトがこのオブジェクトと同じクラスのインスタンスで、比較対象のオブジェクトが保持しているバイト配列と、このオブジェクトが保持しているバイト配列が同じ内容であればtrue
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 79, this);
    }

}
