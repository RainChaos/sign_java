<template>
  <div style="height: 100%;">
    <el-row class="actionBtn">
      <el-form ref="form" :inline="true" :model="params" label-width="80px" size="small">
        <el-form-item label="批次：" label-width="66px">
          <el-select v-model="params.pici_id" placeholder="请选择季节批次" style="width:180px" @change="changePici">
            <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-button size="small" type="success" @click="handleSort"><i class="el-icon-sort" aria-hidden="true"/> 调整排序</el-button>
        <el-button size="small" type="success" @click="handleEditWidth"><i class="el-icon-sort" aria-hidden="true"/> 修改表单长度</el-button>
        <el-button size="small" type="success" @click="refreshData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
      </el-form>
    </el-row>
    <el-row style="height: calc(100% - 50px)">
      <el-table
        v-loading="listLoading"
        :data="listData"
        element-loading-text="正在加载中..."
        size="small"
        border
        fit
        height="100%"
        highlight-current-row>
        <el-table-column align="center" type="selection" width="50"/>
        <el-table-column align="center" label="排序" width="50">
          <template slot-scope="scope">
            {{ scope.row.sort }}
          </template>
        </el-table-column>
        <el-table-column label="英文标识" width="150">
          <template slot-scope="scope">
            {{ scope.row.fieldName }}
          </template>
        </el-table-column>
        <el-table-column label="中文名称" width="150">
          <template slot-scope="scope">
            {{ scope.row.describe }}
          </template>
        </el-table-column>
        <el-table-column label="应用场景" width="170">
          <template slot-scope="scope">
            {{ scope.row.formName }}
          </template>
        </el-table-column>
        <!-- <el-table-column label="字段类型" width="80">
          <template slot-scope="scope">
            {{ scope.row.type }}
          </template>
        </el-table-column>-->

        <el-table-column label="Label长度" width="100">
          <template slot-scope="scope">
            {{ scope.row.labelWidth }}
          </template>
        </el-table-column>
        <el-table-column label="长度" width="100">
          <template slot-scope="scope">
            {{ scope.row.width }}
          </template>
        </el-table-column>
        <el-table-column label="是否显示" width="80" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.isShow===false" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.isShow===true" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column label="禁止修改" width="80" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.isDisabed===false" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.isDisabed===true" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column label="是否必填" width="80" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.isMustInput===false" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.isMustInput===true" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column label="提示说明" width="200">
          <template slot-scope="scope">
            {{ scope.row.remark }}
          </template>
        </el-table-column>
        <el-table-column label="默认值">
          <template slot-scope="scope">
            {{ scope.row.value }}
          </template>
        </el-table-column>
        <el-table-column
          fixed="right"
          label="操作"
          width="200"
          align="center">
          <template slot-scope="scope">
            <el-button size="mini" plain @click="handleUp(scope.$index, scope.row)">上移</el-button>
            <el-button size="mini" plain @click="handleDown(scope.$index, scope.row)">下移</el-button>
            <el-button type="success" size="mini" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="StudentFormFieldForm" :rules="rules" :model="form" size="small">
        <el-form-item style="margin-bottom: 0px;">
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否显示：">
              <el-switch
                v-model="form.isShow"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="禁止修改：">
              <el-switch
                v-model="form.isDisabed"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否必填：">
              <el-switch
                v-model="form.isMustInput"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="英文标识：" prop="fieldName">
          <el-input :disabled="form.id>0" v-model="form.fieldName" auto-complete="off" placeholder="请输入字段标识名称"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="中文名称：" prop="describe">
          <el-input v-model="form.describe" auto-complete="off" placeholder="请输入字段中文名称"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="默认值：" prop="remark">
          <el-input v-model="form.value" auto-complete="off" placeholder="请输入默认值"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="提示说明：" prop="remark">
          <el-input v-model="form.remark" auto-complete="off" placeholder="请输入字段说明描述"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="Label长度：" prop="width">
          <el-input-number v-model="form.labelWidth" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="表单长度：" prop="width">
          <el-input-number v-model="form.width" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：" prop="sort">
          <el-input-number v-model="form.sort" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="字段验证规则：" prop="rules">
          <el-input v-model="form.rules" :rows="4" type="textarea" auto-complete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialogDrag :visible.sync="dialogFormWidthVisible" width="600px">
      <div slot="title"><i class="el-icon-setting"/>设置表单长度参数</div>
      <el-form ref="StudentFormFieldWidthForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="Label长度：" prop="lableWidth">
          <el-input-number v-model="widthForm.labelWidth" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="表单长度：" prop="width">
          <el-input-number v-model="widthForm.width" auto-complete="off"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormWidthVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmitWidth">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getStudentFormFieldLists, updateStudentFormField, updateStudentFormFieldWidth, setOrderSortAll } from '@/api/studentFormField'
