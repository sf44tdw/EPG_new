/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epgtools.dumpepgfromts;

import epgtools.dumpepgfromts.dataextractor.PredicateSet;
import epgtools.dumpepgfromts.dataextractor.channel.Channel;
import epgtools.dumpepgfromts.dataextractor.channel.ChannelDataExtractor;
import epgtools.dumpepgfromts.dataextractor.programme.Programme;
import epgtools.dumpepgfromts.dataextractor.programme.ProgrammeDataExtractor;
import epgtools.loggerfactory.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collection;
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
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.collections4.keyvalue.MultiKey;
import org.apache.commons.logging.Log;

/**
 * 指定されたtsファイルを指定のパケット数だけ読み、それに含まれている情報の一部を取り出し、xmlファイルとして保存する。
 * ファイル名はtsファイル名+SDT.xmlになる。 取り出す情報は以下の通り。 transport_stream_id
 * original_network_id service_id display-name(service_name)
 *
 * @author normal
 */
public class Main {

    /**
     * falseのとき、このクラスはログを出さなくなる
     */
    public static final boolean CLASS_LOG_OUTPUT_MODE = true;
    
    private static final Log LOG;
    
    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = new LoggerFactory(myClass, Main.CLASS_LOG_OUTPUT_MODE).getLOG();
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        try {
            new Main().start(args);
            System.exit(0);
        } catch (ParseException ex) {
            LOG.fatal("オプションの解釈に失敗しました。", ex);
            System.exit(1);
        }
    }
    
    private String dumpCollection(Collection<?> target) {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Object s : target) {
            sb.append(s.getClass());
            sb.append("=");
            sb.append(s);
            sb.append(" ");
        }
        sb.append("}");
        return sb.toString();
    }
    
    private String dumpArgs(String[] args) {
        Collection<String> strs = Arrays.asList(args);
        return this.dumpCollection(strs);
    }
    
    private String dumpSet(Set<?> target) {
        return this.dumpCollection(target);
    }
    
    public void start(String[] args) throws org.apache.commons.cli.ParseException {
        final String fileName;
        final Long limit;
        
        System.out.println("args   : " + dumpArgs(args));
        
        final Option fileNameOption = Option.builder("f")
                .required()
                .longOpt("tsfile")
                .desc("tsファイル")
                .hasArg()
                .type(String.class)
                .build();
        
        final Option limitOption = Option.builder("l")
                .required(false)
                .longOpt("limit")
                .desc("読み込みパケット数(省略可。省略した場合は10000000パケット読む。)")
                .hasArg()
                .type(Long.class)
                .build();
        
        Options opts = new Options();
        opts.addOption(fileNameOption);
        opts.addOption(limitOption);
        CommandLineParser parser = new DefaultParser();
        CommandLine cl;
        HelpFormatter help = new HelpFormatter();
        
        try {

            // parse options
            cl = parser.parse(opts, args);
            
            fileName = cl.getOptionValue(fileNameOption.getOpt());
            if (fileName == null) {
                throw new ParseException("ファイル名が指定されていません。");
            }
            
            Long xl = null;
            try {
                xl = Long.parseUnsignedLong(cl.getOptionValue(limitOption.getOpt()));
            } catch (NumberFormatException e) {
                xl = 1000000L;
            } finally {
                limit = xl;
            }
            
            LOG.info("Starting application...");
            LOG.info("filename   : " + fileName);
            LOG.info("limit : " + limit);

            //ファイルを開く。
            File tsFile = new File(fileName);
            if (!tsFile.isFile()) {
                throw new FileNotFoundException("ファイルがありません。パス = " + tsFile.getAbsolutePath());
            }

            //SDTとEITを取得。
            final Set<Integer> pids = new HashSet<>();
            pids.addAll(RESERVED_PROGRAM_ID.SDT_OR_BAT.getPids());
            pids.addAll(RESERVED_PROGRAM_ID.EIT_GR_ST.getPids());

            //全部のファイルからパケットを抽出してから一気にセクションを再構成しようとするとメモリ不足になるので、ファイルを1個ずつ処理する。
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
            
            final Map<MultiKey<Integer>, Channel> multiKey_channels_temp = new ConcurrentHashMap<>();
            final Map<MultiKey<Integer>, PredicateSet<Programme>> muiltiKey_programmes_temp = new ConcurrentHashMap<>();
            
            for (Integer pid : pids_sections.keySet()) {
                LOG.info("処理対象pid = " + Integer.toHexString(pid) + " pid定数 = " + RESERVED_PROGRAM_ID.reverseLookUp(pid));
                for (Section s : pids_sections.get(pid)) {
                    if (RESERVED_PROGRAM_ID.reverseLookUp(pid) == RESERVED_PROGRAM_ID.SDT_OR_BAT) {
//                        multiKey_channels_temp.putAll(new ChannelDataExtractor(s).getDataList());
                    } else if (RESERVED_PROGRAM_ID.reverseLookUp(pid) == RESERVED_PROGRAM_ID.EIT_GR_ST) {
                        muiltiKey_programmes_temp.putAll(new ProgrammeDataExtractor(s).getDataList());
                    }
                }
            }

//            //仮処理。チャンネル情報をダンプ。
//            for (MultiKey<Integer> k : multiKey_channels_temp.keySet()) {
//                LOG.info("***********************************************************************************************************************************************************************************************************");
//                LOG.info("***********************************************************************************************************************************************************************************************************");
//                LOG.info(multiKey_channels_temp.get(k));
//                LOG.info("***********************************************************************************************************************************************************************************************************");
//                LOG.info(pids_sections.get(k));
//                LOG.info("***********************************************************************************************************************************************************************************************************");
//                LOG.info("***********************************************************************************************************************************************************************************************************");
//            }
//
//            //仮処理。番組情報をダンプ。
//            for (MultiKey<Integer> k : muiltiKey_programmes_temp.keySet()) {
//                for (Programme p : muiltiKey_programmes_temp.get(k)) {
//                    LOG.info("***********************************************************************************************************************************************************************************************************");
//                    LOG.info(p);
//                    LOG.info("***********************************************************************************************************************************************************************************************************");
//                }
//            }
            
            System.gc();
            
        } catch (ParseException e) {
            // print usage.
            help.printHelp("My Java Application", opts);
            throw e;
        } catch (FileNotFoundException ex) {
            LOG.fatal("ファイルが見つかりません。", ex);
        }
    }
    
}
