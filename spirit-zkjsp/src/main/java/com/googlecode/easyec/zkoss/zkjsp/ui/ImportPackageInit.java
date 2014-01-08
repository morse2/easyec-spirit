package com.googlecode.easyec.zkoss.zkjsp.ui;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.zkoss.lang.ImportedClassResolver;
import org.zkoss.zk.ui.Page;
import org.zkoss.zk.ui.util.Initiator;

import java.util.Map;
import java.util.Set;

/**
 * 导入类、包的初始化类。
 *
 * @author JunJie
 */
public class ImportPackageInit implements Initiator {

    private static final Logger logger = LoggerFactory.getLogger(ImportPackageInit.class);

    public void doInit(Page page, Map<String, Object> args) throws Exception {
        ImportedClassResolver resl = new ImportedClassResolver();
        addPackageAndClass(resl, args);
        page.addClassResolver(resl);
    }

    private void addPackageAndClass(ImportedClassResolver resl, Map<String, Object> initArgs) {
        Set<String> keySet = initArgs.keySet();
        for (String key : keySet) {
            if (key.matches("arg\\d+$")) {
                Object v = initArgs.get(key);
                if (null != v) {
                    String s = v.toString();

                    if (StringUtils.isNotBlank(s)) {
                        try {
                            resl.addImportedClass(s);
                        } catch (ClassNotFoundException e) {
                            logger.error(e.getMessage(), e);
                        }
                    }
                }
            }
        }
    }
}
