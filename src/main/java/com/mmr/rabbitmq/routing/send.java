package com.mmr.rabbitmq.routing;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 *          key1--queue --c1
 * p--->x   key1
 *          key2    --queue --c2
 *          key3
 */
public class send {
    private  static final String EXCHANGE_NAME="test_exchange_direct";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection=ConnectionUtils.getConnection();
        //获取通道
        Channel channel=connection.createChannel();
        //申明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");

        String msg="hello direct";

        String routingKey= " ";
        channel.basicPublish(EXCHANGE_NAME,routingKey,null,msg.getBytes());
        System.out.println("-- send direct msg :" +msg);

        channel.close();
        connection.close();

    }
}
