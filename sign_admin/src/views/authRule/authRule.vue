<template>
  <div class="app-container" style="height: 100%">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增权限</el-button>
      <el-button size="small" type="success" plain @click="fetchData"> 刷新</el-button>
    </el-row>
    <div style="height:calc(100% - 60px)">
      <tree-table :data="treeListsData" :columns="columns" expand-all border height="100%">
        <el-table-column label="权限路径" width="300">
          <template slot-scope="scope">
            {{ scope.row.url }}
          </template>
        </el-table-column>
        <el-table-column label="备注信息" width="300">
          <template slot-scope="scope">
            {{ scope.row.remark }}
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
            <el-button size="mini" type="success" plain @click="handleView(scope.$index,scope.row)"> 接口查看</el-button>
            <el-button size="mini" type="success" plain @click="handleAddInterface(scope.$index,scope.row)"> 接口添加</el-button>
            <el-button size="mini" type="danger" plain @click="handleDeleteRow(scope.$index,scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </tree-table>
    </div>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="AuthRuleForm" :rules="rules" :model="form" size="small">
        <el-form-item :label-width="formLabelWidth" label="上级分类：" prop="father_id" v-if="form.father_id != '0'">
          <!-- <el-cascader
            :options="typeListsOptions"
            :props="typeProps"
            v-model="form.father_id"
            :change-on-select="true"
            style="width:100%"
            clearable
            placeholder="请选择上级分类"/> -->
            <el-select v-model="form.father_id" placeholder="请选择">
              <el-option
                v-for="item in authParentList"
                :key="item.id"
                :label="item.name"
                :value="item.id">
              </el-option>
            </el-select>

        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="权限名称：" prop="name">
          <el-input v-model="form.name" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="权限路径：" prop="url">
          <el-input v-model="form.url" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：" prop="sort">
          <el-input-number v-model="form.sort" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="备注信息：" prop="remark">
          <el-input v-model="form.remark" auto-complete="off" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog  v-dialogDrag :visible.sync="addVisible" width="800px">
      <div slot="title"><i :class="formIconClass"/> 新增功能接口</div>

      <el-form :model="addForm" label-width="80px" :rules="addFormRules" size="small">
        <el-form-item label="接口地址">
          <el-input v-model="addForm.url"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="addForm.remark"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitAddForm(addForm)">创建</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

    <el-dialog v-dialogDrag :visible.sync="viewVisible" width="800px">
      <div slot="title"><i :class="formIconClass"/> 接口列表</div>
      <!--      <ul v-for="item in interfaceList" :key="item.id" :value="item.id">-->
      <!--        {{item.auth_rule_url}}-->
      <!--      </ul>-->
      <el-table :data="interfaceList" style="width: 100%">
        <el-table-column
                prop="auth_rule_url"
                label="接口地址"
        >
        </el-table-column>
        <el-table-column
                prop="auth_rule_url_remark"
                label="备注"
        >
        </el-table-column>
        <el-table-column label="备注">
          <template slot-scope="scope">
            <el-button size="mini" type="text"  @click="handleInterfaceEdit(scope.$index, scope.row)"> 编辑</el-button>
            <el-button size="mini" type="text"  @click="handleInterfaceDelete(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>

      </el-table>
    </el-dialog>

    <el-dialog  v-dialogDrag :visible.sync="editInterfaceVisible" width="800px">
      <div slot="title"><i :class="formIconClass"/> 修改功能接口</div>

      <el-form :model="updateForm" label-width="80px" :rules="addFormRules" size="small">
        <el-form-item label="接口地址">
          <el-input v-model="updateForm.url"></el-input>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="updateForm.remark"></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="submitUpdateForm(updateForm)">更新</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>

  </div>
</template>

