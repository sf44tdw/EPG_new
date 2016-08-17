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

import static common.TestChannel.*;
import static common.TestKeyFields.a;
import static common.TestKeyFields.b;
import static common.TestKeyFields.c;
import java.lang.invoke.MethodHandles;
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

/**
 *
 * @author normal
 */
public class ChannelTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    @Rule
    public ExpectedExceptionRule rule = new ExpectedExceptionRule();

    public ChannelTest() {
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
    @ExpectedExceptionMessage("サービス名がnullもしくは空文字列です。")
    public void testDispNull() {
        LOG.info("");
        Channel test = new Channel(100, 100, 100, null);
    }

    @Test
    @ExpectedExceptionMessage("サービス名がnullもしくは空文字列です。")
    public void testDispEmpty() {
        LOG.info("");
        Channel test = new Channel(100, 100, 100, "");
    }

    /**
     * Test of getTransport_stream_id method, of class Channel.
     */
    @Test
    public void testGetTransport_stream_id() {
        LOG.info("getTransport_stream_id");
        Channel instance = CH_1;
        int expResult = a;
        int result = instance.getTransport_stream_id();
        assertEquals(expResult, result);
    }

    /**
     * Test of getOriginal_network_id method, of class Channel.
     */
    @Test
    public void testGetOriginal_network_id() {
        LOG.info("getOriginal_network_id");
        Channel instance = CH_1;
        int expResult = b;
        int result = instance.getOriginal_network_id();
        assertEquals(expResult, result);
    }

    /**
     * Test of getService_id method, of class Channel.
     */
    @Test
    public void testGetService_id() {
        LOG.info("getService_id");
        Channel instance = CH_1;
        int expResult = c;
        int result = instance.getService_id();
        assertEquals(expResult, result);
    }

    /**
     * Test of getDisplay_name method, of class Channel.
     */
    @Test
    public void testGetDisplay_name() {
        LOG.info("getDisplay_name");
        Channel instance = CH_1;
        String expResult = s;
        String result = instance.getDisplay_name();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class Channel.
     */
    @Test
    public void testHashCode() {
        LOG.info("hashCode");
        assertEquals(CH_1.hashCode(), CH_1.hashCode());
        assertTrue(CH_1.hashCode() != CH_4.hashCode());
    }

    /**
     * Test of equals method, of class Channel.
     */
    @Test
    public void testEquals_same() {
        LOG.info("");
        EqualsChecker<Channel> eqc = new EqualsChecker<>();
        assertTrue(eqc.check_same(CH_1, CH_1, CH_2_EQUALS_CH_1));
    }

    /**
     * Test of equals method, of class Channel.
     */
    @Test
    public void testEquals_notSame() {
        LOG.info("");
        EqualsChecker<Channel> eqc = new EqualsChecker<>();
        assertTrue(eqc.check_not_same(CH_1, CH_2_EQUALS_CH_1, CH_3));
    }

    /**
     * Test of toString method, of class Channel.
     */
    @Test
    public void testToString() {
        LOG.info("toString");
        System.out.println(CH_4);
    }

}
