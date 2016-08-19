/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epgtools.dumpepgfromts.test.common;

import org.apache.commons.codec.DecoderException;
import org.junit.Test;

/**
 *
 * @author normal
 */
public class DumpTest {

    public DumpTest() {
    }

    @Test
    public void dump() throws DecoderException {
        System.out.println("\n***********************************************************************************************************************************************************************************************");
        System.out.println(TestSection.getSdt1());
        System.out.println("\n***********************************************************************************************************************************************************************************************");
        System.out.println("\n***********************************************************************************************************************************************************************************************");
        System.out.println(TestSection.getSdt2());
        System.out.println("\n***********************************************************************************************************************************************************************************************");
    }
}
