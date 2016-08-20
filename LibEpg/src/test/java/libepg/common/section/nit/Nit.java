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
package libepg.common.section.nit;

import libepg.epg.section.Section;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * NITのサンプル。(ch=101 bs1)
 *
 * @author normal
 */
public class Nit {

    public static final String NIT = "40f3430004c70000f012400c0e894253204469676974616cfe020201f32440100004f024411500970100980100990102f1c002f3c002f4c002f5c0430b011727481100e80288600840110004f021411200a10100a20100a30100a9a102fec00300c0430b011727481100e80288600840300004f018410900bf010317c00318c0430b011765841100e80288600840310004f021411200ab0100ac0100ad0100b3a10309c0030ac0430b011765841100e80288600840900004f012410300d301430b011880921100e80288600840910004f015410600c8010320c0430b011880921100e80288600840920004f012410300de01430b011880921100e80288600840d00004f0244115008d01008e01008f010090a102e8c002e9c002eac0430b011957641100e80288600840d10004f024411500b50100b60100b70100bca100bda1030cc0030dc0430b011957641100e80288600840f10004f021411200650100660102bcc002bdc002c3c003a1a4430b011996001100e80288600840f20004f0184109006701006801038ec0430b011996001100e80288600844500004f012410300c001430b011804201100e80288600844510004f012410300c101430b011804201100e80288600844700004f015410600c90100ca01430b011842561100e80288600846710004f012410300ec01430b011842561100e80288600846720004f0124103010001430b011842561100e80288600846b00004f012410300ee01430b011919281100e80288600846b10004f018410900f1010348c00349c0430b011919281100e80288600846b20004f01b410c00e70100e80100e901021302430b011919281100e80288600847300004f012410300ea01430b012072721100e80288600847310004f012410300f201430b012072721100e80288600847320004f012410300f301430b012072721100e80288600847500004f012410300fc01430b012111081100e80288600847510004f012410300f401430b012111081100e80288600847520004f012410300f501430b012111081100e80288600847700004f012410300fb01430b012149441100e80288600847710004f012410300ff01430b012149441100e80288600847720004f0124103010201430b012149441100e8028860082b5b8afb";

    public static Section getNit() throws DecoderException {
        return new Section(Hex.decodeHex(NIT.toCharArray()));
    }
}
