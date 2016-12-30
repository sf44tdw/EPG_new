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
package libepg.ts.reader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PushbackInputStream;
import java.lang.invoke.MethodHandles;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import libepg.ts.packet.TsPacket;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;

/**
 * ファイルからTSパケットを指定された数だけ読み込む。 何らかの不正な値を含むパケットもしくはトランスポートエラーインジケータが1のパケットは無視する。
 *
 * @author normal
 */
public class Reader2 {

    /**
     * 読み込み関連のログを抑止するか。trueなら抑止しない。
     * 読み込み関連のログを抑止しないでログレベルをtraceに設定した場合、ログの量が読み込み元のtsファイルの容量を超える(1GBを超えることもある)ため、このクラスのデバッグ時以外は抑止しておく。
     */
    public static final boolean NOT_DETERRENT_READ_TRACE_LOG = false;

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TsReader.CLASS_LOG_OUTPUT_MODE).getLOG();
        if (NOT_DETERRENT_READ_TRACE_LOG == false) {
            LOG.info("読み込みログ抑止中。");
        }
    }

    private static final int EOF = -1;
    private final File TSFile;
    private final Long readLimit;

    /**
     * ファイルからTSパケットを指定された数だけ読み込む。
     * パケットの1バイト目が動機ワードではないか、トランスポートエラーインジケータが1のパケットは読み込まない。
     *
     * @author normal
     * @param TSFile 読み込むファイル。
     * @param readLimit ファイル冒頭から読み込むパケット数。<br>
     * この数字未満でファイルの終端が来た場合は、それ以上読み込まない。<br>
     * @throws java.io.FileNotFoundException ファイルが見つからなかったとき。
     */
    public Reader2(File TSFile, Long readLimit) throws FileNotFoundException {
        this.TSFile = new File(TSFile.getAbsolutePath());
        if (this.TSFile.isFile() == false) {
            throw new FileNotFoundException("TSファイルが見つかりません。");
        }
        this.readLimit = readLimit;
        if (this.readLimit != null) {
            if (this.readLimit < 0) {
                throw new IllegalArgumentException("読み込みパケット数の最大値が0未満です。");
            }
            LOG.info("このファイルの" + this.readLimit + "パケット目まで読み込みを行います。");
        } else {
            LOG.info("このファイルの最後まで読み込みを行います。");
        }
    }

    /**
     * ファイルからTSパケットをファイルの最後まで読み込む。
     * パケットの1バイト目が動機ワードではないか、トランスポートエラーインジケータが1のパケットは読み込まない。
     *
     * @author normal
     * @param TSFile 読み込むファイル。
     * @throws java.io.FileNotFoundException ファイルが見つからなかったとき。
     */
    public Reader2(File TSFile) throws FileNotFoundException {
        this(TSFile, null);
    }

    public synchronized List<TsPacket> getPackets() {
        ByteBuffer packetBuffer = ByteBuffer.allocate(TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength());
        byte[] byteData = new byte[1];

        //データ格納用
        List<TsPacket> packets = new ArrayList<>();

        FileInputStream fis = null;
        PushbackInputStream pis = null;
        try {

            fis = new FileInputStream(this.TSFile);
            pis = new PushbackInputStream(fis);

            boolean tipOfPacket = false;//パケット先端発見済みフラグ

            long count = 0;

            //パケットの先端が見つかるまで1バイトずつ読んでゆく。
            while (pis.read(byteData) != EOF) {

                //最初にパケットの開始を示す値が来るまで、読み込んだ値を保存しない。
                if ((byteData[0] == TsPacket.TS_SYNC_BYTE) && (tipOfPacket == false)) {
                    tipOfPacket = true;
                    if (LOG.isTraceEnabled() && NOT_DETERRENT_READ_TRACE_LOG) {
                        LOG.trace("パケットの頭を読み取ったので、ストリームの読み込みを1バイト前に戻す。");
                    }
                    pis.unread(byteData);
                }

                if (tipOfPacket == true) {
                    byte[] tsPacketData = new byte[TsPacket.TS_PACKET_BYTE_LENGTH.PACKET_LENGTH.getByteLength()];
                    if (pis.read(tsPacketData) != EOF) {
                        if (LOG.isTraceEnabled() && NOT_DETERRENT_READ_TRACE_LOG) {
                            LOG.trace("パケットの頭を発見済みの場合、パケットバッファに、現在の位置からパケット長分の値を読み込む。");
                        }
                        packetBuffer.put(tsPacketData);
                    } else {
                        break;
                    }
                }

                if (packetBuffer.remaining() == 0) {
                    byte[] BeforeCutDown = packetBuffer.array();
                    byte[] AfterCutDown = new byte[packetBuffer.position()];
                    System.arraycopy(BeforeCutDown, 0, AfterCutDown, 0, AfterCutDown.length);

                    try {
                        TsPacket tsp = new TsPacket(AfterCutDown);
                        
//                        LOG.debug(Hex.encodeHexString(tsp.getData()));
                        
                        if (LOG.isTraceEnabled() && NOT_DETERRENT_READ_TRACE_LOG) {
                            LOG.trace("1パケット分読み込んだら、内容を専用オブジェクトにコピーしてパケットバッファを初期化 ");
                            LOG.trace(tsp.toString());
                        }

                        if (tsp.getTransport_error_indicator() != 0) {
                            if (LOG.isWarnEnabled()) {
                                LOG.warn("トランスポートエラーインジケータが1のパケットです。読み込み開始位置がずれた可能性があります。\n"
                                        + "パケット開始位置を再捜索します。");
                                LOG.warn(tsp);
                                LOG.warn(TSFile);
                            }
                            tipOfPacket = false;
                        } else {
                            packets.add(tsp);
                            count++;
                        }
                    } catch (IllegalArgumentException ex) {
                        LOG.warn(ex.getMessage());
                    }
                    packetBuffer.clear();
                    tipOfPacket = false;

                    if (this.readLimit != null && count >= this.readLimit) {
                        if (LOG.isInfoEnabled()) {
                            LOG.info("指定された数のパケットを読み込みました。読み込みを中断します。 指定されたパケット数 = " + this.readLimit);
                        }
                        break;
                    }
                }

            }
            if (LOG.isTraceEnabled() && NOT_DETERRENT_READ_TRACE_LOG) {
                LOG.trace("この時点でパケットバッファの中身は空のはず。");
                LOG.trace("中身 = " + Hex.encodeHexString(packetBuffer.array()));
            }

            pis.close();
            fis.close();
            LOG.info("合計読み込みパケット数 = " + count);

        } catch (FileNotFoundException e) {
            LOG.fatal("ファイルが見つかりません。", e);
        } catch (IOException e) {
            LOG.fatal("ファイル読み込みに失敗。", e);
        }

        return Collections.unmodifiableList(packets);
    }

}
