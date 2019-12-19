<template>
  <div class="app-container" style="height: 100%">
    <object id="AddStudentPlugin" type="application/cert-reader" width="0" height="0" style="display: none"/>
    <el-row>
      <div style="background-color:#F8F8F9;padding: 10px;border-bottom: 1px solid #ddd;">
        <el-button size="small" type="warning" @click="ReadIDCardInfo"><i class="fa fa-search" aria-hidden="true"/> 读取身份证</el-button>
        <el-button size="small" style="float:right" type="warning" @click="handleFormFieldSet"><i class="el-icon-setting" aria-hidden="true"/> 表单设置</el-button>
      </div>
      <el-form
        ref="studentInfoForm"
        :inline-message="false"
        :show-message="false"
        :status-icon="true"
        :model="formData"
        :rules="rules"
        style="background-color:#F8F8F9;padding:10px;overflow: hidden"
        size="small">
        <el-col :span="12">
          <el-form-item label-width="150px" >
            <label slot="label">批次:</label>
            <el-select v-model="piciValue" placeholder="请选择季节批次" size="small" style="width:400px" @change="changePici">
              <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
          </el-form-item>
        </el-col>
        <template v-for="(item) in formDataLists" v-if="item.is_show==1">
          <el-col :span="12" style="overflow: hidden">
            <el-form-item
              v-if="item.data_type=='字符串'"
              :label-width="item.label_width+'px'"
              :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input v-model="formData[item.field_name]" :placeholder="'请填写'+item.describe" :style="{width:item.width+'px'}"/>
            </el-form-item>
            <el-form-item
              v-if="item.data_type=='文本'"
              :label-width="item.label_width+'px'"
              :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input v-model="formData[item.field_name]" :style="{width:item.width+'px'}" :placeholder="'请填写'+item.describe" type="textarea"/>
            </el-form-item>

            <el-form-item v-if="item.data_type=='省市区'" :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader
                v-if="item.event_name&&item.event_name.length>0"
                v-model="formData[item.field_name]"
                :options="regionOptions"
                :props="regionProps"
                :style="{width:item.width+'px'}"
                :placeholder="'请选择'+item.describe"
                @active-item-change="handleItemChange"
                @change="item.event_name"/>
              <el-cascader
                v-else-if="item.event_name.length==0"
                v-model="formData[item.field_name]"
                :options="regionOptions"
                :props="regionProps"
                :style="{width:item.width+'px'}"
                :placeholder="'请选择'+item.describe"
                @active-item-change="handleItemChange"/>
            </el-form-item>

            <el-form-item v-if="item.data_type=='标准数据'" :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-select v-model="formData[item.field_name]" :style="{width:item.width+'px'}" :placeholder="'请选择'+item.describe">
                <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value"/>
              </el-select>
            </el-form-item>

            <el-form-item v-if="item.data_type=='单位结构'" :label-width="item.label_width+'px'" :prop="item.field_name" >
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader
              :show-all-levels="false"
                v-model="formData[item.field_name]"
                :options="unitOptions"
                :props="unitProps"
                :placeholder="'请选择'+item.describe"
                :style="{width:item.width+'px'}"
                filterable/>
            </el-form-item>

            <el-form-item v-if="item.data_type=='毕业中学'" :label-width="item.label_width+'px'" :prop="item.field_name" >
              <label slot="label">{{ item.describe }}:</label>
              <el-select
                v-model="formData[item.field_name]"
                :placeholder="'请选择'+item.describe"
                :style="{width:item.width+'px'}"
                filterable
                allow-create>
                <el-option v-for="middle in middleSchoolLists" :key="middle.schoolName" :label="middle.schoolName" :value="middle.schoolName"/>
              </el-select>
            </el-form-item>

            <el-form-item v-if="item.data_type=='图片'" :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-upload
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :name="item.field_name"
                v-model="formData[item.field_name]"
                :file-list="item.file"
                :data="{describe:item.describe,name:item.field_name}"
                :action="uploadImageUrl"
                :limit="item.limit"
                :multiple="item.multiple"
                :on-success="uploadImageSuccess"
                :style="{width:item.width+'px'}"
                list-type="picture">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">{{ item.remark }}</div>
              </el-upload>
            </el-form-item>
          </el-col>
        </template>
      </el-form>
      <div style="background-color:#F8F8F9;padding: 10px;border-top: 1px solid #ddd;text-align: center;">
        <el-button size="small" style="width: 200px;" type="info" @click="handleCancelStudent">
          <i class="fa fa-reply"/> 返回
        </el-button>
        <el-button v-loading="formSumbitLoading" size="small" type="primary" style="width:200px" @click.native="handleSaveStudent">
          <i class="el-icon-plus" aria-hidden="true"/> 保 存
        </el-button>
      </div>
    </el-row>
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
</template>

