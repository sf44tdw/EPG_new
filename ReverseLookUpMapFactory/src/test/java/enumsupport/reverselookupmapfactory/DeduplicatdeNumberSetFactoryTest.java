/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package enumsupport.reverselookupmapfactory;

import enumsupport.reverselookupmapfactory.DeduplicatdeNumberSetFactory;
import epgtools.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang3.Range;
import org.apache.commons.logging.Log;
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
public class DeduplicatdeNumberSetFactoryTest {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, DeduplicatdeNumberSetFactoryTest.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    public DeduplicatdeNumberSetFactoryTest() {
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

    /**
     * Test of makeSet method, of class DeduplicatdeNumberSetFactory.
     */
    @Test
    public void testMakeSet1() {
        LOG.info("makeSet");
        DeduplicatdeNumberSetFactory<Integer> instance = new DeduplicatdeNumberSetFactory<>();
        Set<Integer> expResult = new HashSet<>();
        expResult.add(10);
        expResult.add(11);
        expResult.add(12);
        expResult.add(13);
        expResult.add(14);
        expResult.add(15);
        expResult.add(16);
        expResult.add(17);
        expResult.add(18);
        expResult.add(19);
        expResult.add(20);
        Set<Integer> result = instance.makeSet(Range.between(10, 20), 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20);
        assertEquals(expResult, result);
    }

    /**
     * Test of makeSet method, of class DeduplicatdeNumberSetFactory.
     */
    @Test
    public void testMakeSet2() {
        LOG.info("makeSet");
        DeduplicatdeNumberSetFactory<Integer> instance = new DeduplicatdeNumberSetFactory<>();
        Set<Integer> expResult = new HashSet<>();
        expResult.add(10);
        Set<Integer> result = instance.makeSet(Range.is(10), 10);
        assertEquals(expResult, result);
    }

    /**
     * Test of makeSet method, of class DeduplicatdeNumberSetFactory.
     */
    @Test
    @ExpectedExceptionMessage("引数リスト内にnullが混じっています")
    public void testMakeSet3() {
        LOG.info("makeSet");
        DeduplicatdeNumberSetFactory<Integer> instance = new DeduplicatdeNumberSetFactory<>();
        Set<Integer> result = instance.makeSet(Range.is(10), null, 10);
    }

    /**
     * Test of makeSet method, of class DeduplicatdeNumberSetFactory.
     */
    @Test
    @ExpectedExceptionMessage("引数リスト内にnullが混じっています")
    public void testMakeSet4() {
        LOG.info("makeSet");
        DeduplicatdeNumberSetFactory<Integer> instance = new DeduplicatdeNumberSetFactory<>();
        Set<Integer> result = instance.makeSet(Range.between(10, 20), 10, 11, 12, null, 13);
    }

    /**
     * Test of makeSet method, of class DeduplicatdeNumberSetFactory.
     */
    @Test
    @ExpectedExceptionMessage("範囲外の値が含まれています")
    public void testMakeSet5() {
        LOG.info("makeSet");
        DeduplicatdeNumberSetFactory<Integer> instance = new DeduplicatdeNumberSetFactory<>();
        Set<Integer> result = instance.makeSet(Range.between(10, 20), 10, 11, 12, 21, 13);
    }

    /**
     * Test of makeSet method, of class DeduplicatdeNumberSetFactory.
     */
    @Test
    @ExpectedExceptionMessage("範囲外の値が含まれています")
    public void testMakeSet6() {
        LOG.info("makeSet");
        DeduplicatdeNumberSetFactory<Integer> instance = new DeduplicatdeNumberSetFactory<>();
        Set<Integer> result = instance.makeSet(Range.between(10, 20), 10, 9, 12, 20, 13);
    }

}
