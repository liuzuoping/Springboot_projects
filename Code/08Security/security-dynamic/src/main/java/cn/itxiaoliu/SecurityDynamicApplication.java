package cn.itxiaoliu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan(basePackages = "cn.itxiaoliu.mapper")
public class SecurityDynamicApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityDynamicApplication.class, args);
    }

}
