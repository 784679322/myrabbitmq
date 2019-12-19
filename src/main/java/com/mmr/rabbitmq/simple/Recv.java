package com.mmr.rabbitmq.simple;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者消费消息
 * 耦合性高，生产者一一对应消费者
 * 如果想要多个消费者消费队列中的信息，就不行了
 *
 * 队列名变更，这时需要同时改变消费者名字
 */
public class Recv {
    private  static final String QUEUE_NAME="test_simple_queue";

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection= ConnectionUtils.getConnection();
        //获取通道
        Channel channel=connection.createChannel();
        //创建队列申明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        //定义消费者
        DefaultConsumer consumer=  new DefaultConsumer(channel){
            //获取到达的消息
             @Override
             public void handleDelivery(String consumerTag, Envelope envelope,
                                        AMQP.BasicProperties properties, byte[] body) throws IOException {
                 super.handleDelivery(consumerTag, envelope, properties, body);
                 String msgString  =new String(body,"utf-8");
                 System.out.println("Recv1 msg ="+msgString);
             }
         };
        //监听队列（消费者添加到监听）
        channel.basicConsume(QUEUE_NAME,true,consumer);

    }

    private static void  oldapi() throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection= ConnectionUtils.getConnection();
        //获取通道
        Channel channel=connection.createChannel();
        //定义队列的消费者
        QueueingConsumer consumer=   new QueueingConsumer(channel);
        //监听队列
        channel.basicConsume(QUEUE_NAME,true,consumer);
        while(true){
            QueueingConsumer.Delivery delivery= consumer.nextDelivery();
            String msgString  =new String(delivery.getBody());
            System.out.println("Recv1 msg ="+msgString);
        }
    }
}
