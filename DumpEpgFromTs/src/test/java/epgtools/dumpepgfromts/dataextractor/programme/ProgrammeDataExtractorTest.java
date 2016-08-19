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
package epgtools.dumpepgfromts.dataextractor.programme;

import epgtools.dumpepgfromts.test.common.TestSection;
import java.lang.invoke.MethodHandles;
import java.util.Set;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author normal
 */
public class ProgrammeDataExtractorTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    public ProgrammeDataExtractorTest() {
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
     * Test of getDataList method, of class ProgrammeDataExtractor.
     */
    @Test
    public void testGetDataList() throws DecoderException {
        LOG.info("getDataList");
        ProgrammeDataExtractor instance = new ProgrammeDataExtractor();
        instance.makeDataSet(TestSection.getEit1());
        Set<Programme> result = instance.getUnmodifiableDataSet();
        for (Programme p : result) {
            LOG.info("***********************************************************************************************************************************************************************************************************");
            LOG.info(p);
            LOG.info("***********************************************************************************************************************************************************************************************************");
        }

    }


}
