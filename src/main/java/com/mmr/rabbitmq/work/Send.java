package com.mmr.rabbitmq.work;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class send {
    private  static final String QUEUE_NAME="test_work_queue";
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection=ConnectionUtils.getConnection();
        //获取通道
        Channel channel=connection.createChannel();
        //创建队列申明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        for(int i=0;i<50;i++){
            String msg="hello work"+i;
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
            System.out.println("-- send msg :" +msg);
            Thread.sleep(i*20);
        }

        channel.close();
        connection.close();

    }
}
