/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libepg.epg.section.transportstreamid;

import org.apache.commons.lang3.Range;

/**
 * トランスポートストリーム識別の範囲チェックを行う。<br>
 * ARIB TR-B15 8.1.1 トランスポートストリーム識別（transport_stream_id）割り当てガイドライン (p7-51)<br>
 * より、0x0000、および0xFFFFはtransport_stream_idとして使用されない。<br>
 *
 * @author normal
 */
public final class TransportStreamIdRangeChecker {

    private TransportStreamIdRangeChecker() {
    }
    public static final Range<Integer> TRANSPORT_STREAM_ID_RANGE = Range.between(0x1, 0xFFFE);
}
