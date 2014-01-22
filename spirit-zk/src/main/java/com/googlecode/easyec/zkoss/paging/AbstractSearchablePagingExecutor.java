package com.googlecode.easyec.zkoss.paging;

import com.googlecode.easyec.spirit.web.controller.formbean.impl.AbstractSearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.impl.SearchFormBean;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.SearchTermsTransform;
import com.googlecode.easyec.spirit.web.controller.formbean.terms.impl.FuzzyMatchTermsTransform;
import org.apache.commons.collections.MapUtils;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.event.KeyEvent;
import org.zkoss.zk.ui.event.SerializableEventListener;
import org.zkoss.zul.*;
import org.zkoss.zul.impl.FormatInputElement;
import org.zkoss.zul.impl.InputElement;
import org.zkoss.zul.impl.NumberInputElement;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.googlecode.easyec.zkoss.utils.SelectorUtils.find;
import static org.apache.commons.lang.StringUtils.isNotBlank;
import static org.zkoss.zk.ui.event.Events.ON_OK;

/**
 * 抽象的分页搜索操作执行器类。
 * 此类提供了基本的分页搜索的方法。
 *
 * @author JunJie
 */
public abstract class AbstractSearchablePagingExecutor<T extends Component> extends AbstractPagingExecutor<T> implements SearchablePagingExecutor {

    private static final long serialVersionUID = -6240850960089282666L;

    /**
     * 构造方法。
     *
     * @param paging 分页组件对象
     * @param comp   呈现分页结果组件对象
     */
    protected AbstractSearchablePagingExecutor(Paging paging, T comp) {
        super(paging, comp);
    }

    private static final String SELECTORS = "textbox,combobox,datebox,intbox,decimalbox,div";

    private final List<Component> searchComponents = new ArrayList<Component>();
    private Component searchScope;

    public void setSearchScope(Component searchScope) {
        this.searchScope = searchScope;
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
            this.searchComponents.addAll(find(getActualSearchScope(), searchSelectors));
        }
    }

    @Override
    public void doInit() {
        // fix at 0.2.8
        // 先初始化搜索组件
        addSearchSelectors(SELECTORS);

        // 默认为Input控件添加OnOK事件，提高搜索效率
        for (Component comp : searchComponents) {
            if (comp instanceof InputElement) {
                comp.addEventListener(ON_OK, new KeyPressOKEventListener());
            }
        }

        super.doInit();
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

    public Map<String, Object> getSearchTerms() {
        return combineSearchTerms().getSearchTerms();
    }

    /**
     * 合并搜索参数。
     *
     * @return <code>AbstractSearchFormBean</code>
     */
    protected AbstractSearchFormBean combineSearchTerms() {
        AbstractSearchFormBean bean = createSearchFormBean();

        for (Component c : searchComponents) {
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

            // 读取合并ZK控件中的属性值
            combineComponentAttributes(c, bean);
        }

        return bean;
    }

    protected List<SearchTermsTransform> createSearchTermsTransforms() {
        List<SearchTermsTransform> transforms = new ArrayList<SearchTermsTransform>();
        transforms.add(new FuzzyMatchTermsTransform());

        return transforms;
    }

    /**
     * 合并添加组件中的自定义属性作为搜索条件。
     *
     * @param c    ZK组件实例
     * @param bean <code>AbstractSearchFormBean</code>
     */
    private void combineComponentAttributes(Component c, AbstractSearchFormBean bean) {
        Map<String, Object> attributes = c.getAttributes();
        if (MapUtils.isNotEmpty(attributes)) {
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
                    bean.addSearchTerm(id, ((String) value).replaceAll("\\*", "%"));
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
        return new SearchFormBean(createSearchTermsTransforms());
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
}
