### 第一章	搭建项目框架与集成Mybatis、Redis



#### 1. 项目框架

- ##### controller层

    - SampleController.java

- ##### dao层

    - UserDao.java

- ##### domain层

    - User.java

- ##### result层

    - CodeMsg.java
    - Result.java

- ##### service层

    - UserService.java



##### 各层设置含义

- **controller层**：接口控制层，对接用户访问请求

  ​	Controller层负责具体的业务模块流程的控制，在此层里面要调用Serice层的接口来控制业务流程，控制的配置也同样是在Spring的配置文件里面进行，针对具体的业务流程，会有不同的控制器，我们具体的设计过程中可以将流程进行抽象归纳，设计出可以重复利用的子单元流程模块，这样不仅使程序结构变得清晰，也大大减少了代码量。

- **dao层**：数据访问对象层(Data Access Object, DAO)，持久化数据，负责与数据库进行联络的任务都封装在此

  ​	DAO层的设计首先是设计DAO的接口，然后在Spring的配置文件中定义此接口的实现类，然后就可在模块中调用此接口来进行数据业务的处理，而不用关心此接口的具体实现类是哪个类，显得结构非常清晰，DAO层的数据源配置，以及有关数据库连接的参数都在Spring的配置文件中进行配置。

  ​	注意不是汉语拼音发音的“道”层，是英文字母的D-A-O，这里是引用domain层里的实体对象进行crud操作的数据库操作相关。

- **domain层**：数据表对象层，通常用于放置 该系统中与数据库表一一对应的 JavaBean

  ​	model层 封装的是前端JS脚本需要使用的数据

  ​    域对象层，通俗的讲就是被进行crud(增删改查)操作的对象，如学生管理系统里的学生对象、班级对象等。

- **result层**：封装返回结果，使代码变得简洁优雅



- **service层**：service层主要负责业务模块的逻辑应用设计。

  ​	同样是首先设计接口，再设计其实现的类，接着再Spring的配置文件中配置其实现的关联。这样我们就可以在应用中调用Service接口来进行业务处理。Service层的业务实现，具体要调用到已定义的DAO层的接口，封装Service层的业务逻辑有利于通用的业务逻辑的独立性和重复利用性，程序显得非常简洁。

  ​	如果细分，分为数据操作service层和业务逻辑service层，数据操作service层是引用dao层数据库操作相关代码，进行数据复杂操作；业务逻辑service层，即实现业务功能逻辑代码，可以引用数据操作service层。

- **view视图层**：view层与controller层结合紧密，需要二者协同工作。View层主要负责前台jsp页面的表示。





#### 集成Mybatis

- ##### 集成Mybatis

    - domain层

      User.java

      ```java
      public class User {
          private int id;
          private String name;
          /**
           *Generate Getter and Setter 
           */
      }
      ```

    - dao层

      UserDao.java

      ```java
      @Mapper
      public interface UserDao {
      
          @Select("select * from user where id = #{id}")
          public User getById(@Param("id") int id);
      
          @Insert("insert into user(id,name) values(#{id}, #{name})")
          public int insert(User user);
      }
      ```

- ##### 测试连接数据库

  ​	以下是任务驱动

    - controller层

      SamController.java

      ```java
      @RequestMapping("/db/get")
          @ResponseBody
          public Result<User> dbGet() {
      
              User user = userService.getById(1);
              return Result.success(user);
      
          }
      ```

    - service层

      UserService.java

      ```java
      
      @Service
      public class UserService {
      
          @Autowired
          UserDao userDao;
      
          public User getById(int id) {
              return userDao.getById(id);
          }
      }
      ```

    - dao层

      UserDao.java

      ```java
      package ...
      import ...
      
      @Mapper
      public interface UserDao {
      
          @Select("select * from user where id = #{id}")
          public User getById(@Param("id") int id);
      
      }
      ```

- ##### 测试数据库事务

  @Transactional



#### 集成Redis

































































#### 集成Mybatis可能遇到的问题

​	注意 jar包与MySQL版本一致

​		MySQL版本

<div align=center><img width="650" src="https://github.com/GavinZJZhang/G-Flashsale/blob/main/imgReadMe/img/image-20210427145018554.png"/></div>

​		pom.xml

<div align=center><img width="600" src="https://github.com/GavinZJZhang/G-Flashsale/blob/main/imgReadMe/img/image-20210427145127788.png"/></div>

