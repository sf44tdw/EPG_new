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
package libepg.epg.section.body.networkinformationtable;

import epgtools.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import libepg.epg.section.body.SectionBody;
import libepg.epg.section.body.eventinformationtable.EventInformationTableRepeatingPart;
import libepg.epg.section.body.util.SubTableSectionCommonFields;
import libepg.epg.section.descriptor.DescriptorsLoop;
import libepg.util.bytearray.ByteArraySplitter;
import libepg.util.bytearray.ByteConverter;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;

//network_information_section(){
//table_id 8 uimsbf
//section_syntax_indicator 1 bslbf
//reserved_future_use 1 bslbf
//reserved 2 bslbf
//section_length 12 uimsbf
//network_id 16 uimsbf
//reserved 2 bslbf
//version_number 5 uimsbf
//current_next_indicator 1 bslbf
//section_number 8 uimsbf
//last_section_number 8 uimsbf
//reserved_future_use 4 bslbf
//network_descriptors_length 12 uimsbf
//for(i=0;i<N;i++){
//descriptor()
//}
//reserved_future_use 4 bslbf
//transport_stream_loop_length 12 uimsbf
//for(i=0;i<N;i++){
//transport_stream_id 16 uimsbf
//original_network_id 16 uimsbf
//reserved_future_use 4 bslbf
//transport_descriptors_length 12 uimsbf
//for(j=0;j<N;j++){
//descriptor()
//}
//}
//CRC_32 32 rpchof
//}
/**
 * ネットワーク情報テーブル（NIT）（Network Information Table） NIT（表5－3
 * 参照）は、ネットワークにより運ばれる多重／トランスポートストリーム の物理的構成に関する情報、およびネットワーク自身の特性を表わす。オリジナルネット
 * ワーク識別とトランスポートストリーム識別の組み合わせにより、各トランスポートスト
 * リームを本規格の適用範囲全体において唯一に定義することが可能である。ネットワーク
 * は固有のネットワーク識別を割り当てられ、それがネットワークの固有識別符号として機
 * 能する。ネットワーク識別の値は標準化機関の規定による。また、トランスポート識別の
 * 値は事業者が独自に選定することができる。トランスポートストリームが発生したネット ワークにおいてNIT
 * が伝送された場合には、ネットワーク識別とオリジナルネットワーク 識別は同一値である。 ARIB STD-B10 第２部 p79より
 *
 * @author normal
 */
public class NetworkInformationTableBody extends SectionBody {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, NetworkInformationTableBody.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final SubTableSectionCommonFields commonFields = new SubTableSectionCommonFields(this);

    public NetworkInformationTableBody(SectionBody sectionBody) {
        super(sectionBody);
    }

    /**
     * network_id（ネットワーク識別）：これは16 ビットのフィールドで、NIT が示す分配シ
     * ステムを他の分配システムと区別して識別するラベルの役割をする。このフィールド値の 割り当ては標準化機関の規定による。（付録Ｎ参照）
     */
    public final synchronized int getNetwork_id() {
        return this.commonFields.getFirst16Bit();
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、その値が将来ISO で定義される拡張子として使用されるかもしれないことを表す。 2bit。
     *
     * @return 上記の値
     */
    public final synchronized int getReserved1() {
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
    public final synchronized int getSection_number() {
        return this.commonFields.getFifth8Bit();
    }

    /**
     * last_section_number(最終セクション番号):この8ビットのフィールドは、 そのセクションが属するサブテーブルの最後のセクション
     * (すなわち、最大のセクション番号を持つセクション)の番号を規定する。
     *
     * @return 上記の値
     */
    public final synchronized int getLast_section_number() {
        return this.commonFields.getSixth8Bit();
    }

    //ここまでで5バイト
    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、 その値が将来、ARIB
     * STD-B10が定義する拡張子として使用されるかもしれないことを表す。 4bit
     *
     * @return 上記の値
     */
    public final synchronized int getReserved_future_use1() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[5]);
        temp = temp >>> 4;
        return temp;
    }

