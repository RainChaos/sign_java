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
      <el-col style="height:100%;width:calc(100% - 220px)" >
        <el-row class="actionBtn">
          <el-col :span="24" style="border-bottom: 1px solid #ddd;padding-bottom:10px;">
            <el-button :disabled="!setRuzhu" size="mini" type="primary" @click.native="handleInBed"><i class="fa fa-table" aria-hidden="true"/> 设置入住</el-button>
            <el-button :disabled="!setLisu" size="mini" type="primary" @click.native="handleOutBed"><i class="fa fa-table" aria-hidden="true"/> 设置离宿</el-button>
            <el-button :disabled="!setWaisu" size="mini" type="primary" @click.native="handleAddOut"><i class="fa fa-table" aria-hidden="true"/> 设置外宿</el-button>
            <el-button size="mini" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>
            <el-button size="mini" type="primary" @click.native="searchData"><i class="el-icon-search" aria-hidden="true"/> 搜 索</el-button>
            <el-button size="mini" style="float:right" type="warning" @click="handleTableSet"><i class="el-icon-setting" aria-hidden="true"/> 表格设置</el-button>
          </el-col>
          <el-col :span="24">
            <el-form :inline="true" :model="params" size="mini">

              <el-col :span="3">
              <el-form-item label="批次：" label-width="55px" style="margin-bottom: 0px;margin-top:10px;">
                <el-select v-model="params.pici_id" placeholder="请选择季节批次">
                  <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
                </el-select>
              </el-form-item>
              </el-col>

              <el-col :span="3">
              <el-form-item label="关键词:" style="margin-bottom: 0px;margin-top:10px;" >
                <el-input v-model="params.conditions.keyword"  placeholder="姓名/学号/身份证号码"/>
              </el-form-item></el-col>

              <template v-for="(item) in studentSearchFields">

                <el-col :span="3">
                <el-form-item
                  v-if="item.data_type=='字符串'"
                  :prop="item.field_name"
                  style="margin-bottom: 0px;margin-top:10px;">
                  <label slot="label">{{ item.describe }}:</label>
                  <el-input v-model="params.conditions[item.field_name]" />
                </el-form-item></el-col>


                <el-col :span="3">
                <el-form-item
                  v-if="item.data_type=='标准数据'"
                  :prop="item.field_name"
                  style="margin-bottom: 0px;margin-top:10px;">
                  <label slot="label">{{ item.describe }}:</label>
                  <el-select v-model="params.conditions[item.field_name]"  :placeholder="'请选择'+item.describe">
                    <el-option key="-1" value="-1" label="全部"/>
                    <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value"/>
                  </el-select>
                </el-form-item></el-col>

                <el-col :span="3">
                <el-form-item
                  v-if="item.field_name=='create_time_start' || item.field_name=='create_time_end'"
                  :prop="item.field_name"
                  style="margin-bottom: 0px;margin-top:10px;">
                  <label slot="label">{{ item.describe }}:</label>
                  <el-date-picker
                    v-model="params.conditions[item.field_name]"
                    :picker-options="pickerOptions"
                    format="yyyy-MM-dd"
                    value-format="yyyy-MM-dd"
                    type="date"
                    align="right"/>
                </el-form-item></el-col>

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
          @current-change="currentStudent"
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
import { getListsLimitByOperatorClass } from '@/api/unit'
import { vilidateBtn } from '@/utils/auth'
import { getExpandedUnitCode } from '@/api/config'
import { getStudentLists, getSearchField } from '@/api/student'
import { inBedAuto, outBed, addStuOut } from '@/api/dor'
import { getPiciLists } from '@/api/pici'
import Tongzhidan from './components/Tongzhidan'

