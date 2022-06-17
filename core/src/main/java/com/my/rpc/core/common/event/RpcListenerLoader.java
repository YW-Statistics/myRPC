package com.my.rpc.core.common.event;

import com.my.rpc.core.common.event.listener.ServiceUpdateListener;
import com.my.rpc.core.common.utils.CommonUtils;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/16 22:32
 * @Description 事件发送
 **/
public class RpcListenerLoader {

    private static List<RpcListener> rpcListenerList = new ArrayList<>();

    private static ExecutorService eventThreadPool = Executors.newFixedThreadPool(2);

    public static void registerListener(RpcListener rpcListener) {
        rpcListenerList.add(rpcListener);
    }

    public void init() {
        registerListener(new ServiceUpdateListener());
    }

    /**
     * 获取接口上的泛型T
     */
    public static Class<?> getInterfaceT(Object o) {
        Type[] types = o.getClass().getGenericInterfaces();
        ParameterizedType parameterizedType = (ParameterizedType) types[0];
        Type type = parameterizedType.getActualTypeArguments()[0];
        if (type instanceof Class<?>) {
            return (Class<?>) type;
        }
        return null;
    }

    public static void sendEvent(RpcEvent rpcEvent) {
        if (CommonUtils.isEmptyList(rpcListenerList)) {
            return;
        }
        for (RpcListener<?> rpcListener: rpcListenerList) {
            Class<?> type = getInterfaceT(rpcListener);
            if (Objects.equals(type, rpcEvent.getClass())) {
                eventThreadPool.execute(
                        () -> {
                            try {
                                rpcListener.callback(rpcEvent.getData());
                            } catch (Exception exception) {
                                exception.printStackTrace();
                            }
                        }
                );
            }
        }
    }
}
