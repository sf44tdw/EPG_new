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
package libepg.epg.util.datetime;

import libepg.util.bytearray.ByteConverter;
import java.lang.invoke.MethodHandles;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.Range;
import org.apache.commons.logging.Log;
import java.util.Arrays;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import libepg.util.bytearray.ByteDataBlock;

/**
 *
 * @author normal
 */
public class DateTimeFieldConverter {

    /**
     * 定義なしの時の値
     */
    private static final ByteDataBlock UNDEFINED_MJD_DATE_BLOCK = new ByteDataBlock(new byte[]{(byte) 0xff, (byte) 0xff});
    /**
     * 定義なしの時の値
     */
    private static final ByteDataBlock UNDEFINED_BCD_TIME_BLOCK = new ByteDataBlock(new byte[]{(byte) 0xff, (byte) 0xff, (byte) 0xff});
    /**
     * 定義なしの時の値
     */
    private static final ByteDataBlock UNDEFINED_DATETIME_BLOCK = new ByteDataBlock(ArrayUtils.addAll(UNDEFINED_MJD_DATE_BLOCK.getData(), UNDEFINED_BCD_TIME_BLOCK.getData()));

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass(); 
        LOG = new LoggerFactory(myClass, DateTimeFieldConverter.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private static final String DATE_PATTERN = "yyyyMMddHHmmss";

    //1桁しかない場合は上の位を0で埋めて2桁にする。
    private static final DecimalFormat ZERO_FILL_FORMAT = new DecimalFormat("00");

    private DateTimeFieldConverter() {
    }

    /**
     * 修正ユリウス日の年月日をyyyymmdd形式の年月日に変換する。 1900 年3 月1 日から2100 年2 月28 日までの間有効な変換式
     *
     * @param mjd mjd形式の年月日(16ビット)
     * @return yyyymmdd形式の年月日
     * @throws IndexOutOfBoundsException 配列長が2バイト以外。
     * @throws IllegalArgumentException 与えられた値が定義なし(0xffff)の時。
     */
    public static synchronized String mjdToString(byte[] mjd) throws IndexOutOfBoundsException, IllegalArgumentException {
        if (mjd.length != 2) {
            throw new IndexOutOfBoundsException("配列が想定外のサイズです。2バイト以外には対応していません。" + " 配列=" + Hex.encodeHexString(mjd));
        }

        if (Arrays.equals(mjd, UNDEFINED_MJD_DATE_BLOCK.getData())) {
            throw new IllegalArgumentException("日付が定義なしです。変換できません。" + " 配列=" + Hex.encodeHexString(mjd));
        }
        int tnum = ByteConverter.bytesToInt(mjd);
        int yy = (int) ((tnum - 15078.2) / 365.25);
        int mm = (int) (((tnum - 14956.1) - (int) (yy * 365.25)) / 30.6001);
        int dd = (tnum - 14956) - (int) (yy * 365.25) - (int) (mm * 30.6001);

        if (mm == 14 || mm == 15) {
            yy += 1;
            mm = mm - 1 - (1 * 12);
        } else {
            mm = mm - 1;
        }
        yy += 1900;
        if (LOG.isTraceEnabled()) {
            LOG.trace("yy=" + yy + " mm=" + mm + " dd=" + dd);
        }

        StringBuilder sb = new StringBuilder();
        sb.append(yy);
        sb.append(ZERO_FILL_FORMAT.format(mm));
        sb.append(ZERO_FILL_FORMAT.format(dd));
        return sb.toString();
    }

    private static enum HMS_KEY {
        HOUR, MINUTE, SECOND;
    }

    private static Map<HMS_KEY, Integer> BcdDateTimeToMap(byte[] hms) {
        Map<HMS_KEY, Integer> ret = new HashMap<>();
        if (hms.length != 3) {
            throw new IndexOutOfBoundsException("配列が想定外のサイズです。3バイト以外には対応していません。" + " 配列=" + Hex.encodeHexString(hms));
        }

        //ARIBの仕様で時刻の定義がないときはこうなっている。
        if (Arrays.equals(hms, UNDEFINED_BCD_TIME_BLOCK.getData())) {
            throw new IllegalArgumentException("時刻が定義なしです。変換できません。" + " 配列=" + Hex.encodeHexString(hms));
        }

        final int hour = new BCD(hms[0]).getDecimal();
        final int minute = new BCD(hms[1]).getDecimal();
        final int second = new BCD(hms[2]).getDecimal();

        ret.put(HMS_KEY.HOUR, hour);
        ret.put(HMS_KEY.MINUTE, minute);
        ret.put(HMS_KEY.SECOND, second);

        return ret;
    }

    /**
     * 時分秒で表示された時間(最大99時間99分99秒)を6個の4ビット2進化10進数(BCD)で符号化したbyte配列(01:45:30は「0x014530」と符号化される。)を、それに対応する秒数に変換する。<br>
     * ((h*60)+m)*60+s<br>
     *
     * @param hms 時分秒
     * @return 与えられた値を秒数に変換したもの。
     * @throws IndexOutOfBoundsException 配列長が3バイト以外。
     * @throws IllegalArgumentException 与えられた値が定義なし(0xffffff)の時。
     */
    public static synchronized long BcdTimeToSecond(byte[] hms) throws IndexOutOfBoundsException, IllegalArgumentException {
        Map<HMS_KEY, Integer> m = BcdDateTimeToMap(hms);
        long ret = m.get(HMS_KEY.HOUR) * 60;
        ret = (ret + m.get(HMS_KEY.MINUTE)) * 60;
        ret = ret + m.get(HMS_KEY.SECOND);
        return ret;
    }

    private static String BcdTimeToString(Map<HMS_KEY, Integer> hms) {
        StringBuilder sb = new StringBuilder();
        sb.append(ZERO_FILL_FORMAT.format(hms.get(HMS_KEY.HOUR)));
        sb.append(ZERO_FILL_FORMAT.format(hms.get(HMS_KEY.MINUTE)));
        sb.append(ZERO_FILL_FORMAT.format(hms.get(HMS_KEY.SECOND)));
        return sb.toString();
    }

    /**
     * BCDが24時間表示の時刻表示の範囲かどうかを判別する。時0から23 分0から59 秒0から59 範囲外の場合例外を投げる。
     *
     * @throws IllegalArgumentException BCDが時刻表示で使用できる範囲外の場合。
     */
    private static void dateTimeRangeChecker(Map<HMS_KEY, Integer> hms) throws IllegalArgumentException {
        final int hour = hms.get(HMS_KEY.HOUR);
        final int minute = hms.get(HMS_KEY.MINUTE);
        final int second = hms.get(HMS_KEY.SECOND);
        Object[] parameters = null;
        CHECK:
        {
            final Range<Integer> HOUR_RANGE = Range.between(0, 23);

            if (!HOUR_RANGE.contains(hour)) {
                parameters = new Object[]{Integer.toHexString(hour), "時", hour};
                break CHECK;
            }

            final Range<Integer> MINUTE_AND_SECOND_RANGE = Range.between(0, 59);

            if (!MINUTE_AND_SECOND_RANGE.contains(minute)) {
                parameters = new Object[]{Integer.toHexString(minute), "分", minute};
                break CHECK;
            }

            if (!MINUTE_AND_SECOND_RANGE.contains(second)) {
                parameters = new Object[]{Integer.toHexString(second), "秒", second};
                break CHECK;
            }
        }
        if (LOG.isTraceEnabled()) {
            LOG.trace("hour=" + hour + " minute=" + minute + " second=" + second);
        }
        if (parameters != null) {
            MessageFormat msg = new MessageFormat("時刻表示の範囲外になっている部分があります。与えられた値={0} 時分秒={1} 値={2}");
            throw new IllegalArgumentException(msg.format(parameters));
        }
    }

    /**
     * 下記の形式で与えられた日付をjava.sql.Timestamp型に変換する。 <br>
     * MJD の下位16 ビットを16 ビットで符号化し、続く24ビットを6個の4ビット2進化10進数（BCD）で符号化する。<br>
     * 定義されない場合には、このフィールドの全てのビットは「1」に設定する。 <br>
     * 例１：93/10/13 12:45:00は「0xC079124500」と符号化される。
     *
     * @param source 日付
     * @return 日時の入ったTimestampオブジェクト。
     * @throws IndexOutOfBoundsException 与えられた配列のサイズが5バイト以外の場合。
     * @throws IllegalArgumentException フィールドのすべてのビットが｢1｣の場合か、他の理由で変換できなかった場合。
     * @throws ParseException  下記参照
     * @see java.text.SimpleDateFormat#parse(java.lang.String,
     * java.text.ParsePosition)
     */
    public static synchronized java.sql.Timestamp BytesToSqlDateTime(byte[] source) throws IllegalArgumentException, IndexOutOfBoundsException, ParseException {
        if (source.length != 5) {
            throw new IndexOutOfBoundsException("配列が想定外のサイズです。5バイト以外には対応していません。" + " 配列=" + Hex.encodeHexString(source));
        }

        if (Arrays.equals(source, UNDEFINED_DATETIME_BLOCK.getData())) {
            throw new IllegalArgumentException("与えられたフィールドは未定義になっています。 配列 = " + Hex.encodeHexString(source));
        }

        StringBuilder sb = new StringBuilder();

        byte[] mjd = new byte[2];
        System.arraycopy(source, 0, mjd, 0, mjd.length);
        sb.append(mjdToString(mjd));

        byte[] hms = new byte[3];
        System.arraycopy(source, 2, hms, 0, hms.length);
        Map<HMS_KEY, Integer> hmsMap = BcdDateTimeToMap(hms);
        dateTimeRangeChecker(hmsMap);
        sb.append(BcdTimeToString(hmsMap));

        String dateStr = sb.toString();

        java.text.SimpleDateFormat dateParser = new java.text.SimpleDateFormat(DATE_PATTERN);
        dateParser.setLenient(false);
        long dt = dateParser.parse(dateStr).getTime();
        if (LOG.isTraceEnabled()) {
            LOG.trace(dt);
        }
        return new java.sql.Timestamp(dt);
    }
}
