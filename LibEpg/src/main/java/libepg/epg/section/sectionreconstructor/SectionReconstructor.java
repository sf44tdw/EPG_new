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
package libepg.epg.section.sectionreconstructor;

import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import libepg.epg.section.Section;
import libepg.epg.section.Section.CRC_STATUS;
import libepg.epg.section.TABLE_ID;
import libepg.ts.packet.TsPacket;
import libepg.ts.packet.TsPacketParcel;
import loggingsupport.loggerfactory.LoggerFactory;
import java.nio.BufferOverflowException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;

/**
 * 指定したpidを持つTSパケットを受け取る。
 *
 * TSパケットのペイロードからセクションを取り出して分割、再結合し、専用のオブジェクトに格納する。
 * payload_unit_start_indicatorが1の場合、TSヘッダ4バイトの次に(そのペイロード内における最初の)セクションの開始位置を示すポインターフィールド(1バイト)がある。
 *
 * 1:payload_unit_start_indicatorが1、かつポインタフィールドが0であれば、ペイロードの2バイト目から数えて、次にpayload_unit_start_indicatorが1のパケットが来るまでが1セクション。<br>
 * 2:payload_unit_start_indicatorが1、かつポインタフィールドが0以外であれば、ペイロードの2バイト目から数えてポインタフィールドの値バイトまでが前のセクションの一部、<br>
 * ポインタフィールドの値バイト+1からペイロードの終端まで次のセクションの先頭。<br>
 * 3:payload_unit_start_indicatorが0であれば、そのペイロードにはセクションの先頭は存在しない。<br>
 *
 * payload_unit_start_indicatorが0の場合はポインターフィールドが無く、 ペイロード内にセクションの先頭が無い
 *
 *
 * PID の値が望みの PSI のものかを調べる payload_unit_start_indicator を見て先頭パケットを探す
 * adaptation_field_control と pointer_field を見て data_byte が何バイト目から始まるか調べる
 * 次にpayload_unit_start_indicator が 1 になるまで payload をバッファしておいて、次の先頭パケットの
 * pointer_field 分を取り込んで返す
 *
 * @author normal
 */
public final class SectionReconstructor {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, SectionReconstructor.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final List<TsPacketParcel> parcels;
    private final int pid;

    /**
     * @param parcels 欠落チェック済みのパケット
     * @param pid 特定のpid 注:設定されたpid以外のpidを持つパケットが混じっていた場合、そのパケットは無視される。
     */
    public SectionReconstructor(List<TsPacketParcel> parcels, int pid) {
        this.pid = pid;
        List<TsPacketParcel> tempParcels = new ArrayList<>();
        for (TsPacketParcel parcel : parcels) {
            int ppid = parcel.getPacket().getPid();
            if (this.pid == ppid) {
                tempParcels.add(parcel);
            } else {
                MessageFormat WRONG_PID = new MessageFormat("指定されたPid以外のPidを持つパケットが混じっています。このパケットは除外されます。指定されたPid = {0} パケットのpid = {1} パケット = {2}");
                Object[] parameters = {Integer.toHexString(this.getPid()), Integer.toHexString(ppid), Hex.encodeHexString(parcel.getPacket().getData())};
                LOG.error(WRONG_PID.format(parameters));
            }
        }
        this.parcels = Collections.unmodifiableList(tempParcels);
    }

    /**
     * @return 最終的に受け入れられた処理対象パケットの数。
     */
    public int getPacketListSize() {
        return this.parcels.size();
    }

    public int getPid() {
        return pid;
    }

    /**
     * getSectionByteArrays()で取得したバイト列のリストをセクションのリストに変換する。CRCエラーのあるものはリストに追加しない。
     *
     * @return セクションのリスト。
     */
    public synchronized List<Section> getSections() {
        List<byte[]> section_byte = this.getSectionByteArrays();
        List<Section> ret = new ArrayList<>();
        for (byte[] b : section_byte) {
            final Section sec;
            Section s_t = null;
            try {
                s_t = new Section(b);
            } catch (IllegalArgumentException e) {
                LOG.error("セクションの構築中に問題が発生しました。このセクションは無視されます。", e);
                s_t = null;
            } finally {
                sec = s_t;
            }
            if (sec != null) {
                if (sec.checkCRC() == CRC_STATUS.NO_CRC_ERROR) {
                    ret.add(sec);
                } else {
                    LOG.error("CRCが正しくありません。このセクションは無視されます。");
                }
            }
        }
        return Collections.unmodifiableList(ret);
    }

