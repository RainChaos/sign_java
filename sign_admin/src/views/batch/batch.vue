<template>
<div class="app-container">
  <el-row class="actionBtn">
    <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true" /> 新增批次</el-button>
    <!-- <el-button size="small" type="success" @click="handleSort"><i class="el-icon-sort" aria-hidden="true" /> 调整排序</el-button> -->
    <!-- <el-button size="small" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true" /> 批量删除</el-button> -->
    <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true" /> 刷 新</el-button>
  </el-row>
  <el-table v-loading="listLoading" :data="list.data" element-loading-text="正在加载中..." size="small" border fit highlight-current-row>
    <el-table-column align="center" label="排序" width="60">
      <template slot-scope="scope">{{ scope.row.sort }}</template>
    </el-table-column>
    <el-table-column label="报名批次名称" width="200">
      <template slot-scope="scope">
        {{ scope.row.name }}
      </template>
    </el-table-column>
    <el-table-column label="批次代码" width="100">
      <template slot-scope="scope">
        {{ scope.row.code }}
      </template>
    </el-table-column>
    <el-table-column label="报名开始时间" width="200">
      <template slot-scope="scope">
        {{ scope.row.start_time }}
      </template>
    </el-table-column>
    <el-table-column label="报名结束时间" width="200">
      <template slot-scope="scope">
        {{ scope.row.end_time }}
      </template>
    </el-table-column>
    <el-table-column align="center" label="是否开放报名" width="150">
      <template slot-scope="scope">
        <i v-if="scope.row.status===0" class="el-icon-close" style="color:firebrick" />
        <i v-if="scope.row.status===1" class="el-icon-check" style="color:forestgreen" />
      </template>
    </el-table-column>
    <el-table-column align="center" label="是否结束收费" width="150">
      <template slot-scope="scope">
        <i v-if="scope.row.is_end_charge===0 || scope.row.is_end_charge===false" class="el-icon-close" style="color:firebrick" />
        <i v-if="scope.row.is_end_charge===1 || scope.row.is_end_charge===true" class="el-icon-check" style="color:forestgreen" />
      </template>
    </el-table-column>
    <el-table-column label="结束交费时间" width="200">
      <template slot-scope="scope">
        {{ scope.row.end_charge_time }}
      </template>
    </el-table-column>
    <el-table-column :show-overflow-tooltip="true" label="批次备注信息" width="400">
      <template slot-scope="scope">
        {{ scope.row.remark }}
      </template>
    </el-table-column>
    <el-table-column fixed="right" label="操作" width="160" align="center">
      <template slot-scope="scope">
        <!-- <el-button size="mini" plain @click="handleUp(scope.$index, scope.row)">上移</el-button>
        <el-button size="mini" plain @click="handleDown(scope.$index, scope.row)">下移</el-button> -->
        <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
        <el-button type="danger" size="mini" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
      </template>
    </el-table-column>
  </el-table>

  <!-- <el-row v-if="pagination.total>0">
      <el-pagination
        :current-page="pagination.current_page"
        :page-sizes="[20,30, 50, 100]"
        :page-size="pagination.pre_page"
        :total="pagination.total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </el-row> -->
  <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px">
    <div slot="title"><i :class="formIconClass" /> {{ formTitle }}</div>
    <el-form ref="articleCategoryForm" :rules="rules" :model="form" size="small">
      <el-form-item :label-width="formLabelWidth" label="批次名称：" prop="name">
        <el-input v-model="form.name" auto-complete="off" />
      </el-form-item>
      <el-form-item :label-width="formLabelWidth" label="批次代码：" prop="code">
        <el-input v-model="form.code" auto-complete="off" />
      </el-form-item>


      <el-form-item :label-width="formLabelWidth" label="是否开放报名：">
        <el-switch v-model="form.status" :active-value="statusSwitch.activeValue" :inactive-value="statusSwitch.inactiveValue" />
      </el-form-item>
      <el-form-item :label-width="formLabelWidth" label="报名时间区间：">
        <el-date-picker v-model="form.rangetime" type="datetimerange" range-separator="至" value-format="yyyy-MM-dd HH:mm:ss" start-placeholder="开始报名日期" end-placeholder="结束报名日期" style="width:100%" />
      </el-form-item>
      <el-form-item :label-width="formLabelWidth" label="结束报名提示：">
        <el-input :rows="2" v-model="form.end_sign_remark" type="textarea" auto-complete="off" />
      </el-form-item>


      <el-form-item :label-width="formLabelWidth" label="是否结束收费：">
        <el-switch v-model="form.is_end_charge" :active-value="statusSwitch.activeValue" :inactive-value="statusSwitch.inactiveValue" />
      </el-form-item>
      <el-form-item :label-width="formLabelWidth" label="结束收费时间：">
        <el-date-picker value-format="yyyy-MM-dd HH:mm:ss" v-model="form.end_charge_time" type="datetime" placeholder="选择日期时间" style="width:100%" />
      </el-form-item>


      <el-form-item :label-width="formLabelWidth" label="批次备注信息：">
        <el-input :rows="2" v-model="form.remark" type="textarea" auto-complete="off" />
      </el-form-item>

      <el-form-item :label-width="formLabelWidth" label="排序：" prop="sort">
        <el-input-number v-model="form.sort" :min="1" :max="1000" label="排序" />
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
import {
  getPiciLists,
  addPici,
  updatePici,
  setOrderSortAll,
  deletePici
} from '@/api/pici'

