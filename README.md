# 便捷开发框架

------

基于：java、spring boot2.x、vue-cli4.x

依赖：
> * jdk8、jdk11测试通过，通过修改根目录下pom文件中java.version即可
> * 多种数据库支持，通过修改pom.xml中的jdbc依赖，以及application.yml文件中spring.datasource.driver-class-name、spring.jpa.database-platform、spring.jpa.database即可
> * 首次运行样例需要在启动后向数据库导入脚本，core/resources/import.sql

特性：
> * 前后端可分离部署，也可合并打包。合并打包需先在前端执行npm run build
> * 前后端数据交互使用axios，后端数据验证使用spring validation
> * 前端使用npm包管理，框架使用vue + element
> * 数据库访问使用spring data jpa + alibaba druid
> * 用户鉴权使用spring security + jwt的无状态认证方式
> * 用户鉴权前端路由拦截

使用：
> * 后端配置文件在boot/resources/config/
> * 前端配置文件在ui/.env.development、ui/.env.production，分别对应npm的server和build环境
> * 如需修改或删除上下文，需要同时修改application.yml、vue.config.js、.env.development、.env.production四个文件中的配置
> * 前端在ui/下先执行npm install，然后再执行npm run server或npm run build即可
> * 后端运行boot/java/cn/spicybar/frame/BootApplication文件
