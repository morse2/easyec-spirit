package com.googlecode.easyec.spirit.web.utils;

import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Created by 俊杰 on 2014/5/3.
 */
public class WebUtils {

    private static final Logger logger = LoggerFactory.getLogger(WebUtils.class);

    private WebUtils() { /* no op*/ }

    public static String encodeQueryString(Map<String, String> params) {
        if (MapUtils.isEmpty(params)) return "";

        StringBuffer buf = new StringBuffer();
        Iterator<String> iter = params.keySet().iterator();
        for (int i = 0; iter.hasNext(); i++) {
            if (i > 0) buf.append("&");

            String key = iter.next();
            String val = params.get(key);
            logger.debug("Query string [" + i + "], key: ["
                + key + "], value: [" + val + "].");

            buf.append(key).append("=").append(val);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("All query string: [{}].", buf);
        }

        return buf.toString();
    }

    public static Map<String, String> decodeQueryString(HttpServletRequest request) {
        return decodeQueryString(request.getQueryString());
    }

    public static Map<String, String> decodeQueryString(String qs) {
        if (isBlank(qs)) return Collections.emptyMap();

        logger.debug("Decode query string is: [{}].", qs);

        Map<String, String> params = new HashMap<String, String>();
        StringTokenizer tokenizer = new StringTokenizer(qs, "&");
        while (tokenizer.hasMoreTokens()) {
            String[] parts = tokenizer.nextToken().split("=");
            if (logger.isDebugEnabled()) {
                logger.debug("Parameter is: {}.", Arrays.toString(parts));
            }

            params.put(parts[0], parts[1]);
        }

        return params;
    }
}
