# Spring Boot整合MyBatis多数据源

关于多数据源的配置，前面和大伙介绍过JdbcTemplate多数据源配置，那个比较简单，本文来和大伙说说MyBatis多数据源的配置。
其实关于多数据源，我的态度还是和之前一样，复杂的就直接上分布式数据库中间件，简单的再考虑多数据源。这是项目中的建议，技术上的话，当然还是各种技术都要掌握的。



## 工程创建

首先需要创建MyBatis项目，项目创建和前文的一样，添加MyBatis、MySQL以及Web依赖：

[![img](http://www.javaboy.org/images/sb/3-1.png)](http://www.javaboy.org/images/sb/3-1.png)

项目创建完成后，添加Druid依赖，和JdbcTemplate一样，这里添加Druid依赖也必须是专为Spring boot打造的Druid，不能使用传统的Druid。完整的依赖如下：

```java
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.mybatis.spring.boot</groupId>
            <artifactId>mybatis-spring-boot-starter</artifactId>
            <version>2.1.2</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>druid-spring-boot-starter</artifactId>
            <version>1.1.10</version>
        </dependency>
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <scope>runtime</scope>
        </dependency>
```

## 多数据源配置

接下来配置多数据源，这里基本上还是和JdbcTemplate多数据源的配置方式一致，首先在application.properties中配置数据库基本信息，然后提供两个DataSource即可，这里我再把代码贴出来，里边的道理条条框框的，大伙可以参考前面的文章，这里不再赘述。

文件结构如下

![1592217287301](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592217287301.png)

pom文件中添加以下配置

```xml
        <resources>
            <resource>
                <directory>src/main/java</directory>
                <includes>
                    <include>**/*.xml</include>
                </includes>
            </resource>
            <resource>
                <directory>src/main/resources</directory>
            </resource>
        </resources>
```



application.properties中的配置：

```java
spring.datasource.one.username=xiaoliu
spring.datasource.one.password=960614abcd
spring.datasource.one.url=jdbc:mysql:///ssm?serverTimezone=UTC
spring.datasource.one.type=com.alibaba.druid.pool.DruidDataSource

spring.datasource.two.username=xiaoliu
spring.datasource.two.password=960614abcd
spring.datasource.two.url=jdbc:mysql:///xiaoliu?serverTimezone=UTC
spring.datasource.two.type=com.alibaba.druid.pool.DruidDataSource
```

创建实体类

```java
public class User {
    private Integer id;
    private String username;
    private String address;
    //getter&setter&toString()
```



然后再提供两个DataSource的DataSourceConfig.java，如下：

```java
@Configuration
public class DataSourceConfig {
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.one")
    DataSource dsOne(){
        return DruidDataSourceBuilder.create().build();
    }
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.two")
    DataSource dsTwo(){
        return DruidDataSourceBuilder.create().build();
    }
}
```

## MyBatis配置

接下来则是MyBatis的配置，不同于JdbcTemplate，MyBatis的配置要稍微麻烦一些，因为要提供两个Bean，因此这里两个数据源我将在两个类中分开来配置，首先来看第一个数据源的配置：

```java
@Configuration
@MapperScan(basePackages = "cn.itxaioliu.mapper1",sqlSessionFactoryRef = "sqlSessionFactory1",
        sqlSessionTemplateRef = "sqlSessionTemplate1")
public class MybatisConfigOne {
    @Resource(name = "dsOne")
    DataSource dsOne;
    @Bean
    SqlSessionFactory sqlSessionFactory1(){
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        try {
            bean.setDataSource(dsOne);
            return bean.getObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Bean
    SqlSessionTemplate sqlSessionTemplate1(){
        return new SqlSessionTemplate(sqlSessionFactory1());
    }
}
```

创建MyBatisConfigOne类，首先指明该类是一个配置类，配置类中要扫描的包是org.sang.mybatis.mapper1，即该包下的Mapper接口将操作dsOne中的数据，对应的SqlSessionFactory和SqlSessionTemplate分别是sqlSessionFactory1和sqlSessionTemplate1，在MyBatisConfigOne内部，分别提供SqlSessionFactory和SqlSessionTemplate即可，SqlSessionFactory根据dsOne创建，然后再根据创建好的SqlSessionFactory创建一个SqlSessionTemplate。

这里配置完成后，依据这个配置，再来配置第二个数据源即可：

```java
@Configuration
@MapperScan(basePackages = "cn.itxaioliu.mapper2",sqlSessionFactoryRef = "sqlSessionFactory2",
        sqlSessionTemplateRef = "sqlSessionTemplate2")
public class MybatisConfigTwo {
    @Resource(name = "dsTwo")
    DataSource dsTwo;
    @Bean
    SqlSessionFactory sqlSessionFactory2(){
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        try {
            bean.setDataSource(dsTwo);
            return bean.getObject();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    @Bean
    SqlSessionTemplate sqlSessionTemplate2(){
        return new SqlSessionTemplate(sqlSessionFactory2());
    }
}
```

好了，这样MyBatis多数据源基本上就配置好了，接下来只需要在org.sang.mybatis.mapper1和org.sang.mybatis.mapper2包中提供不同的Mapper，Service中注入不同的Mapper就可以操作不同的数据源。

## mapper创建

org.sang.mybatis.mapper1中的mapper：

```java
public interface UserMapper1 {
    List<User> getAllUsers();
}
```

对应的XML文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.itxaioliu.mapper1.UserMapper1">
    <select id="getAllUsers" resultType="cn.itxaioliu.bean.User">
        select * from user ;
    </select>
</mapper>
```

org.sang.mybatis.mapper2中的mapper：

```java
public interface UserMapper2 {
    List<User> getAllUsers();
}
```

对应的XML文件：

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="cn.itxaioliu.mapper2.UserMapper2">
    <select id="getAllUsers" resultType="cn.itxaioliu.bean.User">
        select * from user ;
    </select>
</mapper>
```

接下来，在Service中注入两个不同的Mapper，不同的Mapper将操作不同的数据源。

### 在测试类中进行测试

```java
@RunWith(SpringRunner.class)
@SpringBootTest
public class Mybatis2ApplicationTests {

    /**
     *
     */
    @Autowired
    UserMapper2 userMapper2;
    @Autowired
    UserMapper1 userMapper1;
    @Test
    public void contextLoads() {
        List<User> allUsers=userMapper1.getAllUsers();
        System.out.println(allUsers);
        List<User> allUsers1 = userMapper2.getAllUsers();
        System.out.println(allUsers1);
    }

}

```

![1588157504534](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588157504534.png)

