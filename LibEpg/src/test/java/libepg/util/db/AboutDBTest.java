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

import java.io.File;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import libepg.common.packet.TestPacket_BitError;
import libepg.ts.packet.TsPacket;
import libepg.ts.reader.Reader2;
import static libepg.util.db.AboutDB.CLASS_LOG_OUTPUT_MODE;
import static libepg.util.db.AboutDB.debug_dump_table;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.logging.Log;
import static org.eclipse.jetty.io.nio.SelectChannelEndPoint.LOG;
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
public class AboutDBTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, AboutDB.CLASS_LOG_OUTPUT_MODE).getLOG();
        if (CLASS_LOG_OUTPUT_MODE == false) {
            LOG.info("チェックログ抑止中。");
        }
    }
    private final TestPacket_BitError be;

    public AboutDBTest() throws DecoderException {
        be = new TestPacket_BitError();
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
     * Test of convertToTable method, of class Reader2.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testConvert() throws IOException {
        LOG.info("convert");

        List<TsPacket> _expResult = be.getExpResult_Normal_And_Wrong_Sync();
        TestPacket_BitError.dumpPacketList(_expResult);

        try (JDBCAccessor ac = JDBCAccessor.getInstance()) {
            ac.connect(AboutDB.URL);
            Connection conn = ac.getConnection();
            AboutDB.convertToTable(_expResult, conn);
            debug_dump_table(conn);

            List<TsPacket> result = AboutDB.convertToList(conn);
            TestPacket_BitError.dumpPacketList(result);

            assertEquals(ListUtils.isEqualList(_expResult, result), true);
        } catch (SQLException ex) {
            LOG.fatal(ex);
            fail();
        }
    }
}
