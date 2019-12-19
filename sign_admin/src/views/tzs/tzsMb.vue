<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增模板</el-button>
      <el-button size="small" type="primary" @click="handlePrintDesign"><i class="fa fa-eye" aria-hidden="true"/> 模板设计</el-button>
      <!-- <el-button size="small" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true"/> 批量删除</el-button> -->
      <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
    </el-row>
    <el-table
      v-loading="listLoading"
      :data="list.data"
      element-loading-text="正在加载中..."
      size="small"
      border
      fit
      highlight-current-row
      @selection-change="handleSelectionChange">
      <el-table-column fixed="left" align="center" type="selection" width="50"/>
      <el-table-column align="center" label="序号" width="60">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="通知书模板名称" width="200">
        <template slot-scope="scope">
          {{ scope.row.template_name }}
        </template>
      </el-table-column>
      <el-table-column label="排序" width="150">
        <template slot-scope="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>
      <el-table-column label="是否启用" width="150" align="center">
        <template slot-scope="scope">
          <i v-if="scope.row.status===0 || scope.row.status=== false" class="el-icon-close" style="color:firebrick" />
          <i v-if="scope.row.status===1 || scope.row.status=== true" class="el-icon-check" style="color:forestgreen" />
        </template>
      </el-table-column>
      <el-table-column
        label="操作">
        <template slot-scope="scope">
          <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
          <el-button type="danger" size="mini" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="800px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="PrintTemplateForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="打印模板名称：" prop="template_name">
          <el-input v-model="form.template_name" auto-complete="off" placeholder="请输入通知书打印项"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="打印内容：" prop="template_content">
          <el-input v-model="form.template_content" :rows="12" auto-complete="off" type="textarea"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="是否启用：">
          <el-switch v-model="form.status" :active-value="statusSwitch.activeValue" :inactive-value="statusSwitch.inactiveValue"/>
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
import { getPrintTemplateLists, addPrintTemplate, updatePrintTemplate, deletePrintTemplate } from '@/api/printTemplate'
import { LodopFuncs } from '@/utils/LodopFuncs'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  type: 'tzs',
  template_name: '',
  template_content: '',
  sort: 0,
  status: 1
}
export default {
  data() {
    return {
      list: {},
      multipleSelection: [],
      listLoading: true,
      params: {
        limit: 20,
        page: 1
      },
      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0
      },

      dialogFormVisible: false,
      formLabelWidth: '120px',
      formTitle: '添加通知书模板',
      formIconClass: 'fa fa-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: Object.assign({}, defaultForm),
      rules: {
        template_name: [
          { required: true, message: '请输入通知书模板名称', trigger: 'blur' },
          { min: 2, max: 18, message: '长度在 2 到 18 个字符', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchData()
  },
  created() {
    setCurrentUrl(this.$route.path)
    LodopFuncs.beforeInit()
    this.fetchData()
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fetchData() {
      this.listLoading = true
      getPrintTemplateLists(this.params).then(response => {
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
      this.formTitle = '添加通知书打印项'
      this.form = Object.assign({}, defaultForm)
      this.dialogFormVisible = true
    },
    /**
       * 弹出修改管理员信息对话框
       */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改通知书打印项'
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
    handlePrintDesign() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择通知书模板', '提示', { type: 'warning' })
      } else if (rows.length > 1) {
        this.$alert('请只选中一个通知书模板数据', '提示', { type: 'warning' })
      } else {
        var LODOP = LodopFuncs.getLodop()
        var temp = rows[0]
        eval(temp.template_content)
        LODOP.PRINT_DESIGN()
        var _this = this
        LODOP.On_Return = function(TaskID, Value) {
          temp.template_content = Value
          updatePrintTemplate(temp).then(response => {
            _this.fetchData()
          }).catch(error => { console.log(error) })
        }
      }
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
        deletePrintTemplate({ ids: ids }).then(response => {
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
      this.$refs.PrintTemplateForm.validate((valid) => {
        if (valid) {
          // const formdata = JSON.parse(JSON.stringify(this.form))
          const formdata = this.form
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addPrintTemplate(this.form).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(error => {
              this.formSumbitLoading = false
            })
          } else {
            updatePrintTemplate(this.form).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('修改成功')
              this.formSumbitLoading = false
            }).catch(error => {
              this.formSumbitLoading = false
            })
          }
        }
      })
    }
  }
}
</script>
