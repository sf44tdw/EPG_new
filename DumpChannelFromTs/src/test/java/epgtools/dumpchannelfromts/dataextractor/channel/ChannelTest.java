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
public class ChannelTest {
    
    private final Channel target=new Channel(100,200,300,"name");
    
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

    /**
     * Test of getTransport_stream_id method, of class Channel.
     */
    @Test
    public void testGetTransport_stream_id() {
        System.out.println("getTransport_stream_id");
        Channel instance = null;
        int expResult = 0;
        int result = instance.getTransport_stream_id();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getOriginal_network_id method, of class Channel.
     */
    @Test
    public void testGetOriginal_network_id() {
        System.out.println("getOriginal_network_id");
        Channel instance = null;
        int expResult = 0;
        int result = instance.getOriginal_network_id();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getService_id method, of class Channel.
     */
    @Test
    public void testGetService_id() {
        System.out.println("getService_id");
        Channel instance = null;
        int expResult = 0;
        int result = instance.getService_id();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDisplay_name method, of class Channel.
     */
    @Test
    public void testGetDisplay_name() {
        System.out.println("getDisplay_name");
        Channel instance = null;
        String expResult = "";
        String result = instance.getDisplay_name();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of hashCode method, of class Channel.
     */
    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        Channel instance = null;
        int expResult = 0;
        int result = instance.hashCode();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of equals method, of class Channel.
     */
    @Test
    public void testEquals() {
        System.out.println("equals");
        Object obj = null;
        Channel instance = null;
        boolean expResult = false;
        boolean result = instance.equals(obj);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of toString method, of class Channel.
     */
    @Test
    public void testToString() {
        System.out.println("toString");
        Channel instance = null;
        String expResult = "";
        String result = instance.toString();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
