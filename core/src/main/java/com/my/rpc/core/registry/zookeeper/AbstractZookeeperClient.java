package com.my.rpc.core.registry.zookeeper;

import org.apache.zookeeper.Watcher;

import java.util.List;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/14 22:49
 * @Description 基于Zookeeper的模板抽象类
 **/
public abstract class AbstractZookeeperClient {

    private String zkAddress;

    private Integer baseSleepTimes;

    private Integer maxRetryTimes;

    public AbstractZookeeperClient(String zkAddress) {
        this.zkAddress = zkAddress;
        this.baseSleepTimes = 3000;
        this.maxRetryTimes = 3;
    }

    public AbstractZookeeperClient(String zkAddress, Integer baseSleepTimes, Integer maxRetryTimes) {
        this.zkAddress = zkAddress;
        if (null == baseSleepTimes) {
            baseSleepTimes = 3000;
        }else {
            this.baseSleepTimes = baseSleepTimes;
        }
        if (null == maxRetryTimes) {
            this.maxRetryTimes = 3;
        }else {
            this.maxRetryTimes = maxRetryTimes;
        }
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public Integer getBaseSleepTimes() {
        return baseSleepTimes;
    }

    public void setBaseSleepTimes(Integer baseSleepTimes) {
        this.baseSleepTimes = baseSleepTimes;
    }

    public Integer getMaxRetryTimes() {
        return maxRetryTimes;
    }

    public void setMaxRetryTimes(Integer maxRetryTimes) {
        this.maxRetryTimes = maxRetryTimes;
    }

    public abstract void updateNodeData(String address, String data);

    public abstract Object getClient();

    /**
     * 拉取节点的数据
     */
    public abstract String getNodeData(String path);

    /**
     * 获取指定目录下的字节点数据
     */
    public abstract List<String> getChildrenData(String path);

    /**
     * 创建持久化类型节点数据信息
     */
    public abstract void createPersistentData(String address, String data);

    /**
     * 创建持久化类型的有序节点数据信息
     */
    public abstract void createPersistentWithSeqData(String address, String data);

    /**
     * 创建有序且临时类型节点数据信息
     */
    public abstract void createTemporarySeqData(String address, String data);

    /**
     * 创建临时节点数据信息
     */
    public abstract void createTemporaryData(String address, String data);

    /**
     * 设置临时节点数据信息
     */
    public abstract void setTemporaryData(String address, String data);

    /**
     * 断开zk的客户端链接
     */
    public abstract void destroy();

    /**
     * 展示这个节点下面的数据
     */
    public abstract List<String> listNode(String address);

    /**
     * 删除节点下的数据
     */
    public abstract boolean deleteNode(String address);

    /**
     * 判断是否存在其他节点
     */
    public abstract boolean existNode(String address);

    /**
     * 监听path路径下某个节点的数据变化
     */
    public abstract void watchNodeData(String path, Watcher watcher);

    /**
     * 监听path路径子节点下的数据变化
     */
    public abstract void watchChildNodeData(String path, Watcher watcher);
}
