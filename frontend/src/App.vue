<template>
    <el-container v-if="isRouterAlive">
        <el-header>
            <Menu/>
        </el-header>
        <el-main>
            <router-view/>
        </el-main>
    </el-container>
</template>

<script>
    import Menu from '@/components/Menu.vue'

    export default {
        name: 'App',
        components: {
            Menu
        },
        data() {
            return {
                isRouterAlive: true
            }
        },
        created() {
            const localToken = localStorage.getItem('token');
            const sessionToken = sessionStorage.getItem('token');
            if (localToken !== null || sessionToken !== null) {
                this.$http.defaults.headers.common['Authorization'] = localToken === null ? sessionToken : localToken;
                this.$store.commit('setLoginStatus', true);
                this.$http.get('/user').then((response) => {
                    this.$store.commit('setUser', response.data);
                    this.reload();
                }).catch((error) => {
                    this.$message.error(error.message);
                });
            }
        },
        methods: {
            reload() {
                this.isRouterAlive = false;
                this.$nextTick(() => (this.isRouterAlive = true));
            }
        }
    }
</script>