    /**
     * descriptors_loop_length(記述子ループ長):network_descriptors_length（ネットワーク記述子長）：この12
     * ビットのフィールドは、 後に続くネットワーク記述子の全バイト数を表す。
     *
     * @return 上記の値
     */
    public synchronized int getDescriptors_loop_length() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 5, t, 0, t.length);
        int it = ByteConverter.bytesToInt(t);
        it = it & 0x0FFF;
        return it;
    }

    /**
     * 記述子ループを取得する。
     *
     * @return 上記の値
     */
    public synchronized DescriptorsLoop getDescriptors_loop() {
        byte[] t = new byte[this.getDescriptors_loop_length()];
        if (t.length > 0) {
            System.arraycopy(this.getData(), 7, t, 0, t.length);
        }
        return new DescriptorsLoop(t);
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、 その値が将来、ARIB
     * STD-B10が定義する拡張子として使用されるかもしれないことを表す。 4bit
     *
     * @return 上記の値
     */
    public final synchronized int getReserved_future_use2() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[5 + this.getDescriptors_loop_length() + 1 + 1]);
        temp = temp >>> 4;
        return temp;
    }

    /**
     * transport_stream_loop_length（トランスポートストリームループ長）：これは、CRC_32
     * の最初のバイトの直前に終わるトランスポートストリームループの全バイト数を規定する 12 ビットのフィールドである。
     */
    public synchronized int getTransport_stream_loop_length() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 5 + this.getDescriptors_loop_length() + 1 + 1, t, 0, t.length);
        int it = ByteConverter.bytesToInt(t);
        it = it & 0x0FFF;
        LOG.debug(Hex.encodeHexString(t) + " -> " + it);
        return it;
    }

    /**
     * トランスポートストリームループを取得する。
     *
     * @return 上記の値
     */
    public synchronized List<TransportStreamLoop> getTransport_streams_loop() {
        List<TransportStreamLoop> ret = new ArrayList<>();
        byte[] t = new byte[this.getTransport_stream_loop_length()];
        if (t.length > 0) {
            System.arraycopy(this.getData(), 5 + this.getDescriptors_loop_length() + 1 + 1 + 2, t, 0, t.length);
            List<byte[]> result = ByteArraySplitter.splitByLengthField(t, 5, 2, (x) -> x & 0x0FFF);
            for (byte[] b : result) {
                ret.add(new TransportStreamLoop(b));
            }
        }
        return ret;
    }

    private static final String TITLE = "ネットワーク情報テーブル";
    private static final MessageFormat TABLE_DESC = new MessageFormat(
            TITLE + " {0}\n"
            + TITLE + " ネットワーク識別 = {1}\n"
            + TITLE + " 予約1 = {2}\n"
            + TITLE + " バージョン番号 = {3}\n"
            + TITLE + " カレントネクスト指示 = {4}\n"
            + TITLE + " セクション番号 = {5}\n"
            + TITLE + " 最終セクション番号 = {6}\n"
            + TITLE + " 予約1 = {7}\n"
            + TITLE + " 記述子ループ長 = {8}\n"
            + TITLE + " 記述子ループ={9}\n"
            + TITLE + " 予約2 = {10}\n"
            + TITLE + " トランスポートストリームループ長 = {11}\n"
            + TITLE + " トランスポートストリームループ = {12}\n"
    );

    @Override
    /**
     * このクラスで取得できるフィールドの内容を表示する。
     */
    public String toString() {
        List<TransportStreamLoop> l = this.getTransport_streams_loop();
        StringBuilder s = new StringBuilder();
        for (TransportStreamLoop rp : l) {
            s.append(rp);
        }
        Object[] parameters = {
            super.toString(),
            Integer.toHexString(this.getNetwork_id()),
            this.getReserved1(),
            this.getVersion_number(),
            this.getCurrent_next_indicator(),
            this.getSection_number(),
            this.getLast_section_number(),
            this.getReserved_future_use1(),
            this.getDescriptors_loop_length(),
            this.getDescriptors_loop(),
            this.getReserved_future_use2(),
            this.getTransport_stream_loop_length(),
            s};
        return TABLE_DESC.format(parameters);
    }

}
