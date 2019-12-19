package com.mmr.rabbitmq.translation.confirm;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 */
public class Recv3 {
    private  static final String QUEUE_NAME="test_queue_confirm3";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection= ConnectionUtils.getConnection();
        final Channel channel=connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        channel.basicConsume(QUEUE_NAME,true, new DefaultConsumer(channel){
                 @Override
                 public void handleDelivery(String consumerTag, Envelope envelope,
                                            AMQP.BasicProperties properties, byte[] body) throws IOException {
                     super.handleDelivery(consumerTag, envelope, properties, body);
                     String msgString  =new String(body,"utf-8");
                     System.out.println("Recv1 test_queue_confirm1 msg ="+msgString);
                 }
             }
         );



    }


}
