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
          <el-col :span="24" style="border-bottom: 1px solid #ddd;padding-bottom:10px;">
            <el-select v-model="selectTemplate" size="mini" placeholder="请选择通知书模板">
              <el-option v-for="item in templateLists.data" :key="item.id" :label="item.template_name" :value="item.id"/>
            </el-select>
            <el-button size="mini" type="primary" @click.native="handlePreviewTzs"><i class="el-icon-printer" aria-hidden="true"/> 打印预览</el-button>
            <el-button size="mini" type="primary" @click.native="handlePrintTzs"><i class="el-icon-printer" aria-hidden="true"/> 打印通知书</el-button>
              <el-button size="mini" type="primary" @click.native="search"><i class="el-icon-search" aria-hidden="true"/> 搜 索</el-button>
             <el-button size="mini" type="primary" :loading="exportLoading" @click.native="exportExcel"><i class="el-icon-search" aria-hidden="true"/> 导 出</el-button>
            <el-button size="mini" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
            <el-button size="mini" style="float:right" type="warning" @click="handleTableSet"><i class="el-icon-setting" aria-hidden="true"/> 表格设置</el-button>
          </el-col>
          <el-col :span="24">
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
          height="calc(100% - 138px)"
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
              <el-table-column :fixed="item.field_name =='name'?'left':false" :label="item.describe" :key="item.field_name" :width="item.width" :align="item.is_center==1?'center':'left'">
              <template slot-scope="scope">{{ scope.row[item.field_name] }}</template>
            </el-table-column>
          </template>
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
  </div>
</template>

<script>
import { getStructureLists, getListsLimitByOperatorClass} from '@/api/unit'
import { getExpandedUnitCode } from '@/api/config'
import { getStudentLists, printTzs, setIsPrintTzs , exportTzsStudentLists, getSearchField} from '@/api/student'
import { getPrintTemplateLists } from '@/api/printTemplate'
import { LodopFuncs } from '@/utils/LodopFuncs'
import { getPiciLists } from '@/api/pici'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
import store from '@/store'
export default {
  data() {
    return {
      exportLoading:false,
      studentSearchFields: [],
      stuctureData: [],
      expandedUnitCode: ['001001'],
      TreeData: [],
      selectUnit: {},
      defaultProps: {
        children: 'children',
        label: 'unit_name'
      },
      templateLists: [],
      selectTemplate: null,
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
        form_name: '打印通知书列表',
        conditions: {},
        keyword: ''
      },
      copyParams:{
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_dor: true,
        form_name: '打印通知书列表',
        conditions: {},
        keyword: ''
      },
      multipleSelection: [],
      listLoading: true,
      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0
      }

    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchSearchField()
    this.fetchPiciData()
    // this.fetchExpandedUnitCode()
    this.fetchTemplateData()
    this.fetchStructure()
    this.fetchData()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    let data = JSON.parse(localStorage.getItem("info"));
    this.params.pici_id = data.pici_id


    LodopFuncs.beforeInit()
    this.fetchSearchField()
    this.fetchPiciData()
    // this.fetchExpandedUnitCode()
    this.fetchTemplateData()
    this.fetchStructure()
    this.fetchData()


  },
  methods: {
    fetchSearchField() {
      getSearchField({ form_name: '打印通知书列表' }).then(response => {
        this.studentSearchFields = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    paramsConvert(){
      // console.log(this.studentSearchFields);
      // console.log(this.params.conditions)
      this.copyParams = JSON.parse(JSON.stringify(this.params))
      for(var key in this.copyParams.conditions){
        this.copyParams.conditions[key] += ''
      }
    },
    search(){
      this.params.page = 1
      this.fetchData()
    },
    fetchStructure(){
      let data = JSON.parse(localStorage.getItem("info"))
      getListsLimitByOperatorClass({farther_code: '001', pici_id: this.params.pici_id, operator_id: data.id, addForm: ''}).then(response => {
        this.stuctureData = response.data.schoolStructure
      }).catch(error => { console.log(error) })
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
          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },
    fetchTemplateData() {
      getPrintTemplateLists(this.params).then(response => {
        this.templateLists = response.data
        this.selectTemplate = this.templateLists.data[0].id
      })
    },
    fetchData() {
      this.listLoading = true
      this.paramsConvert()
      getStudentLists(this.copyParams).then(response => {
        this.list = response.data
        this.columns = response.data.columns
        this.pagination.total = response.data.total
        this.listLoading = false
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
    loadNode(node, resolve) {
      if (node.level === 0) {
        getStructureLists().then(response => {
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
        getStructureLists({ farther_code: node.data.unit_code }).then(response => {
          const data = response.data
          resolve(data)
        }).catch(error => {
          console.log(error)
        })
      }
    },
    handleAddStudent() {
      this.$router.push({ path: '/student/addStudent' })
    },
    handleTableSet() {
      this.$router.replace({ path: '/setting/setStudentGrid', query: { formName: '打印通知书列表' }})
    },
    /**
     * 批量删除用户数据
     */
    handlePreviewTzs() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要打印通知单的学生', '提示', { type: 'warning' })
      } else if (rows.length > 1) {
        this.$alert('请只选中一个学生进行打印通知单', '提示', { type: 'warning' })
      } else {
        var student_id = rows[0].id
        printTzs({ template_id: this.selectTemplate, student_id: student_id }).then(response => {
          const printOrder = response.data
          var LODOP = LodopFuncs.getLodop()
          eval(printOrder)
          LODOP.PREVIEW()
        }).catch(error => {
          console.log(error)
        })
      }
    },
    /**
       * 批量删除用户数据
       */
    handlePrintTzs() {
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要打印通知单的学生', '提示', { type: 'warning' })
      } else if (rows.length > 1) {
        this.$alert('请只选中一个学生进行打印通知单', '提示', { type: 'warning' })
      } else {
        var student_id = rows[0].id
        printTzs({ template_id: this.selectTemplate, student_id: student_id }).then(response => {
          const printOrder = response.data
          var LODOP = LodopFuncs.getLodop()
          eval(printOrder)
          LODOP.PRINT()
          LODOP.On_Return = function(TaskID, Value) {
            if (Value) {
              setIsPrintTzs({ student_id: student_id }).then(response => {
                // console.log(student_id + '打印成功')
              }).catch(error => {
                console.log(error)
              })
            }
          }
        }).catch(error => {
          console.log(error)
        })
      }
    },
    // 设置当前结构数据
    currentChange(data, node) {
      this.params.unit_code = data.unit_code
      this.fetchData()
    },
    exportExcel(){
      this.exportLoading=true
      exportTzsStudentLists(this.params).then(response => {
        let url = process.env.BASE_API + response.data
        this.exportLoading=false
        this.$alert('<a style="color:#409EFF;font-weight:900;text-align: center" target="_blank" href="' + url + '">点击下载导出的表格</a>', '导出下载', {
          dangerouslyUseHTMLString: true,
          type: 'warning'
        })
      }).catch(error => {
        console.log(error)
        this.exportLoading=false
      })
    }
  }
}
</script>

<style scoped>

</style>
