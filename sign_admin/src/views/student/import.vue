<template>
  <div class="app-container" style="height: 100%">
    <el-row>
      <el-button type="primary" size="small" >
        <i class="el-icon-upload" aria-hidden="true"/>
        <a :href="templateUrl">模板下载</a>
      </el-button>
    </el-row>
    <el-row class="top-form-ct">
      <el-form ref="form" :inline="true" size="small">
        <el-select v-model="piciValue" placeholder="请选择批次" size="mini" style="width:200px;">
          <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
        </el-select>
        <el-button type="primary" size="small" @click.native="showUploadDialog">
          <i class="el-icon-upload" aria-hidden="true"/> 1：上传学生Excel表格
        </el-button>


        <el-button v-loading="importBtnLoading" type="warning" size="small" @click.native="importStudentData">
          <i class="fa fa-upload" /> 2：开始导入数据
        </el-button>
      </el-form>
    </el-row>

    <el-row style="margin-top: 10px">
      <el-row v-for="item in errorList" style="font-size: 14px; color: #1F2F3D">
        {{item}}
      </el-row>
    </el-row>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px" style="min-height: 300px;">
      <div slot="title"><i class="fa fa-upload"/> 1. 上传学生Excel表格</div>
      <el-form ref="ImportStudentInfoForm" size="small" style="min-height: 200px;padding:20px;">
        <el-upload
          ref="updateStudentInfo"
          :file-list="fileList"
          :auto-upload="false"
          :multiple="false"
          :headers="actionHeaders"
          :action="uploadExcelFieldUrl"
          :on-error="uploadError"
          :on-success="uploadSuccess"
          :before-upload="beforeAvatarUpload"
          class="updateStudentInfo">
          <el-button slot="trigger" size="small" type="info">
            选择需要上传的学生数据Excel表格
          </el-button>
          <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">
            <i class="el-icon-upload"/> 上传到服务器
          </el-button>
          <div slot="tip" class="el-upload__tip">只能上传xls、xlsx格式，且不超过10M</div>
        </el-upload>
      </el-form>
    </el-dialog>

    <el-dialog :visible.sync="importDialogVisible" :show-close="true" title="导入学生进度" width="30%">
      <div><el-progress :text-inside="true" :stroke-width="18" :percentage="percentage"/></div>
    </el-dialog>
  </div>
</template>

<script>
import { getExcelField, setExcelMajorMap, getStudentMapMajorLists, updateStudentMapMajor, deleteStudentMapMajor, getStudentMapFieldLists, updateStudentMapField, deleteStudentMapField, importStudentData } from '@/api/importStudent'
import { getStructureOnlyYxZyLists } from '@/api/unit'
import { getStudentFieldLists } from '@/api/studentField'
import { getToken } from '@/utils/auth'
import { getPiciLists } from '@/api/pici'

