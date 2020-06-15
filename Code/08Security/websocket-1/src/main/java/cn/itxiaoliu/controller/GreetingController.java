package cn.itxiaoliu.controller;

import cn.itxiaoliu.bean.Chat;
import cn.itxiaoliu.bean.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
public class GreetingController {
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Message greeting(Message message){
//        return message;
//    }
    @MessageMapping("/hello")
    public void greeting(Message message){
        simpMessagingTemplate.convertAndSend("/topic/greetings",message);
    }
    @MessageMapping("/chat")
    public void chat(Principal principal, Chat chat){
        chat.setFrom(principal.getName());
        simpMessagingTemplate.convertAndSendToUser(chat.getTo(),"/queue/chat" ,chat );
    }
}
