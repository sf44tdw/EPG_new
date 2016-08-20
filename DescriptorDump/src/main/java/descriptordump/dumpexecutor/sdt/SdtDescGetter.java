/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package descriptordump.dumpexecutor.sdt;

import descriptordump.dumpexecutor.AbstractExecutor;
import java.util.List;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableBody;
import libepg.epg.section.body.servicedescriptiontable.ServiceDescriptionTableRepeatingPart;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.DescriptorsLoop;

/**
 * SDT内の記述子をすべて取得する。
 *
 * @author normal
 */
public class SdtDescGetter extends AbstractExecutor {

    public SdtDescGetter() {
        super(TABLE_ID.SDT_OTHER_STREAM, TABLE_ID.SDT_THIS_STREAM);
    }

    @Override
    protected void _process(Set<Descriptor> target, Section s) {
        ServiceDescriptionTableBody sdtb = (ServiceDescriptionTableBody) s.getSectionBody();
        List<ServiceDescriptionTableRepeatingPart> rplist = sdtb.getSDTRepeatingPartList();
        for (ServiceDescriptionTableRepeatingPart rp : rplist) {
            DescriptorsLoop dl = rp.getDescriptors_loop();
            for (Descriptor desc : dl.getDescriptors_loopList()) {
                target.add(desc);
            }
        }
    }

}
