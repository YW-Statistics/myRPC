package com.my.rpc.core.registry;

import com.my.rpc.core.registry.zookeeper.ProviderNodeInfo;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/13 17:23
 * @Description 配置类
 **/
public class URL {

    /**
     * 工程服务名称
     */
    private String applicationName;

    /**
     * 注册到节点的服务名服务名
     */
    private String serviceName;

    /**
     * 这里面可以自定义不限进行扩展
     * 分组
     * 权重
     * 服务提供者的地址
     * 服务提供者的端口
     */
    private Map<String, String> params = new HashMap<>();

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public Map<String, String> getParams() {
        return params;
    }

    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * 生成zk的提供者节点下的URL字符串
     */
    public static String buildProvideUrlStr(URL url) {
        String host = url.getParams().get("host");
        String port = url.getParams().get("port");
        return new String((url.getApplicationName() + ";" + url.getServiceName() + ";" + host + ";" + port + ";"
                + System.currentTimeMillis()).getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 生成zk的消费者节点下的URL字符串
     */
    public static String buildConsumerUrlStr(URL url) {
        String host = url.getParams().get("port");
        return new String((url.getApplicationName() + ";" + url.getServiceName() + ";" + host + ";"
                + System.currentTimeMillis()).getBytes(), StandardCharsets.UTF_8);
    }

    /**
     * 将某个节点的信息转化为Provider对象
     */
    public static ProviderNodeInfo buildURLFromUrlStr(String providerNodeStr) {
        String[] items = providerNodeStr.split("/");
        ProviderNodeInfo providerNodeInfo = new ProviderNodeInfo();
        providerNodeInfo.setServiceName(items[2]);
        providerNodeInfo.setAddress(items[4]);
        return providerNodeInfo;
    }
}
