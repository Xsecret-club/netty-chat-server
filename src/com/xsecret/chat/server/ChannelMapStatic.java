package com.xsecret.chat.server;

import io.netty.channel.Channel;
import io.netty.channel.socket.SocketChannel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by pangff on 16/1/16.
 */
public class ChannelMapStatic {

    private static Map<String,SocketChannel> channelMap = new ConcurrentHashMap();


    public static void addChannel(String id,SocketChannel socketChannel){
        channelMap.put(id,socketChannel);
    }

    public static Channel getChannel(String clientId){
        return channelMap.get(clientId);
    }

    public static void removeChannel(SocketChannel socketChannel){
        for (Map.Entry entry:channelMap.entrySet()){
            if (entry.getValue()==socketChannel){
                channelMap.remove(entry.getKey());
            }
        }
    }
}
