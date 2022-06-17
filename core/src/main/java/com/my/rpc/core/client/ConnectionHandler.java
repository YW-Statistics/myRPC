package com.my.rpc.core.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/17 17:34
 * @Description 当注册中心的节点新增或者移除或者权重变化的时候, 这个类主要负责对内存中的url做变更
 **/
public class ConnectionHandler {

    /**
     * 负责连接通信
     * 专门用于负责和服务端构建连接通信
     */
    private static Bootstrap bootstrap;

    public static ChannelFuture createChannelFuture(String host, Integer port) {
        return null;
    }

}
