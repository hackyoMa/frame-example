import axios from "axios";
import router from './router'
import store from './store'

const http = axios.create({
    baseURL: process.env.VUE_APP_BASE_API_URL
});

http.interceptors.response.use((response) => {
    return response;
}, (error) => {
    if (error.response.status === 401) {
        store.commit('setLoginStatus', false);
        router.push({
            path: '/login',
            query: {redirect: router.app._route.path}
        });
        error.message = '请重新登录';
    }
    if (error.response.status === 403) {
        error.message = '权限不足，无法访问';
    }
    return Promise.reject(error);
});

export default http;
