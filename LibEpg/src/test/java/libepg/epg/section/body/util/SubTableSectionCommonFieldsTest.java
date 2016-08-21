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
package libepg.epg.section.body.util;

import epgtools.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationTargetException;
import libepg.common.SectionBodyMaker;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.SectionBody;
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
public class SubTableSectionCommonFieldsTest {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, SubTableSectionCommonFieldsTest.CLASS_LOG_OUTPUT_MODE).getLOG();
    }
    private final byte[] x;
    private final SectionBody dummyBody;
    private final SubTableSectionCommonFields target;

    public SubTableSectionCommonFieldsTest() throws DecoderException, InvocationTargetException {
        x = Hex.decodeHex("ffff3eff00ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".toCharArray());
        dummyBody = SectionBodyMaker.init(TABLE_ID.OTHER_TABLE_IDS, this.x);
        this.target = new SubTableSectionCommonFields(this.dummyBody);
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
     * Test of getFirst16Bit method, of class SubTableSectionCommonFields.
     */
    @Test
    public void testGetFirst16Bit() {
        LOG.info("getFirst16Bit");
        SubTableSectionCommonFields instance = target;
        int expResult = 0xffff;
        int result = instance.getFirst16Bit();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSecond2Bit method, of class SubTableSectionCommonFields.
     */
    @Test
    public void testGetSecond2Bit() {
        LOG.info("getSecond2Bit");
        SubTableSectionCommonFields instance = target;
        int expResult = 0;
        int result = instance.getSecond2Bit();
    }

    /**
     * Test of getTherd5Bit method, of class SubTableSectionCommonFields.
     */
    @Test
    public void testGetTherd5Bit() {
        LOG.info("getTherd5Bit");
        SubTableSectionCommonFields instance = target;
        int expResult = 0x1f;
        int result = instance.getTherd5Bit();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFourth1Bit method, of class SubTableSectionCommonFields.
     */
    @Test
    public void testGetFourth1Bit() {
        LOG.info("getFourth1Bit");
        SubTableSectionCommonFields instance = target;
        int expResult = 0;
        int result = instance.getFourth1Bit();
        assertEquals(expResult, result);
    }

    /**
     * Test of getFifth8Bit method, of class SubTableSectionCommonFields.
     */
    @Test
    public void testGetFifth8Bit() {
        LOG.info("getFifth8Bit");
        SubTableSectionCommonFields instance = target;
        int expResult = 0xff;
        int result = instance.getFifth8Bit();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSixth8Bit method, of class SubTableSectionCommonFields.
     */
    @Test
    public void testGetSixth8Bit() {
        LOG.info("getSixth8Bit");
        SubTableSectionCommonFields instance = target;
        int expResult = 0;
        int result = instance.getSixth8Bit();
        assertEquals(expResult, result);
    }

}
