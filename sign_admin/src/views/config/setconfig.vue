<template>
  <div class="app-container">
    <el-row style="border: 1px solid #ebebeb;border-radius: 3px;padding:20px;">
      <el-col :span="24" style="margin-bottom: 20px;min-height: 500px;">
        <el-form ref="setConfigForm" size="small" label-position="top">
          <el-tabs v-model="activeName" @tab-click="handleClick">
            <el-tab-pane v-for="(item,key) in configGroupDatas" v-if="item.value !== 0" :key="item.value"
                         :label="item.text" :name="item.value+''">
              <!-----form-------->
              <template v-for="(item2,key) in formLists" v-if=" item2.group===item.value">
                <el-collapse-transition>
                  <el-form-item v-if="item2.type === 0">
                    <label slot="label">{{ item2.title }} 【{{ item2.name }}】</label>
                    <el-input-number v-model="item2.value" style="min-width:300px;"/>
                  </el-form-item>
                  <el-form-item v-if="item2.type === 1">
                    <label slot="label">{{ item2.title }} 【{{ item2.name }}】</label>
                    <el-input v-model="item2.value" type="text" style="width:50%"/>
                  </el-form-item>
                  <el-form-item v-if="item2.type === 2">
                    <label slot="label">{{ item2.title }} 【{{ item2.name }}】</label>
                    <el-input v-model="item2.value" type="textarea"/>
                  </el-form-item>
                  <el-form-item v-if="item2.type === 3">
                    <label slot="label">{{ item2.title }} 【{{ item2.name }}】</label>
                    <el-input :rows="item2.height" v-model="item2.value" type="textarea"/>
                  </el-form-item>
                  <el-form-item v-if="item2.type === 4 || item2.type === 5">
                    <label slot="label">{{ item2.title }} 【{{ item2.name }}】</label>
                    <el-select v-model="item2.value" placeholder="请选择类型" style="width:50%">
                      <el-option v-for="item3 in item2.extra" :key="item3.value" :label="item3.text"
                                 :value="item3.value"/>
                    </el-select>
                  </el-form-item>
                  <el-form-item v-if="item2.type === 6">
                    <label slot="label">
                      {{ item2.title }} 【{{ item2.name }}】
                    </label>
                    <el-upload
                      :on-preview="handlePreview"
                      :on-remove="handleRemove"
                      v-model="item2.value"
                      :file-list="item2.file"
                      :data="{describe:item2.title,field_name:item2.name}"
                      :action="ImageConfig.uploadImageUrl"
                      :limit="ImageConfig.limit"
                      :multiple="ImageConfig.multiple"
                      :on-success="uploadImageSuccess"
                      name="photo"
                      style="width:50%"
                      list-type="picture">
                      <el-button size="small" type="primary">点击上传</el-button>
                      <span style="font-size:13px;color:#999;"><i class="el-icon-warning"/> 只能上传jpg/png文件</span>
                    </el-upload>
                  </el-form-item>

                  <el-form-item v-if="item2.type === 6">
                    <label slot="label">
                      {{ item2.title }} 【{{ item2.name }}】
                    </label>
                    <el-upload
                      :on-preview="handlePreview"
                      :on-remove="handleRemove"
                      v-model="item2.value"
                      :file-list="item2.file"
                      :data="{describe:item2.title,field_name:item2.name}"
                      :action="ImageConfig.BGImageUrl"
                      :limit="ImageConfig.limit"
                      :multiple="ImageConfig.multiple"
                      :on-success="uploadImageSuccess"
                      name="photo"
                      style="width:50%"
                      list-type="picture">
                      <el-button size="small" type="primary">点击上传手机端背景图</el-button>
                      <span style="font-size:13px;color:#999;"><i class="el-icon-warning"/> 只能上传jpg/png文件</span>
                    </el-upload>
                  </el-form-item>

                </el-collapse-transition>
              </template>
              <!-----form-------->
            </el-tab-pane>
          </el-tabs>
        </el-form>
      </el-col>
      <el-col :span="24" style="text-align: center;border-top:1px solid #ebebeb;padding:20px;padding-bottom: 0px;">
        <el-button :loading="formSumbitLoading" style="width:200px" size="small" type="primary" @click="handleSubmit">确
          定
        </el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import { getConfigGroup, getGroupLists, saveConfig } from '@/api/config'
  import { deleteFile } from '@/api/file'
  import { setCurrentUrl, getCurrentUrl } from '@/utils/auth.js'

  export default {
    data() {
      return {
        apiRoot: '',
        activeName: '1',
        formLabelWidth: '200px',
        configGroupDatas: [],
        formSumbitLoading: false,
        formLists: [],
        ImageConfig: {
          uploadImageUrl: process.env.BASE_API + '/admin/file/uploadImage',
          BGImageUrl: process.env.BASE_API + '/admin/file/backGroundImage',
          multiple: false,
          limit: 1
        }
      }
    },
    activated() {
      setCurrentUrl(this.$route.path)
      this.fetchData()
      this.getConfigGroup()
    },
    created() {
      this.apiRoot = process.env.BASE_API
      setCurrentUrl(this.$route.path)
      this.fetchData()
      this.getConfigGroup()
    },
    methods: {
      handleClick(tab, event) {
        const { name } = tab
        this.fetchData(name)
      },
      fetchData(group) {
        this.loading = true
        return new Promise((resolve, reject) => {
          group = group === undefined ? 1 : group
          getGroupLists({ group: group }).then(response => {
            this.formLists = response.data.data
            for (let i = 0; i < this.formLists.length; i++) {
              if (this.formLists[i].file) {
                // console.log("+")
                if (this.formLists[i].value) {
                  console.log(this.formLists[i])
                  this.formLists[i].file[0].url = process.env.BASE_API + this.formLists[i].file[0].url
                }

              }
            }
            // console.log(this.formLists)
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      getConfigGroup() {
        this.loading = true
        return new Promise((resolve, reject) => {
          getConfigGroup().then(response => {
            this.configGroupDatas = response.data
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      handleRemove(file, fileList) {
        for (let i = 0; i < this.formLists.length; i++) {

          if (this.formLists[i].file) {
            let item = this.formLists[i].file.find(item => {
              return item == file
            })
            if (item) {
              this.formLists[i].file = []
              this.formLists[i].value = ''
              file = {}
              fileList = []
            }
          }
        }
      },
      handlePreview(file) {
        var data = JSON.parse(JSON.stringify(file))
        this.ShowImage.title = data.name
        this.ShowImage.url = data.url
        this.dialogShowImageVisible = true
      },
      uploadImageSuccess(response, file, fileList) {
        console.log(response.data)
        fileList[0].url = process.env.BASE_API + response.data.url
        this.formLists.forEach((item, index) => {
          console.log(item.name + '--' + response.data.field_name)
          if (item.name === response.data.field_name) {
            this.formLists[index].value = response.data.url
            this.formLists[index].file = fileList
          }
        })
      },
      /**
       * 提交添加或者修改管理员信息
       */
      handleSubmit() {
        const formLists = JSON.parse(JSON.stringify(this.formLists))
        const formData = {}
        formLists.forEach((item) => {
          formData[item.name] = item.value
        })
        console.log(formData)
        this.formSumbitLoading = true
        return new Promise((resolve, reject) => {
          saveConfig(formData).then(response => {
            this.fetchData(this.activeName)
            this.$message.success('修改成功')
            this.formSumbitLoading = false
            resolve(response)
          }).catch(error => {
            this.formSumbitLoading = false
            reject(error)
          })
        })
      }
    }
  }
</script>

<style>
  .el-upload-list--picture .el-upload-list__item-thumbnail {
    width: auto !important;
  }
</style>
