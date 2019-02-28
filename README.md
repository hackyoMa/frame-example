基于：jdk1.8、spring boot2.1.3、vue-cli3.4.1

特性：
  数据库访问使用spring data jpa+alibaba druid
  前后端数据交互使用axios
  前后端交互数据验证使用validation
  前端使用vue+element
  前端使用了npm管理包
  用户鉴权使用spring security+jwt的无状态认证方式
  用户鉴权前端路由做了拦截

使用：
  后端配置文件在boot/src/main/resources/config/
  前端配置文件在ui/.env.development、ui/.env.production，分别对应npm的server和build环境
  前端上下文配置在ui/vue.config.js中
  拉取代码，在ui/下运行：npm install，idea导入maven项目即可。
  后端运行CinemaApplication文件，前端在ui/下运行npm run server或npm run build

代码地址：https://github.com/hackyoMa/frame-example
