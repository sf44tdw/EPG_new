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
import java.util.Objects;
import org.apache.commons.codec.binary.Hex;
import libepg.util.bytearray.ByteDataBlock;

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
            Object[] parameters1 = {this.getClass(), this.tableID.getDataType(), Hex.encodeHexString(this.data.getData())};
            throw new IllegalArgumentException(msg1.format(parameters1));
        }
    }

    /**
     * データとテーブルIDから生成する。Sectionオブジェクトのみが使用する。
     *
     * @param tableID
     * @param data
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
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.tableID);
        hash = 73 * hash + Objects.hashCode(this.data);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SectionBody other = (SectionBody) obj;
        if (this.tableID != other.tableID) {
            return false;
        }
        if (!Objects.equals(this.data, other.data)) {
            return false;
        }
        return true;
    }

    private static final MessageFormat SECTION_BODY_DESC = new MessageFormat(
            "セクション本体 テーブル識別値 = {0}\n"
            + "セクション本体 バイト列 = {1}\n"
    );

    @Override
    public String toString() {
        Object[] parameters = {this.getTableID(),
            this.data.toString()};
        return SECTION_BODY_DESC.format(parameters);
    }

}
