<template>
  <div class="app-container">
    <el-row>
      <tree-grid
        :columns="columns"
        :rowdata="treeListData"
        :need-update="needUpdate"
        :tree-loading="treeLoading"
        @refreshTable="refreshTable"
        @uploadmodify="uploadmodify"
        @uploaddelete="uploaddelete"
      />
    </el-row>
  </div>
</template>

<script>
import { getAuthRuleLists, updateAuthRule, deleteAuthRule } from '@/api/authRule'
import TreeGrid from '@/components/TreeGrid/index'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  components: { TreeGrid },
  filters: {
    statusFilter(status) {
      const statusMap = {
        published: 'success',
        draft: 'gray',
        deleted: 'danger'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      columns: [
        { name: 'ID', prop: 'id', width: 60 },
        { name: '删除', delete: true }, // 删除、操作不需要width
        { name: '名称', prop: 'name', width: 260, isTree: true, edit: true },
        { name: '操作', operate: true }, // 删除、操作不需要width
        { name: '层级', prop: 'level', width: 120 },
        { name: 'URL', prop: 'url', edit: true },
        { name: '排序', prop: 'sort', edit: true, width: 80 },
        { name: '备注', prop: 'remark', edit: true }
      ],
      treeListData: [],
      needUpdate: Date.now(),
      treeLoading: false
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchData()
  },
  created() {
    setCurrentUrl(this.$route.path)
    this.fetchData()
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fetchData() {
      this.treeLoading = true
      return new Promise((resolve, reject) => {
        getAuthRuleLists().then(response => {
          this.treeListData = response.data
          this.treeLoading = false
          this.needUpdate = Date.now()
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    refreshTable() {
      this.treeLoading = true
      return new Promise((resolve, reject) => {
        getAuthRuleLists().then(response => {
          this.treeListData = response.data
          this.treeLoading = false
          this.needUpdate = Date.now()
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    uploadmodify([data, successFn_callback, faildFn_callback]) {
      if (Math.random() > 0.1) {
        var updatedata = {
          id: data.id,
          name: data.name,
          parentid: data.parentid,
          type: data.type,
          status: data.status,
          sort: data.sort,
          level: data.level,
          url: data.url,
          remark: data.remark
        }
        return new Promise((resolve, reject) => {
          updateAuthRule(updatedata).then(response => {
            this.fetchData()
            resolve(response)
            successFn_callback()
          }).catch(error => {
            reject(error)
          })
        })
      } else {
        faildFn_callback()
      }
    },
    uploaddelete([deleteIdArray, successFn_callback, faildFn_callback]) {
      setTimeout(() => {
        if (Math.random() > 0.1) {
          return new Promise((resolve, reject) => {
            var ids = deleteIdArray.join(',')
            deleteAuthRule({ ids: ids }).then(response => {
              successFn_callback()
              resolve(response)
            }).catch(error => {
              reject(error)
            })
          })
          // 删除成功
        } else {
          // 删除失败
          faildFn_callback()
        }
      }, 1100)
    }
  }
}
</script>
