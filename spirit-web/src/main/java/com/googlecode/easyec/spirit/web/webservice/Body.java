package com.googlecode.easyec.spirit.web.webservice;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * 信封的消息体类。
 *
 * @author JunJie
 */
@XmlRootElement
public abstract class Body {

    @XmlElementRef
    private List<BodyContent> bodyContents = new ArrayList<BodyContent>();

    /**
     * 默认构造方法
     */
    protected Body() {}

    /**
     * 构造方法
     *
     * @param bodyContents 一组消息体内容对象列表
     */
    protected Body(BodyContent[] bodyContents) {
        if (!isEmpty(bodyContents)) {
            addAll(this.bodyContents, bodyContents);
        }
    }

    /**
     * 返回消息体内容对象列表
     *
     * @return <code>BodyContent</code>对象列表
     */
    public List<BodyContent> getBodyContents() {
        return unmodifiableList(bodyContents);
    }

    /**
     * 向当前列表中添加一个消息体内容对象
     *
     * @param bodyContent 消息体内容对象
     * @return 添加成功则返回真
     */
    public boolean addBodyContent(BodyContent bodyContent) {
        return null != bodyContent && bodyContents.add(bodyContent);
    }

    /**
     * 删除给定索引号的消息头对象
     *
     * @param index 消息体内容列表的索引号
     * @return 删除成功返回真
     */
    public boolean removeBodyContent(int index) {
        return
            index > -1
                && index < bodyContents.size()
                && null != bodyContents.remove(index);
    }

    /**
     * 判断有无消息体内容
     *
     * @return 有消息体内容返回真
     */
    public boolean hasBodyContent() {
        return !this.bodyContents.isEmpty();
    }

    /**
     * 查找给定类型的消息体内容对象
     *
     * @param cls 指定消息体内容类型
     * @param <T> 泛型类型
     * @return 符合条件的消息体内容对象列表
     */
    public <T extends BodyContent> List<T> find(Class<T> cls) {
        List<T> result = new ArrayList<T>();

        if (null != cls) {
            for (BodyContent bodyContent : bodyContents) {
                if (cls.isInstance(bodyContent)) {
                    result.add(cls.cast(bodyContent));
                }
            }
        }

        return result;
    }

    /**
     * 查找给定类型的消息体内容对象。
     * 此方法返回第一个匹配条件的对象。
     *
     * @param cls 指定消息体内容类型
     * @param <T> 泛型类型
     * @return 符合条件的消息体内容对象
     */
    public <T extends BodyContent> T findFirst(Class<T> cls) {
        if (null != cls) {
            for (BodyContent bodyContent : bodyContents) {
                if (cls.isInstance(bodyContent)) {
                    return cls.cast(bodyContent);
                }
            }
        }

        return null;
    }
}
