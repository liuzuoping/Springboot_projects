# 创建 Spring Boot 项目





# Spring Boot 介绍

我们刚开始学习 JavaWeb 的时候，使用 Servlet/JSP 做开发，一个接口搞一个 Servlet ，很头大，后来我们通过隐藏域或者反射等方式，可以减少 Servlet 的创建，但是依然不方便，再后来，我们引入 Struts2/SpringMVC 这一类的框架，来简化我们的开发 ，和 Servlet/JSP 相比，引入框架之后，生产力确实提高了不少，但是用久了，又发现了新的问题，即配置繁琐易出错，要做一个新项目，先搭建环境，环境搭建来搭建去，就是那几行配置，不同的项目，可能就是包不同，其他大部分的配置都是一样的，Java 总是被人诟病配置繁琐代码量巨大，这就是其中一个表现。那么怎么办？Spring Boot 应运而生，Spring Boot 主要提供了如下功能：

1. 为所有基于 Spring 的 Java 开发提供方便快捷的入门体验。
2. 开箱即用，有自己自定义的配置就是用自己的，没有就使用官方提供的默认的。
3. 提供了一系列通用的非功能性的功能，例如嵌入式服务器、安全管理、健康检测等。
4. 绝对没有代码生成，也不需要XML配置。

Spring Boot 的出现让 Java 开发又回归简单，因为确确实实解决了开发中的痛点，因此这个技术得到了非常广泛的使用，松哥很多朋友出去面试 Java 工程师，从2017年年初开始，Spring Boot基本就是必问，现在流行的 Spring Cloud 微服务也是基于 Spring Boot，因此，所有的 Java 工程师都有必要掌握好 Spring Boot。

# 系统要求



| Build Tool | Version |
| :--------- | :------ |
| Maven      | 3.3+    |
| Gradle     | 4.4+    |

内置的容器版本分别如下：

| Name         | Version |
| :----------- | :------ |
| Tomcat 9.0   | 4.0     |
| Jetty 9.4    | 3.1     |
| Undertow 2.0 | 4.0     |

# 三种创建方式

初学者看到 Spring Boot 工程创建成功后有那么多文件就会有点懵圈，其实 Spring Boot 工程本质上就是一个 Maven 工程

## Maven 创建



首先创建一个普通的 Maven 项目，以 IntelliJ IDEA 为例，创建步骤如下：

[![img](http://www.javaboy.org/images/boot/1-8.png)](http://www.javaboy.org/images/boot/1-8.png)

注意这里不用选择项目骨架（如果大伙是做练习的话，也可以去尝试选择一下，这里大概有十来个 Spring Boot 相关的项目骨架），直接点击 Next ，下一步中填入一个 Maven 项目的基本信息，如下图



然后点击 Next 完成项目的创建。

创建完成后，在 pom.xml 文件中，添加如下依赖：

```xml
<parent>   
	<groupId>org.springframework.boot</groupId>   
	<artifactId>spring-boot-starter-parent</artifactId>    			       <version>2.1.4.RELEASE</version>
</parent>
<dependencies>    
    <dependency>        
    <groupId>org.springframework.boot</groupId>        
    <artifactId>spring-boot-starter-web</artifactId>    
    </dependency>
</dependencies>
```

添加成功后，再在 main.java 目录下创建包，包中创建一个名为 启动类，如下：

```java
@SpringBootApplication
public class HelloWorldMainApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloWorldMainApplication.class,args);
    }
}
```

然后执行这里的 main 方法就可以启动一个 Spring Boot 工程了。

**创建HelloController**

```java
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
```

运行主程序访问8080端口即可

# 项目结构

使用工具创建出来的项目结构大致如下图：

![1592182007017](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592182007017.png)

对于我们来说，src 是最熟悉的， Java 代码和配置文件写在这里，test 目录用来做测试，pom.xml 是 Maven 的坐标文件，就这几个。

## 使用开发工具创建

有人觉得上面的步骤太过于繁琐，那么也可以使用 IDE 来创建，松哥这里以 IntelliJ IDEA 和 STS 为例，需要注意的是，IntelliJ IDEA 只有 ultimate 版才有直接创建 Spring Boot 项目的功能，社区版是没有此项功能的。

### IntelliJ IDEA

首先在创建项目时选择 Spring Initializr，如下图：

[![img](http://www.javaboy.org/images/boot/1-3.png)](http://www.javaboy.org/images/boot/1-3.png)

然后点击 Next ，填入 Maven 项目的基本信息，如下

![1588035077715](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588035077715.png)

再接下来选择需要添加的依赖，如下图：

[![img](http://www.javaboy.org/images/boot/1-5.png)](http://www.javaboy.org/images/boot/1-5.png)

勾选完成后，点击 Next 完成项目的创建。

创建后pom文件如下

```xml
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>1.5.9.RELEASE</version>
        <relativePath/> <!-- lookup parent from repository -->
    </parent>
    <properties>
        <java.version>1.8</java.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
    </build>
```

默认生成的启动类如下

```java
@SpringBootApplication
public class Springboot02Application {
    public static void main(String[] args) {
        SpringApplication.run(Springboot02Application.class, args);
    }
}
```

创建HelloController.java

```java
@RestController
public class HelloController {
    @RequestMapping("/hello")
    public String hello(){
        return "hello world";
    }
}
```

运行启动类访问8080端口即可，文件结构如下

![1592182640107](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592182640107.png)





## 在线创建

这是官方提供的一个创建方式，实际上，如果我们使用开发工具去创建 Spring Boot 项目的话（即第二种方案），也是从这个网站上创建的，只不过这个过程开发工具帮助我们完成了，我们只需要在开发工具中进行简单的配置即可。

首先打开 `https://start.spring.io` 这个网站，如下

![1588034943631](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588034943631.png)

这里要配置的按顺序分别如下：

- 项目构建工具是 Maven 还是 Gradle ？Java 后端，目前来看还是 Maven 为主，因此这里选择第一项。
- 开发语言，这个当然是选择 Java 了。
- Spring Boot 版本 ，这里我们就是用最新稳定版。
- 既然是 Maven 工程，当然要有项目坐标，项目描述等信息了，另外这里还让输入了包名，因为创建成功后会自动创建启动类。
- Packing 表示项目要打包成 jar 包还是 war 包，Spring Boot 的一大优势就是内嵌了 Servlet 容器，打成 jar 包后可以直接运行，所以这里建议打包成 jar 包，当然，开发者根据实际情况也可以选择 war 包。
- 然后选选择构建的 JDK 版本。
- 最后是选择所需要的依赖，输入关键字如 web ，会有相关的提示，这里我就先加入 web 依赖。

所有的事情全部完成后，点击最下面的 `Generate Project` 按钮，或者点击 `Alt+Enter` 按键，此时会自动下载项目，将下载下来的项目解压，然后用 IntelliJ IDEA 或者 Eclipse 打开即可进行开发。





