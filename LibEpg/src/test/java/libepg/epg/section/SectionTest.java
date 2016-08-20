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

import libepg.util.bytearray.ByteConverter;
import java.lang.invoke.MethodHandles;
import libepg.common.packet.TestPacket_SDT;
import epgtools.loggerfactory.LoggerFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import testtool.EqualsChecker;
import testtool.testrule.regexmessagerule.ExpectedExceptionMessage;
import testtool.testrule.regexmessagerule.ExpectedExceptionRule;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/*
バイト列 = 42f0977fe1d100007fe1ff0408f30020481201000f0e4e484b451d461d6c310f456c357ec10184cf0701fe08f00104080409f3001c481201000f0e4e484b451d461d6c320f456c357ec10184cf0302fe08040af3001c481201000f0e4e484b451d461d6c330f456c357ec10184cf0302fe080588e5001f480ec0000b0e4e484b0f374842530e32c10188cf0a030e4e484b0f215d0e32722fa2b5ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
セクション共通項目(
table_id:42
(section_syntax_indicator=1,reserved_future_use=1,reserved=3,section_length=151):f097
)
SDT固有項目(
transport_stream_id:7fe1
(reserved=3,version_number=8,current_next_indicator=1):d1
section_number:00
last_section_number:00
original_network_id:7fe1
reserved_future_use:ff

残りの部分 = 0408f30020481201000f0e4e484b451d461d6c310f456c357ec10184cf0701fe08f00104080409f3001c481201000f0e4e484b451d461d6c320f456c357ec10184cf0302fe08040af3001c481201000f0e4e484b451d461d6c330f456c357ec10184cf0302fe080588e5001f480ec0000b0e4e484b0f374842530e32c10188cf0a030e4e484b0f215d0e32722fa2b5ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff
1個目(
service_id:0408
(reserved_future_use=7,EIT_user_defined_flags=4,EIT_schedule_flag=1,EIT_present_following_flag=1):f3
(running_status=0,free_CA_mode=0,descriptors_loop_length=32):0020
481201000f0e4e484b451d461d6c310f456c357ec10184cf0701fe08f0010408
)
2個目(
service_id:0409
(reserved_future_use=7,EIT_user_defined_flags=4,EIT_schedule_flag=1,EIT_present_following_flag=1):f3
(running_status=0,free_CA_mode=0,descriptors_loop_length=28):001c
481201000f0e4e484b451d461d6c320f456c357ec10184cf0302fe08
)
3個目(
service_id:040a
(reserved_future_use=7,EIT_user_defined_flags=4,EIT_schedule_flag=1,EIT_present_following_flag=1):f3
(running_status=0,free_CA_mode=0,descriptors_loop_length=28):001c
481201000f0e4e484b451d461d6c330f456c357ec10184cf0302fe08
)
4個目(
service_id:0588
(reserved_future_use=7,EIT_user_defined_flags=1,EIT_schedule_flag=0,EIT_present_following_flag=1):e5
(running_status=0,free_CA_mode=0,descriptors_loop_length=31):001f
480ec0000b0e4e484b0f374842530e32c10188cf0a030e4e484b0f215d0e32
)
CRC:722fa2b5
 */
/**
 *
 * @author normal
 */
public class SectionTest {
    