import { getPiciLists } from '@/api/pici'
const defaultForm = {
  id: 0,
  fieldName: '',
  describe: '',
  /* dictionary_type: [],*/
  piciId: localStorage.getItem('piciId'),
  sort: 1,
  isShow: 1,
  isMustInput: 1,
  isDisabed: 0,
  status: 1
}
export default {
  props: {
    formName: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      piciLists: [],
      dataTypeLists: [],
      dictionaryTypeLists: [],
      widthForm: {
        labelWidth: 150,
        width: 400
      },
      cascaderProps: {
        value: 'value',
        label: 'name'
      },
      listData: [],
      listLoading: true,
      params: {
        isShow: 1,
        pici_id: 0
      },
      statusSwitch: {
        activeValue: true,
        inactiveValue: false
      },
      dialogFormWidthVisible: false,
      dialogFormVisible: false,
      formLabelWidth: '140px',
      formTitle: '添加扩展字段',
      formIconClass: 'fa fa-plus',
      formSumbitLoading: false,
      form: Object.assign({}, defaultForm),
      rules: {
        /*lableWidth: [
          {required: true, message: '请输入Lable长度', trigger: 'blur' }
        ],
        width: [
          {required: true, message: '请输入表单长度', trigger: 'blur' }
        ]*/
      }
    }
  },
  created() {
    var _this = this
    _this.params.pici_id = _this.$store.getters.currentPici
    localStorage.setItem('piciId', this.$store.getters.currentPici)
    this.fetchData(this.formName)
    this.fetchPiciData()
  },
  methods: {
    changePici(value) {
      localStorage.setItem('piciId', value)
      this.$emit('fetchSelectFieldDefaultSelect')
      this.fetchData(this.formName)
    },
    refreshData() {
      this.fetchData(this.formName)
    },
    fetchPiciData() {
      getPiciLists().then(response => {
        this.piciLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchData(formName) {
      this.listLoading = true
      getStudentFormFieldLists({ formName: formName, piciId: localStorage.getItem('piciId') }).then(response => {
        this.listData = response.data
        this.listLoading = false
      })
    },
    handleSort() {
      setOrderSortAll({ formName: this.formName, piciId: localStorage.getItem('piciId') }).then(response => {
        this.refreshData()
        this.$message.success('调整成功')
      }).catch(() => {})
    },
    /**
     *  上移
     */
    handleUp(index, row) {
      if (index > 0) {
        var downData = JSON.parse(JSON.stringify(this.listData[index]))
        var upData = JSON.parse(JSON.stringify(this.listData[index - 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateStudentFormField(upData).then(response => {})
        updateStudentFormField(downData).then(response => {})
        this.listData.splice(index - 1, 1)
        this.listData[index - 1]['sort'] = downData['sort']
        this.listData.splice(index, 0, upData)
      } else {
        this.$alert('已经是第一条，不可上移', '提示', { type: 'warning' })
      }
    },
    /**
     *  下移
     */
    handleDown(index, row) {
      if ((index + 1) === this.listData.length) {
        this.$alert('已经是最后一条，不可下移', '提示', { type: 'warning' })
      } else {
        var upData = JSON.parse(JSON.stringify(this.listData[index]))
        var downData = JSON.parse(JSON.stringify(this.listData[index + 1]))
        var upSort = upData['sort']
        upData['sort'] = downData['sort']
        downData['sort'] = upSort
        updateStudentFormField(upData).then(response => {})
        updateStudentFormField(downData).then(response => {})
        this.listData.splice(index + 1, 1)
        this.listData.splice(index, 0, downData)

        this.listData[index + 1]['sort'] = upData['sort']
      }
    },
    /**
       * 弹出修改管理员信息对话框
       */
    handleEditWidth(index, row) {
      this.dialogFormWidthVisible = true
    },
    /**
       * 弹出修改管理员信息对话框
       */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改表单字段信息'
      var formdata = JSON.parse(JSON.stringify(row))
      this.form = formdata
      this.dialogFormVisible = true
    },

    /**
       * 提交添加或者修改管理员信息
       */
    handleSubmit() {
      this.$refs.StudentFormFieldForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          formdata['piciId'] = localStorage.getItem('piciId')
          console.log(formdata)
          this.formSumbitLoading = true
          if (formdata.id > 0) {
            updateStudentFormField(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchData(this.formName)
              this.$message.success('修改成功')
              this.formSumbitLoading = false
            })
          }
        } else {
          return false
        }
      })
    },

    handleSubmitWidth() {
      this.$refs.StudentFormFieldWidthForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.widthForm))
          this.formSumbitLoading = true
          formdata['formName'] = this.formName
          formdata['piciId'] = localStorage.getItem('piciId')
          updateStudentFormFieldWidth(formdata).then(response => {
            this.dialogFormWidthVisible = false
            this.fetchData(this.formName)
            this.$message.success('修改成功')
            this.formSumbitLoading = false
          }).catch(error => { this.formSumbitLoading = false })
        }
      })
    }
  }
}
</script>
