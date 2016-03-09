package com.xsecret.chat.server;

import com.xsecret.chat.HelloWorldService;
import org.apache.thrift.TException;

/**
 * Created by jhss on 16/3/9.
 */
public class HelloWorldImpl implements HelloWorldService.Iface {
    @Override
    public String sayHello(String username) throws TException {
        return "hi " + username;
    }
}
