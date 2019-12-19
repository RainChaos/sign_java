<template>
    <div class="main-page">
    <x-header style="background-color:#0068b7;">
        教师登陆
        <router-link slot="right" :to="{path:'/'}">返回</router-link>
    </x-header>
        <div class="form-content">
            <el-row style="height:100%;background-color: #FFF;">
                <el-col :span="24" style="padding-top:0px;">
                    <el-form ref="loginForm"
                             :show-message="true"
                             :rules="searchRules"
                             :model="loginForm"
                             size="small"
                             label-position="left">
                        <el-form-item  label="用户名：" prop="username">
                            <el-input v-model="loginForm.username" placeholder="请输入用户名称" style="width:100%" auto-complete="off"/>
                        </el-form-item>
                        <el-form-item  label="密 码：" prop="password" >
                            <el-input v-model="loginForm.password"  type="password" placeholder="请输入密码"   style="width:100%" auto-complete="off"/>
                        </el-form-item>
                    </el-form>
                </el-col>
                <el-col :span="24" style="margin-top:20px;">
                    <x-button type="primary" @click.native="handleLogin">登 陆</x-button>
                </el-col>
            </el-row>
        </div>
    </div>
</template>

<script>
  import { XHeader,XInput,Selector,PopupPicker,Group,Flexbox, FlexboxItem,XButton } from 'vux'
  import { Loading } from 'element-ui';
  export default {
    name: "TeacherLogin",
    components: {
      XHeader,
      XInput,
      Selector,
      PopupPicker,
      Group,
      Flexbox,
      FlexboxItem,
      XButton
    },
    data () {
      return {
        loginForm: {
          username: '',
          password: ''
        },
        formSearchLoading: false,
        searchRules: {
          username: [
            { required: true, message: '用户名不能为空', trigger: 'blur'}
          ],
          password: [
            { required: true, message: '密码不能为空', trigger: 'blur' },
          ]
        }
      }
    },
    mounted(){
      let loadingInstance = Loading.service({});
      this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
        loadingInstance.close();
      });
    },
    methods: {
      handleLogin() {
        this.$refs.loginForm.validate((valid,errors) => {
          if (valid) {
            this.loading = true
            this.$store.dispatch('Login', this.loginForm).then(response => {
              this.loading = false
              this.$router.replace({ path: '/t/zhuanyeCount' })
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

<style scoped>

</style>
