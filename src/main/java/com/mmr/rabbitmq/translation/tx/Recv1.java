package com.mmr.rabbitmq.translation.tx;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Recv1 {
    private  static final String QUEUE_NAME="test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtils.getConnection();
        final Channel channel=connection.createChannel();

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
             }
         };

        channel.basicConsume(QUEUE_NAME,true,consumer);

    }


}
