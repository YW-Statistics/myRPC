package com.my.rpc.core.registry.zookeeper;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/14 11:58
 * @Description 提供者节点信息
 **/
public class ProviderNodeInfo {

    private String serviceName;

    private String address;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "ProviderNodeInfo{" +
                "serviceName='" + serviceName + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
