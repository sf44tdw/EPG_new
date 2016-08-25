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

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libepg.common.tsfile.TsFile;
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
public class SectionLoaderTest {

    private final SectionLoader target;
    private final Set<Integer> pids;

    public SectionLoaderTest() {
        this.pids = new HashSet<>();
        this.pids.add(0x10);
        target = new SectionLoader(TsFile.getTsFile(), null, this.pids);
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
     * Test of load method, of class SectionLoader.
     */
    @Test
    public void testLoad() throws Exception {
        System.out.println("load");
        SectionLoader instance = this.target;
        Map<Integer, List<Section>> result = instance.load();
        assertEquals(1, result.size());
    }

    /**
     * Test of getTsFile method, of class SectionLoader.
     */
    @Test
    public void testGetTsFile() {
        System.out.println("getTsFile");
        SectionLoader instance = this.target;
        File expResult = TsFile.getTsFile();
        File result = instance.getTsFile();
        assertEquals(expResult, result);
    }

}
