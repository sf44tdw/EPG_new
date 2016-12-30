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
package libepg.ts.reader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import libepg.ts.packet.TsPacket;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.logging.Log;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author normal
 */
public class Reader2Test {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Reader2.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final List<byte[]> list_NotContainError = new ArrayList<>();
    private final List<byte[]> list_ContainError_WrongSync = new ArrayList<>();
    private final List<byte[]> list_ContainError_WrongLength = new ArrayList<>();

    private final List<TsPacket> expResult_Normal_And_Wrong_Sync = new ArrayList<>();

    private final List<TsPacket> expResult_Wrong_Length = new ArrayList<>();

    public Reader2Test() throws DecoderException {
        byte[] p_0 = Hex.decodeHex("4701001e33b352c804e0f0b3a0a1305983b300764fa9d4b501d98ef434c00859f5a0e58a8b934013ddc1222e00efe01b060069c006380ed9c002bf1313f92d08ff9e64ca6b8d992107ae01243ab554829e03c0358a9187d5143860679533bccd58f5bb3a5dc25449e9a8ba12e78195ddd15225f0000001093ba4960b82e818fffe99ab09899b4c7cb38bdadd74dd58cb4d751f40e8003517e79849793806511a00e8835f301800040672c8b0900e058b17363407d423c0b05200273f".toCharArray());
        byte[] p_1 = Hex.decodeHex("4701001f888593c06406eeade753789dc03d144f80210f56201ba849e17c78801f883e064fff02305884b8297ff09130076416af8a2200c87c3996eb7805c4476869c45a65d66b0a7839000c0cffff03d000cc23d03a000cc24c0197ffe22d4d001fc01f40333e611f2aa21de001e5093c9c2b0900e60b017f001c406a01a2c8a1c41abac5f229043e27808a2300d43a101601f50cfbd0d3bd0cfc36ddf8072200e789b60684a88a2599da8496835000c01b007503afff9f450f414a".toCharArray());
        byte[] p_2 = Hex.decodeHex("47010010ab0198006849e0dc00311c8a4f1500b8e00f8d68f066001a123b091602e66a0b8068a3c8ca01f51f450f61eca8600b644b18c042a306e480f6532bb071e4136e3c0e401f10439e6a125c4fc02f2707e9601f1b000f9e847b066fffa62c3f99911c552855b7805ef44955133da3aa29d601a35d3255b348bac5f4003e85d32145db2aeeb0476600d29906641eb2231487dc2aea10e7032aecbe39d0bba859d1b4d41d6285e2aec7dd42c986cbbf44d2b024612e328a9d8a51".toCharArray());
        byte[] p_3 = Hex.decodeHex("4701001170179950bba859c0c9fd314b26f1eac3699a36984e96666d62a842999daaeea066c9507c1d454ec22dc150bbbd7758b0131501e855dd4620164f88f77581fcfa3c55154a0463ab8c8da2a8b26ae1d7454e8583394d7eed9f757b9807da17a998278aa2562601f488d620ddd8fe0177454ec09e780d65dda3262a17750be867c65d46948f774391f543ee99c9b5dc0a02ec7a64acbac5626ba85c4c5465154b0b4d85d0f581ba26720d02620033ba66b98d4364c527d32232".toCharArray());
        byte[] p_4 = Hex.decodeHex("47010012eba671ff1310c10c014961671f4552a5d378f1747622515386960c6b97743ba01a27cc069574c468a640c2c5475ddd15c2c5c03e8e80ed2ac09bd40c487194cc585e1974e180fe516842423f9a7a2880a764af1121c7c83fab2ce83031d852ee8a1d8b39a875eba9687b093502bcdda2f60d01e0d45dd628dd284c21b01aaba3d360334c072eeee326b3f405484914df400000010a3ba485ec53b915771806e9a3499923990f8929ee1d9299dc82d00643374af7de00b802".toCharArray());
        byte[] p_5 = Hex.decodeHex("47010013f0c01f7b98fa126647baa6f0707806c17717664f8ea9e468e89f5388d1d3dc2dbde27c4ff53fe1513e27dc20bd9e7b0bb89f4788aa1c754791a3eae46b00e42a4bfb0f308d450e1b6fae23531f6b460750d31a27d4d2344689f758793a20d91623d5c8d11a3e3ea69c222359c3ae15da7ce01144fb8da711a385dce67ced715e6ef1f54dde3eabe8a1c335e60b89d1951e6446b8c3223c4fa980e1fff1f000f8021a9a7c7485a007971a5e38ca27403a1bd6f53c62c00689".toCharArray());
        byte[] p_6 = Hex.decodeHex("47414010003cbffd0200fd0e22110310030fffffffff000fe80200deff000ef9338aaf965ae024f27c359d664b2fd3696adffd5aadc8d388d4c9b39e810cd374b68b90628797291745a21005fda3ed26d13af54307124e3265675942ba7a790f712c938303a416f93194dbeb58e3b6951c861f63c9619ba7d129c17d0c90614da457f19b89f25c23b9c2db80dba2a79af563e02f25cc76b3eec3e5c3e3a3a3e630942ba8f9ba4e90aad5059e642521f286123c0fd62ae0d94e9c3bb4".toCharArray());
        byte[] p_7 = Hex.decodeHex("4701001465ada21e00a36fd82301d806380294a8b1407045d51f1d288932aa84989d5569f454e4cd688d4cc8de26926212b5b71f44cf66624d2987902329a5bd4afce27ddd60bd46b1f190bb3c7d5db3d00e699120eb1601a3b5ddda70cc2e371161d758b2c32efb532169d4282ccbac115994cc50455089c67454e819e74a62988d176f74c01adda7d1571df0dab5a986cc260a7673039997133e39a92bd7018046f60f8bdf6e311dd6dedbf42d6b4268aa59b078b0b89ea8ca2685".toCharArray());
        byte[] p_8 = Hex.decodeHex("47010015012a03a122601a5153d8c45d8eba8291577b17750370c7805d77583b0e23407c891944d240aa81561b76c5ed4f116db254008dc896b61500403cf36d7454f46780d6017515462c035b0ebba81f8c1977454ec179f58ebac8c45f8dba644b7a8db1d9f7759c800d65dd43cee23639dc42866de5dd119a3685ddd19108bf75759217f7af513498242548480398da2a7b9244f80e53205930e434cc5c1ea640dba9dc1c01d44362501ca268b34854a206d5153a74582a8aa6c3".toCharArray());
        byte[] p_9 = Hex.decodeHex("4701621a79a8d9e1a839e1a8a5c2514b87e32d1b72e086e2cd0e390d6cd99085114ca4fdb7eb922358b2520bd84d86749590373b3898bfe08c6732570e939386562ad48e534817b8e3343aee38174d6b019b43c3c71fc955c1164154d5f8e34f4be3db4bd8f8277ebbbfa8c6efb4dd2f59ae33375874a58d6a3aa5f67681cb73274d684c79c45e14cb2b76a25292b72b5322c353a1e6390486f0af2556541758cda64f35adc556d35a4835ed806a3a1967e1d59cf0a9a6bdd86ada0b".toCharArray());
        byte[] p_10 = Hex.decodeHex("470100160da666822a76e877a6606732d28ddb322665101a41d45509d8a9788b4a18b01a4b0e258e85c0768a9f06535cc06da7a2692093f807f2c2ee02177f767001c42a00e6c9d75071eddbb0d80daa32e89a28743040a8ca6f7752a146e07231d5fd2187f96e3aee8e4eb8dce7b41b94b901a784349e058fba1d48cc28964bfe38a700183c120670f80f0d001859f7000000010b3b7216228430c790601e80d699030ea14995cb0b6bb9123cf41a78c900a79400316bd4e58c0018".toCharArray());
        byte[] p_11 = Hex.decodeHex("47010017dc3043ff2e18407bb394064134015c00777a40397b8891433a45d42b71641a35755773e4a218a93d48a127e046c3c1bc006cc226807e2abe6c803bc822e95c03d907000da8a20ab818432616e0244a299af867e4c8c83203d5721b60384410a8340722851120d5000fc4e00883e2f8c6d9557dc56140bc00513c18000577c4890f1820f035ec3c4b29c9e789ba8ad485c93c546dd6483a130a02106320446508f3364e5509330a60390075423b9eaa8aa28385116a00fe29".toCharArray());
        byte[] p_12 = Hex.decodeHex("47014011400be9355b5833559ef3932b0ccac862b5ed77f69facd61646704338f9a8d56986d4092de8eb6871f8f4323a4c2ca223b3cc01133240c25ee8278331221dc4a249f58759e0b92f490548d81c56943993a6d681eab2462f23a462444dc82bb6c213da16f850b52b9049a987a1d5266b4b04cba576558e6fadb4d4289f14c5f16bca2c2f75cc604d917d4cb60c79740b90798af011cf9876027f2547d825d88ba59a5f299076f9b409829d3eb26590748ee99ce4d7127ce0c4".toCharArray());
        byte[] p_13 = Hex.decodeHex("4701001808c2b6a8f6aa9a3283e03fcfe3ee1072073001ba97ad4cc7d079b5ec9124915702acc3e8005445929e344006f495b80d03aae4200e40e903c8240a1141c163cc0a3c4ab9b23609272ee28fef8c121d151e6d48715673c7c2ee8a1dc2eff93c05dc48b8c8a4a3a431094edcf47e84a374ec9cf94fb1b424fa063fff3454698200d782f81c1c16d308f82e8486d40c3fff05080fc26ae51e3c0eadcd898756001d92007ade41d7293a78415a4a4ee44a1d55cdfdfa850fc740".toCharArray());
        byte[] p_14 = Hex.decodeHex("47016019822b7db7a89ff6bca2a62d5a7a33ad2adff5064ee75c77c8d69037925c8efceeb47e3d476aef68123d3abba8b547a176f6ca9dddd507bb0fb7b3776e8f247d7972d58eca5eb4eed40fa34fbae4a6e774fbad03474aa729f74d3bddd96477ef7fec24e31e9aafbcc02ba61537a940e9a9fae47907ed273b6ddf75fb721f39b1b0d07cae2af0548e4ba5e6d9e8a37eee82e7d9e6aeb27a7a30f08baa2ba9ca379bf2ff5bd59afa6b61615ac60f7df7f0b1b48e3dd5d7724974".toCharArray());
        byte[] p_15 = Hex.decodeHex("4701001910808e00e88cf423d54c52c2662d62a9448b80baeadd8dd454e85020a210694184c26384a0cf8307412816d3f427a54e1f43709d74510c50216815405848e820054325600140c95773370ed6fa01424595a014701578ebb88ee3c55a412000c95a4a5180050d450f60169611e28313c079130309622f51441a55655a1c741884961d0620b2eee0ea0e950b833135a030370f9ee06ba3789098a28b8a0cfc75f6b88606244521c5d15d3a79bd143d02269ed2804700344367".toCharArray());
        byte[] p_16 = Hex.decodeHex("4701611d740f686b0ac9b1a0adc90e14f877055681ed7111d22699ff475e808cc722405557ea34404c9257031e01a31c655600ffff25ed06bbaab42c8f6dd047d26ab00fd396c680bdda6c05b09f6b5fa03f90fcaf938181bf81bf81bf81bf81bf81bfbff4378473e7770f9c0aaf211473ecd4c24afce162c13d85d0b9d3955ed327a9288e808ed4ab4c99ecea0d008006f81aaa009dc5ccbaaee50f5ddd31c5d5c977786e66ce3e955a5d65d9e6b8353a79589ce384e58c49132986".toCharArray());
        byte[] p_17 = Hex.decodeHex("4701001ab96134206800a6914510d5003ae2d3e4fba2874ac1cbc2bc304cce3bdc777aed0345c336a2895771c2668fa2874342f5b6274dd336ba289b0e68ef0041339cb8607d144503b71152a8a1d6d8b116f1f71f6e7a2883cc39570a628a912e8a1d2a0077387cce22c0d5d14458184fb0b83952c45dddc1feea89f0cc6bb0fbbac6a23696ff9e01e4b0ea3aee2161fe91337d04cb50fb6a0a1837c4b01e5c4782aae583cf1513e385dcd090b8278899a3afdcf9f1f8d3623c4813".toCharArray());
        byte[] p_18 = Hex.decodeHex("4701001b710d993acc32384d153a160a1f69084752cd0eae35ccbefb6c655124db48d00cab8a9423ddc4881a01044e323cd6b897cf00e6658e8f8f1572c3983e221b6dec65c6680ff660fd5711fe319cf01adb0de6f5c49153b9b72c1cb698609d6102feb9bc8d1204eb509dee32f58756fc054f6117d82ae1079e22f3e00143cec392a9f00ad5750f8d60114fc29a6c226c1fee1afc4b04700397c53c38c8b5076b843326103625f40fce75e0eda5615a69eeb5f02e152c2b4c4f9b".toCharArray());
        byte[] p_19 = Hex.decodeHex("470140120092b99614fd6d964fd364c6a41df26178b840330493e774ba102cbed4d02a5c50d91d505d596cb6315b140cbe5d207feff632f0cff0b0a431a64c24dc310b734d038157cfafd0fa7e31365b1d32988ff9d00847fa4343444411f192b146cda49a1a8e6746864249465e9d423e9f6c30d41ab1f8d410b9f48a58a8c0a78cbc211f2513cd6bd7fb910e903accf55aa2a7b9dd0e0c7bed3836314ba677e33c9155bb6dcb1a1ac2c201f1bbab78f0d8d05022f8c94eb9494208".toCharArray());
        byte[] p_20 = Hex.decodeHex("4701001c5dfc0108cae58854c80e7982aeb1b9ff94261982bdd73ebbe46b1821bd8cc2aea3f5f88ee39a5056883454eaddc0adc9590ee1531d4ce887a220f364eb87c182602c9e99f2deb157a0270430077a268a7775ee45b950aa2a74ac20aae11752261c32eaedb27a6768bac98ef756217990552bb9d4550826307c76d646b4d9202e8e0fbb819601fce076893701dbba2e1cec46bed9d622924796b2734076ddb30ff70f183ed6450f94665b5af922dd0686a75e5893953cea1f".toCharArray());
        byte[] p_21 = Hex.decodeHex("4701001d89f63e36ea74287b1d1b1c03385d153e830e03730fc1d1de14793a4917515421a25753cbd94cf2b49568837185003f9d83e5b1fa09ca1007641c1747f99e70d03675521090cdba791ebb64a06d9c30ae4b57c1553487d1c1314713a42625869199e8424f1d3867082c80f3d2360000010c3ba49467e7760eb8ce9829c3eeeff8e71e6780670cea5b6e2eea08284e12d13e981840065ddb0bee208b9a012e94256b327a2a9366261485f627bc00f80340d3141710899f0697".toCharArray());
        byte[] p_22 = Hex.decodeHex("4701001e95f0f802e01d60dc4bfc95c3b4867927508fad5470bbee0c5fff535e5459d424f0178e0e3fbc02f23007ad1370418e3967ac28df01a144f01f33d5c9c2c0801df01e37011001d43cd124487f270ba11fa7384872f380d7984f89b805204a6593c78b2372063ef3a6bfcaf1d0aafc8de46ca908ac0e7fff424f01f0e47039ed5c7f1f8837987000f04e8d02629c18bffeaf8b01104810ce045a305813eb1c2009f1f085800f09eb9805c24d146af5153915bbc83641b42472".toCharArray());
        list_NotContainError.add(p_0);
        list_NotContainError.add(p_1);
        list_NotContainError.add(p_2);
        list_NotContainError.add(p_3);
        list_NotContainError.add(p_4);
        list_NotContainError.add(p_5);
        list_NotContainError.add(p_6);
        list_NotContainError.add(p_7);
        list_NotContainError.add(p_8);
        list_NotContainError.add(p_9);
        list_NotContainError.add(p_10);
        list_NotContainError.add(p_11);
        list_NotContainError.add(p_12);
        list_NotContainError.add(p_13);
        list_NotContainError.add(p_14);
        list_NotContainError.add(p_15);
        list_NotContainError.add(p_16);
        list_NotContainError.add(p_17);
        list_NotContainError.add(p_18);
        list_NotContainError.add(p_19);
        list_NotContainError.add(p_22);

        //同期ワード化け
        byte[] p_20_ws = Arrays.copyOf(p_20, p_20.length);
        p_20_ws[0] = 0x44;
        byte[] p_21_ws = Arrays.copyOf(p_21, p_21.length);
        p_21_ws[0] = 0x45;
        list_ContainError_WrongSync.add(p_0);
        list_ContainError_WrongSync.add(p_1);
        list_ContainError_WrongSync.add(p_2);
        list_ContainError_WrongSync.add(p_3);
        list_ContainError_WrongSync.add(p_4);
        list_ContainError_WrongSync.add(p_5);
        list_ContainError_WrongSync.add(p_6);
        list_ContainError_WrongSync.add(p_7);
        list_ContainError_WrongSync.add(p_8);
        list_ContainError_WrongSync.add(p_9);
        list_ContainError_WrongSync.add(p_10);
        list_ContainError_WrongSync.add(p_11);
        list_ContainError_WrongSync.add(p_12);
        list_ContainError_WrongSync.add(p_13);
        list_ContainError_WrongSync.add(p_14);
        list_ContainError_WrongSync.add(p_15);
        list_ContainError_WrongSync.add(p_16);
        list_ContainError_WrongSync.add(p_17);
        list_ContainError_WrongSync.add(p_18);
        list_ContainError_WrongSync.add(p_19);
        list_ContainError_WrongSync.add(p_20_ws);
        list_ContainError_WrongSync.add(p_21_ws);
        list_ContainError_WrongSync.add(p_22);
        
        //データ不整合の所為で1バイト分余計な部分がある
        byte[] p_4_long = Arrays.copyOf(p_4, p_4.length + 1);
        //データ不整合の所為で1バイト分無くなっている。
        byte[] p_12_short = Arrays.copyOf(p_12, p_12.length - 1);
        list_ContainError_WrongLength.add(p_0);
        list_ContainError_WrongLength.add(p_1);
        list_ContainError_WrongLength.add(p_2);
        list_ContainError_WrongLength.add(p_3);
        list_ContainError_WrongLength.add(p_4_long);
        list_ContainError_WrongLength.add(p_5);
        list_ContainError_WrongLength.add(p_6);
        list_ContainError_WrongLength.add(p_7);
        list_ContainError_WrongLength.add(p_8);
        list_ContainError_WrongLength.add(p_9);
        list_ContainError_WrongLength.add(p_10);
        list_ContainError_WrongLength.add(p_11);
        list_ContainError_WrongLength.add(p_12_short);
        list_ContainError_WrongLength.add(p_13);
        list_ContainError_WrongLength.add(p_14);
        list_ContainError_WrongLength.add(p_15);
        list_ContainError_WrongLength.add(p_16);
        list_ContainError_WrongLength.add(p_17);
        list_ContainError_WrongLength.add(p_18);
        list_ContainError_WrongLength.add(p_19);
        list_ContainError_WrongLength.add(p_20);
        list_ContainError_WrongLength.add(p_21);
        list_ContainError_WrongLength.add(p_22);

        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_0));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_1));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_2));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_3));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_4));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_5));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_6));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_7));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_8));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_9));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_10));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_11));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_12));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_13));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_14));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_15));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_16));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_17));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_18));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_19));
        expResult_Normal_And_Wrong_Sync.add(new TsPacket(p_22));

        //データ不整合の所為で次のパケットの1バイト目が末尾に付いているパケット。
        byte[] p_12_short_plus_sync = ArrayUtils.add(p_12_short, (byte) 0x47);
        expResult_Wrong_Length.add(new TsPacket(p_0));
        expResult_Wrong_Length.add(new TsPacket(p_1));
        expResult_Wrong_Length.add(new TsPacket(p_2));
        expResult_Wrong_Length.add(new TsPacket(p_3));
        expResult_Wrong_Length.add(new TsPacket(p_4));
        expResult_Wrong_Length.add(new TsPacket(p_5));
        expResult_Wrong_Length.add(new TsPacket(p_6));
        expResult_Wrong_Length.add(new TsPacket(p_7));
        expResult_Wrong_Length.add(new TsPacket(p_8));
        expResult_Wrong_Length.add(new TsPacket(p_9));
        expResult_Wrong_Length.add(new TsPacket(p_10));
        expResult_Wrong_Length.add(new TsPacket(p_11));
        expResult_Wrong_Length.add(new TsPacket(p_12_short_plus_sync));
        expResult_Wrong_Length.add(new TsPacket(p_14));
        expResult_Wrong_Length.add(new TsPacket(p_15));
        expResult_Wrong_Length.add(new TsPacket(p_16));
        expResult_Wrong_Length.add(new TsPacket(p_17));
        expResult_Wrong_Length.add(new TsPacket(p_18));
        expResult_Wrong_Length.add(new TsPacket(p_19));
        expResult_Wrong_Length.add(new TsPacket(p_20));
        expResult_Wrong_Length.add(new TsPacket(p_21));
        expResult_Wrong_Length.add(new TsPacket(p_22));
    }

    private File writeBytesToFile(List<byte[]> dataList) throws IOException {
        final String prefix = "bar";
        final String suffix = "ts"; // null の場合は ".tmp" になる。
        OutputStream os = null;
        File tmp = null;

        tmp = File.createTempFile(prefix, suffix);
        os = new FileOutputStream(tmp);
        for (byte[] b : dataList) {
            os.write(b);
        }
        os.close();

        return tmp;
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

//    /**
//     * Test of getPackets method, of class Reader2.
//     */
//    @Test
//    public void testGetPackets() throws FileNotFoundException {
//        System.out.println("getPackets");
//        Reader2 instance = new Reader2(TsFile.getTsFile(), 23L);
//        List<TsPacket> result = instance.getPackets();
//
//        long count = 0;
//
//        for (TsPacket p : result) {
//            StringBuilder sb1 = new StringBuilder();
//            sb1.append("p_");
//            sb1.append(count);
//
//            StringBuilder sb2 = new StringBuilder();
//            sb2.append("byte[] ");
//            sb2.append(sb1.toString());
//            sb2.append(" = ");
//            sb2.append("Hex.decodeHex(\"");
//            sb2.append(Hex.encodeHexString(p.getData()));
//            sb2.append("\".toCharArray()");
//            sb2.append(");");
//            sb2.append("  list_NotContainError.add(");
//            sb2.append(sb1.toString());
//            sb2.append(");");
//            System.out.println(sb2.toString());
//            count++;
//        }
//    }
    private void dumpBytes(List<byte[]> target) {
        for (byte[] b : target) {
            LOG.info(Hex.encodeHexString(b));
        }
        LOG.info("");
    }

    private void dumpResult(List<TsPacket> target) {
        for (TsPacket p : target) {
            LOG.info(Hex.encodeHexString(p.getData()));
        }
        LOG.info("");
    }

    /**
     * Test of getPackets method, of class Reader2.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPackets_Normal() throws IOException {
        LOG.info("getPackets_Normal");
        File temp_NotContainError = this.writeBytesToFile(this.list_NotContainError);
        Reader2 instance = new Reader2(temp_NotContainError);
        List<TsPacket> _expResult = this.expResult_Normal_And_Wrong_Sync;
        List<TsPacket> result = instance.getPackets();
        LOG.info("expResult");
        dumpResult(_expResult);
        LOG.info("result");
        dumpResult(result);
        assertEquals(ListUtils.isEqualList(_expResult, result), true);
    }

    /**
     * Test of getPackets method, of class Reader2.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPackets_WrongSync() throws IOException {
        LOG.info("getPackets_WrongSync");
        File temp_ContainError_WrongSync = this.writeBytesToFile(this.list_ContainError_WrongSync);
        Reader2 instance = new Reader2(temp_ContainError_WrongSync);
        List<TsPacket> _expResult = this.expResult_Normal_And_Wrong_Sync;
        List<TsPacket> result = instance.getPackets();
        LOG.info("expResult");
        dumpResult(_expResult);
        LOG.info("result");
        dumpResult(result);
        assertEquals(ListUtils.isEqualList(_expResult, result), true);
    }

    /**
     * Test of getPackets method, of class Reader2.
     *
     * @throws java.io.IOException
     */
    @Test
    public void testGetPackets_WrongLength() throws IOException {
        LOG.info("getPackets_WrongLength");
        File temp_ContainError_WrongLength = this.writeBytesToFile(this.list_ContainError_WrongLength);
        Reader2 instance = new Reader2(temp_ContainError_WrongLength);
        List<TsPacket> _expResult = this.expResult_Wrong_Length;
        List<TsPacket> result = instance.getPackets();
        LOG.info("expResult");
        dumpResult(_expResult);
        LOG.info("result");
        dumpResult(result);
        assertEquals(ListUtils.isEqualList(_expResult, result), true);
    }
}
