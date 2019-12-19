<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col :span="4" style="border:1px solid #dddddd;height:100%;">
        <div style="padding:10px;border-bottom:1px solid #dddddd">
          数据分类
        </div>
        <el-scrollbar style="height: calc(100% - 40px);">
          <el-tree
            ref="treeListsBox"
            :props="defaultProps"
            :highlight-current="true"
            :default-expand-all="true"
            :indent="10"
            :data="typeTreeLists"
            node-key="value"
            @current-change="currentChange">
            <div slot-scope="{ node, data }" class="custom-tree-node">
              <div style="font-size:14px;">{{ data.name }}
                <span v-if="data.pid!==0">【{{ data.value }}】</span>
              </div>
            </div>
          </el-tree>
        </el-scrollbar>
      </el-col>
      <el-col :span="20" style="height:100%;padding-left:10px;">
        <el-row class="actionBtn">
          <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/>新 增</el-button>
          <el-button size="small" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true"/> 批量删除</el-button>
          <el-button size="small" type="info" @click="setDefault"><i class="el-icon-data-analysis" aria-hidden="true"/> 设为默认</el-button>
          <el-button size="small" type="success" @click="handleSort"><i class="el-icon-sort" aria-hidden="true"/> 调整排序</el-button>
          <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
        </el-row>
        <el-table
          v-loading="listLoading"
          :data="list.data"
          height="calc(100% - 92px)"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row
          @selection-change="handleSelectionChange">
          <el-table-column align="center" type="selection" width="50"/>
          <el-table-column label="类型" width="200" align="center">
            <template slot-scope="scope">
              {{ scope.row.type }}
            </template>
          </el-table-column>
          <el-table-column label="代码值" width="150" align="center">
            <template slot-scope="scope">
              {{ scope.row.code }}
            </template>
          </el-table-column>

          <el-table-column :show-overflow-tooltip="true" label="标题" width="300" align="center">
            <template slot-scope="scope">
              {{ scope.row.name }}
            </template>
          </el-table-column>

          <el-table-column align="center" label="是否启用" width="100" >
            <template slot-scope="scope">
              <el-switch
                v-model="scope.row.status"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"
                active-color="#13ce66"
                inactive-color="#ff4949"
                @change="handleChangeStatus(scope.$index,scope.row)"/>
            </template>
          </el-table-column>
          <el-table-column label="是否默认值" width="120" align="center">
            <template slot-scope="scope">
              <span v-if="scope.row.is_default == '0'">否</span>
              <span v-if="scope.row.is_default == '1'">是</span>
            </template>
          </el-table-column>
          <el-table-column label="排序" width="80" align="center">
            <template slot-scope="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作">
            <template slot-scope="scope">
              <el-button size="mini" plain @click="handleUp(scope.$index, scope.row)">上移</el-button>
              <el-button size="mini" plain @click="handleDown(scope.$index, scope.row)">下移</el-button>
              <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
              <el-button type="danger" size="mini" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row v-if="pagination.total>0">
          <el-pagination
            :current-page="pagination.current_page"
            :page-sizes="[20,30, 50, 100]"
            :page-size="pagination.pre_page"
            :total="pagination.total"
            background
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"/>
        </el-row>
      </el-col>
    </el-row>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="500px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="dictionaryForm" :model="form" :rules="rules" size="small">
        <el-form-item :label-width="formLabelWidth" label="类型：" prop="type">
          <el-input v-model="form.type" disabled auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="代码值：" prop="code">
          <el-input v-model="form.code" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="名称：" prop="name">
          <el-input v-model="form.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：" prop="sort">
          <el-input-number v-model="form.sort" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="是否启用：">
          <el-switch
            v-model="form.status"
            :active-value="statusSwitch.activeValue"
            :inactive-value="statusSwitch.inactiveValue"
            active-color="#13ce66"
            inactive-color="#ff4949"
          />
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
import { getDictionaryLists, addDictionary, updateDictionary, deleteDictionary, updateStatus, setOrderSortAll, setDefault } from '@/api/dictionary'
import { getDictionaryType } from '@/api/dictionaryType'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      typeTreeLists: [],
      defaultProps: {
        children: 'children',
        label: 'name'
      },
      list: {},
      multipleSelection: [],
      pagination: {
        current_page: 0,
        pre_page: 20,
        total: 0
      },
      listLoading: true,
      params: {
        limit: 20,
        page: 1,
        type: 'HYZKDM'
      },
      statusSwitch: {
        activeValue: true,
        inactiveValue: false,
        activeColor: '#13ce66',
        inactiveColor: '#ff4949'
      },
      dialogFormVisible: false,
      formLabelWidth: '100px',
      formTitle: '添加管理员',
      formIconClass: 'fa fa-user-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: { id: 0, type: '', code: '', name: '', sort: '', status: 0 },
      rules: {
        code: [
          { required: true, message: '请输入代码值', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchTypeData()
    this.fetchData()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    this.fetchTypeData()
    this.fetchData()
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 设置当前结构数据
    currentChange(data, node) {
      this.params.page = 1
      this.params.type = JSON.parse(JSON.stringify(data)).value
      this.fetchData()
    },

    fetchTypeData() {
      return new Promise((resolve, reject) => {
        getDictionaryType().then(response => {
          this.typeTreeLists = response.data
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    fetchData() {
      this.listLoading = true
      return new Promise((resolve, reject) => {
        getDictionaryLists(this.params).then(response => {
          this.list = response.data
          /* this.pagination.current_page = response.data.current_page*/
          this.pagination.total = response.data.total
          this.listLoading = false
          resolve(response)
        }).catch(error => {
          reject(error)
        })
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

    handleSort() {
      setOrderSortAll({ type: this.params.type }).then(response => {
        this.fetchData()
      }).catch(() => {})
    },
    /**
     *  上移
     */
    handleUp(index, row) {
      if (index > 0) {
        var downData = JSON.parse(JSON.stringify(this.list.data[index]))
        var upData = JSON.parse(JSON.stringify(this.list.data[index - 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateDictionary(upData).then(response => {})
        updateDictionary(downData).then(response => {})
        this.list.data.splice(index - 1, 1)
        this.list.data[index - 1]['sort'] = downData['sort']
        this.list.data.splice(index, 0, upData)
      } else {
        this.$alert('已经是第一条，不可上移', '提示', { type: 'warning' })
      }
    },
    /**
     *  下移
     */
    handleDown(index, row) {
      if ((index + 1) === this.list.data.length) {
        this.$alert('已经是最后一条，不可下移', '提示', { type: 'warning' })
      } else {
        var upData = JSON.parse(JSON.stringify(this.list.data[index]))
        var downData = JSON.parse(JSON.stringify(this.list.data[index + 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateDictionary(upData).then(response => {})
        updateDictionary(downData).then(response => {})
        this.list.data.splice(index + 1, 1)
        this.list.data.splice(index, 0, downData)

        this.list.data[index + 1]['sort'] = upData['sort']
      }
    },

    /**
     * 弹出添加管理员信息对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-user-plus'
      this.formTitle = '添加数据信息'
      const sort = this.list.total + 1
      this.form = { id: 0, type: this.params.type, code: '', name: '', sort: sort, status: 0 }
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改管理员信息对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改数据信息'
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    /**
     * 删除数据
     */
    handleDeleteRow(index, row) {
      this.deleteDatas(row.id)
    },
    /**
     * 批量删除数据
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

    setDefault() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择数据', '提示', { type: 'warning' })
      } else if(rows.length > 1){
        this.$alert('只能选择一个为默认值', '提示', { type: 'warning' })
      } else if(rows.length === 1){
        let ids = []
        rows.forEach(row => {
          ids.push(row.id)
        })
        ids = ids.join(',')
        console.log(ids)

        setDefault({ id: ids }).then(response => {
          this.fetchData()
          this.$message.success('设置成功')
        }).catch(() => {})
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
        deleteDictionary({ ids: ids }).then(response => {
          this.fetchData()
          this.$message.success('删除成功')
        }).catch(() => {})
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs.dictionaryForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addDictionary(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message({ type: 'success', message: '添加成功!' })
              this.formSumbitLoading = false
            }).catch(error => {
              this.formSumbitLoading = false
            })
          } else {
            updateDictionary(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message({ type: 'success', message: '修改成功!' })
              this.formSumbitLoading = false
            }).catch(error => {
              this.formSumbitLoading = false
            })
          }
        } else {
          return false
        }
      })
    },

    /**
     * 列表修改是否启用状态
     * @param index
     * @param row
     * @returns {Promise<any>}
     */
    handleChangeStatus(index, row) {
      return new Promise((resolve, reject) => {
        updateStatus({ id: row.id, status: row.status }).then(response => {
          console.log(response.data)
        }).catch(error => {
          reject(error)
        })
      })
    }

  }
}
</script>

<style scoped>

</style>
