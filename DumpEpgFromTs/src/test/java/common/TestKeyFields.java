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

/**
 *
 * @author normal
 */
public final class TestKeyFields {

    public static final int a = 1, b = 1, c = 2;
    public static final epgtools.dumpchannelfromts.dataextractor.channel.KeyFields KEY_1 = new epgtools.dumpchannelfromts.dataextractor.channel.KeyFields(a, b, c);
    public static final epgtools.dumpchannelfromts.dataextractor.channel.KeyFields KEY_2_EQUALS_KEY_1 = new epgtools.dumpchannelfromts.dataextractor.channel.KeyFields(a, b, c);

    //サブチャンネル想定
    public static final int x = 3, y = 3;
    public static final epgtools.dumpchannelfromts.dataextractor.channel.KeyFields KEY_3 = new epgtools.dumpchannelfromts.dataextractor.channel.KeyFields(x, y, 3);
    public static final epgtools.dumpchannelfromts.dataextractor.channel.KeyFields KEY_4 = new epgtools.dumpchannelfromts.dataextractor.channel.KeyFields(x, y, 4);

    private TestKeyFields() {
    }

}
