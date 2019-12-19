<template>
  <div :class="className" :style="{height:height,width:width}"/>
</template>

<script>
import echarts from 'echarts'
require('echarts/theme/macarons') // echarts theme
import { debounce } from '@/utils'

const animationDuration = 100

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
    nanCount: {
      type: Array,
      default: []
    },
    nvCount: {
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
      /*var xbchart = echarts.init(document.getElementById('xibu'))*/
      this.chart = echarts.init(this.$el, 'macarons')
      this.chart.setOption({
      title: {
          text: '系部报名人数统计图'
        },
        legend: {
          data: ['男生人数', '女生人数']
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: { // 坐标轴指示器，坐标轴触发有效
            type: 'shadow' // 默认为直线，可选为：'line' | 'shadow'
          }
        },
        grid: {
          left: '2%',
          right: '2%',
          bottom: '3%',
          containLabel: true
        },
        yAxis: [{
          inverse: true,
          type: 'category',
          data: this.xAxisData,
          axisTick: {
            alignWithLabel: true
          },
          axisLabel: { 'interval': 0, rotate: 0 }
        }],
        xAxis: [{
          type: 'value',
          axisTick: {
            show: false
          },
          axisLabel: {
            formatter: '{value} (人)'
          }
        }],
        /*        dataZoom: [
          {
            show: true,
            start: 0,
            end: 90
          }
        ],*/
        series: [{
          name: '男生人数',
          type: 'bar',
          stack: 'vistors',
          barWidth: '100%',
          data: this.nanCount,
          label: {
            normal: {
              show: true,
              position: 'inside'
            }
          },
          animationDuration
        }, {
          name: '女生人数',
          type: 'bar',
          stack: 'vistors',
          barWidth: '100%',
          data: this.nvCount,
          label: {
            normal: {
              show: true,
              position: 'inside'
            }
          },
          animationDuration
        }
        ]
      })
    }
  }
}
</script>
