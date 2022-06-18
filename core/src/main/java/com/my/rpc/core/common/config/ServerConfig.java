package com.my.rpc.core.common.config;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/10 16:32
 * @Description 服务端配置
 **/
public class ServerConfig {

    private Integer serverPort;

    private String registerAddr;

    private String applicationName;

    public Integer getServerPort() {
        return serverPort;
    }

    public void setServerPort(Integer serverPort) {
        this.serverPort = serverPort;
    }

    public String getRegisterAddr() {
        return registerAddr;
    }

    public void setRegisterAddr(String registerAddr) {
        this.registerAddr = registerAddr;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }
}
