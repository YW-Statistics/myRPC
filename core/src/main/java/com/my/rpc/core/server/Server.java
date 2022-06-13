package com.my.rpc.core.server;

import com.my.rpc.core.common.RpcDecoder;
import com.my.rpc.core.common.RpcEncoder;
import com.my.rpc.core.common.config.ServerConfig;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import static com.my.rpc.core.common.cache.CommonServerCache.PROVIDER_CLASS_MAP;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/8 22:07
 * @Description 服务端
 **/
public class Server {

    private static EventLoopGroup bossGroup;

    private static EventLoopGroup workGroup;

    private ServerConfig serverConfig;

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
        bootstrap.bind(serverConfig.getPort()).sync();
    }

    /**
     * 注册服务
     * @param serviceBean 需要托管的JavaBean
     */
    public void registerService(Object serviceBean) {
        if (0 == serviceBean.getClass().getInterfaces().length) {
            throw new RuntimeException("service must had interface!");
        }
        Class[] classes = serviceBean.getClass().getInterfaces();
        if (classes.length > 1) {
            throw new RuntimeException("service must only had one interface!");
        }
        Class interfaceClass = classes[0];
        PROVIDER_CLASS_MAP.put(interfaceClass.getName(), serviceBean);
    }

    public static void main(String[] args) throws InterruptedException {
        Server server = new Server();
        ServerConfig serverConfig = new ServerConfig();
        serverConfig.setPort(9090);
        server.setServerConfig(serverConfig);
        server.registerService(new DataServiceImpl());
        server.startApplication();
    }
}
