## @ControllerAdivice的三种用法

### 第一种用法——自定义异常处理

### 第二种用法——处理全局异常

![1588127678015](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588127678015.png)

在启动类夫文件下新建文件HelloController

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(Model model){
        Map<String,Object> map = model.asMap();
        Set<String> keySet=map.keySet();
        for (String key : keySet) {
            System.out.println(key+":"+map.get(key));
        }
        return "hello";
    }
}
```

再新建文件GlobalData

```java
@ControllerAdvice
public class GlobalData {
    @ModelAttribute(value = "info")
    public Map<String,Object> mydata(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","xiaoliu");
        map.put("address","www.liuzuoping.com");
        return map;
    }
}
```

**访问localhost:8080/hello**

**程序返回info:{address=www.liuzuoping.com, name=xiaoliu}**



### 第三种用法——请求参数预处理

新建Book类与Author类

```java
public class Book {
    private String name;
    private Double price;
//getter&setter&toString()
```



```java
public class Author {
    private String name;
    private Integer age;
//getter&setter&toString()
```

再新建文件BookController

```java
@RestController
public class BookController {
    @PostMapping("/book")
    public void addBook(@ModelAttribute("b") Book book, @ModelAttribute("a") Author author){
        System.out.println(book);
        System.out.println(author);
    }
}
```

并且再GlobalData中加入以下代码

```java
    @InitBinder("a")
    public void initA(WebDataBinder binder){
        binder.setFieldDefaultPrefix("a.");
    }
    @InitBinder("b")
    public void initB(WebDataBinder binder){
        binder.setFieldDefaultPrefix("b.");
    }
```

在postman中测试

![1588127636675](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588127636675.png)

程序返回

```
Book{name='三国演义', price=99.0}
Author{name='罗贯中', age=60}
```

