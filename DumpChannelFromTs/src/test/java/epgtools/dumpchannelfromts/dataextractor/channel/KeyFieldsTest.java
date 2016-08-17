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
package epgtools.dumpchannelfromts.dataextractor.channel;

import java.lang.invoke.MethodHandles;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import testtool.EqualsChecker;
import testtool.testrule.regexmessagerule.ExpectedExceptionMessage;
import testtool.testrule.regexmessagerule.ExpectedExceptionRule;
import static common.TestKeyFields.KEY_1;
import static common.TestKeyFields.KEY_3;
import static common.TestKeyFields.KEY_2_EQUALS_KEY_1;

/**
 *
 * @author normal
 */
public class KeyFieldsTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    @Rule
    public ExpectedExceptionRule rule = new ExpectedExceptionRule();



    public KeyFieldsTest() {
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

    @Test
    @ExpectedExceptionMessage("^.*トランスポートストリーム識別.*$")
    public void testSetTransportStreamId_0x0000() {
        LOG.info("");
        final KeyFields target = new KeyFields(0, 1, 2);
    }

    @Test
    @ExpectedExceptionMessage("^.*トランスポートストリーム識別.*$")
    public void testSetTransportStreamId_0xffff() {
        LOG.info("");
        final KeyFields target = new KeyFields(0xffff, 1, 2);
    }

    @Test
    @ExpectedExceptionMessage("^.*オリジナルネットワーク識別.*$")
    public void testSetOriginalNetworkId_less_than_zero() {
        LOG.info("");
        final KeyFields target = new KeyFields(1, -1, 2);
    }

    @Test
    @ExpectedExceptionMessage("^.*オリジナルネットワーク識別.*$")
    public void testSetOriginalNetworkId_more_than_0xffff() {
        LOG.info("");
        final KeyFields target = new KeyFields(1, 0x010000, 2);
    }

    @Test
    @ExpectedExceptionMessage("^.*サービス識別.*$")
    public void testSetServiceId_less_than_zero() {
        LOG.info("");
        final KeyFields target = new KeyFields(1, 1, -1);
    }

    @Test
    @ExpectedExceptionMessage("^.*サービス識別.*$")
    public void testSetServiceId_more_than_0xffff() {
        LOG.info("");
        final KeyFields target = new KeyFields(1, 1, 0x010000);
    }


    /**
     * Test of getTransportStreamId method, of class KeyFields.
     */
    @Test
    public void testGetTransportStreamId() {
       LOG.info("getTransportStreamId");
        KeyFields instance = KEY_1;
        int expResult = 1;
        int result = instance.getTransport_stream_id();
        assertEquals(expResult, result);

    }

    /**
     * Test of getOriginalNetworkId method, of class KeyFields.
     */
    @Test
    public void testGetOriginalNetworkId() {
       LOG.info("getOriginalNetworkId");
        KeyFields instance = KEY_1;
        int expResult = 1;
        int result = instance.getOriginal_network_id();
        assertEquals(expResult, result);

    }

    /**
     * Test of getServiceId method, of class KeyFields.
     */
    @Test
    public void testGetServiceId() {
       LOG.info("getServiceId");
        KeyFields instance = KEY_1;
        int expResult = 2;
        int result = instance.getService_id();
        assertEquals(expResult, result);
    }

    /**
     * Test of getMuiltiKey method, of class KeyFields.
     */
    @Test
    public void testGetMuiltiKey() {
       LOG.info("getMuiltiKey");
        KeyFields instance = KEY_1;
        MultiKey<Integer> expResult = new MultiKey<>(1, 1, 2);
        MultiKey<Integer> result = instance.getMuiltiKey();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class KeyFields.
     */
    @Test
    public void testHashCode() {
       LOG.info("hashCode");
        KeyFields instance1 = KEY_1;
        KeyFields instance2 = KEY_2_EQUALS_KEY_1;
        KeyFields instance3 = KEY_3;
        assertEquals(instance1.hashCode(), instance2.hashCode());
        assertTrue(!(instance1.hashCode() == instance3.hashCode()));
    }

    /**
     * Test of equals method, of class KeyFields.
     */
    @Test
    public void testEquals() {
        LOG.info("");
        EqualsChecker<KeyFields> eqc = new EqualsChecker<>();
        assertTrue(eqc.check_same(KEY_1, KEY_2_EQUALS_KEY_1, KEY_1));
    }

    /**
     * Test of equals method, of class KeyFields.
     */
    @Test
    public void testNotEquals() {
        LOG.info("");
        EqualsChecker<KeyFields> eqc = new EqualsChecker<>();
        assertTrue(eqc.check_not_same(KEY_3, KEY_1, KEY_2_EQUALS_KEY_1));
    }

    /**
     * Test of toString method, of class KeyFields.
     */
    @Test
    public void testToString() {
       LOG.info("toString");
       LOG.info(KEY_1.toString());

    }

}
