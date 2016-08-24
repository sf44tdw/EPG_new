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
package libepg.epg.section.descriptor.contentdescriptor;

import java.lang.invoke.MethodHandles;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import loggingsupport.loggerfactory.LoggerFactory;
import testtool.EqualsChecker;

/**
 *
 * @author normal
 */
public class NibbleTest {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, NibbleTest.CLASS_LOG_OUTPUT_MODE).getLOG();
    }
    private byte[] data = {0x11, 0x11};

    public NibbleTest() {

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
//強制呼び出し

    /**
     * Test of getData method, of class Nibble.
     *
     * @throws java.lang.Throwable
     */
    @Test
    public void testGetData() throws Throwable {
        LOG.info("getData");
        Nibble instance = NIbbleMaker.init(this.data);
        byte[] expResult = this.data;
        byte[] result = instance.getData();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getContent_nibble_level_1 method, of class Nibble.
     *
     * @throws java.lang.Throwable
     */
    @Test
    public void testGetContent_nibble_level_1() throws Throwable {
        LOG.info("getContent_nibble_level_1");
        Nibble instance = NIbbleMaker.init(this.data);
        NIBBLE_LEVEL_1 expResult = NIBBLE_LEVEL_1.SPORTS;
        NIBBLE_LEVEL_1 result = instance.getContent_nibble_level_1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getContent_nibble_level_2 method, of class Nibble.
     *
     * @throws java.lang.Throwable
     */
    @Test
    public void testGetContent_nibble_level_2() throws Throwable {
        LOG.info("getContent_nibble_level_2");
        Nibble instance = NIbbleMaker.init(this.data);
        NIBBLE_LEVEL_2 expResult = NIBBLE_LEVEL_2.BASEBALL;
        NIBBLE_LEVEL_2 result = instance.getContent_nibble_level_2();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUser_nibble1 method, of class Nibble.
     *
     * @throws java.lang.Throwable
     */
    @Test
    public void testGetUser_nibble1() throws Throwable {
        LOG.info("getUser_nibble1");
        Nibble instance = NIbbleMaker.init(this.data);
        int expResult = 0x1;
        int result = instance.getUser_nibble1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUser_nibble2 method, of class Nibble.
     *
     * @throws java.lang.Throwable
     */
    @Test
    public void testGetUser_nibble2() throws Throwable {
        LOG.info("getUser_nibble2");
        Nibble instance = NIbbleMaker.init(this.data);
        int expResult = 0x1;
        int result = instance.getUser_nibble1();
        assertEquals(expResult, result);
    }

    /**
     * Test of hashCode method, of class Nibble.
     */
    @Test
    public void testHashCode() throws Throwable {
        LOG.info("hashCode");
        Nibble instance = NIbbleMaker.init(this.data);
        int expResult = 41204;
        int result = instance.hashCode();
        assertEquals(expResult, result);
    }
//
//    /**
//     * Test of equals method, of class Nibble.
//     */
//    @Test
//    public void testEquals() {
//        LOG.info("equals");
//        Object obj = null;
//        Nibble instance = null;
//        boolean expResult = false;
//        boolean result = instance.equals(obj);
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
//

    /**
     * Test of equals method, of class Descriptor.
     */
    @Test
    public void testEquals() throws Throwable {
        LOG.info("equals_ok");
        EqualsChecker<Nibble> ec = new EqualsChecker<>();

        Nibble instance1 = NIbbleMaker.init(Hex.decodeHex("82ff".toCharArray()));
        Nibble instance2 = NIbbleMaker.init(Hex.decodeHex("82ff".toCharArray()));
        Nibble instance3 = NIbbleMaker.init(Hex.decodeHex("82ff".toCharArray()));

        boolean result = ec.check_same(instance1, instance2, instance3);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Descriptor.
     */
    @Test
    public void testEquals_ng() throws DecoderException, Throwable {
        LOG.info("equals_ng");
        Nibble instance1 = NIbbleMaker.init(Hex.decodeHex("82ff".toCharArray()));
        Nibble instance2 = NIbbleMaker.init(Hex.decodeHex("81ff".toCharArray()));
        boolean result = instance1.equals(instance2);
        boolean expResult = false;
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Nibble.
     *
     * @throws java.lang.Throwable
     */
    @Test
    public void testToString() throws Throwable {
        LOG.info("toString");
        Nibble instance = NIbbleMaker.init(this.data);
        LOG.info(instance);
    }
}
