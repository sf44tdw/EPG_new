/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package descriptordump.dumpexecutor.nit;

import descriptordump.dumpexecutor.AbstractExecutor;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.networkinformationtable.NetworkInformationTableBody;
import libepg.epg.section.body.networkinformationtable.TransportStreamLoop;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.DescriptorsLoop;

/**
 *
 * @author normal
 */
public class NitDescGetter extends AbstractExecutor {

    public NitDescGetter() {
        super(TABLE_ID.NIT_THIS_NETWORK, TABLE_ID.NIT_OTHER_NETWORK);
    }

    @Override
    protected void _process(Set<Descriptor> target, Section s) {
        NetworkInformationTableBody nitb = (NetworkInformationTableBody) s.getSectionBody();
        DescriptorsLoop dl1 = nitb.getDescriptors_loop();
        for (Descriptor desc : dl1.getDescriptors_loopList()) {
            target.add(desc);
        }
        for (TransportStreamLoop tsl : nitb.getTransport_streams_loop()) {
            for (Descriptor desc2 : tsl.getDescriptors_loop().getDescriptors_loopList()) {
                target.add(desc2);
            }
        }

    }

}
