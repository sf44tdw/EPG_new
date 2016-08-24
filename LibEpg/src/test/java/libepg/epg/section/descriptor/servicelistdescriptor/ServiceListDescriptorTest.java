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
package libepg.epg.section.descriptor.servicelistdescriptor;

import loggingsupport.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.List;
import libepg.common.descriptor.Descriptors;
import libepg.epg.section.descriptor.Descriptor;
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
public class ServiceListDescriptorTest {

    protected static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Descriptor.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final Descriptors descs;

    public ServiceListDescriptorTest() throws DecoderException {
        descs = new Descriptors();
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
     * Test of getServiceList method, of class ServiceListDescriptor.
     */
    @Test
    public void testGetServiceList() throws DecoderException {
        LOG.info("getServiceList");
        ServiceListDescriptor instance = new ServiceListDescriptor(this.descs.getSERVICE_LIST_DESCRIPTOR());
        List<Service> expResult = new ArrayList<>();
        expResult.add(new Service(Hex.decodeHex("009701".toCharArray())));
        expResult.add(new Service(Hex.decodeHex("009801".toCharArray())));
        expResult.add(new Service(Hex.decodeHex("009901".toCharArray())));
        expResult.add(new Service(Hex.decodeHex("02f1c0".toCharArray())));
        expResult.add(new Service(Hex.decodeHex("02f3c0".toCharArray())));
        expResult.add(new Service(Hex.decodeHex("02f4c0".toCharArray())));
        expResult.add(new Service(Hex.decodeHex("02f5c0".toCharArray())));

        List<Service> result = instance.getServiceList();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class ServiceListDescriptor.
     */
    @Test
    public void testToString() {
        LOG.info("toString");
        ServiceListDescriptor instance = new ServiceListDescriptor(this.descs.getSERVICE_LIST_DESCRIPTOR());
        LOG.info(instance);
    }

}
