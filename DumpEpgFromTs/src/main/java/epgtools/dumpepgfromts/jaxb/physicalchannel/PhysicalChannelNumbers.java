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

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;

/**
 *
 * @author normal
 */
public class PhysicalChannelNumbers {

    private Set<PhysicalChannelNumberRecord> Channels;

    public PhysicalChannelNumbers() {
        this.Channels = new HashSet<>();
    }

    public Set<PhysicalChannelNumberRecord> getChannels() {
        return Channels;
    }

    public void setChannels(Set<PhysicalChannelNumberRecord> Channels) {
        this.Channels = Channels;
    }


    public void add(PhysicalChannelNumberRecord channel) {
        this.Channels.add(channel);
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
}
