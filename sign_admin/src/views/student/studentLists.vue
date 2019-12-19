<template>
  <div class="app-container" style="height: 100%;" ref="top">
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
      <el-col style="height:100%;width:calc(100% - 220px)">
        <el-row class="actionBtn">
          <el-col :span="24" style="border-bottom: 1px solid #ddd;padding-bottom:10px;">
            <el-button size="mini" type="primary" @click.native="handleAddStudent"><i class="el-icon-plus"
                                                                                      aria-hidden="true"/> 新增学生
            </el-button>
            <el-button size="mini" type="primary" @click.native="handleTzd"><i class="el-icon-printer"
                                                                               aria-hidden="true"/> 打印通知单
            </el-button>
            <!-- <el-button size="mini" type="danger" @click.native="handleDelete"><i class="el-icon-delete" aria-hidden="true"/> 批量删除</el-button> -->
            <!--<el-button size="mini" type="success" @click="fetchData"><i class="el-icon-refresh" aria-hidden="true"/> 刷 新</el-button>-->
            <el-button size="mini" type="primary" @click.native="searchData"><i class="el-icon-search"
                                                                                aria-hidden="true"/> 搜 索
            </el-button>
            <el-button size="mini" type="info" :loading="exportLoading" @click.native="exportData"><i
              class="fa fa-table" aria-hidden="true"/> 导 出
            </el-button>
            <el-button size="mini" style="float:right" type="warning" @click="handleTableSet"><i class="el-icon-setting"
                                                                                                 aria-hidden="true"/>
              表格设置
            </el-button>
          </el-col>
          <el-col :span="24">
            <el-form :inline="true" :model="params" size="mini">

              <el-col :span="3">
                <el-form-item style="margin-bottom: 0px;margin-top:10px;">
                  <label slot="label">批次:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                  <el-row>
                    <el-select v-model="params.pici_id" placeholder="请选择季节批次">
                      <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
                    </el-select>
                  </el-row>

                </el-form-item>
              </el-col>

              <el-col :span="3">
                <el-form-item style="margin-bottom: 0px;margin-top:10px;">
                  <label slot="label">关键词:&nbsp;&nbsp;&nbsp;&nbsp;</label>
                  <el-input v-model="params.conditions.keyword" placeholder="姓名/学号/身份证号码"/>
                </el-form-item>
              </el-col>


              <template v-for="(item) in studentSearchFields">
                <el-col :span="3">
                  <el-form-item
                    v-if="item.data_type=='省市区'"
                    :prop="item.field_name"
                    style="margin-bottom: 0px;margin-top:10px;">
                    <label slot="label">{{ item.describe }}:<span v-if="item.describe.length < 4">&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
                    <el-cascader :show-all-levels="false" clearable
                                 v-if="item.event_name != null && item.event_name.length > 0"
                                 v-model="params.conditions[item.field_name]" :options="regionOptions"
                                 :props="regionProps" style="width: 100%" :placeholder="'请选择'+item.describe"
                                 @change="item.event_name"/>
                    <el-cascader :show-all-levels="false" clearable
                                 v-else-if="item.event_name == null || item.event_name.length == 0"
                                 v-model="params.conditions[item.field_name]" :options="regionOptions"
                                 :props="regionProps" style="width: 100%"
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
                    <el-select v-model="params.conditions[item.field_name]" placeholder="" style="width: 100%">
                      <el-option key="-1" value="-1" label="全部"/>
                      <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text"
                                 :value="item2.value"/>
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
          :style="{'width':UnitWidth}"
          v-loading="listLoading"
          ref="multipleTable"
          :data="list.data"
          element-loading-text="正在加载中..."
          size="small"
          border
          fit
          highlight-current-row
          :height="tableHeight"
          @current-change="currentStudent"
          @selection-change="handleSelectionChange">
          <el-table-column fixed="left" align="center" type="selection" width="50"/>
          <el-table-column fixed="left" align="center" label="序号" width="50">
            <template slot-scope="scope">{{ scope.$index + 1 }}</template>
          </el-table-column>
          <template v-for="(item) in columns">
            <el-table-column v-if="item.is_show" :fixed="item.field_name =='name'?'left':false" :label="item.describe"
                             :key="item.field_name" :width="item.width" :align="item.is_center==1?'center':'left'">
              <template slot-scope="scope">{{ scope.row[item.field_name] }}</template>
            </el-table-column>
          </template>
          <el-table-column fixed="right" align="center" label="操作" width="80">
            <template slot-scope="scope">
              <el-button size="mini" type="text" @click="handleEdit(scope.$index, scope.row)"> 编辑</el-button>
              <el-button size="mini" type="text" @click="handleDeleteRow(scope.$index, scope.row)">删除</el-button>
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
    <el-dialog v-dialogDrag :visible.sync="dialogPrintTzdVisible" width="800px">
      <div slot="title"><i class="fa fa-print"/> 打印通知单</div>
      <tongzhidan ref="Tongzhidan" :student-info="studentInfo" :charge-items="chargeItems" content-id="PrintContent"/>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogPrintTzdVisible = false">关 闭</el-button>
        <el-button size="small" type="primary" @click="handlePrintView">打印预览</el-button>
        <el-button size="small" type="primary" @click="handlePrintTzd">直接打印</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
  import { getListsLimitByOperatorClass } from '@/api/unit'
  import { getExpandedUnitCode } from '@/api/config'
  import {
    getStudentLists,
    getStudentChargeItems,
    getSearchField,
    deleteStudentInfo,
    exportStudentLists
  } from '@/api/student'
  import { getPiciLists } from '@/api/pici'
  import { LodopFuncs } from '@/utils/LodopFuncs'
  import Tongzhidan from './components/Tongzhidan'
  import {
    getRegionLists,
    getRegion
  } from '@/api/region'

  import { setCurrentUrl, getCurrentUrl } from '@/utils/auth.js'

  export default {
    name: 'StudentLists',
    components: {
      Tongzhidan
    },
    data() {
      return {
        exportLoading: false,
        tableHeight: '0px',
        UnitWidth: '100%',
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
          form_name: '维护学生列表',
          conditions: {},
          keyword: ''
        },
        copyParams: {
          limit: 15,
          page: 1,
          pici_id: 0,
          is_width_dor: true,
          form_name: '维护学生列表',
          conditions: {},
          keyword: ''
        },
        multipleSelection: [],
        listLoading: true,
        statusSwitch: {
          activeValue: 1,
          inactiveValue: 0
        },
        dialogPrintTzdVisible: false,

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
        regionOptions: [],
        regionProps: {
          value: 'code',
          label: 'fullName',
          children: 'children',
          checkStrictly: true
        }

      }
    },
    activated() {
      /*setCurrentUrl(this.$route.path)
      this.fetchSearchField()
      this.fetchShengFenLists()*/
      setCurrentUrl(this.$route.path)
      let data = JSON.parse(localStorage.getItem('info'))
      LodopFuncs.beforeInit()
      this.fetchPiciData()
      this.fetchStructure()
      this.fetchSearchField()
      this.fetchData()
      this.fetchShengFenLists()
    },
    mounted() {
      // setCurrentUrl(this.$route.path)
      setCurrentUrl(this.$route.path)
      let data = JSON.parse(localStorage.getItem('info'))
      this.params.pici_id = data.pici_id
      LodopFuncs.beforeInit()
      this.fetchPiciData()
      this.fetchStructure()
      this.fetchSearchField()
      this.fetchData()
      this.fetchShengFenLists()
      // this.fetchExpandedUnitCode()

    },
    methods: {

      fetchShengFenLists() {
        setCurrentUrl(this.$route.path)
        getRegion().then(response => {
          var ShengFenLists = JSON.parse(JSON.stringify(response.data))
          // ShengFenLists.forEach((item, index) => {
          //   ShengFenLists[index]['children'] = []
          // })
          this.regionOptions = ShengFenLists
        }).catch(error => {
          console.log(error)
        })
      },
      searchData() {
        setCurrentUrl(this.$route.path)
        this.params.page = 1
        this.pagination.current_page = 0
        this.fetchData()
      },
      fetchStructure() {
        setCurrentUrl(this.$route.path)
        let data = JSON.parse(localStorage.getItem('info'))
        getListsLimitByOperatorClass({
          farther_code: '001',
          pici_id: this.params.pici_id,
          operator_id: data.id,
          addForm: ''
        }).then(response => {
          this.stuctureData = response.data.schoolStructure
        }).catch(error => {
          console.log(error)
        })
      },
      currentStudent(currentRow, oldCurrentRow) {
        setCurrentUrl(this.$route.path)
        this.studentInfo = currentRow
        // this.fetchStudentChargeItems(this.studentInfo['id'])
      },
      fetchStudentChargeItems(student_id) {
        setCurrentUrl(this.$route.path)
        getStudentChargeItems({ student_id: student_id }).then(response => {
          this.chargeItems = response.data
        }).catch(error => {
          console.log(error)
        })
      },
      fetchPiciData() {
        setCurrentUrl(this.$route.path)
        getPiciLists().then(response => {
          this.piciLists = response.data.data
        }).catch(error => {
          console.log(error)
        })
      },
      handleSelectionChange(val) {
        setCurrentUrl(this.$route.path)
        this.multipleSelection = val
      },
      fetchExpandedUnitCode() {
        setCurrentUrl(this.$route.path)
        getExpandedUnitCode().then(response => {
          this.expandedUnitCode.push(response.data)
        }).catch(error => {
          console.log(error)
        })
      },
      fetchSearchField() {
        setCurrentUrl(this.$route.path)
        getSearchField({ form_name: '维护学生列表' }).then(response => {
          this.studentSearchFields = response.data
        }).catch(error => {
          console.log(error)
        })
      },
      paramsConvert() {
        setCurrentUrl(this.$route.path)
        // console.log(this.studentSearchFields);
        // console.log(this.params.conditions)
        this.copyParams = JSON.parse(JSON.stringify(this.params))
        for (var key in this.copyParams.conditions) {
          this.copyParams.conditions[key] += ''
        }
      },
      fetchData() {
        setCurrentUrl(this.$route.path)
        this.UnitWidth = '99.999%'
        this.listLoading = true
        this.paramsConvert()
        getStudentLists(this.copyParams).then(response => {
          this.UnitWidth = '100%'
          this.list = response.data
          // console.log(this.list.data)
          this.columns = response.data.columns
          this.pagination.current_page = response.data.current_page
          this.pagination.total = response.data.total
          this.listLoading = false
          this.tableHeight = window.innerHeight - this.$refs.multipleTable.$el.offsetTop - 192

          // console.log(this.$refs.treeBox.$el.offsetTop)
          // this.tableHeight = 645
          // this.tableHeight += 'px'
          // console.log(window.innerHeight)
          // console.log(this.$refs.multipleTable.$el.offsetTop)
          // console.log(this.tableHeight);
        }).catch(error => {
          console.log(error)
        })
      },
      exportData() {
        setCurrentUrl(this.$route.path)
        this.paramsConvert()
        this.exportLoading = true
        exportStudentLists(this.copyParams).then(response => {
          let url = process.env.BASE_API + response.data
          this.exportLoading = false
          this.$alert('<a style="color:#409EFF;font-weight:900;text-align: center" target="_blank" href="' + url + '">点击下载</a>', '导出下载', {
            dangerouslyUseHTMLString: true,
            type: 'warning'
          })
        }).catch(error => {
          this.exportLoading = false
          console.log(error)
        })
      },
      handleSizeChange(val) {
        setCurrentUrl(this.$route.path)
        this.pagination.pre_page = val
        this.params.limit = this.pagination.pre_page
        this.params.page = 1
        this.fetchData()
      },
      handleCurrentChange(val) {
        setCurrentUrl(this.$route.path)
        this.params.limit = this.pagination.pre_page
        this.params.page = val
        this.fetchData()
      },
      // 加载单位结构数据
      loadNode(node, resolve) {
        setCurrentUrl(this.$route.path)
        if (node.level === 0) {
          getListsLimitByOperatorClass({ farther_code: '001', pici_id: this.params.pici_id }).then(response => {
            const data = response.data.schoolStructure
            resolve(data)
            // this.$nextTick(() => {
            //   this.$refs.treeBox.setCurrentKey('001')
            //   this.params.unit_code = '001'
            //   var _this = this
            //   _this.params.pici_id = _this.$store.getters.currentPici
            //   _this.fetchData()
            //   const node2 = this.$refs.treeBox.getCurrentNode()
            //   const data = JSON.parse(JSON.stringify(node2))
            //   this.selectUnit = data
            // })
          }).catch(error => {
            console.log(error)
          })
        } else {
          // getListsLimitByOperatorClass({ farther_code: node.data.unit_code }).then(response => {
          //   const data = response.data
          //   resolve(data)
          // }).catch(error => {
          //   console.log(error)
          // })
        }
      },
      handleAddStudent() {
        this.$router.push({ path: '/student/addStudent' })
      },
      handleTableSet() {
        this.$router.replace({ path: '/setting/setStudentGrid', query: { form_name: '维护学生列表' } })
      },
      /**
       * 跳转到设置权限页面
       */
      handleEdit(index, row) {
        this.$router.push({ path: '/student/editStudent', query: { student_id: row.id } })
      },

      // 设置当前结构数据
      currentChange(data, node) {
        setCurrentUrl(this.$route.path)
        this.params.page = 1
        this.params.unit_code = data.unit_code
        // console.log(this.params.unit_code);
        this.fetchData()
      },

      /**
       * 删除用户数据
       */
      handleDeleteRow(index, row) {
        setCurrentUrl(this.$route.path)
        this.deleteDatas(row.id)
      },
      /**
       * 批量删除用户数据
       */
      handleDelete() {
        setCurrentUrl(this.$route.path)
        const rows = JSON.parse(JSON.stringify(this.multipleSelection))
        if (rows.length === 0) {
          this.$alert('请先选择需要删除的学生', '提示', { type: 'warning' })
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
        setCurrentUrl(this.$route.path)
        this.$confirm('此操作将进行删除选中的学生吗, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          deleteStudentInfo({ student_id: ids }).then(response => {
            this.fetchData()
            this.$message.success('删除成功')
          })
        }).catch(() => {
          this.$alert({
            type: 'info',
            message: '已取消删除'
          })
        })
      },
      handleTzd() {
        setCurrentUrl(this.$route.path)
        if (Object.keys(this.studentInfo).length === 0) {
          this.$alert('请先选中需要打印通知单的学生', '提示', { type: 'warning' })
        } else {
          this.dialogPrintTzdVisible = true
        }
      },
      handlePrintView() {
        setCurrentUrl(this.$route.path)
        var LODOP = LodopFuncs.getLodop()
        var content = this.$refs.Tongzhidan.$el.innerHTML
        LODOP.ADD_PRINT_HTM(20, 60, '660px', '100%', content)
        LODOP.PREVIEW()
      },
      handlePrintTzd() {
        setCurrentUrl(this.$route.path)
        var LODOP = LodopFuncs.getLodop()
        var content = this.$refs.Tongzhidan.$el.innerHTML
        LODOP.ADD_PRINT_HTM(20, 60, '660px', '100%', content)
        LODOP.PRINT()
      }
    }
  }
</script>

<style scoped>

</style>
