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
package epgtools.dumpchannellistfromts.physicalchannelnumberrecord;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import libepg.epg.section.range.SectionValueRangeChecker;
import org.apache.commons.lang3.Range;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author normal
 */
@CsvEntity(header = true)
public final class PhysicalChannelNumberRecord implements Comparable<PhysicalChannelNumberRecord> {

    private final Range<Integer> tsIdRange = SectionValueRangeChecker.TRANSPORT_STREAM_ID_RANGE;
    private final Range<Integer> IdRange = SectionValueRangeChecker.UNSIGNED_SIXTEEN_BIT_FIELD_RANGE;

    @CsvColumn(position = 0, name = "物理チャンネル番号")
    private final int physicalChannelNumber;
    @CsvColumn(position = 1, name = "ネットワーク識別")
    private final int networkId;
    @CsvColumn(position = 2, name = "ネットワーク名")
    private final String networkName;
    @CsvColumn(position = 3, name = "トランスポートストリーム識別")
    private final int transportStreamId;
    @CsvColumn(position = 4, name = "オリジナルネットワーク識別")
    private final int originalNetworkId;
    @CsvColumn(position = 5, name = "サービス識別")
    private final int serviceId;

    public PhysicalChannelNumberRecord(int physicalChannelNumber, int networkId, String networkName, int transportStreamId, int originalNetworkId, int serviceId) {
        this.physicalChannelNumber = physicalChannelNumber;
        this.networkId = networkId;
        this.networkName = networkName;
        this.transportStreamId = transportStreamId;
        this.originalNetworkId = originalNetworkId;
        this.serviceId = serviceId;

        String FieldName = null;
        String value = null;
        CHECK:
        {
            if (!this.IdRange.contains(this.physicalChannelNumber)) {
                FieldName = "物理チャンネル番号";
                value = Integer.toHexString(this.networkId);
                break CHECK;
            }
            if (!this.IdRange.contains(this.networkId)) {
                FieldName = "ネットワーク識別";
                value = Integer.toHexString(this.networkId);
                break CHECK;
            }
            if (!this.tsIdRange.contains(this.transportStreamId)) {
                FieldName = "トランスポートストリーム識別";
                value = Integer.toHexString(this.transportStreamId);
                break CHECK;
            }
            if (!this.IdRange.contains(this.originalNetworkId)) {
                FieldName = "オリジナルネットワーク識別";
                value = Integer.toHexString(this.originalNetworkId);
                break CHECK;
            }
            if (!this.IdRange.contains(this.serviceId)) {
                FieldName = "サービス識別";
                value = Integer.toHexString(this.serviceId);
                break CHECK;
            }
        }
        if (FieldName != null) {
            throw new IllegalStateException("想定外の値が渡されました。フィールド = " + FieldName + " 値 = " + value);
        }
    }

    public int getPhysicalChannelNumber() {
        return physicalChannelNumber;
    }

    public int getNetworkId() {
        return networkId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public int getTransportStreamId() {
        return transportStreamId;
    }

    public int getOriginalNetworkId() {
        return originalNetworkId;
    }

    public int getServiceId() {
        return serviceId;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(3, 71, this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    /**
     * トランスポートストリーム識別<br>
     * オリジナルネットワーク識別<br>
     * サービス識別<br>
     * 物理チャンネル番号<br>
     * ネットワーク識別<br>
     * ネットワーク名<br>
     * それぞれ上から順に昇順で比較。
     */
    @Override
    public int compareTo(PhysicalChannelNumberRecord o) {
        if (this.equals(o)) {
            return 0;
        } else if (this.transportStreamId > o.transportStreamId) {
            return 1;
        } else if (this.originalNetworkId > o.originalNetworkId) {
            return 1;
        } else if (this.serviceId > o.serviceId) {
            return 1;
        } else if (this.physicalChannelNumber > o.physicalChannelNumber) {
            return 1;
        } else if (this.networkId > o.networkId) {
            return 1;
        } else if (this.networkName.compareTo(o.networkName) > 0) {
            return 1;
        } else {
            return -1;
        }
    }

}
