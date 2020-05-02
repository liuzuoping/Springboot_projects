package cn.itxiaoliu.receiver;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DirectReceiver {
    @RabbitListener(queues = "hello.xiaoliu")
    public void handler1(String msg){
        System.out.println("handler1>>>"+msg);
    }
}
