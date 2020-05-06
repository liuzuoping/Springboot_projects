# SpringBoot整合swagger2

# SpringBoot 整合 Swagger2

前后端分离后，维护接口文档基本上是必不可少的工作。

一个理想的状态是设计好后，接口文档发给前端和后端，大伙按照既定的规则各自开发，开发好了对接上了就可以上线了。当然这是一种非常理想的状态，实际开发中却很少遇到这样的情况，接口总是在不断的变化之中，有变化就要去维护，做过的小伙伴都知道这件事有多么头大！还好，有一些工具可以减轻我们的工作量，Swagger2 就是其中之一，至于其他类似功能但是却收费的软件，这里就不做过多介绍了。本文主要和大伙来聊下 在Spring Boot 中如何整合 Swagger2。

## 工程创建

当然，首先是创建一个 Spring Boot 项目，加入 web 依赖，创建成功后，加入两个 Swagger2 相关的依赖，完整的依赖如下：

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger-ui</artifactId>
            <version>2.9.2</version>
        </dependency>
        <dependency>
            <groupId>io.springfox</groupId>
            <artifactId>springfox-swagger2</artifactId>
            <version>2.9.2</version>
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

## Swagger2 配置

Swagger2 的配置也是比较容易的，在项目创建成功之后，只需要开发者自己提供一个 Docket 的 Bean 即可，如下：

```java
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    Docket docket(){
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("cn.itxiaoliu.controller"))
                .paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder()
                        .description("接口文档的描述信息")
                        .title("微人事项目接口文档")
                        .contact(new Contact("xiaoliu","http://www.xiaoliu.com" ,"2330163321@qq.com"))
                        .version("v1.0")
                        .license("Apache2.0")
                        .build());
    }
}
```

这里提供一个配置类，首先通过 @EnableSwagger2 注解启用 Swagger2 ，然后配置一个 Docket Bean，这个 Bean 中，配置映射路径和要扫描的接口的位置，在 apiInfo 中，主要配置一下 Swagger2 文档网站的信息，例如网站的 title，网站的描述，联系人的信息，使用的协议等等。

如此，Swagger2 就算配置成功了，非常方便。

此时启动项目，输入 `http://localhost:8080/swagger-ui.html`，能够看到如下页面，说明已经配置成功了：

![1588385448703](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588385448703.png)

## 创建接口

接下来就是创建接口了，Swagger2 相关的注解其实并不多，而且很容易懂，下面我来分别向小伙伴们举例说明：

```java
@RestController
@Api(tags = "用户数据接口")
public class UserController {
    @ApiOperation(value = "查询用户",notes = "根据用户id查询用户")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,defaultValue = "99")
    @GetMapping("/user")
    public User getUserById(Integer id){
        User user=new User();
        user.setId(id);
        return user;
    }
    @ApiOperation(value = "删除用户",notes = "根据用户id删除用户")
    @ApiImplicitParam(name = "id",value = "用户id",required = true,defaultValue = "99")
    @ApiResponses({
            @ApiResponse(code=200,message = "删除成功"),
            @ApiResponse(code=500,message = "删除失败")
    })
    @DeleteMapping("/user/{id}")
    public void deleteUserById(@PathVariable Integer id){
        System.out.println("deleteUserById:"+id);
    }
    @PutMapping("/user")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id",value = "用户id",required = true,defaultValue = "99"),
            @ApiImplicitParam(name = "username",value = "用户名",required = true,defaultValue = "xiaoliu")
    })
    @ApiOperation(value = "更新用户",notes = "根据用户id更新用户名")
    @ApiIgnore
    public User updateUsernameById(String username,Integer id){
        User user=new User();
        user.setId(id);
        user.setUsername(username);
        return user;
    }

    @PostMapping("/user")
    @ApiOperation(value = "添加用户",notes = "添加用户接口")
    public User addUser(@RequestBody User user){
        return user;
    }
}
```

这里边涉及到多个 API，我来向小伙伴们分别说明：

1. @Api 注解可以用来标记当前 Controller 的功能。
2. @ApiOperation 注解用来标记一个方法的作用。
3. @ApiImplicitParam 注解用来描述一个参数，可以配置参数的中文含义，也可以给参数设置默认值，这样在接口测试的时候可以避免手动输入。
4. 如果有多个参数，则需要使用多个 @ApiImplicitParam 注解来描述，多个 @ApiImplicitParam 注解需要放在一个 @ApiImplicitParams 注解中。
5. 需要注意的是，@ApiImplicitParam 注解中虽然可以指定参数是必填的，但是却不能代替 @RequestParam(required = true) ，前者的必填只是在 Swagger2 框架内必填，抛弃了 Swagger2 ，这个限制就没用了，所以假如开发者需要指定一个参数必填， @RequestParam(required = true) 注解还是不能省略。
6. 如果参数是一个对象（例如上文的更新接口），对于参数的描述也可以放在实体类中。例如下面一段代码：

```java
@ApiModel(value = "用户实体类",description = "用户信息描述类")
public class User {
    @ApiModelProperty(value = "用户id")
    private Integer id;
    @ApiModelProperty(value = "用户名")
    private String username;
    @ApiModelProperty(value = "用户地址")
    private String address;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
```

好了，经过如上配置之后，接下来，刷新刚刚打开的页面，可以看到如下效果：

[![img](http://www.javaboy.org/images/boot2/17-2.png)](http://www.javaboy.org/images/boot2/17-2.png)

可以看到，所有的接口这里都列出来了，包括接口请求方式，接口地址以及接口的名字等，点开一个接口，可以看到如下信息：

[![img](http://www.javaboy.org/images/boot2/17-3.png)](http://www.javaboy.org/images/boot2/17-3.png)

可以看到，接口的参数，参数要求，参数默认值等等统统都展示出来了，参数类型下的 query 表示参数以 `key/value` 的形式传递，点击右上角的 Try it out，就可以进行接口测试：

[![img](http://www.javaboy.org/images/boot2/17-4.png)](http://www.javaboy.org/images/boot2/17-4.png)

点击 Execute 按钮，表示发送请求进行测试。测试结果会展示在下面的 Response 中。

小伙伴们注意，参数类型下面的 query 表示参数以 key/value 的形式传递，这里的值也可能是 body，body 表示参数以请求体的方式传递，例如上文的更新接口，如下：

[![img](http://www.javaboy.org/images/boot2/17-5.png)](http://www.javaboy.org/images/boot2/17-5.png)

当然还有一种可能就是这里的参数为 path，表示参数放在路径中传递，例如根据 id 查询用户的接口：

[![img](http://www.javaboy.org/images/boot2/17-6.png)](http://www.javaboy.org/images/boot2/17-6.png)

当然，除了这些之外，还有一些响应值的注解，都比较简单，小伙伴可以自己摸索下。

## 在 Security 中的配置

如果我们的 Spring Boot 项目中集成了 Spring Security，那么如果不做额外配置，Swagger2 文档可能会被拦截，此时只需要在 Spring Security 的配置类中重写 configure 方法，添加如下过滤即可：

```java
@Override
public void configure(WebSecurity web) throws Exception {
    web.ignoring()
            .antMatchers("/swagger-ui.html")
            .antMatchers("/v2/**")
            .antMatchers("/swagger-resources/**");
}
```

如此之后，Swagger2 文件就不需要认证就能访问了。