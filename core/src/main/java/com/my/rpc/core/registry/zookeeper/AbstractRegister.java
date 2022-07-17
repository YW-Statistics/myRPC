package com.my.rpc.core.registry.zookeeper;

import com.my.rpc.core.registry.RegistryService;
import com.my.rpc.core.registry.URL;

import java.util.List;
import java.util.Map;

import static com.my.rpc.core.common.cache.CommonClientCache.SUBSCRIBE_SERVICE_LIST;
import static com.my.rpc.core.common.cache.CommonServerCache.PROVIDER_URL_SET;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/14 17:06
 * @Description 注册中心数据处理模板
 **/
public abstract class AbstractRegister implements RegistryService {

    @Override
    public void register(URL url) {
        PROVIDER_URL_SET.add(url);
    }

    @Override
    public void unRegister(URL url) {
        PROVIDER_URL_SET.remove(url);
    }

    @Override
    public void subscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.add(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        SUBSCRIBE_SERVICE_LIST.remove(url);
    }

    /**
     * 订阅前动作（扩展方法）
     */
    public abstract void doBeforeSubscribe(URL url);

    /**
     * 订阅后动作（扩展方法）
     */
    public abstract void doAfterSubscribe(URL url);

    /**
     * 获取提供者的ip列表
     */
    public abstract List<String> getProviderIps(String serviceName);

    /**
     * 获取服务的权重信息
     *
     * @param serviceName
     * @return <ip:port --> urlString>,<ip:port --> urlString>,<ip:port --> urlString>,<ip:port --> urlString>
     */
    public abstract Map<String, String> getServiceWeightMap(String serviceName);
}
