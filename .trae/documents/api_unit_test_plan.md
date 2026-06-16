# API单元测试计划

## 1. 项目分析

### 1.1 项目结构

项目为 Spring Boot 3.2.5 + MyBatis-Plus 3.5.7 后端项目，包含以下控制器：

| 控制器                  | 路径              | 功能描述             |
| :------------------- | :-------------- | :--------------- |
| `BookController`     | `/api/book`     | 书籍管理（CRUD、状态更新）  |
| `BorrowController`   | `/api/borrow`   | 借阅管理（借书、还书、续借）   |
| `CategoryController` | `/api/category` | 分类管理（CRUD）       |
| `FileController`     | `/api/file`     | 文件上传             |
| `StatController`     | `/api/stat`     | 数据统计（趋势、占比、热门图书） |
| `TodoController`     | `/api/todo`     | 待办管理（CRUD、状态切换）  |
| `UserController`     | `/api/user`     | 用户管理（注册、登录、CRUD） |

### 1.2 技术栈

* Java 17

* Spring Boot 3.2.5

* MyBatis-Plus 3.5.7

* JUnit 5

* MockMvc（集成测试）

***

## 2. 测试策略

### 2.1 测试类型

采用 **MockMvc 集成测试**，模拟 HTTP 请求验证接口行为。

### 2.2 测试覆盖范围

| 测试场景 | 覆盖内容                  |
| :--- | :-------------------- |
| 成功场景 | 正常请求返回 200 状态码及正确响应结构 |
| 参数校验 | 缺失必填参数、参数格式错误的处理      |
| 业务异常 | 业务规则校验失败的处理           |
| 权限校验 | 未登录、无权限访问的处理          |

### 2.3 响应结构验证

所有接口响应必须符合全局统一格式：

```json
{
  "code": 200,
  "msg": "提示信息",
  "data": {}
}
```

***

## 3. 测试文件规划

### 3.1 文件结构

```
src/test/java/com/todo/todomanagementsystem/
├── controller/
│   ├── BookControllerTest.java      # 书籍管理接口测试
│   ├── BorrowControllerTest.java    # 借阅管理接口测试
│   ├── CategoryControllerTest.java  # 分类管理接口测试
│   ├── FileControllerTest.java      # 文件上传接口测试
│   ├── StatControllerTest.java      # 统计接口测试
│   ├── TodoControllerTest.java      # 待办管理接口测试
│   └── UserControllerTest.java      # 用户管理接口测试
└── TodoManagementSystemApplicationTests.java  # 现有测试
```

### 3.2 测试用例规划

#### 3.2.1 BookController 测试用例

| 序号 | 测试方法                        | 接口路径                    | HTTP方法 | 测试描述      |
| :- | :-------------------------- | :---------------------- | :----- | :-------- |
| 1  | `testBookPage`              | `/api/book/page`        | GET    | 分页查询书籍列表  |
| 2  | `testBookPageWithParams`    | `/api/book/page`        | GET    | 按条件筛选分页查询 |
| 3  | `testGetBookDetail`         | `/api/book/{id}`        | GET    | 获取书籍详情    |
| 4  | `testGetBookDetailNotFound` | `/api/book/{id}`        | GET    | 获取不存在的书籍  |
| 5  | `testAddBook`               | `/api/book`             | POST   | 新增书籍（管理员） |
| 6  | `testAddBookInvalidParams`  | `/api/book`             | POST   | 新增书籍参数校验  |
| 7  | `testUpdateBook`            | `/api/book/{id}`        | PUT    | 更新书籍信息    |
| 8  | `testDeleteBook`            | `/api/book/{id}`        | DELETE | 删除书籍      |
| 9  | `testUpdateBookStatus`      | `/api/book/{id}/status` | PUT    | 更新书籍状态    |

#### 3.2.2 UserController 测试用例

