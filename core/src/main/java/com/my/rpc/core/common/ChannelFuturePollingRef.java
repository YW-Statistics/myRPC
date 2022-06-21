package com.my.rpc.core.common;

import java.util.concurrent.atomic.AtomicLong;

import static com.my.rpc.core.common.cache.CommonClientCache.SERVICE_ROUTER_MAP;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/21 23:18
 * @Description 路由选取
 **/
public class ChannelFuturePollingRef {

    private AtomicLong referenceTimes = new AtomicLong();

    public ChannelFutureWrapper getChannelFutureWrapper(String serviceName) {
        ChannelFutureWrapper[] channelFutureWrapperList = SERVICE_ROUTER_MAP.get(serviceName);
        long i = referenceTimes.getAndIncrement();
        int index = (int) (i % channelFutureWrapperList.length);
        return channelFutureWrapperList[index];
    }

}
