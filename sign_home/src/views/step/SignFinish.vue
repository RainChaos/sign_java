<template>
  <el-scrollbar style="height:100%;overflow-x:hidden;">
    <div class="app-container container" style="height:100%;">
      <el-row>
        <div v-html="topInfoContent">
          {{ topInfoContent }}
        </div>
        <el-steps :active="4" simple style="border-top:3px solid #409EFF;border-radius: 0px;">
          <el-step title="填写报名基础信息" icon="el-icon-plus"/>
          <el-step title="确认修改报名信息" icon="el-icon-edit-outline"/>
          <el-step title="在线缴费" icon="el-icon-tickets"/>
          <el-step title="完成报名" icon="el-icon-check"/>
        </el-steps>
        <el-col :span="22" :offset="1" style="overflow: hidden;min-height: 400px;padding:30px 0px;">
          <div v-if="!showSuccessMsg" class="sign-not-success">
            <i class="el-icon-info"/>  你还没交清费用,还需交费用 {{ NeedPayMoney }} 元
            <el-button size="mini" style="float: right" type="warning" @click.native="handPrePayCharge">
              <i class="el-icon-d-arrow-left" aria-hidden="true"/> 去交费
            </el-button>
          </div>
          <div v-if="showSuccessMsg" class="sign-success">
            <i class="el-icon-success"/>  恭喜你报名成功
          </div>
          <div style="padding:13px 0px;font-size:14px;font-weight: bolder;margin-top:20px;">
            基础信息：
          </div>
          <el-row class="student-info-ct">
            <el-form label-position="left" inline size="small">
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="学号：">
                  <span>{{ studentInfo.sno }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="姓名：">
                  <span>{{ studentInfo.name }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="性别：">
                  <span>{{studentInfo.sex=="1"?"男":"女" }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="8">
                <el-form-item :label-width="labelWidth" label="报读院系：">
                  <span>{{ studentInfo.unit_code_yuanxi }}</span>
                </el-form-item>
              </el-col>
              <el-col :span="16">
                <el-form-item :label-width="labelWidth" label="报读专业：">
                  <span>{{ studentInfo.unit_code_zhuanye }}</span>
                </el-form-item>
              </el-col>
            </el-form>
          </el-row>
          <el-col :span="24" style="padding:13px 0px;font-size:14px;font-weight: bolder">
            收费项目列表：
          </el-col>
          <el-table
            ref="ChargeItemsTable"
            :data="chargeItems"
            tooltip-effect="dark"
            style="width: 100%"
            fit
            border>
            <el-table-column label="学期" width="220">
              <template slot-scope="scope">{{ scope.row.Term }}</template>
            </el-table-column>
            <el-table-column label="收费项目">
              <template slot-scope="scope">{{ scope.row.ItemName }}</template>
            </el-table-column>
            <el-table-column label="应收金额" width="150">
              <template slot-scope="scope">{{ scope.row.ReceivableMoney }}</template>
            </el-table-column>
            <el-table-column label="已交金额" width="150">
              <template slot-scope="scope">{{ scope.row.PayMoney }}</template>
            </el-table-column>
          </el-table>
          <el-row style="margin-top:20px">
            <div class="sum-money">
              合计应收金额：<span class="h-money">{{ ReceivableMoneySum.toFixed(2) }}</span> 元
              -
              合计已交金额：<span class="h-money">{{ PayMoneySum.toFixed(2) }}</span> 元
              <div style="float: right">
                = 需交金额 ：<span class="h-money">{{ NeedPayMoney.toFixed(2) }}</span> 元
              </div>
            </div>
          </el-row>
        </el-col>
        <el-col :span="24" style="padding: 20px;border-top: 1px solid #ddd;text-align: center;">
          <el-button type="primary" style="width:200px" @click.native="handPrePayCharge">
            <i class="el-icon-d-arrow-left" aria-hidden="true"/> 上一步
          </el-button>
        </el-col>
    </el-row></div>
  </el-scrollbar>
</template>

<script>
import { getStudentInfo, getStudentChargeItems } from '@/api/student'
import { getArticleByTitle } from '@/api/article'
export default {
  data() {
    return {
      topInfoContent: '',
      showSuccessMsg: false,
      showNotSuccessMsg: false,
      labelWidth: '100px',
      studentInfo: {},
      chargeItems: []
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
      console.log(this.ReceivableMoneySum)
      if (needPayMoney > 0 && this.ReceivableMoneySum > 0) {
        this.showSuccessMsg = false
      } else {
        this.showSuccessMsg = true
      }
      return needPayMoney
    }
  },
  created() {
    var student_id =  window.localStorage.getItem('student_id')
    this.fetchTopInfoContent()
    this.fetchStudentInfo(student_id)
    this.fetchStudentChargeItems(student_id)
  },
  methods: {
    fetchTopInfoContent() {
      getArticleByTitle({ title: '在线交费页面顶部提示信息' }).then(response => {
        this.topInfoContent = response.data.content
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStudentInfo(student_id) {
      getStudentInfo({ student_id: student_id }).then(response => {
        this.studentInfo = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStudentChargeItems(student_id) {
      getStudentChargeItems({ student_id: student_id }).then(response => {
        this.chargeItems = response.data
      }).catch(error => {
        console.log(error)
      })
    },
    handPrePayCharge() {
      var student_id = window.localStorage.getItem('student_id')
      this.$router.push({ path: '/pay', query: { student_id: student_id }})
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .sign-success{
    padding:15px;
    color:#FFF;
    background-color: #f56c6c;
    font-size:22px;
  }
  .sign-not-success{
    padding:15px;
    color: #dd0000;
    border: 1px solid #dd0000;
    font-size:22px;
    border-radius: 3px;
  }
  .show-student-info{
    margin-bottom: 5px !important;
  }
  .form-label-text{
    color:#dcac6c;
    font-size:12px;
  }
  .el-form-item--mini.el-form-item, .el-form-item--small.el-form-item {
    margin-bottom: 8px !important;
  }
  .student-info-ct{
    background-color:#ececec;
    border:1px solid #409EFF;
    overflow: hidden;
    padding:5px;
    border-radius: 3px;
  }
  .sum-money{
    padding:13px 0px;
    font-size:14px;
    font-weight: bolder;
    color:#666;
    background-color:#ececec;
    border:1px solid #409EFF;
    padding:15px;
    border-radius: 3px;
  }
  .icon-money{
    font-size:16px;
  }
  .h-money{
    color:#dd0000;
    font-size:30px;
  }
</style>
