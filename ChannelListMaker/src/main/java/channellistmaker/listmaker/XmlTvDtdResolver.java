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
package channellistmaker.listmaker;

import java.io.IOException;
import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.net.URL;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * xmltv.dtdを読み込むリゾルバ。
 *
 * @author normal
 */
public final class XmlTvDtdResolver implements EntityResolver {

    private static final Log LOG;

    static {
        final Class<?> myClass = MethodHandles.lookup().lookupClass();
        LOG = LogFactory.getLog(myClass);
    }
    public static final String DTD_NAME = "xmltv.dtd";

 
    /**
     * xmltv.dtdを既定の場所(resourcesフォルダ)から読み込ませる。
     * dtdファイルが存在しない場合、このクラスをセットされたDocumentBuilderはデフォルトの動作をする。
     *
     * @author normal
     */
    public XmlTvDtdResolver() {
    }

    /**
     * 公開識別子もしくはシステム識別子にxmltv.dtdを含んだ文字列があった場合、DocumentBuilderに対し、xmltv.dtdを読み込ませる。
     * ファイルが指定されなかった場合はnullを返す。
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        if ((publicId != null && publicId.contains(DTD_NAME)) || (systemId != null && systemId.contains(DTD_NAME))) {
            LOG.info("識別子を確認しました。");
            final InputSource source = new InputSource(XmlTvDtdResolver.class.getClassLoader().getResourceAsStream(DTD_NAME));
            source.setPublicId(publicId);
            source.setSystemId(systemId);
            return source;
        } else {
            LOG.info("公開識別子、システム識別子とも、" + DTD_NAME + " を含む文字列ではありませんでした。");
            return null;
        }
    }
}
