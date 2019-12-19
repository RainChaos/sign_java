<template>
  <div class="app-container" style="height: 100%;">
    <el-row :gutter="10" style="height: 100%;">
      <el-col :span="8" style="height:100%;">
        <el-row class="actionBtn">
          <el-button size="mini" type="success" @click="handleSort(1)"><i class="el-icon-sort" aria-hidden="true"/>重新排序</el-button>
        </el-row>
        <el-table
          v-loading="shengfenLoading"
          ref="shengfenTable"
          :data="shengfenLists"
          height="calc(100% - 60px)"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row
          @current-change="currentShengfen">
          <el-table-column label="排序" width="68">
            <template slot-scope="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column label="行政代码" width="80">
            <template slot-scope="scope">
              {{ scope.row.code }}
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="全称">
            <template slot-scope="scope">
              {{ scope.row.fullName }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="140" align="center">
            <template slot-scope="scope">
              <el-button size="mini" plain @click="handleUp(scope.$index, scope.row)">上移</el-button>
              <el-button size="mini" plain @click="handleDown(scope.$index, scope.row)">下移</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="8" style="height:100%;">
        <el-row class="actionBtn">
          <el-button size="mini" type="primary" @click="handleChengshiAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增</el-button>
          <el-button size="mini" type="primary" @click="handleChengshiEdit"><i class="fa fa-pencil-square-o" aria-hidden="true"/>修改</el-button>
          <el-button size="mini" type="success" @click="handleSort(2)"><i class="el-icon-sort" aria-hidden="true"/>重新排序</el-button>
          <el-button size="mini" type="danger" @click="handleChengshiDelete"><i class="el-icon-delete" aria-hidden="true"/>删除</el-button>
        </el-row>
        <el-table
          v-loading="chengshiLoading"
          ref="chengshiTable"
          :data="chengshiLists"
          height="calc(100% - 60px)"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row
          @current-change="currentChengshi">
          <el-table-column label="排序" width="68">
            <template slot-scope="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column label="行政代码" width="80">
            <template slot-scope="scope">
              {{ scope.row.code }}
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="全称">
            <template slot-scope="scope">
              {{ scope.row.fullName }}
            </template>
          </el-table-column>
          <el-table-column label="操作" width="140" align="center">
            <template slot-scope="scope">
              <el-button size="mini" plain @click="handleUp(scope.$index, scope.row)">上移</el-button>
              <el-button size="mini" plain @click="handleDown(scope.$index, scope.row)">下移</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
      <el-col :span="8" style="height:100%;">
        <el-row class="actionBtn">
          <el-button size="mini" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增</el-button>
          <el-button size="mini" type="primary" @click="handleEdit"><i class="fa fa-pencil-square-o" aria-hidden="true"/>修改</el-button>
          <el-button size="mini" type="success" @click="handleSort(3)"><i class="el-icon-sort" aria-hidden="true"/>重新排序</el-button>
          <el-button size="mini" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true"/>删除</el-button>
        </el-row>
        <el-table
          v-loading="xianquLoading"
          ref="xianquTable"
          :data="xianquLists"
          height="calc(100% - 60px)"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row
          @current-change="currentXianqu">
          <el-table-column label="排序" width="68">
            <template slot-scope="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column label="行政代码" width="80">
            <template slot-scope="scope">
              {{ scope.row.code }}
            </template>
          </el-table-column>
          <el-table-column :show-overflow-tooltip="true" label="全称">
            <template slot-scope="scope">
              {{ scope.row.fullName }}
            </template>
          </el-table-column>

          <el-table-column label="操作" width="140" align="center">
            <template slot-scope="scope">
              <el-button size="mini" plain @click="handleUp(scope.$index, scope.row)">上移</el-button>
              <el-button size="mini" plain @click="handleDown(scope.$index, scope.row)">下移</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="500px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="regionForm" :model="form" :rules="rules" size="small">
        <el-form-item :label-width="formLabelWidth" label="上级单位：" prop="fatherCode">
          <el-input v-model="form.fatherCode" disabled auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="行政代码：" prop="code">
          <el-input v-model="form.code" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="名称：" prop="name">
          <el-input v-model="form.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="全称：" prop="fullName">
          <el-input v-model="form.fullName" auto-complete="off"/>
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
import { getRegionLists, addRegion, updateRegion, deleteRegion, setOrderSortAll } from '@/api/region'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      shengfenLoading: false,
      chengshiLoading: false,
      xianquLoading: false,
      shengfenLists: [],
      chengshiLists: [],
      xianquLists: [],
      selectShengfen: {},
      selectChengshi: {},
      selectXianqu: {},
      dialogFormVisible: false,
      formLabelWidth: '100px',
      formTitle: '添加省市区',
      formIconClass: 'fa fa-user-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: { id: 0, type: 2, fatherCode: '', code: '', name: '', fullName: '', sort: '' },
      rules: {
        code: [
          { required: true, message: '请输入代码值', trigger: 'blur' }
        ],
        name: [
          { required: true, message: '请输入名称', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.getShengFen()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    this.getShengFen()
  },
  methods: {

    currentShengfen(currentRow, oldCurrentRow) {
      this.selectShengfen = currentRow
      this.getChengShi(currentRow.code)
    },
    currentChengshi(currentRow, oldCurrentRow) {
      this.selectChengshi = currentRow
      this.getXianqu(currentRow.code)
    },
    currentXianqu(currentRow, oldCurrentRow) {
      this.selectXianqu = currentRow
    },
    getShengFen() {
      this.shengfenLoading = true
      getRegionLists({ fatherCode: 100000 }).then(response => {
        this.shengfenLists = response.data
        this.shengfenLoading = false
        this.selectShengfen = this.shengfenLists[0]
        this.$refs.shengfenTable.setCurrentRow(this.shengfenLists[0])
        this.getChengShi(this.selectShengfen.code)
      })
    },
    getChengShi(shengfenCode) {
      this.chengshiLoading = true
      getRegionLists({ fatherCode: shengfenCode }).then(response => {
        this.chengshiLists = response.data
        this.chengshiLoading = false
        this.selectChengshi = this.chengshiLists[0]
        this.$refs.chengshiTable.setCurrentRow(this.chengshiLists[0])
        this.getXianqu(this.selectChengshi.code)
      })
    },
    getXianqu(chengshiCode) {
      this.xianquLoading = true
      getRegionLists({ fatherCode: chengshiCode }).then(response => {
        this.xianquLists = response.data
        this.xianquLoading = false
        this.selectXianqu = this.xianquLists[0]
        this.$refs.xianquTable.setCurrentRow(this.xianquLists[0])
      })
    },
    /**
     * 弹出添加县区信息对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-user-plus'
      this.formTitle = '添加县区信息'
      const sort = this.xianquLists.length + 1
      const fatherCode = this.selectChengshi.code
      // const code = this.xianquLists[this.xianquLists.length - 1].code + 1
      this.form = { id: 0, type: 3, fatherCode: fatherCode, code: 666666, name: '', sort: sort, status: 1 }
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改县区信息对话框
     */
    handleEdit() {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改县区信息'
      this.form = JSON.parse(JSON.stringify(this.selectXianqu))
      this.dialogFormVisible = true
    },
    /**
     * 弹出添加城市信息对话框
     */
    handleChengshiAdd() {
      this.formIconClass = 'fa fa-user-plus'
      this.formTitle = '添加城市信息'
      const sort = this.chengshiLists.length + 1
      const fatherCode = this.selectShengfen.code
      this.form = { id: 0, type: 2, fatherCode: fatherCode, code: 666666, name: '', sort: sort, status: 1 }
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改城市信息对话框
     */
    handleChengshiEdit() {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改城市信息'
      this.form = JSON.parse(JSON.stringify(this.selectChengshi))
      this.dialogFormVisible = true
    },

    handleChengshiDelete() {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRegion({ code: this.selectChengshi.code }).then(response => {
          this.getChengShi(this.selectShengfen.code)
          this.$message.success('删除成功')
        })
      }).catch(() => {
        this.$message.info('已取消删除')
      })
    },

    /**
     * 批量删除数据
     */
    handleDelete() {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteRegion({ code: this.selectXianqu.code }).then(response => {
          this.getXianqu(this.selectChengshi.code)
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
      this.$refs.regionForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addRegion(formdata).then(response => {
              this.dialogFormVisible = false
              if (formdata.type === 2) { this.getChengShi(formdata.fatherCode) }
              if (formdata.type === 3) { this.getXianqu(formdata.fatherCode) }
              this.$message({ type: 'success', message: '添加成功!' })
              this.formSumbitLoading = false
            })
          } else {
            updateRegion(formdata).then(response => {
              this.dialogFormVisible = false
              if (formdata.type === 2) { this.getChengShi(formdata.fatherCode) }
              if (formdata.type === 3) { this.getXianqu(formdata.fatherCode) }
              this.$message({ type: 'success', message: '修改成功!' })
              this.formSumbitLoading = false
            })
          }
        } else {
          return false
        }
      })
    },

    handleSort(type) {
      var params = { type: type, fatherCode: '100000' }
      if (parseInt(type) === 1) {
        params.fatherCode = '100000'
      }
      if (parseInt(type) === 2) {
        params.fatherCode = this.selectShengfen.code
      }
      if (parseInt(type) === 3) {
        params.fatherCode = this.selectChengshi.code
      }
      setOrderSortAll(params).then(response => {
        if (parseInt(type) === 1) {
          this.getShengFen()
        }
        if (parseInt(type) === 2) {
          this.getChengShi(this.selectShengfen.code)
        }
        if (parseInt(type) === 3) {
          this.getXianqu(this.selectChengshi.code)
        }
      }).catch(() => {})
    },
    /**
     *  上移
     */
    handleUp(index, row) {
      if (index > 0) {
        var ListData = this.shengfenLists
        if (parseInt(row.type) === 1) {
          ListData = this.shengfenLists
        }
        if (parseInt(row.type) === 2) {
          ListData = this.chengshiLists
        }
        if (parseInt(row.type) === 3) {
          ListData = this.xianquLists
        }
        var downData = JSON.parse(JSON.stringify(ListData[index]))
        var upData = JSON.parse(JSON.stringify(ListData[index - 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateRegion(upData).then(response => {})
        updateRegion(downData).then(response => {})
        ListData.splice(index - 1, 1)
        ListData[index - 1]['sort'] = downData['sort']
        ListData.splice(index, 0, upData)
      } else {
        this.$alert('已经是第一条，不可上移', '提示', { type: 'warning' })
      }
    },
    /**
     *  下移
     */
    handleDown(index, row) {
      var ListData = this.shengfenLists
      if (parseInt(row.type) === 1) {
        ListData = this.shengfenLists
      }
      if (parseInt(row.type) === 2) {
        ListData = this.chengshiLists
      }
      if (parseInt(row.type) === 3) {
        ListData = this.xianquLists
      }
      if ((index + 1) === ListData.length) {
        this.$alert('已经是最后一条，不可下移', '提示', { type: 'warning' })
      } else {
        var upData = JSON.parse(JSON.stringify(ListData[index]))
        var downData = JSON.parse(JSON.stringify(ListData[index + 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateRegion(upData).then(response => {})
        updateRegion(downData).then(response => {})
        ListData.splice(index + 1, 1)
        ListData.splice(index, 0, downData)
        ListData[index + 1]['sort'] = upData['sort']
      }
    }
  }
}
</script>
