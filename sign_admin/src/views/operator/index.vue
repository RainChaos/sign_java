<template>
  <div class="app-container" style="height: 100%">
    <el-row class="actionBtn">
      <el-button size="small" type="primary" @click="handleAdd"><i class="el-icon-plus" aria-hidden="true"/> 新增用户</el-button>
      <!-- <el-button size="small" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true"/> 批量删除</el-button> -->
      <el-button size="small" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
    </el-row>
    <el-row style="height: calc(100% - 90px)">
      <el-table
        v-loading="listLoading"
        ref="multipleTable"
        :data="list.data"
        height="100%"
        element-loading-text="正在加载中..."
        size="small"
        border
        fit
        highlight-current-row
        @selection-change="handleSelectionChange">
        <el-table-column align="center" type="selection" width="50"/>
        <el-table-column align="center" label="ID" width="60">
          <template slot-scope="scope">{{ scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column label="角色" width="120">
          <template slot-scope="scope">{{ scope.row.group_name }}</template>
        </el-table-column>
        <el-table-column label="用户名" width="120">
          <template slot-scope="scope">{{ scope.row.username }}</template>
        </el-table-column>
        <el-table-column label="真实名字" width="120">
          <template slot-scope="scope"><span>{{ scope.row.realname }}</span></template>
        </el-table-column>
        <el-table-column label="登陆后的默认地址" width="200">
          <template slot-scope="scope"><span>{{ scope.row.default_url }}</span></template>
        </el-table-column>
        <el-table-column label="手机号码" width="150" align="center">
          <template slot-scope="scope">{{ scope.row.phone }}</template>
        </el-table-column>
        <el-table-column align="center" label="是否启用" width="80">
          <template slot-scope="scope">
            <i v-if="scope.row.status===0 || scope.row.status==='0' || scope.row.status===false" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.status===1 || scope.row.status==='1' || scope.row.status===true" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column align="center" label="创建时间" width="200">
          <template slot-scope="scope">
            <i class="el-icon-time"/><span>{{ scope.row.create_time }}</span>
          </template>
        </el-table-column>
        <el-table-column align="center" label="更新时间" width="200">
          <template slot-scope="scope">
            <i class="el-icon-time"/><span>{{ scope.row.update_time }}</span>
          </template>
        </el-table-column>

        <!--<el-table-column align="center" label="是否单页" width="80">-->
          <!--<template slot-scope="scope">-->
            <!--<i v-if="scope.row.is_one===0 || scope.row.is_one==='0' || scope.row.is_one===false" class="el-icon-close" style="color:firebrick" />-->
            <!--<i v-if="scope.row.is_one===1 || scope.row.is_one==='1' || scope.row.is_one===true" class="el-icon-check" style="color:forestgreen" />-->
          <!--</template>-->
        <!--</el-table-column>-->
        <el-table-column
          fixed="right"
          label="操作"
          align="center"
          width="320">
          <template slot-scope="scope">
            <el-button size="mini" type="success" plain @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
            <el-button size="mini" type="success" plain @click="handleGroup(scope.$index, scope.row)"> 设置角色</el-button>
            <el-button size="mini" type="success" plain @click="handleRules(scope.$index, scope.row)"> 设置权限</el-button>
            <el-button size="mini" type="danger" plain @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-row>
    <el-row v-if="pagination.total>0">
      <el-pagination
        :current-page="pagination.current_page"
        :page-sizes="[15,30, 50, 100]"
        :page-size="pagination.pre_page"
        :total="pagination.total"
        background
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"/>
    </el-row>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="500px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="operatorForm" :model="form" :rules="rules" size="small">
        <el-form-item :label-width="formLabelWidth" label="用户名：" prop="username">
          <el-input v-model="form.username" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="真实姓名：" prop="realname">
          <el-input v-model="form.realname" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="默认地址：" prop="phone">
          <el-input v-model="form.default_url" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="电话号码：" prop="phone">
          <el-input v-model="form.phone" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="密码：" prop="password">
          <el-input v-model="form.password" auto-complete="off"/>
        </el-form-item>
        <el-form-item>
          <el-col :span="12">
            <el-form-item :label-width="formLabelWidth" label="是否启用：">
              <el-switch
                v-model="form.status"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <!--<el-col :span="12">-->
            <!--<el-form-item :label-width="formLabelWidth" label="是否单页：">-->
              <!--<el-switch-->
                <!--v-model="form.is_one"-->
                <!--:active-value="statusSwitch.activeValue"-->
                <!--:inactive-value="statusSwitch.inactiveValue"/>-->
            <!--</el-form-item>-->
          <!--</el-col>-->
        </el-form-item>

      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialogDrag :visible.sync="dialogAuthGroupVisible" width="800px">
      <div slot="title"><i class="fa fa-cogs"/> 设置用户角色</div>
      <el-table
        v-loading="listGroupLoading"
        ref="multipleGroupTable"
        :data="authGroupList"
        element-loading-text="正在加载中..."
        border
        fit
        highlight-current-row
        @selection-change="handleSelectionGroupChange">
        <el-table-column align="center" type="selection" width="50"/>
        <el-table-column label="角色名称" width="110"><template slot-scope="scope">{{ scope.row.name }}</template></el-table-column>
        <el-table-column label="类型" width="110"><template slot-scope="scope">{{ scope.row.type }}</template></el-table-column>
        <el-table-column label="备注"><template slot-scope="scope">{{ scope.row.description }}</template></el-table-column>
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogAuthGroupVisible = false">关 闭</el-button>
        <el-button :loading="formSumbitAuthGroupLoading" size="small" type="primary" @click="handleSubmitGroup">保 存</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getOperatorList, updateStatus, addOperator, updateOperator, deleteOperator, assignAuthGroup } from '@/api/operator'
import { getAuthGroupLists } from '@/api/authGroup'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
const defaultForm = {
  id: 0,
  username: '',
  realname: '',
  default_url: '/main',
  phone: '',
  status: 1,
  is_one: 0,
  password: ''
}
export default {
  data() {
    var validatePass = (rule, value, callback) => {
      if (this.form.id === 0 && value === '') {
        callback(new Error('请输入密码'))
      } else {
        callback()
      }
    }
    return {
      list: {},
      pagination: {
        current_page: 0,
        pre_page: 15,
        total: 0
      },
      params: {
        limit: 15,
        page: 1
      },
      multipleSelection: [],
      listLoading: true,
      statusSwitch: {
        activeValue: '1',
        inactiveValue: '0',
        activeColor: '#13ce66',
        inactiveColor: '#ff4949'
      },
      dialogFormVisible: false,
      formLabelWidth: '100px',
      formTitle: '添加管理员',
      formIconClass: 'fa fa-user-plus',
      formSumbitLoading: false,
      formSumbitAuthGroupLoading: false,
      form: Object.assign({}, defaultForm),
      rules: {
        username: [
          { required: true, message: '请输入用户名称', trigger: 'blur' },
          { min: 5, max: 18, message: '长度在 5 到 18 个字符', trigger: 'blur' }
        ],
        realname: [
          { required: true, message: '请输入用户真实姓名', trigger: 'blur' },
          { min: 2, max: 10, message: '长度在 2 到 10 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, validator: validatePass, trigger: 'blur' }
        ]
      },
      listGroupLoading: false,
      dialogAuthGroupVisible: false,
      authGroupList: [],
      multipleGroupSelection: [],
      formOperatorGroup: {
        operator_id: 0,
        auth_group_id: ''
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchData()
  },
  created() {
    this.fetchData()
    setCurrentUrl(this.$route.path)
  },
  methods: {
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fetchData() {
      this.listLoading = true
      getOperatorList( this.params ).then(response => {
        this.list = response.data
        console.log(this.list.data)
        // this.pagination.current_page = response.data.current_page
        this.pagination.total = response.data.total
        this.listLoading = false
      })
    },
    handleSizeChange(val) {
      this.pagination.pre_page = val
      this.params.limit = this.pagination.pre_page
      this.params.page = 1
      this.fetchData()
    },
    handleCurrentChange(val) {
      this.params.limit = this.pagination.pre_page
      this.params.page = val
      this.fetchData()
    },
    /**
     * 弹出添加管理员信息对话框
     */
    handleAdd() {
      this.formIconClass = 'fa fa-user-plus'
      this.formTitle = '添加管理员信息'
      this.form = Object.assign({}, defaultForm)
      this.dialogFormVisible = true
      this.rules.password[0].required = true
    },
    /**
     * 弹出修改管理员信息对话框
     */
    handleEdit(index, row) {
      this.formIconClass = 'fa fa-pencil-square-o'
      this.formTitle = '修改管理员信息'
      this.form = JSON.parse(JSON.stringify(row))
      console.log(this.form)
      this.dialogFormVisible = true
      this.rules.password[0].required = false
    },
    /**
     * 跳转到设置权限页面
     */
    handleRules(index, row) {
      this.$router.push({ path: '/system/assignOperatorRules', query: { operator_id: row.id }})
    },
    /**
     * 弹出设置用户角色界面
     */
    handleGroup(index, row) {
      const data = JSON.parse(JSON.stringify(row))
      console.log(data);
      var authGroups = data.auth_groups
      this.formOperatorGroup.operator_id = data.id
      this.listGroupLoading = true
      this.dialogAuthGroupVisible = true
      getAuthGroupLists().then(response => {
        this.authGroupList = response.data
        this.listGroupLoading = false
        setTimeout(() => {
          // authGroups.forEach(item => {
            this.authGroupList.forEach(it => {
              if (data.group_id === it.id) {
                this.$refs.multipleGroupTable.toggleRowSelection(it)
              }
            })
          // })
        }, 200)
      })
    },
    /**
     * 删除用户数据
     */
    handleDeleteRow(index, row) {
      this.deleteDatas(row.id)
    },
    /**
     * 批量删除用户数据
     */
    handleDelete() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要删除的数据', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => {
          ids.push(row.id)
        })
        ids = ids.join(',')
        this.deleteDatas(ids)
      }
    },

    /**
     * 根据ids 删除用户，ids: 1,2,3,4
     */
    deleteDatas(ids) {
      this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
        cancelButtonText: '取消',
        confirmButtonText: '确定',

        type: 'warning'
      }).then(() => {
        deleteOperator({ id: ids }).then(response => {
          this.fetchData()
          this.$alert('删除成功', '成功提示', { type: 'success' })
        }).catch(error => {
          console.log(error)
        })
      })
    },
    /**
     * 提交添加或者修改管理员信息
     */
    handleSubmit() {
      this.$refs.operatorForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true
          if (formdata.id === 0) {
            addOperator(formdata).then(response => {
              var _this = this
              this.$alert('添加管理员成功', '提示信息', { type: 'success',
                callback: action => {
                  _this.fetchData()
                  _this.dialogFormVisible = false
                  _this.formSumbitLoading = false
                }
              })
            }).catch(error => {
              console.log(error)
              this.dialogFormVisible = false
              this.formSumbitLoading = false
            })
          } else {
            // 如果id不是0则为修改用户，修改用户密码为空则为不修改
            updateOperator(formdata).then(response => {
              var _this = this
              this.$alert('修改管理员信息成功', '提示信息', { type: 'success',
                callback: action => {
                  _this.fetchData()
                  _this.dialogFormVisible = false
                  _this.formSumbitLoading = false
                }
              })
            }).catch(error => {
              console.log(error)
            })
          }
        }
      })
    },
    /**
     * 选择角色，给用户赋值
     */
    handleSelectionGroupChange(val) {
      var auth_group_id = []
      val.forEach(row => {
        auth_group_id.push(row.id)
      })
      this.formOperatorGroup.auth_group_id = auth_group_id.join(',')
    },

    /**
     * 提交用户设置的角色信息
     */
    handleSubmitGroup() {
      var formdata = JSON.parse(JSON.stringify(this.formOperatorGroup))
      console.log(formdata)
      if(formdata.auth_group_id.indexOf(',') >= 0){
        this.$alert('只能选择一个角色', '提示信息', { type: 'warn',
          callback: action => {

          }
        })
        return
      }

      this.formSumbitAuthGroupLoading = true
      assignAuthGroup(formdata).then(response => {
        this.$message({ type: 'success', message: '添加成功!' })
        this.formSumbitAuthGroupLoading = false
        this.fetchData()
      })
    },
    /**
      * 列表修改用户是否启用状态
      * @param index
      * @param row
      * @returns {Promise<any>}
      */
    handleChangeStatus(index, row) {
      updateStatus(row.id, row.status).then(response => {})
    }
  }
}
</script>
