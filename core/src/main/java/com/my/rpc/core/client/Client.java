package com.my.rpc.core.client;

import com.alibaba.fastjson.JSON;
import com.my.rpc.core.common.RpcDecoder;
import com.my.rpc.core.common.RpcEncoder;
import com.my.rpc.core.common.RpcInvocation;
import com.my.rpc.core.common.RpcProtocol;
import com.my.rpc.core.common.config.ClientConfig;
import com.my.rpc.core.common.config.PropertiesBootstrap;
import com.my.rpc.core.common.event.RpcListenerLoader;
import com.my.rpc.core.common.utils.CommonUtils;
import com.my.rpc.core.proxy.jdk.JDKProxyFactory;
import com.my.rpc.core.registry.URL;
import com.my.rpc.core.registry.zookeeper.AbstractRegister;
import com.my.rpc.core.registry.zookeeper.ZookeeperRegister;
import com.my.rpc.core.router.RandomRouterImpl;
import com.my.rpc.core.router.RotateRouterImpl;
import com.my.rpc.core.serialize.fastjson.FastJsonSerializeFactory;
import com.my.rpc.core.serialize.hessian.HessianSerializeFactory;
import com.my.rpc.core.serialize.jdk.JdkSerializeFactory;
import com.my.rpc.core.serialize.kryo.KryoSerializeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import my.rpc.interfaces.DataService;

import java.util.List;
import java.util.Map;

import static com.my.rpc.core.common.cache.CommonClientCache.*;
import static com.my.rpc.core.common.constants.RpcConstants.*;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 17:21
 * @Description 客户端
 **/
public class Client {

    public static EventLoopGroup clientGroup = new NioEventLoopGroup();

    private ClientConfig clientConfig;

    private AbstractRegister abstractRegister;

    private RpcListenerLoader rpcListenerLoader;

    private Bootstrap bootstrap = new Bootstrap();

    public Bootstrap getBootstrap() {
        return bootstrap;
    }

    public void setBootstrap(Bootstrap bootstrap) {
        this.bootstrap = bootstrap;
    }

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    public void initClientConfig() {
        // 初始化路由策略
        String routerStrategy = clientConfig.getRouterStrategy();
        if (RANDOM_ROUTER_TYPE.equals(routerStrategy)) {
            ROUTER = new RandomRouterImpl();
        }else if (ROTATE_ROUTER_TYPE.equals(routerStrategy)) {
            ROUTER = new RotateRouterImpl();
        }
        String clientSerialize = clientConfig.getClientSerialize();
        switch (clientSerialize) {
            case JDK_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new JdkSerializeFactory();
                break;
            case FAST_JSON_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new FastJsonSerializeFactory();
                break;
            case HESSIAN2_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new HessianSerializeFactory();
                break;
            case KRYO_SERIALIZE_TYPE:
                CLIENT_SERIALIZE_FACTORY = new KryoSerializeFactory();
                break;
            default:
                throw new RuntimeException("no match serialize type for " + clientSerialize);
        }
    }

    public RpcReference initClientApplication() {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        bootstrap.group(clientGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new RpcEncoder());
                        socketChannel.pipeline().addLast(new RpcDecoder());
                        socketChannel.pipeline().addLast(new ClientHandler());
                    }
                });
        rpcListenerLoader = new RpcListenerLoader();
        rpcListenerLoader.init();
        this.clientConfig = PropertiesBootstrap.loadClientConfigFromLocal();
        return new RpcReference(new JDKProxyFactory());
    }

    /**
     * 启动服务之前预定对应的RPC服务
     */
    public void doSubscribeService(Class serviceBean) {
        if (null == abstractRegister) {
            abstractRegister = new ZookeeperRegister(clientConfig.getRegisterAddr());
        }
        URL url = new URL();
        url.setApplicationName(clientConfig.getApplicationName());
        url.setServiceName(serviceBean.getName());
        url.addParameter("host", CommonUtils.getIpAddress());
        Map<String, String> result = abstractRegister.getServiceWeightMap(serviceBean.getName());
        URL_MAP.put(serviceBean.getName(), result);
        abstractRegister.subscribe(url);
    }

    /**
     * 开始和各个provider建立连接
     */
    public void doConnectServer() {
        for (URL providerURL: SUBSCRIBE_SERVICE_LIST) {
            List<String> providerIps = abstractRegister.getProviderIps(providerURL.getServiceName());
            for (String providerIp: providerIps) {
                try {
                    ConnectionHandler.connect(providerURL.getServiceName(), providerIp);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            URL url = new URL();
            url.addParameter("servicePath", providerURL.getServiceName() + "/provider");
            url.addParameter("providerIps", JSON.toJSONString(providerIps));
            // 客户端在此新增一个订阅功能
            abstractRegister.doAfterSubscribe(url);
        }
    }

    /**
     * 开启发送线程, 该线程专门负责将数据包发送给服务端
     */
    private void startClient() {
        Thread asyncSendJob = new Thread(new AsyncSendJob());
        asyncSendJob.start();
    }

    /**
     * 异步发送数据
     */
    private class AsyncSendJob implements Runnable {

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞模式
                    RpcInvocation data = SEND_QUEUE.take();
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    ChannelFuture channelFuture = ConnectionHandler.getChannelFuture(data.getTargetServiceName());
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        RpcReference rpcReference = client.initClientApplication();
        DataService dataService = rpcReference.get(DataService.class);
        client.initClientConfig();
        client.doSubscribeService(DataService.class);
        ConnectionHandler.setBootstrap(client.getBootstrap());
        client.doConnectServer();
        client.startClient();
        for (int i = 0;i < 100;i++) {
            try {
                String result = dataService.sendData("test");
                System.out.println(result);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
