package com.googlecode.easyec.zkoss.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;

import java.nio.charset.Charset;

import static org.apache.commons.io.IOUtils.toByteArray;

/**
 * ZK文件处理的工具类
 *
 * @author JunJie
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() { /* no op */ }

    /**
     * 从<code>Media</code>对象中获取字节流信息
     *
     * @param media 媒体流对象
     * @return 字节流对象
     */
    @Deprecated
    public static byte[] getByteData(Media media) {
        return getByteData(media, "utf-8");
    }

    /**
     * 从<code>Media</code>对象中获取字节流信息
     *
     * @param media 媒体流对象
     * @return 字节流对象
     */
    public static byte[] getUtf8ByteData(Media media) {
        return getByteData(media, "utf-8");
    }

    /**
     * 从<code>Media</code>对象中获取字节流信息
     *
     * @param media   媒体流对象
     * @param charset 字符集
     * @return 字节流对象
     */
    public static byte[] getByteData(Media media, String charset) {
        return getByteData(media, Charset.forName(charset));
    }

    /**
     * 从<code>Media</code>对象中获取字节流信息
     *
     * @param media   媒体流对象
     * @param charset 字符集对象
     * @return 字节流对象
     */
    public static byte[] getByteData(Media media, Charset charset) {
        if (null == media) return null;

        if (media.isBinary()) {
            try {
                return media.inMemory()
                    ? media.getByteData()
                    : toByteArray(media.getStreamData());
            } catch (Exception e) {
                logger.error(e.getMessage(), e);

                return new byte[0];
            }
        }

        Charset _curCharset = charset;
        if (_curCharset == null) {
            _curCharset = Charset.defaultCharset();
        }

        try {
            return media.inMemory()
                ? media.getStringData().getBytes(_curCharset)
                : toByteArray(media.getReaderData(), _curCharset);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return new byte[0];
        }
    }
}
