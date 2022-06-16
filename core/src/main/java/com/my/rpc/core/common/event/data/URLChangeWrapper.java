package com.my.rpc.core.common.event.data;

import java.util.List;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/16 11:42
 * @Description URL修改包装器
 **/
public class URLChangeWrapper {

    private String serviceName;

    private List<String> providerUrl;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public List<String> getProviderUrl() {
        return providerUrl;
    }

    public void setProviderUrl(List<String> providerUrl) {
        this.providerUrl = providerUrl;
    }

    @Override
    public String toString() {
        return "URLChangeWrapper{" +
                "serviceName='" + serviceName + '\'' +
                ", providerUrl=" + providerUrl +
                '}';
    }
}
