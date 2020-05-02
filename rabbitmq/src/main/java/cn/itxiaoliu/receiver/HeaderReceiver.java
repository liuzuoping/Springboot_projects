package cn.itxiaoliu.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class HeaderReceiver {
    @RabbitListener(queues = "name-queue")
    public void handler1(byte[] msg){
        System.out.println("HeaderReceiver:handler1:"+new String(msg,0,msg.length));
    }
    @RabbitListener(queues = "age-queue")
    public void handler2(byte[] msg){
        System.out.println("HeaderReceiver:handler2:"+new String(msg,0,msg.length));
    }
}
