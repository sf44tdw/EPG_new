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
package epgtools.dumpchannelfromts.dataextractor.channel;

/**
 * SDTからチャンネル情報を抽出する。
 * 取り出す情報は以下の通り。
 * transport_stream_id=ServiceDescriptionTableBody.getTransport_stream_id
 * original_network_id=ServiceDescriptionTableBody.getOriginal_network_id
 * service_id=ServiceDescriptionTableRepeatingPart.getService_id
* ServiceDescriptor.getService_name_String
 * @author normal
 */
public class ChannelDataExtractor {
    
}
