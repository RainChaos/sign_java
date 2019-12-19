<template>
  <div class="app-container">
    <el-row class="actionBtn" style="padding-bottom: 0px;">

      <el-form ref="form" :inline="true" :model="params" size="mini">
        <el-form-item label="ip地址">
          <el-input v-model="params.ip"/>
        </el-form-item>
        <el-form-item label="操作人">
          <el-input v-model="params.username"/>
        </el-form-item>
        <el-form-item label="关键词">
          <el-input v-model="params.keyword"/>
        </el-form-item>

        <el-form-item label="操作名称">
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
        <el-form-item style="margin-left: 15px;">
          <el-button type="primary" @click.native="search"><i class="el-icon-search" aria-hidden="true"/> 搜 索</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <el-table
      v-loading="listLoading"
      ref="multipleTable"
      :data="list.data"
      :height="tableHeight"
      element-loading-text="正在加载中..."
      size="small"
      border
      fit
      highlight-current-row>
      <el-table-column align="center" label="ID" width="60">
        <template slot-scope="scope">{{ scope.$index + 1 }}</template>
      </el-table-column>
      <el-table-column label="IP地址" width="130">
        <template slot-scope="scope">
          {{ scope.row.ip }}
        </template>
      </el-table-column>
      <el-table-column label="操作人账号" width="100">
        <template slot-scope="scope">
          {{ scope.row.username }}
        </template>
      </el-table-column>
      <el-table-column label="操作人姓名" width="100">
        <template slot-scope="scope">
          {{ scope.row.realname }}
        </template>
      </el-table-column>
      <el-table-column label="学生名字" width="80">
        <template slot-scope="scope">
          {{ scope.row.student_name }}
        </template>
      </el-table-column>
      <el-table-column label="学生学号" width="130">
        <template slot-scope="scope">
          {{ scope.row.student_sno }}
        </template>
      </el-table-column>
      <el-table-column label="学生身份证号码" width="170">
        <template slot-scope="scope">
          {{ scope.row.student_idcode }}
        </template>
      </el-table-column>
      <el-table-column label="操作名称" width="180">
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
<!--      <el-table-column align="center" label="更新时间" width="200">-->
<!--        <template slot-scope="scope">-->
<!--          <i class="el-icon-time"/><span>{{ scope.row.update_time }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column
        fixed="right"
        label="操作"
        align="center"
        width="80">
        <template slot-scope="scope">
          <el-button size="mini" type="success" plain @click="handleChaKan(scope.$index, scope.row)"> 查看</el-button>
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
    <el-dialog v-dialogDrag :visible.sync="dialogStudentVisible" width="800px">
      <div slot="title"><i class="fa fa-cogs"/> 历史数据</div>
      <el-scrollbar style="height:400px;">
        <el-row>
          <el-col :span="12">
            <div>修改前：</div>
            <ol>
              <li :style="{'line-height':line_height,'color':StudentDataColor[key]}" v-for="(value,key) in oldStudentData" :key="key">{{ key }} : {{ value }}</li>
            </ol>
          </el-col>
          <el-col :span="12">
            <div>修改后：</div>
            <ol>
              <li :style="{'line-height':line_height,'color':StudentDataColor[key]}"  v-for="(value,key) in newStudentData" :key="key">{{ key }} : {{ value }}</li>
            </ol>
          </el-col>
        </el-row>
      </el-scrollbar>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogStudentVisible = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStudentLogLists } from '@/api/studentLog'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'

export default {
  data() {
    return {
      color:'#706f6f',
      line_height: '25px',
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
        keyword: '',
        username: '',
        starttime: '',
        endtime: '',
        ip: '',
        name: '',
        url: '',
        student_name: '',
        student_sno: '',
        student_idcode: ''
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
      },
      dialogStudentVisible: false,
      oldStudentData: {},
      newStudentData: {},
      StudentDataColor:{},
      studentData:[]
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
        getStudentLogLists(this.params).then(response => {
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
    },
    handleChaKan(index, row) {
      // this.oldStudentData = row.old_data

      this.oldStudentData = JSON.parse(row.old_data)
      this.newStudentData = JSON.parse(row.old_data)
      let newStuData = JSON.parse(row.new_data)
      let newStuKey = Object.keys(newStuData)
      for(let i = 0; i < newStuKey.length; i++){
        let key = newStuKey[i]
        this.newStudentData[key] = newStuData[key]
        if (this.newStudentData[key] == this.oldStudentData[key]){
          this.StudentDataColor[key]='gray';
        }else if(this.oldStudentData[key] == null && this.newStudentData[key] != ''){
          this.StudentDataColor[key]='red'
          // console.log('null && empty' + this.oldStudentData[key] + '--' + this.newStudentData[key])
        }else if(this.oldStudentData[key] == null && this.newStudentData[key] == ''){
          this.StudentDataColor[key]='gray';
        }else{
          // console.log(this.oldStudentData[key] + '--' + this.newStudentData[key])
          this.StudentDataColor[key]='red';
        }
      }
      this.dialogStudentVisible = true
    }
  }
}
</script>
