package cn.itxiaoliu.jdbctemplate;


import cn.itxiaoliu.jdbctemplate.bean.User;
import cn.itxiaoliu.jdbctemplate.service.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JdbctemplateApplicationTests {

    @Autowired
    UserService userService;
    @Test
    public void contextLoads() {
        User user=new User();
        user.setAddress("www.xiaoliu.com");
        user.setUsername("xiaoliu");
        userService.addUser(user);
    }
    @Test
    public void test1(){
        User user=new User();
        user.setId(1);
        user.setUsername("javaboy");
        userService.updateUsernameById(user);
    }
    @Test
    public void test2(){
        userService.deleteUserById(1);
    }
    @Test
    public void test3(){
        List<User> allUsers=userService.getAllUsers();
        System.out.println(allUsers);
    }
    @Test
    public void test4(){
        List<User> allUsers=userService.getAllUsers2();
        System.out.println(allUsers);
    }
}
