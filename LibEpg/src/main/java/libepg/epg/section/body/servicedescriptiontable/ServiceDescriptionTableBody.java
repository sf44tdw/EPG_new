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
package libepg.epg.section.body.servicedescriptiontable;

import libepg.util.bytearray.ByteConverter;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import libepg.epg.section.SectionBody;
import libepg.epg.section.body.util.SubTableSectionCommonFields;
import libepg.epg.section.range.SectionValueRangeChecker;
import libepg.util.bytearray.ByteArraySplitter;
import org.apache.commons.codec.binary.Hex;

/**
 * サービス記述テーブルSDT（Service Description Table）のセクション本体<br>
 * 編成チャンネルの名称、放送事業者の名称など、編成チャンネルに関する情報を伝送する。<br>
 */
public final class ServiceDescriptionTableBody extends SectionBody {

    public ServiceDescriptionTableBody(SectionBody sectionBody) {
        super(sectionBody);
    }

    private final SubTableSectionCommonFields commonFields = new SubTableSectionCommonFields(this);

    /**
     * トランスポートストリーム識別<br>
     * 16bit。<br>
     * SDTが示すトランスポートストリームをその分配システム内の他の多重から識別するラベルの役割をする<br>
     *
     * @return 上記の値
     * @throws IllegalStateException 0x0000もしくは0xffffのとき。
     */
    public final synchronized int getTransport_stream_id() throws IllegalStateException {
        int x = this.commonFields.getFirst16Bit();
        if (!SectionValueRangeChecker.TRANSPORT_STREAM_ID_RANGE.contains(x)) {
            throw new IllegalStateException("トランスポートストリーム識別が範囲外です。 値 = " + Integer.toHexString(x));
        }
        return x;
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、その値が将来ISO で定義される拡張子として使用されるかもしれないことを表す。 2bit。
     *
     * @return 上記の値
     */
    public final synchronized int getReserved2() {
        return this.commonFields.getSecond2Bit();
    }

    /**
     * version_number(バージョン番号):この5ビットのフィールドはサブテーブルのバージョン番号である。
     * バージョン番号はサブテーブル内の情報に変化があった場合に1加算される。
     * その値が31になった場合は、その次は0に戻る。カレントネクスト指示が「1」の場合は、バージョン番号はテーブル識別とネットワーク識別で定義される現在のサブテーブルのバージョン番号になる。
     * カレントネクスト指示が「0」の場合は、バージョン番号はテーブル識別とネットワーク識別で定義される次のサブテーブルのバージョン番号になる。
     *
     * @return 上記の値
     */
    public final synchronized int getVersion_number() {
        return this.commonFields.getTherd5Bit();
    }

    /**
     * current_next_indicator(カレントネクスト指示):この1ビットの指示は、
     * それが「1」の場合はサブテーブルが現在のサブテーブルであることを示す。
     * 「0」の場合は、送られるサブテーブルはまだ適用されず、次のサブテーブルとして使用されることを示す。
     *
     * @return 上記の値
     */
    public final synchronized int getCurrent_next_indicator() {
        return this.commonFields.getFourth1Bit();
    }

    /**
     * section_number(セクション番号):この8ビットのフィールドはセクションの番号を表す。
     * サブテーブル中の最初のセクションのセクション番号は、0x00である。
     * セクション番号は、同一のテーブル識別、トランスポートストリーム識別、オリジナルネットワーク識別を持つセクションの追加ごとに1加算される。
     *
     * @return 上記の値
     */
//    int section_number;
    public final synchronized int getSection_number() {
        return this.commonFields.getFifth8Bit();
    }

    /**
     * last_section_number(最終セクション番号):この8ビットのフィールドは、 そのセクションが属するサブテーブルの最後のセクション
     * (すなわち、最大のセクション番号を持つセクション)の番号を規定する。
     *
     * @return 上記の値
     */
//    int last_section_number;
    public final synchronized int getLast_section_number() {
        return this.commonFields.getSixth8Bit();
    }

    /**
     * original_network_id(オリジナルネットワーク識別):この16ビットのフィールドは、
     * 元の分配システムのネットワーク識別を規定するラベルの役割をする。
     *
     * @return 上記の値
     */
    public final synchronized int getOriginal_network_id() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 5, t, 0, t.length);
        int temp = ByteConverter.bytesToInt(t);
        return temp;
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、 その値が将来、ARIB
     * STD-B10が定義する拡張子として使用されるかもしれないことを表す。 8bit
     *
     * @return 上記の値
     */
    public final synchronized int getReserved_future_use2() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[7]);
        return temp;
    }

    /**
     * 繰り返し項目のバイト列
     *
     * @return 上記の値
     */
    public final synchronized byte[] getRepeatingPart() {
        byte[] t = new byte[this.getData().length - 8];
        System.arraycopy(this.getData(), 8, t, 0, t.length);
        return t;
    }

    /**
     * 分割済みの繰り返し項目。
     *
     * @return 上記の値
     */
    public final synchronized List<ServiceDescriptionTableRepeatingPart> getSDTRepeatingPartList() {
        List<byte[]> t = ByteArraySplitter.splitByLengthField(this.getRepeatingPart(), 4, 2, (x) -> x & 0x0FFF);
        List<ServiceDescriptionTableRepeatingPart> dest = new ArrayList<>();
        for (byte[] temp : t) {
            dest.add(new ServiceDescriptionTableRepeatingPart(temp));
        }
        return Collections.unmodifiableList(dest);
    }

    private static final String TITLE = "サービス記述テーブル";
    private static final MessageFormat TABLE_DESC = new MessageFormat(
            TITLE + " {0}\n"
            + TITLE + " トランスポートストリーム識別 = {1}\n"
            + TITLE + " Reserved2 = {2}\n"
            + TITLE + " バージョン番号 = {3}\n"
            + TITLE + " カレントネクスト指示 = {4}\n"
            + TITLE + " セクション番号 = {5}\n"
            + TITLE + " 最終セクション番号 = {6}\n"
            + TITLE + " オリジナルネットワーク識別 = {7}\n"
            + TITLE + " Reserved_future_use2 = {8}\n"
            + TITLE + " 繰り返し項目 = {9}\n"
            + TITLE + " 繰り返し項目の解析結果={10}\n"
    );

    @Override
    /**
     * このクラスで取得できるフィールドの内容を表示する。
     */
    public String toString() {
        List<ServiceDescriptionTableRepeatingPart> l = this.getSDTRepeatingPartList();
        StringBuilder s = new StringBuilder();
        for (ServiceDescriptionTableRepeatingPart sdtrp : l) {
            s.append(sdtrp);
        }

        Object[] parameters = {super.toString(),
            Integer.toHexString(this.getTransport_stream_id()),
            Integer.toHexString(this.getReserved2()),
            this.getVersion_number(),
            this.getCurrent_next_indicator(),
            Integer.toHexString(this.getSection_number()),
            Integer.toHexString(this.getLast_section_number()),
            Integer.toHexString(this.getOriginal_network_id()),
            Integer.toHexString(this.getReserved_future_use2()),
            Hex.encodeHexString(this.getRepeatingPart()),
            s.toString()};
        return TABLE_DESC.format(parameters);
    }

}
