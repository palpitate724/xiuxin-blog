# Blog Spring API 接口文档

## 基础信息

- **Base URL**: `/api/v1`
- **Content-Type**: `application/json`
- **响应格式**: 统一使用 `Result` 对象

---

## 文章模块

### 1. 添加文章

**接口地址**: `POST /api/v1/art`

**描述**: 添加新文章

**请求参数**:
```json
{
  "artname": "文章标题",
  "cont": "文章内容",
  "userid": 1,
  "catid": 1,
  "fenmianurl": "封面URL",
  "sum": "文章摘要",
  "tagids": [1, 2, 3]
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 2. 查询文章列表

**接口地址**: `GET /api/v1/art`

**描述**: 查询所有文章列表

**请求参数**: 无

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "artname": "文章标题",
      "userid": 1,
      "catid": 1,
      "fenmianurl": "封面URL",
      "sum": "文章摘要",
      "tagvolist": [
        {
          "id": 1,
          "tagname": "标签名"
        }
      ]
    }
  ]
}
```

---

### 3. 删除文章

**接口地址**: `DELETE /api/v1/art/{artid}`

**描述**: 根据文章ID删除文章

**路径参数**:
- `artid`: 文章ID (Long)

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 4. 上传文章封面文件

**接口地址**: `POST /api/v1/art/minio`

**描述**: 上传文章封面图片到Minio

**请求参数**: `MultipartFile` 文件对象

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "文件对象名"
}
```

---

### 5. 获取文章封面临时URL

**接口地址**: `GET /api/v1/art/minio/{objectname}`

**描述**: 获取文章封面的临时访问URL

**路径参数**:
- `objectname`: 文件对象名 (String)

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "临时URL"
}
```

---

## 分类模块

### 1. 添加分类

**接口地址**: `POST /api/v1/{id}/cat`

**描述**: 添加新分类

**路径参数**:
- `id`: 用户ID

**请求参数**:
```json
{
  "catname": "分类名称"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 2. 查询分类列表

**接口地址**: `GET /api/v1/{id}/cat`

**描述**: 查询所有分类列表

**路径参数**:
- `id`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "catname": "分类名称"
    }
  ]
}
```

---

## 标签模块

### 1. 添加标签

**接口地址**: `POST /api/v1/{id}/tag`

**描述**: 添加新标签

**路径参数**:
- `id`: 用户ID

**请求参数**:
```json
{
  "tagname": "标签名称"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

### 2. 查询标签列表

**接口地址**: `GET /api/v1/{id}/tag`

**描述**: 查询所有标签列表

**路径参数**:
- `id`: 用户ID

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "tagname": "标签名称"
    }
  ]
}
```

---

## 用户模块

### 1. 用户注册

**接口地址**: `POST /api/v1/user/signup`

**描述**: 新用户注册

**请求参数**:
```json
{
  "username": "用户名",
  "password": "密码"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "username": "用户名",
    "password": "加密后的密码",
    "objecturl": "头像URL"
  }
}
```

---

### 2. 用户登录

**接口地址**: `POST /api/v1/user/login`

**描述**: 用户登录验证

**请求参数**:
```json
{
  "username": "用户名",
  "password": "密码"
}
```

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "JWT令牌",
    "user": {
      "id": 1,
      "username": "用户名",
      "objecturl": "头像URL"
    }
  }
}
```

---

### 3. 上传用户头像

**接口地址**: `POST /api/v1/user/{id}/minio`

**描述**: 上传用户头像图片到Minio

**路径参数**:
- `id`: 用户ID

**请求参数**: `MultipartFile` 文件对象

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "文件对象名"
}
```

---

### 4. 获取用户头像临时URL

**接口地址**: `GET /api/v1/user/{id}/minio`

**描述**: 获取用户头像的临时访问URL

**路径参数**:
- `id`: 文件对象名 (String)

**响应示例**:
```json
{
  "code": 200,
  "message": "操作成功",
  "data": "临时URL"
}
```

---

## 统一响应格式说明

所有接口返回统一的 `Result` 对象：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

**字段说明**:
- `code`: 状态码 (200-成功, 其他-失败)
- `message`: 响应消息
- `data`: 响应数据，具体结构根据接口而定

---

## 状态码说明

| 状态码 | 说明 |
|--------|------|
| 200 | 操作成功 |
| 400 | 参数错误 |
| 401 | 未授权 |
| 403 | 禁止访问 |
| 404 | 资源不存在 |
| 500 | 服务器错误 |

---

## 注意事项

1. 所有POST请求的Content-Type必须为 `application/json`
2. 文件上传接口使用 `multipart/form-data` 格式
3. 需要认证的接口需要在请求头中携带JWT Token
4. 路径参数中的 `{id}` 通常指用户ID
