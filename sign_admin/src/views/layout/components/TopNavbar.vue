<template>
  <div id="topNavbar" style="display:flex;display:-webkit-flex;flex-direction:row;align-items:stretch;">
    <el-menu
      :default-active="activeIndex"
      class="top-navbar"
      mode="horizontal"
      background-color="#19548E"
      text-color="#FFF"
      active-text-color="#FFF"
      style="border:none;">
      <template>
        <div class="top-logo-ct">
          <img :src="logoUrl" width="250">
        </div>
        <div class="top-title-ct">学生报名管理系统</div>
      </template>
      <div class="nav-item-right" style="padding-right: 40px;padding-left:8px;" @click="logout"><span class="fa fa-sign-out"/> 退出</div>
      <div class="nav-item-right" style="padding:0px 8px" @click="showResetPassword"><i class="fa fa-pencil-square-o"/>修改密码</div>
      <div class="nav-item-right" style="padding:0px 8px" ><span class="fa fa-user-circle"/> 管理员：{{ username }}</div>
    </el-menu>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="450px">
      <div slot="title"><i class="fa fa-pencil-square-o"/> 修改密码</div>
      <el-form ref="resetPassForm" :model="form" :rules="rules" size="small">
        <el-form-item :label-width="formLabelWidth" label="原密码：" prop="old_password">
          <el-input v-model="form.old_password" type="password" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="新密码：" prop="new_password">
          <el-input v-model="form.new_password" type="password" auto-complete="off"/>
        </el-form-item>
        <el-form-item ref="checkNumber" :label-width="formLabelWidth" label="密码强度：" prop="check_number">
          <el-button-group>
            <el-button :type="form.check_number===0?'danger':'info'">弱鸡</el-button>
            <el-button :type="form.check_number===1?'warning':'info'">还行</el-button>
            <el-button :type="form.check_number===2?'primary':'info'">较强</el-button>
            <el-button :type="form.check_number===3?'success':'info'">很强</el-button>
            <el-button :type="form.check_number===4?'success':'info'">超强</el-button>
          </el-button-group>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="确认密码：" prop="check_password">
          <el-input v-model="form.check_password" type="password" auto-complete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>
<script>
import { updatePassword } from '@/api/operator'
import { checkPassWord } from '@/utils/validate'
import store from '@/store'
export default {
  name: 'TopNavbar',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入新密码'))
      } else {
        if (this.form.check_password !== '') {
          this.$refs['resetPassForm'].validateField('check_password')
        }
        callback()
      }
    }
    var validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请再次输入密码'))
      } else if (value !== this.form.new_password) {
        callback(new Error('两次输入密码不一致'))
      } else {
        callback()
      }
    }
    var checkNumberM = (rule, value, callback) => {
      if (value < 2) {
        callback(new Error('密码太弱，密码至少大于6位且包含字符和数字'))
      } else {
        callback()
      }
    }
    return {
      activeIndex: '1',
      dialogFormVisible: false,
      formLabelWidth: '100px',
      formSumbitLoading: false,
      form: {
        operator_id: 0,
        old_password: '',
        new_password: '',
        check_number: 0,
        check_password: ''
      },
      rules: {
        old_password: [
          { required: true, message: '原密码不能为空', trigger: 'blur' },
          { min: 5, max: 18, message: '长度在 5 到 18 个字符', trigger: 'blur' }
        ],
        new_password: [
          { required: true, validator: validatePass, trigger: 'blur' },
          { min: 5, max: 18, message: '长度在 5 到 18 个字符', trigger: 'blur' }
        ],
        check_password: [
          { required: true, validator: validatePass2, trigger: 'blur' }
        ],
        check_number: [
          { required: true, validator: checkNumberM, trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    username: function() {
      return store.getters.username
    },
    logoUrl: function() {
      // console.log("logoUrl" + store.getters.logo_image_url)
      return process.env.BASE_API + store.getters.logo_image_url
    }
  },
  watch: {
    form: {
      handler(curVal, oldVal) {
        this.$refs.checkNumber.clearValidate()
        var checkNumber = checkPassWord(curVal.new_password)
        this.form.check_number = checkNumber
      },
      deep: true
    }
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch('ToggleSideBar')
    },
    logout() {
      this.$store.dispatch('LogOut').then(() => {
        location.reload() // 为了重新实例化vue-router对象 避免bug
      })
    },
    showResetPassword() {
      this.dialogFormVisible = true
    },
    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs.resetPassForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          return new Promise((resolve, reject) => {
            updatePassword(formdata).then(response => {
              this.dialogFormVisible = false
              this.$message.success('修改成功，下次登陆生效')
              this.formSumbitLoading = false
              resolve(response)
            }).catch(error => {
              this.formSumbitLoading = false
              reject(error)
            })
          })
        } else {
          return false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
     @import "src/styles/variables.scss";
    .top-navbar {
      border-radius: 0px !important;
      width:100%;
      height:63px;
      .nav-item-right{
        color:#ececec;
        font-size: 14px;
        float: right;
        line-height: 63px;
        cursor:pointer;
      }
      .nav-item-right:hover{
        color:#FFF;
      }
      .top-logo-ct{
        min-width:220px;
        float: left;
        padding:5px 20px;
        margin-right:20px;
      }
      .top-title-ct{
        float: left;
        padding-top:10px;
        line-height: 40px;
        font-weight: 400;
        font-size:22px;
        color:#fff;
        letter-spacing: 1px;
      }
    }
</style>
