package com.my.rpc.core.client;

import com.alibaba.fastjson.JSON;
import com.my.rpc.core.common.RpcDecoder;
import com.my.rpc.core.common.RpcEncoder;
import com.my.rpc.core.common.RpcInvocation;
import com.my.rpc.core.common.RpcProtocol;
import com.my.rpc.core.common.config.ClientConfig;
import com.my.rpc.core.proxy.jdk.JDKProxyFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import my.rpc.interfaces.DataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.my.rpc.core.common.cache.CommonClientCache.SEND_QUEUE;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/11 17:21
 * @Description 客户端
 **/
public class Client {

    private Logger logger = LoggerFactory.getLogger(Client.class);

    public static EventLoopGroup clientGroup = new NioEventLoopGroup();

    private ClientConfig clientConfig;

    public ClientConfig getClientConfig() {
        return clientConfig;
    }

    public void setClientConfig(ClientConfig clientConfig) {
        this.clientConfig = clientConfig;
    }

    /**
     * 客户端启动方法
     */
    public RpcReference startClientApplication() throws InterruptedException {
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
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
        // 链接netty服务端
        ChannelFuture channelFuture = bootstrap.connect(clientConfig.getServerAddr(), clientConfig.getPort()).sync();
        logger.info("-------------------服务启动----------------");
        this.startClient(channelFuture);
        // 注入代理工程, 解释：通过代理类将uuid存入本地缓存
        return new RpcReference(new JDKProxyFactory());
    }

    /**
     * 开启发送线程, 该线程专门负责将数据包发送给服务端
     * @param channelFuture Channel异步结果封装类
     */
    private void startClient(ChannelFuture channelFuture) {
        Thread asyncSendJob = new Thread(new AsyncSendJob(channelFuture));
        asyncSendJob.start();
    }

    /**
     * 异步发送数据
     */
    private class AsyncSendJob implements Runnable {

        private ChannelFuture channelFuture;

        public AsyncSendJob(ChannelFuture channelFuture) {
            this.channelFuture = channelFuture;
        }

        @Override
        public void run() {
            while (true) {
                try {
                    // 阻塞模式
                    RpcInvocation data = SEND_QUEUE.take();
                    String json = JSON.toJSONString(data);
                    RpcProtocol rpcProtocol = new RpcProtocol(json.getBytes());
                    channelFuture.channel().writeAndFlush(rpcProtocol);
                } catch (InterruptedException exception) {
                    exception.printStackTrace();
                }
            }
        }
    }

    public static void main(String[] args) throws Throwable {
        Client client = new Client();
        ClientConfig clientConfig = new ClientConfig();
        clientConfig.setServerAddr("localhost");
        clientConfig.setPort(9090);
        client.setClientConfig(clientConfig);
        RpcReference rpcReference = client.startClientApplication();
        DataService dataService = rpcReference.get(DataService.class);
        for (int i = 0;i < 10;i++) {
            String result = dataService.sendData("test");
            System.out.println(result);
        }
    }
}
