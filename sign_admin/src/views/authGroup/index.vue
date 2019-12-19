<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus"/> 新增用户角色</el-button>
      <!-- <el-button size="small" type="danger" @click="handleDelete"><i class="el-icon-delete"/> 批量删除</el-button> -->
      <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
    </el-row>
    <el-table
      v-loading="listLoading"
      ref="multipleTable"
      :data="list"
      element-loading-text="正在加载中..."
      size="small"
      border
      fit
      highlight-current-row
      @selection-change="handleSelectionChange">

      <el-table-column
        align="center"
        type="selection"
        width="50"/>

      <el-table-column align="center" label="ID" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>

      <el-table-column label="角色名称" width="110">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="备注" width="500">
        <template slot-scope="scope">
          {{ scope.row.description }}
        </template>
      </el-table-column>
      <!--<el-table-column align="center" label="是否启用" width="100">-->
        <!--<template slot-scope="scope">-->
          <!--<el-switch-->
            <!--v-model="scope.row.status"-->
            <!--:active-value="statusSwitch.activeValue"-->
            <!--:inactive-value="statusSwitch.inactiveValue"-->
            <!--active-color="#13ce66"-->
            <!--inactive-color="#ff4949"-->
            <!--@change="handleChangeStatus(scope.$index,scope.row)"/>-->
        <!--</template>-->
      <!--</el-table-column>-->
      <el-table-column
        fixed="right"
        label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
          <el-button size="mini" type="success" plain @click="handleRules(scope.$index, scope.row)"> 设置权限</el-button>
          <el-button size="mini" type="danger" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="500px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="authGroupForm" :rules="rules" :model="form" size="mini">
        <el-form-item :label-width="formLabelWidth" label="名称：" prop="name">
          <el-input v-model="form.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="备注：">
          <el-input :rows="2" v-model="form.description" type="textarea" auto-complete="off"/>
        </el-form-item>
        <!--<el-form-item :label-width="formLabelWidth" label="是否启用：">-->
          <!--<el-switch-->
            <!--v-model="form.status"-->
            <!--:active-value="statusSwitch.activeValue"-->
            <!--:inactive-value="statusSwitch.inactiveValue"-->
            <!--active-color="#13ce66"-->
            <!--inactive-color="#ff4949"/>-->
        <!--</el-form-item>-->
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getAuthGroupLists, updateStatus, addAuthGroup, updateAuthGroup, deleteAuthGroup } from '@/api/authGroup'
import { vilidateBtn } from '@/utils/auth'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      list: [],
      multipleSelection: [],
      listLoading: true,
      dialogFormVisible: false,
      formLabelWidth: '100px',
      statusSwitch: {
        activeValue: '1',
        inactiveValue: '0',
        activeColor: '#13ce66',
        inactiveColor: '#ff4949'
      },
      formTitle: '添加用户角色',
      formIconClass: 'fa fa-plus',
      formSumbitLoading: false,
      form: {
        id: 0,
        name: '',
        type: '',
        description: '',
        status: 0
      },
      rules: {
        name: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
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
    this.fetchData()
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fetchData() {
      this.listLoading = true
      return new Promise((resolve, reject) => {
        getAuthGroupLists().then(response => {
          this.list = response.data
          this.listLoading = false
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    handleSizeChange(val) {
      console.log(`每页 ${val} 条`)
    },
    handleCurrentChange(val) {
      console.log(`当前页: ${val}`)
    },
    /**
     * 弹出添加用户组信息对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-plus'
      this.formTitle = '添加用户角色信息'
      this.form = { id: 0, name: '', type: '', description: '', status: 0 }
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改用户组信息对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改用户角色信息'
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    /**
     * 跳转到设置权限页面
     */
    handleRules(index, row) {
      this.$router.push({ path: '/system/assignGroupRules', query: { group_id: row.id }})
    },
    /**
       * 弹出修改用户组信息对话框
       */
    handleDeleteRow(index, row) {
      this.deleteDatas(row.id)
    },
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

    deleteDatas(ids) {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteAuthGroup({ ids: ids }).then(response => {
          this.fetchData()
          this.$alert('删除成功', '提示', { type: 'success' })
        }).catch(error => {
          console.log(error)
        })
      })
    },
    /**
       * 提交用户组信息
       */
    handleSubmit() {
      this.$refs['authGroupForm'].validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            return new Promise((resolve, reject) => {
              addAuthGroup(formdata).then(response => {
                this.dialogFormVisible = false
                this.fetchData()
                this.$message({ type: 'success', message: '添加成功!' })
                this.formSumbitLoading = false
                resolve(response)
              }).catch(error => {
                this.formSumbitLoading = false
                reject(error)
              })
            })
          } else {
            // 如果id不是0则为修改用户，修改用户密码为空则为不修改
            return new Promise((resolve, reject) => {
              updateAuthGroup(formdata).then(response => {
                this.dialogFormVisible = false
                this.fetchData()
                this.$message({ type: 'success', message: '修改成功!' })
                this.formSumbitLoading = false
                resolve(response)
              }).catch(error => {
                this.formSumbitLoading = false
                reject(error)
              })
            })
          }
        } else {
          return false
        }
      })
    },
    /**
     * 列表修改用户是否启用状态
     * @param index
     * @param row
     * @returns {Promise<any>}
     */
    handleChangeStatus(index, row) {
      return new Promise((resolve, reject) => {
        updateStatus(row.id, row.status).then(response => {
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    }
  }
}
</script>
