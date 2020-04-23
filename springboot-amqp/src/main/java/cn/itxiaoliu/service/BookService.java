package cn.itxiaoliu.service;

import cn.itxiaoliu.bean.Book;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    @RabbitListener(queues = "itxiaoliu.news")
    public void receive(Book book){
        System.out.println("收到信息"+book);
    }
    @RabbitListener(queues = "itxiaoliu")
    public void receive02(Message message){
        System.out.println(message.getBody());
        System.out.println(message.getMessageProperties());
    }
}
