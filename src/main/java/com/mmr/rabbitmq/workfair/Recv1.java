package com.mmr.rabbitmq.workfair;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *公平分发
 */
public class Recv1 {
    private  static final String QUEUE_NAME="test_work_queue";

    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection= ConnectionUtils.getConnection();
        //获取通道
        final Channel channel=connection.createChannel();
        //创建队列申明
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        int prefetCount=1;
        channel.basicQos(prefetCount);

        //定义消费者
        DefaultConsumer consumer=  new DefaultConsumer(channel){
            //获取到达的消息
             @Override
             public void handleDelivery(String consumerTag, Envelope envelope,
                                        AMQP.BasicProperties properties, byte[] body) throws IOException {
                 super.handleDelivery(consumerTag, envelope, properties, body);
                 String msgString  =new String(body,"utf-8");
                 System.out.println("Recv1 work msg ="+msgString);
                 try {
                     Thread.sleep(2002);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }finally {
                     System.out.println("[1] done ");
                     //手动回执
                     channel.basicAck(envelope.getDeliveryTag(),false);
                 }
             }
         };

        boolean autoAck=false;//自动应答改成false
        //一旦rabbitmq将信息分发给消费者，就会从内存中删除，这种情况下，如果杀死正在执行的消费者，就会丢失正在处理的信息
        //改为手动模式，如果有消费者挂掉，就会发给另一个消费者，rabbitmq支持消息应答，才会删除*内存*中的信息
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);

    }


}
