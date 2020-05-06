# 消息服务——AcitiveMQ安装

这里我们将其安装在Windows上

【1】下载压缩包

下载地址：http://activemq.apache.org/activemq-5122-release.html

![1588334772176](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588334772176.png)
【2】安装

① 解压到指定路径

![1588334823652](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588334823652.png)

② 进入win64文件夹

    内部有两个文件夹，分别对应32位和64位操作系统

![1588334871134](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588334871134.png)

③ 注册为windows服务

首先呢，可以使用win64下的activemq.bat启动服务。但是关闭该窗口，服务停止。故需要将activemq注册为windows服务。

    使用 InstallService.bat注册服务

![1588334934063](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588334934063.png)

使用activemq.bat启动服务如下：

![1588334956626](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588334956626.png)

进入控制台端口默认为8161，61616为默认对外服务端口。

当端口号冲突时，可以修改这两个端口号。进入conf目录下修改activemq.xml-修改里面的61616端口。修改jetty.xml-修改里面的8161端口。

浏览器输入：http://localhost:8161/，出现如下页面则安装成功。

![1588334999958](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588334999958.png)
【3】添加密码

ActiveMQ默认用户名密码为admin=admin,可以在conf/user.properties中找到。如下所示：
在这里插入图片描述

如何修改密码？

修改activemq.xml，在broker标签内部添加配置如下：

```xml
<plugins>
	<simpleAuthenticationPlugin>
		<users>
			<authenticationUser username="hh_mq" password="hh_mq@123456" groups="users,admins"/>
		</users>
	</simpleAuthenticationPlugin>
</plugins>
```




