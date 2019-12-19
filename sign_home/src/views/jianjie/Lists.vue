<template>
    <div class="app-container container" style="height:100%;background-color: #fff;">
      <div v-for="(item, index) in ArticleLists" :key="item.id" class="article-item">
        {{ index + 1 }}. <router-link :to="{path:'/jianjie/detail', query: { id: item.id }}" >{{ item.title }}</router-link>
      </div>
    </div>
</template>

<script>
import { getArticlesByCate } from '@/api/article'
export default {
  data() {
    return {
      activeIndex: '2',
      ArticleLists: []
    }
  },
  computed: {
  },
  mounted() {
    this.fetchArticleLists()
  },
  methods: {
    fetchArticleLists() {
      getArticlesByCate({ name: '招生简介' }).then(response => {
        this.ArticleLists = response.data
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>
<style rel="stylesheet/scss" lang="scss" scoped>
  .app-container{
    padding:20px;
  }
  .article-item{
    font-size:16px;
    line-height: 32px;
  }
</style>
