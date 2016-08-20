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
package epgtools.dumpepgfromts.jaxb.physicalchannel;

import com.orangesignal.csv.annotation.CsvColumn;
import com.orangesignal.csv.annotation.CsvEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author normal
 */
@CsvEntity(header = true)
public final class PhysicalChannelNumberRecord {

    /**
     * 物理チャンネル番号
     */
    @CsvColumn(position = 0, name = " 物理チャンネル番号")
    
    public int physical_channel_number;
    /**
     * サービス識別
     */
    @CsvColumn(position = 1, name = " サービス識別")
    public int service_id;
    
    /**
     * トランスポートストリーム識別
     */
    @CsvColumn(position = 2, name = " トランスポートストリーム識別")
    public int transport_stream_id;

    /**
     * オリジナルネットワーク識別
     */
    @CsvColumn(position = 3, name = " オリジナルネットワーク識別")
    public int original_network_id;

    /**
     * サービス名
     */
    @CsvColumn(position = 4, name = " サービス名")
    public String service_name;

    /**
     * デフォルトコンストラクタ
     */
    public PhysicalChannelNumberRecord() {
    }

    public PhysicalChannelNumberRecord(int physical_channel_number, int transport_stream_id, int original_network_id, int service_id, String service_name) {
        this.physical_channel_number = physical_channel_number;
        this.transport_stream_id = transport_stream_id;
        this.original_network_id = original_network_id;
        this.service_id = service_id;
        this.service_name = service_name;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(5, 97, this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }

    public int getPhysical_channel_number() {
        return physical_channel_number;
    }

    public void setPhysical_channel_number(int physical_channel_number) {
        this.physical_channel_number = physical_channel_number;
    }

    public int getTransport_stream_id() {
        return transport_stream_id;
    }

    public void setTransport_stream_id(int transport_stream_id) {
        this.transport_stream_id = transport_stream_id;
    }

    public int getOriginal_network_id() {
        return original_network_id;
    }

    public void setOriginal_network_id(int original_network_id) {
        this.original_network_id = original_network_id;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getService_name() {
        return service_name;
    }

    public void setService_name(String service_name) {
        this.service_name = service_name;
    }

}
