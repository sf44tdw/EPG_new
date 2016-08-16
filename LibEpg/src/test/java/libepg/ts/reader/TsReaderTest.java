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
import libepg.ts.packet.PROGRAM_ID;
import libepg.ts.packet.TsPacketParcel;
import epgtools.loggerfactory.LoggerFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
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
public class TsReaderTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TsReader.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final Set<Integer> pids;

    public TsReaderTest() {
        pids = new HashSet();
        pids.addAll(PROGRAM_ID.SDT_OR_BAT.getPids());
        pids.addAll(PROGRAM_ID.EIT_GR_ST.getPids());

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

    private TsReader limittedWrapper(File TSFile, Set<Integer> pids, Long readLimit) throws FileNotFoundException {
        LOG.info("このテストは実際のtsファイルが必要なため、ファイルが無い場合は実施しないこと。");
        try {
            return new TsReader(TSFile, pids, readLimit);
        } catch (FileNotFoundException ex) {
            LOG.fatal("tsファイルが無いため、このテストは実施できません。", ex);
            throw ex;
        }
    }

    private TsReader eofWrapper(File TSFile, Set<Integer> pids) throws FileNotFoundException {
        return limittedWrapper(TSFile, pids, null);
    }

    @Test(expected = FileNotFoundException.class)
    public void noFile() throws FileNotFoundException {
        LOG.info("noFile");
        TsReader instance = new TsReader(new File("aaa.ts"), this.pids);
    }

    @Test(expected = IllegalArgumentException.class)
    public void nopid_null() {
        try {
            LOG.info("nopid_null");
            TsReader instance = eofWrapper(TsFile.getTsFile(), null);
        } catch (FileNotFoundException ex) {

        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void nopid_notNull() {
        try {
            LOG.info("nopid_notNull");
            TsReader instance = eofWrapper(TsFile.getTsFile(), new HashSet<>());
        } catch (FileNotFoundException ex) {

        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void limit_less_than_0() {
        try {
            LOG.info("limit_less_than_0");
            TsReader instance = limittedWrapper(TsFile.getTsFile(), this.pids, -1L);
        } catch (FileNotFoundException ex) {

        }
    }

    /**
     * Test of isReadEOF method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testIsReadEOF_true() {
        try {
            LOG.info("isReadEOF_true");
            TsReader instance = this.eofWrapper(TsFile.getTsFile(), this.pids);
            boolean expResult = true;
            boolean result = instance.isReadEOF();
            assertEquals(expResult, result);
        } catch (FileNotFoundException ex) {

        }
    }

    /**
     * Test of isReadEOF method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testIsReadEOF_false() {
        try {
            LOG.info("isReadEOF_false");
            TsReader instance = this.limittedWrapper(TsFile.getTsFile(), this.pids, 2000L);
            boolean expResult = false;
            boolean result = instance.isReadEOF();
            assertEquals(expResult, result);
        } catch (FileNotFoundException ex) {

        }
    }

    /**
     * Test of getReadLimit method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test
    public void testGetReadLimit() {
        try {
            LOG.info("getReadLimit");
            TsReader instance = this.limittedWrapper(TsFile.getTsFile(), this.pids, 2000L);
            long expResult = 2000L;
            long result = instance.getReadLimit();
            assertEquals(expResult, result);
        } catch (FileNotFoundException ex) {

        }
    }

    /**
     * Test of getReadLimit method, of class TsReader.
     *
     * @throws java.io.FileNotFoundException
     */
    @Test(expected = NullPointerException.class)
    public void testGetReadLimit_readEOF() {
        try {
            LOG.info("getReadLimit_readEOF");
            TsReader instance = this.eofWrapper(TsFile.getTsFile(), this.pids);
            long l = instance.getReadLimit();
        } catch (FileNotFoundException ex) {

        }
    }

    /**
     * Test of getPackets method, of class TsReader.
     */
    @Test
    public void testGetPackets_NoLimit() {
        try {
            LOG.debug("getPackets_noLimit");
            TsReader instance = this.eofWrapper(TsFile.getTsFile(), this.pids);
            this.read(instance);
        } catch (FileNotFoundException ex) {
        }
    }

    /**
     * Test of getPackets method, of class TsReader.
     */
    @Test
    public void testGetPackets_Limit() {
        try {
            LOG.debug("getPackets_Limit");
            TsReader instance = this.limittedWrapper(TsFile.getTsFile(), this.pids, 500L);
            this.read(instance);
        } catch (FileNotFoundException ex) {

        }
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
