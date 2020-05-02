package cn.itxiaoliu;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class HelloService {
//    @Scheduled(fixedDelay = 2000)
//    public void fixedDelay(){
//        System.out.println("fixedDelay>>>"+new Date());
//    }
//    @Scheduled(fixedRate = 2000)
//    public void fixedRate(){
//        System.out.println("fixedRate>>>"+new Date());
//    }

//    @Scheduled(cron = "0/5 17 * * * ?")
//    public void cron(){
//        System.out.println("cron>>>"+new Date());
//    }
    @Scheduled(cron = "0/5 * * * * *")
    public void cron() {
        System.out.println(new Date());
    }
}
