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
        <el-alert
          :closable="false"
          title="提示: 一键分班需要设置要分班的班级数量，班级模板规则，比如：想要生成【计算机1班】、【计算机2班】，单位结构选择到年级之后，点击一键分班，模板填写【计算机#N#班】,然后选择班级数量，会把年级下所有学生根据男女均衡随机打乱自动分配到对应班级中"
          style="border-radius: 0px;font-weight: bolder;"
          type="error"/>
        <div style="margin-top: 5px; margin-bottom: 5px">
          <el-button type="mini" @click.native="handleFenBan"><i class="el-icon-menu" aria-hidden="true"/> 一键分班</el-button>
          <el-button size="mini" type="primary" @click.native="searchData"><i class="el-icon-search" aria-hidden="true"/> 查询</el-button>
          <el-button size="mini" style="float:right" type="warning" @click="handleTableSet"><i class="el-icon-setting" aria-hidden="true"/> 表格设置</el-button>
        </div>
        <el-row class="actionBtn">
          <el-col :span="24">
            <el-form :inline="true" :model="form" size="mini">

              <el-col :span="3">
              <el-form-item label="批次：" style="margin-bottom: 0px;margin-top:0px;">
                <el-select v-model="params.pici_id" placeholder="请选择季节批次" >
                  <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
                </el-select>
              </el-form-item>
              </el-col>

              <el-col :span="3">
              <el-form-item label="关键词:" style="margin-bottom: 0px;margin-top:0px;" >
                <el-input v-model="params.keyword" placeholder="姓名/学号/身份证号码"/>
              </el-form-item>
              </el-col>

              <template v-for="(item) in studentSearchFields">
                <el-col :span="3">
                  <el-form-item
                          v-if="item.data_type=='省市区'"
                          :prop="item.field_name"
                          style="margin-bottom: 0px;margin-top:0px;">
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
                          style="margin-bottom: 0px;margin-top:0px;">
                    <label slot="label">{{ item.describe }}:<span v-if="item.describe.length < 4">&nbsp;&nbsp;&nbsp;&nbsp;</span></label>
                    <el-input v-model="params.conditions[item.field_name]" style="width: 100%"/>
                  </el-form-item>
                </el-col>

                <el-col :span="3">
                  <el-form-item
                          v-if="item.data_type=='标准数据'"
                          :prop="item.field_name"
                          style="margin-bottom: 0px;margin-top:0px;">
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
                          style="margin-bottom: 0px;margin-top:0px;">
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



              <el-form-item style="margin-top: 0px;">

              </el-form-item>
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
    <el-dialog v-dialogDrag :visible.sync="dialogFormVisible" width="600px" id="lodingDiv">
      <div slot="title"><i class="fa fa-plus"/> 一键分班配置</div>
      <el-form ref="autoFenbanForm" :rules="rules" :model="form" size="small">
        <el-form-item label="班级名称模板 :" label-width="160px" style="margin-bottom:0px;margin-top:10px;">
          <el-input v-model="form.banji_template" style="width:100%"/>
        </el-form-item>
        <el-form-item label="班级数量 :" v-if="!form.ldybz" label-width="160px"  style="margin-bottom:0px;margin-top:10px;">
          <el-input-number v-model="form.banji_number" :min="1" :max="20" style="width: 40%"/>
        </el-form-item>
        <el-form-item style="margin-bottom: 0px;margin-top: 10px" v-if="form.ldybz">
            <el-form-item label-width="160px" label="劳班数量 :">
              <el-input-number v-model="form.lao_banji_number" :min="1" :max="20" style="width: 40%"/>
            </el-form-item>
            <el-form-item label-width="160px" label="非劳班数量 :">
              <el-input-number v-model="form.mian_banji_number" :min="1" :max="20" style="width: 40%"/>
            </el-form-item>
        </el-form-item>
        <el-form-item style="margin-bottom: 0px;margin-top: 10px">
          <el-col :span="8">
            <el-form-item label-width="160px" label="是否区分劳动预备制 :">
              <el-switch
                v-model="form.ldybz"
                :active-value="statusSwitch.activeValue"
                :inactive-value="statusSwitch.inactiveValue"
                />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label-width="160px" label="是否缴费 :">
              <el-select v-model="form.is_pay" placeholder="请选择是否交费" style="width:180px">
                <el-option v-for="item in YesOrNotLists" :key="item.value" :label="item.text" :value="item.value"/>
              </el-select>
            </el-form-item>
          </el-col>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button :loading="formSumbitLoading"  size="small" type="primary" @click="handleSubmit">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getListsLimitByOperatorClass } from '@/api/unit'
