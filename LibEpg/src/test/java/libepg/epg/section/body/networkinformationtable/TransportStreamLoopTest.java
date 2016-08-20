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

import epgtools.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
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
import testtool.EqualsChecker;

/**
 *
 * @author normal
 */
public class TransportStreamLoopTest {


    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TransportStreamLoop.CLASS_LOG_OUTPUT_MODE).getLOG();
    }
    
    private final byte[] data1;
    private final byte[] data2;
    private final TransportStreamLoop target1;
    private final TransportStreamLoop target1_2;
    private final TransportStreamLoop target2;

    public TransportStreamLoopTest() throws DecoderException {
        data1 = Hex.decodeHex("40100004f024411500970100980100990102f1c002f3c002f4c002f5c0430b011727481100e802886008".toCharArray());
        data2 = Hex.decodeHex("40110004f021411200a10100a20100a30100a9a102fec00300c0430b011727481100e802886008".toCharArray());
        target1 = new TransportStreamLoop(this.data1);
        this.target1_2 = new TransportStreamLoop(this.data1);
        target2 = new TransportStreamLoop(this.data2);
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
     * Test of getData method, of class TransportStreamLoop.
     */
    @Test
    public void testGetData() {
        LOG.info("getData");
        TransportStreamLoop instance = target1;
        byte[] expResult = this.data1;
        byte[] result = instance.getData();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getTransport_stream_id method, of class TransportStreamLoop.
     */
    @Test
    public void testGetTransport_stream_id() {
        LOG.info("getTransport_stream_id");
        TransportStreamLoop instance = target1;
        int expResult = 0x4010;
        int result = instance.getTransport_stream_id();
        assertEquals(expResult, result);

    }

    /**
     * Test of getOriginal_network_id method, of class TransportStreamLoop.
     */
    @Test
    public void testGetOriginal_network_id() {
        LOG.info("getOriginal_network_id");
        TransportStreamLoop instance = target1;
        int expResult = 0x4;
        int result = instance.getOriginal_network_id();
        assertEquals(expResult, result);

    }

    /**
     * Test of getReserved_future_use method, of class TransportStreamLoop.
     */
    @Test
    public void testGetReserved_future_use() {
        LOG.info("getReserved_future_use");
        TransportStreamLoop instance = target1;
        int expResult = 0xf;
        int result = instance.getReserved_future_use();
        assertEquals(expResult, result);

    }

    /**
     * Test of getDescriptors_loop_length method, of class TransportStreamLoop.
     */
    @Test
    public void testGetDescriptors_loop_length() {
        LOG.info("getDescriptors_loop_length");
        TransportStreamLoop instance = target1;
        int expResult = 0x24;
        int result = instance.getDescriptors_loop_length();
        assertEquals(expResult, result);

    }

    /**
     * Test of getDescriptors_loop method, of class TransportStreamLoop.
     */
    @Test
    public void testGetDescriptors_loop() throws DecoderException {
        LOG.info("getDescriptors_loop");
        TransportStreamLoop instance = target1;
        DescriptorsLoop expResult = new DescriptorsLoop(Hex.decodeHex("411500970100980100990102f1c002f3c002f4c002f5c0430b011727481100e802886008".toCharArray()));
        DescriptorsLoop result = instance.getDescriptors_loop();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class TransportStreamLoop.
     */
    @Test
    public void testHashCode() {
        LOG.info("hashCode");
        TransportStreamLoop instance1 = target1;
        TransportStreamLoop instance2 = this.target1_2;
        TransportStreamLoop instance3 = target2;
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertTrue(instance1.hashCode() != instance3.hashCode());

    }

    /**
     * Test of equals method, of class TransportStreamLoop.
     */
    @Test
    public void testEquals() {
        LOG.info("equals");
        TransportStreamLoop instance1 = target1;
        TransportStreamLoop instance2 = this.target1_2;
        TransportStreamLoop instance3 = target2;
        EqualsChecker<TransportStreamLoop> ec = new EqualsChecker<>();
        assertTrue(ec.check_same(instance1, instance1, instance2));
        assertEquals(true, ec.check_not_same(instance1, instance2, instance3));
    }

    /**
     * Test of toString method, of class TransportStreamLoop.
     */
    @Test
    public void testToString() {
        LOG.info("toString");
        TransportStreamLoop instance = target1;
        LOG.info(instance);
    }

}
