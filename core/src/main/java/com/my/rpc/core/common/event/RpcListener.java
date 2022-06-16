package com.my.rpc.core.common.event;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/16 17:42
 * @Description 监听接口
 **/
public interface RpcListener<T> {

    void callback(Object t);

}
