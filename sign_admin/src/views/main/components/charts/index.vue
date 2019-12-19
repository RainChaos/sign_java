<template>
  <el-row :gutter="15">
    <el-col :span="12">
      <div class="chart-wrapper">
        <bar-chart ref="BarChart" :x-axis-data="xAxisData" :nan-count="NanCount" :nv-count="NvCount" height="360px"/>
      </div>
    </el-col>
    <el-col :span="12">
      <div class="chart-wrapper">
        <pie-chart ref="PieChart" :legend-data="xAxisData" :pie-data="PieData" height="360px"/>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import BarChart from './BarChart'
import PieChart from './PieChart'
import { getCountZyLists } from '@/api/main'
export default {
  name: 'Index',
  components: {
    BarChart,
    PieChart
  },
  data() {
    return {
      chart: null,
      xAxisData: [],
      NanCount: [],
      NvCount: [],
      SumCount: [],
      PieData: []
    }
  },
  mounted() {
    this.fetchZyCount()
  },
  methods: {
    fetchZyCount() {
      getCountZyLists().then(response => {
        var lists = response.data
        lists.forEach(item => {
          this.xAxisData.push(item.zhuanye)
          this.SumCount.push(item.student_count)
          this.NanCount.push(item.student_nan_count)
          this.NvCount.push(item.student_nv_count)
          var data = {
            name: item.zhuanye,
            value: item.student_count
          }
          this.PieData.push(data)
        })
        this.$refs.BarChart.initChart()
        this.$refs.PieChart.initChart()
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .chart-wrapper {
    background: #fff;
    padding: 16px 16px 0;
    margin-bottom: 20px;
  }
</style>
