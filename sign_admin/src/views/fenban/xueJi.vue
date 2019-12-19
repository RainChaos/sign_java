<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col style="border:1px solid #dddddd;height:100%;width:220px;">
        <div style="padding:10px;border-bottom:1px solid #dddddd">
          学生单位结构
        </div>
        <el-scrollbar style="height: calc(100% - 40px);">
          <el-tree
            ref="treeBox"
            :props="defaultProps"
            :highlight-current="true"
            :default-expand-all="false"
            :expand-on-click-node="false"
            :default-expanded-keys="expandedUnitCode"
            :check-strictly="true"
            :indent="10"
            :data="stuctureData"
            node-key="unit_code"
            @current-change="currentChange"/>
        </el-scrollbar>
      </el-col>
      <el-col style="height:100%;width:calc(100% - 220px); " >
        <el-row class="actionBtn">
          <el-col>

            <el-button size="mini" type="success" @click.native="handleChangeUnit"><i class="el-icon-sort" aria-hidden="true"/>批量修改学籍状态</el-button>
            <el-button size="mini" type="primary" @click.native="search"><i class="el-icon-search" aria-hidden="true"/> 搜 索</el-button>
<!--            <el-button size="mini" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>-->
            <el-button size="mini" type="warning" @click="handleTableSet" style="float: right"><i class="el-icon-setting" aria-hidden="true"/> 表格设置</el-button>
          </el-col>
          <el-col :span="24" >
            <el-form :inline="true" :model="params" size="mini">

              <el-col :span="3">
                <el-form-item    style="margin-bottom: 0px;margin-top:10px;">
                  <label slot="label">批次:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                  <el-row>
                    <el-select v-model="params.pici_id" placeholder="请选择季节批次">
                      <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                  </el-row>

                </el-form-item>
              </el-col>

              <el-col :span="3">
                <el-form-item  style="margin-bottom: 0px;margin-top:10px;" >
                  <label slot="label">关键词:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                  <el-input v-model="params.conditions.keyword"  placeholder="姓名/学号/身份证号码"/>
                </el-form-item>
              </el-col>


              <template v-for="(item) in studentSearchFields">
                <el-col :span="3">
                  <el-form-item
                          v-if="item.data_type=='省市区'"
                          :prop="item.field_name"
                          style="margin-bottom: 0px;margin-top:10px;">
                    <label slot="label">{{ item.describe }}:<span v-if="item.describe.length < 4">&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
                    <el-cascader :show-all-levels="false" clearable v-if="item.event_name != null && item.event_name.length > 0" v-model="params.conditions[item.field_name]" :options="regionOptions" :props="regionProps" style="width: 100%" :placeholder="'请选择'+item.describe"
                                 @change="item.event_name" />
                    <el-cascader :show-all-levels="false" clearable v-else-if="item.event_name == null || item.event_name.length == 0" v-model="params.conditions[item.field_name]" :options="regionOptions" :props="regionProps" style="width: 100%"
                    />

                  </el-form-item>
                </el-col>

                <el-col :span="3">
                  <el-form-item
                          v-if="item.data_type=='字符串' || item.data_type=='毕业中学'"
                          :prop="item.field_name"
                          style="margin-bottom: 0px;margin-top:10px;">
                    <label slot="label">{{ item.describe }}:<span v-if="item.describe.length < 4">&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
                    <el-input v-model="params.conditions[item.field_name]" style="width: 100%"/>
                  </el-form-item>
                </el-col>

                <el-col :span="3">
                  <el-form-item
                          v-if="item.data_type=='标准数据'"
                          :prop="item.field_name"
                          style="margin-bottom: 0px;margin-top:10px;">
                    <label slot="label">{{ item.describe }}:<span v-if="item.describe.length < 4">&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
                    <el-select v-model="params.conditions[item.field_name]"  placeholder="" style="width: 100%">
                      <el-option key="-1" value="-1" label="全部"/>
                      <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value"/>
                    </el-select>
                  </el-form-item>
                </el-col>

                <el-col :span="3">
                  <el-form-item
                          v-if="item.data_type=='日期'"
                          :prop="item.field_name"
                          style="margin-bottom: 0px;margin-top:10px;">
                    <label slot="label">{{ item.describe }}:<span v-if="item.describe.length < 4">&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
                    <el-date-picker style="width: 100%"
                                    v-model="params.conditions[item.field_name]"
                                    :picker-options="pickerOptions"
                                    format="yyyy-MM-dd"
                                    value-format="yyyy-MM-dd"
                                    type="datetimerange"
                                    align="right"/>
                  </el-form-item>
                </el-col>

              </template>


            </el-form>
          </el-col>
        </el-row>
        <el-table
          v-loading="listLoading"
          ref="multipleTable"
          :data="list.data"
          :height="tableHeight"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row
          @selection-change="handleSelectionChange">
          <el-table-column fixed="left" align="center" type="selection" width="50"/>
          <el-table-column fixed="left" align="center" label="序号" width="50">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <template v-for="(item) in columns">
            <el-table-column v-if="item.is_show" :fixed="item.field_name =='name'?'left':false" :label="item.describe" :key="item.field_name" :width="item.width" :align="item.is_center==1?'center':'left'">
              <template slot-scope="scope">{{ scope.row[item.field_name] }}</template>
            </el-table-column>
          </template>
          <el-table-column fixed="right" align="center" label="操作" width="120">
            <template slot-scope="scope">
              <el-button size="mini" type="success" plain @click="handleChangeUnitRow(scope.$index, scope.row)"> 修改学籍状态</el-button>
            </template>
          </el-table-column>
        </el-table>
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
      </el-col>
    </el-row>

    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="900px">
      <div slot="title"><i class="el-icon-sort"/> 学籍变动 </div>
      <el-form ref="changeStudentUnitForm" :rules="rules" :model="form" label-position="top" size="small" >
        <el-form-item label-width="100px" label="学籍状态" prop="status">
          <el-select v-model="form.status" placeholder="学籍状态" >
            <el-option v-for="item in studentStatusList" :key="item.code" :label="item.name" :name="item.name" :value-key="item.code" :value="item.code"/>
          </el-select>
        </el-form-item>
        <el-form-item :label-width="formLabelWidth" label="原因：">
          <el-input :rows="2" v-model="form.description" type="textarea" auto-complete="off"/>
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
import { getListsLimitByOperatorClass } from '@/api/unit'
import { getExpandedUnitCode } from '@/api/config'
import { getStudentLists, changeStudentStatus ,getSearchField} from '@/api/student'
import { getAllLists } from '@/api/unit'
import { getPiciLists } from '@/api/pici'
import { getUseDictionaryLists } from '@/api/dictionary'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  data() {
    return {
      tableHeight: '0px',
      studentSearchFields: [],
      stuctureData: [],
      expandedUnitCode: ['001'],
      TreeData: [],
      selectUnit: {},
      defaultProps: {
        children: 'children',
        label: 'unit_name'
      },
      list: {},
      columns: [],
      tableRowsIndex: ['id'],
      pagination: {
        current_page: 0,
        pre_page: 15,
        total: 0
      },
      piciLists: [],
      YesOrNotLists: [
        { value: -1, text: '全部' },
        { value: 1, text: '是' },
        { value: 0, text: '否' }
      ],
      params: {
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_dor: true,
        form_name: '学籍异动列表',
        conditions: {},
        keyword: ''
      },
      copyParams:{
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_dor: true,
        form_name: '学籍异动列表',
        conditions: {},
        keyword: ''
      },
      multipleSelection: [],
      listLoading: true,
      dialogFormVisible: false,
      formSumbitLoading: false,
      studentStatusList: [],
      formLabelWidth: '150px',
      form: {
        student_ids: '',
        status: '',
        description: ''
      },
      pickerOptions: {
        disabledDate(time) {
          return time.getTime() > Date.now()
        },
        shortcuts: [{
          text: '今天',
          onClick(picker) {
            picker.$emit('pick', new Date())
          }
        }, {
          text: '昨天',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24)
            picker.$emit('pick', date)
          }
        }, {
          text: '一周前',
          onClick(picker) {
            const date = new Date()
            date.setTime(date.getTime() - 3600 * 1000 * 24 * 7)
            picker.$emit('pick', date)
          }
        }]
      },
      unitOptions: [],
      unitProps: {
        value: 'unit_code',
        label: 'unit_name',
        children: 'children'
      },
      rules: {
        status: [
          { required: true, message: '请选择需要调整的学籍状态', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    this.studentStatusList= [],
    setCurrentUrl(this.$route.path)
    let data = JSON.parse(localStorage.getItem("info"));
    getUseDictionaryLists({ type: 'ZXZTDM' }).then(response => {
      this.studentStatusList = response.data.data
    }).catch(error => {
      console.log(error)
    })
    this.fetchPiciData()
    this.fetchExpandedUnitCode()
    this.fetchStructure()
    this.fetchSearchField()
    this.fetchData()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    let data = JSON.parse(localStorage.getItem("info"));
    this.params.pici_id = data.pici_id
    getUseDictionaryLists({ type: 'ZXZTDM' }).then(response => {
      this.studentStatusList = response.data.data
    }).catch(error => {
      console.log(error)
    })
    this.fetchPiciData()
    this.fetchExpandedUnitCode()
    this.fetchStructure()
    this.fetchSearchField()
    this.fetchData()
    //this.fetchUnitLists()
  },
  methods: {
    search(){
      this.params.page = 1
      this.pagination.current_page = 0
      this.fetchData()
    },
    fetchSearchField() {
      getSearchField({ form_name: '学籍异动列表' }).then(response => {
        this.studentSearchFields = response.data
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
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fetchExpandedUnitCode() {
      return new Promise((resolve, reject) => {
        getExpandedUnitCode().then(response => {
          this.expandedUnitCode.push(response.data)
          console.log(this.expandedUnitCode)
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    /*fetchUnitLists() {
      getAllLists().then(response => {
        var UnitLists = JSON.parse(JSON.stringify(response.data))
        this.unitOptions = UnitLists
      })
    },*/
    fetchData() {
      this.listLoading = true
      this.paramsConvert()
      getStudentLists(this.params).then(response => {
        this.list = response.data
        this.columns = response.data.columns
        this.pagination.current_page = response.data.current_page
        this.pagination.total = response.data.total
        this.tableHeight = window.innerHeight - this.$refs.multipleTable.$el.offsetTop - 192;
        this.listLoading = false
      }).catch(error => { console.log(error) })
    },
    paramsConvert(){
      // console.log(this.studentSearchFields);
      // console.log(this.params.conditions)
      this.copyParams = JSON.parse(JSON.stringify(this.params))
      for(var key in this.copyParams.conditions){
        this.copyParams.conditions[key] += ''
      }
    },
    fetchStructure(){
      let data = JSON.parse(localStorage.getItem("info"))
      getListsLimitByOperatorClass({farther_code: '001', pici_id: this.params.pici_id, operator_id: data.id, addForm: ''}).then(response => {
        this.stuctureData = response.data.schoolStructure
        this.unitOptions =response.data.schoolStructure
      }).catch(error => { console.log(error) })
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
    // 加载单位结构数据
   /* loadNode(node, resolve) {
      if (node.level === 0) {
        getListsLimitByOperatorClass().then(response => {
          const data = response.data
          resolve(data)
          this.$nextTick(() => {
            this.$refs.treeBox.setCurrentKey('001')
            this.params.unit_code = '001'
            this.params.pici_id = this.$store.getters.currentPici
            this.fetchData()
            const node2 = this.$refs.treeBox.getCurrentNode()
            const data = JSON.parse(JSON.stringify(node2))
            this.selectUnit = data
          })
        }).catch(error => { console.log(error) })
      } else {
        return new Promise((resolve2, reject) => {
          getListsLimitByOperatorClass({ farther_code: node.data.unit_code }).then(response => {
            const data = response.data
            resolve(data)
            resolve2(response)
          }).catch(error => {
            reject(error)
          })
        })
      }
    },*/
    handleTableSet() {
      this.$router.replace({ path: '/setting/setStudentGrid', query: { formName: '维护学生列表' }})
    },
    // 设置当前结构数据
    currentChange(data, node) {
      this.params.page = 1
      this.params.unit_code = data.unit_code
      this.fetchData()
    },

    /**
       * 删除用户数据
       */
    handleChangeUnitRow(index, row) {
      this.form.status = row.status
      this.ChangeUnitDatas(row.id)
    },

    handleChangeUnit() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要变动学籍的学生', '提示', { type: 'warning' })
      } else {
        this.form.status = rows[0].status
        var ids = []
        rows.forEach(row => {
          ids.push(row.id)
        })
        ids = ids.join(',')
        this.ChangeUnitDatas(ids)
      }
    },

    ChangeUnitDatas(ids) {
      this.form.student_ids = ids
      this.form.unit_code = []
      this.dialogFormVisible = true
    },

    handleSubmit() {
      this.$refs.changeStudentUnitForm.validate((valid) => {
        if (valid) {
          const formdata = JSON.parse(JSON.stringify(this.form))
          const student_ids = formdata.student_ids
          const status = formdata.status
          this.formSumbitLoading = true
          changeStudentStatus({ student_ids: student_ids, status: status, remark: formdata.description }).then(response => {
            this.dialogFormVisible = false
            this.fetchData()
            this.$message.success('修改成功')
            this.formSumbitLoading = false
          }).catch(error => {
            console.log(error)
            this.fetchData()
            this.formSumbitLoading = false
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
