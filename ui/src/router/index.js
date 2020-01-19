import Vue from 'vue'
import VueRouter from 'vue-router'
import {Message} from 'element-ui'

Vue.use(VueRouter);

const routes = [
    {
        path: '/',
        name: 'home',
        meta: {requiresAuth: false},
        component: () => import('../views/Home.vue')
    },
    {
        path: '/login',
        name: 'login',
        meta: {requiresAuth: false},
        component: () => import('../views/Login.vue')
    },
    {
        path: '/user/info',
        name: 'userInfo',
        meta: {requiresAuth: true},
        component: () => import('../views/user/UserInfo.vue')
    }
];

const router = new VueRouter({
    routes
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
