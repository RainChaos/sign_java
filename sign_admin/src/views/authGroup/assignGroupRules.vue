<template>
  <div class="app-container">
    <el-row style="border: 1px solid #ebebeb;border-radius: 3px;">
      <el-col :span="24" style="padding:20px">
        <ul v-loading="loading" class="role-table" style="margin:0px;padding:0px;">
          <li class="role-header">
            <div class="left">菜单列表</div>
            <div class="right">功能权限</div>
          </li>
          <div class="role-vertical-line"/>
          <li v-for="(item) in dataList" :key="item.id">
            <div class="left h40" @click="fold(item)">
              <i
                v-cloak
                v-if="item.children && item.children[0].children"
                :class="{'el-icon-caret-right':item.folded,'el-icon-caret-bottom':!item.folded}"
                class="item-icon"/>
              <el-checkbox v-cloak  v-model="item.checked" @change="checkChildrenAll($event,item)"> {{ item.name }}</el-checkbox>
              <span v-if="item.children && item.children[0].children" v-cloak>{{ item.name }}</span>
            </div>
            <div class="right h40">
              <el-checkbox v-if="item.children && item.children[0].children" v-model="item.checked" @change="checkChildrenAll($event,item)"> 所有</el-checkbox>
              <div v-if="item.children && !item.children[0].children">
                <el-checkbox v-for="m in item.children" v-cloak v-model="m.checked" :label="m.id" :key="m.id" @change="handleTwoChange($event,item)">
                  {{ m.name }}
                </el-checkbox>
              </div>
            </div>

            <div class="line"/>
            <ul v-show="item.children && item.children[0].children && !item.folded" style="margin: 0px;padding:0px;">
              <li v-for="(children) in item.children" :key="children.id" class="h40">
                <div class="left h40">
                  <el-checkbox v-cloak v-model="children.checked" @change="checkChildrenAll2($event,item,children)">
                    {{ children.name }}
                  </el-checkbox>
                </div>
                <div class="right h40">
                  <div>
                    <el-checkbox v-for="p in children.children" v-cloak v-model="p.checked" :label="p.id" :key="p.id" @change="handleThreeChange($event,item,children)">
                      {{ p.name }}
                    </el-checkbox>
                  </div>
                </div>
                <div class="line"/>
              </li>
            </ul>
          </li>
        </ul>
      </el-col>
      <el-col :span="24" style="text-align: center;border-top:1px solid #ebebeb;padding:20px;">
        <el-button size="small" style="width: 200px;" type="info" @click="$router.go(-1)"><i class="fa fa-reply"/> 返回</el-button>
        <el-button size="small" style="width: 200px;" type="primary" @click="handleSaveGroupRules"><i class="fa fa-save"/> 保存</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getAuthRulesTree, assignGroupRules } from '@/api/authGroup'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      loading: false,
      dialogAuthRulesTitle: '设置角色权限',
      dataList: []
    }
  },
  activated(){
    setCurrentUrl('/system/authGroup')
    this.fetchData()
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 收起或展开列表
    fold: function(item) {
      if (typeof item.folded === 'undefined') {
        this.$set(item, 'folded', true)
      } else {
        item.folded = !item.folded
      }
    },
    // 点击选中或者取消选中所有
    checkChildrenAll: function(val, item) {
      item.children.forEach(one => {
        one.checked = val
        if (typeof one.children !== 'undefined') {
          one.children.forEach(two => {
            two.checked = val
          })
        }
      })
    },
    // 点击选中或者取消选中所有
    checkChildrenAll2: function(val, item, children) {
      item.checked = false
      children.children.forEach(one => {
        one.checked = val
        if (one.checked) {
          item.checked = true
        }
        if (typeof one.children !== 'undefined') {
          one.children.forEach(two => {
            two.checked = val
          })
        }
      })
    },
    // 单选最后一项
    handleTwoChange(val, item) {
      item.checked = false
      item.children.forEach(two => {
        if (two.checked) {
          item.checked = true
        }
      })
    },
    // 单选最后一项
    handleThreeChange(val, item, children) {
      children.checked = false
      item.checked = false
      children.children.forEach(three => {
        if (three.checked) {
          children.checked = true
        }
      })
      item.children.forEach(two => {
        if (two.checked) {
          item.checked = true
        }
      })
    },
    fetchData() {
      this.loading = true
      return new Promise((resolve, reject) => {
        getAuthRulesTree({ group_id: this.$route.query.group_id }).then(response => {
          var data = response.data
          this.dataList = data
          this.loading = false
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    handleSaveGroupRules() {
      var rule_ids = []
      this.dataList.forEach(row => {
        if (row.checked) {
          rule_ids.push(row.id)
        }
        if (typeof row.children !== 'undefined' && row.children.length > 0) {
          row.children.forEach(two => {
            if (two.checked) {
              rule_ids.push(two.id)
            }
            if (typeof two.children !== 'undefined' && two.children.length > 0) {
              two.children.forEach(three => {
                if (three.checked) {
                  rule_ids.push(three.id)
                }
              })
            }
          })
        }
      })

      assignGroupRules({ group_id: this.$route.query.group_id, rules: rule_ids.join(',') }).then(response => {
        this.fetchData()
        this.$alert('保存成功', '提示', { type: 'success' })
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" >
  .role-table {
    .el-checkbox__label {
      font-size: 12px;
    }
  }
</style>

<style rel="stylesheet/scss" lang="scss" scoped>
  .role-table {
    border: 1px solid #e0e0e0;
    border-bottom: none;
    padding: 0;
    position: relative;
    li{
      list-style: none;
    }
    .role-header {
      min-height: 40px;
      line-height: 40px;
      border-bottom: 1px solid #e7e7e7;
      background: #F8F8F9;
      // text-align: center;
      font-weight: bolder;
      font-size: 14px;
      .left , .right{
        color: #909399;
      }
    }
    .role-vertical-line {
      width: 1px;
      height: 100%;
      background: #ddd;
      position: absolute;
      left: 25%;
      top: 0
    }
    .left {
      width: 25%;
      float: left;
      padding-left: 10px;
      user-select: none;
      cursor: pointer;
    }
    .one {
      padding-left: 20px;
    }
    .right {
      width: 75%;
      float: left;
      padding-left: 10px;
    }
    .item-icon {
      margin-left: -5px;
      padding: 5px;
    }
    .line {
      clear: both;
      width: 100%;
      height: 1px;
      background: #e0e0e0;
    }
    .h40{
      min-height: 39px;
      line-height: 39px;
    }
    .el-checkbox{
      margin-right: 20px;
      margin-left: 0px;
    }
    [v-cloak]{
      display: none;
    }
  }
</style>
