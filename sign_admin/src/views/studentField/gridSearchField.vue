<template>
  <div class="app-container">
    <el-row class="actionBtn">
      <el-button :type="tableName=='维护学生列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('维护学生列表')">维护学生列表</el-button>
      <el-button :type="tableName=='预备制审核列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('预备制审核列表')">预备制审核列表</el-button>
      <el-button :type="tableName=='打印通知书列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('打印通知书列表')">打印通知书列表</el-button>
      <el-button :type="tableName=='入住公寓列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('入住公寓列表')">入住公寓列表</el-button>
      <el-button :type="tableName=='分班管理列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('分班管理列表')">分班管理列表</el-button>
      <el-button :type="tableName=='学籍异动列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('学籍异动列表')">学籍异动列表</el-button>
      <el-button :type="tableName=='一键分班列表'?'primary':'default'" size="small" @click.native="handleChangeFormName('一键分班列表')">一键分班列表</el-button>
    </el-row>
    <el-row style="height: calc(100% - 60px);margin-top:10px;">
      <el-col :span="4" style="height: 100%;">
        <div style="height: 100%;border:1px solid #ebeef5">
          <div style="padding:10px;border-bottom:1px solid #ebeef5">可选择字段</div>
          <el-scrollbar style="height: calc(100% - 40px);">
            <el-tree
              v-loading="listLoading"
              ref="selectSearchFieldTree"
              :props="unitProps"
              :default-expand-all="true"
              :indent="0"
              :data="StudentSearchLists"
              :check-strictly="true"
              :highlight-current="true"
              :expand-on-click-node="false"
              :default-checked-keys="defaultCheckedKeys"
              class="selectStudentField"
              show-checkbox
              element-loading-text="正在加载中..."
              node-key="fieldName"
              @check="SearchFieldChange">
              <span slot-scope="{ node, data }" class="custom-tree-node">
                <span>{{ data.describe }}-{{ data.fieldName }}</span>
              </span>
            </el-tree>
          </el-scrollbar>
        </div>
      </el-col>
      <el-col :span="20">
        <el-table
          v-loading="listLoading2"
          :data="GridSearchLists"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row>
          <el-table-column align="center" label="ID" width="60">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column label="字段名称" width="200">
            <template slot-scope="scope">
              {{ scope.row.describe }}
            </template>
          </el-table-column>
          <el-table-column label="字段" width="150">
            <template slot-scope="scope">
              {{ scope.row.fieldName }}
            </template>
          </el-table-column>
          <el-table-column label="条件sql" width="300">
            <template slot-scope="scope">
              {{ scope.row.whereSql }}
            </template>
          </el-table-column>
          <el-table-column label="排序" width="150">
            <template slot-scope="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column
            fixed="right"
            label="操作">
            <template slot-scope="scope">
              <el-button size="mini" plain @click="handleUp(scope.$index, scope.row)">上移</el-button>
              <el-button size="mini" plain @click="handleDown(scope.$index, scope.row)">下移</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStudentSearchFieldLists } from '@/api/studentSearchField'
import { getGridSearchFieldLists, getFieldNameByTableName, addGridSearchField, deleteGridSearchField, updateGridSearchField, setOrderSortAll } from '@/api/gridSearchField'
import { isInArray } from '@/utils/index'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      tableName: '维护学生列表',
      StudentSearchLists: [],
      GridSearchLists: [],
      defaultCheckedKeys: [],
      listLoading: true,
      listLoading2: true,
      unitProps: {
        children: 'children',
        label: 'describe'
      },
      dataLists: []
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
  },
  created() {
    setCurrentUrl(this.$route.path)
    var tableName = this.$route.query.formName
    if (tableName === undefined) {
      tableName = '维护学生列表'
    }
    this.tableName = tableName
    this.fetchData()
    this.fetchDataLists()
  },
  methods: {
    fetchSelectFieldDefaultSelect() {
      getFieldNameByTableName({ tableName: this.tableName }).then(response => {
        this.defaultCheckedKeys = response.data
        this.$refs.selectSearchFieldTree.setCheckedKeys(this.defaultCheckedKeys)
      })
    },
    fetchData() {
      this.listLoading = true
      getStudentSearchFieldLists({ tableName: this.tableName }).then(response => {
        this.StudentSearchLists = response.data
        this.fetchSelectFieldDefaultSelect()
        this.listLoading = false
      })
    },
    fetchDataLists() {
      this.listLoading2 = true
      getGridSearchFieldLists({ tableName: this.tableName }).then(response => {
        this.GridSearchLists = response.data
        this.listLoading2 = false
      })
    },
    SearchFieldChange(data, status) {
      const params = {
        fieldName: data.fieldName,
        tableName: this.tableName
      }
      if (isInArray(params.fieldName, status.checkedKeys)) {
        addGridSearchField(params).then(response => {
          this.fetchDataLists()
        })
      } else {
        deleteGridSearchField(params).then(response => {
          this.fetchDataLists()
        })
      }
    },
    handleSort() {
      setOrderSortAll({ form_name: this.formName }).then(response => {
        this.fetchDataLists()
      }).catch(() => {})
    },
    /**
     *  上移
     */
    handleUp(index, row) {
      if (index > 0) {
        var downData = JSON.parse(JSON.stringify(this.GridSearchLists[index]))
        var upData = JSON.parse(JSON.stringify(this.GridSearchLists[index - 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateGridSearchField(upData).then(response => {})
        updateGridSearchField(downData).then(response => {})
        this.GridSearchLists.splice(index - 1, 1)
        this.GridSearchLists[index - 1]['sort'] = downData['sort']
        this.GridSearchLists.splice(index, 0, upData)
      } else {
        this.$alert('已经是第一条，不可上移', '提示', { type: 'warning' })
      }
    },
    /**
     *  下移
     */
    handleDown(index, row) {
      if ((index + 1) === this.GridSearchLists.length) {
        this.$alert('已经是最后一条，不可下移', '提示', { type: 'warning' })
      } else {
        var upData = JSON.parse(JSON.stringify(this.GridSearchLists[index]))
        var downData = JSON.parse(JSON.stringify(this.GridSearchLists[index + 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateGridSearchField(upData).then(response => {})
        updateGridSearchField(downData).then(response => {})
        this.GridSearchLists.splice(index + 1, 1)
        this.GridSearchLists.splice(index, 0, downData)

        this.GridSearchLists[index + 1]['sort'] = upData['sort']
      }
    },
    handleChangeFormName(formName) {
      this.tableName = formName
      this.fetchSelectFieldDefaultSelect()
      this.fetchDataLists()
      this.$router.replace({ path: '/setting/gridSearchField', query: { formName: formName }})
    }
  }
}
</script>
