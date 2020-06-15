package cn.itxiaoliu.mapper;

import org.apache.ibatis.annotations.Mapper;
import cn.itxiaoliu.bean.User;
import cn.itxiaoliu.bean.Role;

import java.util.List;


@Mapper
public interface UserMapper {
    User loadUserByUsername(String username);
    List<Role> getUserRolesById(Integer id);
}
