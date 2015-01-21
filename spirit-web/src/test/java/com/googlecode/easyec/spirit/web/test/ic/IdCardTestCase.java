package com.googlecode.easyec.spirit.web.test.ic;

import com.googlecode.easyec.spirit.web.ic.IdCard;
import com.googlecode.easyec.spirit.web.ic.InvalidIdException;
import com.googlecode.easyec.spirit.web.ic.impl.GenericIdCardCheck;
import org.junit.Test;

/**
 * ID card测试类
 */
public class IdCardTestCase {

    @Test
    public void checkChinese18() throws InvalidIdException {
        String id = "310113198210081934";
        GenericIdCardCheck.China18.validate(id, IdCard.IdType.Male);
    }

    @Test
    public void checkChinese15() throws InvalidIdException {
        String id = "310113821008193";
        GenericIdCardCheck.China15.validate(id, IdCard.IdType.Male);
    }
}
