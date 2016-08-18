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
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * セクションを1つ受け取り、そこから必要な情報を集めて、T型のオブジェクトのリストとして返す。
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
    private final Section source;
    private final TABLE_ID tableId;

    /**
     * サブクラス用コンストラクタ。<br>
     * サブクラス側で独自にセクションのみをとるpublicコンストラクタを実装することが望ましい。<br>
     *
     * @param source 処理対象のセクション
     * @param tableId 処理対象のセクションを規定するテーブルID]
     * @throws IllegalArgumentException　<br>
     * 1:テーブル識別値がセクションから取得した値と相違している。場合<br>
     * 2:テーブルでCRCエラーが起きている場合。<br>
     */
    protected AbstractDataExtractor(Section source, TABLE_ID tableId) throws IllegalArgumentException {
        this.source = source;
        this.tableId = tableId;
        final String hexDump = Hex.encodeHexString(this.source.getData());
        if (this.source.checkCRC() != Section.CRC_STATUS.NO_CRC_ERROR) {
            throw new IllegalArgumentException("CRCエラーです。 セクション = " + hexDump);
        }
        if (this.source.getTable_id_const() != TABLE_ID.SDT) {
            throw new IllegalArgumentException("テーブル識別が想定と違います。　想定値 = " + this.tableId + " 実測値 = " + this.source.getTable_id_const() + " セクション = " + hexDump);
        }
    }

    /**
     * @return セクション
     */
    public Section getSource() {
        return source;
    }

    /**
     * キー生成メソッドを持つT型オブジェクトが入ったSetの中身を、そのキー生成メソッドを使用してマップに移し替える。
     */
    protected final Map<MultiKey<Integer>, T> SetToMap(Set<T> src) {
        Map<MultiKey<Integer>, T> ret = new ConcurrentHashMap<>();
        for (T value : src) {
            ret.put(value.getMuiltiKey(), value);
        }
        return Collections.unmodifiableMap(ret);
    }

    /**
     * セクションから必要な情報を集めて専用オブジェクトに格納し、そのリストを作って返す
     *
     * @return
     * 作成されたすべてのオブジェクトについて。トランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を複合キーとして格納したマップ。
     */
    public abstract Map<MultiKey<Integer>, T> getDataList();
}
