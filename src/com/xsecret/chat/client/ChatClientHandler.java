/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.xsecret.chat.client;

import com.xsecret.chat.MsgProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Handles a client-side channel.
 */
public class ChatClientHandler extends SimpleChannelInboundHandler<MsgProtocol.MsgContent> {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    MsgProtocol.MsgContent.Builder builder = MsgProtocol.MsgContent.newBuilder();
                    builder.setMsgType(MsgProtocol.MsgType.PING);
                    ctx.writeAndFlush(builder.build());
                    System.out.println("send ping to server----------");
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, MsgProtocol.MsgContent baseMsg) throws Exception {
        MsgProtocol.MsgType msgType = baseMsg.getMsgType();
        if (MsgProtocol.MsgType.LOGIN.equals(msgType)) {
            //向服务器发起登录
            MsgProtocol.MsgContent.Builder builder = MsgProtocol.MsgContent.newBuilder();
            builder.setMsgType(MsgProtocol.MsgType.LOGIN)
                    .setUserPassword("111111")
                    .setUserName("pangff");
            ctx.writeAndFlush(builder.build());
        }
        if (MsgProtocol.MsgType.PING.equals(msgType)) {
            System.out.println("receive ping from server----------");
        }
        if (MsgProtocol.MsgType.SEND.equals(msgType)) {
            System.out.println("收到：" + baseMsg.getClientId() + "的消息" + baseMsg.getToClientMsg());
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
