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
package libepg.epg.section;

import libepg.util.bytearray.ByteConverter;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.Arrays;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import loggingsupport.loggerfactory.LoggerFactory;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
//sub_table［サブテーブル］：同一のテーブル識別（table_id）および下記を持つセクショ
//ンの集まり。
//NITの場合 ： 同一のテーブル識別拡張（network_id）とバージョン番号
//BATの場合 ： 同一のテーブル識別拡張（bouquet_id）とバージョン番号
//SDTの場合 ： 同一のテーブル識別拡張（transport_stream_id）、
//同一のオリジナルネットワーク識別およびバージョン番号
//EITの場合 ： 同一のテーブル識別拡張（service_id）、
//同一のトランスポートストリーム識別、
//同一のオリジナルネットワーク識別およびバージョン番号
//テーブル識別拡張（table_id_extension）フィールドは、セクションシンタクス指示
//（section_syntax_indicator）の値が「1」にセットされるとき、セクションの第4 および
//第5 バイト目になる。
//table［テーブル］：同一のテーブル識別（table_id）を持つ複数のサブテーブルから構成
//される。

/**
 * 各種セクションの共通メソッド
 *
 * @author normal
 */
public final class Section {

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
    private final TABLE_ID tableId;

    /**
     * セクションを受け取る。渡されたデータがテーブルID定数に記載されている最大セクション長+3バイトより長い場合、超過した部分については切り詰める。
     *
     * @param data セクションのデータ
     * @throws IllegalArgumentException
     * 1:渡された配列の配列長が、テーブルID定数に設定された最大セクション長に3バイトを加えた値より大きい場合。<br>
     * 2:渡された配列の配列長が、その配列から取得したセクション長に3バイトを加えた値より小さい場合。<br>
     * 3:セクション長フィールドの先頭2ビットが0ではない場合<br>
     * 4:受け取ったデータのテーブル識別値がテーブルID定数に含まれていない場合。<br>
     */
    public Section(byte[] data) throws IllegalArgumentException {
        byte[] temp = Arrays.copyOf(data, data.length);
        final int id = ByteConverter.byteToInt(temp[0]);
        tableId = TABLE_ID.reverseLookUp(id);

        String hexValue = Hex.encodeHexString(data);

        //テーブルID定数は、テーブルIDの取りうる値すべてを網羅しているので、テーブルID定数に問題がない限りはこのエラーは発生しない。(たいていのテーブルIDはその他の扱いになっている。)
        if (tableId == null) {
            MessageFormat msg2 = new MessageFormat("テーブル識別値が見つかりません。 渡された配列={0}");
            Object[] parameters2 = {hexValue};
            throw new IllegalArgumentException(msg2.format(parameters2));
        }

        byte[] t = new byte[2];
        System.arraycopy(temp, 1, t, 0, t.length);
        int sectionLength = ByteConverter.bytesToInt(t);

        if (LOG.isTraceEnabled()) {
            LOG.trace("セクション長フィールドを含むバイト列 = " + Integer.toHexString(sectionLength));
        }

        sectionLength = sectionLength & 0x0FFF;

        if (LOG.isTraceEnabled()) {
            LOG.trace("セクション長フィールドのバイト列 = " + Integer.toHexString(sectionLength));
        }

        //セクション長フィールドの値が指定された値以上になった場合。
        if (sectionLength > tableId.getMaxSectionLength().getMaxSectionBodyLength()) {
            MessageFormat msg3 = new MessageFormat("セクション長フィールドの値が大きすぎます。セクション長フィールドの最大値={0} セクション長フィールドの値={1} 渡された配列={2}");
            Object[] parameters3 = {tableId.getMaxSectionLength().getMaxSectionBodyLength(), sectionLength, hexValue};
            if (LOG.isTraceEnabled()) {
                LOG.trace(msg3.format(parameters3));
            }
            throw new IllegalArgumentException(msg3.format(parameters3));
        }

        //セクション長フィールドの長さに合わせて渡された配列を切り詰める。
        int targetLength = Integer.MIN_VALUE;
        byte[] sectionByteArray = null;
        try {
            targetLength = sectionLength + 3;
            sectionByteArray = new byte[targetLength];
            if (LOG.isTraceEnabled()) {
                LOG.trace("セクション長フィールドからの全長算出値(targetLength) = " + targetLength);
                LOG.trace("切り詰め前の配列(temp)全長 = " + temp.length);
                LOG.trace("切り詰め前の配列(temp) = " + Hex.encodeHexString(temp));
                LOG.trace("切り詰め先配列全長(sectionByteArray) = " + sectionByteArray.length);
            }
            System.arraycopy(temp, 0, sectionByteArray, 0, sectionByteArray.length);
            if (LOG.isTraceEnabled()) {
                LOG.trace("切り詰め後の配列(sectionByteArray)全長 = " + sectionByteArray.length);
                LOG.trace("切り詰め後の配列(sectionByteArray) = " + Hex.encodeHexString(sectionByteArray));
            }
            this.data = new ByteDataBlock(sectionByteArray);
        } catch (ArrayIndexOutOfBoundsException ex) {
            throw new IllegalArgumentException("配列切り詰め中に問題発生。"
                    + "\nセクション長フィールドからの全長算出値(targetLength) = " + targetLength
                    + "\n切り詰め前の配列(temp)全長 = " + temp.length
                    + "\n切り詰め前の配列 = " + Hex.encodeHexString(temp)
                    + "\n切り詰め後の配列(sectionByteArray)全長 = " + sectionByteArray.length
                    + "\n切り詰め後の配列(sectionByteArray) = " + Hex.encodeHexString(sectionByteArray), ex);
        }
    }

