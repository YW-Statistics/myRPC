package com.my.rpc.core.serialize;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/7/17 10:46
 * @Description 序列化工厂
 **/
public interface SerializeFactory {

    /**
     * 序列化
     */
    <T> byte[] serialize(T t);

    /**
     * 反序列化
     */
    <T> T deserialize(byte[] data, Class<T> clazz);

}
