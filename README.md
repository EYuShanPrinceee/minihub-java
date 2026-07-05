# MiniHub Java

MiniHub 是一个基于 Spring Boot 4 的内容社区后端项目，用于学习 Java 后端开发中的用户认证、文章管理、权限控制、事务、缓存、异常处理和部署准备。

## 技术栈

- Java 21
- Spring Boot 4
- Spring MVC
- MyBatis
- MySQL
- Redis
- JWT
- BCrypt
- Maven
- Docker
- Apifox
- Swagger / OpenAPI

## 功能模块

- 用户注册
- 用户登录
- JWT 鉴权
- 当前用户注入
- RBAC 权限控制
- 文章发布
- 文章分页查询
- 文章详情查询
- 文章更新
- 文章软删除
- 点赞和浏览计数
- Redis 文章详情缓存
- 统一响应
- 统一异常处理

## 本地启动

1. 启动 MySQL
2. 启动 Redis

```bash
docker start minihub-redis