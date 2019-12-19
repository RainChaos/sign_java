<template>
  <div class="app-container" style="height:100%;overflow: hidden;padding:15px;">
    <el-row :gutter="15" style="height:calc(100% - 100px);overflow: hidden">
      <el-col :span="6" style="height:100%;">
        <div style="padding:10px;border-bottom:1px solid #dddddd;background-color: #ddd">
          <i class="el-icon-menu" />  单位权限
        </div>
        <el-input
          placeholder="输入关键字进行过滤"
          v-model="filterText"
          size="small"
          style="margin-top:5px; margin-bottom:5px">
        </el-input>
        <el-scrollbar  style="height: calc(100% - 93px)">
          <el-tree
            ref="treeBox"
            :data="schoolData"
            :props="defaultProps"
            :highlight-current="true"
            :default-expand-all="false"
            :expand-on-click-node="true"
            :check-on-click-node="true"
            :check-strictly="false"
            :indent="10"
            :load="loadNode"
            :filter-node-method="filterNode"
            :default-expanded-keys="defaultExpandedKeys"
            :default-checked-keys="defaultCheckedKeys"
            style="border:1px solid #ddd;height:100%"
            node-key="unit_code"
            element-loading-text="正在加载中..."
            show-checkbox/>
        </el-scrollbar>
      </el-col>
      <el-col :span="18" style="height:100%">
        <div style="padding:10px;border-bottom:1px solid #dddddd;background-color: #ddd">
          <i class="el-icon-menu" /> 菜单权限
        </div>
        <ul v-loading="loading" class="role-table" style="margin:0px;padding:0px;height:100%">
          <li class="role-header">
            <div class="left">菜单列表</div>
            <div class="right">功能权限</div>
          </li>
          <div class="role-vertical-line"/>
          <el-scrollbar wrap-class="scrollbar-wrapper" style="height: calc(100% - 63px)">
            <li v-for="(item) in dataList" :key="item.id">
              <div class="left h40" @click="fold(item)">
                <i
                  v-cloak
                  v-if="item.children && item.children[0].children"
                  :class="{'el-icon-caret-right':item.folded,'el-icon-caret-bottom':!item.folded}"
                  class="item-icon"/>
                <el-checkbox v-cloak v-model="item.checked" @change="checkChildrenAll($event,item)"> {{ item.name }}</el-checkbox>
                <span v-if="item.children && item.children[0].children" v-cloak>{{ item.name }}</span>
              </div>
              <div class="right h40">
                <span v-for="m in item.children">
                  <el-checkbox v-if="m.children" v-model="item.checked" @change="checkChildrenAll($event,item)"> 所有</el-checkbox>
                  <span v-if="!m.children">
                    <el-checkbox v-cloak v-model="m.checked" :label="m.id" :key="m.id" @change="handleTwoChange($event,item)">
                      {{ m.name }}
                    </el-checkbox>
                  </span>
                </span>
              </div>
              <div class="line"/>
              <span v-for="m in item.children">
                <ul v-show="m && m.children && !item.folded" style="margin: 0px;padding:0px;">
                  <span v-for="(children) in item.children" :key="children.id">
                    <li class="h40">
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
                  </span>
                </ul>
              </span>
            </li>
          </el-scrollbar>
        </ul>
      </el-col>
    </el-row>
    <el-row style="text-align: center;border-top:1px solid #ebebeb;padding:20px;">
      <el-button size="small" style="width: 200px;" type="info" @click="$router.go(-1)"><i class="fa fa-reply"/> 返回</el-button>
      <el-button size="small" style="width: 200px;" type="primary" @click="handleSaveGroupRules"><i class="fa fa-save"/> 保存</el-button>
    </el-row>
  </div>
</template>

<script>
import { getAuthRulesTree, assignOperatorRules, getOperatorInfo } from '@/api/operator'
import { getStructureLists } from '@/api/unit'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  watch: {
    filterText(val) {
      this.$refs.treeBox.filter(val);
    }
  },
  data() {
    return {
      filterText: '',
      loading: false,
      dialogAuthRulesTitle: '设置角色权限',
      dataList: [],
      defaultExpandedKeys: [],
      defaultCheckedKeys: [],
      operatorInfo: {
        username: '',
        realname: ''
      },
      defaultProps: {
        children: 'children',
        label: 'unit_name'
      },
      schoolData: []
    }
  },
  activated(){
    setCurrentUrl('/system/operator')
    this.fetchData()
    this.fetchOperatorInfo()
    getStructureLists().then(response => {
      const data = response.data
      this.schoolData = data
    }).catch(error => {
      console.log(error)
    })
  },
  mounted() {
    this.fetchData()
    this.fetchOperatorInfo()
    getStructureLists().then(response => {
      const data = response.data
      this.schoolData = data
    }).catch(error => {
      console.log(error)
    })
  },
  methods: {
    filterNode(value, data){
      if (!value)
        return true;
      return data.unit_name.indexOf(value) !== -1;
    },
    // 加载单位结构数据
    loadNode(node, resolve) {
      if (node.level === 0) {

      // } else {
      //   getStructureLists({ farther_code: node.data.unit_code }).then(response => {
      //     const data = response.data
      //     resolve(data)
      //   }).catch(error => {
      //     console.log(error)
      //   })
      }
    },
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

    fetchOperatorInfo() {
      const operator_id = this.$route.query.operator_id
      getOperatorInfo({ operator_id: operator_id }).then(response => {
        var data = response.data
        this.operatorInfo = data
        var keys = []
        var keys2 = []
        data['unit_class'].forEach(item => {

          keys.push(item)

          if( item.length === 18 ){
            keys2.push(item)
          }
        })
        this.defaultExpandedKeys = keys
        this.defaultCheckedKeys = keys2
      }).catch(error => {
        console.log(error)
      })
    },

    fetchData() {
      this.loading = true
      getAuthRulesTree({ operator_id: this.$route.query.operator_id }).then(response => {
        var data = response.data
        this.dataList = data
        this.loading = false
      }).catch(error => {
        console.log(error)
      })
    },
    handleSaveGroupRules() {
      const checkedNodes = this.$refs.treeBox.getCheckedNodes()
      var class_code = []
      checkedNodes.forEach(item => {
        class_code.push(item.unit_code)
      })
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
      const params = { operator_id: this.$route.query.operator_id, rules: rule_ids.join(','), operator_class: class_code.join(',') }
      assignOperatorRules(params).then(response => {
        this.fetchData()
        this.$alert('设置成功', '提示', { type: 'success' })
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
    font-size: 13px;
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
