package com.googlecode.easyec.spirit.web.webservice.factory.impl;

import com.googlecode.easyec.spirit.web.webservice.factory.StreamObjectFactory;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.Charset;

/**
 * 字符串格式的对象工厂类
 *
 * @author JunJie
 */
public class StringObjectFactory implements StreamObjectFactory {

    private Charset charset = Charset.defaultCharset();

    public void setCharset(String charsetName) {
        if (StringUtils.isNotBlank(charsetName)) {
            this.charset = Charset.forName(charsetName);
        }
    }

    @Override
    public byte[] writeValue(Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                return ((String) obj).getBytes(charset);
            } else {
                return obj.toString().getBytes(charset);
            }
        }

        return new byte[0];
    }

    @Override
    public <T> T readValue(byte[] bs, Class<T> classType) {
        return classType.cast(new String(bs, charset));
    }

    @Override
    public boolean accept(String contentType) {
        return true;
    }
}
