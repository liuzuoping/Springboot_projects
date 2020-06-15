package cn.itxiaoliu;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Cacheable(cacheNames = "mycache")
    public User getUserById(Integer id){
        User user=new User();
        user.setId(id);
        System.out.println("getUserById>>>"+id);
        return user;
    }
}
