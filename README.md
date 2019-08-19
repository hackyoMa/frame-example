# 便捷开发框架

------

基于：java、mysql、spring boot2.1.x、vue-cli3.x

依赖：
> * jdk8、jdk11测试通过，通过修改根目录下pom文件中java.version即可
> * 数据库mysql8、mysql5.7测试通过，通过修改spring boot配置文件中spring.jpa.database-platform即可
> * 首次运行需要在启动后向数据库导入脚本，core/src/main/resources/import.sql

特性：
> * 前后端分离部署
> * 前后端数据交互使用axios，后端数据验证使用spring validation
> * 前端使用npm包管理，框架使用vue+element
> * 数据库访问使用spring data jpa+alibaba druid
> * 用户鉴权使用spring security+jwt的无状态认证方式
> * 用户鉴权前端路由拦截

使用：
> * 后端配置文件在boot/src/main/resources/config/
> * 前端配置文件在ui/.env.development、ui/.env.production，分别对应npm的server和build环境
> * 前端上下文配置在ui/vue.config.js中
> * 拉取代码，在ui/下运行：npm install，idea导入maven项目即可。
> * 后端运行boot/src/main/java/cn/spicybar/frame/FrameApplication文件，前端在ui/下运行npm run server或npm run build
