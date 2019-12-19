package com.mmr.rabbitmq.work;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.TimeoutException;

/**
 *轮询分发 round-robin
 */
public class Recv1 {
    private  static final String QUEUE_NAME="test_work_queue";

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
                 System.out.println("Recv1 work msg ="+msgString);
                 try {
                     Thread.sleep(2002);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }finally {
                     System.out.println("[1] done ");
                 }
             }
         };
        //监听队列（消费者添加到监听）
        boolean autoAck=true;
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);

    }


}
