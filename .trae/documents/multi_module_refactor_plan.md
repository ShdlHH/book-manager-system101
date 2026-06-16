# Maven多模块重构计划

## 一、当前项目分析

### 1.1 项目现状

* **项目类型**: 单体Spring Boot项目

* **JDK版本**: 17

* **Spring Boot版本**: 3.2.5

* **核心依赖**: MyBatis-Plus 3.5.7、JWT 0.11.5、Spring Security Crypto、SpringDoc OpenAPI

### 1.2 当前目录结构

```
Todo Management System/
├── src/main/java/com/todo/todomanagementsystem/
│   ├── config/           # CorsConfig, JwtConfig, MybatisPlusConfig, OpenApiConfig, WebConfig
│   ├── controller/       # 7个Controller
│   ├── dto/              # 10个DTO类
│   ├── entity/           # 6个Entity类
│   ├── exception/        # BusinessException, GlobalExceptionHandler
│   ├── mapper/           # 6个Mapper接口
│   ├── properties/       # JwtProperties
│   ├── service/          # 8个Service接口 + 8个Impl实现
│   ├── util/             # JwtUtil, Result
│   └── vo/               # 10个VO类
├── src/main/resources/
│   ├── com/todo/todomanagementsystem/mapper/  # BorrowRecordMapper.xml, SysUserMapper.xml
│   ├── mapper/           # BookMapper.xml, CategoryMapper.xml, NoticeMapper.xml, TodoMapper.xml
│   └── application.yaml
└── pom.xml
```

### 1.3 关键依赖关系分析

```
JwtUtil → JwtProperties (util依赖properties)
GlobalExceptionHandler → Result, BusinessException (exception依赖util)
Controller → Service → Mapper → Entity
Service → Result (业务层需要返回Result)
```

***

## 二、重构目标

1. **统一版本管理**: 父pom集中管理所有依赖版本
2. **模块化拆分**: 按职责分层拆分为独立模块
3. **依赖解耦**: 明确模块间依赖关系，避免循环依赖
4. **构建优化**: 支持独立模块编译、测试、打包
5. **遵循规范**: 严格遵守全局强制固定规则，不修改包名、不新增自定义逻辑

***

## 三、模块设计方案

### 3.1 模块划分（保持原有包名结构）

| 模块名称           | 功能定位                               | 依赖模块           |
| -------------- | ---------------------------------- | -------------- |
| `todo-common`  | 公共工具类、常量、异常、Result封装、JwtProperties | 无（最底层）         |
| `todo-entity`  | 实体类、DTO、VO定义                       | `todo-common`  |
| `todo-mapper`  | MyBatis Mapper接口及XML               | `todo-entity`  |
| `todo-service` | 业务逻辑层（Service接口及实现）                | `todo-mapper`  |
| `todo-web`     | Controller层、启动类、配置类、全局异常处理         | `todo-service` |

### 3.2 依赖传递规则

```
todo-web → todo-service → todo-mapper → todo-entity → todo-common
```

**Scope规范**:

* `compile`: 默认范围，编译、测试、运行时都有效

* `provided`: 编译和测试时有效，运行时由容器提供（如servlet-api）

* `runtime`: 运行时需要，编译时不需要（如JDBC驱动）

* `test`: 仅测试时需要（如JUnit）

### 3.3 完整目录树

