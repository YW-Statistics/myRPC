package com.my.rpc.core.serialize.jdk;

import com.my.rpc.core.serialize.SerializeFactory;

import java.io.*;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/7/17 10:50
 * @Description JDK序列化
 **/
public class JdkSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) {
        byte[] data;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream output = new ObjectOutputStream(os);
            output.writeObject(t);
            output.flush();
            output.close();
            data = os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        ByteArrayInputStream is = new ByteArrayInputStream(data);
        try {
            ObjectInputStream input = new ObjectInputStream(is);
            Object result = input.readObject();
            return (T) result;
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
