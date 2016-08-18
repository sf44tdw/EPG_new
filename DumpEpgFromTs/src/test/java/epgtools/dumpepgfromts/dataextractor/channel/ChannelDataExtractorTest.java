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
package epgtools.dumpepgfromts.dataextractor.channel;

import epgtools.dumpepgfromts.test.common.TestSection;
import java.lang.invoke.MethodHandles;
import java.util.Map;
import libepg.epg.section.Section;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Rule;
import testtool.testrule.regexmessagerule.ExpectedExceptionMessage;
import testtool.testrule.regexmessagerule.ExpectedExceptionRule;

/**
 *
 * @author normal
 */
public class ChannelDataExtractorTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    @Rule
    public ExpectedExceptionRule rule = new ExpectedExceptionRule();

    public ChannelDataExtractorTest() {
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
    @ExpectedExceptionMessage("^.*CRCエラーです.*$")
    public void testConstructor_CRC_Error() throws DecoderException {
        LOG.info("");
        final ChannelDataExtractor instance = new ChannelDataExtractor(TestSection.getSdt3_CRCERROR());
    }

    @Test
    @ExpectedExceptionMessage("^.*テーブル識別が想定と違います.*$")
    public void testConstructor_Not_SDT() throws DecoderException {
        LOG.info("");
        final ChannelDataExtractor instance = new ChannelDataExtractor(TestSection.getEit1());
    }

    /**
     * Test of getChannels method, of class ChannelDataExtractor.
     */
    @Test
    public void testGetChannels() throws DecoderException {
        LOG.info("");
        Section sec = TestSection.getSdt1();
        ChannelDataExtractor instance = new ChannelDataExtractor(sec);
      Map<MultiKey<Integer>, Channel> map =  instance.getDataList();
      assertTrue(map.size()==1);
      Channel ch=map.get( new MultiKey<>(0x4750,0x4,0xfc));
      assertEquals(ch.getDisplay_name(),"イマジカＢＳ・映画");
    }

    /**
     * Test of getSource method, of class ChannelDataExtractor.
     */
    @Test
    public void testGetSource() throws DecoderException {
        LOG.info("");
        Section sec = TestSection.getSdt1();
        ChannelDataExtractor instance = new ChannelDataExtractor(sec);
        LOG.info("\nセクション_S****************************************************************************************************************************************************************************************************");
        LOG.info(instance.getSource());
        LOG.info("セクション_E****************************************************************************************************************************************************************************************************\n");
    }

}