```
Todo Management System/
├── pom.xml                                                # 父pom（聚合+继承）
├── todo-common/
│   ├── pom.xml
│   └── src/main/java/com/todo/todomanagementsystem/
│       ├── exception/BusinessException.java               # 业务异常类
│       ├── properties/JwtProperties.java                  # JWT属性配置
│       └── util/                                          # 工具类
│           ├── JwtUtil.java
│           └── Result.java
├── todo-entity/
│   ├── pom.xml
│   └── src/main/java/com/todo/todomanagementsystem/
│       ├── dto/                                           # 数据传输对象
│       │   ├── BookAddDTO.java
│       │   ├── BookPageDTO.java
│       │   ├── BookStatusDTO.java
│       │   ├── BookUpdateDTO.java
│       │   ├── BorrowAddDTO.java
│       │   ├── BorrowPageDTO.java
│       │   ├── CategoryAddDTO.java
│       │   ├── CategoryPageDTO.java
│       │   ├── CategoryUpdateDTO.java
│       │   ├── UserAddDTO.java
│       │   ├── UserPageDTO.java
│       │   ├── UserStatusDTO.java
│       │   └── UserUpdateDTO.java
│       ├── entity/                                        # 实体类
│       │   ├── Book.java
│       │   ├── BorrowRecord.java
│       │   ├── Category.java
│       │   ├── Notice.java
│       │   ├── SysUser.java
│       │   └── Todo.java
│       └── vo/                                            # 视图对象
│           ├── BookDetailVO.java
│           ├── BorrowTrendVO.java
│           ├── BorrowVO.java
│           ├── CategoryRatioVO.java
│           ├── CategoryVO.java
│           ├── DashboardVO.java
│           ├── FileUploadVO.java
│           ├── HotBookVO.java
│           ├── UserInfoVO.java
│           ├── UserListVO.java
│           └── UserVO.java
├── todo-mapper/
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/todo/todomanagementsystem/mapper/     # Mapper接口
│       │   ├── BookMapper.java
│       │   ├── BorrowRecordMapper.java
│       │   ├── CategoryMapper.java
│       │   ├── NoticeMapper.java
│       │   ├── SysUserMapper.java
│       │   └── TodoMapper.java
│       └── resources/com/todo/todomanagementsystem/mapper/ # XML映射
│           ├── BookMapper.xml
│           ├── BorrowRecordMapper.xml
│           ├── CategoryMapper.xml
│           ├── NoticeMapper.xml
│           ├── SysUserMapper.xml
│           └── TodoMapper.xml
├── todo-service/
│   ├── pom.xml
│   └── src/main/java/com/todo/todomanagementsystem/service/
│       ├── impl/                                          # Service实现类
│       │   ├── BookServiceImpl.java
│       │   ├── BorrowRecordServiceImpl.java
│       │   ├── CategoryServiceImpl.java
│       │   ├── NoticeServiceImpl.java
│       │   ├── StatServiceImpl.java
│       │   ├── SysUserServiceImpl.java
│       │   ├── TodoServiceImpl.java
│       │   └── UserServiceImpl.java
│       ├── BookService.java
│       ├── BorrowRecordService.java
│       ├── CategoryService.java
│       ├── NoticeService.java
│       ├── StatService.java
│       ├── SysUserService.java
│       ├── TodoService.java
│       └── UserService.java
└── todo-web/
    ├── pom.xml
    └── src/main/
        ├── java/com/todo/todomanagementsystem/
        │   ├── config/                                    # 配置类
        │   │   ├── CorsConfig.java
        │   │   ├── JwtConfig.java
        │   │   ├── MybatisPlusConfig.java
        │   │   ├── OpenApiConfig.java
        │   │   └── WebConfig.java
        │   ├── controller/                                # 控制层
        │   │   ├── BookController.java
        │   │   ├── BorrowController.java
        │   │   ├── CategoryController.java
        │   │   ├── FileController.java
        │   │   ├── StatController.java
        │   │   ├── TodoController.java
        │   │   └── UserController.java
        │   ├── exception/
        │   │   └── GlobalExceptionHandler.java            # 全局异常处理
        │   └── TodoManagementSystemApplication.java       # 启动类
        └── resources/
            └── application.yaml                           # 应用配置
```

### 3.4 模块职责说明

**todo-common**: 纯工具模块，不依赖Spring容器

* BusinessException: 业务异常基类

* JwtProperties: JWT配置属性类（@ConfigurationProperties）

* JwtUtil: JWT工具类（@Component，依赖JwtProperties）

* Result: 统一响应封装类

**todo-entity**: 数据模型模块

* DTO: 请求参数对象

* Entity: 数据库实体映射

* VO: 响应视图对象

**todo-mapper**: 数据访问模块

* Mapper接口: MyBatis-Plus数据访问接口

* XML映射文件: SQL语句定义

**todo-service**: 业务逻辑模块

* Service接口: 业务接口定义

* Impl实现: 业务逻辑实现

**todo-web**: 启动入口模块

* Controller: REST API控制层

* Config: Spring配置类

* GlobalExceptionHandler: 全局异常处理

* Application: Spring Boot启动类

***

## 四、POM配置方案

### 4.1 父pom.xml设计

**核心配置要点**:

