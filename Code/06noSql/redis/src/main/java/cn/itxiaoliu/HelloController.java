package cn.itxiaoliu;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @GetMapping("/set")
    public void set(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("name","xiaoliu" );
    }
    @GetMapping("/get")
    public void get(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        System.out.println(ops.get("name"));
    }
}

