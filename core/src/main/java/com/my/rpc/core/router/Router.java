package com.my.rpc.core.router;

import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.registry.URL;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/21 11:26
 * @Description 路由接口
 **/
public interface Router {

    /**
     * 刷新路由数组
     */
    void refreshRouterArr(Selector selector);

    /**
     * 获取请求到连接通道
     */
    ChannelFutureWrapper select(Selector selector);

    /**
     * 更新权重信息
     */
    void updateWeight(URL url);
}
