package com.googlecode.easyec.spirit.mybatis.plugin.support;

import javax.servlet.*;
import java.io.IOException;

public class TransactionalLazyLoadFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        TransactionalContextHolder.setUse(true);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
        try {
            chain.doFilter(req, resp);
        } finally {
            TransactionalContextHolder.clear();
        }
    }

    @Override
    public void destroy() { }
}