| 序号 | 测试方法                         | 接口路径                 | HTTP方法 | 测试描述        |
| :- | :--------------------------- | :------------------- | :----- | :---------- |
| 1  | `testRegister`               | `/api/user/register` | POST   | 用户注册        |
| 2  | `testRegisterDuplicate`      | `/api/user/register` | POST   | 注册重复用户名     |
| 3  | `testLogin`                  | `/api/user/login`    | POST   | 用户登录        |
| 4  | `testLoginInvalid`           | `/api/user/login`    | POST   | 登录参数错误      |
| 5  | `testGetUserInfo`            | `/api/user/info`     | GET    | 获取用户信息（需登录） |
| 6  | `testGetUserInfoWithoutAuth` | `/api/user/info`     | GET    | 未登录获取用户信息   |
| 7  | `testUserPage`               | `/api/user/page`     | GET    | 分页查询用户（管理员） |
| 8  | `testAddUser`                | `/api/user`          | POST   | 管理员新增用户     |
| 9  | `testUpdateUserByAdmin`      | `/api/user/{id}`     | PUT    | 管理员更新用户     |
| 10 | `testDeleteUser`             | `/api/user/{id}`     | DELETE | 管理员删除用户     |

#### 3.2.3 CategoryController 测试用例

| 序号 | 测试方法                       | 接口路径                 | HTTP方法 | 测试描述    |
| :- | :------------------------- | :------------------- | :----- | :------ |
| 1  | `testGetCategoryList`      | `/api/category/list` | GET    | 获取所有分类  |
| 2  | `testGetCategoryPage`      | `/api/category/page` | GET    | 分页查询分类  |
| 3  | `testAddCategory`          | `/api/category`      | POST   | 新增分类    |
| 4  | `testAddCategoryDuplicate` | `/api/category`      | POST   | 新增重复分类名 |
| 5  | `testUpdateCategory`       | `/api/category/{id}` | PUT    | 更新分类    |
| 6  | `testDeleteCategory`       | `/api/category/{id}` | DELETE | 删除分类    |

#### 3.2.4 BorrowController 测试用例

| 序号 | 测试方法                         | 接口路径                      | HTTP方法 | 测试描述          |
| :- | :--------------------------- | :------------------------ | :----- | :------------ |
| 1  | `testBorrowBook`             | `/api/borrow`             | POST   | 用户借书          |
| 2  | `testBorrowBookNotAvailable` | `/api/borrow`             | POST   | 借阅不可用书籍       |
| 3  | `testReturnBook`             | `/api/borrow/{id}/return` | PUT    | 归还图书          |
| 4  | `testRenewBook`              | `/api/borrow/{id}/renew`  | PUT    | 续借图书          |
| 5  | `testGetMyBorrowList`        | `/api/borrow/my-list`     | GET    | 获取我的借阅记录      |
| 6  | `testGetBorrowPage`          | `/api/borrow/page`        | GET    | 获取所有借阅记录（管理员） |
| 7  | `testGetOverdueList`         | `/api/borrow/overdue`     | GET    | 获取逾期列表（管理员）   |

#### 3.2.5 TodoController 测试用例

| 序号 | 测试方法                  | 接口路径                           | HTTP方法 | 测试描述     |
| :- | :-------------------- | :----------------------------- | :----- | :------- |
| 1  | `testGetTodoList`     | `/api/todo/list`               | GET    | 分页查询待办列表 |
| 2  | `testAddTodo`         | `/api/todo/add`                | POST   | 新增待办     |
| 3  | `testUpdateTodo`      | `/api/todo/update`             | PUT    | 修改待办     |
| 4  | `testDeleteTodo`      | `/api/todo/delete/{id}`        | DELETE | 删除待办     |
| 5  | `testBatchDeleteTodo` | `/api/todo/delete-batch`       | DELETE | 批量删除待办   |
| 6  | `testChangeStatus`    | `/api/todo/change-status/{id}` | PUT    | 切换待办状态   |

#### 3.2.6 StatController 测试用例

