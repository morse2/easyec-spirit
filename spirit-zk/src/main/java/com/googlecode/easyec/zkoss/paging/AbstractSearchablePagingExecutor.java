package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsFilter;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsTransform;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.impl.FuzzyMatchTermsTransform;
import com.googlecode.easyec.spirit.web.qseditors.CustomDateQsEditor;
import com.googlecode.easyec.spirit.web.qseditors.CustomNumberQsEditor;
import com.googlecode.easyec.spirit.web.qseditors.CustomStringQsEditor;
import com.googlecode.easyec.spirit.web.qseditors.QueryStringEditor;
import com.googlecode.easyec.spirit.web.utils.WebUtils;
import com.googlecode.easyec.zkoss.paging.terms.AfterRenderListenerSearchTermFilter;
import com.googlecode.easyec.zkoss.paging.terms.BindComposerSearchTermFilter;
import com.googlecode.easyec.zkoss.paging.terms.BlankStringSearchTermFilter;
import org.springframework.util.Assert;
import org.zkoss.xel.fn.CommonFns;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.Event;
import org.zkoss.zk.ui.event.EventListener;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.FormatInputElement;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.NumberInputElement;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static com.googlecode.easyec.zkoss.utils.SelectorUtils.find;
import static org.apache.commons.collections4.MapUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.zkoss.zk.ui.event.Events.ON_OK;
import static org.zkoss.zul.event.ZulEvents.ON_AFTER_RENDER;

/**
 * 抽象的分页搜索操作执行器类。
 * 此类提供了基本的分页搜索的方法。
 *
 * @author JunJie
 */
