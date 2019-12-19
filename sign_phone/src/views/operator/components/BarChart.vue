<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

const animationDuration = 2000

export default {
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '100%'
    },
    height: {
      type: String,
      default: '350px'
    },
    xAxisData: {
      type: Array,
      default: []
    },
    CountNumber: {
      type: Array,
      default: []
    },
    PayCountNumber: {
      type: Array,
      default: []
    },
    RegisterCountNumber: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      chart: null
    }
  },
  mounted() {
    this.__resizeHandler = debounce(() => {
      if (this.chart) {
        this.chart.resize()
      }
    }, 100)
    window.addEventListener('resize', this.__resizeHandler)
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    window.removeEventListener('resize', this.__resizeHandler)
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      var seriesLabel = {
        normal: {
          show: true,
          textBorderColor: '#333',
          textBorderWidth: 3
        }
      }
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
        legend: {
          data: ['报名人数', '交费人数','报到人数']
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: '2%',
          containLabel: true
        },
        yAxis: [{
          type: 'category',
          data: this.xAxisData,
          axisTick: {
            alignWithLabel: true
          },
          axisLabel: { 'interval': 0, rotate: 0 }
        }],
        xAxis: [{
          type: 'value',
          name: '人',
          axisLabel: {
            formatter: '{value}'
          }
        }],
        dataZoom: [
            {
              show: true,
              start: 0,
              end: 90
            }
        ],
        series: [{
          name: '报名人数',
          type: 'bar',
          barWidth: '100%',
          stack: 'vistors',
          data: this.CountNumber,
          label: seriesLabel,
          animationDuration
        }, {
          name: '交费人数',
          type: 'bar',
          //stack: 'vistors',
          barWidth: '10%',
          data: this.PayCountNumber,
          label: seriesLabel,
          animationDuration
        },
          {
            name: '报到人数',
            type: 'bar',
            //stack: 'vistors',
            barWidth: '10%',
            data: this.RegisterCountNumber,
            label: seriesLabel,
            animationDuration
          }
        ]
      })
    }
  }
}
</script>