    /**
     * バッファへの配列追加と、バッファ状態のログへの出力を行う。(traceレベルのみ)
     */
    private void put(ByteBuffer buf, byte[] putData) {
        if (LOG.isTraceEnabled()) {
            LOG.trace(
                    "\nバッファ容量 = " + buf.capacity()
                    + "\nバッファ位置 = " + buf.position()
                    + "\nバッファ残量 = " + buf.remaining()
                    + "\nバッファ = " + Hex.encodeHexString(buf.array())
                    + "\n追加予定データ長 = " + putData.length
                    + "\n追加予定データ = " + Hex.encodeHexString(putData));
        }
        buf.put(putData);
    }

    /**
     * セクションのバイト列を再構成し、リスト化する。 既に再構成されている同一のセクションがあった場合はリストに追加しない。
     *
     * @return セクションを表すバイト列のリスト
     *
     */
    public synchronized List<byte[]> getSectionByteArrays() {

        //重複排除したい。
        Set<byte[]> dic = new TreeSet<>((byte[] left, byte[] right) -> {
            for (int i = 0, j = 0; i < left.length && j < right.length; i++, j++) {
                int a = (left[i] & 0xff);
                int b = (right[j] & 0xff);
                if (a != b) {
                    return a - b;
                }
            }
            return left.length - right.length;
        });

        //サブテーブルの関係上。読み込み順にしたい。
        List<byte[]> ret = new ArrayList<>();

        boolean first_start_indicator_found = false;

        ByteBuffer buf = null;

        PayLoadSplitter splitter = new PayLoadSplitter();
        try {
            for (TsPacketParcel parcel : parcels) {

                splitter.setPacket(parcel.getPacket());
                Map<PayLoadSplitter.PAYLOAD_PART_KEY, byte[]> t_map = splitter.getSplittedPayLoad();

                if ((buf == null) || (parcel.isMissingJustBefore() == TsPacketParcel.MISSING_PACKET_FLAG.MISSING_JUST_BEFORE)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("直前のパケットが欠落していたか、バッファの初期化がなされていない場合、バッファを初期化する。");
                    }
                    if (buf == null) {
                        //セクションの最大容量ギリギリまで迫ったあげく、パケットのパディング部分で容量を超過することがあるので、セクション最大容量に加え、パケット2個分の容量を余計に確保しておく。
                        buf = ByteBuffer.allocate(TABLE_ID.MAX_SECTION_LENGTH.BYTE_4093.getMaxSectionLength() + TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength() * 2);
                    } else {
                        buf.clear();
                    }
                }

                if ((first_start_indicator_found == false) && (parcel.getPacket().getPayload_unit_start_indicator() == TsPacket.PAYLOAD_UNIT_START_INDICATOR.START_PES_OR_START_SECTION)) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("最初のセクション開始パケットを発見。");
                    }
                    first_start_indicator_found = true;
                }

