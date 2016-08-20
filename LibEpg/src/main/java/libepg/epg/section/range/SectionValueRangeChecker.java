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
package libepg.epg.section.range;

import org.apache.commons.lang3.Range;

/**
 * 特定の値が禁止されている場合、数値項目の範囲チェックを行うためのオブジェクトを保管する。
 */
public final class SectionValueRangeChecker {

    private SectionValueRangeChecker() {
    }
    /**
     * トランスポートストリーム識別の範囲チェックを行う。<br>
     * ARIB TR-B15 8.1.1 トランスポートストリーム識別（transport_stream_id）割り当てガイドライン
     * (p7-51)<br>
     * より、0x0000、および0xFFFFはtransport_stream_idとして使用されない。<br>
     *
     * @author normal
     */
    public static final Range<Integer> TRANSPORT_STREAM_ID_RANGE = Range.between(0x1, 0xFFFE);

    /**
     * 通常の符号なし16ビットフィールド
     */
    public static final Range<Integer> UNSIGNED_SIXTEEN_BIT_FIELD_RANGE = Range.between(0x0, 0xFFFF);
}
