### 定制springboot项目的启动banner

![1588036523837](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588036523837.png)

上图是我们正常启动spring boot项目时出现的banner，要想修改只需要在resources包下添加banner.txt文件即可

例如在resources包下添加banner.txt文件并且输入hello spring boot！那么启动后如下

![1588036657998](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588036657998.png)

还可以在

http://patorjk.com/software/taag

定制banner

### 关闭banner

只需要在启动类做如下配置

```java
@SpringBootApplication
public class DemoApplication {
    public static void main(String[] args) {
        //SpringApplication.run(DemoApplication.class, args);
        SpringApplicationBuilder builder=new SpringApplicationBuilder(DemoApplication.class);
        SpringApplication build = builder.build();
        build.setBannerMode(Banner.Mode.OFF);
        build.run(args);
    }
}
```

