package com.googlecode.easyec.zkspring.ui.i18n;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.zkoss.web.Attributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Locale;

/**
 * 支持ZK本地化切换的拦截器类
 *
 * @author JunJie
 */
public class LocaleChangeSupportInterceptor extends HandlerInterceptorAdapter implements InitializingBean {

    private LocaleResolver localeResolver;

    public final void setLocaleResolver(LocaleResolver localeResolver) {
        this.localeResolver = localeResolver;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Locale locale = localeResolver.resolveLocale(request);
        if (locale != null) {
            HttpSession session = request.getSession();
            Locale preferredLocale = (Locale) session.getAttribute(Attributes.PREFERRED_LOCALE);
            if (_needReplace(locale, preferredLocale)) {
                session.setAttribute(Attributes.PREFERRED_LOCALE, locale);
            }
        }

        return true;
    }

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(localeResolver, "Class " + LocaleResolver.class.getName() + " cannot be null.");
    }

    /* 判断是否需要替换当前本地化语言的方法 */
    private boolean _needReplace(Locale newLocale, Locale curLocale) {
        return curLocale == null || !curLocale.equals(newLocale);
    }
}
