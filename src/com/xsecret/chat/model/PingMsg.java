package com.xsecret.chat.model;

/**
 * Created by pangff on 16/1/16.
 */
public class PingMsg extends BaseMsg {

    public PingMsg(){
        this.msgType = MsgType.PING;
    }
}
