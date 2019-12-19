<template>
  <div class="app-container">
    <el-row style="border: 1px solid #ebebeb;border-radius: 3px;">
      <el-form ref="configForm" :rules="rules" :model="form" size="small" label-position="top">
        <el-col :offset="3" :span="8" style="padding:20px">
          <el-form-item label="标识名称（只能使用英文且不能重复）：" prop="name">
            <el-input v-model="form.name" auto-complete="off"/>
          </el-form-item>
          <el-form-item label="配置类型（系统会根据不同类型解析配置值）：" prop="type">
            <el-select v-model="form.type" placeholder="请选择类型" style="width: 100%;">
              <el-option v-for="item in configTypeDatas" :key="item.value" :label="item.text" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="配置标题（用于后台显示的配置标题）：" prop="title">
            <el-input v-model="form.title" auto-complete="off"/>
          </el-form-item>
          <el-form-item label="配置分组（配置分组 用于批量设置 不分组则不会显示在系统设置中）：" prop="group">
            <el-select v-model="form.group" placeholder="请选择分组" style="width: 100%;">
              <el-option v-for="item in configGroupDatas" :key="item.value" :label="item.text" :value="item.value"/>
            </el-select>
          </el-form-item>
          <el-form-item label="配置项（如果是枚举型 需要配置该项）：" prop="extra">
            <el-input :rows="2" v-model="form.extra" type="textarea" auto-complete="off"/>
          </el-form-item>
          <el-form-item label="说明（配置详细说明）：">
            <el-input :rows="2" v-model="form.remark" type="textarea" auto-complete="off"/>
          </el-form-item>
        </el-col>
        <el-col :span="8" style="padding:20px">
          <el-form-item label="配置值：" prop="value">
            <el-input :rows="2" v-model="form.value" type="textarea" auto-complete="off"/>
          </el-form-item>
          <el-form-item label="属性值：" prop="pname">
            <el-input :rows="2" v-model="form.pname" type="textarea" auto-complete="off"/>
          </el-form-item>
          <el-form-item label="配置排序：" prop="sort">
            <el-input-number v-model="form.sort" :min="1" :max="1000" auto-complete="off"/>
          </el-form-item>
          <el-form-item label="编辑框高度：" prop="height">
            <el-input-number v-model="form.height" :min="1" :max="10" />
          </el-form-item>
          <el-form-item label="是否启用：">
            <el-switch
              v-model="form.status"
              :active-value="statusSwitch.activeValue"
              :inactive-value="statusSwitch.inactiveValue"/>
          </el-form-item>
        </el-col>
      </el-form>
      <el-col :span="24" style="text-align: center;border-top:1px solid #ebebeb;padding:20px;">
        <el-button size="small" type="info" style="width:200px;" @click="$router.go(-1)"><i class="fa fa-reply"/> 返回</el-button>
        <el-button :loading="formSumbitLoading" style="width:200px" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { addConfig, updateConfig, getConfigGroup, getConfigData, getConfigType } from '@/api/config'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = { id: 0, name: '', type: '', title: '', group: '', extra: '', status: 1, pname: '', value: '', sort: 1, height: 2 }
export default {
  data() {
    return {
      formLabelWidth: '200px',
      formSumbitLoading: false,
      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0
      },
      configTypeDatas: [],
      configGroupDatas: [],
      form: Object.assign({}, defaultForm),
      rules: {
        name: [
          { required: true, message: '请输入标识名称', trigger: 'blur' }
        ],
        type: [
          { required: true, message: '请选择类型', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    this.configTypeDatas = []
    this.configGroupDatas = []
    this.form = Object.assign({}, defaultForm),
    setCurrentUrl("/system/config")
    this.fetchData()
    this.getConfigType()
    this.getConfigGroup()
  },
  created() {
    setCurrentUrl("/system/config")
    this.fetchData()
    this.getConfigType()
    this.getConfigGroup()
  },
  methods: {
    fetchData() {
      this.loading = true
      var config_id = this.$route.query.config_id
      if (config_id !== undefined && config_id > 0) {
        return new Promise((resolve, reject) => {
          getConfigData({ id: this.$route.query.config_id }).then(response => {
            this.form = response.data
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      }
    },

    getConfigType() {
      this.loading = true
      return new Promise((resolve, reject) => {
        getConfigType().then(response => {
          this.configTypeDatas = response.data
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
    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs['configForm'].validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            return new Promise((resolve, reject) => {
              addConfig(formdata).then(response => {
                this.fetchData()
                this.$message.success('添加成功')
                this.formSumbitLoading = false
                resolve(response)
              }).catch(error => {
                reject(error)
                this.formSumbitLoading = false
              })
            })
          } else {
            // 如果id不是0则为修改用户，修改用户密码为空则为不修改
            return new Promise((resolve, reject) => {
              updateConfig(formdata).then(response => {
                this.$message.success('修改成功')
                this.formSumbitLoading = false
                resolve(response)
              }).catch(error => {
                this.formSumbitLoading = false
                reject(error)
              })
            })
          }
        } else {
          return false
        }
      })
    }
  }
}
</script>
