package cn.ixiaoliu.cacheredis;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "c1")
public class UserService {
    //@Cacheable(cacheNames = "c1",keyGenerator = "myKeyGenerator")
    @Cacheable
    public User getUserById(Integer id){
        System.out.println("getUserById>>>"+id);
        User user=new User();
        user.setId(id);
        return user;
    }
    @CacheEvict
    public void deleteUserById(Integer id){
        System.out.println("deleteUserById>>>"+id);
    }
//    @CachePut(cacheNames = "c1")
//    public User updateUserById(Integer id){
//        User user=new User();
//        user.setId(id);
//        user.setUsername("xiaoliu");
//        return user;
//    }
    @CachePut(key = "#user.id")
    public User updateUserById(User user){
        return user;
    }
}
