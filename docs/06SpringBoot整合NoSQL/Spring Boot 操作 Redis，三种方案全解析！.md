# Spring Boot 操作 Redis，三种方案全解析！

在 Redis 出现之前，我们的缓存框架各种各样，有了 Redis ，缓存方案基本上都统一了

使用 Java 操作 Redis 的方案很多，Jedis 是目前较为流行的一种方案，除了 Jedis ，还有很多其他解决方案，如下：

[![img](http://www.javaboy.org/images/boot/13-1.png)](http://www.javaboy.org/images/boot/13-1.png)

除了这些方案之外，还有一个使用也相当多的方案，就是 Spring Data Redis。

在传统的 SSM 中，需要开发者自己来配置 Spring Data Redis ，这个配置比较繁琐，主要配置 3 个东西：连接池、连接器信息以及 key 和 value 的序列化方案。

在 Spring Boot 中，默认集成的 Redis 就是 Spring Data Redis，默认底层的连接池使用了 lettuce ，开发者可以自行修改为自己的熟悉的，例如 Jedis。

Spring Data Redis 针对 Redis 提供了非常方便的操作模板 RedisTemplate 。这是 Spring Data 擅长的事情，那么接下来我们就来看看 Spring Boot 中 Spring Data Redis 的具体用法。

# 方案一：Spring Data Redis

## 创建工程

创建工程，引入 Redis 依赖：

[![img](http://www.javaboy.org/images/boot/13-2.png)](http://www.javaboy.org/images/boot/13-2.png)

创建成功后完整的 pom.xml 依赖如下：

```xml
   <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-redis</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
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
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>
```

这里主要就是引入了 Spring Data Redis + 连接池。

## 配置 Redis 信息

接下来配置 Redis 的信息，信息包含两方面，一方面是 Redis 的基本信息，另一方面则是连接池信息:

```xml
spring.redis.host=localhost
spring.redis.database=0
spring.redis.port=6379
spring.redis.password=
```

## 自动配置

当开发者在项目中引入了 Spring Data Redis ，并且配置了 Redis 的基本信息，此时，自动化配置就会生效。

我们从 Spring Boot 中 Redis 的自动化配置类中就可以看出端倪：

```java
@Configuration
@ConditionalOnClass({JedisConnection.class, RedisOperations.class, Jedis.class})
@EnableConfigurationProperties({RedisProperties.class})
public class RedisAutoConfiguration {
    public RedisAutoConfiguration() {
    }

    @Configuration
    protected static class RedisConfiguration {
        protected RedisConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean(
            name = {"redisTemplate"}
        )
        public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
            RedisTemplate<Object, Object> template = new RedisTemplate();
            template.setConnectionFactory(redisConnectionFactory);
            return template;
        }
```

这个自动化配置类很好理解：

1. 首先标记这个是一个配置类，同时该配置在 RedisOperations 存在的情况下才会生效(即项目中引入了 Spring Data Redis)
2. 然后导入在 application.properties 中配置的属性
3. 然后再导入连接池信息（如果存在的话）
4. 最后，提供了两个 Bean ，RedisTemplate 和 StringRedisTemplate ，其中 StringRedisTemplate 是 RedisTemplate 的子类，两个的方法基本一致，不同之处主要体现在操作的数据类型不同，RedisTemplate 中的两个泛型都是 Object ，意味者存储的 key 和 value 都可以是一个对象，而 StringRedisTemplate 的 两个泛型都是 String ，意味者 StringRedisTemplate 的 key 和 value 都只能是字符串。如果开发者没有提供相关的 Bean ，这两个配置就会生效，否则不会生效。

## 使用

接下来，

```java
@RestController
public class HelloController {
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @GetMapping("/set")
    public void set(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        ops.set("name","xiaoliu" );
    }
    @GetMapping("/get")
    public void get(){
        ValueOperations<String, String> ops = stringRedisTemplate.opsForValue();
        System.out.println(ops.get("name"));
    }
}


```

另外需要注意 ，Spring Boot 的自动化配置，只能配置单机的 Redis ，如果是 Redis 集群，则所有的东西都需要自己手动配置，关于如何操作 Redis 集群，松哥以后再来和大家分享。

# 方案二：Spring Cache

通过 Spring Cache 的形式来操作 Redis，Spring Cache 统一了缓存江湖的门面

# 方案三：回归原始时代

第三种方案，就是直接使用 Jedis 或者 其他的客户端工具来操作 Redis ，这种方案在 Spring Boot 中也是支持的，虽然操作麻烦，但是支持。