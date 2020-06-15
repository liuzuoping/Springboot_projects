package cn.itxiaoliu;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @GetMapping("/hello")
    public String hello(){
        return "hello Shiro!";
    }
    @GetMapping("/login")
    public String login(){
        return "please login";
    }
    @RequestMapping(value="/doLogin",method = {RequestMethod.POST})
    public void doLogin(String username,String password){
        Subject subject= SecurityUtils.getSubject();
        try{
            subject.login(new UsernamePasswordToken(username,password));
            System.out.println("success");
        }catch (AuthenticationException e){
            e.printStackTrace();
            System.out.println("fail>>"+e.getMessage());
        }
    }
}
