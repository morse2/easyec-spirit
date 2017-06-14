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
import com.googlecode.easyec.zkoss.paging.finder.ValueFinder;
import com.googlecode.easyec.zkoss.paging.finder.impl.*;
import com.googlecode.easyec.zkoss.paging.terms.AfterRenderListenerSearchTermFilter;
import com.googlecode.easyec.zkoss.paging.terms.BindComposerSearchTermFilter;
import com.googlecode.easyec.zkoss.paging.terms.BlankStringSearchTermFilter;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.Predicate;
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
import static org.apache.commons.collections.CollectionUtils.exists;
import static org.apache.commons.collections.MapUtils.isNotEmpty;
import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.zkoss.zk.ui.event.Events.ON_OK;
import static org.zkoss.zul.event.ZulEvents.ON_AFTER_RENDER;

/**
 * 抽象的分页搜索操作执行器类。
 * 此类提供了基本的分页搜索的方法。
 *
 * @author JunJie
 */
public abstract class AbstractSearchablePagingExecutor<T extends Component> extends AbstractPagingExecutor<T> implements SearchablePagingExecutor {

    public static final String AFTER_RENDER_LISTENER = "afterRenderListener";
    public static final String COMPONENT_VALUE_FINDER = "valueFinder";
    public static final String COMP_VALUE_FINDER_ID = "$valueFinder$";
    private static final long serialVersionUID = -9001080708477883494L;

    /**
     * 构造方法。
     *
     * @param paging 分页组件对象
     * @param comp   呈现分页结果组件对象
     */
    protected AbstractSearchablePagingExecutor(Paging paging, T comp) {
        super(paging, comp);
    }