​		jar包依赖Libraries

<div align=center><img width="400" src="https://github.com/GavinZJZhang/G-Flashsale/blob/main/imgReadMe/img/image-20210427145453844.png"/></div>



#### 集成Redis可能遇到的问题

##### 可能遇到的问题1

问题描述：

spring boot configuration annotation processor not configured

问题类型定义：缺少依赖

错误解决方案：

https://blog.csdn.net/weixin_42362496/article/details/103805993



##### 可能遇到的问题2

问题描述：

org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sampleController': Unsatisfied dependency expressed through field 'redisService';

问题类型定义：循环依赖问题

```java
"C:\Program Files\Java\jdk1.8.0_251\bin\java.exe" -XX:TieredStopAtLevel=1 -noverify -Dspring.output.ansi.enabled=always "-javaagent:E:\Jetbrains\IntelliJ IDEA\IntelliJ IDEA 2020.3.3\lib\idea_rt.jar=65517:E:\Jetbrains\IntelliJ IDEA\IntelliJ IDEA 2020.3.3\bin" -Dcom.sun.management.jmxremote -Dspring.jmx.enabled=true -Dspring.liveBeansView.mbeanDomain -Dspring.application.admin.enabled=true -Dfile.encoding=UTF-8 -classpath "C:\Program Files\Java\jdk1.8.0_251\jre\lib\charsets.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\deploy.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\access-bridge-64.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\cldrdata.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\dnsns.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\jaccess.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\jfxrt.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\localedata.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\nashorn.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\sunec.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\sunjce_provider.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\sunmscapi.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\sunpkcs11.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\ext\zipfs.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\javaws.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\jce.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\jfr.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\jfxswt.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\jsse.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\management-agent.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\plugin.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\resources.jar;C:\Program Files\Java\jdk1.8.0_251\jre\lib\rt.jar;E:\历史项目\G-FlashSale\target\classes;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-starter-web\1.5.8.RELEASE\spring-boot-starter-web-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-starter\1.5.8.RELEASE\spring-boot-starter-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot\1.5.8.RELEASE\spring-boot-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-autoconfigure\1.5.8.RELEASE\spring-boot-autoconfigure-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-starter-logging\1.5.8.RELEASE\spring-boot-starter-logging-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\ch\qos\logback\logback-classic\1.1.11\logback-classic-1.1.11.jar;C:\Users\dell\.m2\repository\ch\qos\logback\logback-core\1.1.11\logback-core-1.1.11.jar;C:\Users\dell\.m2\repository\org\slf4j\jcl-over-slf4j\1.7.25\jcl-over-slf4j-1.7.25.jar;C:\Users\dell\.m2\repository\org\slf4j\jul-to-slf4j\1.7.25\jul-to-slf4j-1.7.25.jar;C:\Users\dell\.m2\repository\org\slf4j\log4j-over-slf4j\1.7.25\log4j-over-slf4j-1.7.25.jar;C:\Users\dell\.m2\repository\org\springframework\spring-core\4.3.12.RELEASE\spring-core-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\yaml\snakeyaml\1.17\snakeyaml-1.17.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-starter-tomcat\1.5.8.RELEASE\spring-boot-starter-tomcat-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\org\apache\tomcat\embed\tomcat-embed-core\8.5.23\tomcat-embed-core-8.5.23.jar;C:\Users\dell\.m2\repository\org\apache\tomcat\tomcat-annotations-api\8.5.23\tomcat-annotations-api-8.5.23.jar;C:\Users\dell\.m2\repository\org\apache\tomcat\embed\tomcat-embed-el\8.5.23\tomcat-embed-el-8.5.23.jar;C:\Users\dell\.m2\repository\org\apache\tomcat\embed\tomcat-embed-websocket\8.5.23\tomcat-embed-websocket-8.5.23.jar;C:\Users\dell\.m2\repository\org\hibernate\hibernate-validator\5.3.5.Final\hibernate-validator-5.3.5.Final.jar;C:\Users\dell\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.8.10\jackson-databind-2.8.10.jar;C:\Users\dell\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.8.0\jackson-annotations-2.8.0.jar;C:\Users\dell\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.8.10\jackson-core-2.8.10.jar;C:\Users\dell\.m2\repository\org\springframework\spring-web\4.3.12.RELEASE\spring-web-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\spring-aop\4.3.12.RELEASE\spring-aop-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\spring-beans\4.3.12.RELEASE\spring-beans-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\spring-context\4.3.12.RELEASE\spring-context-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\spring-webmvc\4.3.12.RELEASE\spring-webmvc-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\spring-expression\4.3.12.RELEASE\spring-expression-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-starter-thymeleaf\1.5.8.RELEASE\spring-boot-starter-thymeleaf-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\org\thymeleaf\thymeleaf-spring4\2.1.5.RELEASE\thymeleaf-spring4-2.1.5.RELEASE.jar;C:\Users\dell\.m2\repository\org\thymeleaf\thymeleaf\2.1.5.RELEASE\thymeleaf-2.1.5.RELEASE.jar;C:\Users\dell\.m2\repository\ognl\ognl\3.0.8\ognl-3.0.8.jar;C:\Users\dell\.m2\repository\org\javassist\javassist\3.21.0-GA\javassist-3.21.0-GA.jar;C:\Users\dell\.m2\repository\org\unbescape\unbescape\1.1.0.RELEASE\unbescape-1.1.0.RELEASE.jar;C:\Users\dell\.m2\repository\org\slf4j\slf4j-api\1.7.25\slf4j-api-1.7.25.jar;C:\Users\dell\.m2\repository\nz\net\ultraq\thymeleaf\thymeleaf-layout-dialect\1.4.0\thymeleaf-layout-dialect-1.4.0.jar;C:\Users\dell\.m2\repository\org\codehaus\groovy\groovy\2.4.12\groovy-2.4.12.jar;C:\Users\dell\.m2\repository\org\mybatis\spring\boot\mybatis-spring-boot-starter\2.1.3\mybatis-spring-boot-starter-2.1.3.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-starter-jdbc\1.5.8.RELEASE\spring-boot-starter-jdbc-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\org\apache\tomcat\tomcat-jdbc\8.5.23\tomcat-jdbc-8.5.23.jar;C:\Users\dell\.m2\repository\org\apache\tomcat\tomcat-juli\8.5.23\tomcat-juli-8.5.23.jar;C:\Users\dell\.m2\repository\org\springframework\spring-jdbc\4.3.12.RELEASE\spring-jdbc-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\springframework\spring-tx\4.3.12.RELEASE\spring-tx-4.3.12.RELEASE.jar;C:\Users\dell\.m2\repository\org\mybatis\spring\boot\mybatis-spring-boot-autoconfigure\2.1.3\mybatis-spring-boot-autoconfigure-2.1.3.jar;C:\Users\dell\.m2\repository\org\mybatis\mybatis\3.5.5\mybatis-3.5.5.jar;C:\Users\dell\.m2\repository\org\mybatis\mybatis-spring\2.0.5\mybatis-spring-2.0.5.jar;C:\Users\dell\.m2\repository\com\alibaba\druid\1.0.5\druid-1.0.5.jar;C:\Users\dell\.m2\repository\mysql\mysql-connector-java\8.0.20\mysql-connector-java-8.0.20.jar;C:\Users\dell\.m2\repository\com\google\protobuf\protobuf-java\3.6.1\protobuf-java-3.6.1.jar;C:\Users\dell\.m2\repository\redis\clients\jedis\2.9.0\jedis-2.9.0.jar;C:\Users\dell\.m2\repository\org\apache\commons\commons-pool2\2.4.2\commons-pool2-2.4.2.jar;C:\Users\dell\.m2\repository\com\alibaba\fastjson\1.2.38\fastjson-1.2.38.jar;C:\Users\dell\.m2\repository\org\springframework\boot\spring-boot-configuration-processor\1.5.8.RELEASE\spring-boot-configuration-processor-1.5.8.RELEASE.jar;C:\Users\dell\.m2\repository\com\vaadin\external\google\android-json\0.0.20131108.vaadin1\android-json-0.0.20131108.vaadin1.jar" com.gavyselflearn.flashsale.MainApplication

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.8.RELEASE)

2021-04-28 17:01:02.772  INFO 16288 --- [           main] c.g.flashsale.MainApplication            : Starting MainApplication on DESKTOP-OL7R4SU with PID 16288 (started by gdell in E:\历史项目\G-FlashSale)
2021-04-28 17:01:02.778  INFO 16288 --- [           main] c.g.flashsale.MainApplication            : No active profile set, falling back to default profiles: default
2021-04-28 17:01:03.248  INFO 16288 --- [           main] ationConfigEmbeddedWebApplicationContext : Refreshing org.springframework.boot.context.embedded.AnnotationConfigEmbeddedWebApplicationContext@6a472554: startup date [Wed Apr 28 17:01:03 CST 2021]; root of context hierarchy
2021-04-28 17:01:04.310  INFO 16288 --- [           main] s.b.c.e.t.TomcatEmbeddedServletContainer : Tomcat initialized with port(s): 8080 (http)
2021-04-28 17:01:04.316  INFO 16288 --- [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2021-04-28 17:01:04.316  INFO 16288 --- [           main] org.apache.catalina.core.StandardEngine  : Starting Servlet Engine: Apache Tomcat/8.5.23
2021-04-28 17:01:04.391  INFO 16288 --- [ost-startStop-1] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2021-04-28 17:01:04.391  INFO 16288 --- [ost-startStop-1] o.s.web.context.ContextLoader            : Root WebApplicationContext: initialization completed in 1143 ms
2021-04-28 17:01:04.487  INFO 16288 --- [ost-startStop-1] o.s.b.w.servlet.ServletRegistrationBean  : Mapping servlet: 'dispatcherServlet' to [/]
2021-04-28 17:01:04.490  INFO 16288 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'characterEncodingFilter' to: [/*]
2021-04-28 17:01:04.491  INFO 16288 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'hiddenHttpMethodFilter' to: [/*]
2021-04-28 17:01:04.491  INFO 16288 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'httpPutFormContentFilter' to: [/*]
2021-04-28 17:01:04.491  INFO 16288 --- [ost-startStop-1] o.s.b.w.servlet.FilterRegistrationBean   : Mapping filter: 'requestContextFilter' to: [/*]
2021-04-28 17:01:04.693  WARN 16288 --- [           main] ationConfigEmbeddedWebApplicationContext : Exception encountered during context initialization - cancelling refresh attempt: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sampleController': Unsatisfied dependency expressed through field 'redisService'; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'redisService': Unsatisfied dependency expressed through field 'jedisPool'; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'JedisFactory' defined in class path resource [com/gavyselflearn/flashsale/redis/RedisService.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [redis.clients.jedis.JedisPool]: Circular reference involving containing bean 'redisService' - consider declaring the factory method as static for independence from its containing instance. Factory method 'JedisFactory' threw exception; nested exception is java.lang.NullPointerException
2021-04-28 17:01:04.695  INFO 16288 --- [           main] o.apache.catalina.core.StandardService   : Stopping service [Tomcat]
2021-04-28 17:01:04.712  INFO 16288 --- [           main] utoConfigurationReportLoggingInitializer : 

Error starting ApplicationContext. To display the auto-configuration report re-run your application with 'debug' enabled.
2021-04-28 17:01:04.718 ERROR 16288 --- [           main] o.s.boot.SpringApplication               : Application startup failed

org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'sampleController': Unsatisfied dependency expressed through field 'redisService'; nested exception is org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'redisService': Unsatisfied dependency expressed through field 'jedisPool'; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'JedisFactory' defined in class path resource [com/gavyselflearn/flashsale/redis/RedisService.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [redis.clients.jedis.JedisPool]: Circular reference involving containing bean 'redisService' - consider declaring the factory method as static for independence from its containing instance. Factory method 'JedisFactory' threw exception; nested exception is java.lang.NullPointerException
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:588) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:88) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:366) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1264) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:553) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:197) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:761) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:867) ~[spring-context-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:543) ~[spring-context-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.boot.context.embedded.EmbeddedWebApplicationContext.refresh(EmbeddedWebApplicationContext.java:122) ~[spring-boot-1.5.8.RELEASE.jar:1.5.8.RELEASE]
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:693) [spring-boot-1.5.8.RELEASE.jar:1.5.8.RELEASE]
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:360) [spring-boot-1.5.8.RELEASE.jar:1.5.8.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:303) [spring-boot-1.5.8.RELEASE.jar:1.5.8.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1118) [spring-boot-1.5.8.RELEASE.jar:1.5.8.RELEASE]
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1107) [spring-boot-1.5.8.RELEASE.jar:1.5.8.RELEASE]
	at com.gavyselflearn.flashsale.MainApplication.main(MainApplication.java:10) [classes/:na]
Caused by: org.springframework.beans.factory.UnsatisfiedDependencyException: Error creating bean with name 'redisService': Unsatisfied dependency expressed through field 'jedisPool'; nested exception is org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'JedisFactory' defined in class path resource [com/gavyselflearn/flashsale/redis/RedisService.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [redis.clients.jedis.JedisPool]: Circular reference involving containing bean 'redisService' - consider declaring the factory method as static for independence from its containing instance. Factory method 'JedisFactory' threw exception; nested exception is java.lang.NullPointerException
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:588) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.annotation.InjectionMetadata.inject(InjectionMetadata.java:88) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor.postProcessPropertyValues(AutowiredAnnotationBeanPostProcessor.java:366) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.populateBean(AbstractAutowireCapableBeanFactory.java:1264) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:553) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:208) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1138) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1066) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:585) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	... 19 common frames omitted
Caused by: org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'JedisFactory' defined in class path resource [com/gavyselflearn/flashsale/redis/RedisService.class]: Bean instantiation via factory method failed; nested exception is org.springframework.beans.BeanInstantiationException: Failed to instantiate [redis.clients.jedis.JedisPool]: Circular reference involving containing bean 'redisService' - consider declaring the factory method as static for independence from its containing instance. Factory method 'JedisFactory' threw exception; nested exception is java.lang.NullPointerException
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:599) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1173) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1067) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:513) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:483) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory$1.getObject(AbstractBeanFactory.java:306) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:230) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:302) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:202) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.config.DependencyDescriptor.resolveCandidate(DependencyDescriptor.java:208) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.doResolveDependency(DefaultListableBeanFactory.java:1138) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.resolveDependency(DefaultListableBeanFactory.java:1066) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor$AutowiredFieldElement.inject(AutowiredAnnotationBeanPostProcessor.java:585) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	... 32 common frames omitted
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [redis.clients.jedis.JedisPool]: Circular reference involving containing bean 'redisService' - consider declaring the factory method as static for independence from its containing instance. Factory method 'JedisFactory' threw exception; nested exception is java.lang.NullPointerException
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:189) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:588) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	... 44 common frames omitted
Caused by: java.lang.NullPointerException: null
	at com.gavyselflearn.flashsale.redis.RedisService.JedisFactory(RedisService.java:88) ~[classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_251]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_251]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_251]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_251]
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:162) ~[spring-beans-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	... 45 common frames omitted


Process finished with exit code 1

```



