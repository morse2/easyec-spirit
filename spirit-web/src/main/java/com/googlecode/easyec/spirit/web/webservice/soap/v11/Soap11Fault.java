package com.googlecode.easyec.spirit.web.webservice.soap.v11;

import com.googlecode.easyec.spirit.web.webservice.Fault;
import com.googlecode.easyec.spirit.web.webservice.soap.FaultCodes;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 支持SOAP协议的错误体消息类
 *
 * @author JunJie
 */
@XmlRootElement(name = "Fault")
public class Soap11Fault extends Fault {

    @XmlElement(name = "faultcode")
    private FaultCodes faultCode;
    @XmlElement(name = "faultstring")
    private String     faultString;
    @XmlElement(name = "faultactor")
    private String     faultActor;
    @XmlElement(name = "detail")
    private String     detail;

    /**
     * 返回供识别故障的代码
     *
     * @return 故障代码枚举
     */
    public FaultCodes getFaultCode() {
        return faultCode;
    }

    /**
     * 设置供识别故障的代码
     *
     * @param faultCode 故障代码枚举
     */
    public void setFaultCode(FaultCodes faultCode) {
        this.faultCode = faultCode;
    }

    /**
     * 返回可供人阅读的有关故障的说明
     *
     * @return 故障说明
     */
    public String getFaultString() {
        return faultString;
    }

    /**
     * 设置可供人阅读的有关故障的说明
     *
     * @param faultString 故障说明
     */
    public void setFaultString(String faultString) {
        this.faultString = faultString;
    }

    /**
     * 返回有关是谁引发故障的信息
     *
     * @return 故障制造者
     */
    public String getFaultActor() {
        return faultActor;
    }

    /**
     * 设置有关是谁引发故障的信息
     *
     * @param faultActor 故障制造者
     */
    public void setFaultActor(String faultActor) {
        this.faultActor = faultActor;
    }

    /**
     * 返回存留涉及 Body 元素的应用程序专用错误信息
     *
     * @return 错误细节
     */
    public String getDetail() {
        return detail;
    }

    /**
     * 设置存留涉及 Body 元素的应用程序专用错误信息
     *
     * @param detail 错误细节
     */
    public void setDetail(String detail) {
        this.detail = detail;
    }
}
