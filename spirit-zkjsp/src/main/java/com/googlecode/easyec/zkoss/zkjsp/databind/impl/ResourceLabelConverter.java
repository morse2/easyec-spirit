package com.googlecode.easyec.zkoss.zkjsp.databind.impl;

import com.googlecode.easyec.zkoss.zkjsp.databind.ReadonlyFieldTypeConverter;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static com.googlecode.easyec.zkoss.utils.CommonFnsUtils.getLabel;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * 国际化消息类型的转换类
 *
 * @author JunJie
 */
public class ResourceLabelConverter extends ReadonlyFieldTypeConverter<Object, String> {

    public static final String ARG_KEY         = "key";
    public static final String ARG_FRAG        = "frag";
    public static final String ARG_DEFAULT_KEY = "default";
    public static final String ARG_NULL_PRINT  = "null-print";

    @Override
    public String coerceToUi(Object val, Map<String, Object> bindingArgs) {
        String defaultKey = (String) bindingArgs.get(ARG_DEFAULT_KEY);
        String messageKey = (String) bindingArgs.get(ARG_KEY);

        // 如果key和当前的值为空，则输出默认的key信息
        if (isBlank(messageKey) && null == val) {
            return isNotBlank(defaultKey)
                ? getLabel(defaultKey)
                : "";
        }

        // 如果默认的key也为空，则输出当前值信息
        if (isBlank(messageKey)) {
            return String.class.isInstance(val)
                ? (String) val
                : val.toString();
        }

        if (null == val) {
            String nullPrint = (String) bindingArgs.get(ARG_NULL_PRINT);
            if (isNotBlank(nullPrint) && ARG_DEFAULT_KEY.equals(nullPrint)) {
                if (isNotBlank(defaultKey)) return getLabel(defaultKey);
            }
        }

        Object[] args = extractArgs(val, bindingArgs);

        if (bindingArgs.containsKey(ARG_FRAG)) {
            Object messageFrag = bindingArgs.get(ARG_FRAG);
            if ("this".equals(messageFrag)) messageFrag = val;

            return getLabel(messageKey, messageFrag, args);
        }

        return getLabel(messageKey, args);
    }

    /**
     * 解析自定义参数数组的方法。
     * <p>
     * 此方法专门负责解析以arg开头的参数，
     * 例如：arg0、arg1等
     * </p>
     *
     * @param val         表达式对象
     * @param bindingArgs 绑定的参数集合
     * @return
     */
    protected Object[] extractArgs(Object val, Map<String, Object> bindingArgs) {
        Map<Integer, Object> args = new TreeMap<Integer, Object>();

        Set<String> keySet = bindingArgs.keySet();
        for (String key : keySet) {
            if (key.matches("arg\\d+$")) {
                Object v = bindingArgs.get(key);
                if (null != v) {
                    if ("this".equals(v)) v = val;

                    args.put(
                        Integer.valueOf(key.replaceAll("arg", "")),
                        v
                    );
                }
            }
        }

        return args.values().toArray();
    }
}
