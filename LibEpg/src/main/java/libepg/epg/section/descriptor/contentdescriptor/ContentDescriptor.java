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
package libepg.epg.section.descriptor.contentdescriptor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import libepg.epg.section.descriptor.Descriptor;
import libepg.util.bytearray.ByteArraySplitter;

/**
 * コンテント記述子（Content descriptor） コンテント記述子はイベントのジャンルを示すものである。
 */
public class ContentDescriptor extends Descriptor {

    public ContentDescriptor(Descriptor discriptor) {
        super(discriptor);
    }

    /**
     * ジャンルリストを取得する。
     *
     * @return 上記の値
     */
    public synchronized List<Nibble> getNibbles() {
        List<byte[]> t = ByteArraySplitter.split(this.getDescriptor_Body(), 2);
        List<Nibble> tn = new ArrayList<>();
        for (byte[] b : t) {
            tn.add(new Nibble(b));
        }
        return Collections.unmodifiableList(tn);
    }

    @Override
    public String toString() {
        List<Nibble> tn = this.getNibbles();
        StringBuilder s = new StringBuilder();
        for (Nibble nb : tn) {
            s.append(nb);
        }
        return "コンテント記述子" + " ジャンルコード = " + s.toString() + "\n";
    }

}
