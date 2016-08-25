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
package epgtools.dumpservicefromnit;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.List;
import libepg.epg.section.Section;
import libepg.epg.section.Section.CRC_STATUS;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.networkinformationtable.NetworkInformationTableBody;
import libepg.epg.section.body.networkinformationtable.TransportStreamLoop;
import libepg.epg.section.descriptor.DESCRIPTOR_TAG;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.DescriptorsLoop;
import libepg.epg.section.descriptor.networkdescriptor.NetworkNameDescriptor;
import libepg.epg.section.descriptor.servicedescriptor.SERVICE_TYPE;
import libepg.epg.section.descriptor.servicelistdescriptor.Service;
import libepg.epg.section.descriptor.servicelistdescriptor.ServiceListDescriptor;
import loggingsupport.loggerfactory.LoggerFactory;
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
 * NITのhexダンプとサービス形式種別を与えると、そのサービス識別を持つ トランスポートストリーム識別、ネットワーク識別、サービス識別の一覧を返す。
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
        } catch (ParseException | FileNotFoundException ex) {
            System.exit(1);
        } catch (IOException ex) {
            System.exit(1);
        }
    }

    public void start(String[] args) throws org.apache.commons.cli.ParseException, FileNotFoundException, IOException {
        final byte[] sectionByte;
        final SERVICE_TYPE serviceType;
        final Option sectionDumpOption = Option.builder("s")
                .required()
                .longOpt("section")
                .desc("セクションのhexダンプ")
                .hasArg()
                .type(String.class)
                .build();

        final Option serviceTypeOption = Option.builder("t")
                .required(false)
                .longOpt("servicetype")
                .desc("サービス形式種別")
                .hasArg()
                .type(Integer.class)
                .build();

        Options opts = new Options();
        opts.addOption(sectionDumpOption);
        opts.addOption(serviceTypeOption);
        CommandLineParser parser = new DefaultParser();
        CommandLine cl;
        HelpFormatter help = new HelpFormatter();

        try {

            // parse options
            cl = parser.parse(opts, args);

            try {
                sectionByte = Hex.decodeHex(cl.getOptionValue(sectionDumpOption.getOpt()).toCharArray());
            } catch (DecoderException ex) {
                LOG.error(ex);
                throw new ParseException("hexダンプの解釈に失敗しました。");
            }

            serviceType = SERVICE_TYPE.reverseLookUp(Integer.parseInt(cl.getOptionValue(serviceTypeOption.getOpt()), 16));

        } catch (ParseException e) {
            // print usage.
            help.printHelp("My Java Application", opts);
            throw e;
        }

        LOG.info("Starting application...");
        LOG.info("section           : " + Hex.encodeHexString(sectionByte));
        LOG.info("service type      : " + serviceType);

        Section section = new Section(sectionByte);

        if (section.checkCRC() != CRC_STATUS.NO_CRC_ERROR) {
            LOG.error("CRCエラーです。");
        }

        if (section.getTable_id_const() != TABLE_ID.NIT_THIS_NETWORK && section.getTable_id_const() != TABLE_ID.NIT_OTHER_NETWORK) {
            LOG.error("NITではありません。");
        }

        NetworkInformationTableBody nitbody = (NetworkInformationTableBody) section.getSectionBody();

        final int networkId = nitbody.getNetwork_id();
        LOG.info("networkId = " + Integer.toHexString(networkId));

        for (Descriptor d1 : nitbody.getDescriptors_loop().getDescriptors_loopList()) {
            final String networkName;
            if (d1.getDescriptor_tag_const() == DESCRIPTOR_TAG.NETWORK_NAME_DESCRIPTOR) {
                final NetworkNameDescriptor nnd = (NetworkNameDescriptor) d1;
                networkName = nnd.getChar_String();
                LOG.info("networkName = " + networkName);
            }
        }

        final List<TransportStreamLoop> tsLoopList = nitbody.getTransport_streams_loop();

        for (TransportStreamLoop tsLoop : tsLoopList) {
            final int transportStreamId = tsLoop.getTransport_stream_id();
            LOG.info("transportStreamId = " + Integer.toHexString(transportStreamId));
            final int originalNetworkId = tsLoop.getOriginal_network_id();
            LOG.info("originalNetworkId = " + Integer.toHexString(originalNetworkId));
            final DescriptorsLoop dloop = tsLoop.getDescriptors_loop();
            final List<Descriptor> dList = dloop.getDescriptors_loopList();
            for (Descriptor desc : dList) {

                if (desc.getDescriptor_tag_const() == DESCRIPTOR_TAG.SERVICE_LIST_DESCRIPTOR) {
                    ServiceListDescriptor sd = (ServiceListDescriptor) desc;
                    List<Service> svList = sd.getServiceList();
                    for (Service service : svList) {
                        if (service.getService_type_Enum() == serviceType) {
                            final int serviceId = service.getService_id();
                            LOG.info("serviceId = " + Integer.toHexString(serviceId));
                        }
                    }
                }
            }
        }

    }
}
