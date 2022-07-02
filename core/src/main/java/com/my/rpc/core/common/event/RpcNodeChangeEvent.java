package com.my.rpc.core.common.event;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/29 21:24
 * @Description Rpc节点变更事件
 **/
public class RpcNodeChangeEvent {

    private Object data;

    public RpcNodeChangeEvent(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
