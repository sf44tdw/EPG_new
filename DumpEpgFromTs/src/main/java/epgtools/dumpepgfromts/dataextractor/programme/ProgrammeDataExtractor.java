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
package epgtools.dumpepgfromts.dataextractor.programme;

import epgtools.dumpepgfromts.dataextractor.AbstractDataExtractor;
import epgtools.dumpepgfromts.dataextractor.channel.Channel;

/**
 * EITから番組情報を取り出し、重複を除去してリスト化する。取り出す情報は以下の通り。
 * EventInformationTableBody.getTransport_stream_id
 * EventInformationTableBody.getOriginal_network_id
 * EventInformationTableBody.getService_id
 * EventInformationTableRepeatingPart.getEvent_id
 * EventInformationTableRepeatingPart.getStart_time_Object
 * EventInformationTableRepeatingPart.getStopTime_Object
 * ShortEventDescriptor.getEvent_name_String 
 * ShortEventDescriptor.getText_String
 * ExtendedEventDescriptor.getStoredText
 * ContentDescriptor.getNibbles
 *
 * @author normal
 */
public class ProgrammeDataExtractor extends AbstractDataExtractor<Channel> {

}
