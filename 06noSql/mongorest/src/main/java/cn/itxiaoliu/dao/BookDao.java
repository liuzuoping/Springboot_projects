package cn.itxiaoliu.dao;

import cn.itxiaoliu.bean.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookDao extends MongoRepository<Book,Integer> {

}
