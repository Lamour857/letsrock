** 按领域模型划分 **
org.wj.letsrock
├── application/        # 应用层，协调领域对象和基础设施
├── domain/             # 领域层，核心业务逻辑
├── infrastructure/     # 基础设施层，与外部系统交互
└── interfaces/         # 接口层，处理外部请求

** 包结构 **
org.wj.letsrock
├── application/             # 应用服务层
│   ├── article/             # 文章相关服务
│   ├── user/                # 用户相关服务
│   ├── comment/             # 评论相关服务
│   └── notification/        # 通知相关服务
│
├── domain/                  # 领域模型层
│   ├── article/
│   │   ├── entity/          # 领域实体
│   │   ├── repository/      # 仓储接口
│   │   ├── service/         # 领域服务
│   │   └── event/           # 领域事件
│   ├── user/
│   ├── comment/
│   └── common/              # 公共领域模型
│
├── infrastructure/          # 基础设施层
│   ├── config/              # 配置类
│   ├── security/            # 安全相关
│   ├── persistence/         # 持久化实现
│   │   ├── jpa/             # JPA实现
│   │   ├── mybatis/         # MyBatis实现
│   │   └── repository/      # 仓储实现
│   ├── cache/               # 缓存实现
│   ├── messaging/           # 消息队列
│   ├── search/              # 搜索引擎
│   ├── file/                # 文件存储
│   ├── client/              # 外部服务客户端
│   └── job/                 # 定时任务
│
├── interfaces/              # 接口层
│   ├── api/                 # API接口
│   │   ├── controller/      # 控制器
│   │   ├── dto/             # 数据传输对象
│   │   ├── assembler/       # DTO/实体转换器
│   │   └── advice/          # 全局异常处理
│   ├── mq/                  # 消息队列监听器
│   └── job/                 # 定时任务入口
│
└── common/                  # 通用模块
├── constants/           # 常量定义
├── enums/               # 枚举类型
├── exception/           # 自定义异常
├── utils/               # 工具类
│   ├── DateUtils.java
│   ├── StringUtils.java
│   └── JsonUtils.java
├── aspect/              # AOP切面
├── annotation/          # 自定义注解
└── model/               # 公共数据模型