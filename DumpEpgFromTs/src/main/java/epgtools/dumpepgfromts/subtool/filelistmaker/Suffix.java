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
package epgtools.dumpepgfromts.subtool.filelistmaker;

import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;

/**
 * tsファイルの拡張子
 * @author normal
 */
public class Suffix {

    private static final IOFileFilter SUFFIX_1 = FileFilterUtils.suffixFileFilter("ts");
    private static final IOFileFilter SUFFIX_2 = FileFilterUtils.suffixFileFilter("Ts");
    private static final IOFileFilter SUFFIX_3 = FileFilterUtils.suffixFileFilter("tS");
    private static final IOFileFilter SUFFIX_4 = FileFilterUtils.suffixFileFilter("TS");

    public static final IOFileFilter TS_SUFFIX = FileFilterUtils.or(SUFFIX_1, SUFFIX_2, SUFFIX_3, SUFFIX_4);

}
