
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
package libepg.common.tsfile;

import epgtools.loggerfactory.LoggerFactory;
import java.io.File;
import java.lang.invoke.MethodHandles;
import org.apache.commons.logging.Log;
import static org.junit.Assume.assumeTrue;

/**
 * テスト用のファイルは同梱できない。
 *
 * @author normal
 */
public class TsFile {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TsFile.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    public static final String TS_FILE_NAME = "H:/_D20160321161346P21020T90C101.ts";
//    public static final String TS_FILE_NAME = "H:/dummy_D20160321161346P21020T90C101.ts";

    /**
     * テスト用のファイルを取得する。ファイルが存在しない場合、
     * このメソッドからファイルを取得しようとしたテストの、このメソッドを最初に呼び出した場所から先はすべてスキップされる。
     *
     * @return テスト用のファイルを示すオブジェクト
     */
    public static File getTsFile() {
        File f = new File(TS_FILE_NAME);
        if (!f.isFile()) {
            LOG.error("テスト用のtsファイルがありません。このファイルを利用したテストは実施できません。パス = " + TS_FILE_NAME);
        }
        assumeTrue(f.isFile());
        return f;
    }

}
