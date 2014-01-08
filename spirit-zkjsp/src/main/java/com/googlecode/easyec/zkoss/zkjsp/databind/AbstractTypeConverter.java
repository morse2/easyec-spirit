package com.googlecode.easyec.zkoss.zkjsp.databind;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.DataBinder;
import org.zkoss.zkplus.databind.TypeConverter;

import java.util.Map;

import static org.apache.commons.collections.MapUtils.isEmpty;

/**
 * Created by 俊杰 on 13-12-26.
 */
public abstract class AbstractTypeConverter implements TypeConverter {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 得到组件中绑定的额外的参数信息
     *
     * @param comp 组件对象
     * @return 额外的参数列表信息
     */
    @SuppressWarnings("unchecked")
    protected Map<String, Object> getBindingArgs(Component comp) {
        return (Map<String, Object>) comp.getAttribute(DataBinder.ARGS);
    }

    /**
     * 返回给定名字的额外绑定的参数值
     *
     * @param comp        组件对象
     * @param bindingName 绑定的名字
     * @return 参数值
     */
    protected Object getBindingArg(Component comp, String bindingName) {
        return getBindingArg(comp, bindingName, Object.class);
    }

    /**
     * 返回给定名字的额外绑定的参数值,
     * 并且试图强制转换成给定的对象类型
     *
     * @param comp        组件对象
     * @param bindingName 绑定的名字
     * @param type        强转类型
     * @param <T>         泛型类型
     * @return 参数值
     */
    protected <T> T getBindingArg(Component comp, String bindingName, Class<T> type) {
        Map<String, Object> args = getBindingArgs(comp);

        if (isEmpty(args) || !args.containsKey(bindingName)) {
            return null;
        }

        Object o = args.get(bindingName);
        logger.debug("Value of binding arg is null? [{}].", (null == o));

        return null == o || !type.isInstance(o) ? null : type.cast(o);
    }
}
