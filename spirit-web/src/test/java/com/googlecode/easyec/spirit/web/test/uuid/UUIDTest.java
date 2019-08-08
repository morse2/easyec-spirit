package com.googlecode.easyec.spirit.web.test.uuid;

import com.fasterxml.uuid.Generators;
import com.googlecode.easyec.spirit.web.utils.ShortUuid;
import com.googlecode.easyec.spirit.web.utils.ShortUuidBuilder;
import org.junit.Test;

import java.util.UUID;

public class UUIDTest {

    @Test
    public void shortUUID() {
        UUID generate = Generators.nameBasedGenerator().generate("{'name':'test'}");

        ShortUuidBuilder builder = new ShortUuidBuilder();
        ShortUuid build = builder.build(generate);
        System.out.println(build);
        System.out.println(generate);
        System.out.println(UUID.fromString(builder.decode(build.toString())));
    }
}
