package com.googlecode.easyec.zkoss.servlet.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.view.AbstractUrlBasedView;
import org.zkoss.lang.Exceptions;
import org.zkoss.mesg.Messages;
import org.zkoss.web.servlet.Servlets;
import org.zkoss.web.servlet.http.Https;
import org.zkoss.zk.mesg.MZk;
import org.zkoss.zk.ui.*;
import org.zkoss.zk.ui.ext.Includer;
import org.zkoss.zk.ui.http.DesktopRecycles;
import org.zkoss.zk.ui.http.ExecutionImpl;
import org.zkoss.zk.ui.http.I18Ns;
import org.zkoss.zk.ui.http.WebManager;
import org.zkoss.zk.ui.impl.RequestInfoImpl;
import org.zkoss.zk.ui.metainfo.PageDefinition;
import org.zkoss.zk.ui.metainfo.PageDefinitions;
import org.zkoss.zk.ui.sys.*;
import org.zkoss.zk.ui.util.Configuration;
import org.zkoss.zk.ui.util.DesktopRecycle;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 基于Spring配置的zul视图呈现的实现类
 *
 * @author JunJie
 */
public class ZHtmlView extends AbstractUrlBasedView {

    private static final Logger log = LoggerFactory.getLogger(ZHtmlView.class);
    private boolean _compress = true;

    @Override
    public boolean checkResource(Locale locale) throws Exception {
        return new File(
            getServletContext().getRealPath("/") + getUrl()
        ).exists();
    }

    /**
     * 设置是否压缩zul的内容
     *
     * @param compress 压缩标记
     */
    public void setCompress(boolean compress) {
        this._compress = compress;
    }

    @Override
    protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        final Session sess = WebManager.getSession(getServletContext(), request);
        if (!SessionsCtrl.requestEnter(sess)) {
            response.sendError(
                HttpServletResponse.SC_SERVICE_UNAVAILABLE,
                Messages.get(MZk.TOO_MANY_REQUESTS)
            );

            return;
        }

