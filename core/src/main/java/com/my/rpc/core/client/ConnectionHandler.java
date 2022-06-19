package com.my.rpc.core.client;

import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.common.utils.CommonUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static com.my.rpc.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.my.rpc.core.common.cache.CommonClientCache.SERVER_ADDRESS;

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

    public static void setBootstrap(Bootstrap bootstrap) {
        ConnectionHandler.bootstrap = bootstrap;
    }

    /**
     * 构建单个连接通道 元操作, 既要处理连接, 还要统一将连接进行内存存储管理
     */
    public static void connect(String providerServiceName, String providerIp) throws InterruptedException {
        if (null == bootstrap) {
            throw new RuntimeException("bootstrap can not be null!");
        }
        // 过滤格式错误的信息
        if (!providerIp.contains(":")) {
            return;
        }
        String[] providerAddress = providerIp.split(":");
        String host = providerAddress[0];
        int port = Integer.parseInt(providerAddress[1]);
        ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
        ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
        channelFutureWrapper.setHost(host);
        channelFutureWrapper.setPort(port);
        channelFutureWrapper.setChannelFuture(channelFuture);
        SERVER_ADDRESS.add(providerIp);
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(providerServiceName);
        if (!CommonUtils.isEmptyList(channelFutureWrapperList)) {
            channelFutureWrapperList = new ArrayList<>();
        }
        channelFutureWrapperList.add(channelFutureWrapper);
        CONNECT_MAP.put(providerServiceName, channelFutureWrapperList);
    }

    /**
     * 创建ChannelFuture
     */
    public static ChannelFuture createChannelFuture(String host, Integer port) throws InterruptedException {
        return bootstrap.connect(host, port).sync();
    }

    /**
     * 断开连接
     */
    public static void disconnect(String providerServiceName, String providerIp) {
        SERVER_ADDRESS.remove(providerIp);
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(providerServiceName);
        if (!CommonUtils.isEmptyList(channelFutureWrapperList)) {
            channelFutureWrapperList.removeIf(channelFutureWrapper -> providerIp.equals(channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort()));
        }
    }

    /**
     * 获取ChannelFuture
     */
    public static ChannelFuture getChannelFuture(String providerServiceName) {
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(providerServiceName);
        if (CommonUtils.isEmptyList(channelFutureWrapperList)) {
            throw new RuntimeException("no provider for " + providerServiceName);
        }
        return channelFutureWrapperList.get(new Random().nextInt(channelFutureWrapperList.size())).getChannelFuture();
    }
}
