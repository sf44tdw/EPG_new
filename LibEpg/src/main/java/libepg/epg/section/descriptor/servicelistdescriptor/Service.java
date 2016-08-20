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

import libepg.epg.section.descriptor.servicedescriptor.SERVICE_TYPE;
import libepg.util.bytearray.ByteConverter;
import libepg.util.bytearray.ByteDataBlock;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * サービス
 *
 * @author normal
 */
public final class Service {

    private final ByteDataBlock data;

    public Service(byte[] data) {
        this.data = new ByteDataBlock(data);
    }

    public byte[] getData() {
        return data.getData();
    }

    /**
     * service_id(サービス識別):これは16ビットのフィールドで、
     * そのトランスポートストリーム内の他のサービスからこのサービスを識別するためのラベルの役割をする。
     * サービス識別は、対応するプログラムマップセクション内の放送番組番号識別（program_number）に等しい。
     *
     * @return 上記の値
     */
    public final synchronized int getService_id() {
        byte[] t = new byte[2];
        System.arraycopy(this.getData(), 0, t, 0, t.length);
        int temp = ByteConverter.bytesToInt(t);
        return temp;
    }

    /**
     * service_type(サービス形式種別):これは8ビットのフィールドで、サービスの種類を表す。 <br>
     *
     * @return サービス形式種別。 <br>
     */
    public synchronized int getService_type() {
        return ByteConverter.byteToInt(this.getData()[2]);
    }

    /**
     * service_type(サービス形式種別)に対応する定数を表示する。 <br>
     *
     * @return サービス形式種別。 <br>
     */
    public synchronized SERVICE_TYPE getService_type_Enum() {
        return SERVICE_TYPE.reverseLookUp(this.getService_type());
    }

    @Override
    public String toString() {
        return "サービス データ = " + this.data + "\n"
                + "サービス サービス識別 = " + Integer.toHexString(this.getService_id()) + "\n"
                + "サービス サービス形式種別 = " + Integer.toHexString(this.getService_type()) + "\n"
                + "サービス サービス形式種別(定数) = " + this.getService_type_Enum() + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj, true);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(3, 47, this);
    }

}
