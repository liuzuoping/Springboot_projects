### spring boot中的属性注入

创建一个spring boot项目

在启动类父文件夹上创建Book类

```java
package cn.itxiaoliu;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Component
@PropertySource("classpath:book.properties")
@ConfigurationProperties(prefix = "book")
public class Book {
    //@Value("${book.id}")
    private Long id;
    //@Value("${book.name}")
    private String name;
    //@Value("${book.author}")
    private String author;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}

```

在resorces文件夹下创建book.properties文件

```
book.id=99
book.author=罗贯中
book.name=三国演义
```

在测试类中进行测试

```java
package cn.itxiaoliu;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    Book book;
    @Test
    public void contextLoads() {
        System.out.println(book);
    }

}

```

![1588039335075](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588039335075.png)