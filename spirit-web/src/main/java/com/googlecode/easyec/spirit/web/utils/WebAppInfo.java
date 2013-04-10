package com.googlecode.easyec.spirit.web.utils;

import org.apache.commons.lang.StringUtils;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: JunJie
 * Date: 12-5-19
 * Time: 上午12:18
 * To change this template use File | Settings | File Templates.
 */
class WebAppInfo {

    private static WebAppInfo webAppInfo;

    private String fullPath;
    private String attachPath;
    private String attachTypes[] = new String[0];
    private int attachSize = -1;

    WebAppInfo(String fullPath, String attachPath, String attachTypes, String attachSize) {
        if (StringUtils.isNotBlank(fullPath)) {
            if (fullPath.endsWith(File.separator)) {
                fullPath = fullPath.substring(0, fullPath.lastIndexOf(File.separator));
            }

            this.fullPath = fullPath;
        }

        if (StringUtils.isNotBlank(attachPath)) {
            if (attachPath.startsWith(File.separator)) {
                attachPath = attachPath.replaceFirst(File.separator, "");
            }

            if (attachPath.endsWith(File.separator)) {
                attachPath = attachPath.substring(0, attachPath.lastIndexOf(File.separator));
            }

            this.attachPath = attachPath;
        }

        if (StringUtils.isNotBlank(attachTypes)) {
            this.attachTypes = attachTypes.split(",\\s*");
        }

        if (StringUtils.isNotBlank(attachSize)) {
            try {
                this.attachSize = Integer.parseInt(attachSize);
            } catch (NumberFormatException e) {
                this.attachSize = -1;
            }
        }
    }

    static WebAppInfo instantiate(String fullPath, String attachPath, String attachTypes, String attachSize) {
        webAppInfo = new WebAppInfo(fullPath, attachPath, attachTypes, attachSize);
        return webAppInfo;
    }

    static WebAppInfo getDefault() {
        return webAppInfo;
    }

    public String getFullPath() {
        return fullPath;
    }

    public String getAttachPath() {
        return attachPath;
    }

    public String[] getAttachTypes() {
        return attachTypes;
    }

    public int getAttachSize() {
        return attachSize;
    }
}
