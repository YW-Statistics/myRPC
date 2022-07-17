package com.my.rpc.core.server;

import com.my.rpc.core.common.RpcDecoder;
import com.my.rpc.core.common.RpcEncoder;
import com.my.rpc.core.common.config.PropertiesBootstrap;
import com.my.rpc.core.common.config.ServerConfig;
import com.my.rpc.core.common.utils.CommonUtils;
import com.my.rpc.core.registry.RegistryService;
import com.my.rpc.core.registry.URL;
import com.my.rpc.core.registry.zookeeper.ZookeeperRegister;
import com.my.rpc.core.serialize.fastjson.FastJsonSerializeFactory;
import com.my.rpc.core.serialize.hessian.HessianSerializeFactory;
import com.my.rpc.core.serialize.jdk.JdkSerializeFactory;
import com.my.rpc.core.serialize.kryo.KryoSerializeFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.my.rpc.core.common.cache.CommonServerCache.*;
import static com.my.rpc.core.common.constants.RpcConstants.*;
import static com.my.rpc.core.common.constants.RpcConstants.KRYO_SERIALIZE_TYPE;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/8 22:07
 * @Description 服务端
 **/
public class Server {

    private static EventLoopGroup bossGroup;

    private static EventLoopGroup workGroup;

    private ServerConfig serverConfig;

    private RegistryService registerService;

    public ServerConfig getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(ServerConfig serverConfig) {
        this.serverConfig = serverConfig;
    }

    /**
     * 应用启动
     */
    public void startApplication() throws InterruptedException {
        // 创建两个线程组：boss用于服务端accept监听, work用于IO操作
        bossGroup = new NioEventLoopGroup();
        workGroup = new NioEventLoopGroup();

        // 创建服务端的启动对象，并设置参数
        ServerBootstrap bootstrap = new ServerBootstrap();
        // 设置两个线程组到启动对象
        bootstrap.group(bossGroup, workGroup)
                // 设置服务端通道实现类型
                .channel(NioServerSocketChannel.class)
                // 设置线程队列的连接个数
                .option(ChannelOption.SO_BACKLOG, 1024)
                // 设置发送缓冲区的大小
                .option(ChannelOption.SO_SNDBUF, 16 * 1024)
                // 设置接收缓冲区的大小
                .option(ChannelOption.SO_RCVBUF, 16 * 1024)
                // 设置心跳间隔（保活时间）
                .option(ChannelOption.SO_KEEPALIVE, true)
                // 初始化通道对象
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        System.out.println("初始化provider过程");
                        socketChannel.pipeline().addLast(new RpcEncoder());
                        socketChannel.pipeline().addLast(new RpcDecoder());
                        socketChannel.pipeline().addLast(new ServerHandler());
                    }
                });
        this.batchExportUrl();
        bootstrap.bind(serverConfig.getServerPort()).sync();
    }

    /**
     * 批量注册服务
     */
    private void batchExportUrl() {
        Thread task = new Thread(() -> {
            try {
                Thread.sleep(2500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (URL url: PROVIDER_URL_SET) {
                registerService.register(url);
            }
        });
        task.start();
    }

    public void initServerConfig() {
        ServerConfig serverConfig = PropertiesBootstrap.loadServerConfigFromLocal();
        this.setServerConfig(serverConfig);
        String serverSerialize = serverConfig.getServerSerialize();
        switch (serverSerialize) {
            case JDK_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new JdkSerializeFactory();
                break;
            case FAST_JSON_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new FastJsonSerializeFactory();
                break;
            case HESSIAN2_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new HessianSerializeFactory();
                break;
            case KRYO_SERIALIZE_TYPE:
                SERVER_SERIALIZE_FACTORY = new KryoSerializeFactory();
                break;
            default:
                throw new RuntimeException("no match serialize type for " + serverSerialize);
        }
        System.out.println("serverSerialize is " + serverSerialize);
    }

    /**
     * 暴露服务信息
     */
    public void exportService(Object serviceBean) {
        if (0 == serviceBean.getClass().getInterfaces().length) {
            throw new RuntimeException("service must had interface!");
        }
        Class[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interface!");
        }
        if (null == registerService) {
            registerService = new ZookeeperRegister(serverConfig.getRegisterAddr());
        }
        Class interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
        URL url = new URL();
        url.setServiceName(interfaceClass.getName());
        url.setApplicationName(serverConfig.getApplicationName());
        url.addParameter("host", CommonUtils.getIpAddress());
        url.addParameter("port", String.valueOf(serverConfig.getServerPort()));
        PROVIDER_URL_SET.add(url);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        server.initServerConfig();
        server.exportService(new DataServiceImpl());
        server.startApplication();
    }
}