public abstract class AbstractSearchablePagingExecutor<T extends Component> extends AbstractPagingExecutor<T>
    implements SearchablePagingExecutor {

    public static final String AFTER_RENDER_LISTENER = "afterRenderListener";
    private static final long serialVersionUID = -3852863972879222491L;

    /**
     * 构造方法。
     *
     * @param paging 分页组件对象
     * @param comp   呈现分页结果组件对象
     */
    protected AbstractSearchablePagingExecutor(Paging paging, T comp) {
        super(paging, comp);
    }

    private static final String SELECTORS = "textbox,combobox,datebox,intbox,decimalbox,checkbox,radiogroup,radio,div";

    /* 不可变的搜索条件集合 */
    private final Map<String, Object> immutableSearchTerms = new HashMap<String, Object>();

    /* 可变的搜索条件组件集合 */
    private final List<Component> searchComponents = new ArrayList<Component>();
    /* 搜索条件的根组件，搜索条件应该放在此组件下 */
    private Component searchScope;

    private boolean searchSelectorsInPage;

    /* URL查询参数 */
    private String qs;

    public void setSearchScope(Component searchScope) {
        this.searchScope = searchScope;
    }

    @Override
    public void setSearchSelectorsInPage(boolean searchSelectorsInPage) {
        this.searchSelectorsInPage = searchSelectorsInPage;
    }

    /**
     * 返回参加查询条件的组件
     *
     * @return 组件列表
     */
    protected List<Component> getSearchComponents() {
        return searchComponents;
    }

    /**
     * 返回查询条件组件的搜索范围。
     *
     * @return 搜索范围的组件对象
     */
    protected Component getSearchScope() {
        return searchScope;
    }

    /**
     * 设置搜索控件的选择器。
     * 此方法会重置当前参与搜索的组件。
     *
     * @param searchSelectors 搜索控件的选择器
     */
    public void setSearchSelectors(String searchSelectors) {
        if (isNotBlank(searchSelectors)) {
            this.searchComponents.clear();
            this.searchComponents.addAll(find(getActualSearchScope(), searchSelectors));
        }
    }

    /**
     * 添加新的搜索控件的选择器。
     * 此方法会追加要参与搜索的组件。
     *
     * @param searchSelectors 搜索控件的选择器
     */
    public void addSearchSelectors(String searchSelectors) {
        if (isNotBlank(searchSelectors)) {
            List<Component> list = find(
                getActualSearchScope(), searchSelectors, isSearchSelectorsInPage()
            );

            // 默认为Input控件添加OnOK事件，提高搜索体验
            for (Component c : list) {
                if (c instanceof InputElement) {
                    c.addEventListener(ON_OK, new KeyPressOKEventListener());
                }

                this.searchComponents.add(c);
            }
        }
    }

    @Override
    public void doInit() {
        // fix at 0.2.8
        // 先初始化搜索组件
        addSearchSelectors(SELECTORS);

        // 如果查询参数可用，则进行解码动作
        int currentPageNumber = 1;
        Map<String, Object> queryMap = null;
        if (isNotBlank(qs)) {
            AbstractSearchFormBean bean = createSearchFormBean();
            bean.setSearchTermsAsText(WebUtils.decodeQueryString(qs));
            currentPageNumber = bean.getPageNumber();
            queryMap = bean.getRawSearchTerms();
        }

        // 默认为Input控件添加OnOK事件，提高搜索体验
        for (Component c : searchComponents) {
            if (queryMap != null && !queryMap.isEmpty()) {
                // 如果组件的ID可用，并且与查询参数的key相等，则设置组件的值
                if (isNotBlank(c.getId()) && queryMap.containsKey(c.getId())) {
                    restoreQsParameter(c, queryMap.remove(c.getId()));
                }
            }
        }

        if (!isLazyLoad()) {
            List<Component> list = find(getActualSearchScope(), "combobox,radiogroup");
            if (!list.isEmpty()) {
                // 初始化统一延迟加载搜索控制事件监听对象
                UniversalLazyLoadingEventListener lstnr
                    = new UniversalLazyLoadingEventListener(list.size(), currentPageNumber);

                for (Component c : list) {
                    boolean shouldAddEventListener
                        = (c instanceof Combobox && ((Combobox) c).getModel() != null)
                        || (c instanceof Radiogroup && ((Radiogroup) c).getModel() != null);
                    logger.debug("Should component be added onAfterRender listener? ["
                        + shouldAddEventListener + "], id: [" + c.getId() + "].");

                    if (shouldAddEventListener) {
                        c.addEventListener(ON_AFTER_RENDER, lstnr);

                        // 设置当前搜索为延迟加载
                        if (!isLazyLoad()) setLazyLoad(true);
                    }
                }
            }
        }

        init0(currentPageNumber);
    }

    @Override
    public void firePaging(int currentPage) {
        firePaging(currentPage, true);
    }

    public void firePaging(int currentPage, boolean withSearchTerms) {
        AbstractSearchFormBean searchFormBean = withSearchTerms ? combineSearchTerms() : clearSearchTerms();
        searchFormBean.setPageNumber(currentPage);
        firePaging(searchFormBean);
    }

    public void setQueryString(String qs) {
        this.qs = qs;
    }

    public String encodeSearchTerms() {
        AbstractSearchFormBean bean = combineSearchTerms(false);
        bean.setPageNumber(getPaging().getActivePage() + 1);

        Map<String, String> qsMap = bean.getSearchTermsAsText();
        Map<String, String> externalQS = createExternalQS(bean);
        if (isNotEmpty(externalQS)) qsMap.putAll(externalQS);

        return WebUtils.encodeQueryString(qsMap);
    }

    public AbstractSearchFormBean getSearchFormBean() {
        return combineSearchTerms();
    }

    public Map<String, Object> getRawSearchTerms() {
        return combineSearchTerms().getRawSearchTerms();
    }

    public Map<String, Object> getSearchTerms() {
        return combineSearchTerms().getSearchTerms();
    }

    public Map<String, Object> getMutableRawSearchTerms() {
        return combineSearchTerms(false).getRawSearchTerms();
    }

    public Map<String, Object> getMutableSearchTerms() {
        return combineSearchTerms(false).getSearchTerms();
    }

    public Map<String, Object> getImmutableSearchTerms() {
        return new HashMap<String, Object>(immutableSearchTerms);
    }

    public boolean removeImmutableSearchTerm(String key) {
        if (isNotBlank(key) && immutableSearchTerms.containsKey(key)) {
            immutableSearchTerms.remove(key);

            return true;
        }

        return false;
    }

    public boolean addImmutableSearchTerm(String key, Object value) {
        if (isNotBlank(key) && null != value) {
            immutableSearchTerms.put(key, value);

            return true;
        }

        return false;
    }

    /**
     * 返回当前搜索组件是否包含ZK的Page范围中
     */
    public boolean isSearchSelectorsInPage() {
        return searchSelectorsInPage;
    }

    /**
     * 为给定的组件设置查询参数值。
     * 当URL查询参数的名字与组件的ID
     * 相等时，该方法才会被触发。
     * <p>
     * <b>注意：</b>
     * 该方法默认为文本框、数字框、
     * 日期框组件对象设置参数值
     * </p>
     *
     * @param c 与查询参数名称相等ID的组件
     * @param v 查询参数值
     */
    protected void restoreQsParameter(Component c, Object v) {
        // 设置文本框的值
        if (c instanceof Textbox) {
            if (c instanceof Combobox) {
                Object o = c.getAttribute(AFTER_RENDER_LISTENER);
                if (o != null) {
                    try {
                        c.addEventListener(
                            ON_AFTER_RENDER,
                            (EventListener<? extends Event>) CommonFns.new_(o, v)
                        );
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                } else {
                    logger.warn("No any listener was set on component combobox. so ignore.");
                }
            } else ((Textbox) c).setRawValue(v);
        }
        // 设置数字框的值
        else if (c instanceof NumberInputElement) {
            ((NumberInputElement) c).setRawValue(v);
        }
        // 设置日期框的值
        else if (c instanceof FormatInputElement) {
            ((FormatInputElement) c).setRawValue(v);
        }
        // 设置单选框、复选框的值
        else if (c instanceof Checkbox) {
            if (v != null && (v instanceof Boolean)) {
                ((Checkbox) c).setChecked((Boolean) v);
            }
        }
        // 设置单选框组件的选中
        else if (c instanceof Radiogroup) {
            Object o = c.getAttribute(AFTER_RENDER_LISTENER);
            if (o != null) {
                try {
                    c.addEventListener(
                        ON_AFTER_RENDER,
                        (EventListener<? extends Event>) CommonFns.new_(o, v)
                    );
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            } else {
                logger.warn("No any listener was set on component radiogroup. so ignore.");
            }
        }
    }

    /**
     * 合并搜索参数。
     *
     * @return <code>AbstractSearchFormBean</code>
     */
    protected AbstractSearchFormBean combineSearchTerms() {
        return combineSearchTerms(true);
    }

    /**
     * 合并搜索参数。
     *
     * @param withImmutableSearchTerms 是否带固定的搜索条件
     * @return <code>AbstractSearchFormBean</code>
     */
    protected AbstractSearchFormBean combineSearchTerms(boolean withImmutableSearchTerms) {
        AbstractSearchFormBean bean = createSearchFormBean();

        if (withImmutableSearchTerms) {
            // 添加固定的搜索条件
            Set<String> keySet = immutableSearchTerms.keySet();
            for (String key : keySet) {
                bean.addSearchTerm(key, immutableSearchTerms.get(key));
            }
        }

        // 添加动态的搜索条件
        for (int j = 0; j < searchComponents.size(); j++) {
            Component c = searchComponents.get(j);

            // 判断此控件是否还有效，如果该控件失效的话，则移除此控件
            if (c.getPage() == null) {
                searchComponents.remove(j--);

                continue;
            }

            // 合并文本框的值
            if (c instanceof Textbox) {
                if (c instanceof Combobox) {
                    int i = ((Combobox) c).getSelectedIndex();
                    if (i > -1) {
                        Comboitem item = ((Combobox) c).getSelectedItem();
                        addOrRemoveSearchArg(c.getId(), item.getValue(), bean);
                    }
                } else if (c instanceof Bandbox) {
                    addOrRemoveSearchArg(c.getId(), ((Bandbox) c).getValue(), bean);
                } else {
                    addOrRemoveSearchArg(c.getId(), ((Textbox) c).getValue(), bean);
                }
            }

            // 合并数字框的值
            else if (c instanceof NumberInputElement) {
                if (c instanceof Intbox) {
                    addOrRemoveSearchArg(c.getId(), ((Intbox) c).getValue(), bean);
                } else if (c instanceof Longbox) {
                    addOrRemoveSearchArg(c.getId(), ((Longbox) c).getValue(), bean);
                } else if (c instanceof Decimalbox) {
                    addOrRemoveSearchArg(c.getId(), ((Decimalbox) c).getValue(), bean);
                } else if (c instanceof Doublebox) {
                    addOrRemoveSearchArg(c.getId(), ((Doublebox) c).getValue(), bean);
                } else if (c instanceof Spinner) {
                    addOrRemoveSearchArg(c.getId(), ((Spinner) c).getValue(), bean);
                } else {
                    addOrRemoveSearchArg(c.getId(), ((Doublespinner) c).getValue(), bean);
                }
            }

            // 合并日期框的值
            else if (c instanceof FormatInputElement) {
                if (c instanceof Datebox) {
                    addOrRemoveSearchArg(c.getId(), ((Datebox) c).getValue(), bean);
                } else {
                    addOrRemoveSearchArg(c.getId(), ((Timebox) c).getValue(), bean);
                }
            }

            // 合并单选框、复选框的值
            else if (c instanceof Checkbox) {
                Checkbox chk = (Checkbox) c;
                if (chk.isChecked()) {
                    addOrRemoveSearchArg(c.getId(), chk.getValue(), bean);
                }
            }

            // 单选框组件
            else if (c instanceof Radiogroup) {
                Radiogroup radiogroup = (Radiogroup) c;
                if (radiogroup.getSelectedIndex() > -1) {
                    addOrRemoveSearchArg(c.getId(), radiogroup.getSelectedItem().getValue(), bean);
                }
            }

            // 读取合并ZK控件中的属性值
            combineComponentAttributes(c, bean);
        }

        return bean;
    }

    /**
     * 清除搜索参数。
     *
     * @return <code>AbstractSearchFormBean</code>
     */
    protected AbstractSearchFormBean clearSearchTerms() {
        AbstractSearchFormBean bean = createSearchFormBean();

        // 添加固定的搜索条件
        Set<String> keySet = immutableSearchTerms.keySet();
        for (String key : keySet) {
            bean.addSearchTerm(key, immutableSearchTerms.get(key));
        }

        // 清除动态的搜索条件
        for (Component c : searchComponents) {
            // 清除文本框的值
            if (c instanceof Textbox) {
                if (c instanceof Combobox) {
                    int i = ((Combobox) c).getSelectedIndex();
                    if (i > -1) {
                        ((Combobox) c).setValue(null);
                    }
                } else if (c instanceof Bandbox) {
                    ((Bandbox) c).setValue(null);
                } else {
                    ((Textbox) c).setValue(null);
                }
            }

            // 清除数字框的值
            else if (c instanceof NumberInputElement) {
                if (c instanceof Intbox) {
                    ((Intbox) c).setValue(null);
                } else if (c instanceof Longbox) {
                    ((Longbox) c).setValue(null);
                } else if (c instanceof Decimalbox) {
                    ((Decimalbox) c).setValue((BigDecimal) null);
                } else if (c instanceof Doublebox) {
                    ((Doublebox) c).setValue(null);
                } else if (c instanceof Spinner) {
                    ((Spinner) c).setValue(null);
                } else {
                    ((Doublespinner) c).setValue(null);
                }
            }

            // 清除日期框的值
            else if (c instanceof FormatInputElement) {
                if (c instanceof Datebox) {
                    ((Datebox) c).setValue(null);
                } else {
                    ((Timebox) c).setValue(null);
                }
            }

            // 清除复选框的值
            else if ((c instanceof Checkbox) && !(c instanceof Radio)) {
                addOrRemoveSearchArg(c.getId(), ((Checkbox) c).isChecked(), bean);
            }

            // 读取合并ZK控件中的属性值
            combineComponentAttributes(c, bean);
        }

        return bean;
    }

    /**
     * 为搜索条件Bean提供一组搜索条件的转换类，
     * 用于转换搜索条件值。
     *
     * @return <code>SearchTermsTransform</code>对象
     */
    protected List<SearchTermsTransform> createSearchTermsTransforms() {
        List<SearchTermsTransform> transforms = new ArrayList<SearchTermsTransform>();
        transforms.add(new FuzzyMatchTermsTransform());

        return transforms;
    }

    /**
     * 为搜索条件Bean提供一组搜索条件的过滤器类，
     * 用于判断是否接受设置的搜索条件。
     *
     * @return <code>SearchTermsFilter</code>对象
     */
    protected List<SearchTermsFilter> createSearchTermsFilters() {
        List<SearchTermsFilter> filters = new ArrayList<SearchTermsFilter>();
        filters.add(new AfterRenderListenerSearchTermFilter());
        filters.add(new BindComposerSearchTermFilter());
        filters.add(new BlankStringSearchTermFilter());

        return filters;
    }

    /**
     * 为搜索条件Bean提供一组URL查询参数编辑器类，
     * 该组编辑类对象用于URL参数和业务对象之间的转换。
     * <p>
     * 此方法默认通过当前注入的ZK组件来判断，
     * 如果有ID，则判断组件类型，为其加入默认
     * 的URL查询参数编辑对象
     * </p>
     *
     * @return 一组<code>QueryStringEditor</code>对象
     */
    protected Map<String, QueryStringEditor> createQueryStringEditors() {
        Map<String, QueryStringEditor> editors = new HashMap<String, QueryStringEditor>();

        for (Component c : searchComponents) {
            // 主键ID不为空
            if (isNotBlank(c.getId())) {
                // 为文本框控件设置QsEditor
                if (c instanceof Textbox) {
                    if (!(c instanceof Combobox)) {
                        editors.put(c.getId(), new CustomStringQsEditor());
                        logger.debug(
                            "Component [" + c.getClass().getName()
                                + "] has been controlled by editor [CustomStringQsEditor]."
                        );
                    }
                }

                // 为数字框控件设置QsEditor
                else if (c instanceof NumberInputElement) {
                    if (c instanceof Intbox) {
                        editors.put(c.getId(), new CustomNumberQsEditor(Integer.class));
                    } else if (c instanceof Longbox) {
                        editors.put(c.getId(), new CustomNumberQsEditor(Long.class));
                    } else if (c instanceof Decimalbox) {
                        editors.put(c.getId(), new CustomNumberQsEditor(BigDecimal.class));
                    } else if (c instanceof Doublebox) {
                        editors.put(c.getId(), new CustomNumberQsEditor(Double.class));
                    } else if (c instanceof Spinner) {
                        editors.put(c.getId(), new CustomNumberQsEditor(Integer.class));
                    } else {
                        editors.put(c.getId(), new CustomNumberQsEditor(Double.class));
                    }

                    logger.debug(
                        "Component [" + c.getClass().getName()
                            + "] has been controlled by editor [CustomNumberQsEditor]."
                    );
                }

                // 为日期或其他格式化的控件设置QsEditor
                else if (c instanceof FormatInputElement) {
                    if ((c instanceof Datebox) || (c instanceof Timebox)) {
                        editors.put(c.getId(), new CustomDateQsEditor());
                        logger.debug(
                            "Component [" + c.getClass().getName()
                                + "] has been controlled by editor [CustomDateQsEditor]."
                        );
                    }
                }
            }
        }

        return editors;
    }

    /**
     * 通过搜索Bean对象，
     * 创建一个额外的QS条件列表
     *
     * @param bean 搜索Bean对象
     * @return URL搜索参数列表
     */
    protected Map<String, String> createExternalQS(AbstractSearchFormBean bean) {
        return null;
    }

    /**
     * 合并添加组件中的自定义属性作为搜索条件。
     *
     * @param c    ZK组件实例
     * @param bean <code>AbstractSearchFormBean</code>
     */
    private void combineComponentAttributes(Component c, AbstractSearchFormBean bean) {
        Map<String, Object> attributes = c.getAttributes();
        if (isNotEmpty(attributes)) {
            Set<String> keySet = attributes.keySet();
            for (String key : keySet) {
                addOrRemoveSearchArg(key, attributes.get(key), bean);
            }
        }
    }

    /**
     * 添加或删除搜索条件。
     *
     * @param id    组件id
     * @param value 组件值
     * @param bean  <code>AbstractSearchFormBean</code>
     */
    private void addOrRemoveSearchArg(String id, Object value, AbstractSearchFormBean bean) {
        if (null != value) {
            if (value instanceof String) {
                if (isNotBlank((String) value)) {
                    bean.addSearchTerm(id, value);
                } else {
                    bean.removeSearchTerm(id);
                }
            } else {
                bean.addSearchTerm(id, value);
            }
        } else {
            bean.removeSearchTerm(id);
        }
    }

    /**
     * 返回实际搜索范围的组件对象。
     *
     * @return <code>Component</code>对象
     */
    private Component getActualSearchScope() {
        return null != searchScope ? searchScope : this._comp;
    }

    /**
     * 创建默认的搜索Bean对象实例
     *
     * @return 搜索Bean对象
     */
    private AbstractSearchFormBean createSearchFormBean() {
        SearchFormBean formBean = new SearchFormBean();
        formBean.setEditors(createQueryStringEditors());
        formBean.setFilters(createSearchTermsFilters());
        formBean.setTransforms(createSearchTermsTransforms());

        return formBean;
    }

    /**
     * 键盘回车事件监听类
     */
    private class KeyPressOKEventListener implements SerializableEventListener<KeyEvent> {

        private static final long serialVersionUID = -3087898078561018010L;

        public void onEvent(KeyEvent event) throws Exception {
            if (ON_OK.equals(event.getName())) {
                AbstractSearchablePagingExecutor.this.firePaging(1);
            }
        }
    }

    /**
     * 统一延迟加载触发分页查询事件监听类
     * 该类在搜索控件中有下拉框组件，并且
     * 当前分页是非延迟加载的情况下才使用。
     */
    private class UniversalLazyLoadingEventListener implements SerializableEventListener<Event> {

        private static final long serialVersionUID = 6635885014600887877L;
        private final AtomicInteger ai;

        private int initialPageNumber;

        private UniversalLazyLoadingEventListener(int count, int page) {
            Assert.isTrue(count > 0, "Parameter 'count' must be greater than 0.");

            this.ai = new AtomicInteger(count);
            this.initialPageNumber = page;
        }

        public void onEvent(Event event) throws Exception {
            if (this.ai.decrementAndGet() == 0) {
                AbstractSearchablePagingExecutor.this.firePaging(initialPageNumber);
            }
        }
    }
}
