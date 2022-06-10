package com.my.rpc.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/10 17:36
 * @Description RPC编码器
 **/
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol> {

    /**
     * 编码
     */
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, RpcProtocol rpcProtocol, ByteBuf byteBuf) throws Exception {
        byteBuf.writeShort(rpcProtocol.getMagicNumber());
        byteBuf.writeInt(rpcProtocol.getContentLength());
        byteBuf.writeBytes(rpcProtocol.getContent());
    }
}
