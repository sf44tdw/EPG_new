/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epgtools.dumpepgfromts;

import epgtools.dumpepgfromts.dataextractor.channel.Channel;
import epgtools.dumpepgfromts.dataextractor.programme.Programme;
import epgtools.loggerfactory.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
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
                .desc("読み込みパケット数(省略可。省略した場合は100000000パケット読む。)")
                .hasArg()
                .type(Long.class)
                .build();

        Options opts = new Options();
        opts.addOption(fileNameOption);
//        opts.addOption(PhysicalChannelNumberOption);
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
                xl = 10000000L;
            } finally {
                limit = xl;
            }

            LOG.info("Starting application...");
            LOG.info("filename   : " + fileName);
            LOG.info("limit : " + limit);

            FileLoader fl = new FileLoader(new File(fileName), limit);
            fl.load();

            //仮処理。チャンネル情報をダンプ。
            Set<Channel> ch = fl.getChannels();
            LOG.info("チャンネル件数 = " + ch.size());
            for (Channel c : ch) {
                LOG.info("***********************************************************************************************************************************************************************************************************");
                LOG.info(c.getString());
                LOG.info("***********************************************************************************************************************************************************************************************************");
            }

//            仮処理。番組情報をダンプ。
            Set<Programme> p = fl.getProgrammes();
            LOG.info("番組件数 = " + p.size());
            for (Programme pg : p) {
                LOG.info("***********************************************************************************************************************************************************************************************************");
                LOG.info(pg.getString());
                LOG.info("***********************************************************************************************************************************************************************************************************");
            }

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
