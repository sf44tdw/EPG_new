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

import java.io.File;
import java.util.List;
import libepg.ts.packet.RESERVED_PROGRAM_ID;

/**
 * tsファイルのリストを受け取り、からEPGのデータを取り出す。
 * @author normal
 */
public class AbstractAllEpgDataExtractor {
    
    private final List<File> tsList;
    private final RESERVED_PROGRAM_ID pid;

    /**
     * @param tsList tsファイルのリスト
     * @param pid 
     */
    public AbstractAllEpgDataExtractor(List<File> tsList, RESERVED_PROGRAM_ID pid) {
        this.tsList = tsList;
        this.pid = pid;
    }
    
    
    
}
