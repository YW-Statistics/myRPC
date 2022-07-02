package com.my.rpc.core.router;

import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.registry.URL;

import java.util.List;

import static com.my.rpc.core.common.cache.CommonClientCache.*;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/7/2 19:36
 * @Description 轮询调用
 **/
public class RotateRouterImpl implements Router {
    @Override
    public void refreshRouterArr(Selector selector) {
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] arr = new ChannelFutureWrapper[channelFutureWrapperList.size()];
        for (int i = 0;i < channelFutureWrapperList.size();i++) {
            arr[i] = channelFutureWrapperList.get(i);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), arr);
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getProviderServiceName());
    }

    @Override
    public void updateWeight(URL url) {

    }
}
