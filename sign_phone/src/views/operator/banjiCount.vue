<template>
  <div class="main-page">
    <op-header web-title="2019秋季招生数据实时监控"></op-header>
    <scroller lock-x scrollbar-y
              height="100%"
              use-pulldown
              :pulldown-config="pulldownDefaultConfig"
              ref="scroller"
              style="font-size:13px;color:#fff;"
              @on-pulldown-loading="refresh">
      <div style="overflow: auto;background:#fff;margin:0px 5px;padding-bottom: 50px">
        <div style="font-size:12px;color:#fff;background:#666;text-align: center;line-height: 24px;overflow: hidden;">
          截至当前时间：{{NowTime}}
        </div>
        <flexbox style="background:#cf9236;color:#fff;overflow: hidden;">
          <flexbox-item class="count-item">总人数：<span class="count-number">{{CountNumber}}</span> 人</flexbox-item>
          <flexbox-item class="count-item">总交费：<span class="count-number">{{PayNumber}}</span> 人</flexbox-item>
          <flexbox-item class="count-item">总报到：<span class="count-number">{{RegisterNumber}}</span> 人</flexbox-item>
        </flexbox>

        <div class="title-ct">各班级列表数据</div>
        <el-table
            ref="ChargeItemsTable"
            :data="unitCountLists"
            fit
            border
            size="mini">
          <el-table-column fixed="left" label="班级名称">
            <template slot-scope="scope">{{ scope.row.unit_name }}</template>
          </el-table-column>
          <el-table-column label="报名人数">
            <template slot-scope="scope">{{ scope.row.student_count }} [{{scope.row.student_count_precent}}]</template>
          </el-table-column>
          <el-table-column label="已缴费人数">
            <template slot-scope="scope">{{ scope.row.student_pay_count }} [{{scope.row.student_pay_count_precent}}]</template>
          </el-table-column>
          <el-table-column label="已报到人数">
            <template slot-scope="scope">{{ scope.row.student_register_count }} [{{scope.row.student_register_count_precent}}]</template>
          </el-table-column>
        </el-table>
        <!--<div class="title-ct">各班级报名/交费/报到人数统计图</div>-->
        <!--<bar-chart style="overflow: hidden;"-->
                   <!--ref="BarChart"-->
                   <!--:x-axis-data="BmXAxisData"-->
                   <!--:count-number="CountNumberBar"-->
                   <!--:register-count-number="RegisterCountNumberBar"-->
                   <!--:pay-count-number="PayCountNumberBar"-->
                   <!--height="360px"/>-->
        <!--<div class="title-ct">各班级报名人数占比图</div>-->
        <!--<pie-chart style="overflow: hidden;" ref="PieChart" :legend-data="BmXAxisData" :pie-data="BmPieData" height="360px"/>-->
      </div>
    </scroller>
  </div>
</template>

<script>
  import { XHeader,Flexbox, FlexboxItem, Scroller, Divider,XButton } from 'vux'
  import { Loading } from 'element-ui'
  import { countBanjiLists } from '@/api/studentCount'
  import PieChart from './components/PieChart'
  import BarChart from './components/BarChart'
  import OpHeader  from './components/OpHeader'
  import store from '@/store'
  const pulldownDefaultConfig = {
    content: '下拉刷新',
    height: 40,
    autoRefresh: false,
    downContent: '下拉刷新',
    upContent: '释放后刷新',
    loadingContent: '正在刷新...',
    clsPrefix: 'xs-plugin-pulldown-'
  }
  export default {
    components: {
      XButton,
      Scroller,
      Divider,
      XHeader,
      Flexbox,
      FlexboxItem,
      PieChart,
      BarChart,
      OpHeader
    },
    data () {
      return {
        pulldownDefaultConfig: pulldownDefaultConfig,
        NowTime:'',
        unitCountLists: [ ],
        CountNumber:0,
        PayNumber:0,
        RegisterNumber:0,
        BmXAxisData: [],
        BmPieData: [],
        CountNumberBar: [],
        PayCountNumberBar: [],
        RegisterCountNumberBar: [],
        loadingInstance: null
      }
    },
    mounted(){
      window.setInterval(this.fetchNowTime,1000);
      this.fetchData();
      //window.setInterval(this.fetchData,3000);
    },
    methods: {
      refresh() {
        this.fetchData()
      },
      fetchNowTime(){
        var today=new Date()
        var month = parseInt( today.getMonth() ) +1
        var s=today.getFullYear()+"-"+month+"-"+today.getDate()+" "+today.getHours()+":"+today.getMinutes()+":"+today.getSeconds();
        this.NowTime = s
      },
      fetchData() {
        return new Promise((resolve, reject) => {
          this.loadingInstance = Loading.service({});
          var _this = this
          countBanjiLists(this.params).then(response => {
            var listsData = response.data
            var CountNumber = 0
            var PayNumber = 0
            var RegisterNumber = 0
            listsData.forEach(row => {
              CountNumber += row.student_count
              PayNumber += row.student_pay_count
              RegisterNumber += row.student_register_count
            })
            _this.CountNumber = CountNumber
            _this.PayNumber = PayNumber
            _this.RegisterNumber = RegisterNumber

            _this.BmXAxisData = []
            _this.BmPieData = []
            this.CountNumberBar = []
            this.PayCountNumberBar = []
            this.RegisterCountNumberBar = []
            listsData.forEach((row, index) => {
              _this.BmXAxisData.push(row.banji)
              var data = {
                name: row.banji,
                value: row.student_count
              }
              this.CountNumberBar.push(row.student_count)
              this.PayCountNumberBar.push(row.student_pay_count)
              this.RegisterCountNumberBar.push(row.student_register_count)
              listsData[index]["student_count_precent"] = (row.student_count / CountNumber * 100).toFixed() + '%'
              listsData[index]["student_pay_count_precent"] = (row.student_pay_count / CountNumber * 100).toFixed() + '%'
              listsData[index]["student_register_count_precent"] = (row.student_register_count / CountNumber * 100).toFixed(2) + '%'
              _this.BmPieData.push(data)
            })
            _this.unitCountLists = listsData
            // _this.$nextTick(() => {
            //   _this.$refs.BarChart.initChart()
            //   _this.$refs.PieChart.initChart()
            //   _this.$refs.scroller.donePulldown()
            //   _this.$refs.scroller.reset({top: 0})
            //
            // });
              this.loadingInstance.close();
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      }
    }
  }
</script>

<style scoped>
  .title-ct{
    padding-left:10px;
    line-height: 30px;
    background: #0068b7;
    color:#fff;
    font-size:13px;
  }
  .count-item{
    padding:10px;
    font-size:13px;
  }
  .count-number{
    font-weight: bolder;font-size:16px;
  }
</style>
