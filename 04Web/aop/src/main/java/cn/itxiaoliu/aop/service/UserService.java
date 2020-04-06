package cn.itxiaoliu.aop.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public String getUsernameById(Integer id){
        System.out.println("getUsernameById");
        return "xiaoliu";
    }
    public void deleteUserById(Integer id){
        System.out.println("deleteUserById");
    }
}
