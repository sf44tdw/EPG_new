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
package libepg.ts.reader;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libepg.common.tsfile.TsFile;
import libepg.ts.packet.RESERVED_PROGRAM_ID;
import libepg.ts.packet.TsPacketParcel;
import epgtools.loggerfactory.LoggerFactory;
import org.apache.commons.logging.Log;
import static org.hamcrest.CoreMatchers.notNullValue;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeThat;
import org.junit.Rule;
import testtool.testrule.regexmessagerule.ExpectedExceptionMessage;
import testtool.testrule.regexmessagerule.ExpectedExceptionRule;

/**
 *
 * @author normal
 */
public class TsReaderTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TsReader.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    @Rule
    public ExpectedExceptionRule rule = new ExpectedExceptionRule();

    private final Set<Integer> pids;

    public TsReaderTest() {
        pids = new HashSet<>();
        pids.addAll(RESERVED_PROGRAM_ID.SDT_OR_BAT.getPids());
        pids.addAll(RESERVED_PROGRAM_ID.EIT_GR_ST.getPids());

    }

    private final File TARGET_TS_FILE = TsFile.getTsFile();

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        LOG.info("tsファイルが無い場合、このテストは実施できない。");
    }

    @After
    public void tearDown() {
    }

    private TsReader limittedWrapper(File TSFile, Set<Integer> pids, Long readLimit) throws FileNotFoundException {
        TsReader ret = new TsReader(TSFile, pids, readLimit);
        return ret;
    }

    private TsReader eofWrapper(File TSFile, Set<Integer> pids) throws FileNotFoundException {
        return limittedWrapper(TSFile, pids, null);
    }

    @Test
    @ExpectedExceptionMessage("TSファイルが見つかりません。")
    public void noFile() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("noFile");
        TsReader instance = new TsReader(new File("aaa.ts"), this.pids);
    }

    @Test
    @ExpectedExceptionMessage("プログラムID(PID)の指定がありません。")
    public void nopid_null() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("nopid_null");
        TsReader instance = eofWrapper(TARGET_TS_FILE, null);
    }

    @Test
    @ExpectedExceptionMessage("プログラムID(PID)の指定がありません。")
    public void nopid_notNull() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("nopid_notNull");
        TsReader instance = eofWrapper(TARGET_TS_FILE, new HashSet<>());
    }

    @Test
    @ExpectedExceptionMessage("読み込みパケット数の最大値が0未満です。")
    public void limit_less_than_0() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("limit_less_than_0");
        TsReader instance = limittedWrapper(TARGET_TS_FILE, this.pids, -1L);
    }

    /**
     * Test of isReadEOF method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testIsReadEOF_true() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("isReadEOF_true");
        TsReader instance = this.eofWrapper(TARGET_TS_FILE, this.pids);
        boolean expResult = true;
        boolean result = instance.isReadEOF();
        assertEquals(expResult, result);
    }

    /**
     * Test of isReadEOF method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testIsReadEOF_false() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("isReadEOF_false");
        TsReader instance = this.limittedWrapper(TARGET_TS_FILE, this.pids, 2000L);
        boolean expResult = false;
        boolean result = instance.isReadEOF();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReadLimit method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testGetReadLimit() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("getReadLimit");
        TsReader instance = this.limittedWrapper(TARGET_TS_FILE, this.pids, 2000L);
        long expResult = 2000L;
        long result = instance.getReadLimit();
        assertEquals(expResult, result);

    }

    /**
     * Test of getReadLimit method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    @ExpectedExceptionMessage("EOFまで読み込む設定になっています。")
    public void testGetReadLimit_readEOF() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.info("getReadLimit_readEOF");
        TsReader instance = this.eofWrapper(TARGET_TS_FILE, this.pids);
        long l = instance.getReadLimit();
    }

    /**
     * Test of getPackets method, of class TsReader.
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testGetPackets_NoLimit() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.debug("getPackets_noLimit");
        TsReader instance = this.eofWrapper(TARGET_TS_FILE, this.pids);
        this.read(instance);

    }

    /**
     * Test of getPackets method, of class TsReader.
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testGetPackets_Limit() throws FileNotFoundException {
        assumeThat(TARGET_TS_FILE, notNullValue());
        LOG.debug("getPackets_Limit");
        TsReader instance = this.limittedWrapper(TARGET_TS_FILE, this.pids, 500L);
        this.read(instance);
    }

    private void read(TsReader instance) {
        LOG.debug("getPackets_正常系");
        Map<Integer, List<TsPacketParcel>> ss = instance.getPackets();
        for (Integer key : ss.keySet()) {
            List<TsPacketParcel> l = ss.get(key);
            int x = 0;
            Iterator<TsPacketParcel> it = l.iterator();
            while (it.hasNext()) {
                TsPacketParcel pk = it.next();
                LOG.debug("***********************************************************************************************************************************");
                LOG.debug(pk.toString());
                LOG.debug("***********************************************************************************************************************************");

                x++;
            }
        }
    }

}
