## 需要servlet3.0以上版本

## 如果需要在servlet3.0一下版本的web容器中运行（如：jetty7 tomcat6）中运行，需要做一下适配：
    
- pom文件修改

    ##### 改为war形式
    <pre><code>&lt;packaging>war&lt;/packaging></code></pre>

    ##### springboot版本降到 1.x，排除掉所有动态注册servlet和filter的代码，servlet3.0以上才支持动态注册
        <parent>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-parent</artifactId>
            <version>1.5.18.RELEASE</version>
            <relativePath/>
        </parent>
   ##### 设置外置jetty容器启动方式
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
            <exclusions>
                <exclusion>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-tomcat</artifactId>
                </exclusion>
            </exclusions>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-jetty</artifactId>
        </dependency>
        <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>
        </plugins>
        </build>
        <profiles>
        <!--配置profile可方便根据运行环境对配置文件做对应的修改，防止信息泄露-->
        <profile>
            <id>prod</id>
            <build>
                <resources>
                    <resource>
                        <directory>src/main/resources</directory>
                        <excludes>
                            <exclude>配置文件</exclude>
                            <exclude>log配置文件</exclude>
                        </excludes>
                    </resource>
                </resources>
            </build>
            <dependencies>
                <dependency>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-starter-jetty</artifactId>
                    <scope>provided</scope>
                </dependency>
            </dependencies>
        </profile>
        </profiles>
    ##### 添加log4j2并支持yml格式
        <dependency>
            <groupId>com.fasterxml.jackson.dataformat</groupId>
            <artifactId>jackson-dataformat-yaml</artifactId>
        </dependency>

        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-log4j2</artifactId>
        </dependency>
    ##### 添加servlet2.5适配器
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-legacy</artifactId>
            <version>1.1.0.RELEASE</version>
        </dependency>
    ##### 手动添加thymeleaf,由于springboot1.x使用的thymeleaf版本不是最新，有些性能上的提升只有在最新版才能体现
        <dependency>
            <groupId>org.hibernate</groupId>
            <artifactId>hibernate-validator</artifactId>
            <version>5.1.1.Final</version>
        </dependency>

        <dependency>
            <groupId>org.thymeleaf</groupId>
            <artifactId>thymeleaf</artifactId>
            <version>3.0.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>org.thymeleaf</groupId>
            <artifactId>thymeleaf-spring4</artifactId>
            <version>3.0.1.RELEASE</version>
        </dependency>
        <dependency>
            <groupId>nz.net.ultraq.thymeleaf</groupId>
            <artifactId>thymeleaf-layout-dialect</artifactId>
            <version>2.0.3</version>
        </dependency>
    ##### 手动添加guava类库
         <dependency>
            <groupId>com.google.guava</groupId>
            <artifactId>guava</artifactId>
            <version>${guava.version}</version>
        </dependency>
- 添加web.xml
  ##### 如果不添加web.xml文件，直接把war包放到jetty7的webapps目录下的话，jetty不认为这是一个web工程，通过浏览器访问时，jetty会将相应的目录信息展示到浏览器中。

  ##### 由于servlet2.5不支持动态创建servlet，需要显示指定DispatcherServlet和项目中所用到的Filter。在src/main下创建webapp/WEN-INF目录，在该目录下创建web.xml文件
        <?xml version="1.0" encoding="UTF-8"?>
        <web-app version="2.5" xmlns="http://java.sun.com/xml/ns/javaee"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xsi:schemaLocation="http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_2_5.xsd">

        <context-param>
            <param-name>contextConfigLocation</param-name>
            <param-value>com.otms.Application</param-value>
        </context-param>

        <listener>
            <listener-class>org.springframework.boot.legacy.context.web.SpringBootContextLoaderListener</listener-class>
        </listener>

        <!--拦截请求，将请求信息添加到RequestContextHolder中，方便直接调用RequestContextHolder.getRequestAttributes()获取请求信息-->
        <filter>
            <filter-name>traceRequestFilter</filter-name>
            <filter-class>org.springframework.boot.web.filter.OrderedRequestContextFilter</filter-class>
        </filter>

        <!--shiro的过滤器显示指定，否则shiro不生效-->
        <filter>
            <filter-name>shiroFilter</filter-name>
            <filter-class>org.springframework.web.filter.DelegatingFilterProxy</filter-class>
            <init-param>
                <param-name>targetFilterLifecycle</param-name>
                <param-value>true</param-value>
            </init-param>
        </filter>

        <filter-mapping>
            <filter-name>traceRequestFilter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>

        <filter-mapping>
            <filter-name>shiroFilter</filter-name>
            <url-pattern>/*</url-pattern>
        </filter-mapping>

        <servlet>
            <servlet-name>appServlet</servlet-name>
            <servlet-class>org.springframework.web.servlet.DispatcherServlet</servlet-class>
            <init-param>
                <param-name>contextAttribute</param-name>
                <param-value>org.springframework.web.context.WebApplicationContext.ROOT</param-value>
            </init-param>
            <load-on-startup>1</load-on-startup>
        </servlet>

        <servlet-mapping>
            <servlet-name>appServlet</servlet-name>
            <url-pattern>/*</url-pattern>
        </servlet-mapping>

        </web-app>

- .java文件的修改
    ##### 修改Application.java 文件，不再继承自SpringBootServletInitializer
        @SpringBootApplication(exclude ={DataSourceAutoConfiguration.clas})
        @MapperScan("com.template.project.*.*.mapper")
        public class Application{

            public static void main(String[] args) {
                SpringApplication.run(Application.class, args);
            }
        }
    ##### webMVC config需要改为继承自WebMvcConfigurerAdapter
        public class ResourcesConfig extends WebMvcConfigurerAdapter{
            ...
        }
    #### 添加MultipartResolver，将默认由StandardServletMultipartResolver（需要servlet3.0）文件上传方式，改为CommonsMultipartResolver
        @Bean
        // 解决文件上传默认resolver为StandardServletMultipartResolver(需servlet2.5)的问题
        public MultipartResolver multipartResolver() {
            return new CommonsMultipartResolver();
        }

至此，servlet2.5相关配置添加完毕。还有些配置需要按需添加，如：encodingFilter等。
--
参考：[Deploying a WAR in an Old (Servlet 2.5) Container](https://docs.spring.io/spring-boot/docs/1.5.18.RELEASE/reference/htmlsingle/#howto-servlet-2-5)
    