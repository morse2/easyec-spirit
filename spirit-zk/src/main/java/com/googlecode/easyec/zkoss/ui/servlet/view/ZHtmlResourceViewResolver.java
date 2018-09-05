package com.googlecode.easyec.zkoss.ui.servlet.view;

import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

/**
 * 基于Spring的zul文件资源视图解析类
 *
 * @author JunJie
 */
public class ZHtmlResourceViewResolver extends UrlBasedViewResolver {

    private boolean compress = true;

    public ZHtmlResourceViewResolver() {
        setViewClass(requiredViewClass());
    }

    /**
     * 设置是否压缩zul的内容
     *
     * @param compress 压缩标记
     */
    public void setCompress(boolean compress) {
        this.compress = compress;
    }

    /**
     * 返回当前视图解析对象是否要压缩资源的标记
     *
     * @return bool值
     */
    public boolean isCompress() {
        return compress;
    }

    @Override
    protected Class requiredViewClass() {
        return ZHtmlView.class;
    }

    @Override
    protected AbstractUrlBasedView buildView(String viewName) throws Exception {
        ZHtmlView view = (ZHtmlView) super.buildView(viewName);
        view.setServletContext(getServletContext());
        view.setCompress(isCompress());

        return view;
    }
}
