<template>
  <div class="main-page">
    <x-header style="background-color:#0068b7;">
      招生简介
      <router-link slot="right" :to="{path:'/'}">退出</router-link>
    </x-header>
    <div class="form-content">
      <div class="article-item" v-for="(item, index) in ArticleLists">
        {{index + 1}}. <router-link  :to="{path:'/jianjie/detail', query: { id: item.id }}" >{{item.title}}</router-link>
      </div>
    </div>
  </div>
</template>

<script>
  import { XHeader } from 'vux'
  import { getArticlesByCate } from '@/api/article'
  export default {
    components:{
      XHeader
    },
    data(){
      return {
        ArticleLists: [],
      }
    },
    mounted(){
      this.fetchTopInfoContent()
    },
    methods:{
      fetchTopInfoContent() {
        getArticlesByCate({ name: '招生简介' }).then(response => {
          this.ArticleLists = response.data
        }).catch(error => {
          console.log(error)
        })
      },
    }
  }
</script>

<style scoped>
 .form-content{
   padding:20px;
 }
  .article-item{
    font-size:16px;
    line-height: 32px;
  }
</style>