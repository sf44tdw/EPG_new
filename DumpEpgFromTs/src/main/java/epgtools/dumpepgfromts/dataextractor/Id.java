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
package epgtools.dumpepgfromts.dataextractor;

/**
 * 番組と放送局の識別に必要になる項目。
 * @author normal
 */
public interface Id {

    /**
     * @return オリジナルネットワーク識別
     */
    int getOriginal_network_id();

    /**
     * @return サービス識別
     */
    int getService_id();

    /**
     * @return トランスポートストリーム識別
     */
    int getTransport_stream_id();
    
}
