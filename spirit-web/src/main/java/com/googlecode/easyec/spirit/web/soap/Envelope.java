package com.googlecode.easyec.spirit.web.soap;

/**
 * SOAP消息类。
 *
 * @author JunJie
 */
public interface Envelope {

    /**
     * 获取SOAP消息头
     *
     * @return {@link Header}
     */
    Header getHeader();

    /**
     * 获取SOAP消息体
     *
     * @return {@link Body}
     */
    Body getBody();

    /**
     * 获取消息错误体
     *
     * @return {@link Fault}
     */
    Fault getFault();
}
