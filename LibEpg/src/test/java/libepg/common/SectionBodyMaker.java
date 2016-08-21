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
package libepg.common;

import epgtools.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import libepg.epg.section.SectionBody;
import libepg.epg.section.TABLE_ID;
import org.apache.commons.logging.Log;

/**
 *
 * @author normal
 */
public final class SectionBodyMaker {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, SectionBodyMaker.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private SectionBodyMaker() {
    }

    //強制呼び出し
    public static final SectionBody init(TABLE_ID tableID, byte[] data) throws InvocationTargetException {
        try {
            Object[] args = {tableID,data};
            Class<?>[] params = {TABLE_ID.class,byte[].class};
            Constructor<SectionBody> constructor = SectionBody.class.getDeclaredConstructor(params);
            constructor.setAccessible(true);
            SectionBody target = constructor.newInstance(args);
            return target;
        } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException | SecurityException ex) {
            LOG.fatal(ex);
        }
        return null;
    }
}
