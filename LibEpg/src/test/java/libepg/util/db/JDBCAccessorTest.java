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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author normal
 */
public class JDBCAccessorTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, JDBCAccessor.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    public JDBCAccessorTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInstance method, of class JDBCAccessor.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        System.out.println("getInstance");
        JDBCAccessor result = JDBCAccessor.getInstance();
        assertNotNull(result);
        JDBCAccessor.getInstance().close();
    }

    /**
     * Test of getConnection method, of class JDBCAccessor.
     *
     */
    @Test
    public void testGetCon() {
        try {

            LOG.info("getCon");
            JDBCAccessor instance = JDBCAccessor.getInstance();
            String url = "jdbc:sqlite::memory:";
            instance.connect(url);
            java.sql.Connection result = instance.getConnection();
            assertNotNull(result);
            LOG.debug("Connected.");

            Statement stmt = result.createStatement();
            //テーブル作成
            stmt.executeUpdate("create table test1( name string, age integer )");
            LOG.debug("Made table.");

            //値を入力する
            String nameVal = "jjj888えええ";
            int ageVal = 778;
            String sql1 = "insert into test1 values (?,?)";
            PreparedStatement pstmt1 = result.prepareStatement(sql1);
            pstmt1.setString(1, nameVal);
            pstmt1.setInt(2, ageVal);
            pstmt1.executeUpdate();
            LOG.debug("Inserted.");

            //結果を表示する
            String sql2 = "select * from test1 where name=? and age=?";
            PreparedStatement pstmt2 = result.prepareStatement(sql2);
            pstmt2.setString(1, nameVal);
            pstmt2.setInt(2, ageVal);
            ResultSet rs = pstmt2.executeQuery();
            while (rs.next()) {
                String str1 = rs.getString("name");
                int int2 = rs.getInt("age");
                System.out.println(str1);
                System.out.println(int2);
                assertEquals(nameVal, str1);
                assertEquals(ageVal, int2);
            }
            LOG.debug("Got data.");

        } catch (SQLException ex) {
            LOG.fatal(ex);
            fail();
        } finally {
            JDBCAccessor.getInstance().close();
        }
    }
}
