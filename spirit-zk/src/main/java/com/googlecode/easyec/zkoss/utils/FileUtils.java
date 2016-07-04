package com.googlecode.easyec.zkoss.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.util.media.Media;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.io.IOUtils.toInputStream;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * ZK文件处理的工具类
 *
 * @author JunJie
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    private FileUtils() { /* no op */ }

    /**
     * 从<code>Media</code>对象中获取输入流信息
     *
     * @param media 媒体流对象
     * @return 输入流对象
     */
    public static InputStream getStreamData(Media media) {
        if (null == media) return null;

        if (media.isBinary()) {
            return media.inMemory()
                ? new ByteArrayInputStream(media.getByteData())
                : media.getStreamData();
        }

        try {
            return media.inMemory()
                ? toInputStream(media.getStringData())
                : new ByteArrayInputStream(toByteArray(media.getReaderData()));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return null;
        }
    }

    /**
     * 从<code>Media</code>对象中获取字节流信息
     *
     * @param media 媒体流对象
     * @return 字节流对象
     */
    public static byte[] getByteData(Media media) {
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
        if (null == media || isBlank(charset)) return null;

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

        try {
            return media.inMemory()
                ? media.getStringData().getBytes(charset)
                : toByteArray(media.getReaderData(), charset);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);

            return new byte[0];
        }
    }
}
