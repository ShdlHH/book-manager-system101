# 接口测试运行计划

## 1. 测试概述

本计划用于逐步运行所有 API 单元测试，验证各个接口是否正确运行。

## 2. 测试文件清单

| 序号 | 测试文件 | 测试用例数 | 覆盖功能 |
|:---|:---|:---|:---|
| 1 | UserControllerTest.java | 7 | 用户注册、登录、信息查询、登出 |
| 2 | BookControllerTest.java | 7 | 书籍 CRUD、状态更新 |
| 3 | CategoryControllerTest.java | 5 | 分类 CRUD |
| 4 | BorrowControllerTest.java | 6 | 借书、还书、续借、借阅记录 |
| 5 | TodoControllerTest.java | 6 | 待办 CRUD、状态切换 |
| 6 | StatControllerTest.java | 4 | 数据看板、借阅趋势、分类占比、热门图书 |
| 7 | FileControllerTest.java | 3 | 文件上传 |

## 3. 测试执行顺序

| 步骤 | 测试文件 | 预计时间 |
|:---|:---|:---|
| 1 | UserControllerTest | 10秒 |
| 2 | CategoryControllerTest | 10秒 |
| 3 | BookControllerTest | 10秒 |
| 4 | TodoControllerTest | 10秒 |
| 5 | BorrowControllerTest | 10秒 |
| 6 | StatControllerTest | 10秒 |
| 7 | FileControllerTest | 10秒 |
| 8 | 汇总测试结果 | - |

## 4. 运行命令

```bash
# 设置 Java 17 环境
$env:JAVA_HOME="C:\Program Files\Java\jdk-17"
$env:PATH="$env:JAVA_HOME\bin;$env:PATH"

# 运行单个测试
mvn test -Dtest=UserControllerTest

# 运行多个测试
mvn test -Dtest=UserControllerTest,BookControllerTest

# 运行所有测试
mvn test
```

## 5. 预期结果

所有测试应通过，无失败、无错误。

## 6. 风险与注意事项

- 确保 MySQL 数据库服务已启动
- 数据库连接配置正确（application.yaml）
- 需要 Java 17 环境

---

**计划状态**: 待执行
