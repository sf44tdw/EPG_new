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
package libepg.ts.fileseeker;

import java.io.File;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.junit.Assume.assumeTrue;

/**
 *
 * @author normal
 */
public class TsFileSeekerTest {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TsFileSeekerTest.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    public TsFileSeekerTest() {
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

    private final File dir = new File("./test/dummyfile");
    private final TsFileSeeker target = new TsFileSeeker(dir);

    /**
     * Test of isRecursive method, of class FileSeeker.
     */
    @Test
    public void testIsRecursive() {
        LOG.info("");
        TsFileSeeker instance = target;
        boolean expResult = false;
        boolean result = instance.isRecursive();
        assertEquals(expResult, result);
    }

    /**
     * Test of setRecursive method, of class FileSeeker.
     */
    @Test
    public void testSetRecursive() {
        LOG.info("");
        boolean recursive = true;
        TsFileSeeker instance = new TsFileSeeker(dir);
        assertEquals(false, instance.isRecursive());
        instance.setRecursive(recursive);
        assertEquals(true, instance.isRecursive());
    }

    /**
     * Test of seek method, of class FileSeeker. 注:テスト用のダミーファイルが無いとこのテストは行われない。
     */
    @Test
    public void testSeek() {
        LOG.info("");
        assumeTrue(this.dir.isDirectory());
        TsFileSeeker instance = this.target;;
        List<File> expResult = new ArrayList<>();
        File f1 = new File(this.dir, "10.ts").getAbsoluteFile();
        File f2 = new File(this.dir, "11.ts").getAbsoluteFile();
        File f3 = new File(this.dir, "12.ts").getAbsoluteFile();
        File f4 = new File(this.dir, "13.ts").getAbsoluteFile();
        assumeTrue(f1.isFile());
        assumeTrue(f2.isFile());
        assumeTrue(f3.isFile());
        assumeTrue(f4.isFile());
        expResult.add(f1);
        expResult.add(f2);
        expResult.add(f3);
        expResult.add(f4);

        List<File> result = instance.seek();
        assertEquals(expResult.size(), result.size());

        for (File f : expResult) {
            assertTrue(result.contains(f));
        }

    }

}
