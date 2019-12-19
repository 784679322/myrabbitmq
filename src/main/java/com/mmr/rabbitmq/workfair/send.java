package com.mmr.rabbitmq.workfair;

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
//        channel.queueDeclare(QUEUE_NAME,true,false,false,null);持久化
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        /**
         * 每个消费者发送确认消息之前，消息队列不发送下一个消息到消费者，一次只处理一个消息
         *
         * 限制发送给同一个消费者不得超过一条消息
         */
        int prefetCount=1;
        channel.basicQos(prefetCount);

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
