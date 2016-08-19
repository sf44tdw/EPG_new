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
package epgtools.dumpepgfromts.databinder;

import epgtools.dumpepgfromts.dataextractor.channel.Channel;
import epgtools.dumpepgfromts.dataextractor.programme.Programme;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 複合キーを使って、放送サービスと番組をマッチングする。
 *
 * @author normal
 */
public class DataBinder {

    private final Set<Channel> channels;
    private final Set<Programme> programmes;
    private final Map<Channel,Set<Programme>> bindMap=new ConcurrentHashMap<>();

    public DataBinder(Set<Channel> channels, Set<Programme> programmes) {
        this.channels = channels;
        this.programmes = programmes;
    }



}
