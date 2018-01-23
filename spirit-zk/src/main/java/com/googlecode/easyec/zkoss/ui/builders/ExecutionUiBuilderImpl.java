package com.googlecode.easyec.zkoss.ui.builders;

import org.apache.commons.lang3.ArrayUtils;
import org.zkoss.bind.Binder;
import org.zkoss.zk.ui.AbstractComponent;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

import java.util.Map;

import static org.zkoss.bind.impl.BindEvaluatorXUtil.parseArgs;
import static org.zkoss.bind.sys.BinderCtrl.BINDER;

/**
 * 基于ZK <code>Execution</code>的UI组件构建的实现类
 *
 * @author junjie
 */
public class ExecutionUiBuilderImpl implements UiBuilder {

    @Override
    public Component manufacture(UiParameter parameter) {
        return Executions.createComponents(
            parameter.getPageDefinition(),
            parameter.getParent(),
            parameter.getArgs()
        );
    }

    @Override
    public Binder getBinder(Component comp) {
        return getBinder(comp, true);
    }

    @Override
    public Binder getBinder(Component comp, boolean withParent) {
        if (comp == null) return null;

        Component _this = comp;
        Binder binder;

        do {
            binder = (Binder) _this.getAttribute(BINDER);
            if (binder != null || !withParent) break;

            _this = _this.getParent();
        } while (_this != null);

        return binder;
    }

    @Override
    public void addAnnotation(AbstractComponent comp, AnnotationParameter... parameters) {
        addAnnotation(comp, true, parameters);
    }

    @Override
    public void addAnnotation(AbstractComponent comp, boolean withBinder, AnnotationParameter... parameters) {
        if (ArrayUtils.isNotEmpty(parameters)) {
            for (AnnotationParameter parameter : parameters) {
                comp.addAnnotation(
                    parameter.getProperty(),
                    parameter.getAnnotateName(),
                    parameter.getArgs()
                );

                if (withBinder) {
                    Binder binder = getBinder(comp);
                    if (binder != null) {
                        Map<String, String[]> params = parameter.getArgs();
                        String[] commands = params.get("value");
                        if (ArrayUtils.isNotEmpty(commands)) {
                            binder.addCommandBinding(
                                comp, parameter.getProperty(), commands[0],
                                parseArgs(binder.getEvaluatorX(), params)
                            );
                        }
                    }
                }
            }
        }
    }
}
