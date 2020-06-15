# SpringBoot自定义欢迎页，favicon以及除去自动化配置

### SpringBoot自定义欢迎页

spring boot中的欢迎也可以是静态的，定义在resources文件夹下的static目录下，可以是动态的，定义在templates目录下

例如在启动类新建HelloController

```java
@Controller
public class HelloController {
    @GetMapping("/index")
    public String hello(){
        return "index";
    }
}

```

在templates下新建index.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>hello thymeleaf!</h1>
</body>
</html>
```

浏览器访问http://localhost:8080/hello

返回hello thymeleaf！

### SpringBoot自定义favicon

favicon在线制作网站 https://tool.lu/favicon/ 

制作好后将其复制至static目录或者resources目录下便可生效

### SpringBoot除去自动化配置

两种方法除去自动化配置

1、在启动类上添加

```java
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
```

2、在application.properties添加

```xml
spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
```

这样便除去了ErrorMvcAutoConfiguration自动化配置