    /**
     * バイト列を取得する。
     *
     * @return 上記の値
     */
    public final synchronized byte[] getData() {
        return data.getData();
    }

    /**
     * @return テーブル識別値(テーブルID)定数
     */
    public final synchronized TABLE_ID getTable_id_const() {
        return tableId;
    }

    /**
     * 8bit。テーブル識別値(テーブルID)
     *
     * @return テーブル識別値(テーブルID)
     *
     */
    public final synchronized int getTable_id() {
        int id = ByteConverter.byteToInt(this.getData()[0]);
        return id;

    }

    /**
     * 1bit。常時1
     *
     * @return 上記の値
     */
    public final synchronized int getSection_syntax_indicator() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[1]);
        temp = temp >>> 7;
        return temp;
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、 その値が将来、ARIB
     * STD-B10が定義する拡張子として使用されるかもしれないことを表す。 1bit
     *
     * @return 1
     */
    public final synchronized int getReservedFutureUse1() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[1]);
        temp = temp & 0x40;
        temp = temp >>> 6;
        return temp;
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、その値が将来ISO で定義される拡張子として使用されるかもしれないことを表す。 2bit
     *
     * @return 3
     */
    public final synchronized int getReserved1() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[1]);
        temp = temp & 0x30;
        temp = temp >>> 4;
        return temp;
    }

    /**
     * 12bitのフィールドで、先頭の2ビットは常に「00」とする。
     * これは、セクション長フィールドの直後からCRCを含むセクションの最後までのセクションのバイト数を規定する。
     * 全セクションの長さが1024バイトもしくは4096バイトを超えないようにするため、セクション長は1021バイトもしくは4093バイトを超えてはならない
     *
     * @return 上記の値
     */
    public final synchronized int getSectionLength() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 1, t, 0, t.length);
        int temp = ByteConverter.bytesToInt(t);
        if (LOG.isTraceEnabled()) {
            LOG.trace("b1 = " + Integer.toHexString(temp));
        }
        temp = temp & 0x0FFF;
        if (LOG.isTraceEnabled()) {
            LOG.trace("b2 = " + Integer.toHexString(temp));
        }
        return temp;
    }

    /**
     * セクション本体(このクラスが管理するバイト配列の4バイト目からCRCを除いたセクションの最後まで)を取得する。
     *
     * @return セクション本体(このクラスが管理するバイト配列の4バイト目からセクション長の最後-4バイト)への参照。<br>
     * セクション本体はテーブルIDに従ってキャストされる。<br>
     * キャスト中に問題が発生したか、キャスト先が基底クラスの場合は基底クラスを返す。<br>
     *
     */
    public final synchronized SectionBody getSectionBody() {
        int sectionlength;
        sectionlength = this.getSectionLength() - 4;

        if (LOG.isTraceEnabled()) {
            LOG.trace("セクション長=" + sectionlength);
            LOG.trace("クラス管理下のバイト配列長-4=" + (this.getData().length - 4));
        }
        byte[] t = new byte[sectionlength];

        System.arraycopy(
                this.getData(), 3, t, 0, sectionlength);

        SectionBody temp = new SectionBody(this.getTable_id_const(), t);

        try {
            Object[] args = {temp};
            Class<?>[] params = {SectionBody.class};
            Constructor<? extends SectionBody> constructor = this.tableId.getDataType().getDeclaredConstructor(params);
            SectionBody target = constructor.newInstance(args);
            return target;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
            LOG.fatal("キャスト中に問題が発生しました。問題の発生したセクション = " + this.data, ex);
            return temp;
        }
    }

    /**
     * テーブル識別拡張（table_id_extension）16bitのフィールドで、セクションシンタクス指示（section_syntax_indicator）の値が「1」にセットされるとき、<br>
     * セクションの第4 および第5バイト目になる。<br>
     *
     * @return セクションの第4 および第5 バイト目
     * @throws IllegalStateException
     * セクションシンタクス指示（section_syntax_indicator）の値が1以外の時。
     */
    public final synchronized int getTable_id_extension() throws IllegalStateException {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 3, t, 0, t.length);
        int temp = ByteConverter.bytesToInt(t);
        if (this.getSection_syntax_indicator() != 1) {
            throw new IllegalStateException("セクションシンタクス指示（section_syntax_indicator）の値が1ではありません。セクション = " + this.data);
        }
        return temp;
    }

    /**
     * CRC(最後4バイト)を取得する。
     *
     * @return セクション本体(このクラスが管理するバイト配列のセクション長の最後4バイト)への参照
     */
    public final synchronized int getCRC() {
        int crcStartPoint;
        //セクション長からcrc4バイトをマイナスし、セクション長より前のデータ3バイトを足して、このクラスが管理するバイト配列におけるcrcの開始位置を算出。
        crcStartPoint = this.getSectionLength() - 4 + 3;
        byte[] t = new byte[4];
        System.arraycopy(this.getData(), crcStartPoint, t, 0, t.length);
        return ByteConverter.bytesToInt(t);

    }

    /**
     * CRC検査結果
     */
    public static enum CRC_STATUS {
        /**
         * CRC検査結果に問題なし。
         */
        NO_CRC_ERROR(true),
        /**
         * CRC検査結果に問題あり。
         */
        CRC_ERROR(false);

        private final boolean value;

        private CRC_STATUS(boolean value) {
            this.value = value;
        }

        public boolean isValue() {
            return value;
        }
    }

    /**
     * セクションのCRC検査を行う。
     *
     * @return 検査結果
     */
    public final synchronized CRC_STATUS checkCRC() {
        SectionCrcChecker checker = new SectionCrcChecker();

        int res = checker.checkCrc(this.getData());
        if (res == 0) {
            return CRC_STATUS.NO_CRC_ERROR;
        } else {
            if (LOG.isWarnEnabled()) {
                MessageFormat msg = new MessageFormat("CRCの検査結果が0ではありません。値={0}");
                Object[] parameters = {res};
                LOG.warn(msg.format(parameters));
            }
            return CRC_STATUS.CRC_ERROR;
        }
    }

    private static final String TITLE = "セクション";
    private static final MessageFormat TABLE_DESC = new MessageFormat(
            "\n"
            + TITLE + " バイト列 = {0}\n"
            + TITLE + " テーブル識別値定数 = {1}\n"
            + TITLE + " テーブル識別値 = {2}\n"
            + TITLE + " section_syntax_indicator = {3}\n"
            + TITLE + " 予約1 = {4}\n"
            + TITLE + " 予約2 = {5}\n"
            + TITLE + " セクション長 = {6}\n"
            + TITLE + " セクション = {7}\n"
            + TITLE + " テーブル識別拡張 = {8}\n"
            + TITLE + " CRC = {9}\n"
            + TITLE + " CRC検査結果 = {10}\n"
    );

    @Override
    /**
     * このクラスで取得できるフィールドの内容を表示する。
     */
    public String toString() {
        Object[] parameters = {this.data.toString(),
            this.getTable_id_const(),
            Integer.toHexString(this.getTable_id()),
            this.getSection_syntax_indicator(),
            this.getReservedFutureUse1(),
            this.getReserved1(),
            this.getSectionLength(),
            this.getSectionBody(),
            Integer.toHexString(this.getTable_id_extension()),
            Integer.toHexString(this.getCRC()),
            this.checkCRC()};
        return TABLE_DESC.format(parameters);
    }

    /**
     * @return 保管している内容に基づくコード
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 73, this);
    }

    /**
     * @return 下記3つのすべてに当てはまればtrue<br>
     * 1.比較対象のオブジェクトがこのオブジェクトと同じクラスのインスタンスである。<br>
     * 2.比較対象のオブジェクトが保持しているバイト配列と、このオブジェクトが保持しているバイト配列が同じ内容である。<br>
     * 3.比較対象のオブジェクトが受け取るセクションのテーブルID定数と、このオブジェクトが受け取るセクションのテーブルID定数が同じ内容である。<br>
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

}
