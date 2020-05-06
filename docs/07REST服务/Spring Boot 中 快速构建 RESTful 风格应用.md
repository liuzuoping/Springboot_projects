# Spring Boot 中 10 行代码构建 RESTful 风格应用

RESTful ，到现在相信已经没人不知道这个东西了吧！关于 RESTful 的概念，我这里就不做过多介绍了，传统的 Struts 对 RESTful 支持不够友好 ，但是 SpringMVC 对于 RESTful 提供了很好的支持，常见的相关注解有

```xml
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

首先创建一个 Spring Boot 工程，引入 `Web` 、 `Jpa` 、 `MySQL` 、`Rest Repositories` 依赖：

[![img](http://www.javaboy.org/images/boot/16-1.png)](http://www.javaboy.org/images/boot/16-1.png)

创建完成后，还需要锁定 MySQL 驱动的版本以及加入 Druid 数据库连接池，完整依赖如下：

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-rest</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
```

## 配置数据库

主要配置两个，一个是数据库，另一个是 Jpa：

```xml
spring.datasource.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.username=xiaoliu
spring.datasource.password=960614abcd
spring.datasource.url=jdbc:mysql:///xiaoliu?serverTimezone=UTC

spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL57Dialect
spring.jpa.show-sql=true
spring.jpa.database=mysql
spring.jpa.database-platform=mysql
spring.jpa.hibernate.ddl-auto=update
```

这里的配置，和 Jpa 中的基本一致。

前面五行配置了数据库的基本信息，包括数据库连接池、数据库用户名、数据库密码、数据库连接地址以及数据库驱动名称。

接下来的五行配置了 JPA 的基本信息，分别表示生成 SQL 的方言、打印出生成的 SQL 、每次启动项目时根据实际情况选择是否更新表、数据库平台是 MySQL。

## 构建实体类

```java
@Entity(name = "t_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

此时，我们就可以启动项目进行测试了，使用 POSTMAN 来测试（大家也可以自行选择趁手的 HTTP 请求工具）。

此时我们的项目已经默认具备了一些接口，我们分别来看：

#### 根据 id 查询接口

- http://127.0.0.1:8080/books/{id}

这个接口表示根据 id 查询某一本书：

[![img](http://www.javaboy.org/images/boot/16-2.png)](http://www.javaboy.org/images/boot/16-2.png)

#### 分页查询

- http://127.0.0.1:8080/books

这是一个批量查询接口，默认请求路径是类名首字母小写，并且再加一个 s 后缀。这个接口实际上是一个分页查询接口，没有传参数，表示查询第一页，每页 20 条数据。

[![img](http://www.javaboy.org/images/boot/16-3.png)](http://www.javaboy.org/images/boot/16-3.png)

查询结果中，除了该有的数据之外，也包含了分页数据：

[![img](http://www.javaboy.org/images/boot/16-4.png)](http://www.javaboy.org/images/boot/16-4.png)

分页数据中：

1. size 表示每页查询记录数
2. totalElements 表示总记录数
3. totalPages 表示总页数
4. number 表示当前页数，从0开始计

如果要分页或者排序查询，可以使用 _links 中的链接。`http://127.0.0.1:8080/books?page=1&size=3&sort=id,desc` 。

