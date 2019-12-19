<template>
<div class="app-container" style="height: 100%;padding:10px;border:2px solid #ddd;">
  <object id="YingxinStudentPlugin" type="application/cert-reader" width="0" height="0" style="display: none" />
<!--  <div style="height:40px;background-color: #409EFF;color:#fff;font-weight: bolder;font-size:20px;line-height: 40px;padding-left: 20px;letter-spacing: 1px;text-align: center">-->
<!--    {{ SchoolName }}现场迎新确认到校报到-->
<!--  </div>-->
  <el-row class="actionBtn" style="margin-top:10px;">
    <el-form ref="form" :inline="true" :model="params" label-width="80px" size="mini">
      <el-form-item label="批次：" label-width="55px" style="margin-bottom: 0px;">
        <el-select v-model="params.pici_id" placeholder="请选择季节批次" style="width:180px">
          <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id" />
        </el-select>
      </el-form-item>
      <el-form-item label="关键词:" style="margin-bottom:0px;">
        <el-input v-model="params.conditions.keyword" style="width:300px" placeholder="姓名/学号/身份证号码" />
      </el-form-item>
      <el-form-item style="margin-left: 15px;margin-bottom: 0px;">
        <el-button type="primary" @click.native="fetchSearch"><i class="el-icon-search" aria-hidden="true" /> 搜 索</el-button>
        <el-button type="warning" @click="ReadIDCardInfo"><i class="fa fa-search" aria-hidden="true" /> 读取身份证</el-button>
      </el-form-item>
    </el-form>
  </el-row>

  <el-row>
    <el-col :span="12">
      <el-table :style="{'width':UnitWidth}" v-loading="listLoading" ref="YxinStudentTable" :data="list.data" height="563" element-loading-text="正在加载中..." size="small" border fit highlight-current-row @row-click="currentStudent">
        <el-table-column fixed="left" align="center" label="序号" width="50">
          <template slot-scope="scope">{{ scope.$index + 1 }}</template>
        </el-table-column>
        <el-table-column fixed="left" label="姓名" width="100">
          <template slot-scope="scope">{{ scope.row.name }}</template>
        </el-table-column>
        <el-table-column label="学号" width="150">
          <template slot-scope="scope">{{ scope.row.sno }}</template>
        </el-table-column>
        <el-table-column label="身份证号码" width="180" align="center">
          <template slot-scope="scope">{{ scope.row.idcode }}</template>
        </el-table-column>
        <el-table-column label="是否到校报到" width="130" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.is_register===0 || scope.row.is_register===false || scope.row.is_register==='否'" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.is_register===1 || scope.row.is_register===true || scope.row.is_register==='是'" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column label="是否已经交费" width="130" align="center">
          <template slot-scope="scope">
            <i v-if="scope.row.is_pay===0 || scope.row.is_pay===false || scope.row.is_pay==='否'" class="el-icon-close" style="color:firebrick" />
            <i v-if="scope.row.is_pay===1 || scope.row.is_pay===true || scope.row.is_pay==='是'" class="el-icon-check" style="color:forestgreen" />
          </template>
        </el-table-column>
        <el-table-column label="性别" width="80" align="center">
          <template slot-scope="scope">{{ scope.row.sex }}</template>
        </el-table-column>

        <el-table-column label="联系号码" width="150" align="center">
          <template slot-scope="scope">{{ scope.row.phone }}</template>
        </el-table-column>
        <el-table-column label="专业" width="600" align="center">
          <template slot-scope="scope">{{ scope.row.unit_code }}</template>
        </el-table-column>
        <!-- <el-table-column label="年级" width="150" align="center">
          <template slot-scope="scope">{{ scope.row.unit_code_nianji }}</template>
        </el-table-column>
        <el-table-column label="院系" width="150">
          <template slot-scope="scope">{{ scope.row.unit_code_yuanxi }}</template>
        </el-table-column>
        <el-table-column label="专业" width="200">
          <template slot-scope="scope">{{ scope.row.unit_code_zhuanye }}</template>
        </el-table-column>
        <el-table-column label="班级" width="200">
          <template slot-scope="scope">{{ scope.row.unit_code_banji }}</template>
        </el-table-column> -->
        <el-table-column label="住宿状态" width="80">
          <template slot-scope="scope">{{ scope.row.dor_status }}</template>
        </el-table-column>
        <el-table-column label="宿舍" width="300">
          <template slot-scope="scope">{{ scope.row.dor_bed_number }}</template>
        </el-table-column>
