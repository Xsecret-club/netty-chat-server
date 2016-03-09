package com.xsecret.chat.client;

import com.xsecret.chat.HelloWorldService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.*;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.nio.ByteBuffer;

/**
 * Created by jhss on 16/3/9.
 */
public class HelloClientDemo {
    public static final String SERVER_IP = "localhost";

    public static final int SERVER_PORT = 8090;

    public static final int TIME_OUT = 30000;

    public void startClient(String userName) {
        try {
            TTransport transport = new TSocket(SERVER_IP, SERVER_PORT, TIME_OUT);
            transport.open();
            TProtocol protocol = new TBinaryProtocol(transport);
            HelloWorldService.Client clent = new HelloWorldService.Client(protocol);
            System.out.println(clent.sayHello(userName));
        } catch (TTransportException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        HelloClientDemo client = new HelloClientDemo();
        client.startClient("shengxl");
    }
}
