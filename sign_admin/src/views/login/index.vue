<template>
  <div style="width:100%;height:100%;overflow: hidden;background: #409EFF;">
    <div class="login-container">
      <div class="login-input-left">
        <img src="@/assets/images/timg.jpg">
      </div>
      <el-form ref="loginForm" :model="loginForm" :rules="loginRules" class="login-form" auto-complete="on"
               label-position="left">
        <h3 class="title">管理员登陆信息录入</h3>
        <el-form-item prop="username">
          <span class="svg-container svg-container_login">
            <svg-icon icon-class="user"/>
          </span>
          <el-input v-model="loginForm.username" type="text" auto-complete="on" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item prop="password">
          <span class="svg-container">
            <svg-icon icon-class="password"/>
          </span>
          <el-input
            :type="pwdType"
            v-model="loginForm.password"
            auto-complete="on"
            placeholder="请输入密码"
            @keyup.enter.native="handleLogin"/>
          <!--/如果vue事件侦听函数绑定到一个封装好的组件上的原生事件
          则需要.native修饰符，如果是普通组件，则不需要该修饰符
          -->
          <span class="show-pwd" @click="showPwd">
            <svg-icon icon-class="eye"/>
          </span>
        </el-form-item>
        <el-form-item style="margin-top:50px">
          <el-button :loading="loading" type="primary" style="width:100%;" @click.native.prevent="handleLogin">
            登陆
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'Login',
    data() {
      const validatePass = (rule, value, callback) => {
        if (value.length < 5) {
          callback(new Error('密码不能小于5位'))
        } else {
          callback()
        }
      }
      return {
        loginForm: {
          username: 'Admin',
          password: ''
        },
        loginRules: {
          password: [{ required: true, trigger: 'blur', validator: validatePass }]
        },
        loading: false,
        pwdType: 'password',
        redirect: undefined
      }
    },
    //监听路由变化，验证通过后，自动跳转指定页面
    watch: {
      $route: {
        handler: function(route) {
          this.redirect = route.query && route.query.redirect
          //自动跳转指定页面
        },
        immediate: true //立即执行，而不是等到有变化时才执行
      }
    },
    methods: {
      showPwd() {
        if (this.pwdType === 'password') {
          this.pwdType = ''
        } else {
          this.pwdType = 'password'
        }
      },
      handleLogin() {
        this.$refs.loginForm.validate((valid, errors) => {
          //会验证该form所有字段的返回值，如果有不通过的valid就是false
          if (valid) {
            this.loading = true
            this.$store.dispatch('Login', this.loginForm).then(response => {
              this.loading = false
              const data = response.data
              localStorage.setItem('info', JSON.stringify(data))
              // const sl = data.is_one === 1
              // if (data.is_one) {
              //   this.$router.replace({ path: data.default_url, query: { sl: sl }})
              // } else {
              this.$router.replace({ path: data.default_url })
              // }
            }).catch(error => {
              console.log(error)
              this.loading = false
            })
          } else {
            var errorHtml = []
            for (const key in errors) {
              errorHtml.push(errors[key][0].message)
            }
            this.$alert(errorHtml.join('<br />'), '错误信息', { type: 'error', dangerouslyUseHTMLString: true })
            this.loading = false
          }
        })
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss">
  $bg: #889aa4;
  body {
    overflow: hidden;
  }

  /* reset element-ui css */
  .login-container {
    .el-input {
      display: inline-block;
      height: 35px;
      width: 85%;

      input {
        background: transparent;
        border: 0px;
        -webkit-appearance: none;
        border-radius: 0px;
        padding: 12px 5px 12px 15px;
        color: #001528;
        height: 35px;

        &:-webkit-autofill {
          -webkit-box-shadow: 0 0 0px 500px white inset !important;
          -webkit-text-fill-color: #666666 !important;
        }
      }
    }

    .el-form-item__content {
      line-height: 35px;
      position: relative;
      font-size: 14px;
    }

    .el-form-item {
      border-bottom: 1px solid #99a9bf;
      border-radius: 5px;
      color: #454545;
    }
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
  .login-container {
    -webkit-border-radius: 3px;
    border-radius: 3px;
    -moz-border-radius: 3px;
    background-clip: padding-box;
    margin: 150px auto;
    width: 736px;
    overflow: hidden;
    background: #fff;
    box-shadow: 0 0 20px #cac6c6;
    background: -ms-linear-gradient(top, #ace, #00C1DE); /* IE 10 */
    background: -moz-linear-gradient(top, #ace, #00C1DE); /*火狐*/
    background: -webkit-gradient(linear, 0% 0%, 0% 100%, from(#ace), to(#00C1DE)); /*谷歌*/
    background: -webkit-linear-gradient(top, #ace, #00C1DE); /*Safari5.1 Chrome 10+*/
    background: -o-linear-gradient(top, #ace, #00C1DE); /*Opera 11.10+*/
    .login-input-left {
      width: 336px;
      height: 466px;
      float: left;
    }

    .login-form {
      width: 400px;
      float: left;
      padding: 35px 35px 15px 35px;

    }

    .svg-container {
      padding: 6px 5px 6px 15px;
      color: #66b1ff;
      vertical-align: middle;
      width: 30px;
      display: inline-block;

      &_login {
        font-size: 20px;
      }
    }

    .title {
      font-size: 24px;
      font-weight: 400;
      color: #001528;
      margin: 20px auto 50px auto;
      text-align: center;
    }

    .show-pwd {
      position: absolute;
      right: 10px;
      top: 7px;
      font-size: 16px;
      color: #001528;
      cursor: pointer;
      user-select: none;
    }
  }
</style>
