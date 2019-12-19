<template>
  <view-box>
    <router-view/>
    <div>
      <tabbar>
        <tabbar-item :link="AdmissionsProfile">
          <img slot="icon" src="../../../static/tabbar/zhaosheng.png">
          <span slot="label">招生简介</span>
        </tabbar-item>
        <tabbar-item :link="WebSiteAddress">
          <img slot="icon" src="../../../static/tabbar/guanfang.png">
          <span slot="label">官方网站</span>
        </tabbar-item>
        <tabbar-item @on-item-click="chatQQ">
          <img slot="icon" src="../../../static/tabbar/zaixian.png">
          <span slot="label">在线咨询</span>
        </tabbar-item>
        <tabbar-item @on-item-click="handleShare">
          <img slot="icon" src="../../../static/tabbar/fenxiang.png">
          <span slot="label">教师登陆</span>
        </tabbar-item>
        <tabbar-item @on-item-click="admissionInfo">
          <img slot="icon" src="../../../static/tabbar/xuzhi.png">
          <span slot="label">入学须知</span>
        </tabbar-item>
      </tabbar>
    </div>
  </view-box>
</template>

<script>
  import { ViewBox,Tabbar, TabbarItem, Group, Cell } from 'vux'
  import { getWebSiteName ,getAdmissionsProfile,getWebSiteAddress} from '@/api/main'
export default {
  name: 'Layout',
  components: {
    ViewBox,
    Tabbar,
    TabbarItem,
    Group,
    Cell
  },
  data () {
    return {
      AdmissionsProfile:'',
      WebSiteAddress:''
    }
  },
  created(){
    getWebSiteName().then(response => {
      document.title = response.data + '报名平台'
    })
    getAdmissionsProfile().then(response => {
      this.AdmissionsProfile= response.data
    })
    getWebSiteAddress().then(response => {
      this.WebSiteAddress = response.data
    })
  },
  methods: {
    handleShare() {
      this.$router.push({ path: '/login' })
    },
    chatQQ(){
        this.$router.push({ path: '/contactOnline' })
    },
      admissionInfo(){
          this.$router.push({ path: '/admissionInfo' })
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
