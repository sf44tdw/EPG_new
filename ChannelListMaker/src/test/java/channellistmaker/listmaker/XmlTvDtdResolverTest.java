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
package channellistmaker.listmaker;

import static channellistmaker.listmaker.XmlTvDtdResolver.DTD_NAME;
import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 *
 * @author normal
 */
public class XmlTvDtdResolverTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    public XmlTvDtdResolverTest() {
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
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_1() {
        try {
            LOG.info("ResolveEntity_FileExist_1");
            String publicId = XmlTvDtdResolver.DTD_NAME;
            String systemId = XmlTvDtdResolver.DTD_NAME;
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertTrue(result instanceof InputSource);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_2() {
        try {
            LOG.info("ResolveEntity_FileExist_2");
            String publicId;
            publicId = XmlTvDtdResolver.DTD_NAME;
            String systemId = null;
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertTrue(result instanceof InputSource);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_2_2() {
        try {
            LOG.info("ResolveEntity_FileExist_2_2");
            String publicId = XmlTvDtdResolver.DTD_NAME;
            String systemId = "fwlfkdhohas;grhgl;rhgiaqh";
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertTrue(result instanceof InputSource);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_3() {
        try {
            LOG.info("ResolveEntity_FileExist_3");
            String publicId = null;
            String systemId = XmlTvDtdResolver.DTD_NAME;
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertTrue(result instanceof InputSource);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_3_2() {
        try {
            LOG.info("ResolveEntity_FileExist_3_2");
            String publicId = "dsliadguislguvilhgdklsgaklhug;guo";
            String systemId = XmlTvDtdResolver.DTD_NAME;
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertTrue(result instanceof InputSource);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_4() {
        try {
            LOG.info("ResolveEntity_FileExist_4");
            String publicId = null;
            String systemId = null;
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertEquals(null, result);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_5() {
        try {
            LOG.info("ResolveEntity_FileExist_5");
            String publicId = "fowih;weohf;w";
            String systemId = "dsegli4ewqup;g";
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertEquals(null, result);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_6() {
        try {
            LOG.info("ResolveEntity_FileExist_6");
            String publicId = "fowih;weohf;w";
            String systemId = null;
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertEquals(null, result);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

    /**
     * Test of resolveEntity method, of class XmlTvDtdResolver.
     */
    @Test
    public void testResolveEntity_FileExist_7() {
        try {
            LOG.info("ResolveEntity_FileExist_7");
            String publicId = "null";
            String systemId = "dsegli4ewqup;g";
            XmlTvDtdResolver instance = new XmlTvDtdResolver();
            InputSource result = instance.resolveEntity(publicId, systemId);
            assertEquals(null, result);
        } catch (SAXException | IOException ex) {
            LOG.fatal(ex);
        }
    }

}
