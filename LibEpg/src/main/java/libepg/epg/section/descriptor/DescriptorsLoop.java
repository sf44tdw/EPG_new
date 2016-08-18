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
package libepg.epg.section.descriptor;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.logging.Log;
import epgtools.loggerfactory.LoggerFactory;
import libepg.util.bytearray.ByteArraySplitter;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * 記述子ループを保管するクラス。
 *
 * @author normal
 */
public class DescriptorsLoop {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    protected static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, DescriptorsLoop.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final ByteDataBlock data;

    public DescriptorsLoop(byte[] data) {
        this.data = new ByteDataBlock(data);
    }

    /**
     * 記述子ループを取得する。
     *
     * @return 上記の値
     */
    public synchronized byte[] getData() {
        return this.data.getData();
    }

    /**
     * 記述子のリストを取得する。
     * 記述子は記述子タグに従ってキャストされる。キャスト中に問題が発生したか、キャスト先が基底クラスの記述子は、基底クラスとして返す。
     *
     * @return 記述子リスト。
     */
    public synchronized List<Descriptor> getDescriptors_loopList() {
        List<byte[]> t = ByteArraySplitter.splitByLengthField(this.getData(), 2, 1);
        List<Descriptor> dest = new ArrayList<>();
        for (byte[] temp : t) {
            Descriptor desc = new Descriptor(temp);

            Descriptor target = null;
            try {
                Object[] args = {desc};
                Class<?>[] params = {Descriptor.class};
                Constructor<? extends Descriptor> constructor = desc.getDescriptor_tag_const().getDataType().getDeclaredConstructor(params);
                target = constructor.newInstance(args);
            } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException | InvocationTargetException ex) {
                LOG.fatal("キャスト中に問題が発生しました。問題の発生した記述子 = " + desc.toString(), ex);
                target = desc;
            } finally {
                dest.add(target);
            }

        }
        return Collections.unmodifiableList(dest);
    }

    private static final MessageFormat DESC = new MessageFormat(
            "記述子ループ バイト列 = {0}\n"
            + "記述子ループ 記述子 = {1}\n"
    );

    @Override
    public String toString() {
        List<Descriptor> l = this.getDescriptors_loopList();
        StringBuilder s = new StringBuilder();
        for (Descriptor desc : l) {
            s.append(desc);
        }
        Object[] parameters = {this.data, s.toString()};
        return DESC.format(parameters);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(7, 41, this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

}
