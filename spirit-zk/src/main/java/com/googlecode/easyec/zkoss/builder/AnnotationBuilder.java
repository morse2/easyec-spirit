package com.googlecode.easyec.zkoss.builder;

import com.googlecode.easyec.zkoss.builder.paratemters.AnnotationParameter;
import org.apache.commons.lang.ArrayUtils;
import org.zkoss.bind.Binder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.sys.ComponentCtrl;

import java.util.Map;

import static org.zkoss.bind.impl.BindEvaluatorXUtil.parseArgs;
import static org.zkoss.bind.impl.BinderImpl.BINDER;

/**
 * 组件注解构建类
 *
 * @author JunJie
 */
public class AnnotationBuilder {

    /**
     * 为组件设置注解
     *
     * @param ctrl      组件控制对象
     * @param parameter 组件注解参数对象
     */
    public static void addAnnotation(ComponentCtrl ctrl, AnnotationParameter parameter) {
        addAnnotation(getBinder((Component) ctrl), ctrl, parameter);
    }

    /**
     * 为组件设置一组注解
     *
     * @param ctrl       组件控制对象
     * @param parameters 组件注解参数对象集合
     */
    public static void addAnnotations(ComponentCtrl ctrl, AnnotationParameter[] parameters) {
        addAnnotations(getBinder((Component) ctrl), ctrl, parameters);
    }

    /**
     * 为组件设置注解
     *
     * @param binder    Binder对象实例
     * @param ctrl      组件控制对象
     * @param parameter 组件注解参数对象
     */
    public static void addAnnotation(Binder binder, ComponentCtrl ctrl, AnnotationParameter parameter) {
        addAnnotations(binder, ctrl, new AnnotationParameter[] { parameter });
    }

    /**
     * 通过给定组件，查找其关联的
     * {@link Binder}对象
     *
     * @param comp ZK组件
     * @return <code>Binder</code>
     */
    public static Binder getBinder(Component comp) {
        Component _this = comp;
        Binder binder = null;

        do {
            binder = (Binder) _this.getAttribute(BINDER);
            if (binder != null) break;

            _this = _this.getParent();
        } while (_this != null);

        return binder;
    }

    /**
     * 为组件设置一组注解
     *
     * @param binder     Binder对象实例
     * @param ctrl       组件控制对象
     * @param parameters 组件注解参数对象集合
     */
    public static void addAnnotations(Binder binder, ComponentCtrl ctrl, AnnotationParameter[] parameters) {
        for (AnnotationParameter parameter : parameters) {
            ctrl.addAnnotation(
                parameter.getProperty(),
                parameter.getAnnotateName(),
                parameter.getParameters()
            );

            // since 0.5.4
            if (binder != null) {
                Map<String, String[]> params = parameter.getParameters();
                String[] commands = params.get("value");
                if (ArrayUtils.isNotEmpty(commands)) {
                    binder.addCommandBinding(
                        (Component) ctrl, parameter.getProperty(), commands[0],
                        parseArgs(binder.getEvaluatorX(), params)
                    );
                }
            }
        }
    }
}
