/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package libepg.ts.packet;

import java.lang.invoke.MethodHandles;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import libepg.common.packet.TestPacket_SDT;
import libepg.common.packet.TestPacket_pid0x11_count_d_0_0;
import libepg.common.packet.TsPacket_error;
import loggingsupport.loggerfactory.LoggerFactory;
import static libepg.ts.packet.RESERVED_PROGRAM_ID.SDT_OR_BAT;
import libepg.ts.packet.TsPacket.TRANSPORT_SCRAMBLING_CONTROL;
import org.junit.Rule;
import testtool.EqualsChecker;
import testtool.testrule.regexmessagerule.ExpectedExceptionMessage;
import testtool.testrule.regexmessagerule.ExpectedExceptionRule;

/**
 *
 * @author normal
 */
public class TSpacketTest {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, TsPacket.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    @Rule
    public ExpectedExceptionRule rule = new ExpectedExceptionRule();

    TestPacket_pid0x11_count_d_0_0 pd;
    private final TsPacket target00;

    public TSpacketTest() throws DecoderException {
        pd = new TestPacket_pid0x11_count_d_0_0();
        target00 = pd.target_p10;
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

    private final TsPacket_error err = new TsPacket_error();

    @Test
    @ExpectedExceptionMessage("^.*同期ワードが不正です.*$")
    public void testConst_sync_is_not_47() throws DecoderException {
        final char[] p_sync_byte_not_47 = "4801ff20b710728126917eb7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".toCharArray();
        final TsPacket target_sync_byte_not_47 = new TsPacket(Hex.decodeHex(p_sync_byte_not_47));
    }

    @Test
    @ExpectedExceptionMessage("^.*バイト列の長さがパケット長と一致しません.*$")
    public void testConst_too_short() throws DecoderException {
        final char[] p_too_short = "4701ff20b710728126917eb7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".toCharArray();
        final TsPacket target_too_short = new TsPacket(Hex.decodeHex(p_too_short));
    }

    @Test
    @ExpectedExceptionMessage("^.*バイト列の長さがパケット長と一致しません.*$")
    public void testConst_too_long() throws DecoderException {
        final char[] p_too_long = "4701ff20b710728126917eb7ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".toCharArray();
        final TsPacket target_too_long = new TsPacket(Hex.decodeHex(p_too_long));
    }

    /**
     * Test of getSync_byte method, of class TsPacket.
     */
    @Test
    public void testGetSync_byte() {
        LOG.info("getSync_byte");
        TsPacket instance = this.target00;
        int expResult = 0x47;
        int result = instance.getSync_byte();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransport_error_indicator method, of class TsPacket.
     */
    @Test
    public void testGetTransport_error_indicator() {
        LOG.info("getTransport_error_indicator");
        TsPacket instance = this.target00;
        int expResult = 0;
        int result = instance.getTransport_error_indicator();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPayload_unit_start_indicator method, of class TsPacket.
     */
    @Test
    public void testGetPayload_unit_start_indicator() {
        LOG.info("getPayload_unit_start_indicator");
        TsPacket instance = this.target00;
        TsPacket.PAYLOAD_UNIT_START_INDICATOR expResult = TsPacket.PAYLOAD_UNIT_START_INDICATOR.START_PES_OR_START_SECTION;
        TsPacket.PAYLOAD_UNIT_START_INDICATOR result = instance.getPayload_unit_start_indicator();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransport_priority method, of class TsPacket.
     */
    @Test
    public void testGetTransport_priority() {
        LOG.info("getTransport_priority");
        TsPacket instance = this.target00;
        int expResult = 0;
        int result = instance.getTransport_priority();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAdaptation_field_control method, of class TsPacket.
     */
    @Test
    public void testGetAdaptation_field_control() {
        LOG.info("getAdaptation_field_control");
        TsPacket instance = this.target00;
        TsPacket.ADAPTATION_FIELD_CONTROL expResult = TsPacket.ADAPTATION_FIELD_CONTROL.ONLY_PAYLOAD;
        TsPacket.ADAPTATION_FIELD_CONTROL result = instance.getAdaptation_field_control();
        assertEquals(expResult, result);
    }

    /**
     * Test of getContinuity_counter method, of class TsPacket.
     */
    @Test
    public void testGetContinuity_counter() {
        LOG.info("getContinuity_counter");
        TsPacket instance = this.target00;
        int expResult = 0;
        int result = instance.getContinuity_counter();
        assertEquals(expResult, result);
    }

    /**
     * Test of getAdaptation_field method, of class TsPacket.
     */
    @Test
    public void testGetAdaptation_field() {
        LOG.info("getAdaptation_field");
        TsPacket instance = this.target00;
        byte[] expResult = {0};
        byte[] result = instance.getAdaptation_field();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getPayload method, of class TsPacket.
     */
    @Test
    public void testGetPayload() throws DecoderException {
        LOG.info("getPayload");
        TsPacket instance = this.target00;
        String paStr = "0042f0977fe1d100007fe1ff0408f30020481201000f0e4e484b451d461d6c310f456c357ec10184cf0701fe08f00104080409f3001c481201000f0e4e484b451d461d6c320f456c357ec10184cf0302fe08040af3001c481201000f0e4e484b451d461d6c330f456c357ec10184cf0302fe080588e5001f480ec0000b0e4e484b0f374842530e32c10188cf0a030e4e484b0f215d0e32722fa2b5ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff";
        byte[] expResult = Hex.decodeHex(paStr.toCharArray());
        byte[] result = instance.getPayload();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of equals method, of class TsPacket.
     */
    @Test
    public void testEquals_same() throws DecoderException {
        LOG.info("equals_same");

        EqualsChecker<TsPacket> ec = new EqualsChecker<>();

        TsPacket obj = new TsPacket(Hex.decodeHex(this.pd.p10));
        TsPacket obj2 = new TsPacket(Hex.decodeHex(this.pd.p10));
        TsPacket instance = new TsPacket(Hex.decodeHex(this.pd.p10));
//        boolean ret = ec.check_same(instance, obj, obj2);
//        System.out.println("****************************************************" + ret);
//        System.out.println("****************************************************" + obj.hashCode());
//        System.out.println("****************************************************" + obj2.hashCode());
//        System.out.println("****************************************************" + instance.hashCode());
        assertTrue(ec.check_same(instance, obj, obj2));
    }

    /**
     * Test of equals method, of class TsPacket.
     */
    @Test
    public void testEquals_Notsame() throws DecoderException {
        LOG.info("equals_Notsame");

        EqualsChecker<TsPacket> ec = new EqualsChecker<>();

        TsPacket obj = new TsPacket(Hex.decodeHex(this.pd.p10));
        TsPacket obj2 = new TsPacket(Hex.decodeHex(this.pd.p11));
        TsPacket instance = this.target00;
        assertTrue(ec.check_not_same(instance, obj, obj2));
    }

    @Test
    public void testHashCode() throws DecoderException {
        LOG.info("HashCode_same");
        TsPacket obj = new TsPacket(Hex.decodeHex(this.pd.p10));
        TsPacket obj2 = new TsPacket(Hex.decodeHex(this.pd.p10));
        assertEquals(obj.hashCode(), obj2.hashCode());
    }

    @Test
    public void testHashCode_NotSame() throws DecoderException {
        LOG.info("HashCode_Notsame");
        TsPacket obj = new TsPacket(Hex.decodeHex(this.pd.p10));
        TsPacket obj2 = new TsPacket(Hex.decodeHex(this.pd.p11));
        assertNotEquals(obj.hashCode(), obj2.hashCode());

    }

    @Test
    public void testToString() throws DecoderException {
        LOG.info("toString");
        TsPacket instance = new TestPacket_SDT().getTarget100();
        LOG.info(instance);
    }

    /**
     * Test of getData method, of class TsPacket.
     */
    @Test
    public void testGetData() throws DecoderException {
        System.out.println("getData");
        TsPacket instance = this.target00;
        byte[] expResult = Hex.decodeHex(this.pd.p10);
        byte[] result = instance.getData();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getPid method, of class TsPacket.
     */
    @Test
    public void testGetPid() {
        LOG.info("");
        TsPacket instance = this.target00;
        int expResult = 0x11;
        int result = instance.getPid();
        assertEquals(expResult, result);
    }

    /**
     * Test of getPid_Const method, of class TsPacket.
     */
    @Test
    public void testGetPid_Const() {
        LOG.info("");
        TsPacket instance = this.target00;
        RESERVED_PROGRAM_ID expResult = SDT_OR_BAT;
        RESERVED_PROGRAM_ID result = instance.getPid_Const();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransport_scrambling_control_Const method, of class TsPacket.
     */
    @Test
    public void testGetTransport_scrambling_control() {
        LOG.info("");
        TsPacket instance = this.target00;
        int expResult = 0;
        int result = instance.getTransport_scrambling_control();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTransport_scrambling_control_Const method, of class TsPacket.
     */
    @Test
    public void testGetTransport_scrambling_control_Const() {
        LOG.info("");
        TsPacket instance = this.target00;
        TsPacket.TRANSPORT_SCRAMBLING_CONTROL expResult = TRANSPORT_SCRAMBLING_CONTROL.NOT_SCRAMBLED;
        TsPacket.TRANSPORT_SCRAMBLING_CONTROL result = instance.getTransport_scrambling_control_Const();
        assertEquals(expResult, result);
    }

}
