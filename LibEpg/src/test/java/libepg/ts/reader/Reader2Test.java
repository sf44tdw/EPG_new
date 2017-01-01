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
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import libepg.common.packet.TestPacket_BitError;
import static libepg.common.packet.TestPacket_BitError.dumpPacketList;
import libepg.ts.packet.TsPacket;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author normal
 */
public class Reader2Test {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Reader2.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final TestPacket_BitError be;

    private final List<byte[]> list_NotContainError;
    private final List<byte[]> list_ContainError_WrongSync;
    private final List<byte[]> list_ContainError_WrongLength;

    private final List<TsPacket> expResult_Normal_And_Wrong_Sync;
    private final List<TsPacket> expResult_Wrong_Length;

    public Reader2Test() throws DecoderException {
        be = new TestPacket_BitError();
        list_NotContainError = be.getList_NotContainError();
        list_ContainError_WrongSync = be.getList_ContainError_WrongSync();
        list_ContainError_WrongLength = be.getList_ContainError_WrongLength();

        expResult_Normal_And_Wrong_Sync = be.getExpResult_Normal_And_Wrong_Sync();
        expResult_Wrong_Length = be.getExpResult_Wrong_Length();
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

//    /**
//     * Test of getPackets method, of class Reader2.
//     */
//    @Test
//    public void testGetPackets() throws FileNotFoundException {
//        System.out.println("getPackets");
//        Reader2 instance = new Reader2(TsFile.getTsFile(), 23L);
//        List<TsPacket> result = instance.getPackets();
//
//        long count = 0;
//
//        for (TsPacket p : result) {
//            StringBuilder sb1 = new StringBuilder();
//            sb1.append("p_");
//            sb1.append(count);
//
//            StringBuilder sb2 = new StringBuilder();
//            sb2.append("byte[] ");
//            sb2.append(sb1.toString());
//            sb2.append(" = ");
//            sb2.append("Hex.decodeHex(\"");
//            sb2.append(Hex.encodeHexString(p.getData()));
//            sb2.append("\".toCharArray()");
//            sb2.append(");");
//            sb2.append("  list_NotContainError.add(");
//            sb2.append(sb1.toString());
//            sb2.append(");");
//            System.out.println(sb2.toString());
//            count++;
//        }
//    }


    /**
     * Test of getPackets method, of class Reader2.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPackets_Normal() throws IOException {
        LOG.info("getPackets_Normal");
        File temp_NotContainError = TestPacket_BitError.writeBytesToFile(this.list_NotContainError);
        Reader2 instance = new Reader2(temp_NotContainError);
        List<TsPacket> _expResult = this.expResult_Normal_And_Wrong_Sync;
        List<TsPacket> result = instance.getPackets();
        LOG.info("expResult");
        dumpPacketList(_expResult);
        LOG.info("result");
        dumpPacketList(result);
        assertEquals(ListUtils.isEqualList(_expResult, result), true);
    }

    /**
     * Test of getPackets method, of class Reader2.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPackets_WrongSync() throws IOException {
        LOG.info("getPackets_WrongSync");
        File temp_ContainError_WrongSync = TestPacket_BitError.writeBytesToFile(this.list_ContainError_WrongSync);
        Reader2 instance = new Reader2(temp_ContainError_WrongSync);
        List<TsPacket> _expResult = this.expResult_Normal_And_Wrong_Sync;
        List<TsPacket> result = instance.getPackets();
        LOG.info("expResult");
        dumpPacketList(_expResult);
        LOG.info("result");
        dumpPacketList(result);
        assertEquals(ListUtils.isEqualList(_expResult, result), true);
    }

    /**
     * Test of getPackets method, of class Reader2.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPackets_WrongLength() throws IOException {
        LOG.info("getPackets_WrongLength");
        File temp_ContainError_WrongLength = TestPacket_BitError.writeBytesToFile(this.list_ContainError_WrongLength);
        Reader2 instance = new Reader2(temp_ContainError_WrongLength);
        List<TsPacket> _expResult = this.expResult_Wrong_Length;
        List<TsPacket> result = instance.getPackets();
        LOG.info("expResult");
        dumpPacketList(_expResult);
        LOG.info("result");
        dumpPacketList(result);
        assertEquals(ListUtils.isEqualList(_expResult, result), true);
    }

 
}
