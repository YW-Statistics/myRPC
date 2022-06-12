package com.my.rpc.core.proxy;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 22:56
 * @Description 代理工厂
 **/
public interface ProxyFactory {

    <T> Object getProxy(final Class clazz);

}
