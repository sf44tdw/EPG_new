/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libepg.ts.packet;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 * TSパケットへの参照と、直前のパケットの欠落を示すフラグを保管する。
 *
 * @author normal
 */
public final class TsPacketParcel {

    /**
     * 前パケット欠落フラグ
     */
    public static enum MISSING_PACKET_FLAG {
        /**
         * 前パケット欠落なし(false)
         */
        NOT_MISSING(false),
        /**
         * 前パケット欠落あり(true)
         */
        MISSING_JUST_BEFORE(true);
        private boolean flag;

        private MISSING_PACKET_FLAG(boolean flag) {
            this.flag = flag;
        }

        public boolean getFlag() {
            return flag;
        }
    }

    private final MISSING_PACKET_FLAG MissingJustBefore;

    private final TsPacket packet;

    public TsPacketParcel(TsPacket packet, MISSING_PACKET_FLAG MissingJustBefore) {
        this.packet = packet;
        this.MissingJustBefore = MissingJustBefore;
    }

    /**
     * 直前のパケットの欠落を示すフラグ。デフォルトはfalse
     *
     * @return NOT_MISSINGならば、このオブジェクトに格納されているパケットの直前のパケットは欠落していない。
     */
    public MISSING_PACKET_FLAG isMissingJustBefore() {
        return MissingJustBefore;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(3, 97, this);
    }

    public TsPacket getPacket() {
        return packet;
    }

    /**
     * @return
     * 比較対象のオブジェクトがこのオブジェクトと同じクラスのインスタンスで、比較対象のオブジェクトと、このオブジェクトのフィールドの値が同じ内容であればtrue
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

}
