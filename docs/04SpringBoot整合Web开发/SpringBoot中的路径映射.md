## SpringBoot中的路径映射

在启动类父文件夹中新建WebMvcConfig.java

文件结构如下

![1592212248475](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592212248475.png)

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/xiaoliu").setViewName("hello");
    }
}
```

在templates包下新建hello.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
<h1>hello spring boot!</h1>
</body>
</html>
```

浏览器访问

http://localhost:8080/xiaoliu

![1588142994396](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588142994396.png)