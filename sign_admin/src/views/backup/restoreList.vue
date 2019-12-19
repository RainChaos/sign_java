<template>
  <div class="app-container" style="height: 100%;">
    <el-row class="actionBtn">
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
      height="calc( 100% - 60px )">
      <el-table-column align="center" label="序号" width="60">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column label="备份名称" width="260">
        <template slot-scope="scope">
          {{ scope.row.time_text }}
        </template>
      </el-table-column>
      <el-table-column label="卷数" width="80">
        <template slot-scope="scope">
          {{ scope.row.part }}
        </template>
      </el-table-column>
      <el-table-column label="压缩" width="100">
        <template slot-scope="scope">
          {{ scope.row.compress }}
        </template>
      </el-table-column>
      <el-table-column label="大小" width="100">
        <template slot-scope="scope">
          {{ scope.row.size }}
        </template>
      </el-table-column>
      <el-table-column label="备份时间" width="200">
        <template slot-scope="scope">
          {{ scope.row.restore_time }}
        </template>
      </el-table-column>
      <el-table-column label="状态" width="150">
        <template slot-scope="scope">
          {{ scope.row.status }}
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="success" plain @click="handleRestore(scope.$index, scope.row)"> 还原</el-button>
          <el-button size="mini" type="danger" plain @click="handleDelete(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import { importList, deleteBackData, importBackData } from '@/api/Restore'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      list: [],
      listLoading: true
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
    fetchData() {
      this.listLoading = true
      importList().then(response => {
        this.list = response.data
        this.listLoading = false
      }).catch(error => {
        console.log(error)
      })
    },
    handleRestore(index, row) {
      let status = '.'
      const successFunction = function(result) {
        if (result.success) {
          // 如果还在同一卷
          if (result.data.gz) {
            result.data.info += status
            if (status.length === 5) {
              status = '.'
            } else {
              status += '.'
            }
          }
          // 设置显示信息
          row.status = result.msg
          // 还原第二部分
          if (result.data.part) {
            importBackData({ part: result.data.part, start: result.data.start, list: result.data.list })
              .then(successFunction)
              .catch(error => {
                console.log(error)
              })
          }
          // 还原完成
          if (result.data === '还原完成') {
            // window.location.reload();
          }
        } else {
          this.$alert(result.msg, '错误提示', { type: 'error' })
        }
      }
      importBackData({ time: row.time }).then(successFunction).catch(error => {
        console.log(error)
      })
    },
    handleDelete(index, row) {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteBackData({ time: row.time }).then(response => {
          this.fetchData()
        }).catch(error => {
          console.log(error)
        })
      })
    }
  }
}
</script>
