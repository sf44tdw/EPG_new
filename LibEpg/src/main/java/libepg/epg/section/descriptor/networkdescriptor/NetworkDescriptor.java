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
package libepg.epg.section.descriptor.networkdescriptor;

import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.util.Aribstr;
import org.apache.commons.codec.binary.Hex;

//network_name_descriptor(){
//descriptor_tag 8 uimsbf
//descriptor_length 8 uimsbf
//for (i=0;i<N;i++){
//char 8 uimsbf
//}
//}
/**
 * ネットワーク名記述子（Network name descriptor） ネットワーク名記述子は文字符号によりネットワーク名を記述する。表6－21 参照
 *
 * @author normal
 */
public final class NetworkDescriptor extends Descriptor {

    public NetworkDescriptor(Descriptor descriptor) {
        super(descriptor);
    }

    /**
     * char（文字符号）： これは８ビットのフィールドである。一連の文字符号フィールドでは、 NIT
     * で通知される分配システムの名称が記述される。文書情報の符号化に関しては、付録 Ａを参照。
     *
     * @return 文字符号
     */
    public synchronized byte[] getChar_byte() {
        return this.getDescriptor_Body();
    }

    /**
     * @return 文字符号のデコード結果
     */
    public synchronized String getChar_String() {
        return Aribstr.AribToString(this.getDescriptor_Body());
    }

    @Override
    public String toString() {
        return super.toString() + "\n+"
                + "ネットワーク名記述子 文字符号 = " + Hex.encodeHexString(this.getChar_byte()) + "\n"
                + "ネットワーク名記述子 = " + this.getChar_String() + "\n";
    }

}
