package com.mmr.rabbitmq.ps;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 *          --queue --c1
 * p--->x   --queue --c2
 *          --queue --c3
 */
public class send {
    private  static final String EXCHANGE_NAME="test_exchange_fanout";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection=ConnectionUtils.getConnection();
        //获取通道
        Channel channel=connection.createChannel();
        //申明交换机
        channel.exchangeDeclare(EXCHANGE_NAME,"fanout");

        String msg="hello ps";
        channel.basicPublish(EXCHANGE_NAME,"",null,msg.getBytes());
        System.out.println("-- send ps msg :" +msg);


        channel.close();
        connection.close();

    }
}
