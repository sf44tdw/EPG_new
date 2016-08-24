/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libepg.epg.section.body.eventinformationtable;

import libepg.util.bytearray.ByteConverter;
import java.lang.invoke.MethodHandles;
import java.sql.Timestamp;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Objects;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import libepg.epg.section.descriptor.DescriptorsLoop;
import libepg.epg.util.datetime.DateTimeFieldConverter;
import loggingsupport.loggerfactory.LoggerFactory;
import libepg.util.bytearray.ByteDataBlock;

/**
 * イベント記述テーブルの繰り返し項目
 *
 * @author normal
 */
public final class EventInformationTableRepeatingPart {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, EventInformationTableRepeatingPart.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final ByteDataBlock data;

    EventInformationTableRepeatingPart(byte[] data) {
        this.data = new ByteDataBlock(data);
    }

    public synchronized byte[] getData() {
        return data.getData();
    }

    /**
     * event_id（イベント識別）：この16 ビットのフィールドは、記載されているイベントの識別番号 （1
     * サービス内で一意的に割り当てられる）を示す。
     *
     * @return 上記の値
     */
    public synchronized int getEvent_id() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 0, t, 0, t.length);
        return ByteConverter.bytesToInt(t);
    }

    /**
     * start_time（開始時間）：この40 ビットのフィールドは、イベントの開始時間を日本標準 時（JST）と修正ユリウス日（MJD）（付録C
     * 参照）で示す。このフィールドは、MJD の下位16 ビットを16 ビットで符号化し、続く24 ビットを6 個の4 ビット2 進化10 進数
     * （BCD）で符号化する。開始時間が定義されない場合（例えばNVOD 基準サービスなど） には、このフィールドの全てのビットは「1」に設定する。
     * 例１：93/10/13 12:45:00 は「0xC079124500」と符号化される。
     *
     * @return 上記の値
     */
    public synchronized byte[] getStart_time() {
        byte[] t = new byte[5];
        System.arraycopy(this.getData(), 2, t, 0, t.length);
        return t;
    }

    /**
     * start_time（開始時間）
     *
     * @return タイムスタンプに変換された開始時間 変換不能な場合はnull;
     */
    public synchronized Timestamp getStart_time_Object() {
        try {
            byte[] t = this.getStart_time();
            return (DateTimeFieldConverter.BytesToSqlDateTime(t));
        } catch (IllegalArgumentException | IndexOutOfBoundsException | ParseException ex) {
            LOG.error("開始時間を示すタイムスタンプの生成に失敗しました。 繰り返し項目 = " + this.data, ex);
            return null;
        }
    }

    /**
     * duration（継続時間）：24 ビットのフィールドで、イベントの継続時間を時、分、秒で表
     * す。継続時間が定義されない場合（例えば緊急ニュースで終了時刻が未定など）には、こ のフィールドのすべてのビットを「1」に設定する。 形式：6個の4
     * ビットBCD 符号 = 24 ビット 例2：01:45:30 は「0x014530」と符号化される。
     * ※"何時間何分何秒後"を意味するので、最大99時間99分99秒あたりまでゆける(はず)。BSジャパンのstock777は240000と設定していた。
     *
     * @return 上記の値
     */
    public synchronized byte[] getDuration() {
        byte[] t = new byte[3];
        System.arraycopy(this.getData(), 7, t, 0, t.length);
        return t;
    }

    /**
     * 終了日時
     *
     * @return タイムスタンプに変換された 終了日時 変換不能な場合はnull;
     */
    public synchronized Timestamp getStop_Time_Object() {
        try {
            byte[] t = this.getDuration();
            long x = DateTimeFieldConverter.BcdTimeToSecond(t) * 1000;
            x = x + this.getStart_time_Object().getTime();
            return new Timestamp(x);
        } catch (IndexOutOfBoundsException | IllegalArgumentException ex) {
            LOG.error("終了日時を示すタイムスタンプの生成に失敗しました。 繰り返し項目 = " + this.data, ex);
            return null;
        }
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
        int t = ByteConverter.byteToInt(this.getData()[10]);
        t = t >>> 5;
        return t;
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
        temp = ByteConverter.byteToInt(this.data.getData()[10]);
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
        System.arraycopy(this.data.getData(), 10, t, 0, t.length);
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
            System.arraycopy(this.data.getData(), 12, t, 0, t.length);
        }
        return new DescriptorsLoop(t);
    }

    private static final String TITLE = "イベント記述テーブルの繰り返し項目";
    private static final MessageFormat EIT_RP_DESC = new MessageFormat(
            TITLE + " バイト列 = {0}\n"
            + TITLE + " イベント識別 = {1}\n"
            + TITLE + " 開始時間 = {2}\n"
            + TITLE + " 開始時間(タイムスタンプ) = {3}\n"
            + TITLE + " 継続時間 = {4}\n"
            + TITLE + " 終了時間(タイムスタンプ) = {5}\n"
            + TITLE + " 進行状態 = {6}\n"
            + TITLE + " スクランブル = {7}\n"
            + TITLE + " 記述子ループ長 = {8}\n"
            + TITLE + " 記述子ループ = {9}\n"
    );

    @Override
    public String toString() {

        String start = "", stop = "";

        start = this.getStart_time_Object().toString();
        stop = this.getStop_Time_Object().toString();

        Object[] parameters = {this.data,
            Integer.toHexString(this.getEvent_id()),
            Hex.encodeHexString(this.getStart_time()),
            start,
            Hex.encodeHexString(this.getDuration()),
            stop,
            Integer.toHexString(this.getRunning_status()),
            Integer.toHexString(this.getFree_CA_mode()),
            this.getDescriptors_loop_length(),
            this.getDescriptors_loop()};
        return EIT_RP_DESC.format(parameters);

    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EventInformationTableRepeatingPart other = (EventInformationTableRepeatingPart) obj;
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

}
