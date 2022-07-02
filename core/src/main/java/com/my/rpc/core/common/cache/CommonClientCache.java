package com.my.rpc.core.common.cache;

import com.my.rpc.core.common.ChannelFuturePollingRef;
import com.my.rpc.core.common.ChannelFutureWrapper;
import com.my.rpc.core.common.RpcInvocation;
import com.my.rpc.core.registry.URL;
import com.my.rpc.core.router.Router;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/12 11:09
 * @Description 客户端缓存
 **/
public class CommonClientCache {

    public static BlockingQueue<RpcInvocation> SEND_QUEUE = new ArrayBlockingQueue(10);

    public static Map<String, Object> RESP_MAP = new ConcurrentHashMap<>();

    /**
     * 服务名称列表
     */
    public static List<URL> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();

    /**
     * 每次远程调用从这里寻找服务提供者
     */
    public static Map<String, List<ChannelFutureWrapper>> CONNECT_MAP = new ConcurrentHashMap<>();

    public static Set<String> SERVER_ADDRESS = new HashSet<>();

    public static Map<String, List<URL>> URL_MAP = new ConcurrentHashMap<>();

    /**
     * 随机请求服务
     */
    public static Map<String, ChannelFutureWrapper[]> SERVICE_ROUTER_MAP = new HashMap<>();

    public static ChannelFuturePollingRef CHANNEL_FUTURE_POLLING_REF = new ChannelFuturePollingRef();

    public static Router ROUTER;
}
