## spring boot中的属性注入

### 通过application.properties

创建一个spring boot项目

在启动类父文件夹上创建Book类

```java
@Component
public class Book {
    @Value("${book.id}")
    private Long id;
    @Value("${book.name}")
    private String name;
    @Value("${book.author}")
    private String author;
//getters&setters&tostring()
```

application.properties文件下输入

```
book.id=99
book.author=罗贯中
book.name=三国演义
```

在测试类中进行测试

```java
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

如有中文乱码则修改设置

![1592183296597](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592183296597.png)

通过在resources目录下编写book.properties

```
book.id=99
book.author=罗贯中
book.name=三国演义
```

修改Book类

```java
@Component
@PropertySource("classpath:book.properties")
@ConfigurationProperties(prefix = "book")
public class Book {
    private long id;
    private String name;
    private String address;
    //getters&setters&tostring()
```

启动测试类即可