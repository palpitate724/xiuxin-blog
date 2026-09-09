# Xiuxin Blog - 项目完整文档

## 项目概述

Xiuxin Blog 是一个基于 Spring Boot + Vue 3 的博客系统，采用前后端分离架构。项目包含用户管理、文章发布、分类标签管理、文件上传等功能。

**项目地址**: https://github.com/palpitate724/xiuxin-blog

## 技术栈

### 后端 (blog_spring)
- **框架**: Spring Boot 4.1.1
- **Java版本**: Java 17
- **构建工具**: Maven
- **ORM框架**: MyBatis Plus 3.5.16
- **数据库**: MySQL 8.0.28
- **认证**: JWT (jjwt 0.12.6)
- **密码加密**: BCrypt 0.4
- **对象存储**: Minio 8.6.0
- **HTTP客户端**: OkHttp 4.9.1
- **工具库**: Lombok

### 前端 (blog_vue)
- **框架**: Vue 3.5.41
- **语言**: TypeScript 6.0.2
- **构建工具**: Vite 8.2.2
- **编译器**: vue-tsc 3.3.11

## 项目结构

```
xiuxin-blog/
├── blog_spring/              # Spring Boot 后端
│   ├── blog_common/          # 公共模块
│   ├── blog_domain/          # 领域模块
│   ├── blog_web/             # Web模块
│   └── pom.xml              # 父POM
└── blog_vue/                # Vue 3 前端
    ├── src/
    │   ├── App.vue
    │   ├── main.ts
    │   └── components/
    ├── package.json
    └── vite.config.ts
```

## 后端模块详解

### 1. blog_common (公共模块)

**职责**: 提供公共工具类、配置、枚举和统一返回结果

**主要组件**:

#### 配置类
- `JwtProperties`: JWT配置属性
- `MinioConfig`: Minio客户端配置
- `MinioProperties`: Minio连接属性

#### 枚举类
- `UserRole`: 用户角色枚举
  - USER(0): 普通用户
  - AUTHOR(1): 作者
  - ADMIN(2): 管理员
  - SUPER_ADMIN(3): 超级管理员
- `ArticleStatus`: 文章状态枚举
  - DRAFT(0): 草稿
  - PUBLISHED(1): 已发布
  - OFFLINE(2): 已下架
  - DELETED(3): 已删除
- `CommentStatus`: 评论状态枚举
- `StorageType`: 存储类型枚举
- `ResultCode`: 结果码枚举

#### 工具类
- `JwtUtils`: JWT工具类
  - `getToken(Long id, String username)`: 生成JWT令牌
  - `isTokenValid(String token)`: 验证令牌有效性
- `BcryptUtils`: BCrypt密码加密工具
- `MinioUtils`: Minio对象存储工具

#### 统一返回结果
- `Result`: 统一API返回结果类
  - `code`: 状态码
  - `message`: 提示信息
  - `data`: 返回数据

**依赖**:
```xml
- jjwt-api/impl/jackson: 0.12.6
- jbcrypt: 0.4
- minio: 8.6.0
- okhttp: 4.9.1
- lombok
```

### 2. blog_domain (领域模块)

**职责**: 数据访问层、业务逻辑层、实体类、DTO

**主要组件**:

#### 实体类 (Entity)
- `UserEntity`: 用户表
  - id: 用户ID
  - username: 用户名
  - password: 密码
  - touxiangurl: 头像地址
  - email: 邮箱
  - cjiantime: 创建时间
  - deleted: 删除标志

- `ArtEntity`: 文章表
  - id: 文章ID
  - artname: 文章名称
  - userid: 作者ID
  - catid: 分类ID
  - frnmianurl: 封面图片URL
  - sum: 文章摘要
  - cont: 文章内容
  - cjtime: 创建时间
  - deleted: 删除标志

- `CatEntity`: 分类表
  - id: 分类ID
  - catname: 分类名称
  - cjiantime: 创建时间
  - deleted: 删除标志

- `TagEntity`: 标签表
  - id: 标签ID
  - tagname: 标签名称
  - cjiantime: 创建时间
  - deleted: 删除标志

- `ArtTagEntity`: 文章标签关联表
- `RoleEntity`: 角色表
- `UserRoleEntity`: 用户角色关联表

#### 数据传输对象 (DTO)
- `LoginUserDto`: 用户登录DTO
- `SignupUserDto`: 用户注册DTO
- `AddCatDto`: 添加分类DTO
- `AddTagDto`: 添加标签DTO

