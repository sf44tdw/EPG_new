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
package libepg.ts.aligner;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import libepg.ts.packet.TsPacket;
import libepg.ts.packet.TsPacketParcel;
import libepg.util.db.JDBCAccessor;
import libepg.util.db.AboutDB;
import static libepg.util.db.AboutDB.CRATE_TABLE;
import static libepg.util.db.AboutDB.INSERT_SQL;
import static libepg.util.db.AboutDB.PID;
import static libepg.util.db.AboutDB.debug_dump_table;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.logging.Log;

/**
 * 特定のPIDを持つパケットをリストから抽出し、下記の処理を行う。 巡回カウンタによる連送パケットの確認を行い、連送パケットを除去する。<br>
 * 巡回カウンタによる欠落チェックを行い、直前のパケットが欠落していればフラグをセットする。<br>
 * (ただし、アダプテーションフィールドのみのパケットの巡回カウンタは常に0なので、そういうパケットだけを集める場合は欠落扱いにはしない。)<br>
 *
 * @author normal
 */
public class Alligner2 {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Alligner2.CLASS_LOG_OUTPUT_MODE).getLOG();
        if (CLASS_LOG_OUTPUT_MODE == false) {
            LOG.info("チェックログ抑止中。");
        }
    }

    private final int pid;



    private final List<TsPacket> packets;

    /**
     * 抽出対象のPIDとパケットのリストを受け取る。
     *
     * @param pid 抽出対象のPID
     * @param packets パケットのリスト
     * @throws IllegalArgumentException 抽出対象のpidが想定外の値だった場合。
     */
    public Alligner2(int pid, List<TsPacket> packets) throws IllegalArgumentException {
        this.pid = pid;
        if (!TsPacket.PID_RANGE.contains(this.pid)) {
            MessageFormat msg = new MessageFormat("PID(プログラムID)が不正な値です。値={0} 下限={1} 上限={2}");
            Object[] parameters = {this.pid, TsPacket.PID_RANGE.getMinimum(), TsPacket.PID_RANGE.getMaximum()};
            throw new IllegalArgumentException(msg.format(parameters));
        }
        this.packets = new ArrayList<>();
        this.packets.addAll(packets);
    }

    public int getPid() {
        return pid;
    }

    public synchronized List<TsPacketParcel> getAllignedPackets() {
        JDBCAccessor ac = JDBCAccessor.getInstance();
        try {
            ac.connect(AboutDB.URL);
            Connection conn = ac.getConnection();

            Statement stmt = conn.createStatement();
            //テーブル作成
            stmt.executeUpdate(CRATE_TABLE);

            //特定のPIDのパケットだけ抽出してテーブルに追加。
            for (TsPacket tsp : this.packets) {
//                System.out.println(Integer.toHexString(tsp.getPid()));
                if (tsp.getPid() == this.getPid()) {
                    PreparedStatement insertStatement = conn.prepareStatement(INSERT_SQL);
                    insertStatement.setInt(1, tsp.getPid());
                    insertStatement.setInt(2, tsp.getContinuity_counter());
                    insertStatement.setInt(3, 0);
                    insertStatement.setBytes(4, tsp.getData());
                    insertStatement.executeUpdate();
                }
            }

            debug_dump_table(conn);

//            //一時記憶域
//            LinkedList<TsPacketParcel> temp = new LinkedList<>();
//            for (TsPacket tsp : this.packets) {
//
//                //アダプテーションフィールドのみのパケットの巡回カウンタは常に0
//                if (tsp.getAdaptation_field_control() == TsPacket.ADAPTATION_FIELD_CONTROL.ONLY_ADAPTATION_FIELD) {
//                    if (tsp.getContinuity_counter() == 0) {
//                        temp.add(new TsPacketParcel(tsp, TsPacketParcel.MISSING_PACKET_FLAG.NOT_MISSING));
//                    } else if (LOG.isWarnEnabled()) {
//                        LOG.warn("アダプテーションフィールドのみ、巡回カウンタ0以外のパケットを確認。このパケットは無視されます。");
//                        LOG.warn(tsp);
//                    }
//                }
//
//                if (LOG.isTraceEnabled()) {
//                    LOG.trace("巡回カウンタ範囲内でのパケット重複チェック");
//                }
//                if (temp.contains(tsp)) {
//
//                }
//            }
        } catch (SQLException ex) {
            LOG.fatal(ex);
        } finally {
            ac.close();
        }
        return null;
    }



}
