package jpa.dao;

import jpa.bean.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface BookDao extends JpaRepository<Book,Integer> {
    Book findBookById(Integer id);
    List<Book> findBookByIdGreaterThan(Integer id);
    List<Book> findBookByIdLessThanOrNameContaining(Integer id,String name);
    @Query(value = "select * from t_book where id=(select max(id) from t_book)",nativeQuery = true)
    Book getMaxIdBook();
    @Query(value = "insert into t_book(name,author) values(?1,?2)",nativeQuery = true)
    @Modifying
    @Transactional
    Integer addBook(String name,String author);
    @Query(value = "insert into t_book(name,author) values(:name,:author)",nativeQuery = true)
    @Modifying
    @Transactional
    Integer addBook2(@Param("name") String name, @Param("author")String author);
}
