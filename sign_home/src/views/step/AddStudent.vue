<template>
  <div class="app-container container" style="height:100%;">
    <el-scrollbar style="height:100%;overflow-x:hidden;">

      <el-row>
        <!-- <div v-html="topInfoContent">
          {{ topInfoContent }}
        </div>-->
        <el-steps :active="1" simple style="solid:#409EFF;border-radius: 0px;">
          <el-step title="填写基础信息" icon="el-icon-plus"/>
          <el-step title="确认报名信息" icon="el-icon-edit-outline"/>
          <el-step title="在线缴费" icon="el-icon-tickets"/>
          <el-step title="完成报名" icon="el-icon-check"/>
        </el-steps>
        <el-col :span="18" :offset="3" style="overflow: hidden;min-height: 400px;">
          <el-form
            ref="AddStudentInfoForm"
            :show-message="false"
            :inline-message="false"
            :status-icon="true"
            :model="formData"
            :rules="rules"
            label-position="left"
            style="padding:30px 0px;overflow: hidden"
            size="mini">
            <el-form-item :label-width="formDataLists[0].label_width+'px'" label="批次：" style="margin-bottom: 0px;width: 100%;">
              <el-select v-model="piciValue" :style="{width:formDataLists[0].width+'px'}" disabled placeholder="请选择季节批次" required size="small">
                <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
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
                <el-cascader
                  v-model="item.value"
                  :disabled="item.is_disabed==1"
                  :options="unitOptions"
                  :props="unitProps"
                  :placeholder="'请选择'+item.describe"
                  :style="{width:item.width+'px'}"
                  filterable/>
                <label v-if="item.remark!==''&&item.remark!==null" class="label-right-ct"><i class="el-icon-warning"/> {{ item.remark }}</label>
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
                  :name="item.field_name"
                  v-model="item.value"
                  :file-list="item.file"
                  :data="{describe:item.describe,name:item.field_name}"
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
      <div style="padding: 20px;border-top: 1px solid #ddd;text-align: center;">
        <el-button type="primary" style="width:200px" @click.native="handleSearchShow">
          <i class="el-icon-warning" aria-hidden="true"/> 退出
        </el-button>
        <el-button v-loading="formSumbitLoading" type="primary" style="width:200px" @click.native="handleSaveStudent">
          <i class="el-icon-check" aria-hidden="true"/> 提交报名信息
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
    </el-scrollbar>
  </div>
</template>

<script>
import { getRegionLists } from '@/api/region'
import { getSelectUnitLists } from '@/api/selectUnit'
import { addStudentInfo, getFormConfigLists } from '@/api/student'
import { getPiciLists } from '@/api/studentPici'
import { formValidater } from '@/utils/formValidate'
import { getAllMiddleSchoolLists } from '@/api/middleSchool'
import { getArticleByTitle } from '@/api/article'
import { deleteFile } from '@/api/file'
import { Loading } from 'element-ui'
export default {
  data() {
    return {
      piciValue: 1,
      education: '初中生',
      piciLists: [],
      topInfoContent: '',
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
        value: 'unit_code',
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
        console.log(item.field_name+"-"+item.value)
      })

      formData['pici_id']=this.piciValue
     // formData['photo']=this.pictureValue
      return formData
    }
  },
  mounted() {
    this.piciValue = parseInt(this.$route.query.pici)
    this.education = this.$route.query.type
    this.fetchPiciLists()
    this.fetchTopInfoContent()
    this.fetchUnitLists(this.piciValue, this.education)
    this.fetchShengFenLists()
    this.fetchFormFieldLists()
  },
  methods: {
    fetchPiciLists() {
      getPiciLists().then(reponse => {
        this.piciLists = reponse.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    handleSearchShow() {
      this.$router.push({ path: '/' })
    },
    fetchTopInfoContent() {
      getArticleByTitle({ title: '填写基本信息页面顶部提示信息' }).then(response => {
        this.topInfoContent = response.data.content
      }).catch(error => {
        console.log(error)
      })
    },
    fetchFormFieldLists() {
      const loadingInstance = Loading.service({})

      getFormConfigLists({ form_name: '电脑学生报名信息表单',pici_id:this.piciValue }).then(response => {
        this.formDataLists = response.data
        var rules = {}
        this.formDataLists.forEach((item, index) => {
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
                this.fetchRegionLists(item.value)
              } else {
                this.formDataLists[index].value = []
              }
            }
          }

          if (item.data_type === '单位结构') {
            if (typeof value === 'string') {
              if (value.length > 0) {
                this.formDataLists[index].value = JSON.parse(value)
              } else {
                this.formDataLists[index].value = []
              }
            }
          }
          if (item.field_name === 'education') {
            this.formDataLists[index].value = this.$route.query.type
          }
        })
        this.rules = rules
        this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
          loadingInstance.close()
        })
      })
    },
    fetchUnitLists(pici_id, education) {
      getSelectUnitLists({ pici_id:pici_id,education:education }).then(response => {
        var UnitLists = JSON.parse(JSON.stringify(response.data))
        this.unitOptions = UnitLists
      })
    },
    setMiddleSchool(region) {
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
      console.log(data)
      var name=""
      if (data.response===undefined){
        name=data.field_name
      }else{
        name=data.response.data.name
      }
      this.formDataLists.forEach((item,index) => {
        if (item.field_name===name){
          this.formDataLists[index].file=[]
          this.formData[item.field_name] =""
        }
      })
    },
    handlePreview(file) {
      var data = JSON.parse(JSON.stringify(file))
      this.ShowImage.title = data.name
      this.ShowImage.url = data.url
      this.dialogShowImageVisible = true
    },
    studentDataFormat(){
      this.formDataLists.forEach((item) => {
        console.log(item.field_name + '——' + item.value);
        this.formData[item.field_name] = item.value
      })
    },
    uploadImageSuccess(response, file, fileList) {
      console.log(response)
      fileList[0].url =process.env.BASE_API+ response.data.url
      this.formDataLists.forEach((item, index) => {
        if (item.field_name === response.data.name) {
          //this.pictureValue=response.data.url
          this.formDataLists[index].value = response.data.url
          this.formDataLists[index].file = fileList
          this.studentDataFormat()
        }
      })
    },
    handleSaveStudent() {
      this.$refs.AddStudentInfoForm.validate((valid, errors) => {
        if (valid) {
          var formdata = JSON.parse(JSON.stringify(this.formData))
          console.log(formdata)
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true

          for (var k in formdata) {
            var value = formdata[k]
            if (typeof value === 'object') {
              if (value !== null && value.length > 0) { formdata[k] = value[value.length - 1] }
              if (value !== null && value.length === 0) { formdata[k] = null }
            }
          }
          var _this = this
          addStudentInfo(formdata).then(response => {
            this.formSumbitLoading = false
            this.$alert('提交学生信息成功', '提示信息', { type: 'success',
              callback: action => {
                window.localStorage.setItem('student_id', response.data.id);
                _this.$router.push({ path: '/edit', query: { pici_id:response.data.pici_id,education:response.data.education}})
              }
            })
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
  .label-right-ct{
    margin-left:10px;
    color: #d1b21a;
    font-size:12px;
  }
  .form-label-text{
    color:#dcac6c;
    font-size:12px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 8px !important;
  }
</style>
