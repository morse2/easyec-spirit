package com.googlecode.easyec.spirit.web.ic;

/**
 * 身份标识卡接口类
 *
 * @author JunJie
 */
public interface IdCard {

    public static enum IdType {

        /**
         * 标识性别为男
         */
        Male,
        /**
         * 标识性别为女
         */
        Female
    }

    /**
     * 验证给定的ID身份标识是否合法的方法。
     * 如果ID身份标识不合法，则抛出
     * <code>InvalidIdException</code>
     * 异常信息。如果方法没有任何错抛出，
     * 则表示ID身份标识是合法的
     *
     * @param id   ID身份标识
     * @param type ID身份标识类型
     * @throws InvalidIdException
     */
    void validate(String id, IdType type) throws InvalidIdException;
}
