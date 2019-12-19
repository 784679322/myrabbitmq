package com.mmr.rabbitmq.topic;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Recv2 {
    private  static final String QUEUE_NAME="test_queue_topic_2";
    private  static final String EXCHANGE_NAME="test_exchange_topic";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection= ConnectionUtils.getConnection();
        final Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String routingKey= "goods.#";
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,routingKey);

        channel.basicQos(1);

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
                     Thread.sleep(2000);
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
        channel.basicConsume(QUEUE_NAME,autoAck,consumer);

    }


}