                if (first_start_indicator_found == false) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("最初のセクション開始パケットを発見するまでは読み飛ばす。");
                    }
                } else if (parcel.getPacket().getPayload_unit_start_indicator() == TsPacket.PAYLOAD_UNIT_START_INDICATOR.START_PES_OR_START_SECTION) {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("セクションの先頭パケットの場合、ペイロードの必要な部分をバッファに格納");
                    }
                    if (buf.position() == 0) {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("バッファサイズが0=前のセクションの一部は残っていない。->ペイロードの、2バイト目から最後まで、もしくは、ポインタフィールドの値バイト+1から最後までをバッファにコピー");
                        }
                        byte[] temp_array = null;
                        if (t_map.size() == 1) {
                            temp_array = t_map.get(PayLoadSplitter.PAYLOAD_PART_KEY.PAYLOAD_AFTER_2_BYTE);
                        } else {
                            temp_array = t_map.get(PayLoadSplitter.PAYLOAD_PART_KEY.NEXT_POINTER);
                        }
                        this.put(buf, temp_array);
                    } else {
                        if (LOG.isTraceEnabled()) {
                            LOG.trace("バッファサイズが0ではない=前のセクションの一部は残っている。->ペイロードの、2バイト目からポインタフィールドの値バイトまでをバッファにコピーし、セクションを構築。\n完成したセクションを取り出してバッファをクリアした後、存在すればポインタフィールドの値バイト+1から最後までをバッファにコピー");
                        }
                        byte[] temp_array = null;
                        if (t_map.containsKey(PayLoadSplitter.PAYLOAD_PART_KEY.PAYLOAD_AFTER_2_BYTE)) {
                            if (LOG.isTraceEnabled()) {
                                LOG.trace("前のパケットのペイロードのちょうど最後でセクションが終わっていた場合。");
                            }

                            this.addToReturnObject(buf, dic, ret);

                            buf.clear();

                            temp_array = t_map.get(PayLoadSplitter.PAYLOAD_PART_KEY.PAYLOAD_AFTER_2_BYTE);
                            this.put(buf, temp_array);

                        } else {
                            if (LOG.isTraceEnabled()) {
                                LOG.trace("前のパケットのペイロードのちょうど最後でセクションが終わっていなかった場合。");
                            }
                            temp_array = t_map.get(PayLoadSplitter.PAYLOAD_PART_KEY.PREV_POINTER);
                            this.put(buf, temp_array);

                            this.addToReturnObject(buf, dic, ret);

                            buf.clear();

                            temp_array = t_map.get(PayLoadSplitter.PAYLOAD_PART_KEY.NEXT_POINTER);
                            this.put(buf, temp_array);
                        }
                    }
                } else {
                    if (LOG.isTraceEnabled()) {
                        LOG.trace("セクションの先頭パケットではない。->ポインタフィールドがない->ペイロードの内容すべてをバッファにコピー。");
                    }
                    this.put(buf, t_map.get(PayLoadSplitter.PAYLOAD_PART_KEY.ALL_PAYLOAD));
                }
            }
            return Collections.unmodifiableList(ret);
        } catch (BufferOverflowException ex) {
            LOG.fatal(ex);
            throw ex;
        }
    }

    /**
     * バッファからデータだけ抜き出して、コレクションに格納する。重複したものは無視する。
     * ByteBufferのarray()をそのまま使うと、無条件でバッファ長分の配列を受け取ることになる。
     *
     */
    private synchronized void addToReturnObject(ByteBuffer buf, Set<byte[]> dict, List<byte[]> dest) {
        byte[] BeforeCutDown = buf.array();
        byte[] AfterCutDown = new byte[buf.position()];
        System.arraycopy(BeforeCutDown, 0, AfterCutDown, 0, AfterCutDown.length);
        if (LOG.isTraceEnabled()) {
            MessageFormat msg1 = new MessageFormat("\n切り詰め前={0}\n切り詰め後={1}");
            Object[] parameters1 = {Hex.encodeHexString(BeforeCutDown), Hex.encodeHexString(AfterCutDown)};
            LOG.trace(msg1.format(parameters1));
        }
        if (dict.add(AfterCutDown)) {
            dest.add(AfterCutDown);
            if (LOG.isTraceEnabled()) {
                MessageFormat msg2 = new MessageFormat("\n追加されたバイト列={0}");
                Object[] parameters2 = {Hex.encodeHexString(AfterCutDown)};
                LOG.trace(msg2.format(parameters2));
            }
        } else if (LOG.isTraceEnabled()) {
            MessageFormat msg3 = new MessageFormat("\n重複のため無視されたバイト列={0}");
            Object[] parameters3 = {Hex.encodeHexString(AfterCutDown)};
            LOG.trace(msg3.format(parameters3));
        }
    }

}
