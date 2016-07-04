package com.googlecode.easyec.spirit.web.webservice;

import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.addAll;
import static java.util.Collections.unmodifiableList;
import static org.apache.commons.lang3.ArrayUtils.isEmpty;

/**
 * SOAP的消息头类。
 *
 * @author JunJie
 */
@XmlRootElement
public abstract class Header {

    @XmlElementRef
    private List<HeadContent> headContents = new ArrayList<HeadContent>();

    /**
     * 默认构造方法
     */
    protected Header() {
        // no op
    }

    /**
     * 构造方法
     *
     * @param headContents 一组消息头内容对象
     */
    protected Header(HeadContent[] headContents) {
        if (!isEmpty(headContents)) {
            addAll(this.headContents, headContents);
        }
    }

    /**
     * 返回不可变的消息头内容对象列表
     *
     * @return <code>HeadContent</code>对象列表
     */
    public List<HeadContent> getHeadContents() {
        return unmodifiableList(headContents);
    }

    /**
     * 向当前列表中添加一个消息头内容对象
     *
     * @param headContent 消息头内容对象
     * @return 添加成功则返回真
     */
    public boolean addHeadContent(HeadContent headContent) {
        return null != headContent && headContents.add(headContent);
    }

    /**
     * 删除给定索引号的消息头对象
     *
     * @param index 消息头内容列表的索引号
     * @return 删除成功返回真
     */
    public boolean removeHeadContent(int index) {
        return
            index > -1
                && index < headContents.size()
                && null != headContents.remove(index);
    }

    /**
     * 判断有无消息头内容
     *
     * @return 有消息头内容返回真
     */
    public boolean hasHeadContent() {
        return !this.headContents.isEmpty();
    }

    /**
     * 查找给定类型的消息头内容对象
     *
     * @param cls 指定消息头内容类型
     * @param <T> 泛型类型
     * @return 符合条件的消息头内容对象列表
     */
    public <T extends HeadContent> List<T> find(Class<T> cls) {
        List<T> result = new ArrayList<T>();

        if (null != cls) {
            for (HeadContent headContent : headContents) {
                if (cls.isInstance(headContent)) {
                    result.add(cls.cast(headContent));
                }
            }
        }

        return result;
    }

    /**
     * 查找给定类型的消息头内容对象。
     * 此方法返回第一个匹配条件的对象。
     *
     * @param cls 指定消息头内容类型
     * @param <T> 泛型类型
     * @return 符合条件的消息头内容对象
     */
    public <T extends HeadContent> T findFirst(Class<T> cls) {
        if (null != cls) {
            for (HeadContent headContent : headContents) {
                if (cls.isInstance(headContent)) {
                    return cls.cast(headContent);
                }
            }
        }

        return null;
    }
}
