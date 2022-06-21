package com.my.rpc.core.router;

import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.registry.URL;

import java.util.List;

import static com.my.rpc.core.common.cache.CommonClientCache.*;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/21 22:35
 * @Description 随机化路由实现类
 **/
public class RandomRouterImpl implements Router{
    @Override
    public void refreshRouterArr(Selector selector) {
        // 获取服务提供者的数目
        List<ChannelFutureWrapper> channelFutureWrapperList = CONNECT_MAP.get(selector.getProviderServiceName());
        ChannelFutureWrapper[] channelFutureWrapperArr = new ChannelFutureWrapper[channelFutureWrapperList.size()];
        // 提前生成调用先后顺序的数组
        int[] res = createRandomIndex(channelFutureWrapperArr.length);
        // 按照随机数组生成调用顺序
        for (int i = 0;i < res.length;i++) {
            channelFutureWrapperArr[i] = channelFutureWrapperList.get(res[i]);
        }
        SERVICE_ROUTER_MAP.put(selector.getProviderServiceName(), channelFutureWrapperArr);
    }

    private int[] createRandomIndex(int length) {
        return new int[2];
    }

    @Override
    public ChannelFutureWrapper select(Selector selector) {
        return CHANNEL_FUTURE_POLLING_REF.getChannelFutureWrapper(selector.getProviderServiceName());
    }

    @Override
    public void updateWeight(URL url) {

    }
}
