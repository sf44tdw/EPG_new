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

import epgtools.dumpepgfromts.dataextractor.channel.Channel;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import libepg.epg.section.Section;
import libepg.epg.section.SectionBody;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableBody;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.keyvalue.MultiKey;
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
    private final Section source;
    private final Set<TABLE_ID> tableIds;

    /**
     * サブクラス用コンストラクタ。<br>
     * サブクラス側で独自にセクションのみをとるpublicコンストラクタを実装することが望ましい。<br>
     *
     * @param source 処理対象のセクション
     * @param tableId 処理対象のセクションを規定するテーブルID
     * @param tablsIds 処理対象のセクションを規定するテーブルID
     * @throws IllegalArgumentException　<br>
     * 1:テーブル識別値がセクションから取得した値と相違している。場合<br>
     * 2:テーブルでCRCエラーが起きている場合。<br>
     */
    protected AbstractDataExtractor(Section source, TABLE_ID tableId, TABLE_ID... tablsIds) throws IllegalArgumentException {
        this.source = source;

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
        final String hexDump = Hex.encodeHexString(this.source.getData());
        if (this.source.checkCRC() != Section.CRC_STATUS.NO_CRC_ERROR) {
            throw new IllegalArgumentException("CRCエラーです。 セクション = " + hexDump);
        }
        if (!this.tableIds.contains(this.source.getTable_id_const())) {
            StringBuilder sb = new StringBuilder();
            sb.append("処理対象のテーブルIDではありません。\n");
            sb.append("処理対象のテーブルID\n");
            for (TABLE_ID id : this.tableIds) {
                sb.append(id).append("\n");
            }
            sb.append("セクションのテーブルID = ").append(this.source.getTable_id_const()).append("\n");
            sb.append("セクション = ").append(hexDump).append("\n");
            throw new IllegalArgumentException(sb.toString());
        }
    }

    /**
     * @return セクション
     */
    public Section getSource() {
        return source;
    }

    /**
     * @return 処理対象にするテーブルIDの一覧。
     */
    public Set<TABLE_ID> getTableIds() {
        return tableIds;
    }

    /**
     * 一時格納用セットの初期化
     *
     * @return セット
     */
    protected final Set<T> initiarizeSet() {
        if (LOG.isInfoEnabled()) {
            LOG.info("重複排除用セット作製");
        }
        return Collections.synchronizedSet(new HashSet<T>());
    }

    /**
     * キー生成メソッドを持つT型オブジェクトが入ったSetの中身を、そのキー生成メソッドを使用してマップに移し替える。
     *
     * @param src 変換元
     * @return 変換元の中身を移したマップ。キーについては自動生成される。
     */
    protected final Map<MultiKey<Integer>, T> SetToMap(Set<T> src) {
        Map<MultiKey<Integer>, T> ret = new ConcurrentHashMap<>();
        if (LOG.isDebugEnabled()) {
            LOG.debug("セットの要素数 = " + src.size());
        }
        for (T value : src) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("セットからマップへ移動したオブジェクト " + value);
            }
            ret.put(value.getMuiltiKey(), value);
        }
        return Collections.unmodifiableMap(ret);
    }

    /**
     * 例外条件参照
     *
     * @throws IllegalStateException テーブルIDのデータ型指定とセクション本体のデータ型が異なっている場合。
     */
    protected final void checkSectionBodyType() throws IllegalStateException {
        final SectionBody b = this.getSource().getSectionBody();
        if (b.getClass() != this.getSource().getTable_id_const().getDataType()) {
            //まずありえないのでテストケースにはしない。
            throw new IllegalStateException("テーブルIDのデータ型指定とセクション本体のデータ型がが異なっています。テーブルID = " + this.getSource().getClass() + " セクション本体 = " + b.getClass() + " セクションのデータ = " + Hex.encodeHexString(this.getSource().getData()));
        }
    }

    /**
     * セクションから必要な情報を集めて専用オブジェクトに格納し、そのリストを作って返す
     *
     * @return
     * 作成されたすべてのオブジェクトについて。トランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を複合キーとして格納したマップ。
     */
    public abstract Map<MultiKey<Integer>, T> getDataList() throws IllegalStateException;
}
