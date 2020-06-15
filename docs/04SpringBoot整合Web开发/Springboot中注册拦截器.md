## Springboot中注册拦截器

在启动类父文件夹夹中添加HelloController

```java
@RestController
public class HelloController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
```

再添加MyInterceptor.java

```java
public class Myinterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("preHandle");
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("postHandle");
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("afterCompletion");
    }
}
```

添加WebMvcConfig.java

```java
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myinterceptor()).addPathPatterns("/**");
    }
    @Bean
    Myinterceptor myinterceptor(){
        return new Myinterceptor();
    }
}

```

访问http://localhost:8080/hello

程序返回

![1588132497358](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588132497358.png)

**文件结构如下**

![1592210652244](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592210652244.png)