1. `packaging`: `pom`（聚合工程）
2. `modules`: 声明所有子模块
3. `dependencyManagement`: 统一版本锁定
4. `properties`: 全局版本属性
5. `build/pluginManagement`: 插件统一管理

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.todo</groupId>
    <artifactId>TodoManagementSystem</artifactId>
    <version>0.0.1-SNAPSHOT</version>
    <packaging>pom</packaging>
    <name>Todo Management System</name>
    <description>Todo Management System</description>

    <modules>
        <module>todo-common</module>
        <module>todo-entity</module>
        <module>todo-mapper</module>
        <module>todo-service</module>
        <module>todo-web</module>
    </modules>

    <properties>
        <java.version>17</java.version>
        <spring-boot.version>3.2.5</spring-boot.version>
        <mybatis-plus.version>3.5.7</mybatis-plus.version>
        <jwt.version>0.11.5</jwt.version>
        <springdoc.version>2.3.0</springdoc.version>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>com.baomidou</groupId>
                <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
                <version>${mybatis-plus.version}</version>
            </dependency>

            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-api</artifactId>
                <version>${jwt.version}</version>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-impl</artifactId>
                <version>${jwt.version}</version>
                <scope>runtime</scope>
            </dependency>
            <dependency>
                <groupId>io.jsonwebtoken</groupId>
                <artifactId>jjwt-jackson</artifactId>
                <version>${jwt.version}</version>
                <scope>runtime</scope>
            </dependency>

            <dependency>
                <groupId>org.springdoc</groupId>
                <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
                <version>${springdoc.version}</version>
            </dependency>

            <dependency>
                <groupId>com.todo</groupId>
                <artifactId>todo-common</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.todo</groupId>
                <artifactId>todo-entity</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.todo</groupId>
                <artifactId>todo-mapper</artifactId>
                <version>${project.version}</version>
            </dependency>
            <dependency>
                <groupId>com.todo</groupId>
                <artifactId>todo-service</artifactId>
                <version>${project.version}</version>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <build>
        <pluginManagement>
            <plugins>
                <plugin>
                    <groupId>org.springframework.boot</groupId>
                    <artifactId>spring-boot-maven-plugin</artifactId>
                    <version>${spring-boot.version}</version>
                </plugin>
                <plugin>
                    <groupId>org.apache.maven.plugins</groupId>
                    <artifactId>maven-compiler-plugin</artifactId>
                    <version>3.11.0</version>
                    <configuration>
                        <source>${java.version}</source>
                        <target>${java.version}</target>
                        <encoding>UTF-8</encoding>
                    </configuration>
                </plugin>
            </plugins>
        </pluginManagement>
    </build>
</project>
```

### 4.2 子模块pom.xml设计

#### todo-common/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.todo</groupId>
        <artifactId>TodoManagementSystem</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>todo-common</artifactId>
    <packaging>jar</packaging>
    <name>todo-common</name>
    <description>公共工具模块</description>

    <dependencies>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-configuration-processor</artifactId>
            <optional>true</optional>
        </dependency>
        <dependency>
            <groupId>org.springframework.security</groupId>
            <artifactId>spring-security-crypto</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
```

#### todo-entity/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.todo</groupId>
        <artifactId>TodoManagementSystem</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>todo-entity</artifactId>
    <packaging>jar</packaging>
    <name>todo-entity</name>
    <description>实体模块</description>

    <dependencies>
        <dependency>
            <groupId>com.todo</groupId>
            <artifactId>todo-common</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

#### todo-mapper/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.todo</groupId>
        <artifactId>TodoManagementSystem</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>todo-mapper</artifactId>
    <packaging>jar</packaging>
    <name>todo-mapper</name>
    <description>数据访问模块</description>

    <dependencies>
        <dependency>
            <groupId>com.todo</groupId>
            <artifactId>todo-entity</artifactId>
        </dependency>
        <dependency>
            <groupId>com.baomidou</groupId>
            <artifactId>mybatis-plus-spring-boot3-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <scope>runtime</scope>
        </dependency>
    </dependencies>
</project>
```

#### todo-service/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.todo</groupId>
        <artifactId>TodoManagementSystem</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>todo-service</artifactId>
    <packaging>jar</packaging>
    <name>todo-service</name>
    <description>业务逻辑模块</description>

    <dependencies>
        <dependency>
            <groupId>com.todo</groupId>
            <artifactId>todo-mapper</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter</artifactId>
        </dependency>
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <scope>provided</scope>
        </dependency>
    </dependencies>
</project>
```

#### todo-web/pom.xml

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <parent>
        <groupId>com.todo</groupId>
        <artifactId>TodoManagementSystem</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </parent>

    <artifactId>todo-web</artifactId>
    <packaging>jar</packaging>
    <name>todo-web</name>
    <description>Web启动模块</description>

    <dependencies>
        <dependency>
            <groupId>com.todo</groupId>
            <artifactId>todo-service</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springdoc</groupId>
            <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
        </dependency>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <mainClass>com.todo.todomanagementsystem.TodoManagementSystemApplication</mainClass>
                </configuration>
                <executions>
                    <execution>
                        <goals>
                            <goal>repackage</goal>
                        </goals>
                    </execution>
                </executions>
            </plugin>
        </plugins>
    </build>
