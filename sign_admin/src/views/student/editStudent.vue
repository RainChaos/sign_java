<template>
<div class="app-container" style="height: 100%">
  <object id="EditStudentPlugin" type="application/cert-reader" width="0" height="0" style="display: none" />
  <el-row>
    <div style="background-color:#F8F8F9;padding: 10px;border-bottom: 1px solid #ddd;">
      <el-button size="small" type="warning" @click="ReadIDCardInfo"><i class="fa fa-search" aria-hidden="true" /> 读取身份证</el-button>
      <el-button size="small" style="float:right" type="warning" @click="handleFormFieldSet"><i class="el-icon-setting" aria-hidden="true" /> 表单设置</el-button>
    </div>
    <el-form ref="studentInfoForm" :model="formData" :rules="rules" style="background-color:#F8F8F9;padding:10px;overflow: hidden" size="small">
      <el-col :span="12">
        <el-form-item label-width="150px" style="margin-bottom: 0px;width: 100%;">
          <label slot="label">批次:</label>
          <el-select v-model="piciValue" placeholder="请选择类型" size="small" style="width:400px" disabled>
            <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id" />
          </el-select>
        </el-form-item>
      </el-col>
      <template v-for="(item) in formDataLists" v-if="item.is_show==1||item.is_show==true">
        <el-col v-if="item.data_type=='单位结构'" :span="12">
          <el-form-item :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <!--<el-cascader v-model="item.value" :disabled="item.is_disabed==1" :options="unitOptions" :props="unitProps" :placeholder="'请选择'+item.describe" :style="{width:item.width+'px'}" />-->
             <el-input disabled v-model="formData[item.field_name]"  :placeholder="'请填写'+item.describe" :style="{width:item.width+'px'}" type="textarea"/>
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item v-if="item.data_type=='数字'" :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <el-input-number v-model="formData[item.field_name]" :disabled="item.is_disabed==1" :style="{width:item.width+'px'}" />
          </el-form-item>

          <el-form-item v-if="item.data_type=='字符串'" :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <el-input v-model="formData[item.field_name]" :disabled="item.is_disabed==1" :placeholder="'请填写'+item.describe" :style="{width:item.width+'px'}" />
          </el-form-item>

          <el-form-item v-if="item.data_type=='文本'" :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <el-input v-model="formData[item.field_name]" :style="{width:item.width+'px'}" :placeholder="'请填写'+item.describe" type="textarea" />
          </el-form-item>

          <el-form-item v-if="item.data_type=='省市区'" :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <el-cascader v-if="item.event_name!=null&&item.event_name.length>0" v-model="formData[item.field_name]" :options="regionOptions" :props="regionProps" :style="{width:item.width+'px'}" :placeholder="'请选择'+item.describe"
              @active-item-change="handleItemChange" @change="item.event_name" />
            <el-cascader v-else-if="item.event_name==null||item.event_name.length==0" v-model="formData[item.field_name]" :options="regionOptions" :props="regionProps" :style="{width:item.width+'px'}" :placeholder="'请选择'+item.describe"
              @active-item-change="handleItemChange" />
          </el-form-item>

          <el-form-item v-if="item.data_type=='标准数据'" :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <el-select v-model="formData[item.field_name]" :style="{width:item.width+'px'}" :placeholder="'请选择'+item.describe">
              <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value" />
            </el-select>
          </el-form-item>

          <el-form-item v-if="item.data_type=='毕业中学'" :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <el-select v-model="formData[item.field_name]" :placeholder="'请选择'+item.describe" :style="{width:item.width+'px'}" filterable allow-create>
              <el-option v-for="middle in middleSchoolLists" :key="middle.schoolName" :label="middle.schoolName" :value="middle.schoolName" />
            </el-select>
          </el-form-item>

          <el-form-item v-if="item.data_type=='图片'" :label-width="item.label_width+'px'" :prop="item.field_name">
            <label slot="label">{{ item.describe }}:</label>
            <el-upload :on-preview="handlePreview" :on-remove="handleRemove" :name="item.field_name" v-model="formData[item.field_name]" :file-list="item.file" :data="{describe:item.describe,name:item.field_name}" :action="uploadImageUrl" :limit="item.limit"
              :multiple="item.multiple" :on-success="uploadImageSuccess" :style="{width:item.width+'px'}" list-type="picture">
              <el-button size="small" type="primary">点击上传</el-button>
              <div slot="tip" class="el-upload__tip">{{ item.remark }}</div>
            </el-upload>
          </el-form-item>

        </el-col>
      </template>
    </el-form>
    <div style="background-color:#F8F8F9;padding: 10px;border-top: 1px solid #ddd;text-align: center;">
      <el-button size="small" style="width: 200px;" type="info" @click="$router.go(-1)">
        <i class="fa fa-reply" /> 返回
      </el-button>
      <el-button size="small" type="primary" style="width:200px" @click.native="handleAddStudent">
        <i class="el-icon-plus" aria-hidden="true" /> 新增学生
      </el-button>
      <el-button v-loading="formSumbitLoading" size="small" type="primary" style="width:200px" @click.native="handleSaveStudent">
        <i class="el-icon-check" aria-hidden="true" /> 修 改
      </el-button>
    </div>
  </el-row>
  <el-dialog :visible.sync="dialogShowImageVisible" :title="ShowImage.title" style="text-align: center">
    <img :src="ShowImage.url" style="width: 100%">
    <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="dialogShowImageVisible = false">关闭</el-button>
    </span>
  </el-dialog>
