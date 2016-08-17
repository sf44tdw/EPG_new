/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package common;

import libepg.epg.section.Section;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.Test;

/**
 * テスト用のSDTデータ
 *
 * @author normal
 */
public final class TestSDT {

    public static final String TEST_SDT_1 = "46f0fe4750fb00000004ff00fcff10ed4820010e0e494d41474943410f21210e54560f1b7ca4deb8ab0e4253fe0f31473268c10184cb300005130010402805170411a80ccb5ae8174b877da3174113edead4a588ad359d478b0f1505faac0d5fc4f5c135f787004a9446b1000403480318b3ce48564148f2b44d77a4bfc0afcbcfb43243467eac492c4d57c7b90daa3f3d397edfcf1b7cb9abd1f90e211b7ddec70d0f21581b7cb9abd1f921591b7dc738213a770daa45454f43cae90d890e303132302d3131332d3333308a2f8931308a0f2141890e32308a0f3b7e0d0e2a0f3a23c0b13243467e4e41890e308a0f315f0e2189338a0f376e4b76dec79d455353";

    public static final String TEST_SDT_2 = "46f13646b1c100000004ff00f1ff10e3481501081b7cb9abd1f90e210a0e42531b7cb9abd1f921c10184cb310005140010114028051704e2d21356ca2e1fa4a74fe1df0c2d6bebb40401004212bab8e1fed64092d330364ca37885cb004a9446b1000403480318b3ce48564148f2b44d77a4bfc0afcbcfb43243467eac492c4d57c7b90daa3f3d397edfcf1b7cb9abd1f90e211b7ddec70d0f21581b7cb9abd1f921591b7dc738213a770daa45454f43cae90d890e303132302d3131332d3333308a2f8931308a0f2141890e32308a0f3b7e0d0e2a0f3a23c0b13243467e4e41890e308a0f315f0e2189338a0f376e4b76dec70348ff001b4816c0081b7cb9abd1f90e210b1b7cb9abd1f90e21aca4c9c101040349ff001d4818c0081b7cb9abd1f90e210d0e42531b7cb9abd1f921c7f9bfc1010402ee209d";

    public static final String TEST_SDT_3_CRCERROR = "46f13646b1c100000004ff00f1ff10e3481501081b7cb9abd1f90e210a0e42531b7cb9abd1f921c10184cb310005140010114028051704e2d21356ca2e1fa4a74fe1df0c2d6bebb40401004212bab8e1fed64092d330364ca37885cb004a9446b1000403480318b3ce48564148f2b44d77a4bfc0afcbcfb43243467eac492c4d57c7b90daa3f3d397edfcf1b7cb9abd1f90e211b7ddec70d0f21581b7cb9abd1f921591b7dc738213a770daa45454f43cae90d890e303132302d3131332d3333308a2f8931308a0f2141890e32308a0f3b7e0d0e2a0f3a23c0b13243467e4e41890e308a0f315f0e2189338a0f376e4b76dec70348ff001b4816c0081b7cb9abd1f90e210b1b7cb9abd1f90e21aca4c9c101040349ff001d4818c0081b7cb9abd1f90e210d0e42531b7cb9abd1f921c7f9bfc1010402eeff9d";

    private static Section getSdt(String sdtHexDump) throws DecoderException {
        char[] xc = sdtHexDump.toCharArray();
        byte[] xb = Hex.decodeHex(xc);
        return new Section(xb);
    }

    public static Section getSdt1() throws DecoderException {
        return getSdt(TestSDT.TEST_SDT_1);
    }

    public static Section getSdt2() throws DecoderException {
        return getSdt(TestSDT.TEST_SDT_2);
    }

    public static Section getSdt3_CRCERROR() throws DecoderException {
        return getSdt(TestSDT.TEST_SDT_3_CRCERROR);
    }

    private TestSDT() {
    }
}
