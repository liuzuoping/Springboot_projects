package cn.itxiaoliu.mapper;

import cn.itxiaoliu.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


public interface UserMapper {
    List<User> getAllUser();
}
