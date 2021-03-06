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
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import org.apache.commons.codec.DecoderException;
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

    public AbstractDataExtractorTest() throws DecoderException {
        S = TestSection.getEit1();

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
        final AbstractDataExtractor instance = new AbstractDataExtractorImpl();
        instance.checkSection(TestSection.getSdt3_CRCERROR());
    }

    @Test
    @ExpectedExceptionMessage("処理対象のテーブルIDではありません")
    public void testConstructor_Not_SDT() throws DecoderException {
        LOG.info("");
        final AbstractDataExtractor instance = new AbstractDataExtractorImpl();
        instance.checkSection(TestSection.getEit1());
    }

    public class AbstractDataExtractorImpl extends AbstractDataExtractor {

        public AbstractDataExtractorImpl() {
            super(TABLE_ID.SDT_THIS_STREAM);
        }

        public void makeDataSet(Section s) throws IllegalStateException {
            this.checkSection(s);
        }
    }

}
