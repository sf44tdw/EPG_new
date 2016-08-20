/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package descriptordump.dumpexecutor.eit;

import descriptordump.dumpexecutor.AbstractExecutor;
import java.util.List;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.eventinformationtable.EventInformationTableBody;
import libepg.epg.section.body.eventinformationtable.EventInformationTableRepeatingPart;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.DescriptorsLoop;

/**
 *
 * @author normal
 */
public class EitDescGetter extends AbstractExecutor {

    public EitDescGetter() {
        super(TABLE_ID.EIT_THIS_STREAM_NOW_AND_NEXT, TABLE_ID.EIT_THIS_STREAM_8_DAYS, TABLE_ID.EIT_OTHER_STREAM_NOW_AND_NEXT, TABLE_ID.EIT_OTHER_STREAM_8_DAYS);
    }

    @Override
    protected void _process(Set<Descriptor> target, Section s) {
        EventInformationTableBody eitb = (EventInformationTableBody) s.getSectionBody();
        List<EventInformationTableRepeatingPart> rplist = eitb.getEITRepeatingPartList();
        for (EventInformationTableRepeatingPart rp : rplist) {
            DescriptorsLoop dl = rp.getDescriptors_loop();
            for (Descriptor desc : dl.getDescriptors_loopList()) {
                target.add(desc);
            }
        }
    }

}
