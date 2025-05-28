* 按领域模型划分 


`├── application/`        # 应用层，协调领域对象和基础设施<br>
`├── domain/`             # 领域层，核心业务逻辑<br>
`├── infrastructure/`     # 基础设施层，与外部系统交互<br>
`└── interfaces/`         # 接口层，处理外部请求<br>

* 包结构 

`org.wj.letsrock`

`├── application/`             # 应用服务层<br>
`│   ├── article/  `           # 文章相关服务<br>
`│   ├── user/    `            # 用户相关服务<br>
`│   ├── comment/  `           # 评论相关服务<br>
`│   └── notification/ `       # 通知相关服务<br>
`│`
`├── domain/ `                 # 领域模型层<br>
`│   ├── article/<br>`
`│   │   ├── entity/`         # 领域实体<br>
`│   │   ├── repository/`      # 仓储接口<br>
`│   │   ├── service/`         # 领域服务<br>
`│   │   └── event/`           # 领域事件<br>
`│   ├── user/` <br>
`│   ├── comment/`<br>
`│   └── common/`              # 公共领域模型<br>
`│`
`├── infrastructure/`          # 基础设施层<br>
`│   ├── config/`             # 配置类<br>
`│   ├── security/`            # 安全相关<br>
`│   ├── persistence/`         # 持久化实现<br>
`│   │   ├── jpa/`             # JPA实现<br>
`│   │   ├── mybatis/`         # MyBatis实现<br>
`│   │   └── repository/`      # 仓储实现<br>
`│   ├── cache/`               # 缓存实现<br>
`│   ├── messaging/`           # 消息队列<br>
`│   ├── search/`              # 搜索引擎<br>
`│   ├── file/`                # 文件存储<br>
`│   ├── client/`              # 外部服务客户端<br>
`│   └── job/`                 # 定时任务<br>
`│`
`├── interfaces/`              # 接口层<br>
`│   ├── api/`                # API接口<br>
`│   │   ├── controller/`      # 控制器<br>
`│   │   ├── dto/`             # 数据传输对象<br>
`│   │   ├── assembler/`       # DTO/实体转换器<br>
`│   │   └── advice/`          # 全局异常处理<br>
`│   ├── mq/`                  # 消息队列监听器<br>
`│   └── job/`                 # 定时任务入口<br>
`│`
`└── common/`                  # 通用模块<br>
`├── constants/`           # 常量定义<br>
`├── enums/`               # 枚举类型<br>
`├── exception/`           # 自定义异常<br>
`├── utils/`               # 工具类<br>
`│   ├── DateUtils.java`<br>
`│   ├── StringUtils.java`<br>
`│   └── JsonUtils.java`<br>
`├── aspect/`              # AOP切面<br>
`├── annotation/`          # 自定义注解<br>
`└── model/`               # 公共数据模型<br>

* 多级缓存 缓存分类和标签数据