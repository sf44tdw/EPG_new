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
import libepg.epg.section.descriptor.DescriptorsLoop;
import libepg.epg.section.ranges.SectionValueRangeChecker;
import libepg.util.bytearray.ByteConverter;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.logging.Log;

/**
 * トランスポートストリームループ
 *
 * @author normal
 */
public class TransportStreamLoop {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TransportStreamLoop.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final ByteDataBlock data;

    public TransportStreamLoop(byte[] data) {
        this.data = new ByteDataBlock(data);
    }

    public byte[] getData() {
        return data.getData();
    }

    /**
     * トランスポートストリーム識別<br>
     * 16bit。<br>
     * SDTが示すトランスポートストリームをその分配システム内の他の多重から識別するラベルの役割をする<br>
     *
     * @return 上記の値
     * @throws IllegalStateException 0x0000もしくは0xffffのとき。
     */
    public final synchronized int getTransport_stream_id() throws IllegalStateException {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 0, t, 0, t.length);
        int x = ByteConverter.bytesToInt(t);
        if (!SectionValueRangeChecker.TRANSPORT_STREAM_ID_RANGE.contains(x)) {
            throw new IllegalStateException("トランスポートストリーム識別が範囲外です。 値 = " + Integer.toHexString(x));
        }
        return x;
    }

    /**
     * original_network_id(オリジナルネットワーク識別):この16ビットのフィールドは、
     * 元の分配システムのネットワーク識別を規定するラベルの役割をする。
     *
     * @return 上記の値
     */
    public final synchronized int getOriginal_network_id() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 2, t, 0, t.length);
        int temp = ByteConverter.bytesToInt(t);
        return temp;
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、 その値が将来、ARIB
     * STD-B10が定義する拡張子として使用されるかもしれないことを表す。 4bit
     *
     * @return 上記の値
     */
    public final synchronized int getReserved_future_use() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[4]);
        temp = temp >>> 4;
        return temp;
    }

    /**
     * descriptors_loop_length(記述子ループ長):network_descriptors_length（トランスポート記述子長）：この12
     * ビットのフィールドは、 後に続くトランスポートストリーム記述子の全バイト数を表す。
     *
     * @return 上記の値
     */
    public synchronized int getDescriptors_loop_length() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 4, t, 0, t.length);
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
            System.arraycopy(this.getData(), 6, t, 0, t.length);
        }
        return new DescriptorsLoop(t);
    }

    /**
     * @return 保管している内容に基づくコード
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 17, this);
    }

    /**
     * @return 同じオブジェクトならtrue
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    private static final String TITLE = "トランスポートストリームループ";
    private static final MessageFormat TS_DESC = new MessageFormat(
            "\n"
            + TITLE + " バイト列 = {0}\n"
            + TITLE + " トランスポートストリーム識別 = {1}\n"
            + TITLE + " オリジナルネットワーク識別 = {2}\n"
            + TITLE + " 予約 = {3}\n"
            + TITLE + " 記述子ループ長 = {4}\n"
            + TITLE + " 記述子ループ = {5}\n"
    );

    @Override
    /**
     * このクラスで取得できるフィールドの内容を表示する。
     */
    public String toString() {
        Object[] parameters = {this.data,
            Integer.toHexString(this.getTransport_stream_id()),
            Integer.toHexString(this.getOriginal_network_id()),
            this.getReserved_future_use(),
            this.getDescriptors_loop_length(),
            this.getDescriptors_loop()};
        return TS_DESC.format(parameters);
    }

}
