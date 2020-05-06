# SpringBoot中的测试

### SpringBoot中的Service测试

在启动类父文件夹中创建HelloService

```java
@Service
public class HelloService {
    public String sayHello(String name){
        return "hello"+name;
    }
}
```

在测试类中加入测试方法

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {
    @Autowired
    HelloService helloService;
    @Test
    public void contextLoads() {
        String hello = helloService.sayHello("xiaoliu");
        //Assert.assertThat(hello, Matchers.is("hello xiaoliu"));
        //Assert.assertThat(hello,Matchers.is("hello xiaoliu") );
    }
}
```

### SpringBoot中的Controller测试

在启动类父文件夹中创建HelloController

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(String name){
        return "hello"+name;
    }
    @PostMapping("/book")
    public Book addBook(@RequestBody Book book){
        return book;
    }
}
```

并且创建Book实体类

```java
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

在测试类中加入测试方法

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestApplicationTests {
    @Autowired
    WebApplicationContext wac;
    MockMvc mockMvc;
    @Before
    public void before(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        ////构造MockMvc
    }
    @Test
    public void test1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(
                MockMvcRequestBuilders.get("/hello")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("name", "xiaoliu"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

    }

    @Test
    public void test2() throws Exception {
        Book book=new Book();
        book.setId(99);
        book.setName("三国演义");
        book.setAuthor("罗贯中");
        String s = new ObjectMapper().writeValueAsString(book);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/book").contentType(MediaType.APPLICATION_JSON).content(s))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
    }
}
```

### SpringBoot中的json测试

在test目录下添加book.json

```xml
{"id":99,"name":"红楼梦","author":"曹雪芹"}
```

并且添加JsonTest.java

```java
@RunWith(SpringRunner.class)
@org.springframework.boot.test.autoconfigure.json.JsonTest
public class JsonTest {
    @Autowired
    JacksonTester<Book> jacksonTester;
    @Test
    public void contextLoads() throws Exception {
        Book book=new Book();
        book.setId(99);
        book.setName("红楼梦");
        book.setAuthor("曹雪芹");
        Assertions.assertThat(jacksonTester.write(book))
                .isEqualToJson("book.json");
        Assertions.assertThat(jacksonTester.write(book))
                .hasJsonPathStringValue("@.name");
        Assertions.assertThat(jacksonTester.write(book))
                .extractingJsonPathStringValue("@.name")
                .isEqualTo("红楼梦");
    }

    @Test
    public void test2() throws IOException {
        String content="{\"id\":99,\"name\":\"红楼梦\",\"author\":\"曹雪芹\"}";
      Assertions.assertThat(jacksonTester.parseObject(content).getName()).isEqualTo("红楼梦");
    }
}

```

### SpringBoot中的json测试testRestTemplate

```java
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TestApplicationTests2 {
    @Autowired
    TestRestTemplate testRestTemplate;
    @Test
    public void contextLoads() {
        String xiaoliu = testRestTemplate.getForObject("/hello?name={1}", String.class, "xiaoliu");
        System.out.println(xiaoliu);
    }
}
```

