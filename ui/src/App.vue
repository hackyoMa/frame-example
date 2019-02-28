<template>
    <div>
        <Menu></Menu>
        <router-view></router-view>
    </div>
</template>

<script>
    import Menu from '@/components/Menu.vue'

    export default {
        name: 'App',
        components: {
            Menu
        },
        created() {
            const localToken = localStorage.getItem('token');
            const sessionToken = sessionStorage.getItem('token');
            if (localToken !== null || sessionToken !== null) {
                this.$http.defaults.headers.common['Authorization'] = localToken === null ? sessionToken : localToken;
            }
        }
    }
</script>