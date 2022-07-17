package com.my.rpc.core.serialize.fastjson;

import com.alibaba.fastjson.JSON;
import com.my.rpc.core.serialize.SerializeFactory;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/7/17 17:59
 * @Description fastjson序列化
 **/
public class FastJsonSerializeFactory implements SerializeFactory {
    @Override
    public <T> byte[] serialize(T t) {
        String jsonStr = JSON.toJSONString(t);
        return jsonStr.getBytes();
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(new String(data), clazz);
    }
}
