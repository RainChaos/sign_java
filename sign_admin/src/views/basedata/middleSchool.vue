<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col :span="4" style="border:1px solid #dddddd;height:100%;">
        <div style="padding:10px;border-bottom:1px solid #dddddd">
          省市区结构
        </div>
        <el-scrollbar style="height: calc(100% - 40px);">
          <el-tree
            ref="treeRegionBox"
            :props="defaultProps"
            :highlight-current="true"
            :default-expand-all="false"
            :expand-on-click-node="false"
            :check-strictly="true"
            :indent="10"
            :load="loadNode"
            node-key="code"
            lazy
            @current-change="currentChange"/>
        </el-scrollbar>
      </el-col>
      <el-col :span="20" style="height:100%;padding-left:10px;">
        <el-row class="actionBtn">
          <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/>新 增</el-button>
          <el-button size="small" type="success" @click="fetchSchoolLists"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
        </el-row>
        <el-table
          v-loading="listLoading"
          :data="list.data"
          height="calc(100% - 95px)"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row>
          <el-table-column align="center" label="序号" width="60">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <el-table-column label="地区代码" width="200">
            <template slot-scope="scope">
              {{ scope.row.regionCode }}
            </template>
          </el-table-column>
          <el-table-column label="中学名称" width="350">
            <template slot-scope="scope">
              {{ scope.row.schoolName }}
            </template>
          </el-table-column>
          <el-table-column label="排序" width="80">
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
              <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
              <el-button type="danger" size="mini" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <el-row v-if="pagination.total>0">
          <el-pagination
            :current-page="pagination.current_page"
            :page-sizes="[20,30, 50, 100]"
            :page-size="pagination.pre_page"
            :total="pagination.total"
            background
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"/>
        </el-row>
      </el-col>
    </el-row>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="500px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="middleSchoolForm" :model="form" :rules="rules" size="small">
        <el-form-item :label-width="formLabelWidth" label="地区代码：" prop="regionCode">
          <el-input v-model="form.regionCode" disabled auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="名称：" prop="schoolName">
          <el-input v-model="form.schoolName" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：" prop="sort">
          <el-input-number v-model="form.sort" auto-complete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getRegionLists } from '@/api/region'
