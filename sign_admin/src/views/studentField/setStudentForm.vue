<template>
  <div class="app-container" style="height: 100%;">
    <el-row>
      <el-button :type="formName=='电脑后台报名信息表单'?'primary':'default'" size="small" @click.native="handleChangeFormName('电脑后台报名信息表单')">电脑后台报名信息表单</el-button>
      <el-button :type="formName=='电脑后台修改信息表单'?'primary':'default'" size="small" @click.native="handleChangeFormName('电脑后台修改信息表单')">电脑后台修改信息表单</el-button>
      <el-button :type="formName=='电脑学生报名信息表单'?'primary':'default'" size="small" @click.native="handleChangeFormName('电脑学生报名信息表单')">电脑学生报名信息表单</el-button>
      <el-button :type="formName=='电脑学生完善信息表单'?'primary':'default'" size="small" @click.native="handleChangeFormName('电脑学生完善信息表单')">电脑学生完善信息表单</el-button>
      <el-button :type="formName=='手机学生报名信息表单'?'primary':'default'" size="small" @click.native="handleChangeFormName('手机学生报名信息表单')">手机学生报名信息表单</el-button>
      <el-button :type="formName=='手机学生完善信息表单'?'primary':'default'" size="small" @click.native="handleChangeFormName('手机学生完善信息表单')" >手机学生完善信息表单</el-button>
      <el-button :type="formName=='申请劳动预备信息表单'?'primary':'default'" size="small" @click.native="handleChangeFormName('申请劳动预备信息表单')" >申请劳动预备信息表单</el-button>
    </el-row>
    <el-row style="height: calc(100% - 60px);margin-top:10px;">
      <el-col :span="4" style="height: 100%;">
        <student-field-lists ref="studentFieldLists" :default-checked-keys="defaultCheckedKeys" @SelectStudentFieldChange="SelectStudentFieldChange"/>
      </el-col>
      <el-col :span="20" style="height: 100%;">
        <student-form-field ref="studentFormField" :form-name="formName" style="height:100%" @fetchSelectFieldDefaultSelect="fetchSelectFieldDefaultSelect"/>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import StudentFormItem from './components/StudentFormItem'
import StudentFormField from './components/StudentFormField'
import StudentFieldLists from './components/StudentField'
import { isInArray } from '@/utils/index'
import { addStudentFormField, deleteStudentFormField, getFieldNameByFormName } from '@/api/studentFormField'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  components: {
    StudentFormItem,
    StudentFormField,
    StudentFieldLists
  },
  data() {
    return {
      activeName: '1',
      defaultCheckedKeys: [],
      formName: '电脑后台报名信息表单'
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
  },
  created() {
    setCurrentUrl(this.$route.path)
    var formName = this.$route.query.formName
    if (formName === undefined) {
      formName = '电脑后台报名信息表单'
    }
    this.formName = formName
    this.fetchSelectFieldDefaultSelect()
  },
  methods: {
    fetchSelectFieldDefaultSelect() {
      getFieldNameByFormName({ formName: this.formName, piciId: localStorage.getItem('piciId') }).then(response => {
        this.defaultCheckedKeys = response.data
        this.$refs.studentFieldLists.$refs.selectStudentFieldTree.setCheckedKeys(this.defaultCheckedKeys)
      })
    },
    SelectStudentFieldChange(data, status) {
      const params = {
        fieldName: data.fieldName,
        formName: this.formName,
        piciId: localStorage.getItem('piciId')
      }
      if (isInArray(params.fieldName, status.checkedKeys)) {
        addStudentFormField(params).then(response => {
          this.$refs.studentFormField.fetchData(this.formName)
        })
      } else {
        deleteStudentFormField(params).then(response => {
          this.$refs.studentFormField.fetchData(this.formName)
        })
      }
    },
    handleChangeFormName(formName) {
      this.formName = formName
      this.fetchSelectFieldDefaultSelect()
      this.$refs.studentFormField.fetchData(formName)
      this.$router.replace({ path: '/setting/setStudentForm', query: { formName: formName }})
    }
  }
}
</script>

<style scoped>

</style>