<!--        <el-table-column label="床位" width="300">-->
<!--          <template slot-scope="scope">{{ scope.row.dor_bed_number }}</template>-->
<!--        </el-table-column>-->
      </el-table>
      <el-row v-if="pagination.total>0" style="background: #ececec;">
        <el-pagination :current-page="pagination.current_page" :page-sizes="[15,30, 50, 100]" :page-size="pagination.pre_page" :total="pagination.total" :pager-count="4" background layout="total, sizes, prev, pager, next" @size-change="handleSizeChange"
          @current-change="handleCurrentChange" />
      </el-row>
    </el-col>

    <el-col :span="12">
      <el-row >
        <el-row >
          <el-row :span="8">
            <div style="height:40px;background-color: #ececec;color: #666;line-height: 40px;padding:0px 15px;font-weight: bolder;">
              {{ studentInfo.name}} : 收费项目
            </div>
            <el-table ref="ChargeItemsTable" :data="chargeItems" tooltip-effect="dark" style="width: 100%" height="220" fit size="small" border>
              <el-table-column label="学期" width="220">
                <template slot-scope="scope">{{ scope.row.Term }}</template>
              </el-table-column>
              <el-table-column label="收费项目">
                <template slot-scope="scope">{{ scope.row.ItemName }}</template>
              </el-table-column>
              <el-table-column label="应收金额" width="100">
                <template slot-scope="scope">{{ scope.row.ReceivableMoney }}</template>
              </el-table-column>
              <el-table-column label="已交金额" width="100">
                <template slot-scope="scope">{{ scope.row.PayMoney }}</template>
              </el-table-column>
            </el-table>
            <el-row style="height:40px;line-height: 40px;padding:0px 15px;background-color: #ececec;font-size:14px;font-weight: bolder;">
              <div class="sum-money">
                合计应收：<span class="h-money">￥{{ ReceivableMoneySum.toFixed(2) }}</span>
                —
                合计已交：<span class="h-money">￥{{ PayMoneySum.toFixed(2) }}</span>
                = 需交金额 ：<span class="h-money">￥{{ NeedPayMoney.toFixed(2) }}</span>
                <div style="float: right">

                </div>
              </div>
            </el-row>
          </el-row>

          <el-row :span="16" style="height:300px; margin-top:2px;">
            <div style="height:40px;background-color: #ececec;color: #666;line-height: 40px;font-weight: bolder;padding:0px 15px;">
              {{ studentInfo.name }} 详细信息， 是否到校报到：
              <i v-if="studentInfo.is_register===0 || studentInfo.is_register === false||studentInfo.is_register==='否'" class="el-icon-close" style="color:firebrick" />
              <i v-if="studentInfo.is_register===1 || studentInfo.is_register === true||studentInfo.is_register==='是'" class="el-icon-check" style="color:forestgreen"/>
              <el-button type="primary" size="mini" @click="editStuInfo" v-if=""><i class="fa fa-edit" aria-hidden="true" /> 编辑学生信息</el-button>
            </div>
            <el-form label-position="right" inline size="mini" style="border:1px solid #ececec;overflow: hidden;height:260px;margin-bottom: 20px;overflow: hidden" class="student-info">
              <el-scrollbar style="height:260px;overflow: hidden">
                <el-col v-for="(item) in columns" :key="item.field_name" :span="item.field_name=='unit_code'?24:12" v-if="item.is_show==1||item.is_show==true">
                  <el-form-item  :label="item.describe + '：'" class="studentInfo">
                    <span> {{ studentInfo[item.field_name] }}</span>
                  </el-form-item>
                </el-col>
              </el-scrollbar>
            </el-form> ,
          </el-row>
        </el-row>
      </el-row>
    </el-col>


  </el-row>




  <el-row style="margin-top:20px;text-align: center;border-top:1px solid #ddd;padding:20px;">
    <el-button :disabled="Object.keys(studentInfo).length === 0" style="width: 200px;" type="warning" @click.native="handleJfd"><i class="fa fa-print" aria-hidden="true" /> 打印缴费凭证</el-button>

    <el-button :disabled="Object.keys(studentInfo).length === 0" style="width: 200px;" type="warning" @click.native="handleTzd"><i class="fa fa-print" aria-hidden="true" /> 打印通知单</el-button>
    <el-button v-if="studentInfo.is_register == '是'" :disabled="Object.keys(studentInfo).length === 0" style="width: 200px;" type="info" @click.native="handleSumbit"><i class="fa fa-mail-reply-all " aria-hidden="true" /> 取消报到</el-button>
    <el-button v-if="studentInfo.is_register == '否'" :disabled="Object.keys(studentInfo).length === 0" style="width: 200px;" type="primary" @click.native="handleCancel"><i class="el-icon-check" aria-hidden="true" /> 确认到校</el-button>
    <el-button v-if="studentInfo.is_zhuxiao=='是'" :disabled="Object.keys(studentInfo).length === 0" type="primary" @click.native="handleInBed"><i class="fa fa-table" aria-hidden="true" /> 设置入住</el-button>
    <el-button v-if="studentInfo.is_zhuxiao=='是'" :disabled="Object.keys(studentInfo).length === 0" type="primary" @click.native="handleOutBed"><i class="fa fa-table" aria-hidden="true" /> 设置离宿</el-button>
    <el-button v-if="studentInfo.is_zhuxiao=='否'" :disabled="Object.keys(studentInfo).length === 0" type="primary" @click.native="handleOutBed"><i class="fa fa-table" aria-hidden="true" /> 设置外宿</el-button>
  </el-row>

  <el-dialog v-dialogDrag :visible.sync="dialogPrintTzdVisible" width="800px">
    <div slot="title"><i class="fa fa-print" /> 打印通知单</div>
    <tongzhidan ref="Tongzhidan" :student-info="studentInfo" :charge-items="chargeItems" content-id="PrintContent" />
    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="dialogPrintTzdVisible = false">关 闭</el-button>
      <el-button size="small" type="primary" @click="handlePrintView">打印预览</el-button>
      <el-button size="small" type="primary" @click="handlePrintTzd">直接打印</el-button>
    </div>
  </el-dialog>

  <el-dialog v-dialogDrag :visible.sync="dialogPrintJfdVisible" width="800px">
    <div slot="title"><i class="fa fa-print" /> 打印缴费凭证</div>
    <jiaofeidan ref="Jiaofeidan" :student-info="studentInfo" :charge-items="chargeItems" content-id="PrintContent" />
    <div slot="footer" class="dialog-footer">
      <el-button size="small" @click="dialogPrintJfdVisible = false">关 闭</el-button>
      <el-button size="small" type="primary" @click="handlePrintViewJfd">打印预览</el-button>
      <el-button size="small" type="primary" @click="handlePrintJfd">直接打印</el-button>
    </div>
  </el-dialog>

