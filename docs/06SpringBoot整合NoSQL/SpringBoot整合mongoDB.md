# SpringBoot整合mongoDB

创建一个SpringBoot项目，pom.xml中引入的依赖如下

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

在application.properties中引入mongoDB的配置

```xml
spring.data.mongodb.host=127.0.0.1
spring.data.mongodb.port=27017
spring.data.mongodb.database=test
```

在启动类父文件夹下创建bean包，在其下创建Book实体类

```java
package cn.itxiaoliu.bean;

public class Book {
    private Integer id;
    private String name;
    private String author;
//setter&getter&toString()
```

在启动类父文件夹下创建dao包，在其下创建BookDao.java

```java
public interface BookDao extends MongoRepository<Book,Integer>{
    List<Book> findBookByNameContaining(String name);
}
```

### 在测试类中进行测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoApplicationTests {

    @Autowired
    BookDao bookDao;
    @Test
    public void contextLoads() {
        Book book=new Book();
        book.setAuthor("小刘");
        book.setName("java");
        book.setId(4);
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
        book.setId(5);
        book.setName("python");
        book.setAuthor("老刘");
        mongoTemplate.insert(book);
        List<Book> list = mongoTemplate.findAll(Book.class);
        System.out.println(list);
    }

}

```

目录结构如下

![1588235626443](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588235626443.png)

操作完成后mongoDB中数据如下

![1588235674719](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588235674719.png)