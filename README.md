# 在线学习平台---后端（待完善，欢迎大佬指导学习）

## 项目概述

本项目旨在开发一个功能全面、用户体验优良的在线学习平台。平台涵盖用户管理、学习资源整合、在线考试及每日练题等核心功能模块，以满足日益增长的在线教育需求。系统采用前后端分离架构，前端使用 React 框架，结合 Ant Design 组件库和 Umi 开发框架；后端使用 Spring Boot 框架，结合 MyBatis 进行数据访问，并使用 MySQL 进行数据存储。

## 技术栈

### 前端

- **HTML**: 结构标记语言
- **CSS**: 样式表语言
- **JavaScript**: 编程语言
- **React**: 前端开发框架
- **Ant Design Pro**: 项目模板
- **Ant Design**: 组件库
- **Umi**: 开发框架
- **Umi Request**: 请求库
- **正向和反向代理**: 用于处理请求和提高性能

### 后端

- **Java**: 编程语言
- **Spring**: 依赖注入框架
- **SpringMVC**: Web 框架
- **SpringBoot**: 微服务框架
- **MyBatis**: 数据访问框架
- **MyBatis Plus**: MyBatis 的增强工具
- **MySQL**: 数据库
- **jUnit**: 单元测试库

## 功能模块

1. **用户管理模块**: 实现用户登录、注册及账户管理功能。
2. **导航菜单模块**: 提供平台内部页面的导航功能。
3. **用户列表管理模块**: 管理和展示平台上的所有用户信息。
4. **在线考试系统模块**: 支持用户进行在线测试，记录和反馈答案。
5. **导航管理模块**: 管理平台的导航项。
6. **用户查询模块**: 普通用户可以查询系统中的其他用户信息。

## 安装步骤

### 前端

1. **克隆仓库**

   ```bash
   git clone https://github.com/Cccc2212/study_platform_Front-end.git
   cd study_platform_Front-end
   ```

2. **安装依赖**

   ```bash
   npm install
   ```

   或

   ```bash
   yarn
   ```

3. **启动项目**

   ```bash
   npm start
   ```

4. **构建项目**

   ```bash
   npm run build
   ```

5. **检查代码风格**

   ```bash
   npm run lint
   ```

   自动修复一些 lint 错误：

   ```bash
   npm run lint:fix
   ```

6. **测试代码**

   ```bash
   npm test
   ```

7. **更多信息**

   你可以查看完整的文档在 [Ant Design Pro 官方网站](https://pro.ant.design)。欢迎在 [GitHub](https://github.com/ant-design/ant-design-pro) 提供反馈。

### 后端

1. **克隆仓库**

   ```bash
   git clone https://github.com/Cccc2212/study_platform_Back-end.git
   cd study_platform_Back-end
   ```

2. **配置环境**

   在项目根目录下创建一个 `.env` 文件，并添加以下内容来配置数据库连接和其他环境变量：

   ```
   SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/study_platform
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=your_password
   ```

   请确保将 `your_password` 替换为您的 MySQL 数据库密码。

3. **构建项目**

   使用 Maven 进行项目构建和依赖管理：

   ```bash
   mvn clean install
   ```

4. **启动项目**

   使用 Maven 启动 Spring Boot 应用程序：

   ```bash
   mvn spring-boot:run
   ```

   或者在 IDE 中直接运行 `com.example.StudyPlatformApplication` 类的 `main` 方法。

5. **数据库初始化**

   项目启动时会自动创建数据库表。如果需要手动初始化数据库，可以在 `src/main/resources` 目录下找到 SQL 脚本文件并执行。

6. **运行单元测试**

   确保项目正常构建后，运行单元测试以验证代码功能：

   ```bash
   mvn test
   ```

## 配置说明

- **数据库连接**: 配置文件中的 `SPRING_DATASOURCE_URL`、`SPRING_DATASOURCE_USERNAME` 和 `SPRING_DATASOURCE_PASSWORD` 应与实际的数据库设置一致。
- **日志配置**: 可以在 `src/main/resources/application.properties` 文件中配置日志级别和日志输出位置。

## 更多信息

有关更多详细信息和文档，请访问 [Spring Boot 官方文档](https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/) 以及 [MyBatis 官方文档](https://mybatis.org/mybatis-3/).

如有任何问题或反馈，请通过 [GitHub Issues](https://github.com/Cccc2212/study_platform_Back-end/issues) 联系我们。
```
