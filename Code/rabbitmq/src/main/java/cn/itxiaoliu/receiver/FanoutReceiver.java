package cn.itxiaoliu.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class FanoutReceiver {
    @RabbitListener(queues = "queue-one")
    public void handler1(String msg){
        System.out.println("FanoutReceiver:handler1:"+msg);
    }
    @RabbitListener(queues = "queue-two")
    public void handler2(String msg){
        System.out.println("FanoutReceiver:handler2:"+msg);
    }
}
