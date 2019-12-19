<template>
  <div class="main-page">
    <x-header style="background-color:#0068b7;">
      查询我的报名信息
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>

      <div class="form-content">
        <el-row style="height:100%;background-color: #FFF;">
          <el-col :span="24" style="padding-top:0px;">
            <el-form ref="SearchStudentForm"
                     :show-message="false"
                     :rules="searchRules"
                     :model="search"
                     size="small"
                     label-position="left">
              <el-form-item  label="姓名：" prop="search_name">
                <el-input v-model="search.search_name" placeholder="请输入学生姓名" style="width:100%" auto-complete="off"/>
              </el-form-item>
              <el-form-item  label="身份证号码：" prop="search_idcode">
                <el-input v-model="search.search_idcode"  placeholder="请输入学生身份证号码"  style="width:100%" auto-complete="off"/>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="24" style="margin-top:20px;">
            <x-button type="primary" @click.native="handleSearch"><i class="el-icon-search"></i> 查 询</x-button>
            <x-button type="default" @click.native="handleAddStudent">还没报名，去报名</x-button>
          </el-col>
        </el-row>
      </div>
  </div>
</template>

<script>
  import { XHeader,XInput,Selector,PopupPicker,Group,Flexbox, FlexboxItem,XButton } from 'vux'
  import { searchStudentInfo } from '@/api/student'
  import { formValidater } from '@/utils/formValidate'
  import { getArticleByTitle } from '@/api/article'
  import { Loading } from 'element-ui';
  export default {
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
    name: 'SearchStudent',
    data () {
      return {
        search: {
          search_name: '',
          search_idcode: ''
        },
        formSearchLoading: false,
        searchRules: {
          search_name: [{
            required: true,
            message: '学生姓名不能为空',
            trigger: 'blur'
          }],
          search_idcode: [
            { required: true, message: '学生身份证号码不能为空', trigger: 'blur' },
            { validator: formValidater.checkdIdCard, trigger: 'blur' }
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
      handleSearch() {
        this.formSearchLoading = true
        var _this =  this
        this.$refs.SearchStudentForm.validate((valid,errors) => {
          if (valid) {
            var data = JSON.parse(JSON.stringify(this.search))
            searchStudentInfo({ name: data.search_name, idcode: data.search_idcode }).then(response => {
              var student_id = response.data.id
              var name = response.data.name
              var sno = response.data.sno
              var pici_id=response.data.pici_id
              var education=response.data.education
              var message = '查询到学生【姓名：' + name + ',学号：' + sno + '】'

              this.$vux.alert.show({
                title: '成功提示',
                content: message,
                onHide () {
                  window.localStorage.setItem('student_id', response.data.id);
                  _this.$router.push({ path: '/edit', query: {pici_id:pici_id,education:education, openid: _this.$route.query.openid}})
                }
              })
              this.formSearchLoading = false
            }).catch(error => {
              this.formSearchLoading = false
            })
          } else {
            var errorHtml = []
            for (var k in errors) {
              errorHtml.push(errors[k][0].message)
            }
            this.$vux.alert.show({
              title: '错误提示',
              content: errorHtml.join('<br />')
            })
            this.formSearchLoading = false
          }
        })
      },
      handleAddStudent() {
        this.$router.push({ path: '/' , query:{openid: this.$route.query.openid}})
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
