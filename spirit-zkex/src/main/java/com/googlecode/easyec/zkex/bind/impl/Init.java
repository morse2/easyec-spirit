package com.googlecode.easyec.zkex.bind.impl;

import com.googlecode.easyec.zkex.bind.BeanValidator;
import com.googlecode.easyec.zkex.bind.FormBeanValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.bind.Validator;
import org.zkoss.bind.impl.SystemConverters;
import org.zkoss.bind.impl.SystemValidators;
import org.zkoss.bind.validator.DeferredValidator;
import org.zkoss.lang.Classes;
import org.zkoss.lang.Library;
import org.zkoss.lang.Strings;
import org.zkoss.util.IllegalSyntaxException;
import org.zkoss.zk.ui.WebApp;

import javax.servlet.ServletException;

public class Init {

    public static final String APP_VALIDATORS = "org.zkoss.bind.appValidators";
    private static final Logger _log = LoggerFactory.getLogger(SystemConverters.class);

    public static void init(WebApp wapp) throws ServletException {
        SystemValidators.set("beanValidator", new DeferredValidator(BeanValidator.class.getName()));
        SystemValidators.set("formBeanValidator", new DeferredValidator(FormBeanValidator.class.getName()));

        String validators = Library.getProperty(APP_VALIDATORS);
        if (!Strings.isBlank(validators)) {
            String[][] pairs = parsePairs(validators);
            for (String[] pair : pairs) {
                try {
                    SystemValidators.set(pair[0], (Validator) Classes.newInstanceByThread(pair[1]));
                } catch (Exception x) {
                    _log.error(x.getMessage(), x);
                }
            }
        }
    }

    private static String[][] parsePairs(String pairs) {
        String[] items = pairs.split(",");
        String[][] result = new String[items.length][];
        for (int i = 0; i < items.length; i++) {
            String[] val = items[i].split("=", 2);
            if (val.length != 2) {
                throw new IllegalSyntaxException("pairs syntax error " + pairs);
            }
            result[i] = new String[2];
            result[i][0] = val[0].trim();
            result[i][1] = val[1].trim();
        }
        return result;
    }
}
