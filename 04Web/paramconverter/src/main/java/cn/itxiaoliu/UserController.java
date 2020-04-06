package cn.itxiaoliu;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class UserController {
    @GetMapping("/hello")
    public void hello(Date birth){
        System.out.println(birth);
    }
}
