<template>
  <div class="app-container" style="height: 100%;">
    <el-row class="actionBtn">
      <el-form ref="form" :inline="true" :model="params" label-width="80px" size="mini">
        <el-form-item label="批次：" label-width="55px" style="margin-bottom: 0px;">
          <el-select v-model="params.pici_id" placeholder="请选择季节批次" style="width:180px">
            <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>

        <el-form-item label="字段：" label-width="55px" style="margin-bottom: 0px;">
          <el-select v-model="params.field_name" placeholder="" style="width:180px">
            <el-option v-for="item in fieldList" :key="item.field_name" :label="item.describe" :value="item.field_name"/>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="exportLoading" @click="handleExport"><i class="fa fa-table" aria-hidden="true"/> 导出</el-button>
          <el-button type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
        </el-form-item>
      </el-form>
    </el-row>
    <el-row style="height:calc(100% - 60px); " >
      <el-table
        v-loading="listLoading"
        :data="listsData"
        border
        show-summary
        element-loading-text="正在加载中..."
        fit>
        <el-table-column :label="params.fieldName" prop="code" width="150" />
        <el-table-column label="总数" prop="count" width="100" align="center"/>
        <el-table-column label="男生" prop="nan_count" width="100" align="center" />
        <el-table-column label="女生" prop="nv_count" width="100" align="center" />
        <el-table-column label="已缴费" prop="pay_count" width="100" align="center" />
        <el-table-column label="已缴费男生" prop="pay_nan_count" width="100" align="center" />
        <el-table-column label="已缴费女生" prop="pay_nv_count" width="100" align="center" />
        <el-table-column label="已报到" prop="register_count" width="100" align="center" />
        <el-table-column label="已报到男生" prop="register_nan_count" width="150" align="center" />
        <el-table-column label="已报到女生" prop="register_nv_count" width="150" align="center" />
      </el-table>
    </el-row>
  </div>
</template>

<script>
import { countZsrLists, exportZsrLists, getFieldNameStatisticsConfig, countFieldNameList, exportFieldNameStatistics} from '@/api/studentCount'
import { getPiciLists } from '@/api/pici'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      exportLoading:false,
      listLoading: false,
      listsData: [],
      piciLists: [],
      params: { pici_id: 0, field_name: '',fieldName: '' },
      fieldList: []

    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.params.pici_id = this.$store.getters.currentPici
    this.fetchPiciData()
    this.fetchFieldNameStatisticsConfig()
  },
  created() {
    setCurrentUrl(this.$route.path)
    this.params.pici_id = this.$store.getters.currentPici
    this.fetchPiciData()
    this.fetchFieldNameStatisticsConfig()
  },
  methods: {
    fieldChange(){
      let obj = {};
      obj = this.fieldList.find((item)=>{//这里的userRoleList就是上面遍历的数据源
        return item.field_name === this.params.field_name;//筛选出匹配数据
      });
      this.params.fieldName = obj.describe
    },
    fetchFieldNameStatisticsConfig(){
      getFieldNameStatisticsConfig().then(response => {
        this.fieldList = response.data
        this.params.field_name = this.fieldList[0].field_name
        this.params.fieldName = this.fieldList[0].describe
        // console.log(this.params.fieldName)
        this.fetchData()
      }).catch(error => {
        console.log(error)
      })
    },
    fetchPiciData() {
      getPiciLists().then(response => {
        this.piciLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchData() {
      this.listLoading = true
      this.fieldChange()
      countFieldNameList(this.params).then(response => {
        this.listsData = response.data
        this.listLoading = false
      }).catch(error => {
        console.log(error)
        this.listLoading = false
      })
    },
    handleExport() {
      this.exportLoading=true
      exportFieldNameStatistics(this.params).then(response => {
        let url = process.env.BASE_API + response.data
        this.exportLoading=false
        this.$alert('<a style="color:#409EFF;font-weight:900;text-align: center" target="_blank" href="' + url + '">点击下载</a>', '导出下载', {
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
