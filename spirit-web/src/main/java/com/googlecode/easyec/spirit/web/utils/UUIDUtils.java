package com.googlecode.easyec.spirit.web.utils;

import com.fasterxml.uuid.Generators;

import java.util.UUID;

/**
 * UUID工具类
 *
 * @author JunJie
 */
public class UUIDUtils {

    /**
     * 产生随机的UUID值
     *
     * @return UUID值
     */
    public static String getRandomUUID() {
        return getRandomBasedUUID().toString();
    }

    /**
     * 根据字符串参数，产生一个基于参数的UUID值
     *
     * @param name 字符串参数
     * @return UUID值
     */
    public static String getNameBasedUUID(String name) {
        return Generators.nameBasedGenerator(getRandomBasedUUID()).generate(name).toString();
    }

    /**
     * 产生一个随机的UUID对象
     *
     * @return <code>UUID</code>实例
     */
    private static UUID getRandomBasedUUID() {
        return Generators.randomBasedGenerator().generate();
    }
}
