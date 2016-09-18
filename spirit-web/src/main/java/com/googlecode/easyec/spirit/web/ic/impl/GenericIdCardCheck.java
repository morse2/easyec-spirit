package com.googlecode.easyec.spirit.web.ic.impl;

import com.googlecode.easyec.spirit.web.ic.IdCard;

/**
 * 通用的ID身份标识校验实现类。
 * 该类为抽象类，但通过该类
 * 可以获取到通用情况下的ID
 * 身份标识的实现对象
 *
 * @author JunJie
 */
public abstract class GenericIdCardCheck {

    /**
     * 返回中华人民共和国18位身份证标准校验对象
     */
    public static IdCard China18 = new China18IdCardCheck();

    /**
     * 返回中华人民共和国15位身份证标准校验对象
     *
     * @deprecated 请使用China18算法进行校验
     */
    @Deprecated
    public static IdCard China15 = new China15IdCardCheck();
}
