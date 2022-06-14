package com.my.rpc.core.common.cache;

import com.my.rpc.core.common.RpcInvocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    public static List<String> SUBSCRIBE_SERVICE_LIST = new ArrayList<>();
}
