package cn.itxiaoliu.job;

import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class MyFirstJob {
    public void sayhello(){
        System.out.println("first job say hello"+new Date());
    }
}
