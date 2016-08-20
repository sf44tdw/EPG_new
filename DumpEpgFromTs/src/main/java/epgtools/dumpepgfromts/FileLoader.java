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
package epgtools.dumpepgfromts;

import epgtools.dumpepgfromts.dataextractor.channel.Channel;
import epgtools.dumpepgfromts.dataextractor.channel.ChannelDataExtractor;
import epgtools.dumpepgfromts.dataextractor.programme.Programme;
import epgtools.dumpepgfromts.dataextractor.programme.ProgrammeDataExtractor;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import libepg.epg.section.Section;
import libepg.epg.section.sectionreconstructor.SectionReconstructor;
import libepg.ts.packet.RESERVED_PROGRAM_ID;
import libepg.ts.packet.TsPacketParcel;
import libepg.ts.reader.TsReader;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * ファイルからチャンネル情報と番組情報を得る。
 *
 * @author normal
 */
public final class FileLoader {

    protected static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }

    private final File tsFile;
    private final long limit;
    private Set<Channel> channels = Collections.unmodifiableSet(new HashSet<>());
    private Set<Programme> programmes = Collections.unmodifiableSet(new HashSet<>());

    public FileLoader(File tsFile, long limit) {
        this.tsFile = new File(tsFile.getAbsolutePath());
        this.limit = limit;
    }

    public void load() throws FileNotFoundException {

        //ファイルを開く。
        if (!tsFile.isFile()) {
            throw new FileNotFoundException("ファイルがありません。パス = " + tsFile.getAbsolutePath());
        }

        //SDTとEITを取得。
        final Set<Integer> pids = new HashSet<>();
        pids.addAll(RESERVED_PROGRAM_ID.SDT_OR_BAT.getPids());
        pids.addAll(RESERVED_PROGRAM_ID.EIT_GR_ST.getPids());

        LOG.info("読み込み対象ファイル = " + tsFile.getAbsolutePath());
        final TsReader reader = new TsReader(tsFile, pids, limit);
        Map<Integer, List<TsPacketParcel>> pid_packets = reader.getPackets();

        Map<Integer, Set<Section>> pids_sections_temp = new ConcurrentHashMap<>();
        for (Integer pidKey : pid_packets.keySet()) {
            LOG.info("処理対象pid = " + Integer.toHexString(pidKey) + " pid定数 = " + RESERVED_PROGRAM_ID.reverseLookUp(pidKey));
            SectionReconstructor sectionMaker = new SectionReconstructor(pid_packets.get(pidKey), pidKey);
            Set<Section> sections = sectionMaker.getSections();
            if (sections != null) {
                LOG.info("セクション数 = " + sections.size());
                pids_sections_temp.put(pidKey, sections);
            }
        }
        Map<Integer, Set<Section>> pids_sections = Collections.unmodifiableMap(pids_sections_temp);

        pid_packets = null;
        pids_sections_temp = null;

        ChannelDataExtractor chde = new ChannelDataExtractor();
        ProgrammeDataExtractor pgde = new ProgrammeDataExtractor();

        for (Integer pid : pids_sections.keySet()) {
            LOG.info("処理対象pid = " + Integer.toHexString(pid) + " pid定数 = " + RESERVED_PROGRAM_ID.reverseLookUp(pid));
            for (Section s : pids_sections.get(pid)) {
                try {
                    if (RESERVED_PROGRAM_ID.reverseLookUp(pid) == RESERVED_PROGRAM_ID.SDT_OR_BAT) {
                        chde.makeDataSet(s);
                    } else if (RESERVED_PROGRAM_ID.reverseLookUp(pid) == RESERVED_PROGRAM_ID.EIT_GR_ST) {
                        pgde.makeDataSet(s);
                    }
                } catch (IllegalArgumentException ex) {
                    LOG.warn(ex);
                }
            }
        }
        this.channels = chde.getUnmodifiableDataSet();
        this.programmes = pgde.getUnmodifiableDataSet();
    }

    public File getTsFile() {
        return new File(tsFile.getAbsolutePath());
    }

    public Set<Channel> getChannels() {
        return channels;
    }

    public Set<Programme> getProgrammes() {
        return programmes;
    }

}
