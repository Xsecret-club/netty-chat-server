package com.xsecret.chat.server;

import com.xsecret.chat.MsgProtocol;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pangff on 16/1/16.
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<MsgProtocol.MsgContent> {

    private ConcurrentHashMap<String, Channel> sessionChannelMap = new ConcurrentHashMap<String, Channel>();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ChannelMapStatic.removeChannel((SocketChannel) ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MsgProtocol.MsgContent baseMsg) throws Exception {
        System.out.println("client" + baseMsg.getClientId() + " " + baseMsg.getMsgType());
        if (MsgProtocol.MsgType.LOGIN.equals(baseMsg.getMsgType())) {
            if ("pangff".equals(baseMsg.getUserName()) && "111111".equals(baseMsg.getUserPassword())) {
                //登录成功,把channel存到服务端的map中
                ChannelMapStatic.addChannel(baseMsg.getClientId(), (SocketChannel) ctx.channel());
                System.out.println("client" + baseMsg.getClientId() + " 登录成功");
            }
        } else {
            if (baseMsg.getClientId() == null || ChannelMapStatic.getChannel(baseMsg.getClientId()) == null) {
                //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
                MsgProtocol.MsgContent.Builder builder = MsgProtocol.MsgContent.newBuilder();
                builder.setMsgType(MsgProtocol.MsgType.LOGIN);
                ctx.channel().writeAndFlush(builder.build());
            }
        }
        if (MsgProtocol.MsgType.PING.equals(baseMsg.getMsgType())) {
            ChannelMapStatic.addChannel(baseMsg.getClientId(), (SocketChannel) ctx.channel());
        }
        if (MsgProtocol.MsgType.SEND.equals(baseMsg.getMsgType())) {
            //收到客户端的请求
            String toClientId = baseMsg.getToClientId();
            if (ChannelMapStatic.getChannel(toClientId) == null) {
                //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
                MsgProtocol.MsgContent.Builder builder = MsgProtocol.MsgContent.newBuilder();
                builder.setMsgType(MsgProtocol.MsgType.ERROR)
                        .setErrorMsg("对方已下线");
                ctx.channel().writeAndFlush(builder.build());
            } else {
                ChannelMapStatic.getChannel(toClientId).writeAndFlush(baseMsg);
            }
        }
        System.err.println(baseMsg.getMsgType());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
    }
}