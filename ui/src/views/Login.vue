<template>
    <div>
        <el-row justify="center" type="flex">
            <el-col :lg="8" :md="10" :sm="16" :xl="8" :xs="22" style="text-align: center;">
                <img alt="logo" src="@/assets/images/logo347x74@2x.png" style="width: 347px;height: 74px;margin: 36px 0 18px 0;"/>
                <p style="color:#909399;font-size: 18px;">便捷开发框架</p>
            </el-col>
        </el-row>
        <el-row justify="center" style="margin-top: 18px;" type="flex">
            <el-col :lg="8" :md="8" :sm="15" :xl="8" :xs="22">
                <el-tabs :stretch="true" type="border-card" v-model="activeName">
                    <el-tab-pane label="登录" name="login">
                        <el-form :model="loginForm" :rules="loginRules" ref="loginForm" status-icon style="margin-top: 22px;">
                            <el-form-item prop="username">
                                <el-input auto-complete="on" clearable placeholder="请输入邮箱或手机号" type="text" v-model="loginForm.username"></el-input>
                            </el-form-item>
                            <el-form-item prop="password">
                                <el-input @keyup.enter.native="submitForm('loginForm')" auto-complete="off" clearable placeholder="请输入密码" type="password" v-model="loginForm.password"></el-input>
                            </el-form-item>
                            <el-form-item prop="remember" style="text-align: center">
                                <el-checkbox v-model="loginForm.remember">保持登录状态</el-checkbox>
                            </el-form-item>
                            <el-form-item>
                                <el-button @click="submitForm('loginForm')" style="width: 100%;" type="primary">登录</el-button>
                            </el-form-item>
                        </el-form>
                    </el-tab-pane>
                    <el-tab-pane label="注册" name="registered">
                        <el-form :model="regForm" :rules="regRules" ref="regForm" status-icon style="margin-top: 22px;">
                            <el-form-item prop="username">
                                <el-input auto-complete="on" clearable placeholder="请输入注册邮箱或手机号" type="text" v-model="regForm.username"></el-input>
                            </el-form-item>
                            <el-form-item prop="nickname">
                                <el-input auto-complete="on" clearable placeholder="请输入昵称" type="text" v-model="regForm.nickname"></el-input>
                            </el-form-item>
                            <el-form-item prop="password">
                                <el-input auto-complete="off" clearable placeholder="请设置密码" type="password" v-model="regForm.password"></el-input>
                            </el-form-item>
                            <el-form-item prop="checkPassword">
                                <el-input @keyup.enter.native="submitForm('regForm')" auto-complete="off" clearable placeholder="请再次输入密码" type="password" v-model="regForm.checkPassword"></el-input>
                            </el-form-item>
                            <el-form-item>
                                <el-button @click="submitForm('regForm')" style="width: 100%;" type="primary">注册</el-button>
                            </el-form-item>
                        </el-form>
                    </el-tab-pane>
                </el-tabs>
            </el-col>
        </el-row>
        <Particles></Particles>
    </div>
</template>