</div>
</template>

<script>
import {
  getStudentLists
} from '@/api/student'
import {
  getPiciLists
} from '@/api/pici'
import {
  getStudentChargeItems,
  setRegister
} from '@/api/student'
import {
  inBedAuto,
  outBed,
  addStuOut
} from '@/api/dor'
import {
  ReadIDCard
} from '@/utils/student'
import {
  LodopFuncs
} from '@/utils/LodopFuncs'
import Tongzhidan from './components/Tongzhidan'
import Jiaofeidan from './components/Jiaofeidan'
import axios from 'axios'
import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'
export default {
  name: 'Yingxin',
  components: {
    Tongzhidan,
    Jiaofeidan
  },
  data() {
    return {
      UnitWidth: '100%',
      InfoLabelWidth: '150px',
      piciLists: [],
      params: {
        limit: 15,
        page: 1,
        pici_id: 0,
        is_width_charge_items: false,
        is_width_dor: true,
        formName: '现场迎新列表',
        form_name: '现场迎新列表',
        conditions: {},
        keyword:''
      },
      pagination: {
        current_page: 0,
        pre_page: 15,
        total: 0
      },
      listLoading: false,
      list: {},
      columns: [],
      studentInfo: {},
      tableRowsIndex: ['id'],
      chargeItems: [],
      dialogPrintTzdVisible: false,
      dialogPrintJfdVisible: false
    }
  },
  computed: {
    ReceivableMoneySum: function() {
      var sumMoney = 0
      this.chargeItems.forEach(item => {
        sumMoney += parseFloat(item.ReceivableMoney)
      })
      return sumMoney
    },
    PayMoneySum: function() {
      var sumMoney = 0
      this.chargeItems.forEach(item => {
        sumMoney += parseFloat(item.PayMoney)
      })
      return sumMoney
    },
    NeedPayMoney: function() {
      var needPayMoney = this.ReceivableMoneySum - this.PayMoneySum
      if (needPayMoney > 0 && this.ReceivableMoneySum > 0) {
        this.showSuccessMsg = false
      } else {
        this.showSuccessMsg = true
      }
      return needPayMoney
    },
    SchoolName: function() {
      return this.$store.getters.web_site_name
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    var _this = this
    _this.params.pici_id = _this.$store.getters.currentPici
    LodopFuncs.beforeInit()
    this.fetchPiciData()
    this.fetchData()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    var _this = this
    _this.params.pici_id = _this.$store.getters.currentPici
    LodopFuncs.beforeInit()
    this.fetchPiciData()
    this.fetchData()
  },
  methods: {
    fetchSearch() {
      this.studentInfo = {}
      this.params.page = 1
      this.pagination.current_page = 0
      this.fetchData()
    },
    editStuInfo(){
      this.$router.push({ path: '/student/editStudent', query: { student_id: this.studentInfo['id']}})
    },
      fetchData() {
      this.UnitWidth = '99%'
      this.listLoading = true
      getStudentLists(this.params).then(response => {
        this.UnitWidth = '100%'
        this.list = response.data
        this.columns = response.data.columns
        this.pagination.current_page = response.data.current_page
        this.pagination.total = response.data.total
        this.listLoading = false
        if (response.data.data.length === 0) {
          this.chargeItems = []
          this.studentInfo = {}
          this.$alert('没有查到相关学生数据，请先新增填写报名后再查询确认报到', '错误提示', {
            type: 'error'
          })
        } else {
          this.studentInfo=response.data.data[0]
          this.fetchStudentChargeItems(this.studentInfo['sno'])
          // if (Object.keys(this.studentInfo).length === 0) {
          //   this.studentInfo = this.list.data[0]
          //   this.$refs.YxinStudentTable.setCurrentRow(this.list.data[0])
          // } else {
          //   this.list.data.forEach(item => {
          //     if (item.id === this.studentInfo.id) {
          //       this.$refs.YxinStudentTable.setCurrentRow(item)
          //     }
          //   })
          // }
          // this.fetchStudentChargeItems(this.studentInfo['id'])
        }
      }).catch(error => {
        console.log(error)
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
    currentStudent(row) {
      // console.log(row);
      // console.log(this.columns)
      this.studentInfo = row
      // console.log(this.studentInfo['unit_code'])
      //this.studentInfo = JSON.parse(JSON.stringify(currentRow))
      this.fetchStudentChargeItems(this.studentInfo['sno'])
    },
    fetchStudentChargeItems(sno) {
      getStudentChargeItems({
        sno: sno
      }).then(response => {
        this.chargeItems = response.data
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
    handleCancel() {
      var _this = this

      this.$confirm('确认【' + this.studentInfo.name + '】已到校报到, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        setRegister({
          student_id: this.studentInfo['id'],
          is_register: '是'
        }).then(response => {
          _this.studentInfo['is_register'] = '是'
          _this.$alert('确认到校报到成功', '成功提示', {
            type: 'success',
            callback: action => {
              //_this.fetchData()
            }
          })
        })
      })
    },
    handleSumbit() {
      var _this = this
      this.$confirm('取消【' + this.studentInfo.name + '】已到校报到, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        setRegister({
          student_id: this.studentInfo['id'],
          is_register: '否'
        }).then(response => {
          _this.studentInfo['is_register'] = '否'
          _this.$alert('取消到校报到成功', '成功提示', {
            type: 'success',
            callback: action => {
              _this.fetchData()
            }
          })
        })
      })
    },
    handleTzd() {
      this.dialogPrintTzdVisible = true
    },
    handleJfd() {
      this.dialogPrintJfdVisible = true
    },
    handlePrintView() {
      var LODOP = LodopFuncs.getLodop()
      var content = this.$refs.Tongzhidan.$el.innerHTML
      LODOP.ADD_PRINT_HTM(20, 60, '660px', '100%', content)
      LODOP.PREVIEW()
    },
    handlePrintTzd() {
      var LODOP = LodopFuncs.getLodop()
      var content = this.$refs.Tongzhidan.$el.innerHTML
      LODOP.ADD_PRINT_HTM(20, 60, '660px', '100%', content)
      LODOP.PRINT()
    },
    handlePrintViewJfd() {
      var LODOP = LodopFuncs.getLodop()
      var content = this.$refs.Jiaofeidan.$el.innerHTML
      LODOP.ADD_PRINT_HTM(20, 60, '660px', '100%', content)
      LODOP.PREVIEW()
    },
    handlePrintJfd() {
      var LODOP = LodopFuncs.getLodop()
      var content = this.$refs.Jiaofeidan.$el.innerHTML
      LODOP.ADD_PRINT_HTM(20, 60, '660px', '100%', content)
      LODOP.PRINT()
    },
    ReadIDCardInfo() {
     /* try {
        var result = ReadIDCard('YingxinStudentPlugin')
        if (result.resultFlag === 0) {
          const info = result.resultContent
          this.params.keyword = info.certNumber
          this.studentInfo = {}
          this.fetchData()
        } else {
          this.$alert(result.errorMsg, '错误提示', {
            type: 'error'
          })
        }
      } catch (e) {
        this.$alert('使用身份证读卡器读取信息，由于插件原因只能使用IE浏览器，本系统只支持IE10以上的浏览器，所以只能使用IE10以上的浏览器', '错误提示', {
          type: 'error'
        })
      }*/
      axios.get("http://localhost:29999/api/ReadMsg").then(res=>{
        // console.log(res.data)
        this.params.keyword = res.data.cardno
        this.studentInfo = {}
        this.fetchData()
      }).catch(error=>{
        console.log(error)
      })
    },
    handleInBed() {
      var _this = this
      inBedAuto({
        studentIds: this.studentInfo.id
      }).then(response => {
            _this.fetchData()
      }).catch(error => {
        _this.fetchData()
      })

    },
    handleOutBed() {
      var _this = this
      outBed({
        studentIds: this.studentInfo.id
      }).then(response => {
        _this.fetchData()

      }).catch(error => {
        console.log(error)
        _this.fetchData()
      })
    },
    handleAddOut() {
      var _this = this
      addStuOut({
        studentIds: this.studentInfo.id
      }).then(response => {
        const data = response.data
        this.$alert(data.join('<br />'), '设置外宿信息', {
          type: 'info',
          dangerouslyUseHTMLString: true,
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
</script>

<style scoped>
.el-form-item--mini.el-form-item,
.el-form-item--small.el-form-item {
  margin-bottom: 0px !important;
}

.studentInfo {
  width: 100%;
  margin-top: 3px;
}

.icon-money {
  font-size: 16px;
}

.h-money {
  color: #dd0000;
  font-size: 20px;
}
</style>
