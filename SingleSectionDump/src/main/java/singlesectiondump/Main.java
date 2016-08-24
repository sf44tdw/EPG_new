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
package singlesectiondump;

import loggingsupport.loggerfactory.LoggerFactory;
import java.lang.invoke.MethodHandles;
import libepg.epg.section.Section;
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
 * セクション1個分のhexダンプを与えると、それを解釈した結果を出力する。
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
        final byte[] section;
        byte[] x = null;
        final Option sectionDumpOption = Option.builder("s")
                .required()
                .longOpt("section")
                .desc("セクションのhexダンプ")
                .hasArg()
                .type(String.class)
                .build();
        
        Options opts = new Options();
        opts.addOption(sectionDumpOption);
        try {
            
            CommandLineParser parser = new DefaultParser();
            CommandLine cl = parser.parse(opts, args);
            
            x = Hex.decodeHex(cl.getOptionValue(sectionDumpOption.getOpt()).toCharArray());
            
        } catch (DecoderException ex) {
            LOG.fatal("hexダンプの変換に失敗しました。", ex);
            x = null;
            throw ex;
        } catch (ParseException ex) {
            LOG.fatal("オプションの解釈に失敗しました。", ex);
            x = null;
            throw ex;
        } finally {
            section = x;
            if (section == null) {
                this.printHelp(opts);
            }
        }
        
        Section s = new Section(section);
        System.out.println(s);
        
    }
}
