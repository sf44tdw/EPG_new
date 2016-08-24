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
package libepg.util.bytearray;

import java.lang.invoke.MethodHandles;
import loggingsupport.loggerfactory.LoggerFactory;
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
public class ByteConverterTest {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, ByteConverterTest.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    public ByteConverterTest() {
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
     * Test of byteToInt method, of class ByteConverter.
     */
    @Test
    public void testByteToInt1() {
        LOG.info("byteToInt");
        byte src = (byte) 0xff;
        int expResult = 0xff;
        int result = ByteConverter.byteToInt(src);
        assertEquals(expResult, result);
    }

    /**
     * Test of byteToInt method, of class ByteConverter.
     */
    @Test
    public void testByteToInt2() {
        LOG.info("byteToInt");
        byte src = (byte) 0x0;
        int expResult = 0x0;
        int result = ByteConverter.byteToInt(src);
        assertEquals(expResult, result);
    }

    /**
     * Test of bytesToInt method, of class ByteConverter.
     */
    @Test
    public void testBytesToInt1() {
        LOG.info("bytesToInt");
        byte[] src = {0x00, 0x00, 0x00, 0x00};
        int expResult = 0x00000000;
        int result = ByteConverter.bytesToInt(src);
        assertEquals(expResult, result);
    }

    /**
     * Test of bytesToInt method, of class ByteConverter.
     */
    @Test
    public void testBytesToInt2() {
        LOG.info("bytesToInt");
        byte[] src = {0x12, 0x34};
        int expResult = 0x1234;
        int result = ByteConverter.bytesToInt(src);
        assertEquals(expResult, result);
    }

    /**
     * Test of bytesToInt method, of class ByteConverter.
     */
    @Test
    public void testBytesToInt3() {
        LOG.info("bytesToInt");
        byte[] src = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff};
        int expResult = 0xffffffff;
        int result = ByteConverter.bytesToInt(src);
        assertEquals(expResult, result);
    }

    
    /**
     * Test of bytesToInt method, of class ByteConverter.
     */
    @Test(expected=IndexOutOfBoundsException.class)
    public void testBytesToInt4() {
        LOG.info("bytesToInt");
        byte[] src = {(byte) 0xff, (byte) 0xff, (byte) 0xff, (byte) 0xff,(byte)0xff};
        int result = ByteConverter.bytesToInt(src);
    }
}
