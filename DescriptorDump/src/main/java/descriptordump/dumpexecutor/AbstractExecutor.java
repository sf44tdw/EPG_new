/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package descriptordump.dumpexecutor;

import java.lang.invoke.MethodHandles;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.descriptor.Descriptor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author normal
 */
public abstract class AbstractExecutor {

    protected static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    private final Set<TABLE_ID> tids;

    private final Set<Descriptor> dest = Collections.synchronizedSet(new HashSet<>());

    protected AbstractExecutor(TABLE_ID tid, TABLE_ID... tids) {
        List<TABLE_ID> t = new ArrayList<>();
        if (tid != null) {
            t.add(tid);
        } else {
            throw new NullPointerException("テーブルIDが指定されていません。");
        }
        if (tids != null) {
            t.addAll(Arrays.asList(tids));
        }
        Set<TABLE_ID> temp = Collections.synchronizedSet(new HashSet<>());
        temp.addAll(t);
        this.tids = Collections.unmodifiableSet(temp);

    }

    public void setSection(Section section) {

    }

    public final Set<TABLE_ID> getTids() {
        return tids;
    }

    protected final Set<Descriptor> getDest() {
        return dest;
    }

    public final Set<Descriptor> getUnmodifiableDest() {
        return Collections.unmodifiableSet(dest);
    }

    public final void process(Section section) {
        if (!this.tids.contains(section.getTable_id_const())) {
            MessageFormat msg = new MessageFormat("セクションのテーブルIDが想定外の値です。このセクションは無視されます。セクションのID={0}");
            Object[] parameters = {Integer.toHexString(section.getTable_id())};
            LOG.error(msg.format(parameters));
        } else {
            this._process(this.getDest(), section);
        }
    }

    protected abstract void _process(Set<Descriptor> target, Section s);
}
