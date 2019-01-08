package com.googlecode.easyec.zkoss.ui;

import com.googlecode.easyec.zkoss.utils.ExecUtils;
import org.apache.commons.lang3.StringUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.metainfo.PageDefinition;

import javax.servlet.http.HttpServletRequest;

import static com.googlecode.easyec.zkoss.ui.Breadcrumb.TYPE_PAGE;
import static com.googlecode.easyec.zkoss.ui.Breadcrumb.TYPE_URI;
import static com.googlecode.easyec.zkoss.ui.builders.ExecutionUiBuilderImpl.PARAM_PAGE_DEF;
import static com.googlecode.easyec.zkoss.utils.ExecUtils.FindScope.*;

/**
 * 默认的面包屑构建器类
 *
 * @author junjie
 */
public class DefaultBreadcrumbBuilder implements BreadcrumbCtrl {

    /* 面包屑对象 */
    private Breadcrumb _breadcrumb;

    @Override
    public Breadcrumb getBreadcrumb() {
        return _breadcrumb;
    }

    public void init(Component comp) {
        /* 从外部获取面包屑信息 */
        Breadcrumb _bc = ExecUtils.findParam(BC_ID, Arg, Breadcrumb.class);
        if (_bc == null) _bc = ExecUtils.findParam(BC_ID, Execution, Breadcrumb.class);

        // 如果外部没有提供面包屑，则试图构造面包屑
        if (_bc == null) {
            String uri = null;
            HttpServletRequest request = ExecUtils.getNativeRequest();
            if (request != null) {
                uri = request.getRequestURI();
                String base = request.getContextPath();
                if (StringUtils.isNotBlank(base)) {
                    uri = uri.replaceFirst(base, "");
                }
            }

            /*
             * 如果是以/zkau开头，则说明是ZK的ajax
             * 请求创建的页面，那么就获取当前页面的
             * 绝对路径
             */
            if (StringUtils.startsWithIgnoreCase(uri, "/zkau")) {
                PageDefinition _def = ExecUtils.findParam(PARAM_PAGE_DEF, All, PageDefinition.class);
                if (_def != null) this._breadcrumb = createBreadcrumb(_getLabel(comp), _def.getRequestPath(), TYPE_PAGE);
            } else this._breadcrumb = createBreadcrumb(_getLabel(comp), uri, TYPE_URI);
        } else this._breadcrumb = _bc;
    }

    /**
     * 创建面包屑对象实例
     *
     * @param uri  页面URI
     * @param type 面包屑类型
     * @return 面包屑对象
     */
    protected Breadcrumb createBreadcrumb(String label, String uri, int type) {
        return new Breadcrumb(label, uri, type);
    }

    private String _getLabel(Component comp) {
        Object _bcLbl = comp.getAttribute(BC_ID);
        if (_bcLbl != null) {
            if (_bcLbl instanceof String) {
                return (String) _bcLbl;
            } else {
                return _bcLbl.toString();
            }
        }

        return null;
    }
}
