<template>
    <div>
        <el-menu :default-active="activeIndex" @select="handleSelect" mode="horizontal">
            <el-menu-item index="1">首页</el-menu-item>
            <el-menu-item index="2" style="float: right;" v-show="!loginStatus()">登录</el-menu-item>
            <el-submenu index="3" style="float: right;" v-show="loginStatus()">
                <template slot="title">我的</template>
                <el-menu-item index="3-1">账号信息</el-menu-item>
                <el-menu-item index="3-2">注销</el-menu-item>
            </el-submenu>
        </el-menu>
    </div>
</template>

<script>
    export default {
        name: 'Menu',
        data() {
            return {
                activeIndex: '1',
            };
        },
        mounted() {
            this.watchRoute();
        },
        watch: {
            '$route': 'watchRoute'
        },
        methods: {
            loginStatus() {
                return localStorage.getItem('token') !== null || sessionStorage.getItem('token') !== null;
            },
            watchRoute() {
                switch (this.$route.name) {
                    case 'home':
                        this.activeIndex = '1';
                        break;
                    case 'login':
                        this.activeIndex = '2';
                        break;
                    case 'userInfo':
                        this.activeIndex = '3-1';
                        break;
                }
            },
            handleSelect(key) {
                switch (key) {
                    case '1':
                        this.$router.push('/');
                        break;
                    case '2':
                        this.$router.push('/login');
                        break;
                    case '3-1':
                        this.$router.push('/user/info');
                        break;
                    case '3-2':
                        localStorage.removeItem('token');
                        sessionStorage.removeItem('token');
                        this.$http.defaults.headers.common = {Accept: "application/json, text/plain, */*"};
                        this.$router.push('/');
                        break;
                }
            }
        }
    }
</script>