import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  components: {
    Tongzhidan
  },
  data() {
    return {
      tableHeight: '0px',
      stuctureData: [],
      piciLists: [],
      expandedUnitCode: ['001'],
      studentSearchFields: [],
      studentInfo: {},
      chargeItems: [],
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
      params: {
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_dor: true,
        formName: '入住公寓列表',
        form_name: '入住公寓列表',
        conditions: {},
        keyword: ''
      },
      copyParams:{
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_dor: true,
        formName: '入住公寓列表',
        form_name: '入住公寓列表',
        conditions: {},
        keyword: ''
      },
      multipleSelection: [],
      listLoading: true,
      statusSwitch: {
        activeValue: 1,
        inactiveValue: 0
      },
      dialogPrintTzdVisible: false
    }
  },
  computed: {
    setRuzhu: function() {
      return vilidateBtn('设置入住')
    },
    setLisu: function() {
      return vilidateBtn('设置离宿')
    },
    setWaisu: function() {
      return vilidateBtn('设置外宿')
    }
  },
  activated(){
    let data = JSON.parse(localStorage.getItem("info"));
    setCurrentUrl(this.$route.path)
    this.fetchPiciData()
    this.fetchStructure()
    this.fetchData()
    this.fetchSearchField()
  },
  mounted() {
    let data = JSON.parse(localStorage.getItem("info"));
    this.params.pici_id = data.pici_id
    setCurrentUrl(this.$route.path)
    this.fetchPiciData()
    this.fetchStructure()
    this.fetchData()
    this.fetchSearchField()
  },
  methods: {
    paramsConvert(){
      // console.log(this.studentSearchFields);
      // console.log(this.params.conditions)
      this.copyParams = JSON.parse(JSON.stringify(this.params))
      for(var key in this.copyParams.conditions){
        this.copyParams.conditions[key] += ''
      }
    },
    searchData(){
      this.params.page = 1
      this.fetchData()
    },
    fetchStructure(){
      let data = JSON.parse(localStorage.getItem("info"))
      getListsLimitByOperatorClass({farther_code: '001', pici_id: this.params.pici_id, operator_id: data.id, addForm: ''}).then(response => {
        this.stuctureData = response.data.schoolStructure
      }).catch(error => { console.log(error) })
    },
    currentStudent(currentRow, oldCurrentRow) {
      this.studentInfo = currentRow
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
      getExpandedUnitCode().then(response => {
        this.expandedUnitCode.push(response.data)
      }).catch(error => {
        console.log(error)
      })
    },
    fetchSearchField() {
      getSearchField({ form_name: '入住公寓列表' }).then(response => {
        this.studentSearchFields = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchData() {
      this.listLoading = true
      getStudentLists(this.params).then(response => {
        this.list = response.data
        this.columns = response.data.columns
        this.pagination.current_page = response.data.current_page
        this.pagination.total = response.data.total
        this.listLoading = false
        this.tableHeight = window.innerHeight - this.$refs.multipleTable.$el.offsetTop - 192;
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
        getListsLimitByOperatorClass().then(response => {
          const data = response.data
          resolve(data)
          this.$nextTick(() => {
            this.$refs.treeBox.setCurrentKey('001')
            this.params.unit_code = '001'
            var _this = this
            _this.params.pici_id = _this.$store.getters.currentPici
            _this.fetchData()
            const node2 = this.$refs.treeBox.getCurrentNode()
            const data = JSON.parse(JSON.stringify(node2))
            this.selectUnit = data
          })
        }).catch(error => { console.log(error) })
      } else {
        getListsLimitByOperatorClass({ farther_code: node.data.unit_code }).then(response => {
          const data = response.data
          resolve(data)
        }).catch(error => {
          console.log(error)
        })
      }
    },
    handleTableSet() {
      this.$router.replace({ path: '/setting/setStudentGrid', query: { formName: '维护学生列表' }})
    },

    // 设置当前结构数据
    currentChange(data, node) {
      this.params.page = 1
      this.params.unit_code = data.unit_code
      this.fetchData()
    },
    handleInBed() {
      var _this = this
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要入住的学生', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => {
          ids.push(row.id)
        })
        ids = ids.join(',')
        inBedAuto({ studentIds: ids }).then(response => {
          const data = response.data
          this.$alert(data.join('<br />'), '设置入住信息', { type: 'info', dangerouslyUseHTMLString: true,
            callback: action => {
              _this.fetchData()
            }
          })
        }).catch(error => {
          console.log(error)
        })
      }
    },
    handleOutBed() {
      var _this = this
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要入住的学生', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => {
          ids.push(row.id)
        })
        ids = ids.join(',')
        outBed({ studentIds: ids }).then(response => {
          const data = response.data
          this.$alert(data.join('<br />'), '设置离宿信息', { type: 'info', dangerouslyUseHTMLString: true,
            callback: action => {
              _this.fetchData()
            }
          })
        }).catch(error => {
          console.log(error)
        })
      }
    },
    handleAddOut() {
      var _this = this
      const rows = JSON.parse(JSON.stringify(this.multipleSelection))
      if (rows.length === 0) {
        this.$alert('请先选择需要入住的学生', '提示', { type: 'warning' })
      } else {
        var ids = []
        rows.forEach(row => {
          ids.push(row.id)
        })
        ids = ids.join(',')
        addStuOut({ studentIds: ids }).then(response => {
          const data = response.data
          this.$alert(data.join('<br />'), '设置外宿信息', { type: 'info', dangerouslyUseHTMLString: true,
            callback: action => {
              _this.fetchData()
            }
          })
        }).catch(error => {
          console.log(error)
        })
      }
    }
  }
}
</script>
