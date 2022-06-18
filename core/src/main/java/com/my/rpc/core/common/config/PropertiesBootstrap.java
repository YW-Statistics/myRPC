package com.my.rpc.core.common.config;

import java.io.IOException;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/18 16:06
 * @Description 配置信息
 **/
public class PropertiesBootstrap {

    private volatile boolean configIsReady;

    public static final String SERVER_PORT = "rpc.serverPort";
    public static final String REGISTER_ADDRESS = "rpc.registerAddr";
    public static final String APPLICATION_NAME = "rpc.applicationName";
    public static final String PROXY_TYPE = "rpc.proxyType";

    public static ServerConfig loadServerConfigFromLocal() {
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadServerConfigFromLocal fail, e is {}", e);
        }
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setServerPort(PropertiesLoader.getPropertiesInteger(SERVER_PORT));
        serverConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        serverConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        return serverConfig;
    }

    public static ClientConfig loadClientConfigFromLocal(){
        try {
            PropertiesLoader.loadConfiguration();
        } catch (IOException e) {
            throw new RuntimeException("loadClientConfigFromLocal fail,e is {}", e);
        }
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setApplicationName(PropertiesLoader.getPropertiesStr(APPLICATION_NAME));
        clientConfig.setRegisterAddr(PropertiesLoader.getPropertiesStr(REGISTER_ADDRESS));
        clientConfig.setProxyType(PropertiesLoader.getPropertiesStr(PROXY_TYPE));
        return clientConfig;
    }
}