        try {
            final Object old = I18Ns.setup(
                sess, request, response, sess.getWebApp().getConfiguration().getResponseCharset()
            );

            try {
                if (!process(sess, request, response, getUrl(), false)) {
                    _handleError(sess, request, response, getUrl(), null);
                }
            } catch (Throwable ex) {
                log.error(ex.getMessage(), ex);

                _handleError(sess, request, response, getUrl(), ex);
            } finally {
                I18Ns.cleanup(request, old);
            }
        } finally {
            SessionsCtrl.requestExit(sess);
        }
    }

    protected boolean process(Session sess, HttpServletRequest request, HttpServletResponse response, String path, boolean bRichlet)
        throws ServletException, IOException {
        final WebApp webApp = sess.getWebApp();
        final WebAppCtrl webAppCtrl = (WebAppCtrl) webApp;
        final Configuration config = webApp.getConfiguration();

        final boolean bInclude = Servlets.isIncluded(request);
        final boolean compress = _compress && !bInclude;
        final Writer out = compress ? (Writer) new StringWriter() : response.getWriter();
        final DesktopRecycle dtrc = bInclude ? null : config.getDesktopRecycle();
        final ServletContext ctx = getServletContext();

        Desktop desktop = dtrc != null
            ? DesktopRecycles.beforeService(dtrc, ctx, sess, request, response, path)
            : null;

        try {
            if (desktop != null) { //recycle
                final Page page = _getMainPage(desktop);
                if (page != null) {
                    final Execution exec = new ExecutionImpl(ctx, request, response, desktop, page);
                    WebManager.setDesktop(request, desktop);
                    webAppCtrl.getUiEngine().recycleDesktop(exec, page, out);
                } else
                    desktop = null; //something wrong (not possible; just in case)
            }

            // check voided for ZK-2352: Executions.forward() will show IOException warning
            boolean voided = false;
            if (desktop == null) {
                desktop = WebManager.getWebManagerIfAny(ctx).getDesktop(sess, request, response, path, true);
                if (desktop == null) //forward or redirect
                    return true;

                final RequestInfo ri = new RequestInfoImpl(
                    webApp, sess, desktop, request,
                    PageDefinitions.getLocator(webApp, path)
                );

                ((SessionCtrl) sess).notifyClientRequest(true);

                final UiFactory uf = webAppCtrl.getUiFactory();
                if (uf.isRichlet(ri, bRichlet)) {
                    final Richlet richlet = uf.getRichlet(ri, path);
                    if (richlet == null)
                        return false; //not found

                    final Page page = WebManager.newPage(uf, ri, richlet, response, path);
                    final Execution exec = new ExecutionImpl(
                        ctx, request, response, desktop, page);
                    webAppCtrl.getUiEngine().execNewPage(exec, richlet, page, out);
                    //no need to set device type here, since UiEngine will do it later
                } else {
                    final PageDefinition pagedef = uf.getPageDefinition(ri, path);
                    if (pagedef == null) return false; //not found

                    final Page page = WebManager.newPage(uf, ri, pagedef, response, path);
                    final Execution exec = new ExecutionImpl(
                        ctx, request, response, desktop, page);
                    webAppCtrl.getUiEngine().execNewPage(exec, pagedef, page, out);
                    voided = exec.isVoided();
                }
            }

            // check voided to ignore the IOExecuption that caused by Executions.forward()
            if (compress && !voided) {
                final String result = out.toString();

                try {
                    final OutputStream os = response.getOutputStream();
                    //Call it first to ensure getWrite() is not called yet

                    byte[] data = result.getBytes(config.getResponseCharset());
                    if (data.length > 200) {
                        byte[] bs = Https.gzip(request, response, null, data);
                        if (bs != null) data = bs; //yes, browser support compress
                    }

                    response.setContentLength(data.length);
                    os.write(data);
                    response.flushBuffer();
                } catch (IllegalStateException ex) { //getWriter is called
                    response.getWriter().write(result);
                }
            }
        } finally {
            if (dtrc != null) {
                DesktopRecycles.afterService(dtrc, desktop);
            }
        }

        return true; //success
    }

    /* 处理zul解析异常的方法 */
    private void _handleError(Session sess, HttpServletRequest request, HttpServletResponse response, String path, Throwable err)
        throws ServletException, IOException {
        _resetOwner();

        //Note: if not included, it is handled by Web container
        if (err != null && Servlets.isIncluded(request)) {
            //Bug 1714094: we have to handle err, because Web container
            //didn't allow developer to intercept errors caused by inclusion
            final String errpg = sess.getWebApp().getConfiguration()
                .getErrorPage(sess.getDeviceType(), err);
            if (errpg != null) {
                try {
                    request.setAttribute("javax.servlet.error.message", Exceptions.getMessage(err));
                    request.setAttribute("javax.servlet.error.exception", err);
                    request.setAttribute("javax.servlet.error.exception_type", err.getClass());
                    request.setAttribute("javax.servlet.error.status_code", 500);
                    if (process(sess, request, response, errpg, false)) return; //done

                    log.warn("The error page not found: " + errpg);
                } catch (IOException ex) { //eat it (connection off)
                } catch (Throwable ex) {
                    log.warn("Failed to load the error page: " + errpg, ex);
                }
            }
        }

        _handleError(getServletContext(), request, response, path, err);
    }

    /* 获取Main页面对象的方法 */
    private Page _getMainPage(Desktop desktop) {
        for (final Page page : desktop.getPages()) {
            if (((PageCtrl) page).getOwner() == null) {
                return page;
            }
        }

        return null;
    }

    /* 处理zul解析异常的方法 */
    void _handleError(ServletContext ctx, HttpServletRequest request, HttpServletResponse response, String path, Throwable err)
        throws ServletException, IOException {
        if (Servlets.isIncluded(request)) {
            final String msg = (err != null)
                ? Messages.get(MZk.PAGE_FAILED,
                new Object[] { path, Exceptions.getMessage(err),
                    Exceptions.formatStackTrace(null, err, null, 6) })
                : Messages.get(MZk.PAGE_NOT_FOUND, new Object[] { path });

            final Map<String, String> attrs = new HashMap<String, String>();
            attrs.put(org.zkoss.web.Attributes.ALERT_TYPE, "error");
            attrs.put(org.zkoss.web.Attributes.ALERT, msg);
            Servlets.include(ctx, request, response,
                "~./html/alert.dsp", attrs, Servlets.PASS_THRU_ATTR);
        } else {
            //If not included, let the Web container handle it
            if (err != null) {
                if (err instanceof ServletException) {
                    throw (ServletException) err;
                } else if (err instanceof IOException) {
                    throw (IOException) err;
                } else {
                    throw UiException.Aide.wrap(err);
                }
            }

            response.sendError(HttpServletResponse.SC_NOT_FOUND, path);
        }
    }

    /* 重置方法 */
    private void _resetOwner() {
        final Execution exec = Executions.getCurrent();
        if (exec != null) {
            final Component comp = ((ExecutionCtrl) exec).getVisualizer().getOwner();
            if (comp instanceof Includer) {
                ((Includer) comp).setChildPage(null);
            }
        }
    }
}
