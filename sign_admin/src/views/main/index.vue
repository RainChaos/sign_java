<template>
  <div class="app-container" style="background-color: #f0f2f5;padding:10px;">
    <el-scrollbar style="height: 100%;">
      <div style="overflow: hidden;padding-bottom:20px;">
        <el-row :gutter="5">
          <el-row :gutter="15" class="panel-group">
            <el-col :span="4" class="card-panel-col">
              <div class="card-panel" style="line-height:30px;font-size:16px;padding:20px;font-weight: bolder">
                <el-form :inline-message="true" size="mini">
                  <!-- <el-form-item class="studentInfo" label-width="60px" label="年级：" style="margin-bottom: 8px;">
                    <el-input v-model="configData.grade_name" disabled />
                  </el-form-item> -->
                  <el-form-item label="批次：" label-width="60px" style="margin-bottom: 0px;width: 100%;">
                    <el-select v-model="piciValue" placeholder="请选择季节批次" @change="handleChangePici">
                      <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                  </el-form-item>
                </el-form>
              </div>
            </el-col>
            <el-col :span="4" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="peoples" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">报名人数</div>
                  <count-to :start-val="0" :end-val="studentCountInfo.StudentCount" :duration="300"
                            class="card-panel-num"/>
                </div>
              </div>
            </el-col>
            <el-col :span="4" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="peoples" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">报到人数</div>
                  <count-to :start-val="0" :end-val="studentCountInfo.RegisterCount" :duration="300"
                            class="card-panel-num"/>
                </div>
              </div>
            </el-col>
            <el-col :span="4" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="peoples" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">报到男生</div>
                  <count-to :start-val="0" :end-val="studentCountInfo.RegisterNanCount" :duration="300"
                            class="card-panel-num"/>
                </div>
              </div>
            </el-col>
            <el-col :span="4" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-people">
                  <svg-icon icon-class="peoples" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">报到女生</div>
                  <count-to :start-val="0" :end-val="studentCountInfo.RegisterNvCount" :duration="300"
                            class="card-panel-num"/>
                </div>
              </div>
            </el-col>
            <el-col :span="4" class="card-panel-col">
              <div class="card-panel">
                <div class="card-panel-icon-wrapper icon-money">
                  <svg-icon icon-class="money" class-name="card-panel-icon"/>
                </div>
                <div class="card-panel-description">
                  <div class="card-panel-text">缴费人数</div>
                  <count-to :start-val="0" :end-val="studentCountInfo.StudentPayCount" :duration="300"
                            class="card-panel-num"/>
                </div>
              </div>
            </el-col>
          </el-row>
        </el-row>
        <el-row>
          <el-row :gutter="15">
            <!--<el-col :span="24">-->
            <!--<div class="chart-wrapper">-->
            <!--<pie-chart ref="PieChart" :legend-data="xAxisData" :pie-data="PieData2" height="900px"/>-->
            <!--</div>-->
            <!--</el-col>-->

            <el-col :span="24">
              <div class="chart-wrapper">
                <xi-bar-chart ref="XiBarChart" :x-axis-data="xAxisData2" :nan-count="NanCount2" :nv-count="NvCount2"
                              height="200px" id="xibu"/>
              </div>
            </el-col>

            <el-col :span="24">
              <div class="chart-wrapper">
                <bar-chart ref="BarChart" :x-axis-data="xAxisData" :nan-count="NanCount" :nv-count="NvCount"
                           :height="unit_nameHeight" id="unit_name"/>
              </div>
            </el-col>

          </el-row>
        </el-row>
        <!--<el-row :gutter="15">-->
        <!--<el-col :span="12">-->
        <!--<server-info/>-->
        <!--</el-col>-->
        <!--</el-row>-->
      </div>
    </el-scrollbar>
  </div>
</template>

