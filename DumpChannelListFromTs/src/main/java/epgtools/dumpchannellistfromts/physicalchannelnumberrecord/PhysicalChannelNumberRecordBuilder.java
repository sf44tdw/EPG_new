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

import java.util.Objects;

/**
 *
 * @author normal
 */
public class PhysicalChannelNumberRecordBuilder {

    private int physicalChannelNumber;
    private int networkId;
    private String networkName;
    private int transportStreamId;
    private int originalNetworkId;
    private int serviceId;

    public PhysicalChannelNumberRecord build() {
        return new PhysicalChannelNumberRecord(this.getPhysicalChannelNumber(),this.getNetworkId(), this.getNetworkName(), this.getTransportStreamId(), this.getOriginalNetworkId(), this.getServiceId());
    }

    public int getPhysicalChannelNumber() {
        return physicalChannelNumber;
    }

    public void setPhysicalChannelNumber(int physicalChannelNumber) {
        this.physicalChannelNumber = physicalChannelNumber;
    }

    public int getNetworkId() {
        return networkId;
    }

    public void setNetworkId(int networkId) {
        this.networkId = networkId;
    }

    public String getNetworkName() {
        return networkName;
    }

    public void setNetworkName(String networkName) {
        this.networkName = networkName;
    }

    public int getTransportStreamId() {
        return transportStreamId;
    }

    public void setTransportStreamId(int transportStreamId) {
        this.transportStreamId = transportStreamId;
    }

    public int getOriginalNetworkId() {
        return originalNetworkId;
    }

    public void setOriginalNetworkId(int originalNetworkId) {
        this.originalNetworkId = originalNetworkId;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + this.networkId;
        hash = 19 * hash + Objects.hashCode(this.networkName);
        hash = 19 * hash + this.transportStreamId;
        hash = 19 * hash + this.originalNetworkId;
        hash = 19 * hash + this.serviceId;
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
        final PhysicalChannelNumberRecordBuilder other = (PhysicalChannelNumberRecordBuilder) obj;
        if (this.networkId != other.networkId) {
            return false;
        }
        if (this.transportStreamId != other.transportStreamId) {
            return false;
        }
        if (this.originalNetworkId != other.originalNetworkId) {
            return false;
        }
        if (this.serviceId != other.serviceId) {
            return false;
        }
        if (!Objects.equals(this.networkName, other.networkName)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "NetWorkObjectBuilder{" + "networkId=" + networkId + ", networkName=" + networkName + ", transportStreamId=" + transportStreamId + ", originalNetworkId=" + originalNetworkId + ", serviceId=" + serviceId + '}';
    }

}
