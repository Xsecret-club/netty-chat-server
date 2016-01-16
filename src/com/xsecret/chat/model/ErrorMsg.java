package com.xsecret.chat.model;

/**
 * Created by pangff on 16/1/16.
 */
public class ErrorMsg extends BaseMsg {

    public ErrorMsg(){
        this.errorMsg = MsgType.ERROR;
    }
    public String errorMsg;


}
