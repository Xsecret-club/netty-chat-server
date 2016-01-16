package com.xsecret.chat.model;

import java.io.Serializable;

/**
 * Created by pangff on 16/1/16.
 */
public class BaseMsg implements Serializable{

    public String clientId;

    public String msgType;

    public String stateCode;

    public String getMsgType() {
        return msgType;
    }
}
