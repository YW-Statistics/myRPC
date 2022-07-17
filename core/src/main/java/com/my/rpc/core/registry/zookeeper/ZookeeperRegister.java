package com.my.rpc.core.registry.zookeeper;

import com.alibaba.fastjson.JSON;
import com.my.rpc.core.common.event.RpcEvent;
import com.my.rpc.core.common.event.RpcListenerLoader;
import com.my.rpc.core.common.event.RpcUpdateEvent;
import com.my.rpc.core.common.event.data.URLChangeWrapper;
import com.my.rpc.core.registry.RegistryService;
import com.my.rpc.core.registry.URL;
import my.rpc.interfaces.DataService;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/15 22:27
 * @Description ZK注册器
 **/
public class ZookeeperRegister extends AbstractRegister implements RegistryService {

    private AbstractZookeeperClient zkClient;

    private String ROOT = "/rpc";

    private String getProviderPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/provider/" + url.getParams().get("host") + ":" + url.getParams().get("port");
    }

    private String getConsumerPath(URL url) {
        return ROOT + "/" + url.getServiceName() + "/consumer/" + url.getApplicationName() + ":" + url.getParams().get("host") + ":";
    }

    public ZookeeperRegister(String address) {
        this.zkClient = new CuratorZookeeperClient(address);
    }

    @Override
    public void register(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildProvideUrlStr(url);
        if (zkClient.existNode(getProviderPath(url))) {
            zkClient.deleteNode(getProviderPath(url));
        }
        zkClient.createTemporaryData(getProviderPath(url), urlStr);
        super.register(url);
    }

    @Override
    public void unRegister(URL url) {
        zkClient.deleteNode(getProviderPath(url));
        super.unRegister(url);
    }

    @Override
    public void doUnSubscribe(URL url) {
        zkClient.deleteNode(getConsumerPath(url));
        super.doUnSubscribe(url);
    }

    @Override
    public void subscribe(URL url) {
        if (!this.zkClient.existNode(ROOT)) {
            zkClient.createPersistentData(ROOT, "");
        }
        String urlStr = URL.buildConsumerUrlStr(url);
        if (this.zkClient.existNode(getConsumerPath(url))) {
            zkClient.deleteNode(getConsumerPath(url));
        }
        zkClient.createTemporarySeqData(getConsumerPath(url), urlStr);
        super.subscribe(url);
    }

    @Override
    public void doBeforeSubscribe(URL url) {

    }

    @Override
    public void doAfterSubscribe(URL url) {
        // 监听是否有新的服务注册
        String servicePath = url.getParams().get("servicePath");
        String newServerNodePath = ROOT + "/" + servicePath;
        watchChildNodeData(newServerNodePath);
        String providerIpStrJson = url.getParams().get("providerIps");
        List<String> providerIps = JSON.parseObject(providerIpStrJson, List.class);
        for (String providerIp: providerIps) {
            this.watchNodeDataChange(ROOT + "/" + servicePath + providerIp);
        }
    }

    public void watchNodeDataChange(String newServerNodePath) {
    }

    private void watchChildNodeData(String newServerNodePath) {
        zkClient.watchChildNodeData(newServerNodePath, new Watcher() {
            @Override
            public void process(WatchedEvent watchedEvent) {
                System.out.println(watchedEvent);
                String path = watchedEvent.getPath();
                List<String> childrenDataList = zkClient.getChildrenData(path);
                URLChangeWrapper urlChangeWrapper = new URLChangeWrapper();
                urlChangeWrapper.setProviderUrl(childrenDataList);
                urlChangeWrapper.setServiceName(path.split("/")[2]);
                // 自定义监听组件
                RpcEvent rpcEvent = new RpcUpdateEvent(urlChangeWrapper);
                RpcListenerLoader.sendEvent(rpcEvent);
                // 收到回调后再调用注册一次监听, 这样保证一直能接收消息（ZK节点的消息通知是一次性的）
                watchChildNodeData(path);
            }
        });
    }

    @Override
    public List<String> getProviderIps(String serviceName) {
        return this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
    }

    @Override
    public Map<String, String> getServiceWeightMap(String serviceName) {
        List<String> nodeDataList = this.zkClient.getChildrenData(ROOT + "/" + serviceName + "/provider");
        Map<String, String> result = new HashMap<>();
        for (String ipAndHost : nodeDataList) {
            String childData = this.zkClient.getNodeData(ROOT + "/" + serviceName + "/provider/" + ipAndHost);
            result.put(ipAndHost, childData);
        }
        return result;
    }

}
