# SpringBoot应用监控

### 应用端点监控

pom.xml中引入以下依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
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
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
            <exclusions>
                <exclusion>
                    <groupId>org.junit.vintage</groupId>
                    <artifactId>junit-vintage-engine</artifactId>
                </exclusion>
            </exclusions>
        </dependency>
    </dependencies>
```

在启动类父文件夹下新建SecurityConfig.java

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.requestMatcher(EndpointRequest.toAnyEndpoint())
                .authorizeRequests()
                .anyRequest().hasRole("ADMIN")
                .and()
                .httpBasic();
    }
}
```

在启动类父文件夹下新建XiaoliuInfo.java

```java
@Component
public class XiaoliuInfo implements InfoContributor {

    @Override
    public void contribute(Info.Builder builder) {
        Map<String,Object> info=new HashMap<>();
        info.put("email","2330163321@qq.com" );
        builder.withDetail("author",info );
    }
}

```

在application.properties中做以下配置

```xml
management.endpoint.shutdown.enabled=true
#management.endpoints.enabled-by-default=false
#management.endpoint.info.enabled=true
management.endpoints.web.exposure.include=*

spring.security.user.name=xiaoliu
spring.security.user.password=123
spring.security.user.roles=ADMIN
#management.endpoints.web.base-path=/xiaoliu
#management.endpoints.web.path-mapping..health=xiaoliu-health

management.endpoints.web.cors.allowed-origins=http://localhost:8081
management.endpoints.web.cors.allowed-methods=GET,POST

management.endpoint.health.show-details=when_authorized
spring.redis.host=localhost

info.app.encoding=@project.build.sourceEncoding@
info.app.java.source=@java.version@
info.app.java.target=@java.version@
info.author.name=xiaoliu
info.author.address=www.xiaoliu.com

management.info.git.mode=full
```

启动项目，登录并且访问Info端点与Health端点

![1588396751197](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588396751197.png)

### 监控信息可视化

新建admin项目，pom.xml中加入以下依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-server</artifactId>
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
    </dependencies>
```

启动类上添加以下代码

```java
@SpringBootApplication
@EnableAdminServer
public class AdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(AdminApplication.class, args);
    }

}
```

访问http://localhost:8080/index.html

就可以实现对监控信息的可视化

新建client项目，pom.xml中引入以下依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-actuator</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-mail</artifactId>
        </dependency>
        <dependency>
            <groupId>de.codecentric</groupId>
            <artifactId>spring-boot-admin-starter-client</artifactId>
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
    </dependencies>
```

在application.properties中做以下配置

```xml
management.endpoints.web.exposure.include=*

server.port=8081
spring.boot.admin.client.url=http://localhost:8080
```

启动项目，任然访问http://localhost:8080/index.html

就可以实现对此项目的监控信息可视化

![1588397670126](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588397670126.png)

### 邮件报警

admin项目中的application.properties中添加以下配置便可实现

```xml
spring.mail.host=smtp.qq.com
spring.mail.port=587
spring.mail.username=2330163321@qq.com
spring.mail.password=wjyexmwgemcldide
spring.mail.default-encoding=UTF-8
spring.mail.properties.mail.smtp.socketFactory.class=javax.net.ssl.SSLSocketFactory
spring.mail.properties.mail.debug=true

spring.boot.admin.notify.mail.to=1041344803@qq.com
spring.boot.admin.notify.mail.from=2330163321@qq.com
spring.boot.admin.notify.mail.ignore-changes=

```

