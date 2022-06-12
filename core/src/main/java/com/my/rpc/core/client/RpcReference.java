package com.my.rpc.core.client;


import com.my.rpc.core.proxy.ProxyFactory;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 18:05
 * @Description Rpc代理类
 **/
public class RpcReference {

    public ProxyFactory proxyFactory;

    public RpcReference(ProxyFactory proxyFactory) {
        this.proxyFactory = proxyFactory;
    }

    public <T> T get(Class<T> clazz) throws Throwable{
        return (T) proxyFactory.getProxy(clazz);
    }

}
