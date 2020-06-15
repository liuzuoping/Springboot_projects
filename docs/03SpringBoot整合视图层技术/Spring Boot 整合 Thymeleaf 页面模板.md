#  Spring Boot 整合 Thymeleaf 页面模板

虽然现在慢慢在流行前后端分离开发，一些公司在做前后端不分的开发，而在前后端不分的开发中，我们就会需要后端页面模板（实际上，即使前后端分离，也会在一些场景下需要使用页面模板，例如邮件发送模板）。



早期的 Spring Boot 中还支持使用 Velocity 作为页面模板，现在的 Spring Boot 中已经不支持 Velocity 了，页面模板主要支持 Thymeleaf 和 Freemarker ，当然，作为 Java 最最基本的页面模板 Jsp ，Spring Boot 也是支持的，只是使用比较麻烦。

 

今天我们主要来看看 Thymeleaf 在 Spring Boot 中的整合！

# Thymeleaf 简介

Thymeleaf 是新一代 Java 模板引擎，它类似于 Velocity、FreeMarker 等传统 Java 模板引擎，但是与传统 Java 模板引擎不同的是，Thymeleaf 支持 HTML 原型。

它既可以让前端工程师在浏览器中直接打开查看样式，也可以让后端工程师结合真实数据查看显示效果，同时，SpringBoot 提供了 Thymeleaf 自动化配置解决方案，因此在 SpringBoot 中使用 Thymeleaf 非常方便。

事实上， Thymeleaf 除了展示基本的 HTML ，进行页面渲染之外，也可以作为一个 HTML 片段进行渲染，例如我们在做邮件发送时，可以使用 Thymeleaf 作为邮件发送模板。

另外，由于 Thymeleaf 模板后缀为 `.html`，可以直接被浏览器打开，因此，预览时非常方便。

# 整合

- 创建项目

Spring Boot 中整合 Thymeleaf 非常容易，只需要创建项目时添加 Thymeleaf 即可：

[![img](http://www.javaboy.org/images/boot/18-1.png)](http://www.javaboy.org/images/boot/18-1.png)

创建完成后，pom.xml 依赖如下：

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-thymeleaf</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

## 文件结构如下

![1592191906385](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592191906385.png)

- 创建Book类

  ```java
  public class Book {
      private Integer id;
      private String name;
      private String author;
      private Double price;
  //getter&setter&toString()
  ```
  
  
  
- 创建 BookController

```java
@Controller
public class BookController {
    @GetMapping("/book")
    public String book(Model model){
        List<Book> bookList=new ArrayList<>();
        for (int i = 0; i <10 ; i++) {
            Book book=new Book();
            book.setId(i);
            book.setName("三国演义:"+i);
            book.setAuthor("罗贯中:"+i);
            book.setPrice(30.0);
            bookList.add(book);
        }
        model.addAttribute("books", bookList);
        return "book";
    }
}
```

在 BookController` 中返回逻辑视图名+数据，逻辑视图名为book ，意思我们需要在 `resources/templates` 目录下提供一个名为 book.html` 的 `Thymeleaf` 模板文件。

- 创建 Thymeleaf

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8"/>
    <title>Title</title>
</head>
<body>
<table border="1">
    <tr>
        <td>图书编号</td>
        <td>图书名称</td>
        <td>图书作者</td>
        <td>图书价格</td>
    </tr>
    <tr th:each="book:${books}">
        <td th:text="${book.id}"></td>
        <td th:text="${book.name}"></td>
        <td th:text="${book.author}"></td>
        <td th:text="${book.price}"></td>
    </tr>
</table>
</body>
</html>
```

在 `Thymeleaf` 中，通过 `th:each` 指令来遍历一个集合，数据的展示通过 `th:text` 指令来实现，

注意 book.html` 最上面要引入 `thymeleaf名称空间。

配置完成后，就可以启动项目了，访问 /book接口，就能看到集合中的数据了：

![1588057442730](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588057442730.png)

关于 Thymeleaf 这个页面模板本身更多的用法，大家可以参考 Thymeleaf 的文档：[https://www.thymeleaf.org](https://www.thymeleaf.org/)。

