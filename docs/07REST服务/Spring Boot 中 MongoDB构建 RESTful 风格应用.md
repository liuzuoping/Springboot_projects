# Spring Boot 中 MongoDB构建 RESTful 风格应用

RESTful ，到现在相信已经没人不知道这个东西了吧！关于 RESTful 的概念，我这里就不做过多介绍了，传统的 Struts 对 RESTful 支持不够友好 ，但是 SpringMVC 对于 RESTful 提供了很好的支持，常见的相关注解有：

```
@RestController
@GetMapping
@PutMapping
@PostMapping
@DeleteMapping
@ResponseBody...
```

这些注解都是和 RESTful 相关的，在移动互联网中，RESTful 得到了非常广泛的使用。RESTful 这个概念提出来很早，但是以前没有移动互联网时，我们做的大部分应用都是前后端不分的，在这种架构的应用中，数据基本上都是在后端渲染好返回给前端展示的，此时 RESTful 在 Web 应用中基本就没用武之地，移动互联网的兴起，让我们一套后台对应多个前端项目，因此前后端分离，RESTful 顺利走上前台。

Spring Boot 继承自 Spring + SpringMVC， SpringMVC 中对于 RESTful 支持的特性在 Spring Boot 中全盘接收，同时，结合 Jpa 和 自动化配置，对于 RESTful 还提供了更多的支持，使得开发者几乎不需要写代码（很少几行），就能快速实现一个 RESTful 风格的增删改查。

# 实战

## 创建工程

首先创建一个 Spring Boot 工程，引入 `Web` 、 `Jpa` 、 `MongoDB 、 `Rest Repositories` 依赖：

![1588254910459](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588254910459.png)

创建完成后，还需要锁定 MySQL 驱动的版本以及加入 Druid 数据库连接池，完整依赖如下：

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-mongodb</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-rest</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

## 配置数据库

主要配置两个，一个是数据库，另一个是 Jpa：

```xml
spring.data.mongodb.host=127.0.0.1
spring.data.mongodb.port=27017
spring.data.mongodb.database=test

```



## 构建实体类

```java
package cn.itxiaoliu.bean;

public class Book {
    private Integer id;
    private String name;
    private String author;

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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

这里一个是配置了一个实体类 Book，另一个则是配置了一个 BookRepository ，项目启动成功后，框架会根据 Book 类的定义，在数据库中自动创建相应的表，BookRepository 接口则是继承自 JpaRepository ，JpaRepository 中自带了一些基本的增删改查方法。

好了，代码写完了。

啥？你好像啥都没写啊？是的，啥都没写，啥都不用写，一个 RESTful 风格的增删改查应用就有了，这就是 Spring Boot 的魅力！

## 测试

此时，我们就可以启动项目进行测试了，使用 POSTMAN 来测试（大家也可以自行选择趁手的 HTTP 请求工具）与jpa中的测试一样。

在启动类父文件夹中添加dao.BookDao.java



```java
package cn.itxiaoliu.dao;

import cn.itxiaoliu.bean.Book;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookDao extends MongoRepository<Book,Integer> {

}

```