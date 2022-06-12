package com.my.rpc.core.proxy.jdk;

import com.my.rpc.core.proxy.ProxyFactory;

import java.lang.reflect.Proxy;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/12 11:35
 * @Description JDK代理工厂
 **/
public class JDKProxyFactory implements ProxyFactory {


    @Override
    public <T> T getProxy(Class clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[]{clazz},
                new JDKClientInvocationHandler(clazz));
    }
}
