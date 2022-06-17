package com.my.rpc.core.common.event.listener;

import com.my.rpc.core.client.ConnectionHandler;
import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.common.event.RpcListener;
import com.my.rpc.core.common.event.RpcUpdateEvent;
import com.my.rpc.core.common.event.data.URLChangeWrapper;
import com.my.rpc.core.common.utils.CommonUtils;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.my.rpc.core.common.cache.CommonClientCache.CONNECT_MAP;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/16 23:01
 * @Description 事件推送实现类
 **/
public class ServiceUpdateListener implements RpcListener<RpcUpdateEvent> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceUpdateListener.class);

    @Override
    public void callback(Object t) {
        // 获取字节点数据的信息
        URLChangeWrapper urlChangeWrapper = (URLChangeWrapper) t;
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(urlChangeWrapper.getServiceName());
        if (CommonUtils.isEmptyList(channelFutureWrapperList)) {
            return;
        }else {
            List<String> matchProviderUrl = urlChangeWrapper.getProviderUrl();
            Set<String> finalUrl = new HashSet<>();
            List<ChannelFutureWrapper> finalChannelFutureWrappers = new ArrayList<>();
            for (ChannelFutureWrapper channelFutureWrapper: channelFutureWrapperList) {
                String oldServerAddress = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
                // 如果旧的url不存在了, 则说明已经被移除了
                if (!matchProviderUrl.contains(oldServerAddress)) {
                    continue;
                }else {
                    finalChannelFutureWrappers.add(channelFutureWrapper);
                    finalUrl.add(oldServerAddress);
                }
            }
            // 将旧的url剔除后, 检测是否存在新的url
            List<ChannelFutureWrapper> newChannelFutureWrappers = new ArrayList<>();
            for (String newProviderUrl: matchProviderUrl) {
                if (!finalUrl.contains(newProviderUrl)) {
                    ChannelFutureWrapper channelFutureWrapper = new ChannelFutureWrapper();
                    String host = newProviderUrl.split(":")[0];
                    Integer port = Integer.valueOf(newProviderUrl.split(":")[1]);
                    channelFutureWrapper.setHost(host);
                    channelFutureWrapper.setPort(port);
                    try {
                        ChannelFuture channelFuture = ConnectionHandler.createChannelFuture(host, port);
                        channelFutureWrapper.setChannelFuture(channelFuture);
                        newChannelFutureWrappers.add(channelFutureWrapper);
                        finalUrl.add(newProviderUrl);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            finalChannelFutureWrappers.addAll(newChannelFutureWrappers);
            // 服务更新
            CONNECT_MAP.put(urlChangeWrapper.getServiceName(), finalChannelFutureWrappers);
        }
    }
}