#### Mapper接口
- `UserMapper`: 用户数据访问
- `CatMapper`: 分类数据访问
- `TagMapper`: 标签数据访问
- `UserRoleMapper`: 用户角色数据访问

#### Service接口
- `LoginUserService`: 用户登录服务
- `SignupUserService`: 用户注册服务
- `UserMinioService`: 用户头像上传服务
- `AddCatService`: 添加分类服务
- `SelectCatListService`: 查询分类列表服务
- `AddTagService`: 添加标签服务
- `SelectTagListService`: 查询标签列表服务
- `ArtMinioService`: 文章封面上传服务

#### 配置
- `MybatisplusConfig`: MyBatis Plus配置

**依赖**:
```xml
- blog_common
- mybatis-plus-spring-boot4-starter: 3.5.16
- mysql-connector-java: 8.0.28
- lombok
```

### 3. blog_web (Web模块)

**职责**: 控制器层、API接口、应用启动

**主要组件**:

#### 控制器 (Controller)
- `LoginUserCon`: 用户登录控制器
  - POST `/api/v1/user/login`: 用户登录
  
- `SignupUserCon`: 用户注册控制器
  - POST `/api/v1/user/signup`: 用户注册
  
- `UserMinioCon`: 用户头像上传控制器
  - POST `/api/v1/user/upload`: 上传用户头像
  
- `AddCatCon`: 添加分类控制器
  - POST `/api/v1/cat/add`: 添加分类
  
- `SelectCatListCon`: 查询分类列表控制器
  - GET `/api/v1/cat/list`: 查询分类列表
  
- `AddTagCon`: 添加标签控制器
  - POST `/api/v1/tag/add`: 添加标签
  
- `SelectTagListCon`: 查询标签列表控制器
  - GET `/api/v1/tag/list`: 查询标签列表
  
- `ArtMinioCon`: 文章封面上传控制器
  - POST `/api/v1/art/upload`: 上传文章封面

#### 应用启动类
- `BlogWebApplication`: Spring Boot应用主类
  - 扫描包: `com.example`
  - 启用配置属性: `JwtProperties`

**依赖**:
```xml
- spring-boot-starter-webmvc
- blog_domain
```

## 前端模块详解

### blog_vue (Vue 3 前端)

**职责**: 用户界面、前端交互

**主要组件**:
- `App.vue`: 根组件
- `main.ts`: 应用入口
- `components/`: 组件目录

**配置文件**:
- `package.json`: 项目依赖配置
- `vite.config.ts`: Vite构建配置
- `tsconfig.json`: TypeScript配置

**依赖**:
```json
{
  "vue": "^3.5.41",
  "typescript": "~6.0.2",
  "vite": "^8.2.2",
  "@vitejs/plugin-vue": "^6.0.8",
  "vue-tsc": "^3.3.11"
}
```

## 数据库设计

### 用户表 (user)
```sql
CREATE TABLE user (
    id BIGINT PRIMARY KEY,
    username VARCHAR(255),
    password VARCHAR(255),
    touxiangurl VARCHAR(255),
    email VARCHAR(255),
    cjiantime VARCHAR(255),
    deleted INT DEFAULT 0
);
```

### 文章表 (art)
```sql
CREATE TABLE art (
    id BIGINT PRIMARY KEY,
    artname VARCHAR(255),
    userid BIGINT,
    catid BIGINT,
    frnmianurl VARCHAR(255),
    sum TEXT,
    cont TEXT,
    cjtime VARCHAR(255),
    deleted INT DEFAULT 0
);
```

### 分类表 (cat)
```sql
CREATE TABLE cat (
    id BIGINT PRIMARY KEY,
    catname VARCHAR(255),
    cjiantime VARCHAR(255),
    deleted INT DEFAULT 0
);
```

### 标签表 (tag)
```sql
CREATE TABLE tag (
    id BIGINT PRIMARY KEY,
    tagname VARCHAR(255),
    cjiantime VARCHAR(255),
    deleted INT DEFAULT 0
);
```

### 文章标签关联表 (art_tag)
```sql
CREATE TABLE art_tag (
    art_id BIGINT,
    tag_id BIGINT,
    PRIMARY KEY (art_id, tag_id)
);
```

### 角色表 (role)
```sql
CREATE TABLE role (
    id BIGINT PRIMARY KEY,
    role_name VARCHAR(255)
);
```

