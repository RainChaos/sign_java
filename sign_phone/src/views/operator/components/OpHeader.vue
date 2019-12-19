<template>
  <x-header style="background-color:#0068b7;">
    {{WebTitle}}
    <el-button type="text" style="padding:0px;" slot="right" @click="logout">退出</el-button>
  </x-header>
</template>

<script>
  import { XHeader } from 'vux'
  export default {
    name: "OpHeader",
    components: {
      XHeader
    },
    props: {
      WebTitle: {
        type: String,
        default: '桂林佳朋信息科技公司'
      }
    },
    methods:{
      logout(){
        this.$router.push({ path: '/login'})
      }
    }
  }
</script>
