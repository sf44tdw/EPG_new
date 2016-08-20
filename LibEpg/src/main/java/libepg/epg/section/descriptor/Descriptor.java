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
package libepg.epg.section.descriptor;

import libepg.util.bytearray.ByteConverter;
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import epgtools.loggerfactory.LoggerFactory;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 記述子(descriptor()の部分)の共通フィールド <br>
 *
 * @author normal
 */
public class Descriptor {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    protected static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Descriptor.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final ByteDataBlock data;
    private final DESCRIPTOR_TAG descriptorTag;

    /**
     * 基底クラスから生成する。具体的なフィールドを定義したサブクラスの生成で使用する。
     *
     * @param descriptor 記述子
     */
    public Descriptor(Descriptor descriptor) {

        this.descriptorTag = descriptor.descriptorTag;
        this.data = descriptor.data;
        if (this.getClass() != this.descriptorTag.getDataType()) {
            MessageFormat msg1 = new MessageFormat("このクラスでは渡されたデータをパースできません。このクラスの型={0} データの型={1} データ={2}");
            Object[] parameters1 = {this.getClass(), this.descriptorTag.getDataType(), this.data};
            throw new IllegalArgumentException(msg1.format(parameters1));
        }

    }

    /**
     * 記述子を受け取る。DescriptorsLoopがオブジェクトを生成する際に使う。
     *
     * @param data 記述子のデータ
     * @throws IllegalArgumentException
     * データに含まれる記述子タグがpreferedDescriptorTagで渡された定数に含まれていない場合。
     */
    Descriptor(byte[] data) {

        this.data = new ByteDataBlock(data);

        int lengthFromData = ByteConverter.byteToInt(this.data.getData()[1]) + 2;
        int lengthFromArray = this.getData().length;
        if (lengthFromData != lengthFromArray) {
            MessageFormat msg1 = new MessageFormat("渡されたデータの長さが想定と異なります。データから算出した配列長={0} 配列長={1} データ={2}");
            Object[] parameters1 = {lengthFromData, lengthFromArray, this.data};
            throw new IllegalArgumentException(msg1.format(parameters1));
        }

        int tag = ByteConverter.byteToInt(this.data.getData()[0]);
        this.descriptorTag = DESCRIPTOR_TAG.reverseLookUp(tag);
        if (this.descriptorTag == null) {
            MessageFormat msg = new MessageFormat("記述子タグが見つかりません。タグの値={0} データ={1}");
            Object[] parameters = {Integer.toHexString(tag), this.data};
            throw new IllegalArgumentException(msg.format(parameters));
        }
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
     * @return コンストラクタで設定された記述子タグ定数
     */
    public final synchronized DESCRIPTOR_TAG getDescriptor_tag_const() {
        return this.descriptorTag;
    }

    /**
     * descriptor_tag（記述子タグ）<br>
     *
     * @return 記述子タグの値
     */
    public final synchronized int getDescriptor_tag() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[0]);
        return temp;
    }

    /**
     * descriptor_length（記述子長）：記述子長は8 ビットのフィールドで、このフィールドの
     * 直後に続く記述子のデータ部分の全バイト長を規定する。
     *
     * @return 記述子長
     */
    public final synchronized int getDescriptor_length() {
        int temp;
        temp = ByteConverter.byteToInt(this.getData()[1]);
        return temp;
    }

    /**
     * 記述子のデータ部分(このクラスが管理するバイト配列の3バイト目から最後まで)を取得する。
     *
     * @return 上記の値
     */
    public final synchronized byte[] getDescriptor_Body() {
        int length;
        length = this.getDescriptor_length();
        if (LOG.isTraceEnabled()) {
            LOG.trace("記述子長=" + length);
            LOG.trace("クラス管理下のバイト配列長-2=" + (this.data.length() - 2));
        }
        byte[] t = new byte[length];
        System.arraycopy(this.getData(), 2, t, 0, length);
        return t;
    }

    private static final String TITLE = "記述子";
    private static final MessageFormat DESC = new MessageFormat(
            "\n"
            + TITLE + " バイト列 = {0}\n"
            + TITLE + " 記述子タグ = {1}\n"
            + TITLE + " 記述子タグ定数 = {2}\n"
            + TITLE + " 記述子長 = {3}\n"
            + TITLE + " 記述子データ = {4}\n"
    );

    @Override
    public String toString() {
        Object[] parameters = {this.data,
            Integer.toHexString(this.getDescriptor_tag()),
            this.getDescriptor_tag_const(),
            this.getDescriptor_length(),
            Hex.encodeHexString(this.getDescriptor_Body())};
        return DESC.format(parameters);
    }

    /**
     * @return 保管している内容に基づくコード
     */
    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 83, this);
    }

    /**
     * @return 下記3つのすべてに当てはまればtrue<br>
     * 1.比較対象のオブジェクトがこのオブジェクトと同じクラスのインスタンスである。<br>
     * 2.比較対象のオブジェクトが保持しているバイト配列と、このオブジェクトが保持しているバイト配列が同じ内容である。<br>
     * 3.比較対象のオブジェクトの記述子タグ定数と、このオブジェクトの記述子タグ定数が同じ内容である。<br>
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

}
