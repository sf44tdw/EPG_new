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
package libepg.epg.section;

import java.lang.invoke.MethodHandles;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static libepg.epg.section.TABLE_ID.EIT_OTHER_STREAM_8_DAYS;
import static libepg.epg.section.TABLE_ID.EIT_OTHER_STREAM_NOW_AND_NEXT;
import static libepg.epg.section.TABLE_ID.EIT_THIS_STREAM_8_DAYS;
import static libepg.epg.section.TABLE_ID.EIT_THIS_STREAM_NOW_AND_NEXT;
import static libepg.epg.section.TABLE_ID.OTHER_TABLE_IDS;
import libepg.epg.section.body.eventinformationtable.EventInformationTableBody;
import loggingsupport.loggerfactory.LoggerFactory;
import static libepg.epg.section.TABLE_ID.SDT_THIS_STREAM;

/**
 *
 * @author normal
 */
public class TABLE_IDTest {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TABLE_IDTest.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    public TABLE_IDTest() {
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
     * Test of values method, of class TABLE_ID.
     */
    @Test
    public void testValues() {
        LOG.info("values");
        TABLE_ID[] expResult = {
            TABLE_ID.NIT_THIS_NETWORK, 
            TABLE_ID.NIT_OTHER_NETWORK, 
            TABLE_ID.SDT_THIS_STREAM, 
            TABLE_ID.SDT_OTHER_STREAM, 
            EIT_THIS_STREAM_8_DAYS, 
            EIT_THIS_STREAM_NOW_AND_NEXT, 
            EIT_OTHER_STREAM_8_DAYS, 
            EIT_OTHER_STREAM_NOW_AND_NEXT, 
            OTHER_TABLE_IDS};
        TABLE_ID[] result = TABLE_ID.values();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of valueOf method, of class TABLE_ID.
     */
    @Test
    public void testValueOf() {
        LOG.info("valueOf");
        String name = "SDT_THIS_STREAM";
        TABLE_ID expResult = TABLE_ID.SDT_THIS_STREAM;
        TABLE_ID result = TABLE_ID.valueOf(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of reverseLookUp method, of class TABLE_ID.
     */
    @Test
    public void testReverseLookUp() {
        LOG.info("reverseLookUp");
        Integer tableID = 0x67;
        TABLE_ID expResult = TABLE_ID.EIT_OTHER_STREAM_8_DAYS;
        TABLE_ID result = TABLE_ID.reverseLookUp(tableID);
        assertEquals(expResult, result);
    }

    /**
     * Test of contains method, of class TABLE_ID.
     */
    @Test
    public void testContains() {
        LOG.info("contains");
        Integer tableID = 0x42;
        TABLE_ID instance = TABLE_ID.SDT_THIS_STREAM;
        boolean expResult = true;
        boolean result = instance.contains(tableID);
        assertEquals(expResult, result);
    }

    /**
     * Test of getMaxSectionLength method, of class TABLE_ID.
     */
    @Test
    public void testGetMaxSectionLength() {
        LOG.info("getMaxSectionLength");
        TABLE_ID instance = TABLE_ID.EIT_OTHER_STREAM_8_DAYS;
        TABLE_ID.MAX_SECTION_LENGTH expResult = TABLE_ID.MAX_SECTION_LENGTH.BYTE_4093;
        TABLE_ID.MAX_SECTION_LENGTH result = instance.getMaxSectionLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class TABLE_ID.
     */
    @Test
    public void testToString() {
        LOG.info("toString");
        LOG.info(EIT_OTHER_STREAM_8_DAYS);
    }

    /**
     * Test of getDataType method, of class TABLE_ID.
     */
    @Test
    public void testGetDataType() {
        LOG.info("getDataType");
        TABLE_ID instance = EIT_THIS_STREAM_NOW_AND_NEXT;
        Class<? extends SectionBody> expResult = EventInformationTableBody.class;
        Class<? extends SectionBody> result = instance.getDataType();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableName method, of class TABLE_ID.
     */
    @Test
    public void testGetTableName() {
        LOG.info("getTableName");
        TABLE_ID instance = SDT_THIS_STREAM;
        String expResult = "サービス記述テーブル（自ストリーム）";
        String result = instance.getTableName();
        assertEquals(expResult, result);
    }

}
