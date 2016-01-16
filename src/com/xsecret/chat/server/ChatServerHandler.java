package com.xsecret.chat.server;

import com.xsecret.chat.model.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.SocketChannel;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pangff on 16/1/16.
 */
public class ChatServerHandler extends SimpleChannelInboundHandler<BaseMsg> {

    private ConcurrentHashMap<String, Channel> sessionChannelMap = new ConcurrentHashMap<String, Channel>();

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        ChannelMapStatic.removeChannel((SocketChannel)ctx.channel());
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, BaseMsg baseMsg) throws Exception {
        //ctx.writeAndFlush(msg);
        System.out.println("client"+baseMsg.clientId+" "+baseMsg.msgType);
        if(MsgType.LOGIN.equals(baseMsg.getMsgType())){
            LoginMsg loginMsg=(LoginMsg)baseMsg;
            if("pangff".equals(loginMsg.userName)&&"111111".equals(loginMsg.password)){
                //登录成功,把channel存到服务端的map中
                ChannelMapStatic.addChannel(baseMsg.clientId, (SocketChannel) ctx.channel());
                System.out.println("client"+loginMsg.clientId+" 登录成功");
            }
        }else{
            if(baseMsg.clientId==null || ChannelMapStatic.getChannel(baseMsg.clientId) == null){
                //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
                LoginMsg loginMsg=new LoginMsg();
                ctx.channel().writeAndFlush(loginMsg);
            }
        }
        switch (baseMsg.getMsgType()){
            case MsgType.PING:{
                PingMsg pingMsg=(PingMsg)baseMsg;
                PingMsg replyPing=new PingMsg();
                ChannelMapStatic.addChannel(baseMsg.clientId, (SocketChannel) ctx.channel());
            }
            break;
            case MsgType.SEND:{
                //收到客户端的请求
                SendMsg sendMsg=(SendMsg)baseMsg;
                String toClientId = sendMsg.toClientId;
                if(ChannelMapStatic.getChannel(toClientId) == null){
                    //说明未登录，或者连接断了，服务器向客户端发起登录请求，让客户端重新登录
                    ErrorMsg errorMsg=new ErrorMsg();
                    errorMsg.errorMsg = "对方已下线";
                    ctx.channel().writeAndFlush(errorMsg);
                }else{
                    ChannelMapStatic.getChannel(toClientId).writeAndFlush(sendMsg);
                }
            }
            break;
            default:break;
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