<script>
import { getRegionLists } from '@/api/region'
import { getSelectUnitLists } from '@/api/selectUnit'
import { addStudentInfo, getAddFormConfigLists } from '@/api/student'
import { formValidater } from '@/utils/formValidate'
import { getAllMiddleSchoolLists, getMiddleSchoolLists } from '@/api/middleSchool'
import { ReadIDCard } from '@/utils/student'
import { getPiciLists } from '@/api/pici'
import { deleteFile } from '@/api/file'
import { Loading } from 'element-ui'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      piciValue: '',
      piciLists: [],
      formSumbitLoading: false,
      uploadImageUrl: process.env.BASE_API + '/admin/file/uploadImage',
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
      dialogShowImageVisible: false,
      formData: null
    }
  },
  computed: {
    // formData: function() {
    //   var formData = {}
    //   this.formDataLists.forEach((item) => {
    //      // console.log(item.field_name + '——' + item.value);
    //     formData[item.field_name] = item.value
    //   })
    //   // console.log(formData);
    //   return formData
    // }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.piciValue = this.$store.getters.currentPici
    this.piciLists= []
    this.middleSchoolLists= [],
    this.rules= { }
    this.regionOptions= []
    this.unitOptions= []
    this.ShowImage={
      title: '',
      url: ''
    }
    this.formDataLists = []
    this.formData = null
    this.fetchUnitLists()
    this.fetchPiciData()
    this.fetchShengFenLists()
    this.fetchFormFieldLists()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    this.piciValue = this.$store.getters.currentPici
    this.fetchUnitLists()
    this.fetchPiciData()
    this.fetchShengFenLists()
    this.fetchFormFieldLists()
  },
  methods: {
    changePici(value) {
      // console.log(value);
      this.fetchFormFieldLists()
      this.piciValue = value
      this.fetchUnitLists()
    },
    fetchFormFieldLists() {
      const loadingInstance = Loading.service({ target: this.$refs.studentInfoForm.$el })
      getAddFormConfigLists({ form_name: '电脑后台报名信息表单', pici_id: this.piciValue }).then(response => {
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
        })
        this.rules = rules
        console.log(rules)
        this.$nextTick(() => {
          loadingInstance.close()
        })

        let formdata = {}
          this.formDataLists.forEach((item) => {
             // console.log(item.field_name + '——' + item.value);
            formdata[item.field_name] = item.value
          })
        this.formData = JSON.parse(JSON.stringify(formdata))
          console.log(this.formData);
      })
    },
    fetchPiciData() {
      getPiciLists().then(response => {
        this.piciLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchUnitLists() {
      getSelectUnitLists({ pici_id: this.piciValue, addForm: true, layer: 'dept', operator_open: 1 }).then(response => {
        var UnitLists = JSON.parse(JSON.stringify(response.data))
        this.unitOptions = UnitLists.schoolStructure
      })
    },
    setMiddleSchool(region) {
      var region_code = region[region.length - 1]
      getMiddleSchoolLists({ regionCode: region_code }).then(response => {
        this.middleSchoolLists = response.data.data
      }).catch(() => {})
    },

    fetchShengFenLists() {
      getRegionLists({ fatherCode: 100000 }).then(response => {
        var ShengFenLists = JSON.parse(JSON.stringify(response.data))
        ShengFenLists.forEach((item, index) => {
          ShengFenLists[index]['children'] = []
        })
        this.regionOptions = ShengFenLists
      }).catch(error => {
        console.log(error)
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
        }).catch(error => {
          console.log(error)
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
          }).catch(error => {
            console.log(error)
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
          }).catch(error => {
            console.log(error)
          })
        }
      }
    },

    handleRemove(file, fileList) {
      this.formData
     var data = JSON.parse(JSON.stringify(file))
      var url = data.url
      var name=""
      console.log(JSON.stringify(file))
      if (data.response===undefined){
        name=data.field_name
      }else{
        name=data.response.data.name
      }
      this.formData[name]=""
     /* console.log("name="+name)
      this.formDataLists.forEach((item,index) => {
        if (item.field_name===name){
          console.log("111="+name)
          this.formDataLists[index].file=[]
          this.formData[item.field_name] =""
          console.log(this.formData[item.field_name])
          this.studentDataFormat()
        }
      })*/
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
      this.formData[response.data.name] =  response.data.url

      fileList[0].url = process.env.BASE_API + response.data.url
      // this.formDataLists.forEach((item, index) => {
      //   if (item.field_name === response.data.name) {
      //     this.formDataLists[index].value =  response.data.url
      //     this.formDataLists[index].file = fileList
      //     // this.studentDataFormat()
      //   }
      // })

    },
    handleCancelStudent() {
      this.$router.push({ path: '/student/studentLists' })
    },
    handleSaveStudent() {
      this.$refs.studentInfoForm.validate((valid, errors) => {
        if (valid) {
          // console.log(this.formDataLists);
          var formdata = JSON.parse(JSON.stringify(this.formData))
          console.log(JSON.stringify(this.formData))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true

          for (var k in formdata) {
            var value = formdata[k]
            if (typeof value === 'object') {
              if (value !== null && value.length > 0) { formdata[k] = value[value.length - 1] }
              if (value !== null && value.length === 0) { formdata[k] = null }
            }
          }
          formdata['pici_id'] = this.piciValue
          formdata['add_mode'] = '管理端'
          addStudentInfo(formdata).then(response => {
            this.formSumbitLoading = false
            this.$confirm('添加学生成功', '提示', {
              confirmButtonText: '跳转到编辑页面',
              cancelButtonText: '继续添加信息',
              type: 'warning'
            }).then(() => {
              this.$router.push({ path: '/student/editStudent', query: { student_id: response.data.id }})
            }).catch(() => {
              this.fetchFormFieldLists()
            })
            this.formSumbitLoading = false
          }).catch(error => {
            this.formSumbitLoading = false
            console.log(error)
          })
        } else {
          var errorHtml = []
          for (const key in errors) {
            errorHtml.push(errors[key][0].message)
          }
          this.$alert(errorHtml.join('<br />'), '错误信息', { type: 'error', dangerouslyUseHTMLString: true })
          this.formSumbitLoading = false
        }
      })
    },
    handleFormFieldSet() {
      this.$router.replace({ path: '/setting/setStudentForm', query: { formName: '电脑后台报名信息表单' }})
    },
    ReadIDCardInfo() {
      try {
        var _this = this
        var result = ReadIDCard('AddStudentPlugin')
        if (result.resultFlag === 0) {
          const info = result.resultContent
          this.formDataLists.forEach((item, index) => {
            _this.$alert('读取成功!<br />姓名：' + info.partyName + '<br />身份证号码：' + info.certNumber, '成功提示', { type: 'success', dangerouslyUseHTMLString: true })
            if (item.field_name === 'name') {
              _this.formDataLists[index]['value'] = info.partyName
            }
            if (item.field_name === 'idcode') {
              _this.formDataLists[index]['value'] = info.certNumber
            }
            if (item.field_name === 'sex') {
              _this.formDataLists[index]['value'] = info.gender === 1 ? '1' : '2'
            }
            if (item.field_name === 'address' || item.field_name === 'tzs_address') {
              _this.formDataLists[index]['value'] = info.certAddress
            }
            if (item.field_name === 'region_code' || item.field_name === 'hkszd' || item.field_name === 'tzs_region') {
              const xzqhm = []
              const idcode = info.certNumber
              const qum = parseInt(idcode.substring(0, 6))
              const shim = parseInt(qum / 100) * 100
              const shengm = parseInt(qum / 10000) * 10000
              xzqhm.push(shengm)
              xzqhm.push(shim)
              xzqhm.push(qum)
              console.log('xzqhm', xzqhm)
              _this.formDataLists[index]['value'] = xzqhm
              console.log(xzqhm)
              _this.fetchRegionLists(xzqhm)
            }
          })
        } else {
          _this.$alert(result.errorMsg, '错误提示', { type: 'error' })
        }
      } catch (e) {
        this.$alert('使用身份证读卡器读取信息，由于插件原因只能使用IE浏览器，本系统只支持IE10以上的浏览器，所以只能使用IE10以上的浏览器', '错误提示', { type: 'error' })
      }
    }
  }
}
</script>

<!--
<style scoped>
  .form-label-text{
    color:#dcac6c;
    font-size:12px;
  }
  .el-form-item&#45;&#45;mini.el-form-item, .el-form-item&#45;&#45;small.el-form-item {
    margin-bottom: 10px !important;
    height:38px !important;
    overflow: hidden !important;
  }
</style>
-->
<style scoped>
  .form-label-text{
    color:#dcac6c;
    font-size:12px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 6px !important;
  }
</style>
