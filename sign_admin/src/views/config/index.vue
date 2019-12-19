<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-button  size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增配置项</el-button>
<!--      <el-button  size="small" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true"/> 批量删除</el-button>-->
      <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>

      <el-radio-group v-model="configGroup" style="float: right" @change="handleGroup">
        <el-radio-button v-for="item in configGroupLists" :key="item.value" :label="item.value">{{ item.text }}</el-radio-button>
      </el-radio-group>
    </el-row>
    <el-table
      v-loading="listLoading"
      :data="list.data"
      height="calc(100% - 100px)"
      element-loading-text="正在加载中..."
      size="small"
      border
      fit
      highlight-current-row>
      <el-table-column align="center" type="selection" width="50"/>
      <el-table-column align="center" label="ID" width="60">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="标识" width="300">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="标题" width="250">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="类型" width="80">
        <template slot-scope="scope">
          {{ scope.row.type_text }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="配置" width="200">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="备注" width="200">
        <template slot-scope="scope">
          {{ scope.row.remark }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="值" width="150">
        <template slot-scope="scope">
          {{ scope.row.value }}
        </template>
      </el-table-column>
      <el-table-column label="排序" width="60">
        <template slot-scope="scope">
          {{ scope.row.sort }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="状态" width="70">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.status"
            :active-value="statusSwitch.activeValue"
            :inactive-value="statusSwitch.inactiveValue"
            @change="handleChangeStatus(scope.$index,scope.row)"/>
        </template>
      </el-table-column>
      <el-table-column fixed="right" label="操作">
        <template slot-scope="scope">
          <el-button size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
          <el-button size="mini" type="danger" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
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
  </div>
</template>

<script>
import { getConfigLists, getConfigGroup, deleteConfig } from '@/api/config'
import { vilidateBtn } from '@/utils/auth'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      list: {},
      configGroup: 0,
      configGroupLists: [],
      pagination: {
        current_page: 0,
        pre_page: 20,
        total: 0
      },
      listLoading: true,
      params: {
        group: 0,
        limit: 20,
        page: 1
      },
      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0
      },
      pickerOptions: {
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchData()
    this.fetchGroupData()
  },
  created() {
    setCurrentUrl(this.$route.path)
    this.fetchData()
    this.fetchGroupData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      return new Promise((resolve, reject) => {
        getConfigLists(this.params).then(response => {
          this.list = response.data
          this.pagination.current_page = response.data.current_page
          this.pagination.total = response.data.total
          this.listLoading = false
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    fetchGroupData() {
      return new Promise((resolve, reject) => {
        getConfigGroup({ all: true }).then(response => {
          this.configGroupLists = response.data
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    handleGroup(val) {
      this.params.group = val
      this.params.page = 1
      this.fetchData()
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
      this.$router.push({ path: '/system/configForm' })
    },
    /**
     * 弹出修改管理员信息对话框
     */
    handleEdit(index, row) {
      this.$router.push({ path: '/system/configForm', query: { config_id: row.id }})
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
        return new Promise((resolve, reject) => {
          deleteConfig({ ids: ids }).then(response => {
            this.fetchData()
            this.$message.success('删除成功')
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    }
  }
}
</script>
