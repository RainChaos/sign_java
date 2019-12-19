<template>
  <div class="app-container" style="height: 100%">
    <el-row>
      <el-form
        label-position="left"
        inline
        size="mini"
        class="student-info">
        <!-- <el-row style="background: #ececec;padding:20px;margin:10px 20px;border:1px solid #dddddd;"> -->
          <!-- <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="院系：">
              <span>{{ studentInfo.unit_code_yuanxi }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="专业：">
              <span>{{ studentInfo.unit_code_zhuanye }}</span>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="姓名：">
              <span>{{ studentInfo.name }}</span>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="序号：">
              <span>{{ studentInfo.bm_number }}</span>
            </el-form-item>
          </el-col> -->

          <!-- <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="性别：">
              <span>{{ studentInfo.sex }}</span>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="省份：">
              <span>{{ studentInfo.region_code_shengfen }}</span>
            </el-form-item>
          </el-col> -->
          <!-- <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="学历：">
              <span>{{ studentInfo.education }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="6">
            <el-form-item :label-width="InfoLabelWidth" class="studentInfo" label="类型：">
              <span>{{ studentInfo.student_type }}</span>
            </el-form-item>
          </el-col> -->
        <!-- </el-row> -->
        <el-row
          :style="{backgroundColor:backgroundColor}"
          style="padding:10px;border-top:1px solid #ddd;background: #cf9236;color:#fff;">
          <el-col :span="24" style="text-align: center;font-size:16px;">
            申请状态：
            <span v-if="studentInfo.ldybz_check=='未申请'">未提交申请</span>
            <span v-if="studentInfo.ldybz_check=='已提交，未审核'">已提交申请,请等待老师进行审核</span>
            <span v-if="studentInfo.ldybz_check=='审核通过'">审核通过</span>
            <span v-if="studentInfo.ldybz_check=='审核不通过'">审核未通过,原因：{{ studentInfo.ldybz_remark }}</span>
          </el-col>
        </el-row>
      </el-form>
    </el-row>
    <el-row>
      <el-form
        ref="studentInfoForm"
        :model="formData"
        :rules="rules"
        style="background-color:#F8F8F9;padding:10px;overflow: hidden"
        size="small">
        <el-col :span="12">
          <el-form-item label="批次：" label-width="160px" style="margin-bottom: 0px;width: 100%;">
            <el-select disabled v-model="piciValue" placeholder="请选择类型" size="small" style="width:400px">
              <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
          </el-form-item>
        </el-col>
        <template v-for="(item) in formDataLists" v-if="item.is_show==1">
          <el-col v-if="item.data_type=='单位结构'" :span="12">
            <el-form-item :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
<!--              <el-cascader v-model="item.value" :disabled="item.is_disabed==1" :options="unitOptions" :props="unitProps" :placeholder="'请选择'+item.describe" :style="{width:item.width+'px'}"/>-->
              <el-input disabled v-model="formData[item.field_name]"  :placeholder="'请填写'+item.describe" :style="{width:item.width+'px'}" type="textarea"/>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item
              v-if="item.data_type=='数字' && item.field_name != 'id'"
              :label-width="item.label_width+'px'"
              :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input-number v-model="item.value" :disabled="item.is_disabed==1" :style="{width:item.width+'px'}"/>
            </el-form-item>

            <el-form-item
              v-if="item.data_type=='字符串'"
              :label-width="item.label_width+'px'"
              :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input v-model="item.value" :disabled="item.is_disabed==1" :placeholder="'请填写'+item.describe" :style="{width:item.width+'px'}"/>
            </el-form-item>

            <el-form-item
              v-if="item.data_type=='文本'"
              :label-width="item.label_width+'px'"
              :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input v-model="item.value" :style="{width:item.width+'px'}" :placeholder="'请填写'+item.describe" type="textarea"/>
            </el-form-item>

            <el-form-item v-if="item.data_type=='省市区'" :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader
                v-if="item.event_name!=null&&item.event_name.length>0"
                v-model="item.value"
                :options="regionOptions"
                :props="regionProps"
                :style="{width:item.width+'px'}"
                :placeholder="'请选择'+item.describe"
                @active-item-change="handleItemChange"
                @change="item.event_name"/>
              <el-cascader
                v-else-if="item.event_name==null||item.event_name.length==0"
                v-model="item.value"
                :options="regionOptions"
                :props="regionProps"
                :style="{width:item.width+'px'}"
                :placeholder="'请选择'+item.describe"
                @active-item-change="handleItemChange"/>
            </el-form-item>

            <el-form-item v-if="item.data_type=='标准数据'" :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-select v-model="item.value" :style="{width:item.width+'px'}" :placeholder="'请选择'+item.describe">
                <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value"/>
              </el-select>
            </el-form-item>

            <el-form-item v-if="item.data_type=='毕业中学'" :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-select
                v-model="item.value"
                :placeholder="'请选择'+item.describe"
                :style="{width:item.width+'px'}">
                <el-option v-for="middle in middleSchoolLists" :key="middle.school_name" :label="middle.school_name" :value="middle.school_name"/>
              </el-select>
            </el-form-item>

            <el-form-item v-if="item.data_type=='图片'" :label-width="item.label_width+'px'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-upload
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :name="item.field_name"
                v-model="item.value"
                :file-list="item.file"
                :data="{describe:item.describe,field_name:item.field_name}"
                :action="uploadImageUrl"
                :limit="item.limit"
                :multiple="item.multiple"
                :on-success="uploadImageSuccess"
                :style="{width:item.width+'px'}"
                disabled
                list-type="picture">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">{{ item.remark }}</div>
              </el-upload>
            </el-form-item>
          </el-col>
        </template>
      </el-form>
      <div style="background-color:#F8F8F9;padding: 10px;border-top: 1px solid #ddd;text-align: center;">
        <el-button size="small" style="width: 200px;" type="info" @click="$router.go(-1)">
          <i class="fa fa-reply"/> 返回
        </el-button>
        <el-button v-loading="formSumbitLoading" size="small" type="primary" style="width:200px" @click.native="handleSaveStudent('审核通过', '是')">
          <i class="el-icon-check" aria-hidden="true"/> 审核通过
        </el-button>
        <el-button v-loading="formSumbitLoading" size="small" type="primary" style="width:200px" @click.native="handleBuTongGuo">
          <i class="el-icon-check" aria-hidden="true"/> 审核不通过
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

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="500px">
      <div slot="title"><i class="fa fa-close"/>审核不通过的原因</div>
      <el-form ref="CheckForm" :model="studentInfo" size="small">
          <el-input v-model="studentInfo.ldybz_remark" type="textarea" auto-complete="off"/>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSaveStudent('审核不通过', '否')">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getRegionLists, getRegion } from '@/api/region'
import { getAllLists } from '@/api/unit'
import { getEditFormConfigLists, getStudentInfo } from '@/api/student'
import { getAllMiddleSchoolLists } from '@/api/middleSchool'
import { editStudentInfoForLDYBZ} from '@/api/student'
import { formValidater } from '@/utils/formValidate'
import { parseTime } from '@/utils/index'
import { getPiciLists } from '@/api/pici'
import { deleteFile } from '@/api/file'
import { Loading } from 'element-ui'
import {
  getSelectUnitLists
} from '@/api/selectUnit'

import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      piciValue: 0,
      piciLists: [],
      dialogFormVisible: false,
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
      formLabelWidth: '500px'
    }
  },
  computed: {
    formData: function() {
      var formData = {}
      this.formDataLists.forEach((item) => {
        formData[item.field_name] = item.value
      })
      return formData
    },
    backgroundColor: function() {
      if (this.studentInfo.ldybz_check === 0) {
        return '#cf9236'
      }
      if (this.studentInfo.ldybz_check === 1) {
        return '#cf9236'
      }
      if (this.studentInfo.ldybz_check === 2) {
        return '#5db75d'
      }
      if (this.studentInfo.ldybz_check === 3) {
        return '#ff0000'
      }
    }
  },
  activated(){

    this.studentInfo= {}

            this.formDataLists= []
            this.rules= {}
            this.regionOptions= []
            this.regionProps= {
              value: 'code',
              label: 'fullName',
              children: 'children'
            },
            this.unitOptions= []
            this.unitProps= {
              value: 'unit_code',
              label: 'unit_name',
              children: 'children'
            }
            this.middleSchoolLists= [],
            this.ShowImage= {
              title: '',
              url: ''
            }

    var student_id = this.$route.query.student_id
    setCurrentUrl('/student/ldCheckLists')
    this.fetchStudentInfo(student_id)
    this.fetchPiciData()
    this.fetchShengFenLists()
  },
  mounted() {

    var student_id = this.$route.query.student_id
    this.fetchStudentInfo(student_id)
    // this.fetchUnitLists()
    this.fetchPiciData()
    this.fetchShengFenLists()

  },
  methods: {
    fetchFormFieldLists(student_id) {
      getEditFormConfigLists({ form_name: '申请劳动预备信息表单', student_id: student_id,id: student_id, pici_id: this.piciValue }).then(response => {
        this.formDataLists = response.data
        var rules = {}
        this.formDataLists.forEach((item, index) => {
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
          if (item.data_type === '图片') {
            if (this.formDataLists[index].file.length>0){
              this.formDataLists[index].file[0].url=process.env.BASE_API+this.formDataLists[index].file[0].url
            }
          }
          // if (item.data_type === '省市区') {
          //   if (typeof value === 'string') {
          //     if (value.length > 0) {
          //       this.formDataLists[index].value = JSON.parse(value)
          //     } else {
          //       this.formDataLists[index].value = []
          //     }
          //   }
          //   this.fetchRegionLists(item.value)
          // }
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
        this.rules = rules
      })
    },
    fetchPiciData() {
      getPiciLists().then(response => {
        this.piciLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStudentInfo(student_id) {
      getStudentInfo({ student_id: student_id,id: student_id}).then(response => {
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
      const loadingInstance = Loading.service({ target: this.$refs.studentInfoForm.$el })
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
      var region_code = region[region.length - 1]
      getAllMiddleSchoolLists({ region_code: region_code }).then(response => {
        this.middleSchoolLists = response.data
      }).catch(() => {})
    },

    fetchShengFenLists() {
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
      var data = JSON.parse(JSON.stringify(file))
      var url = data.url
      deleteFile({ url: url }).then(response => {
        console.log(response.data)
      })
    },
    handlePreview(file) {
      var data = JSON.parse(JSON.stringify(file))
      this.ShowImage.title = data.name
      this.ShowImage.url = data.url
      this.dialogShowImageVisible = true
    },
    uploadImageSuccess(response, file, fileList) {
      fileList[0].url = response.data.url
      this.formDataLists.forEach((item, index) => {
        if (item.field_name === response.data.field_name) {
          this.formDataLists[index].value = response.data.value
          this.formDataLists[index].file = fileList
        }
      })
    },
    handleSaveStudent(ldybz_check, is_ldybz) {
      this.$refs.studentInfoForm.validate((valid, errors) => {
        if (valid) {
          var studentInfo = JSON.parse(JSON.stringify(this.studentInfo))
          var formdata = {
            id: studentInfo.id,
            is_ldybz: is_ldybz,
            ldybz_check: ldybz_check,
            ldybz_check_time: parseTime(new Date()),
            ldybz_remark: studentInfo.ldybz_remark
          }
          editStudentInfoForLDYBZ(formdata).then(response => {
            this.$alert('审核学生信息成功', '提示', { type: 'success' })
            this.dialogFormVisible = false
            this.fetchStudentInfo(studentInfo.id)
          }).catch(error => {
            this.dialogFormVisible = false
            console.log(error)
          })
        } else {
          var errorHtml = []
          for (var k in errors) {
            errorHtml.push(errors[k][0].message)
          }
          this.$alert(errorHtml.join('<br />'), '错误信息', { type: 'error', dangerouslyUseHTMLString: true })
          this.dialogFormVisible = false
        }
      })
    },
    handleBuTongGuo() {
      this.dialogFormVisible = true
    }
  }
}
</script>

<style scoped>
  .form-label-text{
    color:#dcac6c;
    font-size:12px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 6px !important;
  }

</style>
