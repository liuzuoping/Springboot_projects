# SpringBoot实现定时任务

# Spring Boot定时任务的两种实现方式

在 Spring + SpringMVC 环境中，一般来说，要实现定时任务，我们有两中方案，一种是使用 Spring 自带的定时任务处理器 @Scheduled 注解，另一种就是使用第三方框架 Quartz ，Spring Boot 源自 Spring+SpringMVC ，因此天然具备这两个 Spring 中的定时任务实现策略，当然也支持 Quartz，本文我们就来看下 Spring Boot 中两种定时任务的实现方式。



## @Scheduled

使用 @Scheduled 非常容易，直接创建一个 Spring Boot 项目，并且添加 web 依赖 `spring-boot-starter-web`，项目创建成功后，添加 `@EnableScheduling` 注解，开启定时任务：

```java
@SpringBootApplication
@EnableScheduling
public class ScheduleApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScheduleApplication.class, args);
    }
}
```

接下来配置定时任务：

```java
@Service
public class HelloService {
    @Scheduled(fixedDelay = 2000)
    public void fixedDelay(){
        System.out.println("fixedDelay>>>"+new Date());
    }
    @Scheduled(fixedRate = 2000)
    public void fixedRate(){
        System.out.println("fixedRate>>>"+new Date());
    }
}
```

1. 首先使用 @Scheduled 注解开启一个定时任务。
2. fixedRate 表示任务执行之间的时间间隔，具体是指两次任务的开始时间间隔，即第二次任务开始时，第一次任务可能还没结束。
3. fixedDelay 表示任务执行之间的时间间隔，具体是指本次任务结束到下次任务开始之间的时间间隔。
4. initialDelay 表示首次任务启动的延迟时间。
5. 所有时间的单位都是毫秒。

上面这是一个基本用法，除了这几个基本属性之外，@Scheduled 注解也支持 cron 表达式，使用 cron 表达式，可以非常丰富的描述定时任务的时间。cron 表达式格式如下：

> [秒] [分] [小时] [日] [月] [周] [年]

具体取值如下：

| 序号 | 说明 | 是否必填 | 允许填写的值    | 允许的通配符 |
| :--- | :--- | :------- | :-------------- | :----------- |
| 1    | 秒   | 是       | 0-59            | - * /        |
| 2    | 分   | 是       | 0-59            | - * /        |
| 3    | 时   | 是       | 0-23            | - * /        |
| 4    | 日   | 是       | 1-31            | - * ? / L W  |
| 5    | 月   | 是       | 1-12 or JAN-DEC | - * /        |
| 6    | 周   | 是       | 1-7 or SUN-SAT  | - * ? / L ## |
| 7    | 年   | 否       | 1970-2099       | - * /        |

**这一块需要大家注意的是，月份中的日期和星期可能会起冲突，因此在配置时这两个得有一个是 `?`**

**通配符含义：**

- `?` 表示不指定值，即不关心某个字段的取值时使用。需要注意的是，月份中的日期和星期可能会起冲突，因此在配置时这两个得有一个是 `?`
- `*` 表示所有值，例如:在秒的字段上设置 `*`,表示每一秒都会触发
- `,` 用来分开多个值，例如在周字段上设置 “MON,WED,FRI” 表示周一，周三和周五触发
- `-` 表示区间，例如在秒上设置 “10-12”,表示 10,11,12秒都会触发
- `/` 用于递增触发，如在秒上面设置”5/15” 表示从5秒开始，每增15秒触发(5,20,35,50)
- `##` 序号(表示每月的第几个周几)，例如在周字段上设置”6##3”表示在每月的第三个周六，(用 在母亲节和父亲节再合适不过了)
- 周字段的设置，若使用英文字母是不区分大小写的 ，即 MON 与mon相同
- `L` 表示最后的意思。在日字段设置上，表示当月的最后一天(依据当前月份，如果是二月还会自动判断是否是润年), 在周字段上表示星期六，相当于”7”或”SAT”（注意周日算是第一天）。如果在”L”前加上数字，则表示该数据的最后一个。例如在周字段上设置”6L”这样的格式,则表示”本月最后一个星期五”
- `W` 表示离指定日期的最近工作日(周一至周五)，例如在日字段上设置”15W”，表示离每月15号最近的那个工作日触发。如果15号正好是周六，则找最近的周五(14号)触发, 如果15号是周未，则找最近的下周一(16号)触发，如果15号正好在工作日(周一至周五)，则就在该天触发。如果指定格式为 “1W”,它则表示每月1号往后最近的工作日触发。如果1号正是周六，则将在3号下周一触发。(注，”W”前只能设置具体的数字,不允许区间”-“)
- `L` 和 `W` 可以一组合使用。如果在日字段上设置”LW”,则表示在本月的最后一个工作日触发(一般指发工资 )

例如，在 @Scheduled 注解中来一个简单的 cron 表达式，每隔5秒触发一次，如下：

