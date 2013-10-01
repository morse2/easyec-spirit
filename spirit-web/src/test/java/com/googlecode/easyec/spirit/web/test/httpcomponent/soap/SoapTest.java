package com.googlecode.easyec.spirit.web.test.httpcomponent.soap;

import com.googlecode.easyec.spirit.web.webservice.soap.FaultCodes;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12Body;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12Fault;
import com.googlecode.easyec.spirit.web.webservice.soap.v12.Soap12Header;
import org.junit.Test;

import javax.xml.bind.JAXBContext;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringWriter;

/**
 * SOAP测试类。
 *
 * @author JunJie
 */
public class SoapTest {

    @Test
    public void marshalUser() throws Exception {
        User user = new User();
        user.setName("JunJie");
        user.setAge(30);

        StringWriter w = new StringWriter();

        Soap12Fault fault = new Soap12Fault();
        fault.setFaultCode(FaultCodes.CLIENT);
        fault.setFaultString("test wrong.");

        Soap12 soap12 = new Soap12(new Soap12Header(null), null);
        JAXBContext ctx = JAXBContext.newInstance(
            Soap12.class,
            Soap12Header.class,
            Soap12Body.class,
            Soap12Fault.class,
            User.class
        );

        ctx.createMarshaller().marshal(soap12, w);

        String s = w.toString();
        System.out.println(s);
        InputStream bis = new ByteArrayInputStream(s.getBytes());

        Object o = ctx.createUnmarshaller().unmarshal(bis);

        System.out.println(o);
    }
}
