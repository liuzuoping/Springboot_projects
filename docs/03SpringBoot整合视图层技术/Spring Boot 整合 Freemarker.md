# Spring Boot 整合 Freemarker

# Freemarker 简介

这是一个相当老牌的开源的免费的模版引擎。通过 Freemarker 模版，我们可以将数据渲染成 HTML 网页、电子邮件、配置文件以及源代码等。Freemarker 不是面向最终用户的，而是一个 Java 类库，我们可以将之作为一个普通的组件嵌入到我们的产品中。

来看一张来自 Freemarker 官网的图片：

[![img](http://www.javaboy.org/images/boot/22-1.png)](http://www.javaboy.org/images/boot/22-1.png)

可以看到，Freemarker 可以将模版和数据渲染成 HTML 。

Freemarker 模版后缀为 `.ftl`(FreeMarker Template Language)。FTL 是一种简单的、专用的语言，它不是像 Java 那样成熟的编程语言。在模板中，你可以专注于如何展现数据， 而在模板之外可以专注于要展示什么数据。

好了，这是一个简单的介绍，接下来我们来看看 Freemarker 和 Spring Boot 的一个整合操作。

# 实践

在 SSM 中整合 Freemarker ，所有的配置文件加起来，前前后后大约在 50 行左右，Spring Boot 中要几行配置呢？ 0 行！

## 1.创建工程

首先创建一个 Spring Boot 工程，引入 Freemarker 依赖，如下图：

[![img](http://www.javaboy.org/images/boot/22-2.png)](http://www.javaboy.org/images/boot/22-2.png)

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-freemarker</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

## 2.创建类

文件目录如下

![1592190772066](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592190772066.png)

首先我们来创建一个 User 类，如下：

```java
public class User {
    private Long id;
    private String username;
    private String address;
    private Integer gender;
//getter&setter&toString()
```

再来创建 `UserController`：

```java
@Controller
public class UserController {
    @GetMapping("/user")
    public String user(Model model){
        List<User> users=new ArrayList<>();
        Random random=new Random();
        for (int i = 0; i <10 ; i++) {
            User user=new User();
            user.setId((long) i);
            user.setUsername("xiaoliu>>>"+i);
            user.setAddress("www.xiaoliu.cn>>>"+i);
            user.setGender(random.nextInt(3));
            users.add(user);
        }
        model.addAttribute("users",users);
        model.addAttribute("hello","<h1>hello</h1>" );
        model.addAttribute("world","<h1>world</h1>" );
        return "user";
    }
}
```

最后在 freemarker 中渲染数据：

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<#include './header.ftl'>
${hello}
${world}
<table border="1">
    <tr>
        <td>编号</td>
        <td>用户名</td>
        <td>用户地址</td>
        <td>性别</td>
    </tr>
    <#list users as u>
    <#--<#if u.id=4>-->
        <#--<#break>-->
    <#--</#if>-->
        <tr>
            <td>${u.id}</td>
            <td>${u.username}</td>
            <td>${u.address}</td>
            <td>
                <#--<#if u.gender==0>-->
                    <#--男-->
                <#--<#elseif u.gender==1>-->
                    <#--女-->
                <#--<#else>-->
                    <#--未知-->
                <#--</#if>-->
                <#switch u.gender>
                    <#case 0>男<#break>
                    <#case 1>女<#break>
                    <#default>未知
                </#switch>
            </td>
        </tr>
    </#list>
</table>
</body>
</html>
```

运行效果如下：

![1588055895866](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588055895866.png)

# 其他配置

如果我们要修改模版文件位置等，可以在 application.properties 中进行配置：

```xml
spring.freemarker.template-loader-path=classpath:/xiaoliu

spring.freemarker.charset=utf-8

spring.freemarker.content-type=text/html

spring.thymeleaf.cache=false
spring.freemarker.suffix=.ftl
```

配置文件按照顺序依次解释如下：

1. HttpServletRequest的属性是否可以覆盖controller中model的同名项
2. HttpSession的属性是否可以覆盖controller中model的同名项
3. 是否开启缓存
4. 模板文件编码
5. 是否检查模板位置
6. Content-Type的值
7. 是否将HttpServletRequest中的属性添加到Model中
8. 是否将HttpSession中的属性添加到Model中
9. 模板文件后缀
10. 模板文件位置

好了，整合完成之后，Freemarker 的更多用法，就和在 SSM 中使用 Freemarker 一样了