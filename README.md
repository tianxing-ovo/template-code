# Template Code

Spring Boot 模板代码项目，涵盖后端开发常用功能模块的示例实现。

## 技术栈

|  类别   |              技术              |
|:-----:|:----------------------------:|
|  框架   |      Spring Boot 3.5.15      |
|  ORM  |      MyBatis-Plus 3.5.7      |
|  数据库  |    MySQL、PostgreSQL（多数据源）    |
|  缓存   |      Redis、Spring Cache      |
| 分布式锁  |           Redisson           |
| 消息队列  |           RabbitMQ           |
| 规则引擎  |         Drools 8.44          |
| Excel | EasyExcel 4.0.3、Apache POI 5.4.0 |
| 模板引擎  |          Thymeleaf           |
|  工具   |        Hutool、Lombok         |

## 项目结构

```
com.ltx
├── aop                   # AOP切面与增强
│   ├── UserAop            - 切面示例(前置/后置/环绕/异常通知)
│   └── UserService        - AOP测试服务
├── common                # 公共通用组件 (Consolidated)
│   ├── annotation/        - 自定义注解 (PreAuthorize, SensitiveInfo)
│   ├── constant/          - 通用常量定义 (Constant, DatasourceConstant, RedisConstant)
│   ├── easyexcel/         - EasyExcel 格式转换与写入策略
│   ├── exception/         - 异常定义与全局异常处理
│   ├── filter/            - 自定义过滤器 (CustomFilter)
│   ├── i18n/              - 国际化 Locale 解析与资源工具
│   ├── interceptor/       - 用户信息拦截器 (CustomInterceptor)
│   ├── typehandler/       - MyBatis 类型转换器 (ListTypeHandler)
│   ├── util/              - 常用工具类 (Redis, Redisson, Thread, UserContext)
│   ├── valid/             - 自定义数据校验逻辑
│   └── Result.java        - 统一响应结果封装
├── config                # 集中式配置模块 (Consolidated)
│   ├── AutoFillConfig     - 自动填充配置(创建/更新时间)
│   ├── CacheConfig        - Spring Cache 缓存配置
│   ├── CorsConfig         - 跨域资源共享配置
│   ├── DroolsConfig       - 规则引擎配置
│   ├── FilterConfig       - 过滤器注册配置
│   ├── MybatisPlusConfig  - MyBatis-Plus分页插件配置
│   ├── RabbitMQConfig     - RabbitMQ交换机与队列配置
│   ├── RedisConfig        - RedisTemplate序列化配置
│   ├── RedissonConfig     - Redisson客户端配置
│   ├── SessionConfig      - Spring Session 分布式会话配置
│   ├── ThreadPoolConfig   - 自定义线程池与异步任务线程池配置
│   ├── ThreadPoolConfigProperties - 自定义线程池属性
│   └── WebMvcConfig       - Web MVC拦截器注册与视图映射配置
├── controller            # 控制器 (Consolidated)
│   ├── UserController     - 用户CRUD、分页、缓存
│   ├── ExportTaskController - 导出任务管理
│   ├── FileController     - 文件上传下载
│   ├── MessageController  - RabbitMQ消息发送
│   └── OrderController    - 订单规则计算
├── entity                # 数据实体
│   ├── drools/            - 规则引擎专用实体 (Order, Customer)
│   ├── po/                - 持久化对象 (User, Temp)
│   ├── pojo/BasePojo      - 基础POJO(自动填充时间)
│   ├── dto/PageDTO        - 分页结果
│   ├── query/PageQuery    - 分页查询参数
│   └── request/           - 请求参数体封装
├── enums                 # 通用枚举定义 (ErrorCode, ExportStatus, Role, Sex)
├── listener              # 数据监听器 (ApplicationListener, UserListener)
├── mapper                # 数据访问层 (UserMapper, TempMapper, ExportTaskMapper)
├── mq                    # 消息队列
│   └── rabbitmq/          - RabbitMQ 消息发送与监听组件
├── runner                # 启动引导任务 (StartupRunner)
├── scheduled             # 定时任务调度 (ScheduledTask)
└── service               # 业务逻辑层
    ├── drools/            - 规则引擎业务逻辑 (RuleService)
    ├── ExportTaskService  - 导出任务管理接口与实现
    └── UserService        - 用户核心业务逻辑与实现
```

## 功能模块

### 1. AOP切面
前置通知、后置通知、返回通知、异常通知、环绕通知的完整示例，以及基于自定义注解 `@PreAuthorize` 的权限校验。

### 2. 全局异常处理
统一处理 `CustomException`、`MethodArgumentNotValidException`、`ConstraintViolationException` 等异常，返回标准JSON格式。

### 3. 参数校验
支持实体类校验（`@Validated` 分组校验）和单个参数校验，包含自定义校验注解 `@ListValue`。

### 4. 文件上传下载
单文件上传、多文件上传、文件下载，包含文件名安全校验（防路径遍历）。

### 5. Excel导入导出
- **导入**: 支持 Apache POI 和 EasyExcel 两种方式
- **导出**: 支持导出到浏览器和异步导出到本地，包含自定义样式和类型转换器