import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const imporJindu = {
  current_page: 0,
  last_page: 0,
  per_page: 1000,
  total: 0,
  finish: false
}
export default {
  data() {
    return {
      errorList:[],
      templateUrl: '',
      piciValue: '',
      piciLists: [],
      majorMapListsLoading: false,
      fieldsMapListsLoading: false,
      majorMapLists: [],
      fieldsMapLists: [],
      cascaderProps: {
        value: 'unit_code',
        label: 'unit_name'
      },
      fileList: [],
      dialogFormVisible: false,
      excelFilePath: '',
      excelSheetLists: [],
      selectExcelSheet: '',
      excelFieldLists: [],
      excelZhuanYeField: '',

      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0,
        activeColor: '#13ce66',
        inactiveColor: '#ff4949'
      },

      unitTreeLists: [],
      multipleMajorMapSelection: [],
      formLabelWidth: '150px',
      dialogMajorMapFormVisible: false,
      mapMajorFormData: { id: 0, excel_major: '', db_major_code: '', db_major_code_path: [], status: 0 },
      formMajorMapSumbitLoading: false,

      dbStudentFieldLists: [],
      multipleFieldMapSelection: [],
      dialogFieldMapFormVisible: false,
      mapFieldFormData: { id: 0, excel_field: '', db_field: '', status: 0 },
      formFieldMapSumbitLoading: false,
      importBtnLoading: false,
      importDialogVisible: false,
      ayncImportStudentRes: Object.assign({}, imporJindu)
    }
  },
  computed: {
    uploadExcelFieldUrl: function() {
      return process.env.BASE_API + '/admin/importStudent/uploadExcel'
    },
    actionHeaders: function() {
      return { 'Admin-Sign-Token': getToken(), "Current-Url" : this.$route.path}
    },
    // 导入学生进度条
    percentage: function() {
      const total = parseInt(this.ayncImportStudentRes.total)
      const finish = this.ayncImportStudentRes.finish
      if (total === 0) {
        return 0
      }
      if (finish) {
        return 100
      }
      const current_page = parseInt(this.ayncImportStudentRes.current_page)
      const per_page = parseInt(this.ayncImportStudentRes.per_page)
      return parseInt(((current_page * per_page) / total) * 100)
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchPiciData()
  },
  created() {
    this.templateUrl = process.env.BASE_API + '/import/template.xls'
    setCurrentUrl(this.$route.path)
    this.fetchPiciData()
  },
  methods: {
    templateDownload(){

    },
    fetchPiciData() {
      getPiciLists().then(response => {
        this.piciLists = response.data.data
        this.piciValue = this.$store.getters.currentPici
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStudentMapMajorData() {
      this.majorMapListsLoading = true
      return new Promise((resolve, reject) => {
        getStudentMapMajorLists(this.params).then(response => {
          this.majorMapLists = response.data
          this.majorMapListsLoading = false
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    fetchStudentMapFieldData() {
      this.fieldsMapListsLoading = true
      getStudentMapFieldLists(this.params).then(response => {
        this.fieldsMapLists = response.data
        this.fieldsMapListsLoading = false
      }).catch(error => {
        console.log(error)
      })
    },
    // 获取专业单位数据
    fetchUnitMajorData() {
      getStructureOnlyYxZyLists().then(response => {
        this.unitTreeLists = response.data
      }).catch(error => {
        console.log(error)
      })
    },

    // 获取系统学生字段数据
    fetchDbStudentFieldData() {
      getStudentFieldLists({ is_show: 1 }).then(response => {
        this.dbStudentFieldLists = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    // 显示上传学生数据页面
    showUploadDialog() {
      this.dialogFormVisible = true
    },
    // 提交上传学生Excel表格数据
    submitUpload() {
      this.$refs.updateStudentInfo.submit()
    },
    // 上传成功后的回调
    uploadSuccess(response) {
      // var gettype = Object.prototype.toString;
      // console.log(gettype.call(response));
      // console.log(response)
      // var res = JSON.parse(response.trim())
      // console.log(res)
      const data = response.data
      // var data = response.data
      console.log(data)
      this.excelFilePath = data
      // this.excelSheetLists = data.sheets
      this.dialogFormVisible = false
      // this.$message({ message: response.msg, type: 'warning' })
    },
    // 上传错误
    uploadError(response) {
      console.log(response.data)
    },
    // 上传前对文件的大小的判断
    beforeAvatarUpload(file) {
      var fileName = new Array()
      fileName = file.name.split('.')
      const extension = fileName[fileName.length - 1] === 'xls'
      const extension2 = fileName[fileName.length - 1] === 'xlsx'
      const isLt2M = file.size / 1024 / 1024 < 10
      if (!extension && !extension2) {
        this.$message({ message: '上传文件只能是xls、xlsx格式!', type: 'warning' })
      }
      if (!isLt2M) {
        this.$message({ message: '上传文件大小不能超过 10MB!', type: 'warning' })
      }
      return extension || extension2 && isLt2M
    },

    // 根据上传的学生数据获取工作表列表信息
    handleSelectExcelSheet() {
      const filePath = this.excelFilePath
      const sheetName = this.selectExcelSheet
      if (filePath !== '' && sheetName !== '') {
        getExcelField({ filePath: filePath, sheetName: sheetName }).then(response => {
          console.log(response.data)
          this.excelFieldLists = response.data
        }).catch(error => {
          console.log(error)
        })
      }
    },
    // 根据上传的学生数据获取专业映射关系
    handleGetMajorMap() {
      const filePath = this.excelFilePath
      const sheetName = this.selectExcelSheet
      const majorName = this.excelZhuanYeField
      if (filePath !== '' && sheetName !== '' && majorName !== '') {
        setExcelMajorMap({ filePath: filePath, sheetName: sheetName, majorName: majorName }).then(response => {
          this.fetchStudentMapMajorData()
          this.fetchStudentMapFieldData()
        }).catch(error => {
          console.log(error)
        })
      }
    },

    // 编辑专业映射关系
    handleEditMajorMap(index, row) {
      this.fetchUnitMajorData()
      this.mapMajorFormData = JSON.parse(JSON.stringify(row))
      this.dialogMajorMapFormVisible = true
    },
    // 设置选中的专业映射关系
    handleMajorMapSelectionChange(val) {
      this.multipleMajorMapSelection = val
    },
    // 提交修改专业映射关系
    handleMapMajorSubmit() {
      this.$refs.mapMajorForm.validate((valid) => {
        if (valid) {
          var formdata = JSON.parse(JSON.stringify(this.mapMajorFormData))
          var db_major_code_path = formdata.db_major_code_path
          formdata.db_major_code = db_major_code_path[db_major_code_path.length - 1]
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formMajorMapSumbitLoading = true
          if (formdata.id > 0) {
            updateStudentMapMajor(formdata).then(response => {
              this.dialogMajorMapFormVisible = false
              this.fetchStudentMapMajorData()
              this.$message.success('修改成功')
              this.formMajorMapSumbitLoading = false
            }).catch(error => {
              this.formMajorMapSumbitLoading = false
              console.log(error)
            })
          }
        }
      })
    },
    // 删除专业映射关系
    handleDeleteMajorMap() {
      const rows = JSON.parse(JSON.stringify(this.multipleMajorMapSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要删除的数据', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => { ids.push(row.id) })
        ids = ids.join(',')
        this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteStudentMapMajor({ ids: ids }).then(response => {
            this.fetchStudentMapMajorData()
            this.$message.success('删除成功')
          }).catch(error => {
            console.log(error)
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      }
    },

    // 编辑字段映射关系
    handleEditFieldMap(index, row) {
      this.fetchDbStudentFieldData()
      this.mapFieldFormData = JSON.parse(JSON.stringify(row))
      this.dialogFieldMapFormVisible = true
    },
    // 设置选中字段映射关系
    handleFieldMapSelectionChange(val) {
      this.multipleFieldMapSelection = val
    },
    // 提交修改字段映射关系
    handleMapFieldSubmit() {
      this.$refs.mapFieldForm.validate((valid) => {
        if (valid) {
          var formdata = JSON.parse(JSON.stringify(this.mapFieldFormData))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formFieldMapSumbitLoading = true
          if (formdata.id > 0) {
            updateStudentMapField(formdata).then(response => {
              this.dialogFieldMapFormVisible = false
              this.fetchStudentMapFieldData()
              this.$message.success('修改成功')
              this.formFieldMapSumbitLoading = false
            }).catch(error => {
              this.formFieldMapSumbitLoading = false
              console.log(error)
            })
          }
        }
      })
    },
    // 删除字段映射关系
    handleDeleteFieldMap() {
      const rows = JSON.parse(JSON.stringify(this.multipleFieldMapSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要删除的数据', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => { ids.push(row.id) })
        ids = ids.join(',')
        this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteStudentMapField({ ids: ids }).then(response => {
            this.fetchStudentMapFieldData()
            this.$message.success('删除成功')
          }).catch(error => {
            console.log(error)
          })
        }).catch(() => {
          this.$message.info('已取消删除')
        })
      }
    },
    importStudentData() {
      const filePath = this.excelFilePath
      const pici = this.piciValue
      // const sheetName = this.selectExcelSheet
      // const majorName = this.excelZhuanYeField
      if (filePath !== '' && pici != '') {
        // 导入学生数据
        var _this = this

        var params = {
          excelFilePath: this.excelFilePath,
          pici: pici
        }
        this.importBtnLoading = true
        importStudentData(params).then(response => {
          this.$alert('导入完成', '提示', { type: 'success' })
          this.importBtnLoading = false
          this.errorList = response.data
        }).catch(error => {
          console.log(error)
        })
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .top-form-ct{
    .el-form-item--mini.el-form-item,
    .el-form-item--small.el-form-item {
      margin-bottom: 0px !important;
    }
  }
</style>
