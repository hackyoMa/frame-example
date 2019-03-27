<template>
    <el-row justify="center" style="margin-top: 38px;" type="flex">
        <el-col :lg="8" :md="10" :sm="16" :xl="8" :xs="22">
            <el-tabs :stretch="true" type="border-card" v-model="activeName">
                <el-tab-pane label="个人信息" name="userInfo">
                    <el-form :model="userInfoFrom" :rules="userInfoRules" ref="userInfoFrom" status-icon style="margin-top: 22px;">
                        <el-form-item prop="username">
                            <el-input auto-complete="on" clearable disabled="disabled" placeholder="请输入邮箱或手机号" type="text" v-model="userInfoFrom.username"></el-input>
                        </el-form-item>
                        <el-form-item prop="nickname">
                            <el-input auto-complete="on" clearable placeholder="请输入要修改的昵称" type="text" v-model="userInfoFrom.nickname"></el-input>
                        </el-form-item>
                        <el-form-item prop="modifyPassword" style="text-align: center;">
                            <el-switch active-text="修改密码" inactive-text="不修改密码" v-model="modifyPassword"></el-switch>
                        </el-form-item>
                        <el-form-item prop="sourcePassword" v-if="modifyPassword">
                            <el-input auto-complete="off" clearable placeholder="请输入原密码" type="password" v-model="userInfoFrom.sourcePassword"></el-input>
                        </el-form-item>
                        <el-form-item prop="password" v-if="modifyPassword">
                            <el-input auto-complete="off" clearable placeholder="请输入新密码" type="password" v-model="userInfoFrom.password"></el-input>
                        </el-form-item>
                        <el-form-item prop="checkPassword" v-if="modifyPassword">
                            <el-input auto-complete="off" clearable placeholder="请再次输入新密码" type="password" v-model="userInfoFrom.checkPassword"></el-input>
                        </el-form-item>
                        <el-form-item>
                            <el-button @click="submitForm('userInfoFrom')" style="width: 100%;" type="primary">保存修改</el-button>
                        </el-form-item>
                    </el-form>
                </el-tab-pane>
            </el-tabs>
        </el-col>
    </el-row>
</template>

<script>
    export default {
        name: "UserInfo",
        data() {
            let validateNicknameByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^[\s]+$/);
                if (value === '' || regular.test(value)) {
                    callback(new Error('请输入昵称'));
                } else if (value.length < 1 || value.length > 16) {
                    callback(new Error('昵称长度必须在1-16位之间'));
                } else {
                    callback();
                }
            };
            let validatePasswordByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^(?![^a-zA-Z]+$)(?!\D+$).{8,64}$/);
                if (value === '') {
                    callback(new Error('请设置新密码'));
                } else if (!regular.test(value)) {
                    callback(new Error('密码长度必须在8-64位之间，且必须包含数字和字母'));
                } else {
                    if (this.userInfoFrom.checkPassword !== '') {
                        this.$refs.userInfoFrom.validateField('checkPassword');
                    }
                    callback();
                }
            };
            let validateCheckPasswordByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^(?![^a-zA-Z]+$)(?!\D+$).{8,64}$/);
                if (value === '') {
                    callback(new Error('请再次输入新密码'));
                } else if (!regular.test(value)) {
                    callback(new Error('密码长度必须在8-64位之间，且必须包含数字和字母'));
                } else if (value !== this.userInfoFrom.password) {
                    callback(new Error('两次输入密码不一致'));
                } else {
                    callback();
                }
            };
            let validatesourcePasswordByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^(?![^a-zA-Z]+$)(?!\D+$).{8,64}$/);
                if (value === '') {
                    callback(new Error('请输入原密码'));
                } else if (!regular.test(value)) {
                    callback(new Error('密码错误'));
                } else {
                    this.$http.post('/user/_valid-password', {"password": value}).then((response) => {
                        if (response.data) {
                            callback();
                        } else {
                            callback(new Error('密码错误'));
                        }
                    }).catch((error) => {
                        this.$message.error(error.message);
                    });
                }
            };
            return {
                activeName: 'userInfo',
                modifyPassword: false,
                userInfoFrom: {
                    username: '',
                    nickname: '',
                    sourcePassword: '',
                    password: '',
                    checkPassword: ''
                },
                userInfoRules: {
                    nickname: [
                        {validator: validateNicknameByRegistered, trigger: 'blur'}
                    ],
                    sourcePassword: [
                        {validator: validatesourcePasswordByRegistered, trigger: 'blur'}
                    ],
                    password: [
                        {validator: validatePasswordByRegistered, trigger: 'blur'}
                    ],
                    checkPassword: [
                        {validator: validateCheckPasswordByRegistered, trigger: 'blur'}
                    ]
                }
            };
        },
        mounted() {
            this.userInfoFrom.nickname = this.$store.getters.getUser['nickname'];
            this.userInfoFrom.username = this.$store.getters.getUser['username'];
        },
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        this.$http.put('/user', {
                            nickname: this.userInfoFrom.nickname,
                            password: this.modifyPassword ? this.userInfoFrom.password : ''
                        }).then((response) => {
                            if (this.modifyPassword) {
                                this.$message.success('修改成功，请重新登录');
                                this.$store.commit('setLoginStatus', false);
                                this.$router.push('/login');
                            } else {
                                this.$message.success('修改成功');
                            }
                        }).catch((error) => {
                            this.$message.error(error.message);
                        });
                    } else {
                        return false;
                    }
                });
            }
        }
    }
</script>