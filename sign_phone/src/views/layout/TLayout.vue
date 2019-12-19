<template>
  <view-box>
    <router-view/>
    <div>
      <tabbar>
        <tabbar-item @on-item-click="handleZhuanYeCount">
          <img slot="icon" src="../../../static/tabbar/zhaosheng.png">
          <span slot="label">专业数据</span>
        </tabbar-item>
        <tabbar-item @on-item-click="handleBanjiCount">
          <img slot="icon" src="../../../static/tabbar/bar.png">
          <span slot="label">班级数据</span>
        </tabbar-item>
        <tabbar-item @on-item-click="handleBanji">
          <img slot="icon" src="../../../static/tabbar/fenxiang.png">
          <span slot="label">调整班级</span>
        </tabbar-item>
        <tabbar-item @on-item-click="handleLists">
          <img slot="icon" src="../../../static/tabbar/guanfang.png">
          <span slot="label">学生列表</span>
        </tabbar-item>
      </tabbar>
    </div>
  </view-box>
</template>

<script>
  import { ViewBox,Tabbar, TabbarItem, Group, Cell } from 'vux'
  import { getWebSiteName } from '@/api/main'
  import store from '@/store'
export default {
  name: 'TLayout',
  components: {
    ViewBox,
    Tabbar,
    TabbarItem,
    Group,
    Cell
  },
  created(){
    store.dispatch('GetInfo').then(res => {
      document.title = res.data.webSiteName + '-管理员'
    }).catch(error=>{
      this.$router.replace({ path: '/login' })
    })
  },
  methods: {
    handleZhuanYeCount() {
      this.$router.push({ path: '/t/zhuanyeCount' })
    },
    handleBanjiCount() {
      this.$router.push({ path: '/t/banjiCount' })
    },
    handleLists() {
      this.$router.push({ path: '/t/studentLists' })
    },
      handleBanji(){
          this.$router.push({ path: '/t/studentBanji' })
      }
  }
}
</script>

<style>
  .weui-tabbar__icon {
    display: inline-block;
    width: 40px !important;
    height: 35px !important;
  }
</style>
