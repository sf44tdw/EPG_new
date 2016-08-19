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
package epgtools.dumpepgfromts.dataextractor.programme;

import epgtools.dumpepgfromts.dataextractor.DataObject;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 事前に設定したトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を持つ番組データのみを保管するセットのラッパー。
 *
 * @author normal
 */
public class ProgrammeSetWrapper extends DataObject implements Set<Programme> {

    private final Set<Programme> set;

    public ProgrammeSetWrapper(Set<Programme> set, int transport_stream_id, int original_network_id, int service_id) throws IllegalArgumentException {
        super(transport_stream_id, original_network_id, service_id);
        this.set = set;
    }

    private boolean predicateIt(Programme e) {
        return (this.getOriginal_network_id() == e.getOriginal_network_id()) && (this.getService_id() == e.getService_id()) && (this.getOriginal_network_id() == e.getOriginal_network_id());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return set.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return set.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean contains(Object o) {
        return set.contains(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Iterator<Programme> iterator() {
        return set.iterator();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object[] toArray() {
        return set.toArray();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T[] toArray(T[] a) {
        return set.toArray(a);
    }

    /**
     * {@inheritDoc}
     * 基本的には上記。ただし、事前に設定したトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を持つ要素のみを追加する。
     */
    @Override
    public boolean add(Programme e) {
        if (predicateIt(e)) {
            return this.set.add(e);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object o) {
        return set.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsAll(Collection<?> c) {
        return set.containsAll(c);
    }

    /**
     * {@inheritDoc}
     * 基本的には上記。ただし、事前に設定したトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を持つ要素のみを追加する。
     */
    @Override
    public boolean addAll(Collection<? extends Programme> c) {
        boolean ret = false;
        for (Programme val : c) {
            if (this.add(val)) {
                ret = true;
            }
        }
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean retainAll(Collection<?> c) {
        return set.retainAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeAll(Collection<?> c) {
        return set.removeAll(c);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        set.clear();
    }

    /**
     * @return 編集できないセットを返す。
     */
    public ProgrammeSetWrapper getUnmodifiableSet() {
        Set<Programme> tempSet = new HashSet<>();
        return new ProgrammeSetWrapper(Collections.unmodifiableSet(this.set), this.getTransport_stream_id(), this.getOriginal_network_id(), this.getService_id());
    }

}
