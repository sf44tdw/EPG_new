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
package epgtools.dumpepgfromts.subtool;

import epgtools.dumpepgfromts.FileLoader;
import epgtools.dumpepgfromts.dataextractor.KeyFields;
import epgtools.dumpepgfromts.dataextractor.channel.Channel;
import epgtools.dumpepgfromts.dataextractor.programme.Programme;
import epgtools.dumpepgfromts.jaxb.physicalchannel.PhysicalChannelNumberRecord;
import epgtools.dumpepgfromts.jaxb.physicalchannel.PhysicalChannelNumbers;
import epgtools.dumpepgfromts.subtool.filelistmaker.FileSeeker;
import epgtools.dumpepgfromts.subtool.filelistmaker.Suffix;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import org.apache.commons.collections4.keyvalue.MultiKey;

/**
 * 特定ディレクトリ下のtsファイルをすべて読み込み、トランスポートストリーム識別、オリジナルネットワーク識別、サービス識別、ファイル名の一覧を作る。
 * 複数の局の番組表を同時に流している場合(衛星とか)があるので、番組表も併せて読み込み、自局の番組のトランスポートストリーム識別、オリジナルネットワーク識別、サービス識別と照合する。
 * ファイル名が物理チャンネル番号であれば(それが前提)、物理チャンネルとの対応表が作れる。 このクラスはjunitで直接動かす予定。
 *
 * @author normal
 */
public class ChannelListMaker {

    private String getNameWithoutExtension(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return "";
    }

    public void maker(File Dir) throws FileNotFoundException {

        FileSeeker seeker = new FileSeeker(Dir, Suffix.TS_SUFFIX);

        seeker.setRecursive(false);

        List<File> tsFiles = seeker.seek();

        Map<MultiKey<Integer>, Channel> chMap = new HashMap<>();

        Map< KeyFields, Integer> idMap = new ConcurrentHashMap<>();

        for (File tsFile : tsFiles) {

            FileLoader fl = new FileLoader(tsFile, 1000000L);

            fl.load();

            for (Channel ch : fl.getChannels()) {
                chMap.put(ch.getMuiltiKey(), ch);
            }

            Set<Programme> ps = fl.getProgrammes();
            //このファイルを流した局の番組データから、識別用のキーを取得する。
            //ファイル名が物理チャンネル番号であるという想定で話を進める

            for (Programme p : ps) {
                if (p.isThis_or_other() == true) {
                    KeyFields kf = p.getKeyfFields();
                    idMap.put(kf, Integer.valueOf(this.getNameWithoutExtension(tsFile)));
                }
            }

        }
        PhysicalChannelNumbers numList = new PhysicalChannelNumbers();

        Set<KeyFields> kfs = idMap.keySet();
        for (KeyFields kf : kfs) {
            Integer pt = idMap.get(kf);
            String name;
            Channel ch = chMap.get(kf.getMuiltiKey());
            if (ch != null) {
                name = ch.getDisplay_name();
            } else {
                name = "";
            }
            numList.add(new PhysicalChannelNumberRecord(pt, kf.getTransport_stream_id(), kf.getOriginal_network_id(), kf.getService_id(), name));
        }

        //一応データはできている。
        for (PhysicalChannelNumberRecord r : numList.getChannels()) {
            System.out.println(r);
        }

    }

}
