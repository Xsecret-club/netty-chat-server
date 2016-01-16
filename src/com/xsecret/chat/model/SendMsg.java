package com.xsecret.chat.model;

/**
 * Created by pangff on 16/1/16.
 */
public class SendMsg extends BaseMsg {

    public String toClientId;

    public String msgBody;

    public SendMsg(){
        this.msgType = MsgType.SEND;
    }
}
