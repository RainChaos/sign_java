<template>
  <el-row style="background-color: #409EFF;margin-bottom: 2px;">
    <div class="container">
      <el-menu
        :default-active="activeIndex"
        background-color="#409EFF"
        text-color="#FFF"
        active-text-color="#FFF"
        style="height: 55px;"
        mode="horizontal">
        <template>
          <div class="top-logo-ct">
            <img style="max-height:55px;" :src="logoUrl">
          </div>
        </template>
        <el-menu-item class="nav-item-right" index="1" @click="handleLogout">我要报名</el-menu-item>
        <el-menu-item class="nav-item-right" index="2" @click="handleHelp">招生简介</el-menu-item>
      </el-menu>
    </div>
  </el-row>
</template>
<script>
  import { getLOGOIMG,getAdmissionsProfile} from '@/api/main'
  export default {
  name: 'AppHeader',
  data() {
    return {
      activeIndex: '1',
      logoUrl:''
    }
  },
    mounted() {
      this.getLOGOIMG()
    },
  methods: {
    getLOGOIMG(){
      getLOGOIMG().then(reponse => {
        console.log(reponse.data)
        this.logoUrl = process.env.BASE_API+reponse.data
      }).catch(error => {
        console.log(error)
      })
    },
    handleLogout() {
      this.$router.push({ path: '/' })
    },
    handleHelp() {
      // this.$router.push({ path: '/jianjie/lists' })
      getAdmissionsProfile().then(response => {
        location.href = response.data
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
    .nav-item-right{
      color:#ececec;
      font-size: 14px;
      float: right;
      cursor:pointer;
    }
    .nav-item-right:hover{
      color:#FFF;
    }
    .top-logo-ct{
      min-width:220px;
      float: left !important;
      margin-right:20px;
    }
    .top-title-ct{
      float: left !important;
      padding-top:20px;
      line-height: 40px;
      font-weight: 900;
      font-size:16px;
      color:#dddddd;
      letter-spacing: 2px;
    }
    .el-menu--horizontal {
      border-right: none;
      border-bottom: none !important;
    }
</style>

