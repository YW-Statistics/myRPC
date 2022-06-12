package com.my.rpc.core.common.config;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 17:25
 * @Description 客户端配置
 **/
public class ClientConfig {

    private Integer port;

    private String serverAddr;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }
}
