package cn.itxiaoliu.service;

import cn.itxiaoliu.bean.Menu;
import cn.itxiaoliu.mapper.MenuMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    @Autowired
    MenuMapper menuMapper;
    public List<Menu>  getAllMenus(){
       return menuMapper.getAllMenus();
    }
}
