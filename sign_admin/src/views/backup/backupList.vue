<template>
  <div class="app-container" style="height: 100%;">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleExport"><i class="el-icon-plus"/> 立即备份</el-button>
<!--      <el-button size="small" type="primary" @click="handleOptimize"><i class="fa fa-cog"/> 优化表</el-button>-->
<!--      <el-button size="small" type="primary" @click="handleRepair"><i class="fa fa-cog"/> 修复表</el-button>-->
<!--      <el-button size="small" type="primary" @click="handleBackupXml"><i class="fa fa-save"/> 备份XML</el-button>-->
<!--      <el-button size="small" type="primary" @click="handleRestoreXml"><i class="fa fa-rebel"/> 还原XML</el-button>-->
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
      :height="tableHeight"
      @selection-change="handleSelectionChange">
      <el-table-column
        align="center"
        type="selection"
        width="50"/>
      <el-table-column align="center" label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="名称" width="260">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
<!--      <el-table-column label="字符类型" width="200">-->
<!--        <template slot-scope="scope">-->
<!--          {{ scope.row.collation }}-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="大小" width="100">-->
<!--        <template slot-scope="scope">-->
<!--          {{ scope.row.data_length }}-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="备份时间" width="200">
        <template slot-scope="scope">
          {{ scope.row.create_time }}
        </template>
      </el-table-column>
      <el-table-column label="" width="200">

        <template slot-scope="scope">
<!--          <el-button size="mini" ><a :href="apiRoot  + '/backup/' +scope.row.name">下载</a></el-button>-->
          <el-button size="mini" @click="download(scope.row.name)">下载</el-button>

        </template>
      </el-table-column>
