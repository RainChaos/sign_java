<template>

  <div class="app-container container" style="height:100%;">
    <el-row style="height:calc(100% - 60px);background-color: #FFF;">
      <el-row>
        <div v-html="topInfoContent" style="margin-left: 10px">
          {{ topInfoContent }}
        </div>
      </el-row>
      <el-row style="padding:10px">
        <el-row style="overflow: hidden;background-color: #ececec;padding:20px;">
          <el-col :span="18" style="padding:0px 20px 0px 20px;border-right: 1px solid #ececec">
            <div style="font-size:15px;font-weight: bolder">报名请先选择报名类型</div>
            <div class="span-text">我是：</div>
            <el-row style="margin-top:10px;">
              <div v-for="item in typeLists" :key="item.id" class="blockItem">
                <div :class="{'typeItem':true, 'selectedItem':studentType==item.code}" @click="handleStudentType(item.code)">{{ item.name }}</div>
              </div>
            </el-row>
            <div class="span-text">我要参加：</div>
            <el-row style="margin-top:10px;">
              <div v-for="item in piciLists" :key="item.id" class="blockItem">
                <div v-if="item.baoming_status===1" :class="{'typeItem':true, 'selectedItem':studentPici==item.id}" @click="handleStudentPici(item.id,item.baoming_status)">{{ item.remark }}</div>
                <div v-if="item.baoming_status===2" :class="{'typeItem':true, 'selectedItem':studentPici==item.id}" @click="handleStudentPici(item.id,item.baoming_status)">{{ item.remark }}[未开始]</div>
                <div v-if="item.baoming_status===3" :class="{'typeItem':true, 'selectedItem':studentPici==item.id}" @click="handleStudentPici(item.id,item.baoming_status)">{{ item.remark }}[已结束]</div>
              </div>
            </el-row>
            <div style="margin-top:25px;">
              <el-button :disabled="baomingStatus!==1" type="primary" style="width:200px;" @click.native="handleAddStudent">下一步</el-button>
            </div>
          </el-col>
          <el-col :span="6" style="padding:0px 20px;text-align: center;border-left:1px solid #fff">
            <div style="width:220px;padding:0px 10px;text-align: center;">
              <div><vue-qr :text="config.weixin" :size="200" :margin="10"/></div>
              <div style="line-height:40px;font-weight: bolder">【微信/支付宝】扫一扫报名</div>
            </div>
          </el-col>
        </el-row>

        <el-row style="overflow: hidden;background-color: #ececec;padding:20px;margin-top:10px;">
          <div style="font-size:15px;font-weight: bolder;width:100%;padding-left:20px;">我已经报名，查询我的信息</div>
          <el-row style="margin-top:20px">
            <div style="float: left;overflow: auto">
              <el-form ref="SearchStudentForm" :show-message="false" :rules="searchRules" :model="search" size="small" label-position="right">
                <el-form-item label-width="120px" label="姓 名：" prop="search_name">
                  <el-input v-model="search.search_name" style="width:330px" auto-complete="off"/>
                </el-form-item>
                <el-form-item label-width="120px" label="身份证号码：" prop="search_idcode">
                  <el-input v-model="search.search_idcode" style="width:330px" auto-complete="off"/>
                </el-form-item>
              </el-form>
            </div>
            <div style="float: left;padding:20px;border-left:1px solid #fff;margin-left:20px;">
              <el-button :loading="formSearchLoading" style="width:200px;" type="warning" @click="handleSearch">
                <i class="el-icon-search"/>
                查询已报名
              </el-button>
            </div>
          </el-row>
        </el-row>
      </el-row>
    </el-row>
  </div>
</template>

