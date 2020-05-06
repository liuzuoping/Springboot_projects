# Spring Boot中的yaml配置

搞Spring Boot的小伙伴都知道，Spring Boot中的配置文件有两种格式，properties或者yaml，一般情况下，两者可以随意使用，选择自己顺手的就行了，那么这两者完全一样吗？肯定不是啦！本文就来和大伙重点介绍下yaml配置，最后再来看看yaml和properties配置有何区别。



# 狡兔三窟

首先application.yaml在Spring Boot中可以写在四个不同的位置，分别是如下位置：

1. 项目根目录下的config目录中
2. 项目根目录下
3. classpath下的config目录中
4. classpath目录下

四个位置中的application.yaml文件的优先级按照上面列出的顺序依次降低。即如果有同一个属性在四个文件中都出现了，以优先级高的为准。

那么application.yaml是不是必须叫application.yaml这个名字呢？当然不是必须的。开发者可以自己定义yaml名字，自己定义的话，需要在项目启动时指定配置文件的名字，像下面这样：

[![img](https://www.javaboy.org/images/sb/9-1.png)](https://www.javaboy.org/images/sb/9-1.png)

当然这是在IntelliJ IDEA中直接配置的，如果项目已经打成jar包了，则在项目启动时加入如下参数：

```
java -jar myproject.jar --spring.config.name=app
```

这样配置之后，在项目启动时，就会按照上面所说的四个位置按顺序去查找一个名为app.yaml的文件。当然这四个位置也不是一成不变的，也可以自己定义，有两种方式，一个是使用`spring.config.location`属性，另一个则是使用`spring.config.additional-location`这个属性，在第一个属性中，表示自己重新定义配置文件的位置，项目启动时就按照定义的位置去查找配置文件，这种定义方式会覆盖掉默认的四个位置，也可以使用第二种方式，第二种方式则表示在四个位置的基础上，再添加几个位置，新添加的位置的优先级大于原本的位置。

配置方式如下：

[![img](https://www.javaboy.org/images/sb/9-2.png)](https://www.javaboy.org/images/sb/9-2.png)

这里要注意，配置文件位置时，值一定要以/结尾。

# 数组注入

yaml也支持数组注入，例如

```yaml
server:
  port: 8081
  servlet:
    context-path: /itxiaoliu
redis:
  port: 6379
  hosts:
    - 192.168.66.128
    - 192.168.66.129
    - 192.168.66.130
    - 192.168.66.131
  redisList:
    - port: 6379
      host: 192.168.66.66
    - port: 6380
      host: 192.168.66.67

```

创建Redis类

```java
package cn.itxiaoliu;

public class Redis {
    private Integer port;
    private String host;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    @Override
    public String toString() {
        return "Redis{" +
                "port=" + port +
                ", host='" + host + '\'' +
                '}';
    }

    public void setHost(String host) {
        this.host = host;
    }
}

```

这段数据可以绑定到一个带Bean的数组中：

```java
package cn.itxiaoliu;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
@Component
@ConfigurationProperties("redis")
public class RedisCluster {
    private Integer port;
    private List<String> hosts;
    private List<Redis> redisList;

    @Override
    public String toString() {
        return "RedisCluster{" +
                "port=" + port +
                ", hosts=" + hosts +
                ", redisList=" + redisList +
                '}';
    }

    public List<Redis> getRedisList() {
        return redisList;
    }

    public void setRedisList(List<Redis> redisList) {
        this.redisList = redisList;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }
}

```

项目启动后，配置中的数组会自动存储到servers集合中。当然，yaml不仅可以存储这种简单数据，也可以在集合中存储对象。例如下面这种：

```yaml
redis:
  port: 6379
  hosts:
    - 192.168.66.128
    - 192.168.66.129
    - 192.168.66.130
    - 192.168.66.131
  redisList:
    - port: 6379
      host: 192.168.66.66
    - port: 6380
      host: 192.168.66.67
```

这个可以被注入到如下类中：

```java
@Component
@ConfigurationProperties("redis")
public class RedisCluster {}
```

测试类中测试

```java
package cn.itxiaoliu;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

    @Autowired
    Book book;
    @Autowired
    RedisCluster redisCluster;
    @Test
    public void contextLoads() {
        System.out.println(redisCluster);
    }

}

```



# 优缺点

不同于properties文件的无序，yaml配置是有序的，这一点在有些配置中是非常有用的，例如在Spring Cloud Zuul的配置中，当我们配置代理规则时，顺序就显得尤为重要了。当然yaml配置也不是万能的，例如，yaml配置目前不支持@PropertySource注解。