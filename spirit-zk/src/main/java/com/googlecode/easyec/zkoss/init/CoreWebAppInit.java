package com.googlecode.easyec.zkoss.init;

import com.googlecode.easyec.zkoss.DelegatingVariableResolver;
import org.apache.commons.lang.StringUtils;
import org.zkoss.lang.Library;
import org.zkoss.zk.ui.WebApp;

import java.util.Arrays;
import java.util.List;

/**
 * Created by JunJie on 4/26/16.
 */
public class CoreWebAppInit implements org.zkoss.zk.ui.util.WebAppInit {

    private static String CORE_RESOLVER = CoreVariableResolver.class.getName();

    @Override
    public void init(WebApp wapp) throws Exception {
        String resolverClass = DelegatingVariableResolver.RESOLVER_CLASS;
        String clazz = Library.getProperty(resolverClass);
        if (StringUtils.isBlank(clazz)) {
            Library.setProperty(resolverClass, CORE_RESOLVER);
        } else {
            String[] classes = StringUtils.split(clazz, ",");
            List<String> list = Arrays.asList(classes);
            list.add(CORE_RESOLVER);

            Library.setProperty(
                resolverClass,
                StringUtils.join(list, ",")
            );
        }
    }
}
