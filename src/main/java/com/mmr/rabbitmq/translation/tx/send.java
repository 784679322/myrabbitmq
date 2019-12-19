package com.mmr.rabbitmq.translation.tx;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * 消息确认机制
 * 事务模式
 * 吞吐量降低
 *
 *
 */
public class send {
    private  static final String QUEUE_NAME="test_queue_tx";

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection=ConnectionUtils.getConnection();
        Channel channel=connection.createChannel();

        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        String msg="商品。。。test_queue_tx。。。msg";

       try{
                channel.txSelect();
                channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
                int a=1/0;
           channel.txCommit();
                System.out.println("-- send tx msg :" +msg);
           }catch(Exception e){
               channel.txRollback();
               System.out.println("异常");
           }finally {
               channel.close();
               connection.close();
           }

    }
}
