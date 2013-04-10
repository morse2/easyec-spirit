package com.googlecode.easyec.spirit.web.utils;

import eu.medsea.mimeutil.MimeType;
import eu.medsea.mimeutil.MimeUtil;
import eu.medsea.mimeutil.detector.MagicMimeMimeDetector;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-5-18
 * Time: 下午11:22
 * To change this template use File | Settings | File Templates.
 */
public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);
    private static final ThreadLocal<FileUtils> t = new ThreadLocal<FileUtils>();
    private static final DateFormat df = new SimpleDateFormat("yyyyMMdd");
    private WebAppInfo webAppInfo;

    public static enum MimeTypes {
        images
    }

    public String getAppFullPath() {
        return webAppInfo.getFullPath();
    }

    protected FileUtils(WebAppInfo webAppInfo) {
        this.webAppInfo = webAppInfo;

        MimeUtil.registerMimeDetector(MagicMimeMimeDetector.class.getName());
    }

    public static FileUtils getInstance() {
        FileUtils fileUtils = t.get();
        if (fileUtils == null) {
            fileUtils = new FileUtils(WebAppInfo.getDefault());
            t.set(fileUtils);
        }

        return fileUtils;
    }

    public byte[] getFileAsBytes(String relativePath) throws IOException {
        String thisPath = relativePath;
        if (StringUtils.isBlank(thisPath)) {
            return new byte[0];
        }

        if (thisPath.startsWith("/")) {
            thisPath = thisPath.replaceFirst("/+", "");
        }

        StringBuffer path = new StringBuffer();
        path.append(webAppInfo.getFullPath());
        path.append(File.separator);
        path.append(thisPath);

        InputStream in = new FileInputStream(path.toString());
        try {
            return IOUtils.toByteArray(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public String writeTo(MimeTypes type, String filename, byte[] bs) throws IOException {
        StringBuffer path = new StringBuffer();
        path.append(webAppInfo.getFullPath());

        StringBuffer sb = new StringBuffer();
        sb.append(File.separator);
        sb.append(webAppInfo.getAttachPath());
        sb.append(File.separator);
        sb.append(type.name());
        sb.append(File.separator);
        sb.append(df.format(new Date()));
        sb.append(File.separator);
        sb.append(UUIDUtils.getRandomUUID());
        sb.append(".").append(MimeUtil.getExtension(filename));

        path.append(sb.toString());

        File file = new File(path.toString());
        if (!file.getParentFile().exists()) {
            boolean b = file.getParentFile().mkdirs();

            logger.info("do mkdirs() method. result: [" + b + "]. dir: [" + file.getParent() + "].");
        }

        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            out.write(bs, 0, bs.length);
            out.flush();

            return sb.toString().replace(File.separator, "/");
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public boolean deleteFile(String assetPath) {
        StringBuffer path = new StringBuffer();
        path.append(webAppInfo.getFullPath());
        path.append(assetPath.replace("/", File.separator));

        logger.info("Prepare to delete file. Path: [" + path + "].");

        File file = new File(path.toString());

        return file.exists() && file.delete();
    }

    public boolean accept(byte[] bs) {
        Collection<MimeType> c = MimeUtil.getMimeTypes(bs);
        for (MimeType m : c) {
            if (hasTypeIn(m.getSubType())) {
                return true;
            }
        }

        return false;
    }

    private boolean hasTypeIn(String subType) {
        for (String type : webAppInfo.getAttachTypes()) {
            if (subType.contains(type)) {
                return true;
            }
        }

        return false;
    }

    public String getFileSize(InputStream in) {
        try {
            byte[] bs = IOUtils.toByteArray(in);
            return new DecimalFormat("0.00").format(bs.length / 1024.0);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean isOutOfSize(byte[] bs) {
        return webAppInfo.getAttachSize() > 0 && (float) (bs.length / 1024) > webAppInfo.getAttachSize();
    }
}
