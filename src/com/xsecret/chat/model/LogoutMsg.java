package com.xsecret.chat.model;

/**
 * Created by pangff on 16/1/16.
 */
public class LogoutMsg extends BaseMsg {

    public LogoutMsg(){
        this.msgType = MsgType.LOGOUT;
    }
}
