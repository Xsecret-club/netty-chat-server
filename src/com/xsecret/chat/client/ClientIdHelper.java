package com.xsecret.chat.client;

/**
 * Created by pangff on 16/1/16.
 */
public class ClientIdHelper {

    private static String clientId = "shengxinlei";
    private static String clientIdChange = "pangff";

    private static ClientIdHelper instance;

    private ClientIdHelper(){}

    public static ClientIdHelper getInstance(){
        if(instance == null){
            instance = new ClientIdHelper();
        }
        return instance;
    }
    public static String getClientId(){
        return clientId;
    }

}