```java
    @Scheduled(cron = "0/5 * * * * *")
    public void cron() {
        System.out.println(new Date());
    }
```

上面介绍的是使用 @Scheduled 注解的方式来实现定时任务，接下来我们再来看看如何使用 Quartz 实现定时任务。

## Quartz

一般在项目中，除非定时任务涉及到的业务实在是太简单，使用 @Scheduled 注解来解决定时任务，否则大部分情况可能都是使用 Quartz 来做定时任务。在 Spring Boot 中使用 Quartz ，只需要在创建项目时，添加 Quartz 依赖即可：

[![img](http://www.javaboy.org/images/boot2/16-1.png)](http://www.javaboy.org/images/boot2/16-1.png)

项目创建完成后，也需要添加开启定时任务的注解：

```java
@SpringBootApplication
@EnableScheduling
public class QuartzApplication {

    public static void main(String[] args) {
        SpringApplication.run(QuartzApplication.class, args);
    }

}
```

Quartz 在使用过程中，有两个关键概念，一个是JobDetail（要做的事情），另一个是触发器（什么时候做），要定义 MyFirstJob，需要先定义 Job，Job 的定义有两种方式：

第一种方式，直接定义一个Bean：

```java
@Component
public class MyFirstJob {
    public void sayhello(){
        System.out.println("first job say hello"+new Date());
    }
}
```

关于这种定义方式说两点：

1. 首先将这个 Job 注册到 Spring 容器中。
2. 这种定义方式有一个缺陷，就是无法传参。

第二种定义方式，则是继承 MySecondJob 并实现默认的方法：

```java
public class MySecondJob extends QuartzJobBean {
    private String name;

    public void setName(String name) {
        this.name = name;
    }

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println("second job say hello"+name+":"+new Date());
    }
}
```

和第1种方式相比，这种方式支持传参，任务启动时，executeInternal 方法将会被执行。

Job 有了之后，接下来创建类，配置 JobDetail 和 Trigger 触发器，如下：

```java
@Configuration
public class QuartzConfig {
    @Bean
    MethodInvokingJobDetailFactoryBean methodInvokingJobDetailFactoryBean(){
        MethodInvokingJobDetailFactoryBean bean=new MethodInvokingJobDetailFactoryBean();
        bean.setTargetBeanName("myFirstJob");
        bean.setTargetMethod("sayhello");
        return bean;
    }
    @Bean
    JobDetailFactoryBean jobDetailFactoryBean(){
        JobDetailFactoryBean bean=new JobDetailFactoryBean();
        JobDataMap map = new JobDataMap();
        map.put("name","xiaoliu" );
        bean.setJobDataAsMap(map);
        bean.setJobClass(MySecondJob.class);
        return bean;
    }
    @Bean
    SimpleTriggerFactoryBean simpleTriggerFactoryBean(){
        SimpleTriggerFactoryBean bean=new SimpleTriggerFactoryBean();
        bean.setJobDetail(methodInvokingJobDetailFactoryBean().getObject());
        bean.setStartTime(new Date());
        bean.setRepeatInterval(2000);
        bean.setRepeatCount(3);
        return bean;
    }
    @Bean
    CronTriggerFactoryBean cronTriggerFactoryBean(){
        CronTriggerFactoryBean bean=new CronTriggerFactoryBean();
        bean.setJobDetail(jobDetailFactoryBean().getObject());
        bean.setCronExpression("* * * * * ?");
        return bean;
    }
    @Bean
    SchedulerFactoryBean schedulerFactoryBean(){
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        bean.setTriggers(simpleTriggerFactoryBean().getObject(),cronTriggerFactoryBean().getObject());
        return bean;
    }
}

```

关于这个配置说如下几点：

1. JobDetail 的配置有两种方式：MethodInvokingJobDetailFactoryBean 和 JobDetailFactoryBean 。
2. 使用 MethodInvokingJobDetailFactoryBean 可以配置目标 Bean 的名字和目标方法的名字，这种方式不支持传参。
3. 使用 JobDetailFactoryBean 可以配置 JobDetail ，任务类继承自 QuartzJobBean ，这种方式支持传参，将参数封装在 JobDataMap 中进行传递。
4. Trigger 是指触发器，Quartz 中定义了多个触发器，这里向大家展示其中两种的用法，SimpleTrigger 和 CronTrigger 。
5. SimpleTrigger 有点类似于前面说的 @Scheduled 的基本用法。
6. CronTrigger 则有点类似于 @Scheduled 中 cron 表达式的用法。

[![img](http://www.javaboy.org/images/boot2/16-2.png)](http://www.javaboy.org/images/boot2/16-2.png)

全部定义完成后，启动 Spring Boot 项目就可以看到定时任务的执行了。

![1588384858761](C:\Users\MI\AppData\Roaming\Typora\typora-user-images\1588384858761.png)