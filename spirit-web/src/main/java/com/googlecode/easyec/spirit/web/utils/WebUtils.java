package com.googlecode.easyec.spirit.web.utils;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.apache.commons.lang.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Web工具类
 *
 * @author JunJie
 */
public class WebUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    private WebUtils() { /* no op*/ }

    /**
     * 编码给定的查询字符串集合对象
     *
     * @param params Map集合对象
     * @return 支持浏览器的查询字符串
     */
    public static String encodeQueryString(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) return "";

        StringBuffer buf = new StringBuffer();
        Iterator<String> iter = params.keySet().iterator();
        for (int i = 0; iter.hasNext(); i++) {
            if (i > 0) buf.append("&");

            String key = iter.next();
            String val = params.get(key);
            logger.debug(
                "Query string [" + i + "], key: ["
                + key + "], value: [" + val + "]."
            );

            buf.append(key).append("=").append(val);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("All query string: [{}].", buf);
        }

        return buf.toString();
    }

    /**
     * 解码查询字符串的内容
     *
     * @param request HttpServletRequest对象
     * @return 查询字符串集合对象
     */
    public static Map<String, String> decodeQueryString(HttpServletRequest request) {
        return decodeQueryString(request.getQueryString());
    }

    /**
     * 解码查询字符串的内容
     *
     * @param qs 查询字符串
     * @return 查询字符串集合对象
     */
    public static Map<String, String> decodeQueryString(String qs) {
        Map<String, String> params = new HashMap<String, String>();
        if (isNotBlank(qs)) {
            logger.debug("Decode query string is: [{}].", qs);

            StringTokenizer tokenizer = new StringTokenizer(qs, "&");
            while (tokenizer.hasMoreTokens()) {
                String[] parts = tokenizer.nextToken().split("=", 2);
                if (logger.isDebugEnabled()) {
                    logger.debug("Parameter is: {}.", Arrays.toString(parts));
                }

                if (isNotEmpty(parts)) {
                    if (parts.length < 2) params.put(parts[0], "");
                    else params.put(parts[0], parts[1]);
                }
            }
        }

        return params;
    }

    /**
     * 获取引用的URI部分
     *
     * @param request HttpServletRequest对象
     * @return 请求的地址的引用URI
     */
    public static String getRefererURI(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        if (isBlank(referer)) return "";

        // 去掉URL参数
        int j = referer.indexOf("?");
        logger.debug("Index of question mark: [{}].", j);

        if (j > -1) referer = referer.substring(0, j);

        // 去掉HTTP或HTTPS
        referer = referer.replaceAll("(http://|https://)", "");

        // 去掉域名、IP、端口信息
        int i = referer.indexOf("/");
        logger.debug("First index of referer: [{}].", i);

        referer = referer.substring(i);

        // 去掉WEB应用的上下文
        String base = request.getContextPath();
        if (isNotBlank(base)) {
            referer = referer.replaceFirst(base, "");
        }

        return referer;
    }
}
