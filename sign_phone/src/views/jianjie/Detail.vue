<template>
  <div class="main-page">
    <x-header style="background-color:#0068b7;">
      {{Article.title}}
      <router-link slot="right" :to="{path:'/'}">退出</router-link>
    </x-header>
    <div class="form-content">
      <el-scrollbar style="height:100%;overflow-x:hidden;">
        <div class="article-content" v-html="Article.content"></div>
      </el-scrollbar>
    </div>
  </div>
</template>

<script>
  import { XHeader } from 'vux'
  import { getArticleById } from '@/api/article'
  export default {
    components:{
      XHeader
    },
    data(){
      return {
        Article: {},
      }
    },
    mounted(){
      var id = this.$route.query.id
      this.fetchArticle(id)
    },
    methods:{
      fetchArticle(id) {
        getArticleById({ id: id }).then(response => {
          this.Article = response.data
        }).catch(error => {
          console.log(error)
        })
      }
    }
  }
</script>

<style scoped>
  .article-content{
    height:100%;
    overflow: hidden;
    padding-top:20px;
  }
</style>