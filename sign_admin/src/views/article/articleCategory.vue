<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-row class="actionBtn">
        <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增分类</el-button>
        <el-button size="small" type="success" @click="fetchTypeData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
      </el-row>
      <tree-table :data="typeTreeLists" :columns="columns" expand-all border>
        <el-table-column label="分类标识" width="150">
          <template slot-scope="scope">
            {{ scope.row.code }}
          </template>
        </el-table-column>
        <el-table-column label="分类描述" width="400">
          <template slot-scope="scope">
            {{ scope.row.description }}
          </template>
        </el-table-column>
        <el-table-column label="是否启用" width="80" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.status===false" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.status===true" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column label="排序" width="100" align="center">
          <template slot-scope="scope">
            {{ scope.row.sort }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button size="mini" type="success" plain @click="handleEdit(scope.$index,scope.row)"> 编辑</el-button>
            <el-button size="mini" type="danger" plain @click="handleDeleteRow(scope.$index,scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </tree-table>
    </el-row>
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="articleCategoryForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="上级分类：" prop="fatherId">
          <el-select v-model="form.fatherId" placeholder="请选择类型" style="width:100%">
            <el-option :key="dingji.id" :value="dingji.id" :label="dingji.name"/>
            <el-option v-for="item in typeLists" v-if="item.id!=form.id" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="分类名称：" prop="name">
          <el-input v-model="form.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="分类代码：" prop="code">
          <el-input v-model="form.code" auto-complete="off" placeholder="请输入分类代码，唯一标识分类的代码"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：" prop="sort">
          <el-input-number v-model="form.sort" :min="1" :max="1000"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="分类描述：">
          <el-input :rows="2" v-model="form.description" type="textarea" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="SEO标题：">
          <el-input v-model="form.seoTitle" auto-complete="off" placeholder="请输入seo标题，主要用于搜素引擎优化"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="SEO关键词：">
          <el-input v-model="form.seoKeywords" auto-complete="off" placeholder="请输入seo关键词，如：中国航母,人工智能"/>
        </el-form-item>
        <el-form-item>
          <el-col :span="8"/>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否单主题分类：">
              <el-switch
                v-model="form.isOne"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否启用：">
              <el-switch
                v-model="form.status"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
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
import { getArticleCategoryTree, getArticleCategoryLists, addArticleCategory, updateArticleCategory, deleteArticleCategory } from '@/api/articleCategory'
import treeTable from '@/components/TreeTable'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  fatherId: 0,
  name: '',
  code: '',
  sort: 1,
  status: 1,
  isOne: 0,
  description: '',
  seoTitle: '',
  seoKeywords: '',
  thumb: ''
}
export default {
  components: { treeTable },
  data() {
    return {
      typeTreeLists: [],
      typeLists: [],
      dingji: { id: 0, name: '顶级分类' },
      columns: [
        {
          text: '分类名称',
          value: 'name',
          width: 250
        }
      ],
      statusSwitch: {
        activeValue: true,
        inactiveValue: false
      },
      form: Object.assign({}, defaultForm),
      rules: {
        name: [
          { required: true, message: '请输入分类名称', trigger: 'blur' }
        ],
        code: [
          { required: true, message: '请输入分类标识', trigger: 'blur' }
        ]
      },
      formLabelWidth: '130px',
      formIconClass: 'fa fa-plus',
      dialogFormVisible: false,
      formTitle: '添加文章分类信息',
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
      getArticleCategoryTree().then(response => {
        this.typeTreeLists = response.data
      })
    },
    fetchListsData() {
      getArticleCategoryLists({ fatherId: 0 }).then(response => {
        this.typeLists = response.data
      })
    },
    /**
     * 弹出添加对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-plus'
      this.formTitle = '添加文章分类信息'
      this.form = Object.assign({}, defaultForm)
      this.fetchListsData()
      this.dialogFormVisible = true
    },
    /**
     * 弹出修改对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改文章分类信息'
      var data = {}
      data['id'] = row.id
      data['fatherId'] = row.fatherId
      data['name'] = row.name
      data['code'] = row.code
      data['status'] = row.status
      data['isOne'] = row.isOne
      data['sort'] = row.sort
      data['description'] = row.description
      data['seoTitle'] = row.seoTitle
      data['seoKeywords'] = row.seoKeywords
      data['thumb'] = row.thumb
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
        deleteArticleCategory({ id: row.id }).then(response => {
          this.fetchTypeData()
          this.$alert('删除成功', '提示', { type: 'success' })
        }).catch(error => {
          console.log(error)
        })
      }).catch(error => {
        console.log(error)
      })
    },
    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs.articleCategoryForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addArticleCategory(formdata).then(response => {
              this.dialogFormVisible = false
              this.fetchTypeData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(() => {})
          } else {
            updateArticleCategory(formdata).then(response => {
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
