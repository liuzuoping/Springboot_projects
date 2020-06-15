## springboot整合Web基础组件

在启动类父亲中新建**MyServlet.java**

```java
@WebServlet(urlPatterns = "/myservlet")
public class MyServlet  extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("MyServlet");
    }
}
```

再新建**MyFilter.java**

```java
@WebFilter(urlPatterns = "/*")
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("MyFilter");
        filterChain.doFilter(servletRequest,servletResponse );
    }
}
```

再新建**MyRequestListener.java**

```java
@WebFilter(urlPatterns = "/*")
public class MyFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        System.out.println("MyFilter");
        filterChain.doFilter(servletRequest,servletResponse );
    }
}

```

浏览器访问

http://localhost:8080/myservlet

程序输出

```
requestInitialized
MyFilter
MyServlet
requestDestroyed
```

文件结构如下

![1592211461432](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1592211461432.png)