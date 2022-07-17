package com.my.rpc.core.serialize.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.my.rpc.core.serialize.SerializeFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/7/17 11:55
 * @Description Kryo序列化
 **/
public class KryoSerializeFactory implements SerializeFactory {

    private static final ThreadLocal<Kryo> kryoContext = new ThreadLocal<Kryo>() {
        @Override
        protected Kryo initialValue() {
            Kryo kryo = new Kryo();
            return kryo;
        }
    };

    @Override
    public <T> byte[] serialize(T t) {
        Output output = null;
        try {
            Kryo kryo = kryoContext.get();
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            output = new Output(byteArrayOutputStream);
            kryo.writeClassAndObject(output, t);
            return output.toBytes();
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (null != output) {
                output.close();
            }
        }

    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        Input input = null;
        try {
            Kryo kryo = kryoContext.get();
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(data);
            input = new Input(byteArrayInputStream);
            return (T) kryo.readClassAndObject(input);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        } finally {
            if (null != input) {
                input.close();
            }
        }
    }
}
