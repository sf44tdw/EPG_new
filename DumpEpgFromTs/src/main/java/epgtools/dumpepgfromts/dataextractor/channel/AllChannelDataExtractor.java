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
package epgtools.dumpepgfromts.dataextractor.channel;

/**
 * ファイルリストのファイルを読み込み、チャンネル情報を取り出してリスト化する。
 *
 * @author normal
 */
public class AllChannelDataExtractor {
//
//    //SDTのみを取得。
//    private static final RESERVED_PROGRAM_ID pids = RESERVED_PROGRAM_ID.SDT_OR_BAT;
//
//    public void getDataFromFile(final List<File> fileList) {
//
//        final Map<MultiKey<Integer>, Channel> channels = new ConcurrentHashMap<>();
//  
//
//            Map<Integer, List<TsPacketParcel>> ret = reader.getPackets();
//
//            for (Integer pid : ret.keySet()) {
//                SectionReconstructor sr = new SectionReconstructor(ret.get(pid), pid);
//                Set<Section> ss = sr.getSections();
//                Map<MultiKey<Integer>, Channel> channels_temp = null;
//                //ぬるぽ対策
//                if (ss.isEmpty()) {
//                    LOG.info("セクション無し。 pid = " + pid);
//                    channels_temp = new ConcurrentHashMap<>();
//                }
//                for (Section s : ss) {
//                    channels_temp = new ChannelDataExtractor(s).getDataList();
//                    if (channels_temp != null) {
//                        Set<MultiKey<Integer>> ks = channels_temp.keySet();
//                        LOG.debug("仮テーブルサイズ = " + channels_temp.size());
//                        if (LOG.isDebugEnabled()) {
//                            for (MultiKey<Integer> k : ks) {
//                                LOG.debug("仮テーブル = \n" + channels_temp.get(k) + "\n");
//                            }
//                        }
//                        channels.putAll(channels_temp);
//                    }
//                }
//            }
//        }
//
//        Set<MultiKey<Integer>> ks = channels.keySet();
//        for (MultiKey<Integer> k : ks) {
//            LOG.info(channels.get(k));
//        }
//    }

}
