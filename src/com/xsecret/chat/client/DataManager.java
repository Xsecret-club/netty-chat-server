package com.xsecret.chat.client;

import com.xsecret.chat.model.*;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by pangff on 16/1/16.
 */
public class DataManager extends Thread {


    private static LinkedBlockingQueue<BaseMsg> msgueue = new LinkedBlockingQueue<>();
    private static String toClientId = "shengxinlei";
    static {
        LoginMsg loginMsg = new LoginMsg();
        loginMsg.userName = "pangff";
        loginMsg.password = "111111";
        loginMsg.clientId = ClientIdHelper.getClientId();
        msgueue.add(loginMsg);

        SendMsg sendMsg = new SendMsg();
        sendMsg.msgBody = "hello";
        sendMsg.toClientId = toClientId;
        sendMsg.clientId = ClientIdHelper.getClientId();
        msgueue.add(sendMsg);

        SendMsg sendMsg2 = new SendMsg();
        sendMsg2.msgBody = "hello===1";
        sendMsg2.clientId = ClientIdHelper.getClientId();
        sendMsg2.toClientId = toClientId;
        msgueue.add(sendMsg2);

        SendMsg sendMsg3 = new SendMsg();
        sendMsg3.msgBody = "hello===2";
        sendMsg3.toClientId = toClientId;
        sendMsg3.clientId = ClientIdHelper.getClientId();
        msgueue.add(sendMsg3);


        LogoutMsg logoutMsg = new LogoutMsg();
        logoutMsg.clientId = ClientIdHelper.getClientId();
        msgueue.add(logoutMsg);
    }

    boolean stopFlag = false;
    @Override
    public void run() {
        while (!stopFlag){
            try {
                BaseMsg baseMsg =  msgueue.take();
                ChatClient.msgLinkedBlockingQueue.add(baseMsg);
                sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void stopThread(){
        stopFlag = true;
    }
}
