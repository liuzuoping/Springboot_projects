package cn.itxiaoliu.dao1;

import cn.itxiaoliu.bean.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao1 extends JpaRepository<Book,Integer> {
}
