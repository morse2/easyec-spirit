package com.googlecode.easyec.spirit.mybatis.cache.serializer;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.KryoException;
import com.esotericsoftware.kryo.Registration;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import org.apache.ibatis.executor.loader.WriteReplaceInterface;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ObjectStreamException;

/**
 * 基于{@link WriteReplaceInterface}接口，
 * 序列化和反序列化对象数据的序列化器类。
 *
 * @author JunJie
 */
public class WriteReplaceInterfaceSerializer extends Serializer {

    private static final Logger logger = LoggerFactory.getLogger(WriteReplaceInterfaceSerializer.class);

    @Override
    public void write(Kryo kryo, Output output, Object object) {
        object.toString(); // load all of properties

        try {
            Object o = ((WriteReplaceInterface) object).writeReplace();

            kryo.writeClass(output, o.getClass());
            kryo.writeObject(output, o);
        } catch (ObjectStreamException e) {
            logger.error(e.getMessage(), e);

            throw new KryoException(e);
        }
    }

    @Override
    public Object read(Kryo kryo, Input input, Class type) {
        Registration reg = kryo.readClass(input);
        logger.debug(
                "Class type: [{}], Serializer: [{}]",
                reg.getType().getName(),
                reg.getSerializer().getClass().getName()
        );

        return kryo.readObject(input, reg.getType(), reg.getSerializer());
    }
}
