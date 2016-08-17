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
package common;

import static common.TestKeyFields.KEY_1;
import static common.TestKeyFields.KEY_2_EQUALS_KEY_1;
import static common.TestKeyFields.KEY_3;
import static common.TestKeyFields.KEY_4;
import epgtools.dumpchannelfromts.dataextractor.channel.Channel;

/**
 *
 * @author normal
 */
public class TestChannel {

    private TestChannel() {
    }

    public static final String s = "Disp1";

    public static final Channel CH_1 = new Channel(KEY_1.getTransport_stream_id(), KEY_1.getOriginal_network_id(), KEY_1.getService_id(), s);
    public static final Channel CH_2_EQUALS_CH_1 = new Channel(KEY_2_EQUALS_KEY_1.getTransport_stream_id(), KEY_2_EQUALS_KEY_1.getOriginal_network_id(), KEY_2_EQUALS_KEY_1.getService_id(), s);
    public static final Channel CH_3 = new Channel(KEY_3.getTransport_stream_id(), KEY_3.getOriginal_network_id(), KEY_3.getService_id(), "Disp3");
    public static final Channel CH_4 = new Channel(KEY_4.getTransport_stream_id(), KEY_4.getOriginal_network_id(), KEY_4.getService_id(), "Disp4");
}