<!--      <el-table-column label="数据表备注" width="400">-->
<!--        <template slot-scope="scope">-->
<!--          {{ scope.row.comment }}-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="备份状态" width="200">-->
<!--        <template slot-scope="scope">-->
<!--          {{ scope.row.status }}-->
<!--        </template>-->
<!--      </el-table-column>-->
    </el-table>

    <el-row >
      <el-pagination
              :current-page="pagination.current_page"
              :page-sizes="[15,30, 50, 100]"
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
import { exportList, exportBackPost, exportBackGet, Optimize, Repair, BackupXml, RestoreXml, signBackUp, getBackList, downloadBackup} from '@/api/backUp'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      apiRoot: process.env.BASE_API,
      tableHeight: '0px',
      list: [],
      multipleSelection: [],
      listLoading: true,
      params: {
        limit: 15,
        page: 1
      },
      pagination: {
        current_page: 0,
        pre_page: 15,
        total: 0
      },
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
    download(name){
      // let url = apiRoot  + '/backup/' + scope.row.name
      downloadBackup({name: name}).then(response => {
        const content = response.data
        const blob = new Blob([content])
        const fileName = name
        if ('download' in document.createElement('a')) { // 非IE下载
          const elink = document.createElement('a')
          elink.download = fileName
          elink.style.display = 'none'
          elink.href = URL.createObjectURL(blob)
          document.body.appendChild(elink)
          elink.click()
          URL.revokeObjectURL(elink.href) // 释放URL 对象
          document.body.removeChild(elink)
        } else { // IE10+下载
          navigator.msSaveBlob(blob, fileName)
        }
      }).catch(response => {
        console.log(response)
      })
    },
    handleSizeChange(val){
      setCurrentUrl(this.$route.path)
      this.pagination.pre_page = val
      this.params.limit = this.pagination.pre_page
      this.params.page = 1
      this.fetchData()
    },
    handleCurrentChange(val) {
      setCurrentUrl(this.$route.path)
      this.params.limit = this.pagination.pre_page
      this.params.page = val
      this.fetchData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fetchData() {
      this.listLoading = true
      // return new Promise((resolve, reject) => {
        getBackList(this.params).then(response => {
          this.list = response.data.data
          this.pagination.total = response.data.total
          this.tableHeight = window.innerHeight - this.$refs.multipleTable.$el.offsetTop - 192;
          this.listLoading = false
          // resolve(response)
        }).catch(error => {
          reject(error)
        })
      // })
    },
    handleExport() {

      // if (this.multipleSelection.length === 0) {
      //   this.$alert('请先选择需要删除的数据', '提示', { type: 'warning' })
      //   return
      // }
      // var tables = []
      // this.multipleSelection.forEach(item => {
      //   tables.push(item.name)
      // })
      // tables = tables.join(',')
      //
      signBackUp().then(response => {
        this.$alert(response.msg, '备份成功', { type: 'success' })
        if (response.success) {

        } else {
          this.$alert(response.msg, '错误提示', { type: 'warning' })
        }
      })
    },

    exportBackData(tab, status) {
      var tables = []
      this.multipleSelection.forEach(item => {
        tables.push(item.name)
      })
      tables = tables.join(',')
      tab['tables'] = tables
      exportBackGet(tab).then(response => {
        if (response.success) {
          // 备份完成
          if (typeof response.data.tab !== 'object') {
            console.log(tab.id)
            console.log(this.list)
            this.list[tab.id].status = response.msg
            this.$alert(response.msg, '成功提示', { type: 'success' })
            return
          }
          // 未完成备份，继续做备份请求
          this.list[tab.id].status = response.msg
          this.exportBackData(response.data.tab, tab.id !== response.data.tab.id)
        } else {
          this.$alert(response.msg, '错误提示', { type: 'warning' })
        }
      })
    },

    // 优化表
    handleOptimize() {
      if (this.multipleSelection.length === 0) {
        this.$alert('请先选择需要优化的数据表', '提示', { type: 'warning' })
        return
      }
      var tables = []
      this.multipleSelection.forEach(item => {
        tables.push(item.name)
      })
      tables = tables.join(',')

      Optimize({ tables: tables }).then(response => {
        if (response.success) {
          this.$alert(response.msg, '成功提示', { type: 'success' })
        } else {
          this.$alert(response.msg, '错误提示', { type: 'warning' })
        }
      })
    },
    //  修复表
    handleRepair() {
      if (this.multipleSelection.length === 0) {
        this.$alert('请先选择需要修复的数据表', '提示', { type: 'warning' })
        return
      }
      var tables = []
      this.multipleSelection.forEach(item => {
        tables.push(item.name)
      })
      tables = tables.join(',')

      Repair({ tables: tables }).then(response => {
        if (response.success) {
          this.$alert(response.msg, '成功提示', { type: 'success' })
        } else {
          this.$alert(response.msg, '错误提示', { type: 'warning' })
        }
      })
    },

    //  备份xml
    handleBackupXml() {
      if (this.multipleSelection.length === 0) {
        this.$alert('请先选择需要备份XML的数据表', '提示', { type: 'warning' })
        return
      }
      var tables = []
      this.multipleSelection.forEach(item => {
        tables.push(item.name)
      })
      tables = tables.join(',')

      BackupXml({ tables: tables }).then(response => {
        if (response.success) {
          this.$alert(response.msg, '成功提示', { type: 'success' })
        } else {
          this.$alert(response.msg, '错误提示', { type: 'warning' })
        }
      })
    },
    //  还原xml
    handleRestoreXml() {
      if (this.multipleSelection.length === 0) {
        this.$alert('请先选择需要还原XML的数据表', '提示', { type: 'warning' })
        return
      }
      var tables = []
      this.multipleSelection.forEach(item => {
        tables.push(item.name)
      })
      tables = tables.join(',')

      RestoreXml({ tables: tables }).then(response => {
        if (response.success) {
          this.$alert(response.msg, '成功提示', { type: 'success' })
        } else {
          this.$alert(response.msg, '错误提示', { type: 'warning' })
        }
      })
    },
    handleAdd() {
    },
    handleEdit(index, row) {
    },
    handleDelete() {
    }
  }
}
</script>
