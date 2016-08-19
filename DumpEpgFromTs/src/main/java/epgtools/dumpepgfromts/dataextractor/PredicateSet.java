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
package epgtools.dumpepgfromts.dataextractor;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.collections4.keyvalue.MultiKey;

/**
 * 事前に設定したトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を持つデータのみを重複なしで保管するセットのラッパー。
 *
 * @author normal
 * @param <T> 格納データ型
 *
 * @see java.util.HashSet
 */
public class PredicateSet<T extends DataObject> extends DataObject implements Set<T> {

    private final Set<T> set;

    public PredicateSet(int transport_stream_id, int original_network_id, int service_id, Set<T> set) throws IllegalArgumentException {
        super(transport_stream_id, original_network_id, service_id);
        this.set = set;
    }

    private boolean predicateIt(T e) {
        if ((this.getTransport_stream_id() == e.getTransport_stream_id()) && (this.getService_id() == e.getService_id()) && (this.getOriginal_network_id() == e.getOriginal_network_id())) {
            return true;
        } else {
            return false;
        }
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
    public Iterator<T> iterator() {
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
     * <br>基本的には上記。ただし、事前に設定したトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を持つ要素のみを追加する。
     */
    @Override
    public boolean add(T e) {
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
     * <br>基本的には上記。ただし、事前に設定したトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別を持つ要素のみを追加する。
     */
    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean ret = false;
        for (T val : c) {
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
        return set.containsAll(c);
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
     *
     * @return このオブジェクトが管理しているSetの変更不可能なビュー
     */
    public Set<T> getSet() {
        return Collections.unmodifiableSet(set);
    }

    /**
     * このオブジェクトが管理しているSetの変更可能なビューを、このオブジェクトが管理しているトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別から生成したキーと関連付けてマップに追加する。
     *
     * @param dest 追加するマップ
     * @return 下記参照
     * @see java.util.Map#put(java.lang.Object, java.lang.Object)
     */
    public Set<T> putToMap(Map<MultiKey<Integer>, PredicateSet<T>> dest) {
        return dest.put(this.getMuiltiKey(), new PredicateSet(this.getTransport_stream_id(),this.getOriginal_network_id(),this.getService_id(),Collections.unmodifiableSet(this.getSet())));
    }

}
