package com.my.rpc.core.common.event.listener;

import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.common.event.RpcListener;
import com.my.rpc.core.common.event.RpcNodeChangeEvent;
import com.my.rpc.core.registry.URL;
import com.my.rpc.core.registry.zookeeper.ProviderNodeInfo;

import java.util.List;

import static com.my.rpc.core.common.cache.CommonClientCache.CONNECT_MAP;
import static com.my.rpc.core.common.cache.CommonClientCache.ROUTER;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/29 21:22
 * @Description 提供者节点数据监听器
 **/
public class ProviderNodeDataChangeListener implements RpcListener<RpcNodeChangeEvent> {
    @Override
    public void callback(Object t) {
        ProviderNodeInfo providerNodeInfo = (ProviderNodeInfo) t;
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(providerNodeInfo.getServiceName());
        for (ChannelFutureWrapper channelFutureWrapper: channelFutureWrapperList) {
            String address = channelFutureWrapper.getHost() + ":" + channelFutureWrapper.getPort();
            if (address.equals(providerNodeInfo.getAddress())) {
                // 修改权重
                channelFutureWrapper.setWeight(providerNodeInfo.getWeight());
                URL url = new URL();
                url.setServiceName(providerNodeInfo.getServiceName());
                // 更新权重
                ROUTER.updateWeight(url);
                break;
            }
        }
    }
}