[![img](http://www.javaboy.org/images/boot/16-5.png)](http://www.javaboy.org/images/boot/16-5.png)

#### 添加

也可以添加数据，添加是 POST 请求，数据通过 JSON 的形式传递，如下：

[![img](http://www.javaboy.org/images/boot/16-6.png)](http://www.javaboy.org/images/boot/16-6.png)

添加成功之后，默认会返回添加成功的数据。

#### 修改

修改接口默认也是存在的，数据修改请求是一个 PUT 请求，修改的参数也是通过 JSON 的形式传递：

[![img](http://www.javaboy.org/images/boot/16-7.png)](http://www.javaboy.org/images/boot/16-7.png)

默认情况下，修改成功后，会返回修改成功的数据。

#### 删除

当然也可以通过 DELETE 请求根据 id 删除数据：

[![img](http://www.javaboy.org/images/boot/16-8.png)](http://www.javaboy.org/images/boot/16-8.png)

删除成功后，是没有返回值的。

不需要几行代码，一个基本的增删改查就有了。

这些都是默认的配置，这些默认的配置实际上都是在 JpaRepository 的基础上实现的，实际项目中，我们还可以对这些功能进行定制。

## 查询定制

最广泛的定制，就是查询，因为增删改操作的变化不像查询这么丰富。对于查询的定制，非常容易，只需要提供相关的方法即可。例如根据作者查询书籍：

```java
@CrossOrigin
//@RepositoryRestResource(path = "bs",collectionResourceRel = "bs",itemResourceRel = "b")
public interface BookDao extends JpaRepository<Book,Integer> {
    //@RestResource(path = "byname",rel = "findbyname")
    List<Book> findBookByNameContaining(@Param("name") String name);
}
```

注意，方法的定义，参数要有 @Param 注解。

定制完成后，重启项目，此时就多了一个查询接口，开发者可以通过 http://localhost:8080/books/search 来查看和 book 相关的自定义接口都有哪些：

[![img](http://www.javaboy.org/images/boot/16-9.png)](http://www.javaboy.org/images/boot/16-9.png)

查询结果表示，只有一个自定义接口，接口名就是方法名，而且查询结果还给出了接口调用的示例。我们来尝试调用一下自己定义的查询接口：

[![img](http://www.javaboy.org/images/boot/16-10.png)](http://www.javaboy.org/images/boot/16-10.png)

开发者可以根据实际情况，在 BookRepository 中定义任意多个查询方法，查询方法的定义规则和 Jpa 中一模一样（不懂 Jpa 的小伙伴，可以参考[干货|一文读懂 Spring Data Jpa！](https://mp.weixin.qq.com/s/Fg5ssXuvabZwEfRMKfpY9Q)，或者在松哥个人网站 [www.javaboy.org](http://www.javaboy.org/) 上搜索 JPA，有相关教程参考）。但是，这样有一个缺陷，就是 Jpa 中方法名太长，因此，如果不想使用方法名作为接口名，则可以自定义接口名：

```java
@CrossOrigin
//@RepositoryRestResource(path = "bs",collectionResourceRel = "bs",itemResourceRel = "b")
public interface BookDao extends JpaRepository<Book,Integer> {
    @RestResource(path = "byname",rel = "findbyname")
    List<Book> findBookByNameContaining(@Param("name") String name);
}
```

@RestResource 注解中，两个参数的含义：

- rel 表示接口查询中，这个方法的 key
- path 表示请求路径

这样定义完成后，表示接口名为 byauthor ，重启项目，继续查询接口：

[![img](http://www.javaboy.org/images/boot/16-11.png)](http://www.javaboy.org/images/boot/16-11.png)

除了 `rel` 和 `path` 两个属性之外，`@RestResource` 中还有一个属性，`exported` 表示是否暴露接口，默认为 `true` ，表示暴露接口，即方法可以在前端调用，如果仅仅只是想定义一个方法，不需要在前端调用这个方法，可以设置 `exported` 属性为 `false` 。

如果不想暴露官方定义好的方法，例如根据 `id` 删除数据，只需要在自定义接口中重写该方法，然后在该方法上加 `@RestResource` 注解并且配置相关属性即可。

```java
@CrossOrigin
//@RepositoryRestResource(path = "bs",collectionResourceRel = "bs",itemResourceRel = "b")
public interface BookDao extends JpaRepository<Book,Integer> {
    @RestResource(path = "byname",rel = "findbyname")
    List<Book> findBookByNameContaining(@Param("name") String name);
}
```

另外生成的 JSON 字符串中的集合名和单个 `item` 的名字都是可以自定义的：

```java
@CrossOrigin
@RepositoryRestResource(path = "bs",collectionResourceRel = "bs",itemResourceRel = "b")
public interface BookDao extends JpaRepository<Book,Integer> {
    @RestResource(path = "byname",rel = "findbyname")
    List<Book> findBookByNameContaining(@Param("name") String name);
}
```

`path` 属性表示请求路径，请求路径默认是类名首字母小写+s，可以在这里自己重新定义。

[![img](http://www.javaboy.org/images/boot/16-12.png)](http://www.javaboy.org/images/boot/16-12.png)

## 其他配置

最后，也可以在 application.properties 中配置 REST 基本参数：

```
spring.data.rest.base-path=/apispring.data.rest.sort-param-name=sort
spring.data.rest.page-param-name=pagespring.data.rest.limit-param-name=size
spring.data.rest.max-page-size=20spring.data.rest.default-page-size=0
spring.data.rest.return-body-on-update=truespring.data.rest.return-body-on-create=true
```

也可以在启动类父文件夹中新建config包，在其下新建RestConfig

```java
@Configuration
public class ResrConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
        config.setDefaultPageSize(2)
                .setBasePath("/xiaoliu");
    }
}

```

配置含义，从上往下，依次是：

1. 给所有的接口添加统一的前缀
2. 配置排序参数的 key ，默认是 sort
3. 配置分页查询时页码的 key，默认是 page
4. 配置分页查询时每页查询页数的 key，默认是size
5. 配置每页最大查询记录数，默认是 20 条
6. 分页查询时默认的页码
7. 更新成功时是否返回更新记录
8. 添加成功时是否返回添加记录

