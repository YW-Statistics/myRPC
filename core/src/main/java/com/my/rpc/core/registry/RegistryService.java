package com.my.rpc.core.registry;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/13 17:03
 * @Description 注册中心业务接口
 **/
public interface RegistryService {

    /**
     * 注册url
     *
     * 将rpc服务写入注册中心节点
     * 当出现网络抖动的时候需要进行适当的重试做法
     * 注册服务url的时候需要写入持久化文件中
     *
     * @param url 地址
     */
    void register(URL url);

    /**
     * 服务下线
     *
     * 持久化节点无法进行下线操作
     * 下线的服务必须保证url完全匹配
     * 移除持久化的一些内容信息
     *
     * @param url 地址
     */
    void unRegister(URL url);

    /**
     * 服务订阅
     *
     * @param url 地址
     */
    void subscribe(URL url);

    /**
     * 取消订阅
     *
     * @param url 地址
     */
    void doUnSubscribe(URL url);
}
