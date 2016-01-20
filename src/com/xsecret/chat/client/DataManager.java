package com.xsecret.chat.client;

import com.xsecret.chat.MsgProtocol;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by pangff on 16/1/16.
 */
public class DataManager extends Thread {

    private static LinkedBlockingQueue<MsgProtocol.MsgContent> msgueue = new LinkedBlockingQueue<>();
    private static String toClientId = "pangff";

    static {
        MsgProtocol.MsgContent.Builder builder = MsgProtocol.MsgContent.newBuilder();
        builder.setMsgType(MsgProtocol.MsgType.LOGIN)
                .setClientId(ClientIdHelper.getClientId())
                .setUserName("pangff")
                .setUserPassword("111111");
        msgueue.add(builder.build());

        builder.clear();
        builder.setMsgType(MsgProtocol.MsgType.SEND)
                .setClientId(ClientIdHelper.getClientId())
                .setToClientId(toClientId)
                .setToClientMsg("hello");
        msgueue.add(builder.build());

        builder.clear();
        builder.setMsgType(MsgProtocol.MsgType.SEND)
                .setClientId(ClientIdHelper.getClientId())
                .setToClientId(toClientId)
                .setToClientMsg("hello111");
        msgueue.add(builder.build());

        builder.clear();
        builder.setMsgType(MsgProtocol.MsgType.LOGOUT)
                .setClientId(ClientIdHelper.getClientId());
        msgueue.add(builder.build());
    }

    boolean stopFlag = false;

    @Override
    public void run() {
        while (!stopFlag) {
            try {
                MsgProtocol.MsgContent baseMsg = msgueue.take();
                ChatClient.msgLinkedBlockingQueue.add(baseMsg);
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread() {
        stopFlag = true;
    }
}
