<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col :span="4" style="border:1px solid #ebeef5;height:100%;">
        <div style="padding:5px 10px;border-bottom:1px solid #ebeef5">
          <el-form size="small">
            <el-form-item label="批次：" style="margin-bottom: 0px;">
              <el-select v-model="piciValue" placeholder="请选择类型" size="mini" @change="selectPiciData" style="width:60%">
                <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
              </el-select>
            </el-form-item>
          </el-form>
        </div>
        <div style="padding:10px;border-bottom:1px solid #ebeef5">可选单位选择</div>
        <el-scrollbar style="height: calc(100% - 90px );">
          <el-tree v-loading="selectUnitLoading" ref="selectUnitClassTree" :props="unitProps" :default-expand-all="true"
                   :indent="15" :data="selectUnitTreeLists" :check-strictly="true" :highlight-current="true"
                   :expand-on-click-node="false"
                   element-loading-text="正在加载中..." node-key="unit_code" @current-change="selectUnitCurrentChange">
            <div slot-scope="{ node, data }" class="custom-tree-node">
              <span>{{ data.unit_name }}</span>
            </div>
          </el-tree>
        </el-scrollbar>
      </el-col>
      <el-col :span="20" style="height:100%;padding-left:10px;">
        <el-row class="actionBtn">
          <el-button size="small" type="primary" @click="handleSetUnitClass"><i class="el-icon-plus"
                                                                                aria-hidden="true"/> 选择可报名班级
          </el-button>
          <!-- <el-button size="small" type="danger" @click="handleDelete"><i class="el-icon-delete" aria-hidden="true" /> 批量删除</el-button> -->
          <el-button size="small" type="success" @click="fetchSelectUnitClassData"><i class="el-icon-refresh"
                                                                                      aria-hidden="true"/> 刷 新
          </el-button>
        </el-row>
        <el-row style="height: calc(100% - 55px);">

          <el-table :style="{'width':UnitWidth}" v-loading="selectUnitClassTableLoading" :data="selectUnitClassData"
                    element-loading-text="正在加载中..." border height="100%" @selection-change="handleSelectionChange">
            <el-table-column fixed="left" align="center" type="selection" width="50"/>
            <el-table-column fixed="left" label="班级名称" width="400">
              <template slot-scope="scope">
                <!-- {{ scope.row.school_structure.unit_path.split('/')[scope.row.school_structure.unit_path.split('/').length-3] }}/
                  {{ scope.row.school_structure.unit_path.split('/')[scope.row.school_structure.unit_path.split('/').length-2] }}/
                  {{ scope.row.school_structure.unit_path.split('/')[scope.row.school_structure.unit_path.split('/').length-1] }} -->
                {{ scope.row.major }} — {{ scope.row.grade }} — {{ scope.row.unit_name }}
              </template>
            </el-table-column>
            <el-table-column label="班级代码" width="180">
              <template slot-scope="scope">
                {{ scope.row.unit_self_code }}
              </template>
            </el-table-column>
            <el-table-column align="center" label="是否劳动预备制">
              <template slot-scope="scope">
                <i v-if="scope.row.is_ldybz===false" class="el-icon-close" style="color:firebrick"/>
                <i v-if="scope.row.is_ldybz===true" class="el-icon-check" style="color:forestgreen"/>
              </template>
            </el-table-column>
            <el-table-column label="班级限制总人数">
              <template slot-scope="scope">{{ scope.row.number }}</template>
            </el-table-column>
            <el-table-column label="班级已报名人数">
              <!-- <template slot-scope="scope">总:{{ scope.row.school_structure.student_count }} 【男：{{ scope.row.school_structure.student_nan_count }}，女：{{ scope.row.school_structure.student_nv_count }}】</template> -->
              <template slot-scope="scope">{{ scope.row.existTotalStudent }}</template>
            </el-table-column>
            <el-table-column label="班级限制男生人数">
              <template slot-scope="scope">{{scope.row.nan_number}}</template>
            </el-table-column>
            <el-table-column label="班级限制女生人数">
              <template slot-scope="scope">{{scope.row.nv_number}}</template>
            </el-table-column>
            <el-table-column align="center" label="是否启用">
              <template slot-scope="scope">
                <i v-if="scope.row.status===false" class="el-icon-close" style="color:firebrick"/>
                <i v-if="scope.row.status===true" class="el-icon-check" style="color:forestgreen"/>
              </template>
            </el-table-column>
            <el-table-column align="center" label="学生可报名" width="100">
              <template slot-scope="scope">
                <i v-if="scope.row.student_open===false" class="el-icon-close" style="color:firebrick"/>
                <i v-if="scope.row.student_open===true" class="el-icon-check" style="color:forestgreen"/>
              </template>
            </el-table-column>
            <el-table-column align="center" label="后台可报名" width="100">
              <template slot-scope="scope">
                <i v-if="scope.row.operator_open===false" class="el-icon-close" style="color:firebrick"/>
                <i v-if="scope.row.operator_open===true" class="el-icon-check" style="color:forestgreen"/>
              </template>
            </el-table-column>
            <el-table-column align="center" label="是否可交费" width="100">
              <template slot-scope="scope">
                <i v-if="scope.row.is_can_charge===false" class="el-icon-close" style="color:firebrick"/>
                <i v-if="scope.row.is_can_charge===true" class="el-icon-check" style="color:forestgreen"/>
              </template>
            </el-table-column>
            <el-table-column label="班主任姓名" width="100">
              <template slot-scope="scope">{{ scope.row.class_teacher }}</template>
            </el-table-column>
            <el-table-column label="班主任电话" width="100">
              <template slot-scope="scope">{{ scope.row.teacher_phone }}</template>
            </el-table-column>
            <el-table-column fixed="right" label="操作" width="150" align="center">
              <template slot-scope="scope">
                <!-- <el-button type="text" size="mini" @click="handleEdit(scope.$index,scope.row)">编辑</el-button>
                  <el-button size="mini" type="text" @click="handleDeleteRow(scope.$index,scope.row)">删除</el-button> -->
                <el-button size="mini" type="success" plain @click="handleEdit(scope.$index,scope.row)"> 编辑</el-button>
                <el-button size="mini" type="danger" plain @click="handleDeleteRow(scope.$index,scope.row)">删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>

        </el-row>
      </el-col>
    </el-row>

    <el-dialog v-dialogDrag :visible.sync="dialogUnitClassVisible" width="800px">
      <div slot="title"><i class="fa fa-pencil-square-o"/> 请选择可报名班级</div>
      <el-table v-loading="listUnitClassLoading" ref="UnitClassTable" :data="UnitClassLists"
                element-loading-text="正在加载中..." border fit height="300"
                @selection-change="handleUnitClassSelectionChange">
        <el-table-column align="center" type="selection" width="50"/>
        <el-table-column label="班级代码" width="200">
          <template slot-scope="scope">{{ scope.row.unit_code }}</template>
        </el-table-column>
        <el-table-column label="班级名称">
          <template slot-scope="scope">{{ scope.row.unit_name }}</template>
        </el-table-column>
        <!-- <el-table-column label="班级名称"><template slot-scope="scope"></template></el-table-column> -->
      </el-table>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogUnitClassVisible = false">关 闭</el-button>
        <el-button :loading="formSumbitSetUnitClassLoading" size="small" type="primary" @click="saveUnitClassLists">保
          存
        </el-button>
      </div>
    </el-dialog>

    <el-dialog v-dialogDrag :visible.sync="dialogUnitClassFormVisible" width="700px">
      <div slot="title"><i class="fa fa-pencil-square-o"/> 设置可报名班级信息</div>
      <el-form ref="SelectUnitClassForm" :model="selectUnitClassFormData" size="mini">
        <el-form-item style="margin-bottom: 0px;">
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否启用：">
              <el-switch v-model="selectUnitClassFormData.status" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否可以交费：">
              <el-switch v-model="selectUnitClassFormData.is_can_charge" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="是否劳动预备制：">
              <el-switch v-model="selectUnitClassFormData.is_ldybz" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item style="margin-bottom: 0px;">
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="学生可报名：">
              <el-switch v-model="selectUnitClassFormData.student_open" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item :label-width="formLabelWidth" label="后台可报名：">
              <el-switch v-model="selectUnitClassFormData.operator_open" :active-value="statusSwitch.activeValue"
                         :inactive-value="statusSwitch.inactiveValue"/>
            </el-form-item>
          </el-col>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="报名批次：">
          <el-select v-model="selectUnitClassFormData.pici_id" :disabled="true" placeholder="请报名批次" style="width:100%">
            <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
          </el-select>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="班级代码：">
          <el-input v-model="selectUnitClassFormData.class_code" :disabled="true" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="班级名称：">
          <el-input v-model="selectUnitClassFormData.unit_path" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="班主任姓名：">
          <el-input v-model="selectUnitClassFormData.class_teacher" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="班主任电话：">
          <el-input v-model="selectUnitClassFormData.teacher_phone" auto-complete="off"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="班级限制总人数：">
          <el-input-number :min="0" :max="2000" v-model="selectUnitClassFormData.number" style="min-width:300px;"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="班级限制男生人数：">
          <el-input-number :min="-1" :max="2000" v-model="selectUnitClassFormData.nan_number" style="min-width:300px;"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="班级限制女生人数：">
          <el-input-number :min="-1" :max="2000" v-model="selectUnitClassFormData.nv_number" style="min-width:300px;"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="排序：">
          <el-input-number :min="0" :max="2000" v-model="selectUnitClassFormData.sort" style="min-width:300px;"/>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="备注：">
          <el-input :rows="3" v-model="selectUnitClassFormData.remark" type="textarea"/>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogUnitClassFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading" size="small" type="primary" @click="handleSelectUnitClassSubmit">确 定
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import {
    getSelectUnitLists
  } from '@/api/selectUnit'

  import { setCurrentUrl, getCurrentUrl } from '@/utils/auth.js'

  import {
    getSelectUnitClassLists,
    addSelectUnitClass,
    updateSelectUnitClass,
    deleteSelectUnitClass
  } from '@/api/selectUnitClass'
  import {
    getPiciLists
  } from '@/api/pici'
  import {
    getClassLists
  } from '@/api/unit'
  import {
    getLimitClassNumber
  } from '@/api/config'

  const defaultForm = {
    id: 0,
    pici_id: '',
    select_unit_id: '',
    class_code: '',
    unit_path: '',
    status: 1,
    sort: 1,
    number: 50,
    nan_number: 25,
    nv_number: 25,
    class_teacher: '',
    teacher_phone: '',
    student_open: 1,
    operator_open: 1,
    is_can_charge: 1,
    is_ldybz: 1,
    remark: ''
  }
  export default {
    data() {
      return {
        UnitWidth: '99.9999%',
        piciValue: 1,
        piciLists: [],
        selectUnitLoading: false,
        selectUnitTreeLists: [],
        unitProps: {
          children: 'children',
          label: 'unit_name'
        },
        multipleSelection: [],
        selectUnitClassTableLoading: false,
        selectUnitClassData: [],
        params: {
          pici_id: 1,
          select_unit_ids: ''
        },
        dialogUnitClassVisible: false,
        listUnitClassLoading: false,
        UnitClassLists: [],
        formSumbitSetUnitClassLoading: false,
        formSetUnitClassData: {
          select_unit_id: 0,
          selectClassUnitCodes: []
        },

        statusSwitch: {
          activeValue: 1,
          inactiveValue: 0
        },

        formLabelWidth: '140px',
        dialogUnitClassFormVisible: false,
        formSumbitLoading: false,
        limitClassNumber: 0,
        selectUnitClassFormData: Object.assign({}, defaultForm)
      }
    },
    activated() {
      setCurrentUrl(this.$route.path)
      this.fetchPiciData()
      var _this = this
      _this.fetchSelectUnitData()
      // _this.fetchSelectUnitClassData()
    },
    mounted() {
      setCurrentUrl(this.$route.path)
      this.fetchPiciData()
      var _this = this
      _this.piciValue = _this.$store.getters.currentPici
      _this.fetchSelectUnitData()
      // _this.fetchSelectUnitClassData()
    },
    methods: {
      fetchLimitClassNumber() {
        return new Promise((resolve, reject) => {
          getLimitClassNumber().then(response => {
            this.limitClassNumber = response.data
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
      },
      fetchPiciData() {
        getPiciLists().then(response => {
          this.piciLists = response.data.data
        }).catch(error => {
          console.log(error)
        })
      },
      fetchSelectUnitData() {
        this.selectUnitLoading = true
        getSelectUnitLists({
          farther_code: '001',
          pici_id: this.piciValue,
          layer: 'dept'
        }).then(response => {
          this.selectUnitTreeLists = response.data.schoolStructure
          this.selectUnitLoading = false
        }).catch(error => {
          this.selectUnitLoading = false
          console.log(error)
        })
      },
      fetchSelectUnitClassData() {
        this.UnitWidth = '99.9999%'
        this.selectUnitClassTableLoading = true
        this.params.pici_id = this.piciValue
        getSelectUnitClassLists(this.params).then(response => {
          this.selectUnitClassData = response.data
          this.UnitWidth = '100%'
          this.selectUnitClassTableLoading = false
        }).catch(error => {
          console.log(error)
        })

      },
      // 选择批次触发的初始化数据
      selectPiciData(data) {
        this.piciValue = data
        this.params.select_unit_ids = ''
        this.fetchSelectUnitData()
        this.fetchSelectUnitClassData()
      },
      selectUnitCurrentChange(data, node) {
        this.params.pici_id = this.piciValue
        if (data.children === undefined) {
          this.params.select_unit_ids = data.id
        } else {
          if (data.children.length > 0) {
            var select_unit_ids_arr = []
            data.children.forEach(item => {
              select_unit_ids_arr.push(item.id)
            })
            this.params.select_unit_ids = select_unit_ids_arr.join(',')
          } else {
            this.params.select_unit_ids = data.id
          }
        }
        this.fetchSelectUnitClassData()
      },

      handleSelectionChange(val) {
        this.multipleSelection = val
      },

      handleUnitClassSelectionChange(val) {
        var class_codes = []
        val.forEach(row => {
          class_codes.push(row.unit_code)
        })
        this.formSetUnitClassData.selectClassUnitCodes = class_codes
      },
      handleSetUnitClass() {
        var nodeData = this.$refs.selectUnitClassTree.getCurrentNode()
        if (nodeData === null || nodeData.children !== undefined) {
          this.$alert('请先选择可报名专业', '提示', {
            type: 'warning'
          })
        } else {
          this.formSetUnitClassData.select_unit_id = nodeData.id
          this.listUnitClassLoading = true
          getClassLists({
            unit_code: nodeData.unit_code
          }).then(response => {
            this.UnitClassLists = response.data

            this.$nextTick(() => {
              this.selectUnitClassData.forEach(item => {
                this.UnitClassLists.forEach(item2 => {
                  if (item.class_code === item2.unit_code) {
                    this.$refs.UnitClassTable.toggleRowSelection(item2)
                  }
                })
              })
            })
            this.listUnitClassLoading = false
          }).catch(error => {
            this.listUnitClassLoading = false
            console.log(error)
          })
          this.dialogUnitClassVisible = true
        }
      },
      saveUnitClassLists() {
        var data = JSON.parse(JSON.stringify(this.formSetUnitClassData))

        var params = {
          pici_id: this.piciValue,
          select_unit_id: data.select_unit_id,
          class_codes: data.selectClassUnitCodes.join(',')
        }

        return new Promise((resolve, reject) => {
          addSelectUnitClass(params).then(response => {
            this.fetchSelectUnitClassData()
            this.dialogUnitClassVisible = false
            resolve(response)
          }).catch(error => {
            reject(error)
          })
        })
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
        data['class_code'] = row.class_code
        data['unit_path'] = row.unit_name
        data['status'] = (row.status == true ? 1 : 0)
        data['sort'] = row.sort
        data['number'] = row.number
        data['nan_number'] = row.nan_number
        data['nv_number'] = row.nv_number
        data['class_teacher'] = row.class_teacher
        data['teacher_phone'] = row.teacher_phone
        data['student_open'] = (row.student_open == true ? 1 : 0)
        data['operator_open'] = (row.operator_open == true ? 1 : 0)
        data['is_can_charge'] = (row.is_can_charge == true ? 1 : 0)
        data['is_ldybz'] = (row.is_ldybz == true ? 1 : 0)
        data['sort'] = row.sort
        data['remark'] = row.remark
        this.selectUnitClassFormData = JSON.parse(JSON.stringify(data))
        this.dialogUnitClassFormVisible = true
      },

      /**
       * 删除数据
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
          this.$alert('请先选择需要删除的数据', '提示', {
            type: 'warning'
          })
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
       * 根据ids 删除，ids: 1,2,3,4
       */
      deleteDatas(ids) {
        var _this = this
        this.$confirm('此操作将进行删除数据, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteSelectUnitClass({
            ids: ids
          }).then(response => {
            _this.$alert('删除成功', '成功提示', {
              type: 'success',
              callback: action => {
                _this.fetchSelectUnitClassData()
              }
            })
          }).catch(error => {
            console.log(error)
          })
        })
      },

      handleSelectUnitClassSubmit() {
        var _this = this
        this.$refs.SelectUnitClassForm.validate((valid) => {
          if (valid) {
            const formdata = JSON.parse(JSON.stringify(this.selectUnitClassFormData))
            this.formSumbitLoading = true
            if (formdata.id > 0) {
              updateSelectUnitClass(formdata).then(response => {
                _this.$alert('修改成功', '成功提示', {
                  type: 'success',
                  callback: action => {
                    _this.dialogUnitClassFormVisible = false
                    _this.formSumbitLoading = false
                    _this.fetchSelectUnitClassData()
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
  .custom-tree-node {
    flex: 1;
    display: flex;
    align-items: center;
    justify-content: space-between;
    font-size: 14px;
    padding-right: 8px;
  }

  .el-form-item--mini.el-form-item,
  .el-form-item--small.el-form-item {
    margin-bottom: 5px !important;
  }

  .el-scrollbar__wrap {
    overflow-x: hidden;
  }

  .el-tree > .el-tree-node {
    min-width: 100%;
    display: inline-block;
  }

  body .el-table th.gutter {
    display: table-cell !important;
  }
</style>
