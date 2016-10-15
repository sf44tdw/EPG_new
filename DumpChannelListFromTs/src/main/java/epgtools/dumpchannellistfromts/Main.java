/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package epgtools.dumpchannellistfromts;

import com.orangesignal.csv.manager.CsvManager;
import com.orangesignal.csv.manager.CsvManagerFactory;
import epgtools.dumpchannellistfromts.physicalchannelnumberrecord.PhysicalChannelNumberRecord;
import epgtools.dumpchannellistfromts.physicalchannelnumberrecord.PhysicalChannelNumberRecordBuilder;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import libepg.epg.section.Section;
import libepg.epg.section.SectionLoader;
import libepg.epg.section.TABLE_ID;
import libepg.epg.section.body.networkinformationtable.NetworkInformationTableBody;
import libepg.epg.section.body.networkinformationtable.TransportStreamLoop;
import libepg.epg.section.descriptor.DESCRIPTOR_TAG;
import libepg.epg.section.descriptor.Descriptor;
import libepg.epg.section.descriptor.networkdescriptor.NetworkNameDescriptor;
import libepg.epg.section.descriptor.servicedescriptor.SERVICE_TYPE;
import libepg.epg.section.descriptor.servicelistdescriptor.Service;
import libepg.epg.section.descriptor.servicelistdescriptor.ServiceListDescriptor;
import libepg.ts.fileseeker.TsFileSeeker;
import libepg.ts.packet.RESERVED_PROGRAM_ID;
import loggingsupport.loggerfactory.LoggerFactory;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.logging.Log;

