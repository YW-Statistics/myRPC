package com.my.rpc.core.common.event;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/16 17:40
 * @Description 节点更新事件
 **/
public class RpcUpdateEvent implements RpcEvent {

    private Object data;

    public RpcUpdateEvent(Object data) {
        this.data = data;
    }

    @Override
    public Object getData() {
        return data;
    }

    @Override
    public RpcEvent setDat(Object data) {
        this.data = data;
        return this;
    }
}