</div>
</template>

<script>
import {
  getRegionLists,
  getRegion
} from '@/api/region'
import {
  getAllLists
} from '@/api/unit'
import {
  getSelectUnitLists
} from '@/api/selectUnit'
import {
  getEditFormConfigLists,
  getStudentInfo
} from '@/api/student'
import {
  getAllMiddleSchoolLists,
  getMiddleSchoolLists
} from '@/api/middleSchool'
import {
  editStudentInfo
} from '@/api/student'
import {
  formValidater
} from '@/utils/formValidate'
import {
  ReadIDCard
} from '@/utils/student'
import {
  vilidateBtn
} from '@/utils/auth'
import {
  getPiciLists
} from '@/api/pici'
import {
  deleteFile
} from '@/api/file'
import {
  Loading
} from 'element-ui'
import axios from 'axios'
import {
  setCurrentUrl,
  getCurrentUrl
} from '@/utils/auth.js'
export default {
  data() {
    return {
      piciValue: 0,
      piciLists: [],
      studentInfo: {},
      formSumbitLoading: false,
      uploadImageUrl: process.env.BASE_API + '/admin/file/uploadImage',
      formDataLists: [],
      rules: {},
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
      middleSchoolLists: [],
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
    //     formData[item.field_name] = item.value
    //   })
    //   return formData
    // }
  },
  activated(){
    setCurrentUrl('/student/studentLists')
    var student_id = this.$route.query.student_id
    this.formDataLists = []
    this.rules = {}
    this.piciLists = []
    this.regionOptions = []
    this.unitOptions = []
    this.middleSchoolLists = []
    this.ShowImage = {
      title: '',
      url: ''
    }
    this.formData = null

    this.fetchStudentInfo(student_id)
    this.fetchPiciData()
    this.fetchShengFenLists()
  },
  mounted() {
    setCurrentUrl('/student/studentLists')
    let data = JSON.parse(localStorage.getItem("info"));
    var student_id = this.$route.query.student_id
    this.fetchStudentInfo(student_id)
    this.fetchPiciData()
    this.fetchShengFenLists()

  },
  methods: {
    fetchFormFieldLists(student_id) {
      setCurrentUrl('/student/studentLists')
      getEditFormConfigLists({
        form_name: '电脑后台修改信息表单',
        student_id: student_id,
        pici_id: this.piciValue
      }).then(response => {
        this.formDataLists = response.data
        var rules = {}
        this.formDataLists.forEach((item, index) => {

          if (item.field_name === 'zsr' && vilidateBtn('允许修改招生老师')) {
            item.is_disabed = 0
          }

          if (item.field_name === 'zsr' && !vilidateBtn('允许修改招生老师')) {
            item.is_disabed = 1
          }

          if (item.rules !== '' && item.rules !== null) {
            var ruleList = JSON.parse(item.rules)
            for (let i = 0; i < ruleList.length; i++) {
              if (ruleList[i].validator !== undefined) {
                if (ruleList[i].validator === 'formValidater.checkdIdCard') {
                  ruleList[i].validator = formValidater.checkdIdCard
                }
                if (ruleList[i].validator === 'formValidater.phoneNumber') {
                  ruleList[i].validator = formValidater.phoneNumber
                }
                if (ruleList[i].validator === 'formValidater.telephoneNumber') {
                  ruleList[i].validator = formValidater.telephoneNumber
                }
              }
            }
            rules[item.field_name] = ruleList
          }
          var value = item.value

          var event_name = item.event_name
          if (event_name === 'setMiddleSchool') {
            this.formDataLists[index].event_name = this.setMiddleSchool
            this.setMiddleSchool(item.value)
            // console.log(item.value[2]);
          } else {
            this.formDataLists[index].event_name = ''
          }
          if (item.data_type === '省市区') {
            console.log(item);
            // if (typeof value === 'string') {
            //   if (value.length > 0) {
            //     this.formDataLists[index].value = JSON.parse(value)
            //   } else {
            //     this.formDataLists[index].value = []
            //   }
            // }
            // this.fetchRegionLists(item.value)
          }
          if (item.data_type === '图片') {
            if (this.formDataLists[index].file.length>0){
              this.formDataLists[index].file[0].url=process.env.BASE_API+this.formDataLists[index].file[0].url
            }
          }
          // if (item.data_type === '单位结构') {
          //   if (typeof value === 'string') {
          //     if (value.length > 0) {
          //       this.formDataLists[index].value = JSON.parse(value)
          //     } else {
          //       this.formDataLists[index].value = []
          //     }
          //   }
          // }
        })
        // console.log(this.formDataLists);

        this.rules = rules

        let formdata = {}
        this.formDataLists.forEach((item) => {
          // console.log(item.field_name + '——' + item.value);
          formdata[item.field_name] = item.value
        })
        this.formData = JSON.parse(JSON.stringify(formdata))
      })
    },
    fetchPiciData() {
      setCurrentUrl('/student/studentLists')
      getPiciLists().then(response => {
        this.piciLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStudentInfo(student_id) {
      setCurrentUrl('/student/studentLists')
      getStudentInfo({
        student_id: student_id
      }).then(response => {
        this.studentInfo = response.data
        this.piciValue = response.data.pici_id
        var student_id = this.$route.query.student_id
        this.fetchFormFieldLists(student_id)
        this.fetchUnitLists()
      }).catch(error => {
        console.log(error)
      })
    },
    fetchUnitLists() {
      setCurrentUrl('/student/studentLists')
      const loadingInstance = Loading.service({
        target: this.$refs.studentInfoForm.$el
      })
      getSelectUnitLists({ pici_id: this.piciValue, addForm: true, layer: 'dept', operator_open: 1 }).then(response => {
        var UnitLists = JSON.parse(JSON.stringify(response.data))
        this.unitOptions = UnitLists.schoolStructure
        this.$nextTick(() => {
          loadingInstance.close()
        })
      }).catch(error => {
        console.log(error)
      })
    },
    setMiddleSchool(region) {
      setCurrentUrl('/student/studentLists')
      getMiddleSchoolLists({
        regionCode: region[2]
      }).then(response => {
        this.middleSchoolLists = response.data.data
        // console.log(this.middleSchoolLists);
      }).catch(() => {})
    },

    fetchShengFenLists() {
      setCurrentUrl('/student/studentLists')
      getRegion().then(response => {
        var ShengFenLists = JSON.parse(JSON.stringify(response.data))
        // ShengFenLists.forEach((item, index) => {
        //   ShengFenLists[index]['children'] = []
        // })
        this.regionOptions = ShengFenLists
      }).catch(error => {
        console.log(error)
      })
    },
    // fetchRegionLists(value) {
    //   var shengfen_code = (value !== null) ? value[0] : null
    //   var chengshi_code = (value !== null) ? value[1] : null
    //   var ShengFenIndex = 0
    //   var ChengShiIndex = 0
    //   // 获取省份信息
    //   this.regionOptions.forEach((item, index) => {
    //     if (this.regionOptions[index].code === shengfen_code) {
    //       ShengFenIndex = index
    //     }
    //   })
    //   // 获取城市信息
    //   if (value !== null && value.length > 0) {
    //     getRegionLists({ fatherCode: shengfen_code }).then(response => {
    //       var ChengShiLists = JSON.parse(JSON.stringify(response.data))
    //       ChengShiLists.forEach((item, index) => {
    //         if (ChengShiLists[index]['children'] === undefined) {
    //           ChengShiLists[index]['children'] = []
    //         }
    //         if (ChengShiLists[index].code === chengshi_code) {
    //           ChengShiIndex = index
    //         }
    //       })
    //       if (this.regionOptions[ShengFenIndex]['children'].length === 0) { this.regionOptions[ShengFenIndex]['children'] = ChengShiLists }
    //       // 获取地区信息
    //       if (this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'].length === 0) {
    //         getRegionLists({ fatherCode: chengshi_code }).then(response => {
    //           var DiquLists = JSON.parse(JSON.stringify(response.data))
    //           this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'] = DiquLists
    //         })
    //       }
    //     }).catch(error => {
    //       console.log(error)
    //     })
    //   }
    // },

    handleItemChange(val) {
      //   var length = val.length
      //   var shengfen_code = val[0]
      //   var ShengFenIndex = 0
      //   var ChengShiIndex = 0
      //   var regionOptions = JSON.parse(JSON.stringify(this.regionOptions))
      //   // 点击省份
      //   if (length === 1) {
      //     for (let i = 0; i < regionOptions.length; i++) {
      //       if (regionOptions[i].code === shengfen_code) {
      //         ShengFenIndex = i
      //         break
      //       }
      //     }
      //     if (this.regionOptions[ShengFenIndex]['children'].length === 0) {
      //       getRegionLists({ fatherCode: shengfen_code }).then(response => {
      //         var regionLists = JSON.parse(JSON.stringify(response.data))
      //         for (let i = 0; i < regionLists.length; i++) {
      //           if (regionLists[i].type !== 3) {
      //             regionLists[i]['children'] = []
      //           }
      //         }
      //         this.regionOptions[ShengFenIndex]['children'] = regionLists
      //       }).catch(error => {
      //         console.log(error)
      //       })
      //     }
      //   }
      //
      //   if (length === 2) {
      //     const chengshi_code = val[1]
      //     for (let i = 0; i < regionOptions.length; i++) {
      //       if (regionOptions[i].code === shengfen_code) {
      //         ShengFenIndex = i
      //         break
      //       }
      //     }
      //     for (let i = 0; i < regionOptions[ShengFenIndex]['children'].length; i++) {
      //       if (regionOptions[ShengFenIndex]['children'][i].code === chengshi_code) {
      //         ChengShiIndex = i
      //         break
      //       }
      //     }
      //     if (this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'].length === 0) {
      //       getRegionLists({ fatherCode: chengshi_code }).then(response => {
      //         var regionLists = JSON.parse(JSON.stringify(response.data))
      //         for (let i = 0; i < regionLists.length; i++) {
      //           if (regionLists[i].type !== 3) {
      //             regionLists[i]['children'] = []
      //           }
      //         }
      //         this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'] = regionLists
      //       }).catch(error => {
      //         console.log(error)
      //       })
      //     }
      //   }
    },

    handleRemove(file, fileList) {
      setCurrentUrl('/student/studentLists')
      var data = JSON.parse(JSON.stringify(file))
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
      setCurrentUrl('/student/studentLists')
      var data = JSON.parse(JSON.stringify(file))
      this.ShowImage.title = data.name
      this.ShowImage.url = data.url
      this.dialogShowImageVisible = true
    },
    uploadImageSuccess(response, file, fileList) {
      setCurrentUrl('/student/studentLists')
      this.formData[response.data.name] =  response.data.url
      console.log(response)
      fileList[0].url = process.env.BASE_API + response.data.url
      // fileList[0].url = process.env.BASE_API + response.data.url
      // console.log( fileList[0].url)
      // this.formDataLists.forEach((item, index) => {
      //   if (item.field_name === response.data.name) {
      //     this.formDataLists[index].value = response.data.url
      //     this.formDataLists[index].file = fileList
      //   }
      // })
    },
    handleAddStudent() {
      setCurrentUrl('/student/studentLists')
      this.$router.push({
        path: '/student/addStudent'
      })
    },
    handleSaveStudent() {
      setCurrentUrl('/student/studentLists')
      this.$refs.studentInfoForm.validate((valid, errors) => {
        if (valid) {
          var formdata = JSON.parse(JSON.stringify(this.formData))
          // console.log(JSON.stringify(this.formData))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true

          for (var k in formdata) {
            var value = formdata[k]
            if (typeof value === 'object') {
              if (value !== null && value.length > 0) {
                formdata[k] = value[value.length - 1]
              }
              if (value !== null && value.length === 0) {
                formdata[k] = null
              }
            }
          }
          formdata['pici_id'] = this.piciValue
          editStudentInfo(formdata).then(response => {
            this.$alert('修改学生信息成功', '提示', {
              type: 'success'
            })
            this.formSumbitLoading = false
          }).catch(error => {
            this.formSumbitLoading = false
            console.log(error)
          })
        } else {
          var errorHtml = []
          for (var k in errors) {
            errorHtml.push(errors[k][0].message)
          }
          this.$alert(errorHtml.join('<br />'), '错误信息', {
            type: 'error',
            dangerouslyUseHTMLString: true
          })
          this.formSumbitLoading = false
        }
      })
    },
    handleFormFieldSet() {
      setCurrentUrl('/student/studentLists')
      this.$router.replace({
        path: '/setting/setStudentForm',
        query: {
          formName: '电脑后台修改信息表单',
          pici_id: this.piciValue
        }
      })
    },
    ReadIDCardInfo() {
      setCurrentUrl('/student/studentLists')
      var _this = this
      axios.get("http://localhost:29999/api/ReadMsg").then(res=>{
        console.log(this.formData)
        var info=res.data
        this.formDataLists.forEach((item, index) => {
          this.$alert('读取成功!<br />姓名：' + info.name + '<br />身份证号码：' + info.cardno, '成功提示', {
            type: 'success',
            dangerouslyUseHTMLString: true
          })
          if (item.field_name === 'name') {
            this.formData['name'] = info.name
          }
          if (item.field_name === 'idcode') {
            this.formData['idcode'] = info.cardno
          }
          if (item.field_name === 'sex') {
            this.formData['sex'] =  info.sex
          }
          if (item.field_name === 'address') {
            this.formData['address'] = info.address
          }
          if (item.field_name === 'region_code' || item.field_name === 'hkszd' || item.field_name === 'tzs_region') {
            const xzqhm = []
            const idcode = info.cardno
            const qum = parseInt(idcode.substring(0, 6))
            const shim = parseInt(qum / 100) * 100
            const shengm = parseInt(qum / 10000) * 10000
            xzqhm.push(shengm)
            xzqhm.push(shim)
            xzqhm.push(qum)
            // console.log('xzqhm', xzqhm)
            this.formData[item.field_name] = xzqhm
            console.log(xzqhm)
           // this.fetchRegionLists(xzqhm)
          }
        })
        console.log(this.formData)
      }).catch(error=>{
        console.log(error)
      })
    }
  }
}
</script>

<style scoped>
.form-label-text {
  color: #dcac6c;
  font-size: 12px;
}

.el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 6px !important;
}
</style>
