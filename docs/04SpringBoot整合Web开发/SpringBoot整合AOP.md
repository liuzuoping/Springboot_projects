# SpringBoot整合AOP

pom.xml引入以下依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>


        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjrt</artifactId>
            <version>1.6.10</version>
        </dependency>

        <dependency>
            <groupId>org.aspectj</groupId>
            <artifactId>aspectjweaver</artifactId>
            <version>1.6.10</version>
        </dependency>

        <!--cglib包是用来动态代理用的,基于类的代理-->
        <dependency>

            <groupId>cglib</groupId>
            <artifactId>cglib</artifactId>
            <version>2.2.2</version>
        </dependency>

        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-aop</artifactId>
            <version>4.3.11.RELEASE</version>
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

在启动类父文件夹中新建service文件夹并在其下新建UserService.java

```java
package cn.itxiaoliu.aop.service;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public String getUsernameById(Integer id){
        System.out.println("getUsernameById");
        return "xiaoliu";
    }
    public void deleteUserById(Integer id){
        System.out.println("deleteUserById");
    }
}

```

在启动类父文件夹下新建UserController.java

```java
package cn.itxiaoliu.aop;

import cn.itxiaoliu.aop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    @Autowired
    UserService userService;
    @GetMapping("/test1")
    public String getUsernameById(Integer id){
        return userService.getUsernameById(id);
    }
    @GetMapping("/test2")
    public void deleteUserById(Integer id){
        userService.deleteUserById(id);
    }
}

```

再新建LogComponent.java

```java
package cn.itxiaoliu.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LogComponent  {
    @Pointcut("execution(* cn.itxiaoliu.aop.service.*.*(..))")
    public void pc1(){
    }
    @Before(value = "pc1()")
    public void before(JoinPoint jp){
        String name=jp.getSignature().getName();
        System.out.println("before---"+name);
    }
    @After(value = "pc1()")
    public void after(JoinPoint jp){
        String name = jp.getSignature().getName();
        System.out.println("after-----"+name);
    }
    @AfterReturning(value = "pc1()",returning = "result")
    public  void afterReturning(JoinPoint jp,Object result){
        String name=jp.getSignature().getName();
        System.out.println("afterReturning-----"+name+"-------"+result);
    }
    @AfterThrowing(value = "pc1()",throwing = "e")
    public void afterThrowing(JoinPoint jp,Exception e){
        String name = jp.getSignature().getName();
        System.out.println("afterThrowing---"+name+"------"+e.getMessage());
    }
    @Around("pc1()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        Object proceed = pjp.proceed();
        return "www.xiaoliu.com";
    }
}

```

浏览器访问

http://localhost:8080/test1

程序返回

before---getUsernameById
getUsernameById
after-----getUsernameById
afterReturning-----getUsernameById-------www.xiaoliu.com

目录结构如下

![1588145369197](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588145369197.png)