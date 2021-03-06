# 两种方式创建ssm项目

### 第一种方式，基于xml

创建一个maven项目，在其pom文件下新增

```xml
<packaging>war</packaging>

    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.0.5.RELEASE</version>
        </dependency>
    </dependencies>
```



点击项目名右键选择打开模块设置或者按F4，创建Webapp目录，并且在其下面创建WEB-INF文件夹以及其下面的web.xml文件

![1592121824608](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592121824608.png)

![1592121901274](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592121901274.png)

在resources文件夹中创建spring的配置文件applicationcontext.xml添加如下代码

![1592122765066](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592122765066.png)

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="cn.itxiaoliu" use-default-filters="true">
        <context:exclude-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
</beans>
```

在resources文件夹中创建springmvc的配置文件spring-servlet.xml,添加如下代码

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:mvc="http://www.springframework.org/schema/mvc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       https://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/mvc
       http://www.springframework.org/schema/mvc/spring-mvc.xsd">

    <context:component-scan base-package="cn.itxiaoliu" use-default-filters="false">
        <context:include-filter type="annotation" expression="org.springframework.stereotype.Controller"/>
    </context:component-scan>
    <mvc:annotation-driven></mvc:annotation-driven>
</beans>
```

在java文件夹下创建cn.itxiaoliu.controller文件夹以及cn.itxiaoliu.service文件夹

在controller包下创建HelloController.java文件

```java
package cn.itxiaoliu.controller;

import cn.itxiaoliu.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    HelloService helloService;
    @GetMapping("/hello")
    public String hello(){
        return helloService.sayHello();
    }
}
```

在service包下创建HelloService.java文件

```java
package cn.itxiaoliu.service;

import org.springframework.stereotype.Service;

@Service
public class HelloService {
    public String sayHello(){
        return "hello java极客技术";
    }
}

```

在web.xml中做如下配置

```xml
<!--    spring的配置-->
    <context-param>
        <param-name>contextConfigLocation</param-name>
        <param-value>classpath:applicationContext.xml</param-value>
    </context-param>
    <listener>
        <listener-class>org.springframework.web.context.ContextLoaderListener</listener-class>
    </listener>
<!--    springmvc的配置-->
    <servlet>
        <servlet-name>springmvc</servlet-name>
        <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
        <init-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>classpath:spring-servlet.xml</param-value>
        </init-param>
    </servlet>
    <servlet-mapping>
        <servlet-name>springmvc</servlet-name>
        <url-pattern>/</url-pattern>
    </servlet-mapping>
```

配置好本地tomcat便可访问

pom.xml文件中只要加入spring-webmvc配置即可

### 第二种方式，基于纯注解

创建maven工程，pom.xml中加入以下配置

```xml
    <packaging>war</packaging>
    <dependencies>
        <dependency>
            <groupId>org.springframework</groupId>
            <artifactId>spring-webmvc</artifactId>
            <version>5.0.2.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>javax.servlet</groupId>
            <artifactId>javax.servlet-api</artifactId>
            <version>4.0.1</version>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.alibaba</groupId>
            <artifactId>fastjson</artifactId>
            <version>1.1.37</version>
        </dependency>
    </dependencies>
```



纯注解需要在java文件夹下创建不止cn.itxiaoliu.controller和cn.itxiaoliu.service,还有cn.itxiaoliu.config以及cn.itxiaoliu.interceptor

**在config包下创建SpringConfig.java相当于spring的xml文件**

```java
@Configuration
@ComponentScan(basePackages = "cn.itxiaoliu",useDefaultFilters = true,
        excludeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Controller.class)})
public class SpringConfig {

}
```

**创建SpringMVCConfig.java相当于springmvc的xml文件**

```java
@Configuration
@ComponentScan(basePackages = "cn.itxiaoliu",useDefaultFilters = false,
        includeFilters = {@ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Controller.class),
        @ComponentScan.Filter(type = FilterType.ANNOTATION,classes = Configuration.class)})
public class SpringMVCConfig extends WebMvcConfigurationSupport {
    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
    }

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor()).addPathPatterns("/**");
    }
    @Bean
    MyInterceptor myInterceptor(){
        return new MyInterceptor();
    }

    @Override
    protected void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        FastJsonHttpMessageConverter e=new FastJsonHttpMessageConverter();
        converters.add(e);
    }
}

```

**创建WebInt.java相当于web.xml文件**

```java
public class WebInt implements WebApplicationInitializer {
    public void onStartup(javax.servlet.ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext ctx = new AnnotationConfigWebApplicationContext();
        ctx.setServletContext(servletContext);
        ctx.register(SpringMVCConfig.class);
        ServletRegistration.Dynamic springmvc = servletContext.addServlet("springmvc", new DispatcherServlet(ctx));
        springmvc.addMapping("/");
        springmvc.setLoadOnStartup(1);
    }
}
```

在controller包下创建HelloController.java

```java
@RestController
public class HelloController {
    @Autowired
    HelloService helloService;
    @GetMapping("/hello")
    public String hello(){
        return helloService.sayHello();
    }
    @GetMapping("/data")
    public List<String> getData(){
        ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i <10 ; i++) {
            list.add("www.justdojava.com>>>"+i);
        }
        return list;
    }
}

```

**在service包下创建HelloService.java**

```java
@Service
public class HelloService {
    public String sayHello(){
        return "hello javassm!";
    }
}
```

在interceptor包下创建MyInterceptor.java

```java
public class MyInterceptor implements HandlerInterceptor {
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        return true;
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
```

文件目录如下

![1592141563611](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592141563611.png)

