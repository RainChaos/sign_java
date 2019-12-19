<template>
  <div style="height: 100%;border:1px solid #ebeef5">
    <div style="padding:10px;border-bottom:1px solid #ebeef5">可选择字段</div>
    <el-scrollbar style="height: calc(100% - 40px);">
      <el-tree
        v-loading="listLoading"
        ref="selectStudentFieldTree"
        :props="unitProps"
        :default-expand-all="true"
        :indent="0"
        :data="selectStudentFieldLists"
        :check-strictly="true"
        :highlight-current="true"
        :expand-on-click-node="false"
        :default-checked-keys="defaultCheckedKeys"
        class="selectStudentField"
        show-checkbox
        element-loading-text="正在加载中..."
        node-key="fieldName"
        @check="selectStudentFieldChange">
        <span slot-scope="{ node, data }" class="custom-tree-node">
          <span>{{ data.describe }}-{{ data.fieldName }}</span>
        </span>
      </el-tree>
    </el-scrollbar>
  </div>
</template>

<script>
import { getStudentFieldListBySearch } from '@/api/studentField'
export default {
  props: {
    defaultCheckedKeys: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      selectStudentFieldLists: [],
      listLoading: true,
      unitProps: {
        children: 'children',
        label: 'describe'
      }
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      return new Promise((resolve, reject) => {
        getStudentFieldListBySearch({ isShow: 1 }).then(response => {
          this.selectStudentFieldLists = response.data
          this.listLoading = false
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    selectStudentFieldChange(data, node) {
      this.$emit('SelectStudentFieldChange', data, node)
    }
  }
}
</script>

<style>
  .selectStudentField .el-icon-caret-right:before {
    content: "" !important;
  }
  .selectStudentField .custom-tree-node {
    font-size:14px;
  }
</style>
