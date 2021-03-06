package cn.itxiaoliu.controller;

import cn.itxiaoliu.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Controller
public class UserController {
    @GetMapping("/user")
    public String user(Model model){
        List<User> users=new ArrayList<>();
        Random random=new Random();
        for (int i = 0; i <10 ; i++) {
            User user=new User();
            user.setId((long) i);
            user.setUsername("xiaoliu>>>"+i);
            user.setAddress("www.xiaoliu.cn>>>"+i);
            user.setGender(random.nextInt(3));
            users.add(user);
        }
        model.addAttribute("users",users);
        model.addAttribute("hello","<h1>hello</h1>" );
        model.addAttribute("world","<h1>world</h1>" );
        return "user";
    }
}
