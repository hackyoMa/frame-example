<template>
    <div>
        <h1>用户名：{{username}}</h1>
    </div>
</template>

<script>
    export default {
        name: "User-Info",
        data() {
            return {
                username: 'username',
            };
        },
        mounted() {
            this.$http.get('/user').then((response) => {
                this.username = response.data['username'];
            }).catch((error) => {
                this.$message.error(error.message);
            });
        }
    }
</script>