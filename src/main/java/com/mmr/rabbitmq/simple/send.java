package com.mmr.rabbitmq.simple;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;

import java.util.concurrent.TimeoutException;

public class send {
    private  static final String QUEUE_NAME="test_simple_queue";
    private  static final String QUEUE_NAME2="test_simple_queue2";
    public static void main(String[] args) throws IOException, TimeoutException {
        //获取链接
        Connection connection=ConnectionUtils.getConnection();
        //获取通道
        Channel channel=connection.createChannel();
        //创建队列申明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg="hello simple";

        channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());


        System.out.println("-- send msg :" +msg);
        channel.close();
        connection.close();

    }
}