| 序号 | 测试方法                   | 接口路径                       | HTTP方法 | 测试描述   |
| :- | :--------------------- | :------------------------- | :----- | :----- |
| 1  | `testGetBorrowTrend`   | `/api/stat/borrow-trend`   | GET    | 获取借阅趋势 |
| 2  | `testGetCategoryRatio` | `/api/stat/category-ratio` | GET    | 获取分类占比 |
| 3  | `testGetHotBooks`      | `/api/stat/hot-books`      | GET    | 获取热门图书 |
| 4  | `testGetDashboard`     | `/api/stat/dashboard`      | GET    | 获取数据看板 |

#### 3.2.7 FileController 测试用例

| 序号 | 测试方法                    | 接口路径               | HTTP方法 | 测试描述       |
| :- | :---------------------- | :----------------- | :----- | :--------- |
| 1  | `testUploadFile`        | `/api/file/upload` | POST   | 上传文件       |
| 2  | `testUploadEmptyFile`   | `/api/file/upload` | POST   | 上传空文件      |
| 3  | `testUploadInvalidType` | `/api/file/upload` | POST   | 上传不支持的文件类型 |

***

## 4. 实施步骤

### 4.1 步骤概述

| 步骤 | 任务                        | 预计时间 |
| :- | :------------------------ | :--- |
| 1  | 创建测试基类（配置 MockMvc、JWT 模拟） | 30分钟 |
| 2  | 编写 BookControllerTest     | 45分钟 |
| 3  | 编写 UserControllerTest     | 45分钟 |
| 4  | 编写 CategoryControllerTest | 30分钟 |
| 5  | 编写 BorrowControllerTest   | 45分钟 |
| 6  | 编写 TodoControllerTest     | 30分钟 |
| 7  | 编写 StatControllerTest     | 30分钟 |
| 8  | 编写 FileControllerTest     | 30分钟 |
| 9  | 运行所有测试并验证                 | 30分钟 |

### 4.2 测试基类设计

创建 `BaseControllerTest` 类，提供：

* MockMvc 配置

* JWT Token 生成与注入

* 统一的响应断言方法

***

## 5. 依赖与环境

### 5.1 测试依赖（pom.xml）

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>
```

### 5.2 测试配置

使用内存数据库或测试专用数据库，避免影响生产数据。

***

## 6. 风险与注意事项

| 风险项   | 描述          | 应对措施                       |
| :---- | :---------- | :------------------------- |
| 数据库依赖 | 测试需要真实数据库连接 | 使用 H2 内存数据库或测试容器           |
| 权限校验  | 需要模拟登录状态    | 在基类中统一处理 JWT Token         |
| 数据隔离  | 测试数据相互影响    | 每个测试方法使用 @Transactional 回滚 |
| 文件上传  | 文件写入磁盘      | 配置临时目录，测试后清理               |

***

## 7. 验收标准

1. 所有测试用例执行通过（成功率 100%）
2. 代码覆盖率达到 80% 以上
3. 测试报告清晰，包含每个接口的测试结果
4. 测试代码符合项目编码规范

***

## 8. 输出交付物

| 交付物                         | 路径                              | 说明       |
| :-------------------------- | :------------------------------ | :------- |
| BookControllerTest.java     | `src/test/java/.../controller/` | 书籍管理接口测试 |
| BorrowControllerTest.java   | `src/test/java/.../controller/` | 借阅管理接口测试 |
| CategoryControllerTest.java | `src/test/java/.../controller/` | 分类管理接口测试 |
| FileControllerTest.java     | `src/test/java/.../controller/` | 文件上传接口测试 |
| StatControllerTest.java     | `src/test/java/.../controller/` | 统计接口测试   |
| TodoControllerTest.java     | `src/test/java/.../controller/` | 待办管理接口测试 |
| UserControllerTest.java     | `src/test/java/.../controller/` | 用户管理接口测试 |
| BaseControllerTest.java     | `src/test/java/.../controller/` | 测试基类     |

