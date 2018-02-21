package com.googlecode.easyec.zkoss.ui.pushstate;

import org.apache.commons.collections4.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.json.JSONObject;
import org.zkoss.json.JSONValue;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Desktop;
import org.zkoss.zk.ui.Executions;
import org.zkoss.zk.ui.util.Clients;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.Collections.synchronizedList;

public class PushState {

    private static Logger logger = LoggerFactory.getLogger(PushState.class);
    private static final String STATE_LIST = "$PopState$";
    private static final String PARAM_INDEX = "_index_";

    /**
     * A ZK wrapper of JS <code>history.pushState()</code>.
     * It will use {@link JSONValue#toJSONString(Object)} to generate JSON string.
     *
     * @param state The state of new url.
     * @param title The browser title, but most browser does not implement.
     * @param url   New url.
     * @see JSONValue#toJSONString(Object)
     * @see JSONObject#toJSONString(Map)
     */
    public static void push(Map<String, ?> state, String title, String url) {
        StringBuffer js = new StringBuffer("history.pushState(");
        js.append(JSONValue.toJSONString(state));
        js.append(",'");
        js.append(title);
        js.append("','");
        js.append(url);
        js.append("')");
        Clients.evalJavaScript(js.toString());
    }

    /**
     * 将<code>PopState</code>推送至当前桌面中
     *
     * @param state <code>PopState</code>对象实例
     */
    public static void push(PopState state) {
        if (state != null) {
            Desktop desktop = Executions.getCurrent().getDesktop();
            @SuppressWarnings("unchecked")
            List<PopState> states = (List<PopState>) desktop.getAttribute(STATE_LIST);
            if (states == null) {
                states = synchronizedList(new ArrayList<>());
                desktop.setAttribute(STATE_LIST, states);
            }

            Component _comp = state.getComponent();
            // 将当前组件状态缓存进ZK桌面
            states.add(state);

            Map<String, Object> params = new HashMap<>();
            params.put(PARAM_INDEX, states.size() - 1);
            // 将当前组件状态推送至客户端
            push(params, "", "");
        }
    }

    /**
     * 从客户端中的状态参数中获取
     * 当前缓存在桌面中的<code>PopState</code>
     * 对象实例。如果没能找到，则返回null
     *
     * @param state <code>Map</code>
     * @return <code>PopState</code>
     */
    public static PopState poll(Map<String, ?> state) {
        if (MapUtils.isEmpty(state)) return null;

        Integer _index = (Integer) state.get(PARAM_INDEX);
        logger.debug("PopState's index is: [{}].", _index);

        if (_index == null) return null;

        Desktop desktop = Executions.getCurrent().getDesktop();
        @SuppressWarnings("unchecked")
        List<PopState> states = (List<PopState>) desktop.getAttribute(STATE_LIST);
        return states != null && _index < states.size() ? states.get(_index) : null;
    }
}
