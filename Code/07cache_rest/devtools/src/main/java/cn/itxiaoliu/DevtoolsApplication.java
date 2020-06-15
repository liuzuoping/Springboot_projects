package cn.itxiaoliu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DevtoolsApplication {

    public static void main(String[] args) {
        //System.setProperties("spring.devtools.restart.enabled","false");
        SpringApplication.run(DevtoolsApplication.class, args);
    }

}
