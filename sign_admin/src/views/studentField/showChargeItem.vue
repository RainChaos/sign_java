<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增项目</el-button>
      <!--<el-button size="small" type="primary" @click="syncChargeItem"><i class="el-icon-plus" aria-hidden="true"/> 同步收费系统项目</el-button>-->
      <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
    </el-row>
    <el-alert
      :closable="false"
      title="提示: 收费项目功能仅用于设置学生缴费时项目的排序、是否默认选中、是否禁止修改等状态"
      style="border-radius: 0px;font-weight: bolder;"
      type="error"/>
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
      <el-table-column label="收费项目id" width="200">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="需要显示的收费项目名称" width="200">
        <template slot-scope="scope">
          {{ scope.row.itemName }}
        </template>
      </el-table-column>
      <el-table-column label="字段名称" width="150">
        <template slot-scope="scope">
          {{ scope.row.fieldName }}
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
      <el-table-column label="是否默认选中" width="150" align="center">
        <template slot-scope="scope">
          <i v-if="scope.row.isSelected===false" class="el-icon-close" style="color:firebrick" />
          <i v-if="scope.row.isSelected===true" class="el-icon-check" style="color:forestgreen" />
        </template>
      </el-table-column>
      <el-table-column label="是否禁止修改" width="150" align="center">
        <template slot-scope="scope">
          <i v-if="scope.row.isDisabled===false" class="el-icon-close" style="color:firebrick" />
          <i v-if="scope.row.isDisabled===true" class="el-icon-check" style="color:forestgreen" />
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
      <el-form ref="ShowChargeItemForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="收费项目ID：" prop="id">
          <el-input v-model="form.id" auto-complete="off" placeholder="请输入收费系统的项目ID"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="项目名称：" prop="itemName">
          <el-input v-model="form.itemName" auto-complete="off" placeholder="请输入收费系统的项目名称"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="字段名称：" prop="fieldName">
          <el-input v-model="form.fieldName" auto-complete="off" placeholder="请输入字段名称"/>
        </el-form-item>
        <el-form-item>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否启用：">
              <el-switch
                v-model="form.status"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否默认选中：">
              <el-switch
                v-model="form.isSelected"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否禁止修改：">
              <el-switch
                v-model="form.isDisabled"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
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
import { getShowChargeItemLists, addShowChargeItem, updateShowChargeItem, deleteShowChargeItem ,syncChargeItem} from '@/api/showChargeItem'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  itemName: '',
  fieldName: '',
  sort: 0,
  status: 1,
  isSelected: 0,
  isDisabled: 0
}
export default {
  data() {
    return {
      list: [],
      listLoading: true,
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
        itemName: [
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
      getShowChargeItemLists(this.params).then(response => {
        this.list = response.data
        this.listLoading = false
      })
    },
    /**
     * 添加需要显示的收费项目
     */
    handleAdd() {
      this.formIconClass = 'fa fa-plus'
      this.formTitle = '添加需要显示的收费项目'
      this.form = Object.assign({}, defaultForm)
      this.dialogFormVisible = true
    },
    /**
     * 同步收费系统的收费项目
     */
    syncChargeItem(){
      this.$confirm('此操作将清空当前的收费项目数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        syncChargeItem().then(response => {
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
     * 修改需要显示的收费项目
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改需要显示的收费项目'
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    /**
       * 删除
       */
    handleDeleteRow(index, row) {
      this.deleteDatas(row.id)
    },
    /**
       * 批量删除
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
        deleteShowChargeItem({ id: ids }).then(response => {
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
      this.$refs.ShowChargeItemForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          if (this.formTitle === '添加需要显示的收费项目') {
            addShowChargeItem(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(error => { this.formSumbitLoading = false })
          } else {
            updateShowChargeItem(formdata).then(response => {
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
