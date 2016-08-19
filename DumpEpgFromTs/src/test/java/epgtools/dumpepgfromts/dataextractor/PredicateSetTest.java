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

import java.util.HashSet;
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
public class PredicateSetTest {

    public PredicateSetTest() {
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
     * Test of contains method, of class PredicateSet.
     */
    @Test
    public void testContains1() {
        System.out.println("contains");
        Object o = null;
        PredicateSet<DataObject> instance = new PredicateSet<>(100, 200, 300, new HashSet<>());
        boolean expResult1 = false;
        boolean result1 = instance.contains(new DataObject(100, 200, 300));
        assertEquals(expResult1, result1);

    }

    /**
     * Test of contains method, of class PredicateSet.
     */
    @Test
    public void testContains2() {
        System.out.println("contains");
        PredicateSet<DataObject> instance = new PredicateSet<>(100, 200, 300, new HashSet<>());
        boolean expResult2 = true;
        instance.add(new DataObject(100, 200, 300));
        boolean result2 = instance.contains(new DataObject(100, 200, 300));
        assertEquals(expResult2, result2);

    }

    /**
     * Test of add method, of class PredicateSet.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        PredicateSet<DataObject> instance = new PredicateSet<>(100, 200, 300, new HashSet<>());

        DataObject e2 = new DataObject(100, 200, 800);
        boolean expResult2 = false;
        boolean result2 = instance.add(e2);
        assertEquals(expResult2, result2);

        DataObject e3 = new DataObject(100, 200, 300);
        boolean expResult3 = true;
        boolean result3 = instance.add(e3);
        assertEquals(expResult3, result3);

    }

    /**
     * Test of add method, of class PredicateSet.
     */
    @Test(expected = NullPointerException.class)
    public void testAdd_nullpo() {
        System.out.println("add");
        PredicateSet<DataObject> instance = new PredicateSet<>(100, 200, 300, new HashSet<>());
        DataObject e1 = null;
        boolean expResult1 = false;
        boolean result1 = instance.add(e1);
        assertEquals(expResult1, result1);

    }
}
