import Vue from 'vue'
import Vuex from 'vuex'
import http from './http'

Vue.use(Vuex);

export default new Vuex.Store({
    state: {
        loginStatus: false,
        user: {}
    },
    getters: {
        getUser: (state) => {
            return state.user;
        },
        getLoginStatus: (state) => {
            return state.loginStatus;
        }
    },
    mutations: {
        setUser: (state, user) => {
            state.user = user;
        },
        setLoginStatus: (state, loginStatus) => {
            if (!loginStatus) {
                localStorage.removeItem('token');
                sessionStorage.removeItem('token');
                http.defaults.headers.common = {Accept: "application/json, text/plain, */*"};
                state.user = {};
            }
            state.loginStatus = loginStatus;
        }
    },
    actions: {}
});
