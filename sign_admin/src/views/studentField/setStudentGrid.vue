<template>
  <div class="app-container" style="height: 100%;">
    <el-row>
      <el-button :type="formName=='现场迎新列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('现场迎新列表')">现场迎新列表</el-button>
      <el-button :type="formName=='维护学生列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('维护学生列表')">维护学生列表</el-button>
      <el-button :type="formName=='预备制审核列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('预备制审核列表')">预备制审核列表</el-button>
      <el-button :type="formName=='打印通知书列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('打印通知书列表')">打印通知书列表</el-button>
      <el-button :type="formName=='入住公寓列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('入住公寓列表')">入住公寓列表</el-button>
      <el-button :type="formName=='分班管理列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('分班管理列表')">分班管理列表</el-button>
      <el-button :type="formName=='学籍异动列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('学籍异动列表')">学籍异动列表</el-button>
      <el-button :type="formName=='一键分班列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('一键分班列表')">一键分班列表</el-button>
    </el-row>
    <el-row style="height: calc(100% - 60px);margin-top:10px;">
      <el-col :span="4" style="height: 100%;">
        <student-field-lists ref="studentFieldLists" :default-checked-keys="defaultCheckedKeys" @SelectStudentFieldChange="SelectStudentFieldChange"/>
      </el-col>
      <el-col :span="20" style="height: 100%;">
        <student-grid-field ref="studentGridField" :form-name="formName" style="height:100%"/>
      </el-col>
    </el-row>
  </div>
</template>
<script>
import StudentFormItem from './components/StudentFormItem'
import StudentGridField from './components/StudentGridField'
import StudentFieldLists from './components/StudentField'
import { isInArray } from '@/utils/index'
import { addStudentFormField, deleteStudentFormField, getFieldNameByFormName } from '@/api/studentFormField'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  components: {
    StudentFormItem,
    StudentGridField,
    StudentFieldLists
  },
  data() {
    return {
      activeName: '1',
      defaultCheckedKeys: [],
      formName: '维护学生列表'
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
  },
  created() {
    setCurrentUrl(this.$route.path)
    var formName = this.$route.query.formName
    if (formName === undefined) {
      formName = '维护学生列表'
    }
    this.formName = formName
    this.fetchSelectFieldDefaultSelect()

  },
  methods: {
    fetchSelectFieldDefaultSelect() {
      getFieldNameByFormName({ formName: this.formName }).then(response => {
        this.defaultCheckedKeys = response.data
        this.$refs.studentFieldLists.$refs.selectStudentFieldTree.setCheckedKeys(this.defaultCheckedKeys)
      })
    },
    SelectStudentFieldChange(data, status) {
      const params = {
        fieldName: data.fieldName,
        formName: this.formName
      }
      if (isInArray(params.fieldName, status.checkedKeys)) {
        addStudentFormField(params).then(response => {
          this.$refs.studentGridField.fetchData(this.formName)
        })
      } else {
        deleteStudentFormField(params).then(response => {
          this.$refs.studentGridField.fetchData(this.formName)
        })
      }
    },
    handleChangeFormName(formName) {
      this.formName = formName
      this.fetchSelectFieldDefaultSelect()
      this.$refs.studentGridField.fetchData(formName)
      this.$router.replace({ path: '/setting/setStudentGrid', query: { formName: formName }})
    }
  }
}
</script>
