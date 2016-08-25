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
package libepg.epg.section;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import libepg.epg.section.sectionreconstructor.SectionReconstructor;
import libepg.ts.packet.RESERVED_PROGRAM_ID;
import libepg.ts.packet.TsPacketParcel;
import libepg.ts.reader.TsReader;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.logging.Log;

/**
 * ファイルを指定のパケット数だけ読み、セクションを再構成してpid毎に分別する。重複したセクションは無視する。
 * 
 * @author normal
 */
public final class SectionLoader {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, SectionLoader.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    private final File tsFile;
    private final Long readLimit;
    private final Set<Integer> pids;

    public SectionLoader(File tsFile, Long readLimit, Set<Integer> pids) {
        this.tsFile = new File(tsFile.getAbsolutePath());
        this.readLimit = readLimit;
        this.pids=pids;
    }

    public Map<Integer, List<Section>> load() throws FileNotFoundException {

        //ファイルを開く。
        if (!tsFile.isFile()) {
            throw new FileNotFoundException("ファイルがありません。パス = " + tsFile.getAbsolutePath());
        }

        LOG.info("読み込み対象ファイル = " + tsFile.getAbsolutePath());
        final TsReader reader = new TsReader(tsFile, pids, readLimit);
        Map<Integer, List<TsPacketParcel>> pid_packets = reader.getPackets();

        Map<Integer, List<Section>> pids_sections_temp = new ConcurrentHashMap<>();
        for (Integer pidKey : pid_packets.keySet()) {
            LOG.info("処理対象pid = " + Integer.toHexString(pidKey) + " pid定数 = " + RESERVED_PROGRAM_ID.reverseLookUp(pidKey));
            SectionReconstructor sectionMaker = new SectionReconstructor(pid_packets.get(pidKey), pidKey);
            List<Section> sections = sectionMaker.getSections();
            if (sections != null) {
                LOG.info("セクション数 = " + sections.size());
                pids_sections_temp.put(pidKey, sections);
            }
        }
        return Collections.unmodifiableMap(pids_sections_temp);

    }

    public File getTsFile() {
        return new File(tsFile.getAbsolutePath());
    }

}