import { getMiddleSchoolLists, addMiddleSchool, updateMiddleSchool, deleteMiddleSchool, setOrderSortAll } from '@/api/middleSchool'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      TreeData: [],
      selectRegion: {},
      defaultProps: {
        children: 'children',
        label: 'fullName'
      },
      list: {},
      pagination: {
        current_page: 0,
        pre_page: 20,
        total: 0
      },
      listLoading: true,
      params: {
        limit: 20,
        page: 1,
        regionCode: 100000
      },
      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0,
        activeColor: '#13ce66',
        inactiveColor: '#ff4949'
      },

      dialogFormVisible: false,
      formLabelWidth: '100px',
      formTitle: '添加中学信息',
      formIconClass: 'fa fa-user-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: { id: 0, schoolName: '', regionCode: '', sort: '' },
      rules: {
        regionCode: [
          { required: true, message: '请输入代码值', trigger: 'blur' }
        ],
        schoolName: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchSchoolLists()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    this.fetchSchoolLists()
  },
  methods: {
    // 加载单位结构数据
    loadNode(node, resolve) {
      if (node.level === 0) {
        getRegionLists({ fatherCode: 100000 }).then(response => {
          const data = response.data
          resolve(data)
          this.$nextTick(() => {
            this.$refs.treeRegionBox.setCurrentKey(450000)
            const node2 = this.$refs.treeRegionBox.getCurrentNode()
            const data = JSON.parse(JSON.stringify(node2))
            this.selectRegion = data
          })
        })
      } else {
        getRegionLists({ fatherCode: node.data.code }).then(response => {
          const data = response.data
          resolve(data)
        })
      }
    },

    // 设置当前结构数据
    currentChange(data, node) {
      this.params.page = 1
      this.selectRegion = JSON.parse(JSON.stringify(data))
      this.fetchSchoolLists()
    },

    fetchSchoolLists() {
      var regionCode = this.selectRegion.code
      var region_type = this.selectRegion.type
      if (region_type === 1) {
        regionCode = parseInt(regionCode / 10000)
      }
      if (region_type === 2) {
        regionCode = parseInt(regionCode / 100)
      }
      if (regionCode === undefined) {
        regionCode = 45
      }
      this.params.regionCode = regionCode
      getMiddleSchoolLists(this.params).then(response => {
        this.list = response.data
        this.pagination.total = response.data.total
        this.listLoading = false
      }).catch(() => {})
    },

    handleSizeChange(val) {
      this.pagination.pre_page = val
      this.params.limit = this.pagination.pre_page
      this.params.page = 1
      this.fetchSchoolLists()
    },
    handleCurrentChange(val) {
      this.params.limit = this.pagination.pre_page
      this.params.page = val
      this.fetchSchoolLists()
    },
    handleSort() {
      var regionCode = this.selectRegion.code
      setOrderSortAll({ regionCode: regionCode }).then(response => {
        this.refreshData()
      }).catch(() => {})
    },
    /**
     *  上移
     */
    handleUp(index, row) {
      if (index > 0) {
        var downData = JSON.parse(JSON.stringify(this.list.data[index]))
        var upData = JSON.parse(JSON.stringify(this.list.data[index - 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateMiddleSchool(upData).then(response => {})
        updateMiddleSchool(downData).then(response => {})
        this.list.data.splice(index - 1, 1)
        this.list.data[index - 1]['sort'] = downData['sort']
        this.list.data.splice(index, 0, upData)
      } else {
        this.$alert('已经是第一条，不可上移', '提示', { type: 'warning' })
      }
    },
    /**
     *  下移
     */
    handleDown(index, row) {
      if ((index + 1) === this.list.data.length) {
        this.$alert('已经是最后一条，不可下移', '提示', { type: 'warning' })
      } else {
        var upData = JSON.parse(JSON.stringify(this.list.data[index]))
        var downData = JSON.parse(JSON.stringify(this.list.data[index + 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateMiddleSchool(upData).then(response => {})
        updateMiddleSchool(downData).then(response => {})
        this.list.data.splice(index + 1, 1)
        this.list.data.splice(index, 0, downData)
        this.list.data[index + 1]['sort'] = upData['sort']
      }
    },
    /**
     * 弹出添加管理员信息对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-user-plus'
      this.formTitle = '添加数据信息'
      const sort = this.list.total + 1
      this.form = { id: 0, regionCode: this.selectRegion.code, schoolName: '', sort: sort },
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改管理员信息对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改数据信息'
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogFormVisible = true
    },
    /**
     * 删除数据
     */
    handleDeleteRow(index, row) {
      this.deleteDatas(row.id)
    },
    /**
     * 批量删除数据
     */
    handleDelete() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要删除的数据', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => { ids.push(row.id) })
        ids = ids.join(',')
        this.deleteDatas(ids)
      }
    },

    /**
     * 根据ids 删除用户，ids: 1,2,3,4
     */
    deleteDatas(ids) {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteMiddleSchool({ id: ids }).then(response => {
          this.fetchSchoolLists()
          this.$message.success('删除成功')
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs.middleSchoolForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addMiddleSchool(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchSchoolLists()
              this.$message({ type: 'success', message: '添加成功!' })
              this.formSumbitLoading = false
            }).catch(() => {})
          } else {
            updateMiddleSchool(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchSchoolLists()
              this.$message({ type: 'success', message: '修改成功!' })
              this.formSumbitLoading = false
            }).catch(() => {})
          }
        } else {
          return false
        }
      })
    }

  }
}
</script>

<style scoped>

</style>
