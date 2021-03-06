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
package libepg.epg.section;

import java.text.MessageFormat;
import libepg.epg.section.TABLE_ID;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * セクション本体部分のバイト列を保管する。
 *
 * @author normal
 */
public class SectionBody {

    private final TABLE_ID tableID;

    private final ByteDataBlock data;

    /**
     * 基底クラスから生成する。具体的なフィールドを定義したサブクラスの生成で使用する。
     *
     * @param sectionBody 基底クラス
     */
    public SectionBody(SectionBody sectionBody) {
        this.tableID = sectionBody.tableID;
        this.data = sectionBody.data;
        if (this.getClass() != this.tableID.getDataType()) {
            MessageFormat msg1 = new MessageFormat("このクラスでは渡されたデータをパースできません。このクラスの型={0} データの型={1} データ={2}");
            Object[] parameters1 = {this.getClass(), this.tableID.getDataType(), this.data};
            throw new IllegalArgumentException(msg1.format(parameters1));
        }
    }

    /**
     * データとテーブルIDから生成する。Sectionオブジェクトのみが使用する。
     *
     * @param tableID セクションのテーブルID
     * @param data データ
     */
    SectionBody(TABLE_ID tableID, byte[] data) {
        this.tableID = tableID;
        this.data = new ByteDataBlock(data);
    }

    /**
     * このクラスが管理するデータが存在していたセクションのテーブルIDを取得する。
     *
     * @return テーブルID
     */
    public TABLE_ID getTableID() {
        return tableID;
    }

    /**
     * セクション本体のバイト列を取得する。
     *
     * @return セクション本体
     */
    public final synchronized byte[] getData() {
        return this.data.getData();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(3, 73, this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    private static final String TITLE = "セクション本体";
    private static final MessageFormat SECTION_BODY_DESC = new MessageFormat(
            "\n" + TITLE + " テーブル識別値 = {0}\n"
            + TITLE + " バイト列 = {1}\n"
    );

    @Override
    public String toString() {
        Object[] parameters = {this.getTableID(),
            this.data.toString()};
        return SECTION_BODY_DESC.format(parameters);
    }

}
