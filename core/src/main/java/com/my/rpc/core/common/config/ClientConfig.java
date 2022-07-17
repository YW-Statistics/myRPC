package com.my.rpc.core.common.config;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 17:25
 * @Description 客户端配置
 **/
public class ClientConfig {

    private String applicationName;

    private String registerAddr;

    private String proxyType;

    private String routerStrategy;

    private String clientSerialize;

    public String getClientSerialize() {
        return clientSerialize;
    }

    public void setClientSerialize(String clientSerialize) {
        this.clientSerialize = clientSerialize;
    }

    public String getRouterStrategy() {
        return routerStrategy;
    }

    public void setRouterStrategy(String routerStrategy) {
        this.routerStrategy = routerStrategy;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getProxyType() {
        return proxyType;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }
}