<script>
    import Particles from '@/components/Particles'

    export default {
        name: "Login",
        components: {
            Particles
        },
        data() {
            let validateUsernameByLogin = (rule, value, callback) => {
                const regular = new RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/);
                const regular2 = new RegExp(/^1\d{10}$/);
                if (value === '') {
                    callback(new Error('请输入邮箱或手机号'));
                } else if (!regular.test(value) && !regular2.test(value)) {
                    callback(new Error('邮箱或手机号格式不正确'));
                } else if (value.length < 5 || value.length > 64) {
                    callback(new Error('邮箱或手机号格式不正确'));
                } else {
                    callback();
                }
            };
            let validatePasswordByLogin = (rule, value, callback) => {
                const regular = new RegExp(/^(?![^a-zA-Z]+$)(?!\D+$).{8,64}$/);
                if (value === '') {
                    callback(new Error('请输入密码'));
                } else if (!regular.test(value)) {
                    callback(new Error('密码错误'));
                } else {
                    callback();
                }
            };
            let validateUsernameByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+[a-zA-Z]+$/);
                const regular2 = new RegExp(/^1\d{10}$/);
                if (value === '') {
                    callback(new Error('请输入注册邮箱或手机号'));
                } else if (!regular.test(value) && !regular2.test(value)) {
                    callback(new Error('邮箱或手机号格式不正确'));
                } else if (value.length < 5 || value.length > 64) {
                    callback(new Error('邮箱长度必须在5-64位之间'));
                } else {
                    this.$http.post('/user/_check-username-exists', {
                        username: value
                    }).then((response) => {
                        if (!response.data) {
                            callback();
                        } else {
                            callback(new Error('用户名已存在'));
                        }
                    }).catch((error) => {
                        callback(new Error(error.message));
                    });
                }
            };
            let validateNicknameByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^[\s]+$/);
                if (value === '' || regular.test(value)) {
                    callback(new Error('请输入注册昵称'));
                } else if (value.length < 1 || value.length > 16) {
                    callback(new Error('昵称长度必须在1-16位之间'));
                } else {
                    callback();
                }
            };
            let validatePasswordByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^(?![^a-zA-Z]+$)(?!\D+$).{8,64}$/);
                if (value === '') {
                    callback(new Error('请设置密码'));
                } else if (!regular.test(value)) {
                    callback(new Error('密码长度必须在8-64位之间，且必须包含数字和字母'));
                } else {
                    if (this.regForm.checkPassword !== '') {
                        this.$refs.regForm.validateField('checkPassword');
                    }
                    callback();
                }
            };
            let validateCheckPasswordByRegistered = (rule, value, callback) => {
                const regular = new RegExp(/^(?![^a-zA-Z]+$)(?!\D+$).{8,64}$/);
                if (value === '') {
                    callback(new Error('请再次输入密码'));
                } else if (!regular.test(value)) {
                    callback(new Error('密码长度必须在8-64位之间，且必须包含数字和字母'));
                } else if (value !== this.regForm.password) {
                    callback(new Error('两次输入密码不一致'));
                } else {
                    callback();
                }
            };
            return {
                activeName: 'login',
                loginForm: {
                    username: '',
                    password: '',
                    remember: false
                },
                loginRules: {
                    username: [
                        {validator: validateUsernameByLogin, trigger: 'blur'}
                    ],
                    password: [
                        {validator: validatePasswordByLogin, trigger: 'blur'}
                    ]
                },
                regForm: {
                    username: '',
                    nickname: '',
                    password: '',
                    checkPassword: ''
                },
                regRules: {
                    username: [
                        {validator: validateUsernameByRegistered, trigger: 'blur'}
                    ],
                    nickname: [
                        {validator: validateNicknameByRegistered, trigger: 'blur'}
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
        methods: {
            submitForm(formName) {
                this.$refs[formName].validate((valid) => {
                    if (valid) {
                        if (formName === 'loginForm') {
                            this.login();
                        } else if (formName === 'regForm') {
                            this.registered();
                        }
                    } else {
                        return false;
                    }
                });
            },
            login() {
                this.$http.post('/user/_login', {
                    username: this.loginForm.username,
                    password: this.loginForm.password
                }).then((response) => {
                    if (response.data['status'] === 'success') {
                        if (this.loginForm.remember) {
                            localStorage.setItem('token', response.data['token']);
                        } else {
                            sessionStorage.setItem('token', response.data['token']);
                        }
                        this.$http.defaults.headers.common['Authorization'] = response.data['token'];
                        this.$store.commit('setLoginStatus', true);
                        this.$http.get('/user').then((response) => {
                            this.$store.commit('setUser', response.data);
                            this.$message.success('登录成功');
                            this.$refs['loginForm'].resetFields();
                            const redirect = this.$route.query['redirect'];
                            if (redirect !== undefined && redirect !== null && redirect !== '') {
                                this.$router.push(redirect + '');
                            } else {
                                this.$router.push('/');
                            }
                        }).catch((error) => {
                            this.$message.error(error.message);
                        });
                    } else {
                        this.$message.warning('用户名或密码错误');
                        this.$refs['loginForm'].clearValidate();
                    }
                }).catch((error) => {
                    this.$message.error(error.message);
                });
            },
            registered() {
                this.$http.post('/user', {
                    username: this.regForm.username,
                    nickname: this.regForm.nickname,
                    password: this.regForm.password
                }).then((response) => {
                    if (response.data['status'] === 'success') {
                        this.$message.success('注册成功，请登录');
                        this.activeName = 'login';
                        this.$refs['regForm'].resetFields();
                    } else {
                        for (let key in response.data) {
                            if (response.data.hasOwnProperty(key) && key !== 'status') {
                                this.$message.warning(response.data[key]);
                            }
                        }
                    }
                }).catch((error) => {
                    this.$message.error(error.message);
                });
            }
        }
    }
</script>