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
import java.util.List;
import libepg.common.packet.TestPacket_1000P;
import static libepg.ts.aligner.Alligner2.CLASS_LOG_OUTPUT_MODE;
import libepg.ts.packet.RESERVED_PROGRAM_ID;
import libepg.ts.packet.TsPacketParcel;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.codec.DecoderException;
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
public class Alligner2Test {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Alligner2.CLASS_LOG_OUTPUT_MODE).getLOG();
        if (CLASS_LOG_OUTPUT_MODE == false) {
            LOG.info("チェックログ抑止中。");
        }
    }
    private final TestPacket_1000P ps;

    public Alligner2Test() throws DecoderException {
        ps = new TestPacket_1000P();
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
     * Test of getPid method, of class Alligner2.
     */
    @Test
    public void testGetPid() throws DecoderException {
        LOG.info("getPid");
        Alligner2 instance = new Alligner2(0x21, ps.getPacketList());
        int expResult = 0x21;
        int result = instance.getPid();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAllignedPackets method, of class Alligner2.
     */
    @Test
    public void b2() throws DecoderException {
        LOG.info("b2");
        int pid = 0x12;
        Alligner2 instance = new Alligner2(pid, ps.getPacketList());
        List<TsPacketParcel> expResult = null;
        List<TsPacketParcel> result = instance.getAllignedPackets();
    }

//    /**
//     * Test of getAllignedPackets method, of class Alligner2.
//     */
//    @Test
//    public void testGetAllignedPackets() {
//        LOG.info("getAllignedPackets");
//        Alligner2 instance = null;
//        List<TsPacketParcel> expResult = null;
//        List<TsPacketParcel> result = instance.getAllignedPackets();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
