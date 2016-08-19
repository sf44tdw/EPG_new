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

import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.SectionBody;
import libepg.epg.section.TABLE_ID;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 1個以上のテーブルIDとセクションを1つ受け取り、そこから必要な情報を集めて、T型のオブジェクトのリストとして返す。
 *
 * @author normal
 * @param <T> s最終的に生成するデータの型
 */
public abstract class AbstractDataExtractor<T extends DataObject> {

    protected static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    private final Set<TABLE_ID> tableIds;

    private final Set<T> dataSet = Collections.synchronizedSet(new HashSet<>());

    /**
     * サブクラス用コンストラクタ。<br>
     * サブクラス側でテーブルIDを指定するとよい<br>
     *
     * @param tableId 処理対象のセクションを規定するテーブルID
     * @param tablsIds 処理対象のセクションを規定するテーブルID
     * @throws IllegalArgumentException　<br>
     * 1:テーブル識別値がない。
     */
    protected AbstractDataExtractor(TABLE_ID tableId, TABLE_ID... tablsIds) throws IllegalArgumentException {

        Set<TABLE_ID> tidset = Collections.synchronizedSet(new HashSet<>());
        if (tableId == null) {
            throw new IllegalArgumentException("テーブルIDの指定がありません。");
        }
        tidset.add(tableId);

        int l = tablsIds.length;
        for (int x = 0; x < l; x++) {
            TABLE_ID y = tablsIds[x];
            if (y != null) {
                tidset.add(y);
            } else {
                LOG.error("テーブルIDの指定にnullが含まれています。nullの項目は無視されます。");
            }
        }
        this.tableIds = Collections.unmodifiableSet(tidset);

    }

    /**
     * @return 処理対象にするテーブルIDの一覧。
     */
    public Set<TABLE_ID> getTableIds() {
        return tableIds;
    }

    protected final Set<T> getDataSet() {
        return dataSet;
    }

    public final Set<T> getUnmodifiableDataSet() {
        return Collections.unmodifiableSet(this.getDataSet());
    }

    public final void clearDataSet() {
        this.dataSet.clear();
    }

    /**
     * セクションに対するチェックを行う。
     *
     * @param s セクション
     * @throws IllegalArgumentException　<br>
     * 1:事前に設定されたテーブル識別値がセクションから取得した値と相違している。場合<br>
     * 2:セクションでCRCエラーが起きている場合。<br>
     */
    protected final void checkSection(Section s) throws IllegalArgumentException {
        final String hexDump = Hex.encodeHexString(s.getData());
        if (s.checkCRC() != Section.CRC_STATUS.NO_CRC_ERROR) {
            throw new IllegalArgumentException("CRCエラーです。 セクション = " + hexDump);
        }
        if (!this.tableIds.contains(s.getTable_id_const())) {
            StringBuilder sb = new StringBuilder();
            sb.append("処理対象のテーブルIDではありません。\n");
            sb.append("処理対象のテーブルID\n");
            for (TABLE_ID id : this.tableIds) {
                sb.append(id).append("\n");
            }
            sb.append("セクションのテーブルID = ").append(s.getTable_id_const()).append("\n");
            sb.append("セクション = ").append(hexDump).append("\n");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /**
     * 例外条件参照
     *
     * @throws IllegalArgumentException テーブルIDのデータ型指定とセクション本体のデータ型が異なっている場合。
     */
    protected final void checkSectionBodyType(Section s) throws IllegalArgumentException {
        SectionBody b = s.getSectionBody();
        if (b.getClass() != s.getTable_id_const().getDataType()) {
            //まずありえないのでテストケースにはしない。
            throw new IllegalStateException("テーブルIDのデータ型指定とセクション本体のデータ型が異なっています。テーブルID = " + s.getClass() + " セクション本体 = " + b.getClass() + " セクションのデータ = " + Hex.encodeHexString(s.getData()));
        }
    }

    /**
     * セクションから必要な情報を集めて専用オブジェクトに格納し、そのリストを作って返す
     *
     * @param s
     */
    public abstract void makeDataSet(Section s) throws IllegalStateException;
}
