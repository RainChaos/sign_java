<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-form ref="form" :inline="true" :model="params" label-width="80px" size="small">
        <el-form-item style="margin-left: 15px;margin-bottom: 0px;">
          <el-button type="primary" @click.native="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 创建文章</el-button>
        </el-form-item>
       <!-- <el-form-item label="用户名称" style="margin-bottom: 0">
          <el-input v-model="params.username"/>
        </el-form-item>
        <el-form-item label="活动时间" style="margin-bottom: 0">
          <el-col :span="11">
            <el-date-picker
              v-model="params.starttime"
              :picker-options="pickerOptions"
              type="datetime"
              placeholder="选择开始日期时间"
              align="right"/>
          </el-col>
          <el-col :span="2" class="line">-</el-col>
          <el-col :span="11">
            <el-date-picker
              v-model="params.endtime"
              :picker-options="pickerOptions"
              type="datetime"
              placeholder="选择结束日期时间"
              align="right"/>
          </el-col>
        </el-form-item>
        <el-form-item style="margin-left: 15px;margin-bottom: 0px;">
          <el-button type="primary" @click.native="fetchData"><i class="el-icon-search" aria-hidden="true"/> 搜 索</el-button>
        </el-form-item>-->
      </el-form>
    </el-row>
    <el-row style="height: calc(100% - 90px)">
      <el-table
        v-loading="listLoading"
        :data="list.data"
        element-loading-text="正在加载中..."
        size="small"
        border
        height="100%"
        fit
        highlight-current-row>
        <el-table-column align="center" label="ID" width="60">
          <template slot-scope="scope">{{ scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column label="标题" width="300">
          <template slot-scope="scope">
            {{ scope.row.title }}
          </template>
        </el-table-column>
        <el-table-column label="分类" width="250">
          <template slot-scope="scope">
            {{ scope.row.cateName }}
          </template>
        </el-table-column>
        <el-table-column label="概要" width="300">
          <template slot-scope="scope">
            {{ scope.row.stitle }}
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建时间" width="200">
          <template slot-scope="scope">
            <i class="el-icon-time"/><span>{{ scope.row.createTime }}</span>
        </template></el-table-column>
        <el-table-column align="center" label="更新时间" width="200">
          <template slot-scope="scope">
            <i class="el-icon-time"/><span>{{ scope.row.updateTime }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="是否发布" width="100">
          <template slot-scope="scope">
            <el-switch
              v-model="scope.row.status"
              :active-value="statusSwitch.activeValue"
              :inactive-value="statusSwitch.inactiveValue"
              active-color="#13ce66"
              inactive-color="#ff4949"
              />
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
    </el-row>
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
import { getArticleLists, deleteArticle } from '@/api/article'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'

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
        activeValue: true,
        inactiveValue: false,
        activeColor: '#13ce66',
        inactiveColor: '#ff4949'
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
  },
  created() {
    setCurrentUrl(this.$route.path)
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      getArticleLists(this.params).then(response => {
        this.list = response.data
        /* this.pagination.current_page = response.data.current_page*/
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
     * 弹出添加文章信息对话框
     */
    handleAdd(index, row) {
      this.$router.push({ path: '/article/editArticle' , query: { id: 0 }})
    },
    /**
     * 弹出修改文章信息对话框
     */
    handleEdit(index, row) {
      this.$router.push({ path: '/article/editArticle', query: { id: row.id }})
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
        deleteArticle({ id: ids }).then(response => {
          this.$alert('删除成功', '提示', { type: 'success' })
          this.fetchData()
        })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>
