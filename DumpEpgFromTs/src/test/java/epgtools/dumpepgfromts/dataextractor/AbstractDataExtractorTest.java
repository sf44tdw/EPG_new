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
package epgtools.dumpepgfromts.dataextractor;

import epgtools.dumpepgfromts.test.common.TestSection;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import org.apache.commons.codec.DecoderException;
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
import testtool.testrule.regexmessagerule.ExpectedExceptionMessage;
import testtool.testrule.regexmessagerule.ExpectedExceptionRule;

/**
 *
 * @author normal
 */
public class AbstractDataExtractorTest {

    protected static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }
    private final Section S;

    private class Target extends AbstractDataExtractor<DataObject> {

        public Target(Section source, TABLE_ID tableId, TABLE_ID... tablsIds) throws IllegalArgumentException {
            super(source, tableId, tablsIds);
        }

        @Override
        public Map<MultiKey<Integer>, DataObject> getDataList() {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }
    }

    private final Target t;

    public AbstractDataExtractorTest() throws DecoderException {
        S = TestSection.getEit1();
        t = new Target(S, S.getTable_id_const(), TABLE_ID.EIT_OTHER_STREAM_NOW_AND_NEXT);
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
    @Rule
    public ExpectedExceptionRule rule = new ExpectedExceptionRule();

    @Test
    @ExpectedExceptionMessage("^.*CRCエラーです.*$")
    public void testConstructor_CRC_Error() throws DecoderException {
        LOG.info("");
        final AbstractDataExtractor instance = new Target(TestSection.getSdt3_CRCERROR(), TABLE_ID.SDT);
    }

    @Test
    @ExpectedExceptionMessage("処理対象のテーブルIDではありません")
    public void testConstructor_Not_SDT() throws DecoderException {
        LOG.info("");
        final AbstractDataExtractor instance = new Target(TestSection.getSdt2(), TABLE_ID.EIT_THIS_STREAM_NOW_AND_NEXT);
    }

    /**
     * Test of getSource method, of class AbstractDataExtractor.
     */
    @Test
    public void testGetSource() throws DecoderException {
        LOG.info("");
        AbstractDataExtractor instance = t;
        Section expResult = TestSection.getEit1();
        Section result = instance.getSource();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTableIds method, of class AbstractDataExtractor.
     */
    @Test
    public void testGetTableIds() {
        LOG.info("");
        AbstractDataExtractor instance = t;
        Set<TABLE_ID> expResult = new HashSet<TABLE_ID>();
        expResult.add(S.getTable_id_const());
        expResult.add(TABLE_ID.EIT_OTHER_STREAM_NOW_AND_NEXT);
        Set<TABLE_ID> result = instance.getTableIds();
        assertEquals(expResult, result);
    }

//    /**
//     * Test of getDataList method, of class AbstractDataExtractor.
//     */
//    @Test
//    public void testGetDataList() {
//        System.out.println("getDataList");
//        AbstractDataExtractor instance = t;
//        Map expResult = null;
//        Map result = instance.getDataList();
//        assertEquals(expResult, result);
//        // TODO review the generated test code and remove the default call to fail.
//        fail("The test case is a prototype.");
//    }
}
