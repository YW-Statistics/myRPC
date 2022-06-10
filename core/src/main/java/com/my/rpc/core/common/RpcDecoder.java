package com.my.rpc.core.common;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static com.my.rpc.core.common.constants.RpcConstants.MAGIC_NUMBER;

/**
 * @Author WWK wuwenkai97@163.com
 * @Date 2022/6/10 17:44
 * @Description Rpc解码器
 **/
public class RpcDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        // 防止收到体积过大的数据包
        if (byteBuf.readableBytes() > 1000) {
            byteBuf.skipBytes(byteBuf.readableBytes());
        }
        int beginReader;
        while (true) {
            // 获取读索引
            beginReader = byteBuf.readerIndex();
            // 标记读索引
            byteBuf.markReaderIndex();
            if (byteBuf.readShort() == MAGIC_NUMBER) {
                break;
            } else {
                // 如果开头不是我们定义的魔数, 则说明该数据由非法客户端发出
                channelHandlerContext.close();
                return;
            }
        }

        int length = byteBuf.readInt();
        // 如果实际长度小于约定的长度, 则说明索引位置错误
        if (byteBuf.readableBytes() < length) {
            // 将读索引移动到 beginReader这个位置
            byteBuf.readBytes(beginReader);
        }
        // 数据写入
        byte[] data = new byte[length];
        byteBuf.readBytes(data);
        RpcProtocol rpcProtocol = new RpcProtocol(data);
        list.add(rpcProtocol);
    }
}
