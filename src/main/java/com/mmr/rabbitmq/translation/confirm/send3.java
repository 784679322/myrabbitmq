package com.mmr.rabbitmq.translation.confirm;

import com.mmr.rabbitmq.util.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

/**
 *
 * confirm模式  批量模式
 *
 */
public class send3 {
    private  static final String QUEUE_NAME="test_queue_confirm3";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Connection connection=ConnectionUtils.getConnection();
        Channel channel=connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);

        //生产者调用confiremselect 将chanel设置成confrim模式
        channel.confirmSelect();
        //未确认的信息标识
        final SortedSet<Long> confirmSet= Collections.synchronizedSortedSet(new TreeSet<Long>());

        //通道添加监听
        channel.addConfirmListener(new ConfirmListener() {
            //回执成功
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                if(multiple){//多条
                    System.out.println("--handleAck-----multiple"+deliveryTag);
                    confirmSet.headSet(deliveryTag+1).clear();//批量移除当前之前的tag
                }else{//单条
                    System.out.println("--handleAck-----multiple false"+deliveryTag);
                    confirmSet.remove(deliveryTag);
                }
            }
            //失败  处理
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                if(multiple){
                    System.out.println("--handleNack--fail---multiple"+deliveryTag);
                    confirmSet.headSet(deliveryTag+1).clear();
                }else{
                    System.out.println("--handleNack---fail-multiple false"+deliveryTag);
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        String ackString="msg...";

        while (true){
            long setNo=channel.getNextPublishSeqNo();
             channel.basicPublish("",QUEUE_NAME,null,ackString.getBytes());
             confirmSet.add(setNo);
        }


    }
}
