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

import java.text.MessageFormat;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * 0以上99以下の10進整数に相当するBCDコードを保持する。
 * @author normal
 */
public class BCD {
    
    private final Range<Integer> BCD_RANGE = Range.between(0, 9);

    private final int high;
    private final int low;

    /**
     *
     * @param bcd BCD2桁分の値のbyte型変数
     * @throws IllegalArgumentException 渡された変数の上位か下位の4ビットが0x0以上0x9以下ではない場合。
     */
    public BCD(byte bcd) throws IllegalArgumentException{
        
        high = bcd >>> 4;
        low = bcd & 0x0f;

        Object[] parameters = null;
        CHECK:
        {
            if (!BCD_RANGE.contains(high)) {
                parameters = new Object[]{Integer.toHexString(bcd),"上", Integer.toHexString(bcd)};
                break CHECK;
            }
            if (!BCD_RANGE.contains(low)) {
                parameters = new Object[]{Integer.toHexString(bcd),"下", Integer.toHexString(bcd)};
                break CHECK;
            }

        }
        if (parameters != null) {
            MessageFormat msg = new MessageFormat("このオブジェクトで取り扱える範囲外の数値です。渡された値={0} 桁={1} その桁の値={2}");
            throw new IllegalArgumentException(msg.format(parameters));
        }

    }

    public int getHigh() {
        return high;
    }

    public int getLow() {
        return low;
    }

    public int getDecimal() {
        return 10 * high + low;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(5, 53, this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
