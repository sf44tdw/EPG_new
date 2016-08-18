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

import java.util.Collection;
import java.util.Iterator;
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

    private class pitem implements Id {

        private final int transport_stream_id;
        private final int original_network_id;
        private final int service_id;
        private final int originalMember;

        public pitem(int transport_stream_id, int original_network_id, int service_id, int originalMember) {
            this.transport_stream_id = transport_stream_id;
            this.original_network_id = original_network_id;
            this.service_id = service_id;
            this.originalMember = originalMember;
        }

        @Override
        public int getTransport_stream_id() {
            return transport_stream_id;
        }

        @Override
        public int getOriginal_network_id() {
            return original_network_id;
        }

        @Override
        public int getService_id() {
            return service_id;
        }

        public int getOriginalMember() {
            return originalMember;
        }

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 89 * hash + this.transport_stream_id;
            hash = 89 * hash + this.original_network_id;
            hash = 89 * hash + this.service_id;
            hash = 89 * hash + this.originalMember;
            return hash;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final pitem other = (pitem) obj;
            if (this.transport_stream_id != other.transport_stream_id) {
                return false;
            }
            if (this.original_network_id != other.original_network_id) {
                return false;
            }
            if (this.service_id != other.service_id) {
                return false;
            }
            if (this.originalMember != other.originalMember) {
                return false;
            }
            return true;
        }

    }

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
        PredicateSet<pitem> instance = new PredicateSet<>(new KeyFields(100, 200, 300));
        boolean expResult1 = false;
        boolean result1 = instance.contains(new pitem(100, 200, 300, 400));
        assertEquals(expResult1, result1);

    }

    /**
     * Test of contains method, of class PredicateSet.
     */
    @Test
    public void testContains2() {
        System.out.println("contains");
        PredicateSet<pitem> instance = new PredicateSet<>(new KeyFields(100, 200, 300));
        boolean expResult2 = true;
        instance.add(new pitem(100, 200, 300, 400));
        boolean result2 = instance.contains(new pitem(100, 200, 300, 400));
        assertEquals(expResult2, result2);

    }

    /**
     * Test of add method, of class PredicateSet.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        PredicateSet<pitem> instance = new PredicateSet<>(new KeyFields(100, 200, 300));

        pitem e2 = new pitem(100, 200, 800, 500);
        boolean expResult2 = false;
        boolean result2 = instance.add(e2);
        assertEquals(expResult2, result2);

        pitem e3 = new pitem(100, 200, 300, 500);
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
        PredicateSet<pitem> instance = new PredicateSet<>(new KeyFields(100, 200, 300));
        pitem e1 = null;
        boolean expResult1 = false;
        boolean result1 = instance.add(e1);
        assertEquals(expResult1, result1);

    }
}
