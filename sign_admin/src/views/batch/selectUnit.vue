<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col :span="4" style="border:1px solid #ebeef5;height: 100%;">
        <div style="padding:5px 10px;border-bottom:1px solid #ebeef5">
          <el-form size="mini">
            <el-form-item label="批次：" style="margin-bottom: 0px;">
              <el-select v-model="piciValue" placeholder="请选择类型" size="mini" @change="selectPiciData" style="width:60%">
                <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <div style="padding:10px;border-bottom:1px solid #ebeef5">选择单位院系</div>
        <el-scrollbar style="height: calc(100% - 84px );  ">
          <el-tree v-loading="treeUnitLoading" ref="treeUnitBox" :props="unitProps" :default-expand-all="false"
                   :indent="15" :data="unitTreeLists" :check-strictly="true"
                   :default-expanded-keys="defaultExpandedKeys"
                   :default-checked-keys="defaultCheckedKeys" show-checkbox element-loading-text="正在加载中..."
                   node-key="unit_code" @check="unitCheckChange">
            <div slot-scope="{ node, data }" class="custom-tree-node">
              <div style="font-size:14px;">
                {{ data.unit_name }}
              </div>
            </div>
          </el-tree>
        </el-scrollbar>
      </el-col>
      <el-col :span="20" style="height: 100%;padding-left:10px;">
        <el-alert :closable="false" title="提示：可选择单位是直接让学生直接选择的报读单位，再设置可选单位班级后，新生报名会直接进到报读单位设置对应的班级中"
                  style="border-radius: 0px;font-weight: bolder" type="error"/>
        <tree-table :style="{'width':UnitWidth}" v-loading="listLoading" :data="selectUnitTree" :columns="columns"
                    :expand-all="true" border element-loading-text="正在加载中..." height="calc(100% - 35px )">
          <el-table-column align="center" label="排序" width="80">
            <template slot-scope="scope">
              {{ scope.row.sort }}
            </template>
          </el-table-column>
          <el-table-column width="140" label="操作">
            <template slot-scope="scope">
              <el-button v-if="scope.row.unit_code.length == 12" size="mini" type="success" plain
                         @click="handleEdit(scope.$index,scope.row)"> 编辑
              </el-button>
              <el-button v-if="scope.row.unit_code.length == 12" size="mini" type="danger" plain
                         @click="handleDeleteRow(scope.$index,scope.row)">删除
              </el-button>
            </template>
          </el-table-column>
        </tree-table>
      </el-col>
    </el-row>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="700px">
      <div slot="title"><i :class="formIconClass"/> {{ formTitle }}</div>
      <el-form ref="SelectUnitForm" :model="form" :rules="rules" size="small">
        <el-form-item style="margin-bottom: 0px;">
          <!--<el-col :span="8">-->
          <!--<el-form-item :label-width="formLabelWidth" label="是否启用：">-->
          <!--<el-switch v-model="form.status" :active-value="statusSwitch.activeValue" :inactive-value="statusSwitch.inactiveValue" />-->
          <!--</el-form-item>-->
          <!--</el-col>-->
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="学生可报名：">
              <el-switch v-model="form.student_open" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="后台可报名：">
              <el-switch v-model="form.operator_open" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="报名批次：">
          <el-select v-model="form.pici_id" :disabled="true" placeholder="请选择类型" size="small" style="width:100%">
            <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="单位代码：">
          <el-input v-model="form.unit_code" :disabled="true" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="名称：">
          <el-input v-model="form.unit_name" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="学制：">
          <el-input v-model="form.xuezhi" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="可报学历：">
          <!-- <el-input v-model="form.education" type="textarea" /> -->
          <el-select multiple collapse-tags placeholder="请选择" v-model="form.education">
            <el-option
              v-for="item in studentEducation"
              :key="item.code"
              :label="item.code"
              :value="item.code">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="备注：">
          <el-input :rows="3" v-model="form.remark" type="textarea"/>
        </el-form-item>
        <el-form-item style="margin-bottom: 0px;">
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="自动创建班级：">
              <el-switch v-model="form.is_auto_add_class" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否是劳动预备制：">
              <el-switch v-model="form.is_ldybz" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="预备制招生人数：">
          <el-input-number v-model="form.ldybz_number" type="textarea"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：">
          <el-input-number v-model="form.sort" type="textarea"/>
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
  import treeTable from '@/components/TreeTable'
  import { setCurrentUrl, getCurrentUrl } from '@/utils/auth.js'
  import {
    getSelectUnitLists,
    addSelectUnitFromStruture,
    deleteSelectUnitFromStruture,
    updateSelectUnit,
    deleteSelectUnit,
    getCheckedKeys,
    getStudentEducation
  } from '@/api/selectUnit'
  import {
    getStructureOnlyYxZyLists
  } from '@/api/unit'
  import {
    getPiciLists
  } from '@/api/pici'
  import {
    isInArray
  } from '@/utils/index'
  import selectUnitClass from './selectUnitClass'

  const defaultForm = {
    id: 0,
    pici_id: '',
    father_id: '',
    unit_code: '',
    unit_name: '',
    xuezhi: '4',
    status: 1,
    is_auto_add_class: 1,
    student_open: 1,
    operator_open: 1,
    is_ldybz: 0,
    ldybz_number: 0,
    sort: 1,
    remark: '',
    educaion: ''
  }
  export default {
    components: {
      treeTable,
      selectUnitClass
    },
    data() {
      return {
        UnitWidth: '100%',
        piciLists: [],
        piciValue: 0,
        unitProps: {
          children: 'children',
          label: 'unit_name'
        },
        treeUnitLoading: false,
        unitTreeLists: [],
        defaultExpandedKeys: [],
        defaultCheckedKeys: [],
        selectUnitTree: [],
        columns: [{
          fixed: 'left',
          text: '单位名称',
          value: 'unit_name',
          width: 400
        }],
        listLoading: false,
        statusSwitch: {
          activeValue: 1,
          inactiveValue: 0
        },
        dialogFormVisible: false,
        formLabelWidth: '150px',
        formTitle: '修改可选单位信息',
        formIconClass: 'fa fa-user-plus',
        formSumbitLoading: false,
        formSumbitAuthGroupLoading: false,
        form: Object.assign({}, defaultForm),
        rules: {
          unit_name: [{
            required: true,
            message: '请输入可选择单位名称',
            trigger: 'blur'
          }]
        },
        studentEducation: []
      }
    },
    activated() {
      setCurrentUrl(this.$route.path)
      let data = JSON.parse(localStorage.getItem('info'))
      this.fetchStudentEducation()
      this.fetchPiciData()
      this.fetchUnitData()
      var _this = this
      _this.fetchData()
    },
    mounted() {

      setCurrentUrl(this.$route.path)
      let data = JSON.parse(localStorage.getItem('info'))
      this.piciValue = data.pici_id
      this.fetchStudentEducation()
      this.fetchPiciData()
      this.fetchUnitData()
      var _this = this
      _this.fetchData()
    },
    methods: {
      fetchStudentEducation() {
        getStudentEducation().then(response => {
          this.studentEducation = response.data
          // console.log(response.data);
        }).catch(error => {
          console.log(error)
        })
      },
      fetchPiciData() {
        getPiciLists().then(response => {
          this.piciLists = response.data.data
        }).catch(error => {
          console.log(error)
        })
      },
      fetchUnitData() {
        this.treeUnitLoading = true
        // console.log(this.piciValue);
        getStructureOnlyYxZyLists({
          farther_code: '001',
          pici_id: this.piciValue,
          // allIn: this.piciValue,
          unitPiciList: this.piciValue
        }).then(response => {
          this.unitTreeLists = response.data.schoolStructure
          this.defaultCheckedKeys = response.data.checkedList
          this.defaultExpandedKeys = response.data.checkedList
          // this.selectUnitTree = response.data.schoolStructure
          this.treeUnitLoading = false
        }).catch(error => {
          this.treeUnitLoading = false
          console.log(error)
        })
      },
      fetchDefaultCheckedKeys() {
        getCheckedKeys({
          pici: this.piciValue
        }).then(response => {
          const checkedLists = response.data
          var checkedKeys = []
          checkedLists.forEach((item) => {
            checkedKeys.push(item.unit_code)
          })
          this.defaultExpandedKeys = checkedKeys
          this.defaultCheckedKeys = checkedKeys
        }).catch(error => {
          console.log(error)
        })
      },
      fetchData() {
        this.UnitWidth = '99.999%'
        this.listLoading = true
        getSelectUnitLists({
          farther_code: '001',
          pici_id: this.piciValue
        }).then(response => {
          this.selectUnitTree = response.data.schoolStructure
          this.UnitWidth = '100%'
          this.listLoading = false
        }).catch(error => {
          this.listLoading = false
          console.log(error)
        })
      },
      selectPiciData(data) {
        this.piciValue = data
        this.fetchUnitData()
        // this.fetchDefaultCheckedKeys()
        this.fetchData()
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
      unitCheckChange(data, status) {
        const params = {
          pici_id: this.piciValue,
          unit_code: data.unit_code,
          unit_name: data.unit_name,
          farther_code: data.farther_code
        }
        if (isInArray(params.unit_code, status.checkedKeys)) {
          addSelectUnitFromStruture(params).then(response => {
            this.fetchData()
          }).catch(error => {
            console.log(error)
          })
        } else {
          deleteSelectUnitFromStruture(params).then(response => {
            this.fetchData()
          }).catch(error => {
            console.log(error)
          })
        }
      },
      /**
       * 添加可选择单位信息
       */
      handleAdd() {
        this.formIconClass = 'fa fa-user-plus'
        this.formTitle = '添加可选择单位信息'
        this.form = Object.assign({}, defaultForm)
        this.dialogFormVisible = true
      },
      /**
       * 修改可选择单位信息
       */
      handleEdit(index, row) {
        this.formIconClass = 'fa fa-pencil-square-o'
        this.formTitle = '修改可选择单位信息'
        var data = {}
        data['id'] = row.id
        data['pici_id'] = row.pici_id
        data['father_id'] = row.father_id
        data['unit_code'] = row.unit_code
        data['unit_name'] = row.unit_name
        data['xuezhi'] = row.xuezhi
        data['status'] = (row.status == true ? 1 : 0)
        data['is_auto_add_class'] = (row.is_auto_add_class === true ? 1 : 0)
        data['student_open'] = (row.student_open == true ? 1 : 0)
        data['operator_open'] = (row.operator_open == true ? 1 : 0)
        data['is_ldybz'] = (row.is_ldybz == true ? 1 : 0)
        data['ldybz_number'] = row.ldybz_number
        data['sort'] = row.sort
        data['remark'] = row.remark
        var edu = row.education.split(',')
        console.log(edu)
        data['education'] = edu
        this.form = JSON.parse(JSON.stringify(data))
        this.dialogFormVisible = true
      },
      /**
       * 删除数据
       */
      handleDeleteRow(index, row) {
        this.deleteDatas(row.id)
      },
      /**
       * 根据ids 删除，ids: 1,2,3,4
       */
      deleteDatas(ids) {
        var _this = this
        this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteSelectUnit({
            ids: ids
          }).then(response => {
            _this.$alert('删除成功', '成功提示', {
              type: 'success',
              callback: action => {
                _this.fetchData()
                _this.fetchUnitData()
              }
            })
          }).catch(error => {
            console.log(error)
          })
        }).catch(error => {
          console.log(error)
        })
      },

      /**
       * 提交添加或者修改
       */
      handleSubmit() {
        var _this = this
        this.$refs.SelectUnitForm.validate((valid) => {
          if (valid) {
            const formdata = JSON.parse(JSON.stringify(this.form))
            formdata.education += ''
            // 如果id是0则为新增用户，新增用户密码不能为空
            this.formSumbitLoading = true
            if (formdata.id > 0) {
              // 如果id不是0则为修改用户，修改用户密码为空则为不修改
              updateSelectUnit(formdata).then(response => {
                _this.$alert('修改成功', '成功提示', {
                  type: 'success',
                  callback: action => {
                    _this.dialogFormVisible = false
                    _this.fetchData()
                    _this.fetchUnitData()
                    _this.formSumbitLoading = false
                  }
                })
              }).catch(error => {
                this.formSumbitLoading = false
                console.log(error)
              })
            }
          }
        })
      }
    }
  }
</script>

<style>
  .el-scrollbar__wrap {
    overflow-x: hidden;
  }

  .el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block;
  }
</style>
