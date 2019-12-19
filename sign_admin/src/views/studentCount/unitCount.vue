<template>
  <div class="app-container" style="height: 100%;">
    <el-row class="actionBtn">
      <el-form ref="form" :inline="true" :model="params" label-width="80px" size="mini">
        <el-form-item label="批次：" label-width="55px" style="margin-bottom: 0px;">
          <el-select v-model="params.pici_id" placeholder="请选择季节批次" style="width:180px">
            <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item label="类型：" label-width="55px" style="margin-bottom: 0px;">
          <el-select v-model="params.type" placeholder="" style="width:180px">
            <el-option v-for="item in typeList" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item>

          <el-button type="success" @click="search"><i class="el-icon-refresh" aria-hidden="true"/> 搜 索</el-button>
          <el-button type="primary" :loading="exportLoading" @click="handleExport"><i class="fa fa-table" aria-hidden="true"/> 导出</el-button>

        </el-form-item>
      </el-form>
    </el-row>
    <el-row style="height:calc(100% - 100px); " >
    <!--<el-row style="margin-top: 10px;">-->
      <el-table
        v-loading="listLoading"
        :data="listsData"
        border
        show-summary
        element-loading-text="正在加载中..."
        fit>
        <el-table-column label="校区" prop="school" width="200" />
         <el-table-column label="院系/专业" prop="unit_name" width="400" />
        <el-table-column label="总数" prop="student_count" width="100" align="center"/>
        <el-table-column label="男生" prop="student_nan_count" width="100" align="center" />
        <el-table-column label="女生" prop="student_nv_count" width="100" align="center" />
        <el-table-column label="已缴费" prop="student_pay_count" width="100" align="center" />
        <el-table-column label="已缴费男生" prop="student_nan_pay_count" width="100" align="center" />
        <el-table-column label="已缴费女生" prop="student_nv_pay_count" width="100" align="center" />
        <el-table-column label="已报到" prop="student_register_count" width="100" align="center" />
        <el-table-column label="已报到男生" prop="student_nan_register_count" width="100" align="center" />
        <el-table-column label="已报到女生" prop="student_nv_register_count" width="100" align="center" />
      </el-table>
    </el-row>


  </div>
</template>

<script>
import { countZyLists, exportZyLists } from '@/api/studentCount'
import { getPiciLists } from '@/api/pici'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
import {
  getCountZyLists,getCountClassLists
} from '@/api/main'
export default {
  data() {
    return {
      exportLoading:false,
      listLoading: false,
      listsData: [],
      piciLists: [],
      params: { pici_id: 0, type: 3 },
      typeList: [
        {id: 3, name: '按系'},
        {id: 4, name: '按专业'},
        {id: 5, name: '按班级'}
      ]
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    var _this = this
    _this.params.pici_id = _this.$store.getters.currentPici
    this.fetchPiciData()
    _this.fetchData()
  },
  created() {
    setCurrentUrl(this.$route.path)
    var _this = this
    _this.params.pici_id = _this.$store.getters.currentPici
    this.fetchPiciData()
    _this.fetchData()
  },
  methods: {
    fetchPiciData() {
      getPiciLists().then(response => {
        this.piciLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    search(){
      if(this.params.type == 3){
        this.fetchData()
      }else if(this.params.type == 4){
        this.fetchMajorData()
      }else if(this.params.type == 5){
        this.fetchClassData()
      }
    },
    fetchData() {
      this.listLoading = true
      countZyLists(this.params).then(response => {
        this.listsData = response.data
        this.listLoading = false
      }).catch(error => {
        console.log(error)
        this.listLoading = false
      })
    },
    fetchClassData() {
      this.listLoading = true
      getCountClassLists(this.params).then(response => {
        this.listsData = response.data
        this.listLoading = false
      }).catch(error => {
        console.log(error)
        this.listLoading = false
      })
    },
    fetchMajorData() {
      this.listLoading = true
      getCountZyLists(this.params).then(response => {
        this.listsData = response.data
        this.listLoading = false
      }).catch(error => {
        console.log(error)
        this.listLoading = false
      })
    },
    handleExport() {
      this.exportLoading=true
      exportZyLists(this.params).then(response => {
        let url = process.env.BASE_API + response.data
        this.exportLoading=false
        this.$alert('<a style="color:#409EFF;font-weight:900;text-align: center" target="_blank" href="' + url + '">点击下载导出的表格</a>', '导出下载', {
          dangerouslyUseHTMLString: true,
          type: 'warning'
        })
      }).catch(error => {
        this.exportLoading=false
        console.log(error) })
    }
  }
}
</script>

<style scoped>

</style>