</project>
```

***

## 五、Spring Boot父依赖冲突解决方案

**问题**: Spring Boot Starter Parent与自定义父pom的冲突

**解决方案**: 使用`dependencyManagement`导入Spring Boot依赖管理，而非直接继承

```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-dependencies</artifactId>
            <version>3.2.5</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```

**原理**:

* `spring-boot-starter-parent`本质是继承`spring-boot-dependencies`

* 在多模块场景下，自定义父pom通过`<scope>import</scope>`引入`spring-boot-dependencies`

* 子模块依然可以使用Spring Boot的版本管理和自动配置

***

## 六、构建与运行

### 6.1 编译顺序（Maven自动处理）

1. todo-common → 2. todo-entity → 3. todo-mapper → 4. todo-service → 5. todo-web

### 6.2 常用命令

```bash
# 编译整个项目
mvn clean compile

# 运行特定模块测试
mvn test -pl todo-service

# 打包（跳过测试）
mvn clean package -DskipTests

# 运行Web模块
cd todo-web
mvn spring-boot:run

# 打包后运行
java -jar todo-web/target/todo-web-0.0.1-SNAPSHOT.jar
```

### 6.3 打包插件配置

* **父pom**: 在`pluginManagement`中声明插件版本，不实际执行

* **todo-web**: 在`build/plugins`中配置`spring-boot-maven-plugin`，使其可执行

***

## 七、常见踩坑点

| 问题          | 原因                           | 解决方案                           |
| ----------- | ---------------------------- | ------------------------------ |
| 版本冲突        | 多个模块依赖同一库不同版本                | 父pom统一锁定版本                     |
| 打包失败        | spring-boot-maven-plugin放错位置 | 仅在web模块配置                      |
| 循环依赖        | 模块间互相依赖                      | 严格遵循单向依赖                       |
| 资源找不到       | mapper.xml未正确配置              | 确保resources目录正确                |
| 启动失败        | 组件扫描路径不对                     | 配置@ComponentScan(basePackages) |
| JwtUtil注入失败 | JwtProperties不在同一模块          | 两者都放在common模块                  |
| 全局异常不生效     | @RestControllerAdvice不在启动模块  | 放在web模块                        |

***

## 八、Maven继承与聚合的设计逻辑

### 8.1 继承（Inheritance）- 纵向关系

**目的**: 子模块继承父pom的配置

**作用**:

* 统一管理依赖版本（dependencyManagement）

* 共享插件配置（pluginManagement）

* 共享properties配置

* 子模块可选择性覆盖父配置

**特点**:

* 一个子模块只能有一个直接父pom

* 子模块通过`<parent>`声明继承关系

* 继承是传递性的

### 8.2 聚合（Aggregation）- 横向关系

**目的**: 统一管理多个子模块的构建

**作用**:

* 定义modules列表，Maven按依赖顺序自动编译

* 在父目录执行命令，自动应用到所有子模块

* 支持`-pl`参数指定特定模块

* 支持`-am`参数自动构建依赖模块

**特点**:

* 一个聚合工程可以包含任意数量子模块

* 通过`<modules>`声明聚合关系

* 聚合与继承可以共存（通常结合使用）

### 8.3 两者分工

```
继承（父pom → 子pom）
├─ 管理版本：properties + dependencyManagement
├─ 管理插件：pluginManagement
└─ 共享配置：build、reporting等

聚合（父pom → modules）
├─ 定义模块清单
├─ 统一构建顺序
└─ 批量执行命令
```

***

## 九、实施步骤

### 步骤1：修改父pom.xml

* 将packaging改为pom

* 添加dependencyManagement

* 添加modules声明

* 添加pluginManagement

### 步骤2：创建todo-common模块

* 创建目录结构和pom

* 迁移util、exception、properties包

### 步骤3：创建todo-entity模块

* 创建目录结构和pom

* 迁移entity、dto、vo包

### 步骤4：创建todo-mapper模块

* 创建目录结构和pom

* 迁移mapper接口和XML

### 步骤5：创建todo-service模块

* 创建目录结构和pom

* 迁移service接口和实现

### 步骤6：创建todo-web模块

* 创建目录结构和pom

* 迁移controller、config、启动类、全局异常处理

### 步骤7：删除原src目录

* 删除旧的单体项目src目录

### 步骤8：验证构建

* 执行mvn clean compile

* 执行mvn clean package

* 启动应用验证运行

