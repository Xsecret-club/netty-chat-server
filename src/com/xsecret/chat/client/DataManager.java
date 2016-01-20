package com.xsecret.chat.client;

import com.xsecret.chat.MsgProtocol;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by pangff on 16/1/16.
 */
public class DataManager extends Thread {

    private LinkedBlockingQueue<MsgProtocol.MsgContent> msgQueue = new LinkedBlockingQueue<>();
    private String clientId;
    private String toClientId;
    boolean stopFlag = false;

    DataManager(String clientId, String toClientId) {
        this.clientId = clientId;
        this.toClientId = toClientId;
    }

    @Override
    public void run() {
        while (!stopFlag) {
            try {
                initMsgQueue();
                MsgProtocol.MsgContent baseMsg = msgQueue.take();
                ChatClient.msgLinkedBlockingQueue.add(baseMsg);
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void initMsgQueue() {
        MsgProtocol.MsgContent.Builder builder = MsgProtocol.MsgContent.newBuilder();
        builder.setMsgType(MsgProtocol.MsgType.LOGIN)
                .setClientId(ClientIdHelper.getClientId())
                .setUserName("pangff")
                .setUserPassword("111111");
        msgQueue.add(builder.build());

        builder.clear();
        builder.setMsgType(MsgProtocol.MsgType.SEND)
                .setClientId(clientId)
                .setToClientId(toClientId)
                .setToClientMsg("hello");
        msgQueue.add(builder.build());

        builder.clear();
        builder.setMsgType(MsgProtocol.MsgType.SEND)
                .setClientId(clientId)
                .setToClientId(toClientId)
                .setToClientMsg("hello111");
        msgQueue.add(builder.build());

        builder.clear();
        builder.setMsgType(MsgProtocol.MsgType.LOGOUT)
                .setClientId(clientId);
        msgQueue.add(builder.build());
    }

    public void stopThread() {
        stopFlag = true;
    }
}
