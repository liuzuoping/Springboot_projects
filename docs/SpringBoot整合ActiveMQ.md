# SpringBoot整合ActiveMQ

pom.xml添加的依赖如下

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-activemq</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
```

在application.properties中配置本地ActiveMQ

```xml
spring.activemq.broker-url=tcp://localhost:61616
spring.activemq.packages.trust-all=true
spring.activemq.user=admin
spring.activemq.password=admin
```

在启动类父文件夹下新建Message.java

```java
public class Message implements Serializable {
    private String content;
    private Date sendDate;

    @Override
    public String toString() {
        return "Message{" +
                "content='" + content + '\'' +
                ", sendDate=" + sendDate +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }
}
```

在启动类父文件夹下新建JmsComponent.java

```java
@Component
public class JmsComponent {
    @Autowired
    JmsMessagingTemplate jmsMessagingTemplate;
    @Autowired
    Queue queue;

    public void send(Message message){
        jmsMessagingTemplate.convertAndSend(this.queue,message);
    }
    @JmsListener(destination = "hello.xiaoliu")
    public void receive(Message message){
        System.out.println(message);
    }
}
```

在启动类中加入以下代码

```java
@SpringBootApplication
public class ActivemqApplication {

    public static void main(String[] args) {
        SpringApplication.run(ActivemqApplication.class, args);
    }
    @Bean
    Queue queue(){
        return new ActiveMQQueue("hello.xiaoliu");
    }
}
```

在测试类中进行测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivemqApplicationTests {

    @Autowired
    JmsComponent jmsComponent;
    @Test
    public void contextLoads() {
        Message message=new Message();
        message.setContent("hello xiaoliu!");
        message.setSendDate(new Date());
        jmsComponent.send(message);
    }
}
```

启动本地AcitiveMQ并且允许项目

![1588335758412](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588335758412.png)

