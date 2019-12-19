<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增字段</el-button>
      <!--<el-button size="small" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true"/> 批量删除</el-button>-->
      <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
    </el-row>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="正在加载中..."
      size="small"
      border
      fit
      highlight-current-row>
      <el-table-column align="center" label="ID" width="60">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="字段名称" width="200">
        <template slot-scope="scope">
          {{ scope.row.describe }}
        </template>
      </el-table-column>
      <el-table-column label="字段" width="150">
        <template slot-scope="scope">
          {{ scope.row.fieldName }}
        </template>
      </el-table-column>
      <el-table-column label="条件sql" width="300">
        <template slot-scope="scope">
          {{ scope.row.whereSql }}
        </template>
      </el-table-column>
      <el-table-column label="排序" width="150">
        <template slot-scope="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>
      <el-table-column label="是否启用" width="150" align="center">
        <template slot-scope="scope">
          <i v-if="scope.row.status===false" class="el-icon-close" style="color:firebrick" />
          <i v-if="scope.row.status===true" class="el-icon-check" style="color:forestgreen" />
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作">
        <template slot-scope="scope">
          <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
          <el-button type="danger" size="mini" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="StudentSearchFieldForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="查询字段：" prop="fieldName">
          <el-input v-model="form.fieldName" auto-complete="off" placeholder="请输入查询字段"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="label名称：" prop="describe">
          <el-input v-model="form.describe" auto-complete="off" placeholder="请输入查询字段名称"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="查询条件sql：" prop="whereSql">
          <el-input v-model="form.whereSql" auto-complete="off" placeholder="请输入查询条件sql语句"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="默认值：" prop="defaultValue">
          <el-input v-model="form.defaultValue" auto-complete="off" placeholder="请输入查询默认值"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="label长度：" prop="labelWidth">
          <el-input-number v-model="form.labelWidth" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="表单长度：" prop="width">
          <el-input-number v-model="form.width" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：" prop="sort">
          <el-input-number v-model="form.sort" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="是否启用：">
          <el-switch
            v-model="form.status"
            :active-value="statusSwitch.activeValue"
            :inactive-value="statusSwitch.inactiveValue"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStudentSearchFieldLists, addStudentSearchField, updateStudentSearchField, deleteStudentSearchField } from '@/api/studentSearchField'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  tableName: '学生信息列表',
  fieldName: '',
  describe: '',
  whereSql: '',
  defaultValue: -1,
  labelWidth: 100,
  width: 150,
  sort: 1,
  status: 1
}
export default {
  data() {
    return {
      list: [],
      listLoading: true,
      params: {
        limit: 20,
        page: 1
      },
      statusSwitch: {
        activeValue: true,
        inactiveValue: false
      },

      dialogFormVisible: false,
      formLabelWidth: '140px',
      formTitle: '添加需要显示的收费项目',
      formIconClass: 'fa fa-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: Object.assign({}, defaultForm),
      rules: {
        item_name: [
          { required: true, message: '请输入文章分类', trigger: 'blur' },
          { min: 2, max: 18, message: '长度在 2 到 18 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  computed: {

  },
  activated(){
    setCurrentUrl(this.$route.path)
  },
  created() {
    setCurrentUrl(this.$route.path)
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getStudentSearchFieldLists(this.params).then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    handleSizeChange(val) {
      this.params.limit = this.pagination.pre_page
      this.params.page = 1
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.params.limit = this.pagination.pre_page
      this.params.page = val
      this.fetchData()
    },
    /**
     * 弹出添加管理员信息对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-plus'
      this.formTitle = '添加表格查询字段'
      this.form = Object.assign({}, defaultForm)
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改管理员信息对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改表格查询字段'
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    /**
       * 删除用户数据
       */
    handleDeleteRow(index, row) {
      this.deleteDatas(row.id)
    },
    /**
       * 批量删除用户数据
       */
    handleDelete() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要删除的数据', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => {
          ids.push(row.id)
        })
        ids = ids.join(',')
        this.deleteDatas(ids)
      }
    },

    /**
       * 根据ids 删除用户，ids: 1,2,3,4
       */
    deleteDatas(ids) {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteStudentSearchField({ id: ids }).then(response => {
          this.fetchData()
          this.$message.success('删除成功')
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs.StudentSearchFieldForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addStudentSearchField(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(error => { this.formSumbitLoading = false })
          } else {
            updateStudentSearchField(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('修改成功')
              this.formSumbitLoading = false
            }).catch(error => { this.formSumbitLoading = false })
          }
        }
      })
    }
  }
}
</script>