<script>
import { searchStudentInfo } from '@/api/student'
import { formValidater } from '@/utils/formValidate'
import { getArticleByTitle } from '@/api/article'
import { getPiciLists } from '@/api/studentPici'
import { getListsByCode } from '@/api/dictionary'
import { getSelectUnitCount } from '@/api/selectUnit'
import VueQr from 'vue-qr'
export default {
  components: {
    VueQr
  },
  data() {
    return {
      activeIndex: '1',
      baomingStatus: 1,
      config: {
        weixin: 'www.baidu.com',
        phone: 'www.baidu.com'
      },
      topInfoContent: '',
      search: {
        search_name: '',
        search_idcode: ''
      },
      formSearchLoading: false,
      searchRules: {
        search_name: [
          { required: true, message: '姓名不能为空，请输入姓名', trigger: 'blur' }
        ],
        search_idcode: [
          { required: true, message: '身份证号码不能为空，请输入身份证号码', trigger: 'blur' },
          { validator: formValidater.checkdIdCard, trigger: 'blur' }
        ]
      },
      studentType: '',
      studentPici: 0,
      piciLists: [],
      typeLists: []
    }
  },

  mounted() {
    var url = 'http://' + document.domain + '/phone'
    this.config.weixin = url
    this.config.phone = url
    this.fetchTopInfoContent()
    this.fetchTypeLists()
    this.fetchPiciLists()
  },
  methods: {
    fetchTopInfoContent() {
      getArticleByTitle({ title: '在线交费页面顶部提示信息' }).then(response => {
        this.topInfoContent = response.data.content
      }).catch(error => {
        console.log(error)
      })
    },
    fetchPiciLists() {
      getPiciLists().then(reponse => {
        this.piciLists = reponse.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchTypeLists() {
      getListsByCode({ type: 'BEDUCATION' }).then(reponse => {
        this.typeLists = reponse.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    handleStudentType(name) {
      this.studentType = name
    },
    handleStudentPici(name,status){
      this.baomingStatus = status
      this.studentPici = name
    },
    handleAddStudent() {
      var type = this.studentType
      var pici = this.studentPici

      if (type === '') {
        this.$alert('请先选择学生类型', '错误提示', { type: 'error' })
        return
      }
      if (pici === 0) {
        this.$alert('请先选择报名季节', '错误提示', { type: 'error' })
        return
      }
      if (type !== '' && pici !== 0) {
        getSelectUnitCount({ pici_id: pici, education: type }).then(response => {
          var count = parseInt(response.data)
          if (count > 0) {
            this.$router.push({ path: '/add', query: { type: type, pici: pici }}) // home/add?type=高中生&pici=4
          } else {
            this.$alert('所选类型还没开放专业报名', '温馨提示', { type: 'info' })
          }
        })
      }
    },
    handleSearch() {
      this.formSearchLoading = true
      this.$refs.SearchStudentForm.validate((valid, errors) => {
        if (valid) {
          var data = JSON.parse(JSON.stringify(this.search))
          searchStudentInfo({ name: data.search_name, idcode: data.search_idcode }).then(response => {
            var student_id = response.data.id
            window.localStorage.setItem('student_id', student_id);
            var name = response.data.name
            var sno = response.data.sno
            var pici_id=response.data.pici_id
            var education=response.data.education
            var message = '查询到学生【姓名：' + name + ',学号：' + sno + '】'
            this.$confirm(message, '温馨提示', {
              confirmButtonText: '进入下一步修改信息',
              cancelButtonText: '跳过下一步，去缴费',
              type: 'success'
            }).then(() => {
              this.$router.push({ path: '/edit', query: { pici_id:pici_id,education:education}})
            }).catch(() => {
              this.$router.push({ path: '/pay', query: {  }})
            })
            this.formSearchLoading = false
          }).catch(error => {
            console.log(error)
            this.formSearchLoading = false
          })
        } else {
          var errorHtml = []
          for (var k in errors) {
            errorHtml.push(errors[k][0].message)
          }
          this.$alert(errorHtml.join('<br />'), '错误信息', { type: 'error', dangerouslyUseHTMLString: true })
          this.formSearchLoading = false
        }
      })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .label-right-ct{
    margin-left:10px;
    color:#666666;
    font-size:12px;
  }
  .form-label-text{
    color:#dcac6c;
    font-size:12px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 8px !important;
  }
  .span-text{
    font-size:16px;
    color:#666;
    margin-top:10px;
  }
  .typeItem{
    cursor:pointer;
    color:#257be8;
    padding:10px;
    min-width: 100px;
    text-align: center;
    border:1px solid #257be8;
  }
  .selectedItem{
    background:#257be8;
    color:#FFF;
  }
  .blockItem{
    width:auto;
    float: left;
    margin-right:20px;
  }
</style>
