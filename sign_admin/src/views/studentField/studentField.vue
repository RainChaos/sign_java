<template>
  <div class="app-container" style="height: 100%;">
    <el-row class="actionBtn">
      <el-form ref="form" :inline="true" :model="params" label-width="80px" size="small">
        <el-form-item label="字段名称" style="margin-bottom: 0">
          <el-input v-model="params.keyword"/>
        </el-form-item>
        <el-button size="small" type="success" @click="fetchData"><i class="el-icon-search" aria-hidden="true"/> 查 询</el-button>
        <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增字段</el-button>
      </el-form>
    </el-row>
    <el-row style="height: calc(100% - 60px);">
      <el-table
        v-loading="listLoading"
        :data="listData"
        element-loading-text="正在加载中..."
        size="small"
        border
        fit
        height="100%"
        highlight-current-row>
        <el-table-column align="center" label="序号" width="60">
          <template slot-scope="scope">{{ scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column label="字段类型" width="150">
          <template slot-scope="scope">
            {{ scope.row.type }}
          </template>
        </el-table-column>
        <el-table-column label="英文标识" width="180">
          <template slot-scope="scope">
            {{ scope.row.fieldName }}
          </template>
        </el-table-column>
        <el-table-column label="中文名称" width="150">
          <template slot-scope="scope">
            {{ scope.row.describe }}
          </template>
        </el-table-column>
        <el-table-column label="表格列长度" width="100">
          <template slot-scope="scope">
            {{ scope.row.width }}
          </template>
        </el-table-column>
        <el-table-column label="数据库字段类型" width="150">
          <template slot-scope="scope">
            {{ scope.row.columnType }}
          </template>
        </el-table-column>
        <el-table-column label="系统数据类型" width="220">
          <template slot-scope="scope">
            <span v-if="scope.row.dataType!='标准数据'">{{ scope.row.dataType }}</span>
            <span v-if="scope.row.dataType=='标准数据'">
              {{ scope.row.dataType }}【{{ scope.row.dictionaryType[1] }}】
            </span>
          </template>
        </el-table-column>
        <el-table-column label="是否显示" width="80" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.isShow===false" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.isShow===true" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column label="是否唯一" width="80" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.isUnique===false" class="el-icon-close" style="color:firebrick"/>
            <i v-if="scope.row.isUnique===true" class="el-icon-check" style="color:forestgreen"/>
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作">
          <template slot-scope="scope">
            <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
            <el-button
              type="danger"
              size="mini"
              plain
              @click="handleDeleteRow(scope.$index, scope.row)">删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="stuFieldForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="英文标识：" prop="fieldName">
          <el-input :disabled="form.id>0" v-model="form.fieldName" auto-complete="off" placeholder="请输入字段标识名称"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="中文名称：" prop="describe">
          <el-input v-model="form.describe" auto-complete="off" placeholder="请输入字段中文名称"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="数据库字段类型：" prop="columnType">
          <el-input v-model="form.columnType" auto-complete="off" placeholder="请输入数据库字段类型，如：varchar(200)"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="表格列长度：" prop="width">
          <el-input-number v-model="form.width" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="数据类型：" prop="dataType">
          <el-select v-model="form.dataType" placeholder="请选择数据类型" style="width:300px;">
            <el-option v-for="item in dataTypeLists" :key="item.code" :label="item.name" :value="item.code"/>
          </el-select>
        </el-form-item>
        <el-form-item
          v-if="form.dataType == '省市区'"
          :label-width="formLabelWidth"
          label="事件："
          prop="event_name">
          <el-input v-model="form.event_name" auto-complete="off" placeholder="请输入字段事件名称"/>
        </el-form-item>
        <el-form-item
          v-if="form.dataType == '标准数据'"
          :label-width="formLabelWidth"
          label="标准类型："
          prop="dictionaryType">
          <el-cascader
            v-model="form.dictionaryType"
            :options="dictionaryTypeLists"
            :props="cascaderProps"
            placeholder="请选择标准类型"
            clearable
            style="width:300px;"
          />
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="是否显示：">
          <el-switch
            v-model="form.isShow"
            :active-value="statusSwitch.activeValue"
            :inactive-value="statusSwitch.inactiveValue"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="是否唯一：">
          <el-switch
            v-model="form.isUnique"
            :active-value="statusSwitch.activeValue"
            :inactive-value="statusSwitch.inactiveValue"/>
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
import { getStudentFieldLists, addStudentField, updateStudentField, deleteStudentField } from '@/api/studentField'
import { getUseDictionaryLists } from '@/api/dictionary'
import { getDictionaryType } from '@/api/dictionaryType'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'

const defaultForm = {
  id: 0,
  fieldName: '',
  describe: '',
  type: '扩展数据',
  columnType: 'varchar(50)',
  dataType: '字符串',
  dictionaryType: [],
  sort: 1,
  isUnique: 0,
  isShow: 0
}
export default {
  data() {
    return {
      dataTypeLists: [],
      dictionaryTypeLists: [],
      params: {
        keyword: ''
      },
      cascaderProps: {
        value: 'value',
        label: 'name'
      },
      listData: [],
      listLoading: true,
      statusSwitch: {
        activeValue: true,
        inactiveValue: false,
        activeColor: '#13ce66',
        inactiveColor: '#ff4949'
      },
      dialogFormVisible: false,
      formLabelWidth: '140px',
      formTitle: '添加扩展字段',
      formIconClass: 'fa fa-plus',
      formSumbitLoading: false,
      form: Object.assign({}, defaultForm),
      rules:
      {
        fieldName: [
          { required: true, message: '请输入字段标识', trigger: 'blur' }
        ],
        describe:
        [
          { required: true, message: '请输入中文名称', trigger: 'blur' }
        ],
        columnType:
        [
          { required: true, message: '请输入数据库字段类型', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
  },
  created() {
    setCurrentUrl(this.$route.path)
    this.fetchDictionaryData()
    this.fetchData()
  },
  methods: {
    fetchDictionaryData() {
      getUseDictionaryLists({ type: 'SJLXDM' }).then(response => {
        this.dataTypeLists = response.data.data
      })
      getDictionaryType().then(response => {
        this.dictionaryTypeLists = response.data
        console.log('---')

        console.log(this.dictionaryTypeLists)
      })
    },
    fetchData() {
      this.listLoading = true
      getStudentFieldLists(this.params).then(response => {
        this.listData = response.data
        console.log(this.listData)
        this.listLoading = false
      })
    },
    /**
       * 弹出添加管理员信息对话框
       */
    handleAdd() {
      this.formIconClass = 'fa fa-plus'
      this.formTitle = '添加扩展字段'
      var sort = this.listData.length + 1
      this.form = Object.assign({}, defaultForm)
      this.form.sort = sort
      this.dialogFormVisible = true
    },
    /**
       * 弹出修改管理员信息对话框
       */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改字段信息'
      var formdata = JSON.parse(JSON.stringify(row))
      console.log(formdata)
      this.form = formdata
      this.dialogFormVisible = true
    },
    /**
       * 删除用户数据
       */
    handleDeleteRow(index, row) {
      this.deleteDatas(row.id)
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
        deleteStudentField({ id: ids }).then(response => {
          this.fetchData()
          this.$message.success('删除成功')
        })
      }).catch(() => {
        this.$message({ type: 'info', message: '已取消删除' })
      })
    },

    /**
       * 提交添加或者修改管理员信息
       */
    handleSubmit() {
      this.$refs.stuFieldForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          const dictionaryType = formdata.dictionaryType
          formdata.dictionaryType = dictionaryType[dictionaryType.length - 1]
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addStudentField(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(error => { this.formSumbitLoading = false })
          } else {
            updateStudentField(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('修改成功')
              this.formSumbitLoading = false
            }).catch(error => { this.formSumbitLoading = false })
          }
        }
      })
    }
  }
}
</script>