    private static final String SELECTORS
        = "bandbox,checkbox,combobox,datebox,decimalbox,doublebox,doublespinner,intbox,longbox,radiogroup,radio,spinner,textbox,timebox";

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
            this.searchComponents.addAll(
                find(
                    getActualSearchScope(),
                    searchSelectors,
                    isSearchSelectorsInPage()
                )
            );
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
                getActualSearchScope(),
                searchSelectors,
                isSearchSelectorsInPage()
            );

            // 默认为Input控件添加OnOK事件，提高搜索体验
            for (Component c : list) {
                boolean exists = exists(
                    getSearchComponents(),
                    new ComponentUuidPredicate(c.getUuid())
                );
                logger.debug("Is this component [{}] in search scope? [{}]",
                    c.getUuid(), exists);

                if (exists) continue;

                ValueFinder<Component> finder = createValueFinder(c);
                if (finder == null) {
                    logger.warn("Component. ID: [{}], class: [{}] has no any ValueFinder. So it will be ignore to search.",
                        c.getId(), c.getClass().getName());

                    continue;
                }

                // 设置ValueFinder实例
                c.setAttribute(COMP_VALUE_FINDER_ID, finder);

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
            List<Component> list = find(
                getActualSearchScope(),
                "combobox,radiogroup",
                isSearchSelectorsInPage()
            );

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
                    // 修复问题：如果Combobox火Radiogroup没有使用model
                    // 来呈现参数，那么延迟加载的数量就减一
                    else lstnr.ai.decrementAndGet();
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
        // 下拉框
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
        }
        // 单选框
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
        // 其他组件
        else {
            extractSearchValue(c, true);
        }
    }

    @SuppressWarnings("unchecked")
    protected <C extends Component> ValueFinder<C> createValueFinder(C c) {
        Object finder = c.getAttribute(COMPONENT_VALUE_FINDER);
        if (finder != null) {
            if (finder instanceof ValueFinder) {
                c.setAttribute(COMP_VALUE_FINDER_ID, finder);
            } else {
                Object finderObj;

                try {
                    finderObj = CommonFns.new_(finder);
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);

                    throw new IllegalArgumentException(e);
                }

                if (!(finderObj instanceof ValueFinder)) {
                    throw new IllegalArgumentException("The object must be instance of ValueFinder.");
                }

                return (ValueFinder<C>) finderObj;
            }
        }

        return _createValueFinder(c);
    }

    /**
     * 根据给定的组件实例，创建自定义的
     * <code>ValueFinder</code>对象。
     * 如果该方法返回null，则该组件将被忽略，
     * 不会有PagingExecutor维护搜索的功能。
     *
     * @param c   ZK组件对象实例
     * @param <C> 范型类型
     * @return <code>ValueFinder</code>对象
     */
    protected <C extends Component> ValueFinder<C> createCustomValueFinder(C c) {
        return null;
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
        for (int i = 0; i < searchComponents.size(); i++) {
            Component c = searchComponents.get(i);

            // 判断此控件是否还有效，如果该控件失效的话，则移除此控件
            if (c.getPage() == null) {
                searchComponents.remove(i--);

                continue;
            }

            _addSearchArg(bean, c, false);
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
        for (int i = 0; i < searchComponents.size(); i++) {
            Component c = searchComponents.get(i);

            if (c.getPage() == null) {
                searchComponents.remove(i--);

                continue;
            }

            _addSearchArg(bean, c, true);
        }

        return bean;
    }

    /* 汲取搜索控件的值 */
    @SuppressWarnings("unchecked")
    private Object extractSearchValue(Component c, boolean reset) {
        Object value = ((ValueFinder) c.getAttribute(COMP_VALUE_FINDER_ID))
            .getValue(c, reset);
        logger.debug("The value from component is [{}]. Component ID: [{}].",
            value, c.getId());

        return value;
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
     * 获得给定ZK组件的name属性值。
     * 有的ZK组件也许没有name属性，
     * 那么则忽略该属性，直接返回null
     *
     * @param comp ZK组件对象
     * @return name属性值
     */
    protected String getComponentName(Component comp) {
        try {
            return BeanUtils.getProperty(comp, "name");
        } catch (Exception e) {
            logger.debug(e.getMessage(), e);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private <C extends Component> ValueFinder<C> _createValueFinder(C c) {
        if (c instanceof NumberInputElement) {
            if (c instanceof Intbox) {
                return (ValueFinder<C>) new IntboxValueFinder();
            }

            if (c instanceof Longbox) {
                return (ValueFinder<C>) new LongboxValueFinder();
            }

            if (c instanceof Doublebox) {
                return (ValueFinder<C>) new DoubleboxValueFinder();
            }

            if (c instanceof Decimalbox) {
                return (ValueFinder<C>) new DecimalboxValueFinder();
            }

            if (c instanceof Spinner) {
                return (ValueFinder<C>) new SpinnerValueFinder();
            }

            if (c instanceof Doublespinner) {
                return (ValueFinder<C>) new DoublespinnerValueFinder();
            }
        }

        if (c instanceof FormatInputElement) {
            if (c instanceof Datebox) {
                return (ValueFinder<C>) new DateboxValueFinder();
            }

            if (c instanceof Timebox) {
                return (ValueFinder<C>) new TimeboxValueFinder();
            }
        }

        if (c instanceof InputElement) {
            if (c instanceof Combobox) {
                return (ValueFinder<C>) new ComboboxValueFinder();
            }

            if (c instanceof Bandbox) {
                return (ValueFinder<C>) new BandboxValueFinder();
            }

            if (c instanceof Textbox) {
                return (ValueFinder<C>) new TextboxValueFinder();
            }
        }

        if (c instanceof Radio) {
            return (ValueFinder<C>) new RadioValueFinder();
        }

        if (c instanceof Checkbox) {
            return (ValueFinder<C>) new CheckboxValueFinder();
        }

        if (c instanceof Radiogroup) {
            return (ValueFinder<C>) new RadiogroupValueFinder();
        }

        return createCustomValueFinder(c);
    }

    /**
     * 为给定的ZK组件添加搜索参数值的方法。
     * 如果组件的ID有值，那么表示该搜索条件是唯一值；
     * 如果组件的ID没值，但name有值，那么此方法认为
     * 搜索条件可能有多个值
     *
     * @param comp  ZK组件对象
     * @param reset 表示是否重设搜索条件值
     * @param bean  表单搜索对象Bean
     */
    @SuppressWarnings("unchecked")
    private void _addSearchArg(AbstractSearchFormBean bean, Component comp, boolean reset) {
        Object value = extractSearchValue(comp, reset);
        if (value == null) {
            logger.debug("No value was found from component. UUID: [{}].", comp.getUuid());

            return;
        }

        String id = comp.getId();
        if (isNotBlank(id)) {
            if (value instanceof String && isBlank((String) value)) return;

            bean.addSearchTerm(id, value);

            return;
        }

        String name = getComponentName(comp);

        if (isBlank(name)) {
            logger.warn("The component [uuid:{}] has no id and name. So ignore add arg into search scope.",
                comp.getUuid());

            return;
        }

        Set<Object> values;
        // 如果当前表单搜索对象中不包含该名字的搜索条件值，则新增一个Set对象
        if (!bean.hasSearchTerm(name)) {
            values = new HashSet<Object>();
            bean.addSearchTerm(name, values);
        } else values = (Set<Object>) bean.getSearchTerms().get(name);

        values.add(value);
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

    /**
     * ZK组件UUID值的比较器对象类
     */
    private class ComponentUuidPredicate implements Predicate {

        private String uuid;

        public ComponentUuidPredicate(String uuid) {
            this.uuid = uuid;
        }

        public boolean evaluate(Object o) {
            return uuid.equals(((Component) o).getUuid());
        }
    }
}

