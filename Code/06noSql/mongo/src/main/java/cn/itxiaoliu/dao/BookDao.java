package cn.itxiaoliu.dao;

import cn.itxiaoliu.bean.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BookDao extends MongoRepository<Book,Integer>{
    List<Book> findBookByNameContaining(String name);
}

