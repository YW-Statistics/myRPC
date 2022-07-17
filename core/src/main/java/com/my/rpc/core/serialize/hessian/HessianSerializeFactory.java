package com.my.rpc.core.serialize.hessian;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.my.rpc.core.serialize.SerializeFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/7/17 11:31
 * @Description Hessian序列化
 **/
public class HessianSerializeFactory implements SerializeFactory {


    @Override
    public <T> byte[] serialize(T t) {
        byte[] data;
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Hessian2Output output = new Hessian2Output(os);
            output.writeObject(t);
            output.getBytesOutputStream().flush();
            output.completeMessage();
            output.close();
            data = os.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        if (null == data) {
            return null;
        }
        Object result = null;
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            Hessian2Input input = new Hessian2Input(is);
            result = input.readObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return (T) result;
    }
}