import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  name: '',
  code: '',
  is_end_charge: 0,
  end_sign_remark: '',
  end_charge_time: '',
  sort: 9999,
  status: 1,
  rangetime: '',
  remark: ''
}
export default {
  data() {
    return {
      list: {},
      pagination: {
        current_page: 0,
        pre_page: 20,
        total: 0
      },
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
      formTitle: '添加管理员',
      formIconClass: 'fa fa-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: Object.assign({}, defaultForm),
      rules: {
        name: [{
          required: true,
          message: '请输入批次名称',
          trigger: 'blur'
        }]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchData()
  },
  created() {
    this.fetchData()
    setCurrentUrl(this.$route.path)
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getPiciLists(this.params).then(response => {
        this.list = response.data
        this.pagination.current_page = response.data.current_page
        this.pagination.total = response.data.total
        this.listLoading = false
      }).catch(error => {
        console.log(error)
      })
    },
    handleSizeChange(val) {
      this.pagination.pre_page = val
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
      this.formTitle = '添加报名批次'
      this.form = Object.assign({}, defaultForm)
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改管理员信息对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改报名批次信息'
      var editdata = JSON.parse(JSON.stringify(row))
      var rangetime = []
      rangetime.push(editdata.start_time)
      rangetime.push(editdata.end_time)
      editdata['rangetime'] = rangetime
      this.form = editdata
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
        this.$alert('请先选择需要删除的数据', '提示', {
          type: 'warning'
        })
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
        deletePici({
          ids: ids
        }).then(response => {
          this.fetchData()
          this.$alert('删除成功', '提示', {
            type: 'success'
          })
        }).catch(error => {
          console.log(error)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },

    handleSort() {
      setOrderSortAll().then(response => {
        this.fetchData()
      }).catch(() => {})
    },
    /**
     *  上移
     */
    handleUp(index, row) {
      var listData = this.list.data
      if (index > 0) {
        var downData = JSON.parse(JSON.stringify(listData[index]))
        var upData = JSON.parse(JSON.stringify(listData[index - 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updatePici(upData).then(response => {})
        updatePici(downData).then(response => {})
        listData.splice(index - 1, 1)
        listData[index - 1]['sort'] = downData['sort']
        listData.splice(index, 0, upData)
      } else {
        this.$alert('已经是第一条，不可上移', '提示', {
          type: 'warning'
        })
      }
    },
    /**
     *  下移
     */
    handleDown(index, row) {
      var listData = this.list.data
      if ((index + 1) === listData.length) {
        this.$alert('已经是最后一条，不可下移', '提示', {
          type: 'warning'
        })
      } else {
        var upData = JSON.parse(JSON.stringify(listData[index]))
        var downData = JSON.parse(JSON.stringify(listData[index + 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updatePici(upData).then(response => {})
        updatePici(downData).then(response => {})
        listData.splice(index + 1, 1)
        listData.splice(index, 0, downData)
        listData[index + 1]['sort'] = upData['sort']
      }
    },

    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs['articleCategoryForm'].validate((valid) => {
        if (valid) {
          this.form.rangetime += ''
          const formdata = JSON.parse(JSON.stringify(this.form))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            return new Promise((resolve, reject) => {
              addPici(formdata).then(response => {
                this.dialogFormVisible = false
                this.fetchData()
                this.$message.success('添加成功')
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
              updatePici(formdata).then(response => {
                this.dialogFormVisible = false
                this.fetchData()
                this.$message.success('修改成功')
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
    }
  }
}
</script>