<script>
import { getAuthRuleLists, addAuthRule, updateAuthRule, deleteAuthRule, getAuthRuleSort, getAuthRuleParentList, getAuthRuleInterface, addAuthRuleInterface, deleteAuthRuleInterface, updateAuthRuleInterface } from '@/api/authRule'
import treeTable from '@/components/TreeTable'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  father_id: [],
  type: 1,
  name: '',
  url: '',
  sort: 1,
  status: 1,
  remark: ''
}
export default {
  components: { treeTable },
  data() {
    return {
      editInterfaceVisible: false,
      addFormRules: {
        url: [
          { required: true, message: '请输入', trigger: 'blur' },
        ],
        remark: [
          { required: true, message: '请输入', trigger: 'blur' },
        ],
      },
      addForm: {url: '', remark: '', id : ''},
      updateForm: {url: '', remark: '', id : ''},
      interfaceList: [],
      addVisible: false,
      viewVisible: false,
      authParentList: [],
      treeLoading: false,
      treeListsData: [],
      columns: [
        {
          text: '权限名称',
          value: 'name',
          width: 250
        }
      ],
      typeProps: {
        value: 'id',
        label: 'name',
        children: 'children'
      },
      typeListsOptions: [],
      dingji: { id: 0, name: '顶级分类' },
      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0
      },
      form: Object.assign({}, defaultForm),
      rules: {
        name: [
          { required: true, message: '请输入权限名称', trigger: 'change' }
        ]
      },
      formLabelWidth: '130px',
      formIconClass: 'fa fa-plus',
      dialogFormVisible: false,
      formTitle: '添加权限信息',
      formSumbitLoading: false,
      currentRowId: ''
    }
  },
  activated(){
    setCurrentUrl('/system/authRule')
    this.fetchData()
    this.fetchAuthRuleParentList(0)
  },
  mounted() {
    this.fetchData()
    this.fetchAuthRuleParentList(0)
  },
  methods: {
    handleInterfaceEdit(index, row){
      this.editInterfaceVisible = true
      this.updateForm.id = row.id
      this.updateForm.url = row.auth_rule_url
      this.updateForm.remark = row.auth_rule_url_remark
    },
    handleInterfaceDelete(index, row){
      deleteAuthRuleInterface({id: row.id}).then(response => {
        this.$message.success('删除成功')
        this.fetchAuthRuleInterface(this.currentRowId)
      })
    },
    fetchData() {
      this.treeLoading = true
      getAuthRuleLists().then(response => {
        this.treeListsData = response.data
        // console.log(this.treeListsData)
        this.treeLoading = false
      }).catch(error => {
        console.log(error)
      })
    },
    fetchListsData() {
      getAuthRuleLists().then(response => {
        this.typeListsOptions = response.data
      })
    },
    fetchAuthRuleParentList(value){
      getAuthRuleParentList().then(response => {
        this.authParentList = response.data
        this.form.father_id = value
      })
    },
    /**
     * 弹出添加对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-plus'
      this.formTitle = '添加权限信息'
      this.form = Object.assign({}, defaultForm)
      this.father_id = []
      this.fetchListsData()
      this.dialogFormVisible = true
    },
    handleAddInterface(index, row){
      this.addVisible = true
      this.addForm.id = row.id
    },
    submitUpdateForm(updateForm){
      updateAuthRuleInterface(updateForm).then(response => {
        this.editInterfaceVisible = false
        this.fetchAuthRuleInterface(this.currentRowId)
        this.$message.success('修改成功')
      })
    },
    submitAddForm(form){
      addAuthRuleInterface(form).then(response => {
        this.addVisible = false
        this.$message.success('添加成功')
      })
    },
    handleView(index, row){
      this.viewVisible = true
      this.currentRowId = row.id
      this.fetchAuthRuleInterface(row.id)
    },
    fetchAuthRuleInterface(id){
      getAuthRuleInterface({id: id}).then(response => {
        this.interfaceList = response.data
      })
    },
    /**
     * 弹出修改对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改权限信息'
      var data = {}
      data['id'] = row.id
      data['father_id'] = row.father_id
      data['type'] = row.type
      data['name'] = row.name
      data['url'] = row.url
      data['status'] = row.status
      data['sort'] = row.sort
      data['remark'] = row.remark
      this.form = JSON.parse(JSON.stringify(data))
      this.fetchAuthRuleParentList(row.father_id)
      // console.log(this.form);
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
        deleteAuthRule({ ids: row.id }).then(response => {
          this.fetchData()
          this.$message.success('删除成功')
        }).catch(() => {})
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
      this.$refs.AuthRuleForm.validate((valid) => {
        if (valid) {
          var formdata = JSON.parse(JSON.stringify(this.form))
          var father = formdata.father_id

          formdata.type = formdata.father_id.length + 1
          formdata.status = 1
          if (father.length === 0) {
            formdata.father_id = 0
          } else {
            formdata.father_id = father[father.length - 1]
          }

          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addAuthRule(this.form).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('添加成功')
              this.formSumbitLoading = false
            }).catch(() => {
              this.formSumbitLoading = false
            })
          } else {
            updateAuthRule(this.form).then(response => {
              this.dialogFormVisible = false
              this.fetchData()
              this.$message.success('修改成功')
              this.formSumbitLoading = false
            }).catch(() => {
              this.formSumbitLoading = false
            })
          }
        }
      })
    }

  }
}
</script>

<style scoped>

</style>
