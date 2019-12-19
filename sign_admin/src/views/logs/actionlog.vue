<template>
  <div class="app-container">
    <el-row class="actionBtn" style="padding-bottom: 0px;">
      <el-form ref="form" :inline="true" :model="params" size="mini">
        <el-form-item label="操作人">
          <el-input v-model="params.username"/>
        </el-form-item>
        <el-form-item label="ip地址">
          <el-input v-model="params.ip"/>
        </el-form-item>
        <el-form-item label="名称">
          <el-input v-model="params.name"/>
        </el-form-item>
        <el-form-item label="url">
          <el-input v-model="params.url"/>
        </el-form-item>
        <el-form-item label="请求参数">
          <el-input v-model="params.describe"/>
        </el-form-item>
        <el-form-item label="操作时间">
          <el-col :span="11">
            <el-date-picker
                    value-format="yyyy-MM-dd HH:mm:ss"
              v-model="params.starttime"
              :picker-options="pickerOptions"
              type="datetime"
              placeholder="选择开始日期时间"
              align="right"/>
          </el-col>
          <el-col :span="2" class="line">-</el-col>
          <el-col :span="11">
            <el-date-picker
                    value-format="yyyy-MM-dd HH:mm:ss"
              v-model="params.endtime"
              :picker-options="pickerOptions"
              type="datetime"
              placeholder="选择结束日期时间"
              align="right"/>
          </el-col>
        </el-form-item>
        <el-form-item style="margin-left: 15px;">
          <el-button type="primary" @click.native="search"><i class="el-icon-search" aria-hidden="true"/> 搜 索</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <el-table
      v-loading="listLoading"
      :data="list.data"
      :height="tableHeight"
      element-loading-text="正在加载中..."
      size="small"
      border
      fit
      ref="multipleTable"
      highlight-current-row>
      <el-table-column align="center" label="ID" width="60">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="操作人账号" width="110">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column label="操作人姓名" width="110">
        <template slot-scope="scope">
          {{ scope.row.realname }}
        </template>
      </el-table-column>
      <el-table-column label="IP地址" width="130">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>
      <el-table-column label="名称" width="140">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="url" width="300">
        <template slot-scope="scope">
          {{ scope.row.url }}
        </template>
      </el-table-column>
      <el-table-column :show-overflow-tooltip="true" label="请求参数" width="300">
        <template slot-scope="scope">
          {{ scope.row.describe }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="创建时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time"/><span>{{ scope.row.create_time }}</span>
      </template></el-table-column>
      <el-table-column align="center" label="更新时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time"/><span>{{ scope.row.update_time }}</span>
        </template>
      </el-table-column>
    </el-table>

    <el-row v-if="pagination.total>0">
      <el-pagination
        :current-page="pagination.current_page"
        :page-sizes="[20,30, 50, 100]"
        :total="pagination.total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </el-row>
  </div>
</template>

<script>
import { getActionLogLists } from '@/api/actionLog'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'

export default {
  data() {
    return {
      tableHeight: '0px',
      list: {},
      pagination: {
        current_page: 0,
        pre_page: 20,
        total: 0
      },
      listLoading: true,
      params: {
        limit: 20,
        page: 1,
        username: '',
        starttime: '',
        endtime: '',
        ip: '',
        name: '',
        describe: '',
        url: ''
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
    search(){
      this.params.page = 1
      this.pagination.current_page = 0
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      return new Promise((resolve, reject) => {
        getActionLogLists(this.params).then(response => {
          this.list = response.data
          this.pagination.current_page = response.data.current_page
          this.pagination.total = response.data.total
          this.listLoading = false
          this.tableHeight = window.innerHeight - this.$refs.multipleTable.$el.offsetTop - 192;
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
    }
  }
}
</script>
