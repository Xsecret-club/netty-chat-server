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
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Simple SSL chat client modified from .
 */
public final class ChatClient {
    static DataManager dataHelper = new DataManager();
    static final String HOST = System.getProperty("host", "127.0.0.1");
    static final int PORT = Integer.parseInt(System.getProperty("port", "8989"));
    public static LinkedBlockingQueue<MsgProtocol.MsgContent> msgLinkedBlockingQueue = new LinkedBlockingQueue<>();

    public static void main(String[] args) throws Exception {
        // Configure SSL.
        final SslContext sslCtx = SslContextBuilder.forClient()
                .trustManager(InsecureTrustManagerFactory.INSTANCE).build();

        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new ChatClientInitializer(sslCtx));

            // Start the connection attempt.
            Channel ch = b.connect(HOST, PORT).sync().channel();

            // Read commands from the stdin.
            ChannelFuture lastWriteFuture = null;

            dataHelper.start();
            for (; ; ) {
                System.out.println("=======循环=========");
                MsgProtocol.MsgContent baseMsg = msgLinkedBlockingQueue.take();
                if (baseMsg == null) {
                    break;
                }
                if (MsgProtocol.MsgType.LOGIN.equals(baseMsg.getMsgType())) {
                    System.out.println("=======login=========");
                }

                // If user typed the 'bye' command, wait until the server closes
                // the connection.
                if (MsgProtocol.MsgType.LOGOUT.equals(baseMsg.getMsgType())) {
                    System.out.println("=======logout=========");
                    ch.closeFuture().sync();
                    dataHelper.stopThread();
                    break;
                }

                // Sends the received line to the server.
                lastWriteFuture = ch.writeAndFlush(baseMsg);
                System.out.println(baseMsg.getMsgType() + " to " + baseMsg.getToClientId() + ": " + baseMsg.getToClientMsg());
            }

            // Wait until all messages are flushed before closing the channel.
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } finally {
            // The connection is closed automatically on shutdown.
            group.shutdownGracefully();
        }
    }
}
