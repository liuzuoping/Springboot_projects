package cn.itxiaoliu.dao2;

import cn.itxiaoliu.bean.Book;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookDao2 extends JpaRepository<Book,Integer> {
}

