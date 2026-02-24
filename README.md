# Template Code

Spring Boot 模板代码项目，涵盖后端开发常用功能模块的示例实现。

## 技术栈

|  类别   |              技术              |
|:-----:|:----------------------------:|
|  框架   |      Spring Boot 2.7.13      |
|  ORM  |      MyBatis-Plus 3.5.3      |
|  数据库  |    MySQL、PostgreSQL（多数据源）    |
|  缓存   |      Redis、Spring Cache      |
| 分布式锁  |           Redisson           |
| 消息队列  |           RabbitMQ           |
| 规则引擎  |         Drools 7.74          |
| Excel | EasyExcel 3.3、Apache POI 5.2 |
| 模板引擎  |          Thymeleaf           |
|  工具   |        Hutool、Lombok         |

## 项目结构

```
com.ltx
├── annotation        # 自定义注解
│   ├── PreAuthorize       - 权限校验注解
│   └── SensitiveInfo      - 敏感信息脱敏注解
├── aop               # AOP切面
│   ├── UserAop            - 切面示例(前置/后置/环绕/异常通知)
│   └── UserService        - AOP测试服务
├── cache             # 缓存配置
│   └── CacheConfig        - Spring Cache + Redis 缓存配置
├── constant          # 常量
│   ├── Constant           - 通用常量
│   ├── DatasourceConstant - 数据源常量
│   └── RedisConstant      - Redis键常量
├── controller        # 控制器
│   ├── UserController               - 用户CRUD + 缓存 + 校验
│   ├── ImportAndExportController     - Excel导入导出
│   ├── UploadAndDownloadController   - 文件上传下载
│   └── MessageController            - RabbitMQ消息发送
├── drools            # 规则引擎
│   ├── config/DroolsConfig    - Drools配置
│   ├── controller/OrderController - 订单规则接口
│   ├── entity/                - 规则实体(Order, Customer)
│   └── service/RuleService    - 规则执行服务
├── easyexcel         # EasyExcel相关
│   ├── converter/         - 类型转换器(List, SexEnum)
│   ├── service/           - 导入导出服务
│   ├── stylestrategy/     - 单元格样式策略
│   └── writehandler/      - 单元格写入处理器
├── entity            # 实体类
│   ├── Result             - 通用响应对象
│   ├── po/                - 持久化对象(User, Temp)
│   ├── pojo/BasePojo      - 基础POJO(自动填充创建/更新时间)
│   ├── dto/PageDTO        - 分页结果DTO
│   ├── query/PageQuery    - 分页查询参数
│   └── request/           - 请求体(UserRequestBody, ExportRequestBody)
├── enums             # 枚举
│   ├── ErrorCodeEnum      - 错误码枚举
│   ├── ExportStatusEnum   - 导出状态枚举
│   └── SexEnum            - 性别枚举
├── exception         # 异常处理
│   ├── CustomException          - 自定义异常
│   └── GlobalExceptionHandler   - 全局异常处理器
├── filter            # 过滤器
│   ├── CustomFilter       - 自定义过滤器(OncePerRequestFilter)
│   └── FilterConfig       - 过滤器注册配置
├── interceptor       # 拦截器
│   ├── CustomInterceptor  - 自定义拦截器(用户信息存入ThreadLocal)
│   ├── WebMvcConfig       - Web MVC配置(拦截器注册 + 视图控制器 + 国际化)
│   └── CorsConfig         - 跨域配置
├── internationalization  # 国际化
│   ├── MyLocaleResolver                          - 自定义Locale解析器
│   ├── MessageSourceUtil                         - MessageSource工具类
│   └── ReloadableResourceBundleMessageSourceUtil - 可重载MessageSource工具类
├── jackson           # JSON配置
│   └── JacksonConfig      - Jackson序列化配置(忽略大小写 + 下划线命名)
├── juc               # 并发
│   ├── ThreadPoolConfig           - 线程池配置
│   ├── ThreadPoolConfigProperties - 线程池属性
│   └── ThreadUtil                 - 线程池工具类
├── listener          # 监听器
│   ├── ApplicationListener - 应用上下文刷新监听
│   └── UserListener        - EasyExcel数据读取监听器
├── mapper            # 数据访问层
│   ├── UserMapper         - 用户Mapper(MyBatis-Plus)
│   ├── TempMapper         - 临时表Mapper(多数据源示例)
│   └── ExportTaskMapper   - 导出任务Mapper
├── mq/rabbitmq       # 消息队列
│   ├── RabbitMQConfig     - RabbitMQ配置(交换机 + 队列 + 延迟队列)
│   ├── MessageSender      - 消息发送者
│   └── RabbitMQListener   - 消息监听者(手动ACK)
├── mybatisplus       # MyBatis-Plus配置
│   ├── AutoFillConfig     - 自动填充配置(创建/更新时间)
│   └── MybatisPlusConfig  - 分页插件配置
├── redis             # Redis配置
│   └── RedisConfig        - RedisTemplate序列化配置
├── redisson          # Redisson配置
│   └── RedissonConfig     - Redisson客户端配置
├── runner            # 启动执行器
│   └── StartupRunner      - 应用启动后执行(CommandLineRunner)
├── scheduled         # 定时任务
│   └── ScheduledTask      - 定时任务(Redisson分布式锁)
├── service           # 业务层
│   ├── ExportTaskService      - 导出任务接口
│   └── impl/ExportTaskServiceImpl - 导出任务实现
├── session           # Session配置
│   └── SessionConfig      - Spring Session + Redis配置
├── typehandler       # MyBatis类型处理器
│   └── ListTypeHandler    - List<String> <-> VARCHAR 转换
├── util              # 工具类
│   ├── RedisUtil          - Redis操作工具类
│   ├── RedissonUtil       - Redisson分布式锁工具类
│   └── UserContext        - 用户上下文(ThreadLocal)
└── valid             # 参数校验
    ├── ListValue                    - 自定义校验注解
    ├── ListValueConstraintValidator - 自定义校验器
    ├── InsertGroup                  - 新增校验分组
    └── UpdateGroup                  - 更新校验分组
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

- JDK 8
- MySQL
- PostgreSQL
- Redis
- RabbitMQ

## 快速启动

### 1. 创建数据库

在 MySQL 中创建 `demo` 数据库，包含 `user` 和 `export_task` 表。

### 2. 修改配置

根据本地环境修改 `application.properties` 中的连接信息：

```properties
# MySQL
spring.datasource.dynamic.datasource.mysql.url=jdbc:mysql://localhost:3306/demo
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
| `GET`  |        `/users`        | 查询用户信息（需要admin角色） |
| `GET`  |     `/users/{id}`      |      查询指定用户       |
| `POST` |     `/users/list`      |      查询用户列表       |
| `POST` |        `/users`        |       新增用户        |
| `POST` |     `/users/{id}`      |       删除用户        |
| `PUT`  |     `/users/{id}`      |       更新用户        |
| `POST` |   `/test-validation`   |     测试实体类校验功能     |
| `GET`  |   `/test-validation`   |    测试单个参数校验功能     |
| `GET`  |      `/test-i18n`      |      测试国际化功能      |
| `POST` |    `/import-by-poi`    |  使用POI库导入xlsx文件   |
| `POST` | `/import-by-easyexcel` |  使用EasyExcel库导入   |
| `POST` |       `/export`        |    导出CSV文件到浏览器    |
| `POST` |   `/export-to-local`   |   异步导出CSV文件到本地    |
| `GET`  |  `/export-task-list`   |     查询导出任务列表      |
| `POST` |    `/single-upload`    |      单个文件上传       |
| `POST` |   `/multiple-upload`   |      多个文件上传       |
| `GET`  | `/download/{fileName}` |       文件下载        |
| `GET`  |        `/send`         |   发送RabbitMQ消息    |
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