    private static final Log LOG;
    
    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Section.CLASS_LOG_OUTPUT_MODE).getLOG();
    }
    @Rule
    public ExpectedExceptionRule rule = new ExpectedExceptionRule();
    
    private final byte[] section_byte1;
    private final Section testSection_OK1;
    private final Section testSection_OK2;
    
    public SectionTest() throws DecoderException {
        this.section_byte1 = Hex.decodeHex("42f0977fe1d100007fe1ff0408f30020481201000f0e4e484b451d461d6c310f456c357ec10184cf0701fe08f00104080409f3001c481201000f0e4e484b451d461d6c320f456c357ec10184cf0302fe08040af3001c481201000f0e4e484b451d461d6c330f456c357ec10184cf0302fe080588e5001f480ec0000b0e4e484b0f374842530e32c10188cf0a030e4e484b0f215d0e32722fa2b5ffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".toCharArray());
        this.testSection_OK1 = new Section(this.section_byte1);
        this.testSection_OK2 = new Section(this.section_byte1);
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

//テーブルID定数は、テーブルIDの取りうる値すべてを網羅しているので、このエラーは発生しない。
//    @Test
//    @ExpectedExceptionMessage("^テーブル識別値が見つかりません.*")
//    public void testConstructor_2() {
//        LOG.info("異常系_記述子タグ相違");
//        
//        Section instance = new Section(TABLE_ID.OTHER_TABLE_IDS, this.testSection_OK.getData());
//    }
    @Test
    @ExpectedExceptionMessage("^セクション長フィールドの値が大きすぎます.*")
    public void testConstructor_3() {
        LOG.info("異常系_セクション長フィールド値最大値超過");
        byte[] wrong02 = this.testSection_OK1.getData();
        wrong02[1] = 0xf;
        wrong02[2] = 0xf;
        Section instance = new Section(wrong02);
    }
    
    @Test
    @ExpectedExceptionMessage("配列切り詰め中に問題発生。")
    public void testConstructor_4() throws DecoderException {
        LOG.info("異常系_セクション長フィールドの値より渡された配列が短い。");
        Section instance = new Section(Hex.decodeHex("09f203110fff1f6a706e540211ff2edd7299ffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffffff".toCharArray()));
    }

    /**
     * Test of getPreferedTableID method, of class Section.
     */
    @Test
    public void testGetTable_id_const() {
        LOG.info("getTable_id_const");
        Section instance = this.testSection_OK1;
        TABLE_ID expResult = TABLE_ID.SDT_THIS_STREAM;
        TABLE_ID result = instance.getTable_id_const();
        assertEquals(expResult, result);
    }

    /**
     * Test of getTable_id method, of class Section.
     */
    @Test
    public void testGetTable_id() {
        LOG.info("getTable_id");
        Section instance = this.testSection_OK1;
        int expResult = 0x42;
        int result = instance.getTable_id();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSection_syntax_indicator method, of class Section.
     */
    @Test
    public void testGetSection_syntax_indicator() {
        LOG.info("getSection_syntax_indicator");
        Section instance = this.testSection_OK1;
        int expResult = 1;
        int result = instance.getSection_syntax_indicator();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReservedFutureUse1 method, of class Section.
     */
    @Test
    public void testGetReservedFutureUse1() {
        LOG.info("getReservedFutureUse1");
        Section instance = this.testSection_OK1;
        int expResult = 1;
        int result = instance.getReservedFutureUse1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getReserved1 method, of class Section.
     */
    @Test
    public void testGetReserved1() {
        LOG.info("getReserved1");
        Section instance = this.testSection_OK1;
        int expResult = 3;
        int result = instance.getReserved1();
        assertEquals(expResult, result);
    }

    /**
     * Test of getSectionLength method, of class Section.
     */
    @Test
    public void testGetSectionLength() {
        LOG.info("getSectionLength");
        Section instance = this.testSection_OK1;
        int expResult = 0x097;
        int result = instance.getSectionLength();
        assertEquals(expResult, result);
    }

    /**
     * Test of getData method, of class Section.
     */
    @Test
    public void testGetSectionBody() throws DecoderException {
        LOG.info("getSectionBody");
        Section instance = this.testSection_OK1;
        byte[] expResult = Hex.decodeHex("7fe1d100007fe1ff0408f30020481201000f0e4e484b451d461d6c310f456c357ec10184cf0701fe08f00104080409f3001c481201000f0e4e484b451d461d6c320f456c357ec10184cf0302fe08040af3001c481201000f0e4e484b451d461d6c330f456c357ec10184cf0302fe080588e5001f480ec0000b0e4e484b0f374842530e32c10188cf0a030e4e484b0f215d0e32".toCharArray());
        byte[] result = instance.getSectionBody().getData();
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getTable_id_extension method, of class Section.
     */
    @Test
    public void testGetTable_id_extension() {
        LOG.info("getTable_id_extension");
        Section instance = this.testSection_OK1;
        int expResult = 0x7fe1;
        int result = instance.getTable_id_extension();
        assertEquals(expResult, result);
    }

    /**
     * Test of getCRC method, of class Section.
     */
    @Test
    public void testGetCRC() throws DecoderException {
        LOG.info("getCRC");
        Section instance = this.testSection_OK1;
        int expResult = ByteConverter.bytesToInt(Hex.decodeHex("722fa2b5".toCharArray()));
        LOG.info(expResult);
        int result = instance.getCRC();
        LOG.info(result);
        assertEquals(expResult, result);
    }

    /**
     * Test of checkCRC method, of class Section.
     */
    @Test
    public void testCheckCRC() {
        LOG.info("checkCRC");
        Section instance = this.testSection_OK1;
        Section.CRC_STATUS expResult = Section.CRC_STATUS.NO_CRC_ERROR;
        Section.CRC_STATUS result = instance.checkCRC();
        assertEquals(expResult, result);
    }

    /**
     * Test of toString method, of class Section.
     */
    @Test
    public void testToString() {
        LOG.info("toString");
        Section instance = this.testSection_OK1;
        LOG.info(instance);
    }

    /**
     * Test of hashCode method, of class Section.
     *
     * @throws org.apache.commons.codec.DecoderException
     */
    @Test
    public void testHashCode() throws DecoderException {
        LOG.info("hashCode");
        Section instance1 = this.testSection_OK1;
        Section instance2 = this.testSection_OK2;
        assertTrue(instance1.hashCode() == instance2.hashCode());
        
        byte[] b = ArrayUtils.remove(new TestPacket_SDT().getTarget200().getPayload(), 0);
        Section instance3 = new Section(b);

//        System.out.println(instance1);
//        System.out.println(instance3);
        assertTrue(instance1.hashCode() != instance3.hashCode());
    }

    /**
     * Test of equals method, of class Section.
     */
    @Test
    public void testEquals() {
        LOG.info("equals_ok");
        EqualsChecker<Section> ec = new EqualsChecker<>();
        Section instance1 = this.testSection_OK1;
        Section instance2 = this.testSection_OK2;
        Section instance3 = this.testSection_OK1;
        boolean result = ec.check_same(instance1, instance1, instance1);
        boolean expResult = true;
        assertEquals(expResult, result);
    }

    /**
     * Test of equals method, of class Section.
     */
    @Test
    public void testEquals_ng() throws DecoderException {
        LOG.info("equals_ng");
        Section instance1 = this.testSection_OK1;
        
        byte[] b = ArrayUtils.remove(new TestPacket_SDT().getTarget200().getPayload(), 0);
        Section instance2 = new Section(b);
        
        boolean result = instance1.equals(instance2);
        boolean expResult = false;
        assertEquals(expResult, result);
    }

    /**
     * Test of getData method, of class Section.
     */
    @Test
    public void testGetData() throws DecoderException {
        LOG.info("getData");
        Section instance = testSection_OK1;
        byte[] expResult = Hex.decodeHex("42f0977fe1d100007fe1ff0408f30020481201000f0e4e484b451d461d6c310f456c357ec10184cf0701fe08f00104080409f3001c481201000f0e4e484b451d461d6c320f456c357ec10184cf0302fe08040af3001c481201000f0e4e484b451d461d6c330f456c357ec10184cf0302fe080588e5001f480ec0000b0e4e484b0f374842530e32c10188cf0a030e4e484b0f215d0e32722fa2b5".toCharArray());
        byte[] result = instance.getData();
//        LOG.info(Hex.encodeHexString(expResult));
//        LOG.info(Hex.encodeHexString(result));
        assertArrayEquals(expResult, result);
        
    }
    
}
