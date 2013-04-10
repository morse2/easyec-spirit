package com.googlecode.easyec.spirit.web.soap.factory;

import com.googlecode.easyec.spirit.web.soap.Body;
import com.googlecode.easyec.spirit.web.soap.Fault;
import com.googlecode.easyec.spirit.web.soap.Header;
import com.googlecode.easyec.spirit.web.soap.Soap;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Namespace;
import org.dom4j.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.apache.commons.collections.CollectionUtils.isEmpty;

/**
 * 抽象的SOAP工厂封装实现类。
 *
 * @author JunJie
 */
public abstract class AbstractSoapFactory implements SoapFactory {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public String asXml(Soap soap) {
        if (null == soap) return null;

        Namespace namespace = soap.getNamespace();
        logger.debug("SOAP's namespace is: [" + namespace + "].");

        Element envelope = DocumentHelper.createElement(new QName("Envelope", namespace));
        // 添加默认的名称空间
        envelope.addNamespace("xsd", "http://www.w3.org/2001/XMLSchema");
        envelope.addNamespace("xsi", "http://www.w3.org/2001/XMLSchema-instance");

        try {
            // 解析SOAP消息头
            Header header = soap.getHeader();
            if (null != header && null != header.getContent()) {
                doMarshal(header.getContent(), envelope, new QName("Header", namespace));
            }

            // 解析SOAP消息体
            Body body = soap.getBody();
            if (null != body && null != body.getContent()) {
                doMarshal(body.getContent(), envelope, new QName("Body", namespace));
            }

            // 解析SOAP错误体
            Fault fault = soap.getFault();
            if (null != fault && null != fault.getContent()) {
                doMarshal(fault.getContent(), envelope, new QName("Fault", namespace));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return DocumentHelper.createDocument(envelope).asXML();
    }

    /**
     * 实现对对象编组的功能。
     *
     * @param o        被编组的对象实例
     * @param envelope 根节点元素对象
     * @param name     被编组的父节点元素名
     * @throws Exception
     */
    private void doMarshal(Object o, Element envelope, QName name) throws Exception {
        String s = marshal(o);
        logger.debug(name.getName() + "'s content: [" + s + "].");

        if (StringUtils.isNotBlank(s)) {
            Element element = DocumentHelper.createElement(name);
            element.add(DocumentHelper.parseText(s).getRootElement());
            envelope.add(element);
        }
    }

    public Soap asSoap(String xml) {
        if (StringUtils.isBlank(xml)) return null;

        try {
            Element root = DocumentHelper.parseText(xml).getRootElement();
            // 获取SOAP定义的命名空间
            Namespace namespace = root.getNamespace();
            if (logger.isDebugEnabled()) {
                logger.debug("Xml of namespace: [" + namespace.asXML() + "].");
            }

            // 解析SOAP消息头
            Object header = doUnmarshal(root.element(new QName("Header", namespace)));
            // 解析SOAP消息体
            Object body = doUnmarshal(root.element(new QName("Body", namespace)));
            // 解析SOAP错误体
            Object fault = doUnmarshal(root.element(new QName("Fault", namespace)));

            // 初始化默认的SOAP对象实例
            return new DefaultSoap(header, body, fault, namespace);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    /**
     * 将DOM元素对象转换成SOAP对象。
     *
     * @param element DOM元素对象
     * @return SOAP对象实例
     * @throws Exception
     */
    private Object doUnmarshal(Element element) throws Exception {
        if (null == element) return null;

        List children = element.elements();
        if (isEmpty(children)) return null;

        String xml = ((Element) children.get(0)).asXML();
        logger.debug("Xml of element: [" + xml + "].");

        return unmarshal(xml);
    }

    /**
     * 实现对对象编组的功能。
     *
     * @param o 被编组的对象实例
     * @throws Exception
     */
    abstract protected String marshal(Object o) throws Exception;

    /**
     * 将XML格式的字符串内容转换成Java类对象。
     *
     * @param xml XML内容
     * @return Java对象实例
     * @throws Exception
     */
    protected abstract Object unmarshal(String xml) throws Exception;
}