##### 可能遇到的问题三

问题描述：连不上Redis

​	java.net.SocketTimeoutException: connect timed out

问题类型定义：服务器网络设置问题

问题解决方案：修改服务器安全组

<img src="C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20210428175531955.png" alt="image-20210428175531955" style="zoom: 45%;" />

<img src="C:\Users\dell\AppData\Roaming\Typora\typora-user-images\image-20210428180056823.png" alt="image-20210428180056823" style="zoom: 40%;" />



问题细节：

```java
2021-04-28 17:40:12.792 ERROR 8924 --- [nio-8080-exec-9] o.a.c.c.C.[.[.[/].[dispatcherServlet]    : Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed; nested exception is redis.clients.jedis.exceptions.JedisConnectionException: Could not get a resource from the pool] with root cause

java.net.SocketTimeoutException: connect timed out
	at java.net.DualStackPlainSocketImpl.waitForConnect(Native Method) ~[na:1.8.0_251]
	at java.net.DualStackPlainSocketImpl.socketConnect(DualStackPlainSocketImpl.java:85) ~[na:1.8.0_251]
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350) ~[na:1.8.0_251]
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206) ~[na:1.8.0_251]
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188) ~[na:1.8.0_251]
	at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:172) ~[na:1.8.0_251]
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392) ~[na:1.8.0_251]
	at java.net.Socket.connect(Socket.java:606) ~[na:1.8.0_251]
	at redis.clients.jedis.Connection.connect(Connection.java:184) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.BinaryClient.connect(BinaryClient.java:93) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.BinaryJedis.connect(BinaryJedis.java:1767) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.JedisFactory.makeObject(JedisFactory.java:106) ~[jedis-2.9.0.jar:na]
	at org.apache.commons.pool2.impl.GenericObjectPool.create(GenericObjectPool.java:868) ~[commons-pool2-2.4.2.jar:2.4.2]
	at org.apache.commons.pool2.impl.GenericObjectPool.borrowObject(GenericObjectPool.java:435) ~[commons-pool2-2.4.2.jar:2.4.2]
	at org.apache.commons.pool2.impl.GenericObjectPool.borrowObject(GenericObjectPool.java:363) ~[commons-pool2-2.4.2.jar:2.4.2]
	at redis.clients.util.Pool.getResource(Pool.java:49) ~[jedis-2.9.0.jar:na]
	at redis.clients.jedis.JedisPool.getResource(JedisPool.java:226) ~[jedis-2.9.0.jar:na]
	at com.gavyselflearn.flashsale.redis.RedisService.get(RedisService.java:20) ~[classes/:na]
	at com.gavyselflearn.flashsale.controller.SampleController.redisGet(SampleController.java:73) ~[classes/:na]
	at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method) ~[na:1.8.0_251]
	at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62) ~[na:1.8.0_251]
	at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43) ~[na:1.8.0_251]
	at java.lang.reflect.Method.invoke(Method.java:498) ~[na:1.8.0_251]
	at org.springframework.web.method.support.InvocableHandlerMethod.doInvoke(InvocableHandlerMethod.java:205) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.method.support.InvocableHandlerMethod.invokeForRequest(InvocableHandlerMethod.java:133) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod.invokeAndHandle(ServletInvocableHandlerMethod.java:97) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.invokeHandlerMethod(RequestMappingHandlerAdapter.java:827) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter.handleInternal(RequestMappingHandlerAdapter.java:738) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.mvc.method.AbstractHandlerMethodAdapter.handle(AbstractHandlerMethodAdapter.java:85) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doDispatch(DispatcherServlet.java:967) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.DispatcherServlet.doService(DispatcherServlet.java:901) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.processRequest(FrameworkServlet.java:970) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.servlet.FrameworkServlet.doGet(FrameworkServlet.java:861) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:635) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.servlet.FrameworkServlet.service(FrameworkServlet.java:846) ~[spring-webmvc-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at javax.servlet.http.HttpServlet.service(HttpServlet.java:742) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:231) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.tomcat.websocket.server.WsFilter.doFilter(WsFilter.java:52) ~[tomcat-embed-websocket-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.RequestContextFilter.doFilterInternal(RequestContextFilter.java:99) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.HttpPutFormContentFilter.doFilterInternal(HttpPutFormContentFilter.java:108) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.HiddenHttpMethodFilter.doFilterInternal(HiddenHttpMethodFilter.java:81) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.springframework.web.filter.CharacterEncodingFilter.doFilterInternal(CharacterEncodingFilter.java:197) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.springframework.web.filter.OncePerRequestFilter.doFilter(OncePerRequestFilter.java:107) ~[spring-web-4.3.12.RELEASE.jar:4.3.12.RELEASE]
	at org.apache.catalina.core.ApplicationFilterChain.internalDoFilter(ApplicationFilterChain.java:193) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.ApplicationFilterChain.doFilter(ApplicationFilterChain.java:166) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardWrapperValve.invoke(StandardWrapperValve.java:199) ~[tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardContextValve.invoke(StandardContextValve.java:96) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.authenticator.AuthenticatorBase.invoke(AuthenticatorBase.java:478) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardHostValve.invoke(StandardHostValve.java:140) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.valves.ErrorReportValve.invoke(ErrorReportValve.java:81) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.core.StandardEngineValve.invoke(StandardEngineValve.java:87) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.catalina.connector.CoyoteAdapter.service(CoyoteAdapter.java:342) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.http11.Http11Processor.service(Http11Processor.java:803) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.AbstractProcessorLight.process(AbstractProcessorLight.java:66) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.coyote.AbstractProtocol$ConnectionHandler.process(AbstractProtocol.java:868) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.tomcat.util.net.NioEndpoint$SocketProcessor.doRun(NioEndpoint.java:1459) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at org.apache.tomcat.util.net.SocketProcessorBase.run(SocketProcessorBase.java:49) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149) [na:1.8.0_251]
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624) [na:1.8.0_251]
	at org.apache.tomcat.util.threads.TaskThread$WrappingRunnable.run(TaskThread.java:61) [tomcat-embed-core-8.5.23.jar:8.5.23]
	at java.lang.Thread.run(Thread.java:748) [na:1.8.0_251]


```





