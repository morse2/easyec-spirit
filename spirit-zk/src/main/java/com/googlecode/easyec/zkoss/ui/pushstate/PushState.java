package com.googlecode.easyec.zkoss.ui.pushstate;

import org.zkoss.json.JSONObject;
import org.zkoss.json.JSONValue;
import org.zkoss.zk.ui.util.Clients;

import java.util.Map;

public class PushState {

    /**
     * A ZK wrapper of JS <code>history.pushState()</code>.
     * It will use {@link JSONValue#toJSONString(Object)} to generate JSON string.
     * @see JSONValue#toJSONString(Object)
     * @see JSONObject#toJSONString(Map)
     * @param state The state of new url.
     * @param title The browser title, but most browser does not implement.
     * @param url New url.
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
}
