package com.mmr.rabbitmq.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * 获取Mq的链接
 */
public class ConnectionUtils {


    public static Connection getConnection() throws IOException, TimeoutException {
        //定义一个链接工厂
        ConnectionFactory factory=new ConnectionFactory();

        //设置服务地址
        factory.setHost("127.0.0.1");
        //设置端口
        factory.setPort(5672);
        //设置vhost
        factory.setVirtualHost("/vhost_mmr");
        //用户名
        factory.setUsername("user_mmr");
        //pwd
        factory.setPassword("123");

        return  factory.newConnection();



    }
}
