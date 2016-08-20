/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libepg.epg.section.transportstreamid;

import static libepg.epg.section.range.SectionValueRangeChecker.TRANSPORT_STREAM_ID_RANGE;
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
public class TransportStreamIdRangeCheckerTest {

    public TransportStreamIdRangeCheckerTest() {
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
    public void testTrue0x0001() {
        assertEquals(TRANSPORT_STREAM_ID_RANGE.contains(0x0001), true);
    }

    @Test
    public void testTrue0xfffe() {
        assertEquals(TRANSPORT_STREAM_ID_RANGE.contains(0xfffe), true);
    }

    @Test
    public void testFalse0x0000() {
        assertEquals(TRANSPORT_STREAM_ID_RANGE.contains(0x0000), false);
    }

    @Test
    public void testTrue0xffff() {
        assertEquals(TRANSPORT_STREAM_ID_RANGE.contains(0xffff), false);
    }
}
