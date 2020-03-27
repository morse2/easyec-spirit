package com.googlecode.easyec.spirit.web.utils;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Stream;

import static org.apache.commons.lang3.ArrayUtils.getLength;
import static org.apache.commons.lang3.ArrayUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.*;

/**
 * Web工具类
 *
 * @author JunJie
 */
public class WebUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    private WebUtils() { /* no op*/ }

    /**
     * 为uri参数适配并添加ctx参数
     *
     * @param ctx web app上下文
     * @param uri URI
     * @return 新的URI
     */
    public static String appendCtx(String ctx, String uri) {
        if (isBlank(ctx)) return uri;
        if (isBlank(uri)) return ctx;
        if (startsWith(uri, ctx)) {
            return uri;
        }
        if (startsWith(uri, "/")) {
            return ctx + uri;
        }

        return uri;
    }

    /**
     * 编码给定的查询字符串集合对象
     *
     * @param params Map集合对象
     * @return 支持浏览器的查询字符串
     */
    public static String encodeQueryString(Map<String, String[]> params) {
        if (MapUtils.isEmpty(params)) return EMPTY;

        final StringBuffer buf = new StringBuffer();
        params.forEach((key, values) -> {
            if (buf.length() > 0) buf.append('&');

            logger.debug(
                "Query string key: [{}], values: [{}].",
                key, Arrays.toString(values)
            );

            final StringBuffer parts = new StringBuffer();
            Stream.of(values).forEach(val -> {
                if (parts.length() > 0) parts.append('&');
                parts.append(key).append('=');

                if (isNotBlank(val)) {
                    try {
                        parts.append(URLEncoder.encode(val, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        parts.append(val);
                    }
                }
            });

            if (parts.length() > 0) buf.append(parts);
        });

        if (logger.isDebugEnabled()) {
            logger.debug("All of query string is: [{}].", buf);
        }

        return buf.toString();
    }

    /**
     * 解码查询字符串的内容
     *
     * @param request HttpServletRequest对象
     * @return 查询字符串集合对象
     */
    public static Map<String, String[]> decodeQueryString(HttpServletRequest request) {
        return decodeQueryString(request.getQueryString());
    }

    /**
     * 解码查询字符串的内容
     *
     * @param qs 查询字符串
     * @return 查询字符串集合对象
     */
    public static Map<String, String[]> decodeQueryString(String qs) {
        Map<String, List<String>> params = new HashMap<>();
        if (isNotBlank(qs)) {
            String decodeQs;

            try {
                decodeQs = URLDecoder.decode(qs, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getMessage(), e);

                decodeQs = qs;
            }

            logger.debug("Decode query string is: [{}].", decodeQs);

            StringTokenizer tokenizer = new StringTokenizer(decodeQs, "&");
            while (tokenizer.hasMoreTokens()) {
                String[] parts = tokenizer.nextToken().split("=", 2);
                if (logger.isDebugEnabled()) {
                    logger.debug("Parameter is: {}.", Arrays.toString(parts));
                }

                if (isNotEmpty(parts)) {
                    List<String> values = params.computeIfAbsent(parts[0], k -> new ArrayList<>());
                    values.add(getLength(parts) < 2 ? EMPTY : parts[1]);
                }
            }
        }

        if (params.isEmpty()) return Collections.emptyMap();
        final Map<String, String[]> result = new HashMap<>(params.size());
        params.forEach((k, v) -> result.put(k, v.toArray(new String[0])));
        return result;
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
