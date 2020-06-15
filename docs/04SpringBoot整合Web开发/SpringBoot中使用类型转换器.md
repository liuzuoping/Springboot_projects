## SpringBoot中使用类型转换器

在启动类父文件夹中新建UserController.java

```java
@RestController
public class UserController {
    @GetMapping("/hello")
    public void hello(Date birth){
        System.out.println(birth);
    }
}
```

浏览器输入

http://localhost:8080/hello?birth=1996-06-14

此时会出现参数类型错误

再新建DateConverter.java

```java
@Component
public class DateConverter implements Converter<String, Date> {
    SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
    @Override
    public Date convert(String source) {
        if(source!=null&&!"".equals(source)){
            try {
                return sdf.parse(source);
            }catch (ParseException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
```

此时访问http://localhost:8080/hello?birth=1996-06-14

程序返回

![1588143516791](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588143516791.png)

文件结构如下

![1592212475520](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592212475520.png)