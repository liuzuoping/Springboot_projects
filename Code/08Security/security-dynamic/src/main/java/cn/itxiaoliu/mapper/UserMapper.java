package cn.itxiaoliu.mapper;

import cn.itxiaoliu.bean.Role;
import cn.itxiaoliu.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

public interface UserMapper {
    List<Role> getRolesById(Integer id);
    User loadUserByUsername(String username);
}
