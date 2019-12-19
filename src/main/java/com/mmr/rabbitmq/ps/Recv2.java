package com.mmr.rabbitmq.ps;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Recv2 {
    private  static final String QUEUE_NAME="test_queue_sms";
    private  static final String EXCHANGE_NAME="test_exchange_fanout";
    @SuppressWarnings("deprecation")
    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        //获取链接
        Connection connection= ConnectionUtils.getConnection();
        //获取通道
        final Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //绑定队列到交换机
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");

        channel.basicQos(1);

        //定义消费者
        DefaultConsumer consumer=  new DefaultConsumer(channel){
            //获取到达的消息
             @Override
             public void handleDelivery(String consumerTag, Envelope envelope,
                                        AMQP.BasicProperties properties, byte[] body) throws IOException {
                 super.handleDelivery(consumerTag, envelope, properties, body);
                 String msgString  =new String(body,"utf-8");
                 System.out.println("Recv2 work msg ="+msgString);
                 try {
                     Thread.sleep(1000);
                 } catch (InterruptedException e) {
                     e.printStackTrace();
                 }finally {
                     System.out.println("[2] done ");
                     channel.basicAck(envelope.getDeliveryTag(),false);
                 }
             }
         };

        boolean autoAck=false;//自动应答改成false
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);

    }


}
