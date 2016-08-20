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
package dumpsection.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author normal
 */
public final class Util {

    private Util() {
    }

    public static String[] stringArrayCharSetDefaultToDesired(String[] src,Charset charset) throws UnsupportedEncodingException {
        Charset cs = Charset.defaultCharset();
        List<String> strs = Arrays.asList(src);
        List<String> strd = new ArrayList<>();
        for (String str : strs) {
            strd.add(StringUtils.toEncodedString(str.getBytes(cs), Charset.forName("UTF-8")));
        }
        return strd.toArray(src);
    }

    public static Set<Integer> stringArrayToUnsignedIntegerSet(String[] src, int radix) {
        Set<Integer> x = new HashSet<>();
        List<String> ls = new ArrayList<>();
        ls.addAll(Arrays.asList(src));
        for (String s : ls) {
            try {
                x.add(Integer.parseUnsignedInt(s, radix));
            } catch (NumberFormatException e) {
                throw e;
            }
        }
        return Collections.unmodifiableSet(x);
    }

}
