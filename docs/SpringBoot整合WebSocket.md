# SpringBoot整合WebSocket

### 实现群聊多点发送

pom文件引入以下依赖

```xml
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-websocket</artifactId>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>sockjs-client</artifactId>
            <version>1.1.2</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>jquery</artifactId>
            <version>3.4.1</version>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>stomp-websocket</artifactId>
            <version>2.3.3</version>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-security</artifactId>
        </dependency>
        <dependency>
            <groupId>org.webjars</groupId>
            <artifactId>webjars-locator-core</artifactId>
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

在启动类父文件夹下创建bean文件夹，并在其下新建Message.java

```java
public class Message {
    private String name;
    private String content;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
```

在启动类父文件夹下创建config文件夹，并在其下新建WebSocketConfig.java

```java
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic","/queue");
        registry.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/chat").withSockJS();
    }
}

```

在启动类父文件夹下创建controller文件夹，并在其下新建GreetingController.java

```java
@Controller
public class GreetingController {
    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Message greeting(Message message){
        return message;
    }
```

在static目录下新建chat.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>群聊</title>
    <script src="/webjars/jquery/min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
</head>
<body>
<table>
    <tr>
        <td>请输入用户名</td>
        <td><input type="text" id="name"></td>
    </tr>
    <tr>
        <td><input type="button" id="connect" value="连接"></td>
        <td><input type="button" id="disconnect" disabled="disabled" value="断开连接"></td>
    </tr>
</table>
<div id="chat" style="display: none">
    <table>
        <tr>
            <td>请输入聊天内容</td>
            <td><input type="text" id="content"></td>
            <td><input type="button" id="send" value="发送"></td>
        </tr>
    </table>
    <div id="conversation">群聊进行中......</div>
</div>
<script>
$(function (){
    $("#connect").click(function () {
        connect();
    })
    $("#disconnect").click(function () {
        if (stompClient!=null){
            stompClient.disconnect();
        }
        setConnected(false);
    })
    $("#send").click(function () {
        stompClient.send("/app/hello",{},JSON.stringify({'name':$("#name").val(),'content':$("#content").val()}))
    })
})
var stompClient=null;
function connect() {
    if (!$("#name").val()){
        return;
    }
    var socket=new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({},function (success) {
        setConnected(true);
        stompClient.subscribe('/topic/greetings',function (msg) {
            showGreeting(JSON.parse(msg.body));
        })
    })
}
function showGreeting(msg) {
    $("#conversation").append('<div>'+msg.name+':'+msg.content+'</div>');
}
function setConnected(flag) {
    $("#connect").prop("disabled",flag);
    $("#disconnect").prop("disabled",!flag);
    if (flag){
        $("#chat").show();
    } else {
        $("#chat").hide();
    }
}
</script>
</body>
</html>
```

访问http://localhost:8080/chat.html便可实现在线群聊

### 实现单聊点对点发送

在bean包下新建Chat.java

```java
public class Chat {
    private String from;
    private String content;
    private String to;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
```

在config包下新建SecurityConfig.java

```java
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    PasswordEncoder passwordEncoder(){
        return NoOpPasswordEncoder.getInstance();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("xiaoliu")
                .password("123")
                .roles("admin")
                .and()
                .withUser("sang")
                .password("123")
                .roles("user");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll();
    }
}
```

在GreetingController.java中新增以下代码

```java
    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;
//    @MessageMapping("/hello")
//    @SendTo("/topic/greetings")
//    public Message greeting(Message message){
//        return message;
//    }
    @MessageMapping("/hello")
    public void greeting(Message message){
        simpMessagingTemplate.convertAndSend("/topic/greetings",message);
    }
    @MessageMapping("/chat")
    public void chat(Principal principal, Chat chat){
        chat.setFrom(principal.getName());
        simpMessagingTemplate.convertAndSendToUser(chat.getTo(),"/queue/chat" ,chat );
    }
}
```

最后在static目录下新增onlinechat.html

```html
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>群聊</title>
    <script src="/webjars/jquery/min.js"></script>
    <script src="/webjars/sockjs-client/sockjs.min.js"></script>
    <script src="/webjars/stomp-websocket/stomp.min.js"></script>
</head>
<body>
<input type="button" id="connect" value="连接">
<input type="button" id="disconnect" disabled="disabled" value="断开连接">
<div id="conversation"></div>
<hr>
消息内容<input type="text" id="content">目标用户：<input type="text" id="to"><input type="button" value="发送" id="send">
<script>
$(function () {
    $("#connect").click(function () {
        connect();
    })
    $("#disconnect").click(function () {
        if (stompClient!=null){
            stompClient.disconnect();
        }
        setConnected(false);
    })
    $("#send").click(function () {
        stompClient.send("/app/chat",{},JSON.stringify({'to':$("#to").val(),'content':$("#content").val()}))
    })
})
var stompClient=null;
function connect() {
    var socket=new SockJS('/chat');
    stompClient = Stomp.over(socket);
    stompClient.connect({},function (success) {
        setConnected(true);
        stompClient.subscribe('/user/queue/chat',function (msg) {
            showGreeting(JSON.parse(msg.body));
        })
    })
}
function showGreeting(msg) {
    $("#conversation").append('<div>'+msg.from+':'+msg.content+'</div>');
}
function setConnected(flag) {
    $("#connect").prop("disabled",flag);
    $("#disconnect").prop("disabled",!flag);
    if (flag){
        $("#chat").show();
    } else {
        $("#chat").hide();
    }
}
</script>
</body>
</html>
```

访问http://localhost:8080/onlinechat.html便可实现在线点对点单聊