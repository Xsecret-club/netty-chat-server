package com.xsecret.chat.client;

/**
 * Created by pangff on 16/1/16.
 */
public class ClientIdHelper {

    private static String clientId = "pangff";
    private static String clientIdChange = "shengxinlei";

    private static ClientIdHelper instance;

    private ClientIdHelper(){
        clientId = clientId;
    }

    public static ClientIdHelper getInstance(){
        if(instance == null){
            instance = new ClientIdHelper();
        }
        return instance;
    }
    public static String getClientId(){
        return clientIdChange;
    }

}
