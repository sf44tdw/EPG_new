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
package libepg.epg.section.body.networkinformationtable;

import loggingsupport.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import libepg.common.section.nit.Nit;
import libepg.epg.section.Section;
import libepg.epg.section.descriptor.DescriptorsLoop;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
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
public class NetworkInformationTableBodyTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, NetworkInformationTableBody.CLASS_LOG_OUTPUT_MODE).getLOG();
    }
    private final Section NitSection;
    private final NetworkInformationTableBody target;

    public NetworkInformationTableBodyTest() throws DecoderException {
        NitSection = Nit.getNit();
        target = (NetworkInformationTableBody) this.NitSection.getSectionBody();
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
     * Test of getNetwork_id method, of class NetworkInformationTableBody.
     */
    @Test
    public void testGetNetwork_id() {
        LOG.info("getNetwork_id");
        NetworkInformationTableBody instance = target;
        int expResult = 0x4;
        int result = instance.getNetwork_id();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReserved1 method, of class NetworkInformationTableBody.
     */
    @Test
    public void testGetReserved1() {
        LOG.info("getReserved1");
        NetworkInformationTableBody instance = target;
        int expResult = 3;
        int result = instance.getReserved1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getVersion_number method, of class NetworkInformationTableBody.
     */
    @Test
    public void testGetVersion_number() {
        LOG.info("getVersion_number");
        NetworkInformationTableBody instance = target;
        int expResult = 3;
        int result = instance.getVersion_number();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCurrent_next_indicator method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetCurrent_next_indicator() {
        LOG.info("getCurrent_next_indicator");
        NetworkInformationTableBody instance = target;
        int expResult = 1;
        int result = instance.getCurrent_next_indicator();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSection_number method, of class NetworkInformationTableBody.
     */
    @Test
    public void testGetSection_number() {
        LOG.info("getSection_number");
        NetworkInformationTableBody instance = target;
        int expResult = 0;
        int result = instance.getSection_number();
        assertEquals(expResult, result);
    }

    /**
     * Test of getLast_section_number method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetLast_section_number() {
        LOG.info("getLast_section_number");
        NetworkInformationTableBody instance = target;
        int expResult = 0;
        int result = instance.getLast_section_number();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReserved_future_use1 method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetReserved_future_use() {
        LOG.info("getReserved_future_use");
        NetworkInformationTableBody instance = target;
        int expResult = 15;
        int result = instance.getReserved_future_use1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescriptors_loop_length method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetDescriptors_loop_length() {
        LOG.info("getDescriptors_loop_length");
        NetworkInformationTableBody instance = target;
        int expResult = 18;
        int result = instance.getDescriptors_loop_length();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDescriptors_loop method, of class NetworkInformationTableBody.
     */
    @Test
    public void testGetDescriptors_loop() throws DecoderException {
        LOG.info("getDescriptors_loop");
        NetworkInformationTableBody instance = target;
        DescriptorsLoop expResult = new DescriptorsLoop(Hex.decodeHex("400c0e894253204469676974616cfe020201".toCharArray()));
        DescriptorsLoop result = instance.getDescriptors_loop();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransport_stream_loop_length method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetTransport_stream_loop_length() {
        LOG.info("getTransport_stream_loop_length");
        NetworkInformationTableBody instance = target;
        int expResult = 804;
        int result = instance.getTransport_stream_loop_length();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransport_streams_loop method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetTransport_streams_loop() throws DecoderException {
        LOG.info("getTransport_streams_loop");
        NetworkInformationTableBody instance = target;
        List<TransportStreamLoop> expResult = new ArrayList<>();
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40100004f024411500970100980100990102f1c002f3c002f4c002f5c0430b011727481100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40110004f021411200a10100a20100a30100a9a102fec00300c0430b011727481100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40300004f018410900bf010317c00318c0430b011765841100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40310004f021411200ab0100ac0100ad0100b3a10309c0030ac0430b011765841100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40900004f012410300d301430b011880921100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40910004f015410600c8010320c0430b011880921100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40920004f012410300de01430b011880921100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40d00004f0244115008d01008e01008f010090a102e8c002e9c002eac0430b011957641100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40d10004f024411500b50100b60100b70100bca100bda1030cc0030dc0430b011957641100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40f10004f021411200650100660102bcc002bdc002c3c003a1a4430b011996001100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("40f20004f0184109006701006801038ec0430b011996001100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("44500004f012410300c001430b011804201100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("44510004f012410300c101430b011804201100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("44700004f015410600c90100ca01430b011842561100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("46710004f012410300ec01430b011842561100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("46720004f0124103010001430b011842561100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("46b00004f012410300ee01430b011919281100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("46b10004f018410900f1010348c00349c0430b011919281100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("46b20004f01b410c00e70100e80100e901021302430b011919281100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47300004f012410300ea01430b012072721100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47310004f012410300f201430b012072721100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47320004f012410300f301430b012072721100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47500004f012410300fc01430b012111081100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47510004f012410300f401430b012111081100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47520004f012410300f501430b012111081100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47700004f012410300fb01430b012149441100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47710004f012410300ff01430b012149441100e802886008".toCharArray())));
        expResult.add(new TransportStreamLoop(Hex.decodeHex("47720004f0124103010201430b012149441100e802886008".toCharArray())));
        List<TransportStreamLoop> result = instance.getTransport_streams_loop();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class NetworkInformationTableBody.
     */
    @Test
    public void testToString() {
        LOG.info("toString");
        NetworkInformationTableBody instance = target;
        LOG.info(target);
    }

    /**
     * Test of getReserved_future_use1 method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetReserved_future_use1() {
        LOG.info("getReserved_future_use1");
        NetworkInformationTableBody instance = target;
        int expResult = 15;
        int result = instance.getReserved_future_use1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReserved_future_use2 method, of class
     * NetworkInformationTableBody.
     */
    @Test
    public void testGetReserved_future_use2() {
        LOG.info("getReserved_future_use2");
        NetworkInformationTableBody instance = target;
        int expResult = 15;
        int result = instance.getReserved_future_use2();
        assertEquals(expResult, result);
    }

    @Test
    public void x() throws DecoderException {
        Nit.gen();
    }
}
