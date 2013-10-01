package com.googlecode.easyec.spirit.web.test.httpcomponent.soap;

import com.googlecode.easyec.spirit.web.webservice.BodyContent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-16
 * Time: 下午1:53
 * To change this template use File | Settings | File Templates.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://easyec.googlecode.com/soap/", name = "User")
public class User extends BodyContent {

    @XmlElement(name = "Name", namespace = "http://easyec.googlecode.com/soap/")
    private String name;
    private Integer age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

//    @XmlElement(name = "Age", namespace = "http://easyec.googlecode.com/soap/")
    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
