package cn.itxiaoliu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Service
public class HelloService {
    public String sayHello(){
        return "hello javassm!";
    }
}