<script>
  import CountTo from 'vue-count-to'
  import ServerInfo from './components/ServerInfo'

  import BarChart from './components/charts/BarChart'
  import XiBarChart from './components/charts/XiBarChart'
  import PieChart from './components/charts/PieChart'
  import { setCurrentUrl, getCurrentUrl } from '@/utils/auth.js'
  import {
    getCountZyLists
  } from '@/api/main'
  import {
    getCountXiLists
  } from '@/api/main'
  import {
    getCountStudent,
    getConfigData
  } from '@/api/main'
  import {
    getPiciLists
  } from '@/api/pici'

  export default {
    name: 'Main',
    components: {
      CountTo,
      ServerInfo,
      BarChart,
      PieChart,
      XiBarChart
    },
    data() {
      return {
        unit_nameHeight: '1200px',
        studentCountInfo: {
          StudentCount: 0,
          RegisterCount: 0,
          RegisterNanCount: 0,
          RegisterNvCount: 0,
          StudentPayCount: 0
        },
        configData: {
          pici_name: '',
          grade_name: ''

        },
        piciValue: 0,
        piciLists: [],

        chart: null,
        xAxisData: [],
        NanCount: [],
        NvCount: [],
        SumCount: [],
        PieData: [],
        xAxisData2: [],
        NanCount2: [],
        NvCount2: [],
        SumCount2: [],
        PieData2: []

      }
    },
    //keep-alive 组件激活时调用 的钩子
    activated() {
      setCurrentUrl(this.$route.path) //此处为main
      var _this = this
      this.fetchPiciData()
      this.fetchStudentCount()
    },
    mounted: function() {
      setCurrentUrl(this.$route.path)
      var _this = this
      _this.piciValue = _this.$store.getters.currentPici
      this.fetchPiciData()
      this.fetchStudentCount()
      _this.fetchZyCount()
      _this.fetchXiCount()
    },
    methods: {
      handleChangePici() {
        // this.updatePiciDiv()
        this.fetchStudentCount()
        this.fetchZyCount()
        this.fetchXiCount()

      },
      fetchPiciData() {
        getPiciLists().then(response => {
          this.piciLists = response.data.data
        }).catch(error => {
          console.log(error)
        })
      },
      fetchStudentCount() {
        getCountStudent({
          pici_id: this.piciValue
        }).then(response => {
          var data = response.data
          this.studentCountInfo = data
        }).catch(error => {
          console.log(error)
        })
      },
      fetchConfigData() {
        getConfigData().then(response => {
          var data = response.data
          this.configData = data
        }).catch(error => {
          console.log(error)
        })
      },
      fetchZyCount() {
        this.xAxisData = []
        this.SumCount = []
        this.NanCount = []
        this.NvCount = []
        this.PieData = []

        getCountZyLists({
          pici_id: this.piciValue
        }).then(response => {
          // console.log(response.data)
          var resizeEvent = new Event('resize')

          var lists = response.data
          lists.forEach(item => {
            this.xAxisData.push(item.unit_name)
            this.SumCount.push(item.student_count)
            this.NanCount.push(item.student_nan_count)
            this.NvCount.push(item.student_nv_count)
            var data = {
              name: item.unit_name,
              value: item.student_count
            }
            this.PieData.push(data)
          })
          this.$refs.BarChart.initChart()
          if (lists.length > 15) {
            this.unit_nameHeight = lists.length * 18 + 'px'
          } else if (lists.length > 10) {
            this.unit_nameHeight = lists.length * 25 + 'px'
          } else if (lists.length > 5) {
            this.unit_nameHeight = lists.length * 30 + 'px'
          } else if (lists.length > 2) {
            this.unit_nameHeight = lists.length * 42 + 'px'
          } else if (lists.length > 1) {
            this.unit_nameHeight = lists.length * 60 + 'px'
          } else {
            this.unit_nameHeight = lists.length * 100 + 'px'
          }

          window.dispatchEvent(resizeEvent)
          // this.$refs.PieChart.initChart()
        }).catch(error => {
          console.log(error)
        })
      },

      fetchXiCount() {
        this.xAxisData2 = []
        this.SumCount2 = []
        this.NanCount2 = []
        this.NvCount2 = []
        this.PieData2 = []

        getCountXiLists({
          pici_id: this.piciValue
        }).then(response => {
          var lists = response.data
          lists.forEach(item => {
            // item = JSON.parse(item);
            this.xAxisData2.push(item.unit_name)
            this.SumCount2.push(item.student_count)
            this.NanCount2.push(item.student_nan_count)
            this.NvCount2.push(item.student_nv_count)
            var data = {
              name: item.unit_name,
              value: item.student_count
            }
            this.PieData2.push(data)
          })
          this.$refs.XiBarChart.initChart()
          // this.$refs.PieChart.initChart()
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

  .panel-group {
    margin-top: 0;

    .card-panel-col {
      margin-bottom: 15px;
    }

    .card-panel {
      min-height: 108px;
      font-size: 12px;
      position: relative;
      overflow: hidden;
      color: #666;
      background: #fff;
      box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
      border-color: rgba(0, 0, 0, .05);

      .card-panel-icon-wrapper {
        color: #fff;
      }

      .icon-people {
        background: #40c9c6;
      }

      .icon-message {
        background: #36a3f7;
      }

      .icon-money {
        background: #f4516c;
      }

      .icon-shopping {
        background: #34bfa3;
      }

      .card-panel-icon-wrapper {
        float: left;
        margin: 18px 0 0 5px;
        padding: 16px;
        transition: all 0.38s ease-out;
        border-radius: 6px;
      }

      .card-panel-icon {
        float: left;
        font-size: 34px;
      }

      .card-panel-description {
        float: left;
        font-weight: bold;
        margin: 26px 1px 26px 5px;

        .card-panel-text {
          line-height: 18px;
          color: rgba(0, 0, 0, 0.45);
          font-size: 16px;
          margin-bottom: 12px;
        }

        .card-panel-num {
          font-size: 20px;
        }
      }
    }
  }
</style>
