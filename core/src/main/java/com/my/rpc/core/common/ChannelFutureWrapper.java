package com.my.rpc.core.common;

import io.netty.channel.ChannelFuture;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/17 16:15
 * @Description ChannelFuture的包装类
 **/
public class ChannelFutureWrapper {

    private ChannelFuture channelFuture;

    private String host;

    private Integer port;

    private Integer weight;

    private String group;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }

    public ChannelFuture getChannelFuture() {
        return channelFuture;
    }

    public void setChannelFuture(ChannelFuture channelFuture) {
        this.channelFuture = channelFuture;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
