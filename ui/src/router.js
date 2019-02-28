import Vue from 'vue'
import Router from 'vue-router'
import {Message} from 'element-ui'
import Home from './views/Home.vue'
import Login from './views/Login.vue'
import UserInfo from './views/user/UserInfo.vue'

Vue.use(Router);

const router = new Router({
    mode: 'history',
    base: process.env.BASE_URL,
    routes: [
        {
            path: '/',
            name: 'home',
            meta: {requiresAuth: false},
            component: Home
        },
        {
            path: '/login',
            name: 'login',
            meta: {requiresAuth: false},
            component: Login
        },
        {
            path: '/user/info',
            name: 'userInfo',
            meta: {requiresAuth: true},
            component: UserInfo
        }
    ]
});

router.beforeEach((to, from, next) => {
    if (to.meta.requiresAuth) {
        if (localStorage.getItem('token') === null && sessionStorage.getItem('token') === null) {
            Message.warning('请先登录');
            next({
                path: '/login',
                query: {redirect: to.path}
            });
        } else {
            next();
        }
    } else {
        if (to.name === 'login' && (localStorage.getItem('token') !== null || sessionStorage.getItem('token') !== null)) {
            Message.warning('您已登录');
            next(from.path);
        } else {
            next();
        }
    }
});

export default router;
