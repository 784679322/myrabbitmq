package com.mmr.rabbitmq.translation.confirm;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 *
 * confirm模式  批量模式
 *
 */
public class send2 {
    private  static final String QUEUE_NAME="test_queue_confirm2";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection=ConnectionUtils.getConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //生产者调用confiremselect 将chanel设置成confrim模式
        channel.confirmSelect();

        String msg="confirm1。。。。。。msg";
        for(int i=0;i<10;i++){
            channel.basicPublish("",QUEUE_NAME,null,msg.getBytes());
        }

        if(!channel.waitForConfirms()){
            System.out.println("error");
        }else{
            System.out.println("-- send test_queue_confirm1 msg :" +msg);
        }

       channel.close();
       connection.close();


    }
}
