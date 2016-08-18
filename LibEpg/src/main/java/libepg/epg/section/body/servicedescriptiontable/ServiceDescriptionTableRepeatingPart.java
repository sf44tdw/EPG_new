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
import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import org.apache.commons.logging.Log;
import libepg.epg.section.descriptor.DescriptorsLoop;
import epgtools.loggerfactory.LoggerFactory;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * サービス記述テーブルの繰り返し項目
 *
 * @author normal
 */
public final class ServiceDescriptionTableRepeatingPart {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, ServiceDescriptionTableRepeatingPart.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final ByteDataBlock data;

    ServiceDescriptionTableRepeatingPart(byte[] data) {
        this.data = new ByteDataBlock(data);
    }

    public byte[] getData() {
        return data.getData();
    }

    //繰り返し
    /**
     * service_id(サービス識別):これは16ビットのフィールドで、<br>
     * そのトランスポートストリーム内の他のサービスからこのサービスを識別するためのラベルの役割をする。<br>
     * サービス識別は、対応するプログラムマップセクション内の放送番組番号識別(program_number)に等しい<br>
     *
     * @return 上記の値
     */
    public synchronized int getService_id() {
        byte[] t = new byte[2];
        System.arraycopy(this.data.getData(), 0, t, 0, t.length);
        return ByteConverter.bytesToInt(t);
    }

    /**
     * 符号化ビットストリームを定義する項の中で使用する場合、 <br>
     * その値が将来、ARIBSTD-B10が定義する拡張子として使用されるかもしれないことを表す。 3bit。<br>
     *
     * @return 上記の値
     */
    public synchronized int getReserved_future_use1() {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[2]);
        temp = temp >>> 5;
        return temp;
    }

    /**
     * EIT_user_defined_flags(EIT事業者定義フラグ):この3ビットのフィールドは、<br>
     * EITの送出有無を示す拡張として、放送事業者が独自に定義することができる。 <br>
     * 使用しない場合は、「111」とする。<br>
     *
     * @return 上記の値
     */
    public synchronized int getEIT_user_defined_flags() {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[2]);
        temp = temp >>> 2;
        temp = temp & 0x07;
        return temp;
    }

    /**
     * EIT_schedule_flag(EIT[スケジュール]フラグ):この1ビットのフィールドは、<br>
     * それが「1」の場合はそのサービスのEIT[スケジュール]情報が現在のトランスポートストリーム内にあることを示す。<br>
     * (EIT[スケジュール]サブテーブルの最大送出間隔については「ガイドライン」を参照。）<br>
     * このフラグが「0」の場合は、そのサービスのEIT[スケジュール]情報が現在のトランスポートストリーム内には存在しないことを表す。<br>
     *
     * @return 上記の値
     */
    public synchronized int getEIT_schedule_flag() {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[2]);
        temp = temp >>> 1;
        temp = temp & 0x01;
        return temp;
    }

    /**
     * EIT_present_following_flag(EIT[現在/次]フラグ):この1ビットのフィールドは、<br>
     * それが「1」の場合はそのサービスのEIT[現在/次]情報が現在のトランスポートストリーム内にあることを示す。<br>
     * (EIT[現在/次]サブテーブルの最大送出間隔については「ガイドライン」を参照。)<br>
     * このフラグが「0」の場合は、そのサービスのEIT[現在/次]情報が現在のトランスポートストリーム内には存在しないことを表す。<br>
     *
     * @return 上記の値
     */
    public synchronized int getEIT_present_following_flag() {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[2]);
        temp = temp & 0x01;
        return temp;
    }

    /**
     * running_status(進行状態):この3ビットフィールドは、サービスの状態を示す。<br>
     * 0__=_未定義<br>
     * 1__=_非実行中<br>
     * 2__=_数秒以内に開始（例：映像記録用）<br>
     * 3__=_停止中<br>
     * 4__=_実行中<br>
     * 5-7=_将来の使用のためにリザーブ<br>
     *
     * @return 上記の値
     */
    public synchronized int getRunning_status() {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[3]);
        temp = temp >>> 5;
        return temp;
    }

    /**
     * free_CA_mode(スクランブル):この1ビットのフィールドは、
     * それが「0」の場合はそのサービス内の全てのコンポーネントストリームがスクランブルされていないことを示す。
     * 「1」の場合は、一つ以上のストリームへのアクセスがCAシステムにより制御されていることを示す。
     *
     * @return 上記の値
     */
    public synchronized int getFree_CA_mode() {
        int temp;
        temp = ByteConverter.byteToInt(this.data.getData()[3]);
        temp = temp >>> 4;
        temp = temp & 0x01;
        return temp;
    }

    /**
     * descriptors_loop_length(記述子ループ長):これは、12ビットのフィールドで、 後続の記述子の全バイト長を規定する
     *
     * @return 上記の値
     */
    public synchronized int getDescriptors_loop_length() {
        byte[] t = new byte[2];
        System.arraycopy(this.data.getData(), 3, t, 0, t.length);
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
            System.arraycopy(this.data.getData(), 5, t, 0, t.length);
        }
        return new DescriptorsLoop(t);
    }

    private static final MessageFormat SDT_RP_DESC = new MessageFormat(
            "サービス記述繰り返し項目 バイト列 = {0}\n"
            + "サービス記述繰り返し項目 サービス識別 = {1}\n"
            + "サービス記述繰り返し項目 予約 = {2}\n"
            + "サービス記述繰り返し項目 EIT事業者定義フラグ = {3}\n"
            + "サービス記述繰り返し項目 EIT[スケジュール]フラグ = {4}\n"
            + "サービス記述繰り返し項目 EIT[現在/次]フラグ = {5}\n"
            + "サービス記述繰り返し項目 進行状態 = {6}\n"
            + "サービス記述繰り返し項目 スクランブル = {7}\n"
            + "サービス記述繰り返し項目 記述子ループ長 = {8}\n"
            + "サービス記述繰り返し項目 記述子ループ = {9}\n"
    );

    @Override
    public String toString() {
        Object[] parameters = {this.data.toString(),
            Integer.toHexString(this.getService_id()),
            Integer.toHexString(this.getReserved_future_use1()),
            Integer.toHexString(this.getEIT_user_defined_flags()),
            Integer.toHexString(this.getEIT_schedule_flag()),
            Integer.toHexString(this.getEIT_present_following_flag()),
            Integer.toHexString(this.getRunning_status()),
            Integer.toHexString(this.getFree_CA_mode()),
            this.getDescriptors_loop_length(),
            this.getDescriptors_loop().toString()};
        return SDT_RP_DESC.format(parameters);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 47, this);
    }


    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

}
