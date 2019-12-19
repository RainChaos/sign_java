<template>
  <el-scrollbar style="height:100%;overflow-x:hidden;">
    <div class="app-container container" style="height:100%;">
      <el-scrollbar style="height:100%;overflow-x:hidden;">
        <div class="article-content" v-html="Article.content"/>
      </el-scrollbar>
    </div>
  </el-scrollbar>
</template>

<script>
import { getArticleById } from '@/api/article'
export default {
  data() {
    return {
      Article: ''
    }
  },
  computed: {
  },
  mounted() {
    var id = this.$route.query.id
    this.fetchArticle(id)
  },
  methods: {
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
<style rel="stylesheet/scss" lang="scss" scoped>
.article-content{
  padding:20px;
}
</style>
