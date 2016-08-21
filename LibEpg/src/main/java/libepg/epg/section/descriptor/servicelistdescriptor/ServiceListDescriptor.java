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
package libepg.epg.section.descriptor.servicelistdescriptor;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import libepg.epg.section.descriptor.Descriptor;
import libepg.util.bytearray.ByteArraySplitter;

//service_list_descriptor(){
//descriptor_tag 8 uimsbf
//descriptor_length 8 uimsbf
//for (i=0;i<N;i++){
//service_id 16 uimsbf
//service_type 8 uimsbf
//}
//}
/**
 * サービスリスト記述子（Service list descriptor） サービスリスト記述子（表6－26 参照）は、サービス識別とサービス形式種別によるサ
 * ービスの一覧を提供する。
 *
 * この記述子は、各トランスポートストリーム内のサービスおよびサービス形式種別の一 覧を提供する。サービスはサービス識別（=MPEG-2
 * プログラム番号）によって識別され 一覧される。トランスポートストリーム識別とオリジナルネットワーク識別は、サービス
 * を一意に識別するために必要なものであり、記述子ループの最初に提示される。 サービスリスト記述子は各ループに1 度のみ使用することができる。この記述子の伝送
 * は他ネットワークではオプションである。しかし伝送される場合は完全なものでなければ ならない
 *
 * @author normal
 */
public class ServiceListDescriptor extends Descriptor {

    public ServiceListDescriptor(Descriptor descriptor) {
        super(descriptor);
    }

    public final synchronized List<Service> getServiceList() {
        List<byte[]> list = ByteArraySplitter.split(this.getDescriptor_Body(), 3);
        List<Service> ret = new ArrayList<>();
        for (byte[] b : list) {
            ret.add(new Service(b));
        }
        return ret;
    }

    private static final String TITLE = "サービスリスト記述子";
    private static final MessageFormat DESC = new MessageFormat(
            TITLE + " {0}"
            + TITLE + " サービス = {1}\n"
    );

    @Override
    public String toString() {
        List<Service> l = this.getServiceList();
        StringBuilder s = new StringBuilder();
        for (Service val : l) {
            s.append(val);
        }
        Object[] parameters = {super.toString(),
            s.toString(),};
        return DESC.format(parameters);
    }
}
