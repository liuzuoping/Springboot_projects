package json.controller;

import json.bean.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//@Controller
@RestController
public class UserController {
//    @ResponseBody
    @GetMapping("/user")
    public List<User> getAllUser(){
        List<User> users=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            User user=new User();
            user.setAddress("www.javaboy.org>>"+i);
            user.setUsername("javaboy>>"+i);
            user.setId(i);
            user.setBirthday(new Date());
            users.add(user);
        }
        return users;
    }
}
