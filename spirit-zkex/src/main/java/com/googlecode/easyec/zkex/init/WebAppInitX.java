package com.googlecode.easyec.zkex.init;

import com.googlecode.easyec.zkex.bind.impl.Init;
import org.zkoss.zk.ui.WebApp;
import org.zkoss.zk.ui.util.WebAppInit;

public class WebAppInitX implements WebAppInit {

    public void init(WebApp wapp) throws Exception {
        Init.init(wapp);
    }
}
