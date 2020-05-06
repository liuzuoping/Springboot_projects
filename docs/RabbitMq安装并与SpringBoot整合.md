# RabbitMq安装并与SpringBoot整合

### rabbitMQ在docker中的安装

在docker中依次执行以下指令

```xml
docker search rabbitmq
docker pull rabbitmq
docker run -d --hostname myrabbitmq -P rabbitmq:3-management
```

![1588336189236](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588336189236.png)

便在docker中安装好了rabbitmq

### SpringBoot整合RabbitMQ

pom.xml中加入的依赖如下

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-amqp</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
        <dependency>
            <groupId>org.springframework.amqp</groupId>
            <artifactId>spring-rabbit-test</artifactId>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>junit</groupId>
            <artifactId>junit</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

在application.properties中加入rabbitmq的配置

```xml
spring.rabbitmq.host=192.168.2.104
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
spring.rabbitmq.port=32771
```



### 测试Direct

在启动类父文件夹下新建config包，其下新建RabbitDirectConfig.java

```java
@Configuration
public class RabbitDirectConfig {
    public final static String DIRECTNAME="xiaoliu-direct";
    @Bean
    Queue queue(){
        return new Queue("hello.xiaoliu");
    }
    @Bean
    DirectExchange directExchange(){
        return new DirectExchange(DIRECTNAME,true,false);
    }

    @Bean
    Binding binding(){
        return BindingBuilder.bind(queue()).to(directExchange()).with("direct");
    }
}
```

在启动类父文件夹下新建receiver包，其下新建DirectReceiver.java

```java
@Component
public class DirectReceiver {
    @RabbitListener(queues = "hello.xiaoliu")
    public void handler1(String msg){
        System.out.println("handler1>>>"+msg);
    }
}

```

在测试类中添加测试方法

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class RabbitmqApplicationTests {

    @Autowired
    RabbitTemplate rabbitTemplate;
    @Test
    public void contextLoads() {
        rabbitTemplate.convertAndSend("hello.xiaoliu","hello xiaoliu!hahaha");
    }
}
```

### 测试Fanout

在config包下新建RabbitFanoutConfig.java

```java
@Configuration
public class RabbitFanoutConfig {
    public static final String FANOUTNAME="xiaoliu-fanout";
    @Bean
    Queue queueOne(){
        return new Queue("queue-one");
    }
    @Bean
    Queue queueTwo(){
        return new Queue("queue-two");
    }
    @Bean
    FanoutExchange fanoutExchange(){
        return new FanoutExchange(FANOUTNAME,true,false);
    }
    @Bean
    Binding bindingOne(){
        return BindingBuilder.bind(queueOne()).to(fanoutExchange());
    }
    @Bean
    Binding bindingTwo(){
        return BindingBuilder.bind(queueTwo()).to(fanoutExchange());
    }
}
```

在receiver包下新建FanoutReceiver.java

```java
@Component
public class FanoutReceiver {
    @RabbitListener(queues = "queue-one")
    public void handler1(String msg){
        System.out.println("FanoutReceiver:handler1:"+msg);
    }
    @RabbitListener(queues = "queue-two")
    public void handler2(String msg){
        System.out.println("FanoutReceiver:handler2:"+msg);
    }
}

```

在测试类中添加测试方法

```java
    @Test
    public void test1(){
        rabbitTemplate.convertAndSend(RabbitFanoutConfig.FANOUTNAME,null ,"hello fanout!" );
    }
```

### 测试Header

在config包下新建RabbitHeaderConfig.java

```java
@Configuration
public class RabbitHeaderConfig {
    public static final String HEADERNAME="xiaoliu-header";
    @Bean
    HeadersExchange headersExchange(){
        return new HeadersExchange(HEADERNAME,true,false);
    }
    @Bean
    Queue queueName(){
        return new Queue("name-queue");
    }
    @Bean
    Queue queueAge(){
        return new Queue("age-queue");
    }
    @Bean
    Binding bindingName(){
        Map<String,Object> map = new HashMap<>();
        map.put("name","xiaoliu" );
        return BindingBuilder.bind(queueName()).to(headersExchange()).whereAny(map).match();
    }
    @Bean
    Binding bindingAge(){
        return BindingBuilder.bind(queueAge()).to(headersExchange()).where("age").exists();
    }
}

```

在receiver包下新建HeaderReceiver.java

```java
@Component
public class HeaderReceiver {
    @RabbitListener(queues = "name-queue")
    public void handler1(byte[] msg){
        System.out.println("HeaderReceiver:handler1:"+new String(msg,0,msg.length));
    }
    @RabbitListener(queues = "age-queue")
    public void handler2(byte[] msg){
        System.out.println("HeaderReceiver:handler2:"+new String(msg,0,msg.length));
    }
}
```

在测试类中添加测试方法

```java
    @Test
    public void test2(){
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME,"xiaomi.news" ,"小米新闻" );
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME,"vivo.phone" ,"vivo手机" );
        rabbitTemplate.convertAndSend(RabbitTopicConfig.TOPICNAME,"huawei.phone" ,"华为手机" );
    }
```

### 测试Topic

在config包下新建RabbitTopicConfig.java

```java
@Configuration
public class RabbitTopicConfig {
    public static final String TOPICNAME="xiaoliu-topic";
    @Bean
    TopicExchange topicExchange(){
        return new TopicExchange(TOPICNAME,true,false);
    }
    @Bean
    Queue xiaomi(){
        return new Queue("xiaomi");
    }
    @Bean
    Queue huawei(){
        return new Queue("huawei");
    }
    @Bean
    Queue phone(){
        return new Queue("phone");
    }
    @Bean
    Binding xiaomiBinding(){
        return BindingBuilder.bind(xiaomi()).to(topicExchange()).with("xiaomi.#");
    }
    @Bean
    Binding huaweiBinding(){
        return BindingBuilder.bind(huawei()).to(topicExchange()).with("huawei.#");
    }
    @Bean
    Binding phoneBinding(){
        return BindingBuilder.bind(phone()).to(topicExchange()).with("#.phone.#");
    }
}
```

在receiver包下新建TopicReceiver.java

```java
@Component
public class TopicReceiver {
    @RabbitListener(queues = "xiaomi")
    public void handler1(String msg){
        System.out.println("TopicReceiver:handler1:"+msg);
    }
    @RabbitListener(queues = "huawei")
    public void handler2(String msg){
        System.out.println("TopicReceiver:handler2:"+msg);
    }
    @RabbitListener(queues = "phone")
    public void handler3(String msg){
        System.out.println("TopicReceiver:handler3:"+msg);
    }
}
```

在测试类中添加测试方法

```java
    @Test
    public void test3(){
        Message nameMsg= MessageBuilder.withBody("hello xiaoliu!".getBytes()).setHeader("name","xiaoliu" ).build();
        Message ageMsg= MessageBuilder.withBody("hello 99!".getBytes()).setHeader("age","99" ).build();
        rabbitTemplate.send(RabbitHeaderConfig.HEADERNAME,null,ageMsg);
        rabbitTemplate.send(RabbitHeaderConfig.HEADERNAME,null,nameMsg);
    }
```

