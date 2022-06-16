package com.my.rpc.core.common.event;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/16 17:37
 * @Description 事件接口
 **/
public interface RpcEvent {

    Object getData();

    RpcEvent setDat(Object data);

}
