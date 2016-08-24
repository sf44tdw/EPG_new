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
package libepg.ts.fileseeker;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.FalseFileFilter;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;

/*
 * 指定されたディレクトリ下のファイルをリストアップする。
 */
public class TsFileSeeker {


        private final IOFileFilter SUFFIX_1 = FileFilterUtils.suffixFileFilter("ts");
        private final IOFileFilter SUFFIX_2 = FileFilterUtils.suffixFileFilter("Ts");
        private final IOFileFilter SUFFIX_3 = FileFilterUtils.suffixFileFilter("tS");
        private final IOFileFilter SUFFIX_4 = FileFilterUtils.suffixFileFilter("TS");
        public final IOFileFilter TS_SUFFIX = FileFilterUtils.or(SUFFIX_1, SUFFIX_2, SUFFIX_3, SUFFIX_4);


    private final File SourceDir;


    private IOFileFilter dirf = FalseFileFilter.INSTANCE;

    /**
     * @param SourceDir 検索対象ディレクトリ
     */
    public TsFileSeeker(File SourceDir ) {
        this.SourceDir = new File(SourceDir.getAbsolutePath());
        if (!this.SourceDir.isDirectory()) {
            throw new IllegalArgumentException("ディレクトリではありません。パス = " + this.SourceDir.getAbsolutePath());
        }
    }

    public synchronized boolean isRecursive() {
        boolean Ret = false;
        if (this.dirf instanceof TrueFileFilter) {
            Ret = true;
        } else if (this.dirf instanceof FalseFileFilter) {
            Ret = false;
        }
        return Ret;
    }

    /**
     * サブディレクトリも検索するか。trueをセットすると検索するようになる。デフォルトはfalse
     *
     * @param recursive セット対象
     */
    public synchronized void setRecursive(boolean recursive) {
        if (recursive == false) {
            this.dirf = FalseFileFilter.INSTANCE;
        } else {
            this.dirf = TrueFileFilter.INSTANCE;
        }
    }

    /**
     * 検索を行い、その結果を返す。
     *
     * @return 見つかったファイルを示すファイルオブジェクトのリスト。
     */
    public synchronized List<File> seek() {
        List<File> list = Collections.synchronizedList(new ArrayList<File>());
        Collection<File> files = FileUtils.listFiles(this.SourceDir, this.TS_SUFFIX, this.dirf);
        list.addAll(files);
        return Collections.unmodifiableList(list);

    }
}
