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
package dumpsection;

import epgtools.loggerfactory.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.sectionreconstructor.SectionReconstructor;
import libepg.ts.packet.TsPacketParcel;
import libepg.ts.reader.TsReader;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.mozilla.universalchardet.UniversalDetector;

/**
 * 指定されたtsファイルの先頭から数えて指定された数のパケットを読み込み、指定されたpidとテーブルIDのセクションを再構成して16進ダンプとして出力する。<br>
 * pidとテーブルIDは16進数で指定する。 16進ダンプのファイル名は、tsファイル名_pid_tableId.txtになる。<br>
 * 読み込んだパケットのうち、指定されていないpidやテーブルIDのパケットはすべて無視する。<br>
 * 再構成したセクションのうち、CRCエラーがあったもの、重複したものはダンプしない。<br>
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
        } catch (ParseException | FileNotFoundException ex) {
            System.exit(1);
        } catch (IOException ex) {
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
    
    private String[] exChangeArgsCharSetDefaultToUtf8(String[] args) throws UnsupportedEncodingException {
        Charset cs = Charset.defaultCharset();
        List<String> strs = Arrays.asList(args);
        List<String> strd = new ArrayList<>();
        for (String str : strs) {
            strd.add(StringUtils.toEncodedString(str.getBytes(cs), Charset.forName("UTF-8")));
        }
        return strd.toArray(args);
    }
    
    public void start(String[] args) throws org.apache.commons.cli.ParseException, FileNotFoundException, IOException {
        final String[] args_i = exChangeArgsCharSetDefaultToUtf8(args);
        
        final String fileName;
        final Integer pid;
        final Integer tableId;
        final Long limit;
        
        final Option fileNameOption = Option.builder("f")
                .required()
                .longOpt("filename")
                .desc("tsファイル名")
                .hasArg()
                .type(String.class)
                .build();
        
        final Option pidOption = Option.builder("p")
                .required()
                .longOpt("pid")
                .desc("ダンプするpidの値。16進数")
                .hasArg()
                .type(Integer.class)
                .build();
        
        final Option tableIdOption = Option.builder("t")
                .required()
                .longOpt("tableid")
                .desc("ダンプするテーブルidの値。16進数")
                .hasArg()
                .type(Integer.class)
                .build();
        
        final Option limitOption = Option.builder("l")
                .required(false)
                .longOpt("limit")
                .desc("読み込みパケット数(省略可。省略した場合はEOFまで読む。)")
                .hasArg()
                .type(Long.class)
                .build();
        
        Options opts = new Options();
        opts.addOption(fileNameOption);
        opts.addOption(pidOption);
        opts.addOption(tableIdOption);
        opts.addOption(limitOption);
        CommandLineParser parser = new DefaultParser();
        CommandLine cl;
        HelpFormatter help = new HelpFormatter();
        
        try {

            // parse options
            cl = parser.parse(opts, args_i);
            
            fileName = cl.getOptionValue(fileNameOption.getOpt());
            if (fileName == null) {
                throw new ParseException("ファイル名が指定されていません。");
            }
            
            Integer xI_p = null;
            try {
                xI_p = Integer.parseUnsignedInt(cl.getOptionValue(pidOption.getOpt()), 16);
            } catch (NumberFormatException e) {
                LOG.error(e);
                throw new ParseException("pidの解釈に失敗しました。");
            } finally {
                pid = xI_p;
            }
            
            Integer xI_t = null;
            try {
                xI_t = Integer.parseUnsignedInt(cl.getOptionValue(tableIdOption.getOpt()), 16);
            } catch (NumberFormatException e) {
                LOG.error(e);
                throw new ParseException("テーブルIDの解釈に失敗しました。");
            } finally {
                tableId = xI_t;
            }
            
            Long xl = null;
            try {
                if (cl.hasOption(limitOption.getOpt())) {
                    xl = Long.parseUnsignedLong(cl.getOptionValue(limitOption.getOpt()));
                }
            } catch (NumberFormatException e) {
                LOG.error(e);
                throw new ParseException("読み込みパケット数の解釈に失敗しました。");
            } finally {
                limit = xl;
            }
        } catch (ParseException e) {
            // print usage.
            help.printHelp("My Java Application", opts);
            throw e;
        }
        
        System.out.println("Starting application...");
        System.out.println("filename   : " + fileName);
        System.out.println("pid        : " + Integer.toHexString(pid));
        System.out.println("tableid    : " + Integer.toHexString(tableId));
        System.out.println("limit      : " + limit);
        
        Set<Integer> pids = new HashSet<>();
        pids.add(pid);
        
        TsReader reader = null;
        try {
            if (limit == null) {
                reader = new TsReader(new File(fileName), pids);
            } else {
                reader = new TsReader(new File(fileName), pids, limit);
            }
        } catch (FileNotFoundException ex) {
            LOG.fatal("ファイルが見つかりません。", ex);
            throw ex;
        }
        
        Map<Integer, List<TsPacketParcel>> ret = reader.getPackets();
        for (Integer k : ret.keySet()) {
            SectionReconstructor sr = new SectionReconstructor(ret.get(k), k);
            try (FileWriter writer = new FileWriter(fileName + "_" + Integer.toHexString(k) + "_" + Integer.toHexString(tableId) + "_.txt")) {
                for (Section s : sr.getSections()) {
                    if (s.getTable_id() == tableId) {
                        writer.write(Hex.encodeHexString(s.getData()) + "\n");
                    }
                }
                writer.flush();
            } catch (IOException ex) {
                LOG.fatal("ファイル入出力エラー", ex);
                throw ex;
            }
        }
    }
}
