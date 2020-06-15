package cn.itxiaoliu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "cn.itxiaoliu")
public class Shiro1Application {

    public static void main(String[] args) {
        SpringApplication.run(Shiro1Application.class, args);
    }

}
