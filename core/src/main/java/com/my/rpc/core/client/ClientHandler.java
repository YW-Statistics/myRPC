package com.my.rpc.core.client;

import com.alibaba.fastjson.JSON;
import com.my.rpc.core.common.RpcInvocation;
import com.my.rpc.core.common.RpcProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

import static com.my.rpc.core.common.cache.CommonClientCache.RESP_MAP;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/12 11:00
 * @Description 客户端处理器
 **/
public class ClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
        Channel channel = ctx.channel();
        if (channel.isActive()) {
            ctx.close();
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        RpcProtocol rpcProtocol = (RpcProtocol) msg;
        byte[] reqContent = rpcProtocol.getContent();
        String json = new String(reqContent, 0, reqContent.length);
        RpcInvocation rpcInvocation = JSON.parseObject(json, RpcInvocation.class);
        if (!RESP_MAP.containsKey(rpcInvocation.getUuid())) {
            throw new IllegalArgumentException("server response is error");
        }
        RESP_MAP.put(rpcInvocation.getUuid(), rpcInvocation);
        ReferenceCountUtil.release(msg);
    }
}
