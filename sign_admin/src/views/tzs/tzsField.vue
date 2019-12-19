<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增打印项</el-button>
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
      highlight-current-row>
      <el-table-column fixed="left" align="center" type="selection" width="50"/>
      <el-table-column align="center" label="序号" width="60">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="通知书打印项" width="200">
        <template slot-scope="scope">
          {{ scope.row.attribute_name }}
        </template>
      </el-table-column>
      <el-table-column label="执行sql语句">
        <template slot-scope="scope">
          {{ scope.row.opt_sql }}
        </template>
      </el-table-column>
      <el-table-column label="样式">
        <template slot-scope="scope">
          {{ scope.row.style }}
        </template>
      </el-table-column>
      <el-table-column label="条形码样式">
        <template slot-scope="scope">
          {{ scope.row.barcode }}
        </template>
      </el-table-column>
      <el-table-column label="排序" width="150">
        <template slot-scope="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>
      <el-table-column label="是否启用" width="150" align="center">
        <template slot-scope="scope">
          <i v-if="scope.row.status===0" class="el-icon-close" style="color:firebrick" />
          <i v-if="scope.row.status===1" class="el-icon-check" style="color:forestgreen" />
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        label="操作"
        width="150">
        <template slot-scope="scope">
          <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
          <el-button type="danger" size="mini" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="PrintAttributeForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="打印项名称：" prop="attribute_name">
          <el-input v-model="form.attribute_name" auto-complete="off" placeholder="请输入通知书打印项"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="执行SQL语句：" prop="opt_sql">
          <el-input v-model="form.opt_sql" :rows="4" auto-complete="off" type="textarea" placeholder="请输入执行SQL语句"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="样式：" prop="opt_sql">
          <el-input v-model="form.style" :rows="4" auto-complete="off" type="textarea" placeholder="请输入执行SQL语句"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="条形码样式：" prop="opt_sql">
          <el-input v-model="form.barcode" :rows="4" auto-complete="off" type="textarea" placeholder="请输入执行SQL语句"/>
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
import { getPrintAttributeLists, addPrintAttribute, updatePrintAttribute, deletePrintAttribute } from '@/api/printAttribute'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  attribute_name: '',
  opt_sql: '',
  sort: 0,
  status: 1,
  style: '',
  barcode: ''
}
export default {
  data() {
    return {
      list: {},
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
      formLabelWidth: '140px',
      formTitle: '添加通知书打印项',
      formIconClass: 'fa fa-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: Object.assign({}, defaultForm), // Object.assign(target, source); 返回source 覆盖的源
      rules: {
        attribute_name: [
          { required: true, message: '请输入通知书打印项名称', trigger: 'blur' },
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
      getPrintAttributeLists(this.params).then(response => {

        this.list = response.data

        console.log(this.list);
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
        deletePrintAttribute({ ids: ids }).then(response => {
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
      this.$refs.PrintAttributeForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addPrintAttribute(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(error => {
              this.formSumbitLoading = false
            })
          } else {
            updatePrintAttribute(formdata).then(response => {
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
