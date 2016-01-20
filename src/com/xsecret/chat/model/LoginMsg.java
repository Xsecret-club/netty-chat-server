package com.xsecret.chat.model;

/**
 * Created by pangff on 16/1/16.
 */
public class LoginMsg extends BaseMsg {

    public String userName;

    public String password;

    public LoginMsg(){
        this.msgType = MsgType.LOGIN;
    }
}
