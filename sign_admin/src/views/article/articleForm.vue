<template>
  <div class="app-container" style="background-color: #ececec;">
    <el-row style="padding-bottom: 100px;">
      <el-col :span="18" :offset="3" style="background-color: #FFF;padding: 20px 0px;">
        <el-form ref="ArticleForm" :label-position="labelPosition" :rules="rules" :label-width="formLabelWidth" :model="formData" size="small">
          <el-row style="padding:20px;">
            <el-col :span="18">
              <el-form-item :label-width="formLabelWidth" label="文章分类：" prop="cid">
                <el-cascader
                  v-model="formData.cid"
                  :options="typeTreeLists"
                  :props="optionsProps"
                  style="width:100%"/>
              </el-form-item>
              <el-form-item :label-width="formLabelWidth" label="标题：" prop="code">
                <el-input v-model="formData.title" auto-complete="off" placeholder="请输入文章的标题"/>
              </el-form-item>
              <el-form-item :label-width="formLabelWidth" label="摘要：" prop="stitle">
                <el-input v-model="formData.stitle" :rows="3" auto-complete="off" type="textarea" placeholder="请输入文章摘要"/>
              </el-form-item>
            </el-col>
            <el-col :span="6" style="padding:10px;text-align: center">
              <el-upload
                :show-file-list="false"
                :on-success="handleAvatarSuccess"
                :before-upload="beforeAvatarUpload"
                :action="uploadImageUrl"
                :data="{describe:'文章缩略图',field_name:'articleCategoryThumb'}"
                name="articleCategoryThumb"
                class="avatar-uploader">
                <img v-if="formData.thumb" :src="formData.thumb" class="avatar">
                <i v-else class="el-icon-plus avatar-uploader-icon"/>
              </el-upload>
              <div style="color:#999999;font-size:13px;line-height:30px;">文章缩略图</div>
            </el-col>
          </el-row>
          <el-row style="padding-left:20px;padding-right:80px;">
            <vue-ueditor-wrap v-model="formData.content" :config="ueditorConfig" style="line-height: normal;"/>
          </el-row>
          <el-row style="text-align: center;margin-top:20px;">
            <el-button size="small" type="primary" style="width:200px;" @click="handleArticleList">返回列表</el-button>
            <el-button size="small" type="primary" style="width:200px;" @click="handleSubmitArticle">保 存</el-button>
          </el-row>
        </el-form>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import VueUeditorWrap from 'vue-ueditor-wrap'
import { getArticleCategoryTree } from '@/api/articleCategory'
import { addArticle, updateArticle, getOneArticle } from '@/api/article'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  title: '',
  stitle: '',
  thumb: '',
  seoKeywords: '',
  seoDescription: '',
  cid: [],
  isTrash: 0,
  status: 1,
  publishTime: '',
  author: '',
  content: '<h2>这里编辑文章内容</h2>',
  sdate: '',
  edate: '',
  sort: 0,
  visits: 0,
  topTime: '',
  isTop: 0,
  newTime: '',
  isNew: 0,
  tuijianTime: '',
  isTuijian: 0,
  seoTitle: ''
}

export default {
  components: {
    VueUeditorWrap
  },
  data() {
    return {
      imageUrl: '',
      uploadImageUrl: process.env.BASE_API + '/admin/article/uploadImage',
      typeTreeLists: [],
      optionsProps: {
        value: 'id',
        label: 'name',
        children: 'children'
      },
      ueditorConfig: {
        // 编辑器不自动被内容撑高
        autoHeightEnabled: false,
        // 初始容器高度
        initialFrameHeight: 400,
        // 初始容器宽度
        initialFrameWidth: '100%',
        // 上传文件接口（这个地址是我为了方便各位体验文件上传功能搭建的临时接口，请勿在生产环境使用！！！）
        serverUrl: process.env.BASE_API + '/admin/upload',
        // UEditor 资源文件存放的根目录，如果你使用的是 vue-cli 3.x，设置为'/UEditor/'（参考下方的常见问题2）
        UEDITOR_HOME_URL: '/static/UEditor/'
      },
      labelPosition: 'right',
      formLabelWidth: '120px',
      formData: Object.assign({}, defaultForm),
      rules: {
        cid: [
          { required: true, message: '请选择文章分类', trigger: 'change' }
        ]
      }
    }
  },
  computed: {
    contentShortLength() {
      return this.formData.stitle.length
    }
  },
  activated(){
    this.fetchData()
    this.fetchTypeData()
  },
  mounted() {
    this.fetchData()
    this.fetchTypeData()
  },
  methods: {
    fetchData() {
      var id = this.$route.query.id
      if (id !== undefined && id > 0) {
        getOneArticle({ id: id }).then(response => {
          this.formData = {}
          this.formData = response.data
        })
      }else{
        this.formData = {}
      }
    },
    fetchTypeData() {
      getArticleCategoryTree().then(response => {
        this.typeTreeLists = response.data
      })
    },
    handleAvatarSuccess(res, file) {
      this.formData.thumb = process.env.BASE_API+res.data.url
    },
    beforeAvatarUpload(file) {
      const isLt2M = file.size / 1024 / 1024 < 2
      if (!isLt2M) {
        this.$alert.error('上传头像图片大小不能超过 2MB!')
      }
      return isLt2M
    },
    handleSubmitArticle() {
      console.log(JSON.parse(JSON.stringify(this.formData)))

      this.$refs.ArticleForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.formData))
          formdata['cid'] = formdata.cid[formdata['cid'].length - 1]
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addArticle(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchTypeData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
              this.$router.push({ path: '/article/articleList' })
            }).catch(() => {})
          } else {
            updateArticle(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchTypeData()
              this.$message.success('修改成功')
              this.formSumbitLoading = false
            }).catch(() => {})
          }
        }
      })
    },
    handleArticleList() {
      this.$router.push({ path: '/article/articleList'})
    }
  }
}
</script>

<style>
  .word-counter {
    width: 40px;
    position: absolute;
    right: -10px;
    top: 0px;
  }
  .avatar-uploader .el-upload {
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    cursor: pointer;
    position: relative;
    overflow: hidden;
  }
  .avatar-uploader .el-upload:hover {
    border-color: #409EFF;
  }
  .avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    line-height: 178px;
    text-align: center;
  }
  .avatar {
    width: 178px;
    height: 178px;
    display: block;
  }
</style>
