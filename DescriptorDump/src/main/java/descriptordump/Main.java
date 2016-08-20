/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package descriptordump;

import descriptordump.dumpexecutor.eit.EitDescGetter;
import descriptordump.dumpexecutor.nit.NitDescGetter;
import descriptordump.dumpexecutor.sdt.SdtDescGetter;
import epgtools.loggerfactory.LoggerFactory;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.invoke.MethodHandles;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import libepg.epg.section.Section;
import libepg.epg.section.Section.CRC_STATUS;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.descriptor.Descriptor;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;

/**
 * 1行1セクションで作られたhexダンプファイルを読みこみ、SDT EIT NITに存在する記述子を、重複なしで全部集める。
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
        } catch (ParseException | DecoderException ex) {
            System.exit(1);
        }
    }

    private void printHelp(Options opts) {
        // print usage.
        HelpFormatter help = new HelpFormatter();
        help.printHelp("My Java Application", opts);
    }

    public void start(String args[]) throws DecoderException, ParseException {
        File inputFile = null;
        Charset cs = null;

        final Option charSetOption = Option.builder("c")
                .required(false)
                .longOpt("charset")
                .desc("読み込み、書き込み用文字コード。省略した場合、システムの標準設定を使う。")
                .hasArg()
                .type(String.class)
                .build();

        final Option fileNameOption = Option.builder("f")
                .required()
                .longOpt("filename")
                .desc("セクションファイル名")
                .hasArg()
                .type(String.class)
                .build();

        Options opts = new Options();
        opts.addOption(fileNameOption);
        CommandLineParser parser = new DefaultParser();
        CommandLine cl;
        HelpFormatter help = new HelpFormatter();
        try {
            cl = parser.parse(opts, args);

            try {
                cs = Charset.forName(cl.getOptionValue(charSetOption.getOpt()));
            } catch (Exception e) {
                LOG.error("文字コード設定に失敗しました。デフォルトの文字コードを使用します。", e);
                cs = Charset.defaultCharset();
            }
            inputFile = new File(cl.getOptionValue(fileNameOption.getOpt()));
            if (!inputFile.isFile()) {
                throw new ParseException("ファイル名が正しくありません。");
            }

        } catch (ParseException e) {
            // print usage.
            help.printHelp("My Java Application", opts);
        }

        final Set<TABLE_ID> tids = new HashSet<>();
        tids.add(TABLE_ID.SDT_THIS_STREAM);
        tids.add(TABLE_ID.SDT_OTHER_STREAM);
        tids.add(TABLE_ID.NIT_THIS_NETWORK);
        tids.add(TABLE_ID.NIT_OTHER_NETWORK);
        tids.add(TABLE_ID.EIT_THIS_STREAM_8_DAYS);
        tids.add(TABLE_ID.EIT_THIS_STREAM_NOW_AND_NEXT);
        tids.add(TABLE_ID.EIT_OTHER_STREAM_8_DAYS);
        tids.add(TABLE_ID.EIT_OTHER_STREAM_NOW_AND_NEXT);

        LOG.info("Starting application...");
        LOG.info("ts filename   : " + inputFile.getAbsolutePath());
        LOG.info("charset       : " + cs);

        Set<Section> sections = new HashSet<>();
        try {
            FileInputStream input = new FileInputStream(inputFile);
            InputStreamReader stream = new InputStreamReader(input, cs);
            BufferedReader buffer = new BufferedReader(stream);

            String line;
            long lines = 0;
            while ((line = buffer.readLine()) != null) {
                byte[] b = Hex.decodeHex(line.toCharArray());
                Section s = new Section(b);
                if (s.checkCRC() == CRC_STATUS.NO_CRC_ERROR && tids.contains(s.getTable_id_const())) {
                    sections.add(s);
                    LOG.trace("1件追加しました。");
                } else {
                    LOG.error("このセクションは無視されます。セクション = " + s);
                }
                lines++;
            }
            LOG.info("読み込みセクション数 = " + lines + " 保存セクション数 = " + sections.size());
            input.close();
            stream.close();
            buffer.close();
        } catch (FileNotFoundException ex) {
            LOG.fatal("読み込みに失敗しました。", ex);
        } catch (IOException ex) {
            LOG.fatal("読み込みに失敗しました。", ex);
        }

        SdtDescGetter sde = new SdtDescGetter();

        NitDescGetter nide = new NitDescGetter();

        EitDescGetter eide = new EitDescGetter();

        for (Section s : sections) {
            sde.process(s);
            nide.process(s);
            eide.process(s);
        }

        Set<Descriptor> descs = new HashSet<>();
        descs.addAll(sde.getUnmodifiableDest());
        descs.addAll(nide.getUnmodifiableDest());
        descs.addAll(eide.getUnmodifiableDest());

        Set<Integer> dtags = new HashSet<>();
        for (Descriptor d : descs) {
            dtags.add(d.getDescriptor_tag());
        }

        for (Integer i : dtags) {
            LOG.info(Integer.toHexString(i));
        }
    }
}
