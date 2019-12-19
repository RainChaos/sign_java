<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-row class="actionBtn">
        <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增分类</el-button>
        <el-button size="small" type="success" @click="fetchTypeData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
      </el-row>
      <tree-table v-loading="treeTableLoading" :data="typeTreeLists" :columns="columns" expand-all border height="calc(100% - 60px)">
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="success" plain @click="handleEdit(scope.$index,scope.row)"> 编辑</el-button>
            <el-button size="mini" type="danger" plain @click="handleDeleteRow(scope.$index,scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </tree-table>
    </el-row>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="500px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="dictionaryTypeForm" :model="form" :rules="rules" size="small">
        <el-form-item :label-width="formLabelWidth" label="上级分类：" prop="name">
          <el-select v-model="form.pid" placeholder="请选择类型" style="width:50%">
            <el-option :key="dingji.id" :value="dingji.id" :label="dingji.name"/>
            <el-option v-for="item in typeLists" v-if="item.id!=form.id" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="分类名称：" prop="name">
          <el-input v-model="form.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="标识代码：" prop="value">
          <el-input v-model="form.value" auto-complete="off"/>
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
import { getDictionaryLists, getDictionaryType, addDictionaryType, updateDictionaryType, deleteDictionaryType } from '@/api/dictionaryType'
import treeTable from '@/components/TreeTable'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = { id: 0, pid: 0, name: '', value: '', sort: 0 }
export default {
  components: { treeTable },
  data() {
    return {
      treeTableLoading: false,
      typeTreeLists: [],
      typeLists: [],
      dingji: { id: 0, name: '顶级分类' },
      columns: [
        {
          text: '分类名称',
          value: 'name',
          width: 300
        },
        {
          text: '分类代码值',
          value: 'value',
          width: 300
        },
        {
          text: '排序',
          value: 'sort',
          width: 100
        }
      ],
      form: Object.assign({}, defaultForm),
      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        value: [
          { required: true, message: '请输入分类标识', trigger: 'blur' }
        ]
      },
      formLabelWidth: '120px',
      formIconClass: 'fa fa-plus',
      dialogFormVisible: false,
      formTitle: '添加数据分类信息',
      formSumbitLoading: false
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchTypeData()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    this.fetchTypeData()
  },
  methods: {
    fetchTypeData() {
      this.treeTableLoading = true
      getDictionaryType().then(response => {
        this.typeTreeLists = response.data
        this.treeTableLoading = false
      }).catch(error => {
        this.treeTableLoading = false
        console.log(error)
      })
    },
    fetchListsData() {
      getDictionaryLists({ pid: 0 }).then(response => {
        this.typeLists = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    /**
     * 弹出添加对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-plus'
      this.formTitle = '添加数据分类信息'
      this.form = Object.assign({}, defaultForm)
      this.fetchListsData()
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改数据分类信息'
      var data = {}
      data['id'] = row.id
      data['pid'] = row.pid
      data['name'] = row.name
      data['value'] = row.value
      data['sort'] = row.sort
      this.form = JSON.parse(JSON.stringify(data))
      this.fetchListsData()
      this.dialogFormVisible = true
    },
    /**
     * 删除用户数据
     */
    handleDeleteRow(index, row) {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteDictionaryType({ id: row.id }).then(response => {
          this.fetchTypeData()
          this.$message.success('删除成功')
        }).catch(error => {
          console.log(error)
        })
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        })
      })
    },
    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs.dictionaryTypeForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addDictionaryType(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchTypeData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(() => {})
          } else {
            updateDictionaryType(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchTypeData()
              this.$message.success('修改成功')
              this.formSumbitLoading = false
            }).catch(() => {})
          }
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
