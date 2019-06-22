package com.googlecode.easyec.zkoss.viewmodel;

import com.googlecode.easyec.zkoss.ui.Steps;
import com.googlecode.easyec.zkoss.ui.builders.UiBuilder;
import com.googlecode.easyec.zkoss.ui.builders.UriUiParameterBuilder;
import com.googlecode.easyec.zkoss.ui.events.StepOutEvent;
import org.zkoss.bind.Binder;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Events;
import org.zkoss.zk.ui.util.ComponentActivationListener;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import static org.zkoss.bind.BindComposer.BINDER_ID;

/**
 * 视图模型的接口类。该类定义了
 * ZK中如果要使用模型-视图，
 * 则必须实现此接口。
 *
 * @author junjie
 */
public interface ViewModelAware<T extends Component> extends Serializable, Steps, ComponentActivationListener {

    /**
     * 返回当前注入的ZK UI构建器对象实例
     *
     * @return <code>UiBuilder</code>
     */
    UiBuilder getUiBuilder();

    /**
     * 返回当前组件的引用对象
     *
     * @return <code>Component</code>对象实例
     */
    T getSelf();

    /**
     * 获取当前已设置的
     * <code>{@link Binder}</code>
     * 对象实例
     *
     * @return <code>Binder</code>对象的实现
     */
    default Binder getBinder() {
        return getBinder(getSelf());
    }

    /**
     * 获取给定的组件对象中
     * 设置的<code>{@link Binder}</code>
     * 对象实例
     *
     * @param comp <code>Component</code>对象
     * @return <code>Binder</code>对象的实现
     */
    default Binder getBinder(Component comp) {
        if (comp == null) return null;
        return (Binder) comp.getAttribute(
            (String) comp.getAttribute(BINDER_ID)
        );
    }

    @Override
    default void stepIn(String uri) {
        stepIn(uri, Collections.emptyMap());
    }

    @Override
    default void stepIn(String uri, Map<Object, Object> args) {
        stepIn(
            UriUiParameterBuilder
                .create()
                .setUri(uri)
                .setParent(getSelf())
                .setArgs(args)
        );
    }

    @Override
    default void stepOut() {
        stepOut(null);
    }

    @Override
    default void stepOut(Object data) {
        Events.postEvent(new StepOutEvent(getSelf(), data));
    }
}
