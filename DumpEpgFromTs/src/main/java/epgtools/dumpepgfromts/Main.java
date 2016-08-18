/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epgtools.dumpepgfromts;

import epgtools.dumpepgfromts.filelistmaker.FileSeeker;
import epgtools.dumpepgfromts.filelistmaker.Suffix;
import epgtools.loggerfactory.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
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
import org.apache.commons.logging.Log;

/**
 * 指定されたtsファイルを指定のパケット数だけ読み、それに含まれているチャンネル情報の一部を取り出し、xmlファイルとして保存する。
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
        final String directoryName;
        final Long limit;

        System.out.println("args   : " + dumpArgs(args));

        final Option directoryNameOption = Option.builder("d")
                .required()
                .longOpt("directory")
                .desc("tsファイルが置いてあるディレクトリ")
                .hasArg()
                .type(String.class)
                .build();

        final Option limitOption = Option.builder("l")
                .required(false)
                .longOpt("limit")
                .desc("読み込みパケット数(省略可。省略した場合はEOFまで読む。)")
                .hasArg()
                .type(Long.class)
                .build();

        Options opts = new Options();
        opts.addOption(directoryNameOption);
        opts.addOption(limitOption);
        CommandLineParser parser = new DefaultParser();
        CommandLine cl;
        HelpFormatter help = new HelpFormatter();

        try {

            // parse options
            cl = parser.parse(opts, args);

            directoryName = cl.getOptionValue(directoryNameOption.getOpt());
            if (directoryName == null) {
                throw new ParseException("ディレクトリ名が指定されていません。");
            }

            Long xl = null;
            try {
                xl = Long.parseUnsignedLong(cl.getOptionValue(limitOption.getOpt()));
            } catch (NumberFormatException e) {
                xl = null;
            } finally {
                limit = xl;
            }

            LOG.info("Starting application...");
            LOG.info("filename   : " + directoryName);
            if (limit != null) {
                LOG.info("limit : EOF");
            } else {
                LOG.info("limit : " + limit);
            }

            //ファイルリストを作成。
            FileSeeker fs = new FileSeeker(new File(directoryName), Suffix.TS_SUFFIX);
            //サブディレクトリは探さない
            fs.setRecursive(false);
            List<File> flist = fs.seek();

            //SDTとEITを取得。
            final Set<Integer> pids = new HashSet<>();
            pids.addAll(RESERVED_PROGRAM_ID.SDT_OR_BAT.getPids());
            pids.addAll(RESERVED_PROGRAM_ID.EIT_GR_ST.getPids());

            //ファイルごとのパケットリスト
            final Map<File, Map<Integer, List<TsPacketParcel>>> SectionSource = new ConcurrentHashMap<>();
            for (File f : flist) {
                final Map<Integer, List<TsPacketParcel>> packets;
                LOG.info("読み込み対象ファイル = " + f.getAbsolutePath());
                TsReader reader;
                if (limit != null) {
                    reader = new TsReader(f, pids);
                } else {
                    reader = new TsReader(f, pids, limit);
                }
                packets = reader.getPackets();
                SectionSource.put(f, packets);
            }

            StringBuilder sb = new StringBuilder();

            sb.append("読み込みファイル数 = ").append(SectionSource.size()).append("\n");
            Set<File> fileKeys = SectionSource.keySet();
            for (File fileKey : fileKeys) {
                sb.append("\n**********************************************************************************************************************************************************************\n");
                sb.append("ファイル = ").append(fileKey.getAbsolutePath()).append("\n");
                final Map<Integer, List<TsPacketParcel>> packets_and_pids = SectionSource.get(fileKey);
                Set<Integer> pidKeys = packets_and_pids.keySet();
                for (Integer pidKey : pidKeys) {
                    sb.append("pid定数 = ").append(RESERVED_PROGRAM_ID.reverseLookUp(pidKey)).append(" pid = ").append(Integer.toHexString(pidKey)).append("\n");
                    final List<TsPacketParcel> packets = packets_and_pids.get(pidKey);
                    sb.append("パケット数 = ").append(packets.size()).append("\n");
                }
                sb.append("\n**********************************************************************************************************************************************************************\n");
            }
            LOG.info(sb.toString());
        } catch (ParseException e) {
            // print usage.
            help.printHelp("My Java Application", opts);
            throw e;
        } catch (FileNotFoundException ex) {
            LOG.fatal("ファイルが見つかりません。", ex);
        }
    }
}
