package cn.itxiaoliu;

import cn.itxiaoliu.bean.Book;
import cn.itxiaoliu.dao.BookDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoApplicationTests {

    @Autowired
    BookDao bookDao;
    @Test
    public void contextLoads() {
        Book book=new Book();
        book.setAuthor("曹雪芹");
        book.setName("红楼梦");
        book.setId(2);
        bookDao.insert(book);
    }

    @Test
    public void test1(){
        List<Book> all = bookDao.findAll();
        System.out.println(all);
        List<Book> books = bookDao.findBookByNameContaining("红");
        System.out.println(books);
    }
    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    public void test2(){
        Book book=new Book();
        book.setId(3);
        book.setName("水浒传");
        book.setAuthor("施耐庵");
        mongoTemplate.insert(book);
        List<Book> list = mongoTemplate.findAll(Book.class);
        System.out.println(list);
    }

}