### 用户角色关联表 (user_role)
```sql
CREATE TABLE user_role (
    user_id BIGINT,
    role_id BIGINT,
    PRIMARY KEY (user_id, role_id)
);
```

## API接口文档

### 用户模块

#### 用户登录
- **接口**: `POST /api/v1/user/login`
- **请求体**:
```json
{
  "username": "string",
  "password": "string"
}
```
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": {
    "token": "jwt_token_string"
  }
}
```

#### 用户注册
- **接口**: `POST /api/v1/user/signup`
- **请求体**:
```json
{
  "username": "string",
  "password": "string",
  "email": "string"
}
```

#### 上传用户头像
- **接口**: `POST /api/v1/user/upload`
- **请求类型**: multipart/form-data
- **参数**: file (文件)

### 分类模块

#### 添加分类
- **接口**: `POST /api/v1/cat/add`
- **请求体**:
```json
{
  "catname": "string"
}
```

#### 查询分类列表
- **接口**: `GET /api/v1/cat/list`
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "catname": "技术",
      "cjiantime": "2023-09-04"
    }
  ]
}
```

### 标签模块

#### 添加标签
- **接口**: `POST /api/v1/tag/add`
- **请求体**:
```json
{
  "tagname": "string"
}
```

#### 查询标签列表
- **接口**: `GET /api/v1/tag/list`
- **响应**:
```json
{
  "code": 200,
  "message": "success",
  "data": [
    {
      "id": 1,
      "tagname": "Java",
      "cjiantime": "2023-09-04"
    }
  ]
}
```

### 文章模块

#### 上传文章封面
- **接口**: `POST /api/v1/art/upload`
- **请求类型**: multipart/form-data
- **参数**: file (文件)

## 配置说明

### JWT配置
需要在 `application.yml` 中配置:
```yaml
jwt:
  secret-key: your-secret-key
```

### Minio配置
需要在 `application.yml` 中配置:
```yaml
minio:
  endpoint: http://localhost:9000
  access-key: your-access-key
  secret-key: your-secret-key
```

### 数据库配置
需要在 `application.yml` 中配置:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/blog
    username: root
    password: your-password
    driver-class-name: com.mysql.cj.jdbc.Driver
```

## 构建和运行

### 后端构建
```bash
cd blog_spring
mvn clean install
```

### 后端运行
```bash
cd blog_web
mvn spring-boot:run
```

### 前端安装依赖
```bash
cd blog_vue
npm install
```

### 前端开发
```bash
npm run dev
```

### 前端构建
```bash
npm run build
```

## 核心功能

1. **用户认证**: JWT令牌认证，支持登录注册
2. **角色权限**: 多角色权限管理（用户、作者、管理员、超级管理员）
3. **文章管理**: 文章发布、编辑、删除、状态管理
4. **分类标签**: 文章分类和标签管理
5. **文件上传**: 基于Minio的对象存储，支持头像和封面上传
6. **密码安全**: BCrypt密码加密存储

## 项目特点

1. **模块化设计**: 清晰的三层架构（common、domain、web）
2. **前后端分离**: Spring Boot + Vue 3 独立部署
3. **统一返回**: 标准化的API响应格式
4. **安全认证**: JWT + BCrypt双重安全机制
5. **对象存储**: Minio分布式文件存储
6. **ORM优化**: MyBatis Plus简化数据访问

## 开发规范

1. **命名规范**: 驼峰命名法，类名大驼峰，方法/变量小驼峰
2. **注释规范**: 类和方法使用JavaDoc注释
3. **包结构**: 按功能模块划分包结构
4. **异常处理**: 统一的异常处理机制
5. **日志规范**: 关键操作添加日志记录

## 扩展建议

1. **文章模块**: 完善文章的增删改查功能
2. **评论模块**: 添加文章评论功能
3. **搜索功能**: 实现文章全文搜索
4. **缓存优化**: 引入Redis缓存热点数据
5. **监控告警**: 添加应用监控和日志分析
6. **CI/CD**: 配置自动化部署流程

## 作者信息

- **作者**: palpitate
- **项目名称**: xiuxin-blog
- **版本**: 0.0.1-SNAPSHOT
- **许可证**: 未指定

## 总结

Xiuxin Blog 是一个结构清晰、模块分明的博客系统，采用现代化的技术栈，具有良好的扩展性。项目目前处于开发阶段，核心功能框架已搭建完成，适合作为学习Spring Boot和Vue 3的参考项目。
