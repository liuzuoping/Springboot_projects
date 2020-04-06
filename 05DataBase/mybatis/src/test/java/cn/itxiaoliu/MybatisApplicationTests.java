package cn.itxiaoliu;

import cn.itxiaoliu.bean.User;
import cn.itxiaoliu.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MybatisApplicationTests {

    /**
     *
     */
    @Autowired
    UserMapper userMapper;
    @Test
    public void contextLoads() {
        List<User> allUser=userMapper.getAllUser();
        System.out.println(allUser);
    }

}