/**
 * ファイル名を物理チャンネル番号にしたtsファイルから自局NITをロードし、
 * トランスポートストリーム識別、オリジナルネットワーク識別、サービス識別と、TV放送サービス用の物理チャンネルとの対応表を生成する<br>
 * 保存する情報は以下の通り。<br>
 * physicalChannelNumber(地上波の場合はファイル名。bsの場合はサービス識別の値。)<br>
 * networkId<br>
 * networkName<br>
 * transportStreamId<br>
 * originalNetworkId<br>
 * serviceId<br>
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
            System.exit(1);
        }
    }
    
    public String getNameWithoutExtension(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return "";
    }
    
    public void start(String[] args) throws ParseException {
        final String fileName;
        final Long limit;
        
        final Option directoryNameOption = Option.builder("d")
                .required()
                .longOpt("directoryname")
                .desc("ディレクトリ名")
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
        
        final Option destFileNameOption = Option.builder("f")
                .required()
                .longOpt("destname")
                .desc("保存先ファイル名")
                .hasArg()
                .type(String.class)
                .build();
        
        Options opts = new Options();
        opts.addOption(directoryNameOption);
        opts.addOption(limitOption);
        opts.addOption(destFileNameOption);
        CommandLineParser parser = new DefaultParser();
        
        HelpFormatter help = new HelpFormatter();
        CommandLine cl;
        try {
            cl = parser.parse(opts, args);
        } catch (ParseException ex) {
            LOG.fatal("オプションの解釈に失敗しました。", ex);
            help.printHelp("My Java Application", opts);
            throw ex;
        }
        
        final File dirName = new File(cl.getOptionValue(directoryNameOption.getOpt()));
        if (!dirName.isDirectory()) {
            throw new IllegalArgumentException("読み込み先にディレクトリ以外が指定されたか、存在しません。ディレクトリ = " + dirName.getAbsolutePath());
        }
        LOG.info("読み込み先ディレクトリ = " + dirName.getAbsolutePath());
        
        Long xl = null;
        try {
            if (cl.hasOption(limitOption.getOpt())) {
                xl = Long.parseUnsignedLong(cl.getOptionValue(limitOption.getOpt()));
            }
        } catch (NumberFormatException e) {
            LOG.error(e);
            throw new IllegalArgumentException("読み込みパケット数の解釈に失敗しました。");
        } finally {
            limit = xl;
            LOG.info("読み込みパケット数 = " + limit);
        }
        
        final File destFile = new File(cl.getOptionValue(destFileNameOption.getOpt()));
        LOG.info("書き込み先ファイル = " + destFile.getAbsolutePath());
        
        List<File> files = new TsFileSeeker(dirName).seek();
        
        LOG.info("読み込みファイル件数 = " + files.size());
        
        final PhysicalChannelNumberRecordBuilder bu = new PhysicalChannelNumberRecordBuilder();
        final Set<PhysicalChannelNumberRecord> records = Collections.synchronizedSet(new TreeSet<>());
        //NITをロードする。
        for (File f : files) {
            SectionLoader loader = new SectionLoader(f, limit, RESERVED_PROGRAM_ID.NIT.getPids());
            try {
                Map<Integer, List<Section>> pids_sections = loader.load();
                for (Integer k : pids_sections.keySet()) {
                    for (Section s : pids_sections.get(k)) {
                        if (s.checkCRC() != Section.CRC_STATUS.NO_CRC_ERROR) {
                            throw new IllegalArgumentException("CRCエラーです。セクション = " + Hex.encodeHexString(s.getData()));
                        } else if (s.getTable_id_const() != TABLE_ID.NIT_THIS_NETWORK) {
                            throw new IllegalArgumentException("自局のNITではありません。セクション = " + Hex.encodeHexString(s.getData()));
                        } else {
                            NetworkInformationTableBody nitbody = (NetworkInformationTableBody) s.getSectionBody();
                            bu.setNetworkId(nitbody.getNetwork_id());
                            for (Descriptor d1 : nitbody.getDescriptors_loop().getDescriptors_loopList()) {
                                if (d1.getDescriptor_tag_const() == DESCRIPTOR_TAG.NETWORK_NAME_DESCRIPTOR) {
                                    final NetworkNameDescriptor nnd = (NetworkNameDescriptor) d1;
                                    bu.setNetworkName(nnd.getChar_String());
                                }
                            }
                            for (TransportStreamLoop tsLoop : nitbody.getTransport_streams_loop()) {
                                bu.setTransportStreamId(tsLoop.getTransport_stream_id());
                                bu.setOriginalNetworkId(tsLoop.getOriginal_network_id());
                                for (Descriptor desc : tsLoop.getDescriptors_loop().getDescriptors_loopList()) {
                                    if (desc.getDescriptor_tag_const() == DESCRIPTOR_TAG.SERVICE_LIST_DESCRIPTOR) {
                                        ServiceListDescriptor sd = (ServiceListDescriptor) desc;
                                        List<Service> svList = sd.getServiceList();
                                        for (Service service : svList) {
                                            if (service.getService_type_Enum() == SERVICE_TYPE.DIGITAL_TV_SERVICE) {
                                                bu.setServiceId(service.getService_id());
                                                
                                                if (bu.getOriginalNetworkId() < 0x10) {
                                                    //BS
                                                    bu.setPhysicalChannelNumber(bu.getServiceId());
                                                } else {
                                                    //地上波          
                                                    bu.setPhysicalChannelNumber(Integer.valueOf(this.getNameWithoutExtension(f)));
                                                }
                                                
                                                records.add(bu.build());
                                            }
                                        }
                                        
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (FileNotFoundException ex) {
                LOG.info("ファイルが見つかりません。ファイル = " + f.getAbsolutePath(), ex);
            }
        }

//        for (PhysicalChannelNumberRecord rec : records) {
//            LOG.info(rec);
//        }
//        
        //重複排除のため一回セットに入れている。
        List<PhysicalChannelNumberRecord> nl = new ArrayList<>();
        nl.addAll(records);
        CsvManager csvManager = CsvManagerFactory.newCsvManager();
        LOG.info(destFile.getAbsolutePath());
        try {
            csvManager.save(nl, PhysicalChannelNumberRecord.class).to(destFile, "UTF-8");
        } catch (IOException ex) {
            LOG.fatal("保存中に問題が発生しました。ファイル = " + destFile.getAbsolutePath(), ex);
        }
        
    }
    
}