import { getStructureLists } from '@/api/unit'
import { getExpandedUnitCode } from '@/api/config'
import { getStudentLists, autoFenBan,getSearchField } from '@/api/student'
import { getPiciLists } from '@/api/pici'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
import {
  getRegionLists,
  getRegion
} from '@/api/region'
export default {
  data() {
    return {
      regionOptions: [],
      regionProps: {
        value: 'code',
        label: 'fullName',
        children: 'children',
        checkStrictly: true
      },
      tableHeight: '0px',
      statusSwitch: {
        activeValue: true,
        inactiveValue: false
      },
      text:'false',
      fullscreenLoading:false,
      formSumbitLoading: false,
      dialogFormVisible:false,
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
        { value: '全部', text: '全部' },
        { value: '是', text: '是' },
        { value: '否', text: '否' }
      ],
      params: {
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_dor: true,
        form_name: '一键分班列表',
        conditions: {},
        keyword: ''
      },
      copyParams:{
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_dor: true,
        form_name: '一键分班列表',
        conditions: {},
        keyword: ''
      },
      multipleSelection: [],
      listLoading: true,
      form: {
        ldybz:false,
        is_fenban:false,
        is_pay:'全部',
        lao_banji_number: 3,
        mian_banji_number: 3,
        banji_number: 3,
        banji_template: '计算机#N#班'
      },
      unitProps: {
        value: 'unit_code',
        label: 'unit_name',
        children: 'children'
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
      rules: {
        unit_code: [
          { required: true, message: '请选择需要调整到的目标班级', trigger: 'blur' }
        ]
      }
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    let data = JSON.parse(localStorage.getItem("info"));
    this.fetchPiciData()
    this.fetchStructure()
    this.fetchSearchField()
    this.fetchExpandedUnitCode()
    this.fetchData()
    this.fetchShengFenLists()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    let data = JSON.parse(localStorage.getItem("info"));
    this.params.pici_id = data.pici_id
    this.fetchPiciData()
    this.fetchStructure()
    this.fetchSearchField()
    this.fetchExpandedUnitCode()
    this.fetchData()
    this.fetchShengFenLists()

  },
  methods: {
    searchData(){
      setCurrentUrl(this.$route.path)
      this.params.page = 1
      this.pagination.current_page = 0
      this.fetchData()
    },
    fetchShengFenLists() {
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
    fetchSearchField() {
      getSearchField({ form_name: '一键分班列表' }).then(response => {
        this.studentSearchFields = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStructure(){
      let data = JSON.parse(localStorage.getItem("info"))
      getListsLimitByOperatorClass({farther_code: '001', pici_id: this.params.pici_id, operator_id: data.id, addForm: ''}).then(response => {
        this.stuctureData = response.data.schoolStructure
        this.unitOptions =response.data.schoolStructure
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
    paramsConvert(){
      // console.log(this.studentSearchFields);
      // console.log(this.params.conditions)
      this.copyParams = JSON.parse(JSON.stringify(this.params))
      for(var key in this.copyParams.conditions){
        this.copyParams.conditions[key] += ''
      }
    },
    fetchData() {
      this.listLoading = true
      this.paramsConvert()
      getStudentLists(this.copyParams).then(response => {
        this.list = response.data
        this.columns = response.data.columns
        this.pagination.current_page = response.data.current_page
        this.pagination.total = response.data.total
        this.tableHeight = window.innerHeight - this.$refs.multipleTable.$el.offsetTop - 192;
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
        }).catch(error => {
          console.log(error)
        })
      } else {
        getStructureLists({ farther_code: node.data.unit_code }).then(response => {
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
    handleFenBan() {
      var unit_code = this.params.unit_code
      var class_length = parseInt(this.$store.getters.structureLength)
      if (unit_code.length !== (class_length - 3)) {
        this.$alert('请先选择到单位年级', '提示', { type: 'warning' })
      } else {
        this.dialogFormVisible=true
      }
    },
    handleSubmit(){
      this.formSumbitLoading=true
      var _this=this
      if (this.form.banji_template.indexOf("#N#")<0) {
        this.$alert('请按提示正确填写班级模板名称', '提示', { type: 'warning' })
        this.formSumbitLoading=false
        return
      }
       var params =  {
         nianji_code: this.params.unit_code,
         banji_number: this.form.banji_number,
         banji_template: this.form.banji_template,
         pici_id: this.params.pici_id,
         is_pay: this.form.is_pay,
         is_ldybz:this.form.ldybz,
         lao_banji_number: this.form.lao_banji_number,
         mian_banji_number: this.form.mian_banji_number,
       }
      const loading = this.$loading({
        lock: true,
        text: '正在分班，请稍候...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)',
        target: document.querySelector('#lodingDiv')
      });
      autoFenBan(params).then(response => {
        this.dialogFormVisible=false
        this.formSumbitLoading=false
        loading.close()
        this.$alert('一键分班成功', '提示信息', { type: 'success',
          callback: action => {
            _this.fetchData()
            _this.fetchStructure()
          }
        })
      }).catch(error => {
        console.log(error)
        loading.close()
        this.dialogFormVisible=false
        this.formSumbitLoading=false
      })
    }
  }
}
</script>

<style scoped>

</style>
