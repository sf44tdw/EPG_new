/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package channellistmaker.dataextractor;

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