### 6. 缓存
Spring Cache + Redis 实现，支持 `@Cacheable`、`@CachePut`、`@CacheEvict` 注解。

### 7. 消息队列
RabbitMQ 配置示例，包含 Topic Exchange、普通队列、延迟队列（TTL + 死信队列），手动ACK消费。

### 8. 定时任务
`@Scheduled` + `@Async` 异步定时任务，使用 Redisson 分布式锁保证集群下只执行一次。

### 9. 拦截器与过滤器
自定义 `HandlerInterceptor` 和 `OncePerRequestFilter`，包含用户信息注入（ThreadLocal）和跨域配置。

### 10. 国际化
自定义 `LocaleResolver`，通过请求参数 `?lang=zh-CN` 切换语言。

### 11. 多数据源
基于 `dynamic-datasource` 实现 MySQL + PostgreSQL 多数据源切换（`@DS` 注解）。

### 12. 规则引擎
Drools 规则引擎集成示例。

### 13. 线程池
自定义 `ThreadPoolExecutor` 和 Spring `ThreadPoolTaskExecutor` 配置，支持通过配置文件调整参数。

### 14. Session管理
Spring Session + Redis 实现分布式 Session。

## 运行环境

- JDK 17
- MySQL
- PostgreSQL
- Redis
- RabbitMQ

## 快速启动

### 1. 创建数据库

在 MySQL 中创建 `template_code` 数据库，包含 `user` 和 `export_task` 表。

### 2. 修改配置

根据本地环境修改 `application.properties` 中的连接信息：

```properties
# MySQL
spring.datasource.dynamic.datasource.mysql.url=jdbc:mysql://localhost:3306/template_code
spring.datasource.dynamic.datasource.mysql.username=root
spring.datasource.dynamic.datasource.mysql.password=123

# Redis
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.password=123

# RabbitMQ
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest
```

### 3. 启动项目

```bash
mvn spring-boot:run
```

项目启动后访问 `http://localhost:8081`

## API 接口

|   方法   |           路径           |        说明         |
|:------:|:----------------------:|:-----------------:|
| `GET`  |      `/users/me`       | 查询当前用户信息（需要admin角色） |
| `GET`  |     `/users/{id}`      |      查询指定用户       |
| `GET`  |        `/users`        |   查询用户列表（支持过滤）    |
| `POST` |        `/users`        |       新增用户        |
| `DELETE`|     `/users/{id}`      |       删除用户        |
| `PUT`  |     `/users/{id}`      |       更新用户        |
| `POST` |   `/test/validation`   |     测试实体类校验功能     |
| `GET`  |   `/test/validation`   |    测试单个参数校验功能     |
| `GET`  |      `/test/i18n`      |      测试国际化功能      |
| `POST` | `/users/import?engine=poi` |  使用POI库导入xlsx文件   |
| `POST` | `/users/import?engine=easyexcel` |  使用EasyExcel库导入   |
| `POST` |    `/users/export`     |    导出Excel(XLSX)文件到浏览器 |
| `POST` | `/users/export/local`  |   异步导出Excel(XLSX)文件到本地 |
| `GET`  |    `/export-tasks`     |     查询导出任务列表      |
| `POST` |    `/files/upload`     |      单个文件上传       |
| `POST` | `/files/batch-upload`  |      多个文件上传       |
| `GET`  |   `/files/{fileName}`  |       文件下载        |
| `POST` |      `/messages`       |   发送RabbitMQ消息    |
| `POST` |       `/orders`        |     订单规则引擎计算      |

## 配置说明

### 线程池配置

```properties
# 自定义线程池
thread-pool.core-pool-size=20
thread-pool.maximum-pool-size=50
thread-pool.keep-alive-time=10
thread-pool.unit=minutes

# 异步任务线程池
spring.task.execution.pool.core-size=20
spring.task.execution.pool.max-size=50
spring.task.execution.pool.keep-alive=60s
spring.task.execution.pool.queue-capacity=100
spring.task.execution.thread-name-prefix=task-

# 定时任务线程池
spring.task.scheduling.pool.size=5
spring.task.scheduling.thread-name-prefix=scheduling-
```

### Actuator 监控

```properties
management.endpoints.web.exposure.include=*
management.endpoint.shutdown.enabled=true
management.endpoint.health.show-details=always
```

启动后访问 `http://localhost:8081/actuator` 查看所有可用端点。

## 请求执行流程

```
HTTP 请求
  │
  ▼
Filter (CustomFilter)          ← 过滤器链顺序执行
  │
  ▼
Interceptor.preHandle          ← 拦截器前置处理(存入用户信息到ThreadLocal)
  │
  ▼
AOP @Before                    ← 切面前置通知
  │
  ▼
Controller                     ← 控制器处理请求
  │
  ▼
AOP @AfterReturning / @After   ← 切面后置通知
  │
  ▼
Interceptor.postHandle         ← 拦截器后置处理
  │
  ▼
视图渲染
  │
  ▼
Interceptor.afterCompletion    ← 拦截器完成处理(清除ThreadLocal)
  │
  ▼
Filter (CustomFilter)          ← 过滤器链逆序执行
  │
  ▼
HTTP 响应
```

## 许可证

本项目基于 [MIT License](LICENSE) 开源。
