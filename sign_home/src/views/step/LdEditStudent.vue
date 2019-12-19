<template>
  <el-scrollbar style="height:100%;overflow-x:hidden;">
    <div class="app-container container" style="height:100%;">
      <el-row>
<!--        <div v-html="topInfoContent">
          {{ topInfoContent }}
        </div>-->
        <el-steps :active="2" simple style="border-top:3px solid #409EFF;border-radius: 0px;">
          <el-step title="填写报名基础信息" icon="el-icon-plus"/>
          <el-step title="确认修改报名信息" icon="el-icon-edit-outline"/>
          <el-step title="在线缴费" icon="el-icon-tickets"/>
          <el-step title="完成报名" icon="el-icon-check"/>
        </el-steps>
        <el-col :span="22" :offset="1" style="padding:30px 0px;">
          <el-row class="student-info-ct">
            <el-form label-position="left" inline size="small">
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="学号：" class="show-student-info">
                  <span>{{ studentInfo.sno }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="姓名：" class="show-student-info">
                  <span>{{ studentInfo.name }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="性别：" class="show-student-info">
                  <span>{{ studentInfo.sex=="1"?"男":"女"}}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="报读院系：" class="show-student-info">
                  <span>{{ studentInfo.unit_code_yuanxi }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item :label-width="labelWidth" label="报读专业：" style="margin-bottom: 0px">
                  <span>{{ studentInfo.unit_code_zhuanye }}</span>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
          <el-row style="margin-top:20px;padding:15px;border-top:1px solid #ddd;background: #cf9236;color:#fff;text-align: center;font-size:16px">
            申请状态：
            <span v-if="studentInfo.ldybz_check=='未申请'">未提交申请</span>
            <span v-if="studentInfo.ldybz_check=='已提交，未审核'">已提交申请,请等待老师进行审核</span>
            <span v-if="studentInfo.ldybz_check=='审核通过'">审核通过</span>
            <span v-if="studentInfo.ldybz_check=='审核不通过'">审核未通过,原因：{{ studentInfo.ldybz_remark }}</span>
          </el-row>
        </el-col>
        <el-col :span="22" :offset="1" style="overflow: hidden;min-height: 400px;">
          <div style="padding:13px 0px;font-size:14px;font-weight: bolder">
            申请劳动预备制：
          </div>
          <el-form
            ref="EditStudentInfoForm"
            :show-message="false"
            :inline-message="true"
            :status-icon="true"
            :model="formData"
            :rules="rules"
            label-position="left"
            size="mini">
            <template v-for="(item) in formDataLists">
              <el-form-item
                      v-if="item.data_type=='字符串'"
                      :label-width="item.label_width+'px'"
                      :prop="item.field_name">
                <label slot="label">{{ item.describe }}:</label>
                <el-input v-model="item.value" :disabled="item.is_disabed==1" :placeholder="'请填写'+item.describe" :style="{width:item.width+'px'}"/>
                <label v-if="item.remark!==''&&item.remark!==null" class="label-right-ct"><i class="el-icon-warning"/> {{ item.remark }}</label>
              </el-form-item>
              <el-form-item
                      v-if="item.data_type=='文本'"
                      :label-width="item.label_width+'px'"
                      :prop="item.field_name">
                <label slot="label">{{ item.describe }}:</label>
                <el-input v-model="item.value" :disabled="item.is_disabed==1" :style="{width:item.width+'px'}" :placeholder="'请填写'+item.describe" type="textarea"/>
                <label v-if="item.remark!==''&&item.remark!==null" class="label-right-ct"><i class="el-icon-warning"/> {{ item.remark }}</label>
              </el-form-item>
              <el-form-item v-if="item.data_type=='省市区'" :label-width="item.label_width+'px'" :prop="item.field_name">
                <label slot="label">{{ item.describe }}:</label>
                <el-cascader
                        v-if="item.event_name&&item.event_name.length>0"
                        v-model="item.value"
                        :disabled="item.is_disabed==1"
                        :options="regionOptions"
                        :props="regionProps"
                        :style="{width:item.width+'px'}"
                        :placeholder="'请选择'+item.describe"
                        @active-item-change="handleItemChange"
                        @change="item.event_name"/>
                <el-cascader
                        v-else-if="item.event_name.length==0"
                        v-model="item.value"
                        :disabled="item.is_disabed==1"
                        :options="regionOptions"
                        :props="regionProps"
                        :style="{width:item.width+'px'}"
                        :placeholder="'请选择'+item.describe"
                        @active-item-change="handleItemChange"/>
                <label v-if="item.remark!==''&&item.remark!==null" class="label-right-ct"><i class="el-icon-warning"/> {{ item.remark }}</label>
              </el-form-item>

              <el-form-item v-if="item.data_type=='标准数据'" :label-width="item.label_width+'px'" :prop="item.field_name">
                <label slot="label">{{ item.describe }}:</label>
                <el-select v-model="item.value" :disabled="item.is_disabed==1" :style="{width:item.width+'px'}" :placeholder="'请选择'+item.describe">
                  <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value"/>
                </el-select>
                <label v-if="item.remark!==''&&item.remark!==null" class="label-right-ct"><i class="el-icon-warning"/> {{ item.remark }}</label>
              </el-form-item>

              <el-form-item v-if="item.data_type=='单位结构'" :label-width="item.label_width+'px'" :prop="item.field_name">
                <label slot="label">{{ item.describe }}:</label>
                <!--  <el-cascader
                    v-model="item.value"
                    :disabled="item.is_disabed==1"
                    :options="unitOptions"
                    :props="unitProps"
                    :placeholder="'请选择'+item.describe"
                    :style="{width:item.width+'px'}"
                    filterable/>-->
                <el-input disabled v-model="item.value"  :placeholder="'请填写'+item.describe" :style="{width:item.width+'px'}" type="textarea"/>
                <!-- <label v-if="item.remark!==''&&item.remark!==null" class="label-right-ct"><i class="el-icon-warning"/> {{ item.remark }}</label>-->
              </el-form-item>

              <el-form-item v-if="item.data_type=='毕业中学'" :label-width="item.label_width+'px'" :prop="item.field_name">
                <label slot="label">{{ item.describe }}:</label>
                <el-select
                        v-model="item.value"
                        :disabled="item.is_disabed==1"
                        :placeholder="'请选择'+item.describe"
                        :style="{width:item.width+'px'}"
                        filterable
                        allow-create>
                  <el-option v-for="middle in middleSchoolLists" :key="middle.schoolName" :label="middle.schoolName" :value="middle.schoolName"/>
                </el-select>
                <label v-if="item.remark!==''&&item.remark!==null" class="label-right-ct"><i class="el-icon-warning"/> {{ item.remark }}</label>
              </el-form-item>

              <el-form-item v-if="item.data_type=='图片'" :label-width="item.label_width+'px'" :prop="item.field_name">
                <label slot="label">{{ item.describe }}:</label>
                <el-upload
                  :on-preview="handlePreview"
                  :on-remove="handleRemove"
                  :name="fileName"
                  v-model="item.value"
                  :file-list="item.file"
                  :data="{describe:item.describe,name:item.field_name}"
                  :readonly="studentInfo.ldybz_check!=='未申请' && studentInfo.ldybz_check!=='审核通过'"
                  :action="uploadImageUrl"
                  :limit="item.limit"
                  :multiple="item.multiple"
                  :on-success="uploadImageSuccess"
                  :style="{width:item.width+'px'}"
                  list-type="picture">
                  <el-button size="small" type="primary">点击上传</el-button>
                  <div slot="tip" class="el-upload__tip label-right-ct"><i class="el-icon-warning"/> 只能上传jpg/png文件</div>
                </el-upload>
              </el-form-item>
            </template>
          </el-form>
        </el-col>
      </el-row>
      <div style="padding: 20px;border-top: 1px solid #ddd;text-align: center;margin-top:10px;">
        <el-button v-if="studentInfo.ldybz_check=='未申请'" v-loading="formSumbitLoading" type="primary" style="width:200px" @click.native="handleSaveStudent">
          <i class="el-icon-check" aria-hidden="true"/> 提交申请
        </el-button>
        <el-button type="primary" style="width:200px" @click.native="handleFanhui">
          <i class="el-icon-d-arrow-left" aria-hidden="true"/> 返回
        </el-button>
      </div>
      <el-dialog
        :visible.sync="dialogShowImageVisible"
        :title="ShowImage.title"
        style="text-align: center">
        <img :src="ShowImage.url" style="width: 100%">
        <span slot="footer" class="dialog-footer">
          <el-button type="primary" @click="dialogShowImageVisible = false">关闭</el-button>
        </span>
      </el-dialog>
    </div>
  </el-scrollbar>
</template>

<script>
import { getRegionLists } from '@/api/region'
import { getSelectUnitLists } from '@/api/selectUnit'
import { editStudentInfoForLDYBZ, getStudentInfo,getEditFormConfigLists } from '@/api/student'
import { formValidater } from '@/utils/formValidate'
import { getAllMiddleSchoolLists } from '@/api/middleSchool'
import { getArticleByTitle } from '@/api/article'
import { deleteFile } from '@/api/file'
import { Loading } from 'element-ui'
export default {
  data() {
    return {
      fileName:'photo',
      piciValue:'',
      topInfoContent: '',
      isSaveStudentInfo: true,
      labelWidth: '100px',
      studentInfo: {},
      formSumbitLoading: false,
      uploadImageUrl: process.env.BASE_API + '/index/student/uploadImage',
      formDataLists: [],
      middleSchoolLists: [],
      rules: { },
      regionOptions: [],
      regionProps: {
        value: 'code',
        label: 'fullName',
        children: 'children'
      },
      unitOptions: [],
      unitProps: {
        value: 'id',
        label: 'unit_name',
        children: 'children'
      },
      ShowImage: {
        title: '',
        url: ''
      },
      dialogShowImageVisible: false
    }
  },
  computed: {
    formData: function() {
      var formData = {}
      this.formDataLists.forEach((item) => {
        formData[item.field_name] = item.value
        // console.log(item.field_name+"-"+item.value)
      })
      formData['student_id'] =window.localStorage.getItem('student_id')
      return formData
    }
  },
  mounted() {
    var student_id =  window.localStorage.getItem('student_id')
    this.piciValue = this.$route.query.pici_id
    this.fetchTopInfoContent()
    this.fetchUnitLists()
    this.fetchShengFenLists()
    this.fetchFormFieldLists(student_id)
    this.fetchStudentInfo(student_id)
  },
  methods: {
    fetchTopInfoContent() {
      getArticleByTitle({ title: '确认报名信息页面顶部提示信息' }).then(response => {
        this.topInfoContent = response.data.content
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStudentInfo(student_id) {
      getStudentInfo({ student_id: student_id }).then(response => {
        this.studentInfo = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    handleNextPay() {
      var student_id = window.localStorage.getItem('student_id')
      this.$router.push({ path: '/pay', query: { student_id: student_id }})
    },
    fetchFormFieldLists(student_id) {

      this.formSumbitLoading = true
      const loadingInstance = Loading.service()
      console.log(this.piciValue)
      getEditFormConfigLists({ form_name: '申请劳动预备信息表单',pici_id:this.piciValue, student_id: student_id,id: student_id }).then(response => {
        this.formDataLists = response.data
        var rules = {}
        this.formDataLists.forEach((item, index) => {
          console.log(item.field_name+":"+item.value)
          // 判断验证规则
          if (item.rules !== '' && item.rules !== null) {
            var ruleList = JSON.parse(item.rules)
            for (let i = 0; i < ruleList.length; i++) {
              if (ruleList[i].validator !== undefined) {
                if (ruleList[i].validator === 'formValidater.checkdIdCard') { ruleList[i].validator = formValidater.checkdIdCard }
                if (ruleList[i].validator === 'formValidater.phoneNumber') { ruleList[i].validator = formValidater.phoneNumber }
                if (ruleList[i].validator === 'formValidater.telephoneNumber') { ruleList[i].validator = formValidater.telephoneNumber }
              }
            }
            rules[item.field_name] = ruleList
          }
          var value = item.value
          var event_name = item.event_name
          if (event_name === 'setMiddleSchool') {
            this.formDataLists[index].event_name = this.setMiddleSchool
          } else {
            this.formDataLists[index].event_name = ''
          }
          // 判断是否存在事件
          if (item.data_type === '省市区') {
            if (typeof value === 'string') {
              if (value.length > 0) {
                this.formDataLists[index].value = JSON.parse(value)
              } else {
                this.formDataLists[index].value = []
              }
            }
            this.fetchRegionLists(item.value)
          }
          /*if (item.data_type === '单位结构') {
            if (typeof value === 'string') {
              if (value.length > 0) {
                this.formDataLists[index].value =JSON.parse(value)
              } else {
                this.formDataLists[index].value = []
              }
            }
          }*/
          if (item.data_type === '图片') {
            if (this.formDataLists[index].file.length>0){
              this.formDataLists[index].file[0].url=process.env.BASE_API+this.formDataLists[index].file[0].url
            }
          }
        })
        this.rules = rules
        this.formSumbitLoading = false
        this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
          loadingInstance.close()
        })
      }).catch(error => {
        console.log(error)
      })
    },
    fetchUnitLists() {
      getSelectUnitLists().then(response => {
        var UnitLists = JSON.parse(JSON.stringify(response.data))
        this.unitOptions = UnitLists
      })
    },
    setMiddleSchool(region) {
      console.log(region)
      var region_code = region[region.length - 1]
      getAllMiddleSchoolLists({ regionCode: region_code }).then(response => {
        this.middleSchoolLists = response.data
      }).catch(() => {})
    },
    fetchShengFenLists() {
      getRegionLists({ fatherCode: 100000 }).then(response => {
        var ShengFenLists = JSON.parse(JSON.stringify(response.data))
        ShengFenLists.forEach((item, index) => {
          ShengFenLists[index]['children'] = []
        })
        this.regionOptions = ShengFenLists
      })
    },
    fetchRegionLists(value) {
      var shengfen_code = (value !== null) ? value[0] : null
      var chengshi_code = (value !== null) ? value[1] : null
      var ShengFenIndex = 0
      var ChengShiIndex = 0
      // 获取省份信息
      this.regionOptions.forEach((item, index) => {
        if (this.regionOptions[index].code === shengfen_code) {
          ShengFenIndex = index
        }
      })
      // 获取城市信息
      if (value !== null && value.length > 0) {
        getRegionLists({ fatherCode: shengfen_code }).then(response => {
          var ChengShiLists = JSON.parse(JSON.stringify(response.data))
          ChengShiLists.forEach((item, index) => {
            if (ChengShiLists[index]['children'] === undefined) {
              ChengShiLists[index]['children'] = []
            }
            if (ChengShiLists[index].code === chengshi_code) {
              ChengShiIndex = index
            }
          })
          if (this.regionOptions[ShengFenIndex]['children'].length === 0) { this.regionOptions[ShengFenIndex]['children'] = ChengShiLists }
          // 获取地区信息
          if (this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'].length === 0) {
            getRegionLists({ fatherCode: chengshi_code }).then(response => {
              var DiquLists = JSON.parse(JSON.stringify(response.data))
              this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'] = DiquLists
            })
          }
        })
      }
    },
    handleItemChange(val) {
      var length = val.length
      var shengfen_code = val[0]
      var ShengFenIndex = 0
      var ChengShiIndex = 0
      var regionOptions = JSON.parse(JSON.stringify(this.regionOptions))
      // 点击省份
      if (length === 1) {
        for (let i = 0; i < regionOptions.length; i++) {
          if (regionOptions[i].code === shengfen_code) {
            ShengFenIndex = i
            break
          }
        }
        if (this.regionOptions[ShengFenIndex]['children'].length === 0) {
          getRegionLists({ fatherCode: shengfen_code }).then(response => {
            var regionLists = JSON.parse(JSON.stringify(response.data))
            for (let i = 0; i < regionLists.length; i++) {
              if (regionLists[i].type !== 3) {
                regionLists[i]['children'] = []
              }
            }
            this.regionOptions[ShengFenIndex]['children'] = regionLists
          })
        }
      }

      if (length === 2) {
        const chengshi_code = val[1]
        for (let i = 0; i < regionOptions.length; i++) {
          if (regionOptions[i].code === shengfen_code) {
            ShengFenIndex = i
            break
          }
        }
        for (let i = 0; i < regionOptions[ShengFenIndex]['children'].length; i++) {
          if (regionOptions[ShengFenIndex]['children'][i].code === chengshi_code) {
            ChengShiIndex = i
            break
          }
        }
        if (this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'].length === 0) {
          getRegionLists({ fatherCode: chengshi_code }).then(response => {
            var regionLists = JSON.parse(JSON.stringify(response.data))
            for (let i = 0; i < regionLists.length; i++) {
              if (regionLists[i].type !== 3) {
                regionLists[i]['children'] = []
              }
            }
            this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'] = regionLists
          })
        }
      }
    },
    handleRemove(file, fileList) {
      var data = JSON.parse(JSON.stringify(file))
      // console.log(JSON.stringify(file))

    },
    handlePreview(file) {
      var data = JSON.parse(JSON.stringify(file))
      this.ShowImage.title = data.name
      this.ShowImage.url = data.url
      this.dialogShowImageVisible = true
    },
    studentDataFormat(){
      this.formDataLists.forEach((item) => {
        // console.log(item.field_name + '——' + item.value);
        this.formData[item.field_name] = item.value
      })
    },
    uploadImageSuccess(response, file, fileList) {
      // console.log(response)
      fileList[0].url = process.env.BASE_API+ response.data.url
      this.formDataLists.forEach((item, index) => {
        if (item.field_name === response.data.name) {
          this.formDataLists[index].value = response.data.url
          this.formDataLists[index].file = fileList
          this.studentDataFormat()
        }
      })
    },
    handleFanhui() {
      var _this = this
      var student_id = window.localStorage.getItem('student_id')
      var pici_id = this.$route.query.pici_id
      var education=this.$route.query.education
      _this.$router.push({ path: '/edit', query: { student_id: student_id ,pici_id:pici_id,education:education}})
    },
    handleSaveStudent() {
      this.$refs.EditStudentInfoForm.validate((valid, errors) => {
        if (valid) {
          var formdata = JSON.parse(JSON.stringify(this.formData))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true

          for (var k in formdata) {
            var value = formdata[k]
            if (typeof value === 'object') {
              if (value !== null && value.length > 0) { formdata[k] = value[value.length - 1] }
              if (value !== null && value.length === 0) { formdata[k] = null }
            }
          }

          formdata['ldybz_shenqing'] = '是'
          formdata['ldybz_check'] = '已提交，未审核'
          for (var o in formdata) {
            if (formdata[o] === '' || formdata[o] === null|| formdata[o] === undefined) {
              this.$alert('请先完善所有信息再提交', '错误信息', { type: 'error' })
              this.formSumbitLoading = false
              return false
            }
          }

          editStudentInfoForLDYBZ(formdata).then(response => {
            this.$alert('申请劳动预备制成功', '提示', { type: 'success' })
            this.formSumbitLoading = false
            this.isSaveStudentInfo = false
          }).catch(error => {
            console.log(error)
            this.formSumbitLoading = false
          })
        } else {
          var errorHtml = []
          for (var k in errors) {
            errorHtml.push(errors[k][0].message)
          }
          this.$alert(errorHtml.join('<br />'), '错误信息', { type: 'error', dangerouslyUseHTMLString: true })
          this.formSumbitLoading = false
        }
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .sign-success{
    padding:15px;
    color:#FFF;
    background-color: #f56c6c;
    font-size:22px;
  }
  .show-student-info{
    margin-bottom: 5px !important;
  }
  .form-label-text{
    color:#dcac6c;
    font-size:12px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 8px !important;
  }
  .student-info-ct{
    background-color:#ececec;
    border:1px solid #409EFF;
    border-radius: 3px;
    overflow: hidden;
    padding:5px;
  }
  .label-right-ct{
    margin-left:10px;
    color:#666666;
    font-size:12px;
  }
</style>
