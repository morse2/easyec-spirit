package com.googlecode.easyec.spirit.web.test.httpcomponent.soap;

import com.googlecode.easyec.spirit.web.soap.Soap;
import com.googlecode.easyec.spirit.web.soap.factory.JaxbContextSoapFactory;
import com.googlecode.easyec.spirit.web.soap.factory.SoapFactory;
import com.googlecode.easyec.spirit.web.soap.impl.Soap11;
import org.junit.Test;

import javax.xml.bind.JAXBException;

/**
 * SOAP测试类。
 *
 * @author JunJie
 */
public class SoapTest {

    @Test
    public void marshalUser() throws JAXBException {
        User user = new User();
        user.setName("JunJie");
        user.setAge(30);

        Soap11 soap11 = new Soap11(user);

        SoapFactory sf = new JaxbContextSoapFactory(new Class[] { User.class });
        String s = sf.asXml(soap11);
        System.out.println(s);

        Soap soap = sf.asSoap(s);
        System.out.println(soap);
    }
}
