package cn.itxiaoliu.service;

import cn.itxiaoliu.bean.User;
import cn.itxiaoliu.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import cn.itxiaoliu.bean.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    @Autowired
    UserMapper  userMapper;
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user=userMapper.loadUserByUsername(s);
        if(user==null){
            throw new UsernameNotFoundException("用户不存在！");
        }
        user.setRoles(userMapper.getRolesById(user.getId()));
        return user;
    }
}
