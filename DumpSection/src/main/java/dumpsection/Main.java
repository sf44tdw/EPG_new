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

import dumpsection.util.Util;
import epgtools.loggerfactory.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
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

/**
 * 指定されたtsファイルの先頭から数えて指定された数のパケットを読み込み、指定されたpid(複数)とテーブルID(複数)のセクションを再構成して16進ダンプとして出力する。<br>
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

    public void start(String[] args) throws org.apache.commons.cli.ParseException, FileNotFoundException, IOException {
        final String[] args_i = Util.stringArrayCharSetDefaultToDesired(args, Charset.forName("UTF-8"));

        final String fileName;
        final Set<Integer> pid;
        final Set<Integer> tableId;
        final Long limit;

        final Option fileNameOption = Option.builder("f")
                .required()
                .longOpt("filename")
                .desc("tsファイル名")
                .hasArg()
                .type(String.class)
                .build();

        final Option pidsOption = Option.builder("p")
                .required()
                .longOpt("pid")
                .desc("pid(複数指定可。16進数で入力)")
                .type(Integer.class)
                .hasArgs()
                .build();

        final Option tableIdsOption = Option.builder("t")
                .required()
                .longOpt("tableid")
                .desc("テーブルid(複数指定可。16進数で入力)")
                .hasArgs()
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
        opts.addOption(pidsOption);
        opts.addOption(tableIdsOption);
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

            Set<Integer> temp1 = null;
            try {
                temp1 = Util.stringArrayToUnsignedIntegerSet(cl.getOptionValues(pidsOption.getOpt()), 16);
            } catch (NumberFormatException e) {
                LOG.error(e);
                throw new ParseException("pidの解釈に失敗しました。");
            } finally {
                pid = temp1;
            }

            Set<Integer> temp2 = null;
            try {
                temp2 = Util.stringArrayToUnsignedIntegerSet(cl.getOptionValues(tableIdsOption.getOpt()), 16);
            } catch (NumberFormatException e) {
                LOG.error(e);
                throw new ParseException("テーブルIDの解釈に失敗しました。");
            } finally {
                tableId = temp2;
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

        LOG.info("Starting application...");
        LOG.info("ts filename   : " + fileName);
        LOG.info("pid           : " + StringUtils.join(pid, " "));
        LOG.info("tableid       : " + StringUtils.join(tableId, " "));
        LOG.info("limit         : " + limit);

        TsReader reader = null;
        try {
            if (limit == null) {
                reader = new TsReader(new File(fileName), pid);
            } else {
                reader = new TsReader(new File(fileName), pid, limit);
            }
        } catch (FileNotFoundException ex) {
            LOG.fatal("ファイルが見つかりません。", ex);
            throw ex;
        }
        Map<Integer, List<TsPacketParcel>> ret = reader.getPackets();

        String destFileNane = fileName + "_pid_" + StringUtils.join(pid, "_") + "_tid_" + StringUtils.join(tableId, "_") + "_.txt";
        LOG.info("保存先ファイル = " + destFileNane);
        try (FileWriter writer = new FileWriter(destFileNane)) {
            for (Integer k : ret.keySet()) {
                SectionReconstructor sr = new SectionReconstructor(ret.get(k), k);
                List<Section> sections = sr.getSections();
                int wsec = 0;
                for (Section s : sections) {
                    if ((s != null) && tableId.contains(s.getTable_id())) {
                        writer.write(Hex.encodeHexString(s.getData()) + "\n");
                        wsec++;
                    }
                }
                LOG.info("pid = " + Integer.toHexString(k) + " セクション数 = " + sections.size() + " 書き込みセクション数 = " + wsec);
            }
            writer.flush();
        } catch (IOException ex) {
            LOG.fatal("ファイル入出力エラー", ex);
            throw ex;
        }

    }
}
