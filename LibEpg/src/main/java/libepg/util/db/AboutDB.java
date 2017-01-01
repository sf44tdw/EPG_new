/*
 * Copyright (C) 2017 normal
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
package libepg.util.db;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import libepg.ts.packet.TsPacket;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;

/**
 *
 * @author normal
 */
public final class AboutDB {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, AboutDB.CLASS_LOG_OUTPUT_MODE).getLOG();
        if (CLASS_LOG_OUTPUT_MODE == false) {
            LOG.info("チェックログ抑止中。");
        }
    }

 

    public AboutDB() {
    }

    /**
     * SqLitをインメモリDBとして接続する。(コネクションをクローズすると消える。)
     */
    public static final String URL = "jdbc:sqlite::memory:";

    /**
     * 作業テーブル名
     */
    public static final String TABLE_NAME = "temp_packet_table";

    /**
     * 作業テーブルの項目
     */
    public static final String NUMBER = "number";
    /**
     * 作業テーブルの項目
     */
    public static final String PID = "pid";
    /**
     * 作業テーブルの項目
     */
    public static final String CONTINUITY_CONTROL = "continuity_control";
    /**
     * 作業テーブルの項目
     */
    public static final String LACKFLAG = "lackflag";
    /**
     * 作業テーブルの項目
     */
    public static final String PACKET = "packet";
    /**
     * 作業テーブルの生成SQL
     */
    public static final String CRATE_TABLE = "create table " + TABLE_NAME + " "
            + "( "
            + NUMBER + "  integer PRIMARY KEY AUTOINCREMENT, "
            + PID + " integer NOT NULL, "
            + CONTINUITY_CONTROL + " integer NOT NULL, "
            + LACKFLAG + " integer NOT NULL, "
            + PACKET + " blob NOT NULL"
            + ")";

    /**
     * 作業テーブルオートインクリメントリセット用
     */
    public static final String RESET_AUTOINCREMENT = "delete from sqlite_sequence where name=" + TABLE_NAME;

    /**
     * 作業テーブルへの追加用SQL<br>
     * Int Pid <br>
     * Int Continuity_counter<br>
     * Int lackflag<br>
     * Bytes packet<br>
     */
    public static final String INSERT_SQL = "insert into "
            + TABLE_NAME
            + " ("
            + PID + ","
            + CONTINUITY_CONTROL + ","
            + LACKFLAG + ","
            + PACKET
            + ")"
            + " values (?,?,?,?)";

    /**
     * 作業テーブル内全レコード削除SQL
     */
    public static final String DELETE_ALL_SQL = "delete from " + TABLE_NAME;

        /**
     * 接続先のDBにテーブルを作ってリストの内容を転記する。
     * @param src 転記元
     * @param conn DBへの接続。
     * @throws java.sql.SQLException 
     * @see libepg.util.db.AboutDB#CRATE_TABLE
     * @see java.sql.Connection#createStatement() 
     */
    public static synchronized void convertToTable(List<TsPacket>src,Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        //テーブル作成
        stmt.executeUpdate(AboutDB.CRATE_TABLE);
        //特定のPIDのパケットだけ抽出してテーブルに追加。
        for (TsPacket tsp : src) {
            PreparedStatement insertStatement = conn.prepareStatement(INSERT_SQL);
            insertStatement.setInt(1, tsp.getPid());
            insertStatement.setInt(2, tsp.getContinuity_counter());
            insertStatement.setInt(3, 0);
            insertStatement.setBytes(4, tsp.getData());
            insertStatement.executeUpdate();
        }
    }
    
    
    /**
     * 作業テーブルから取得した全パケットをリストにして返す。 追加した順に格納する。
     *
     * @param conn 作業テーブルを持つDBへの接続。
     * @return パケットのリスト
     * @throws java.sql.SQLException
     * @see libepg.util.db.AboutDB#CRATE_TABLE
     */
    public static synchronized List<TsPacket> convertToList(Connection conn) throws SQLException {
        final String DUMP = "SELECT " + PACKET + " FROM " + TABLE_NAME + " ORDER BY number ASC";
        ResultSet rs = conn.createStatement().executeQuery(DUMP);

        //データ格納用
        List<TsPacket> packets = new ArrayList<>();
        while (rs.next()) {
            TsPacket tsp = new TsPacket(rs.getBytes(PACKET));
            packets.add(tsp);
        }
        return packets;
    }

    /**
     * 作業テーブルダンプ用(デバッグ時のみ)
     *
     * @param conn 作業テーブルを持つDBへの接続。
     * @throws java.sql.SQLException
     */
    public static void debug_dump_table(Connection conn) throws SQLException {
        if (LOG.isDebugEnabled()) {
            final String DUMP = "SELECT * FROM " + TABLE_NAME + " ORDER BY number ASC";
            ResultSet rs = conn.createStatement().executeQuery(DUMP);
            while (rs.next()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(NUMBER);
                sb1.append(" = ");
                sb1.append(rs.getInt(NUMBER));
                sb1.append("####");
                sb1.append(PID);
                sb1.append(" = ");
                sb1.append(rs.getInt(PID));
                sb1.append("####");
                sb1.append(CONTINUITY_CONTROL);
                sb1.append(" = ");
                sb1.append(rs.getInt(CONTINUITY_CONTROL));
                sb1.append("####");
                sb1.append(LACKFLAG);
                sb1.append(" = ");
                sb1.append(rs.getInt(LACKFLAG));
                sb1.append("####");
                sb1.append(PACKET);
                sb1.append(" = ");
                sb1.append(Hex.encodeHexString(rs.getBytes(PACKET)));
                LOG.debug(sb1.toString());
            }
        }
    }
}
