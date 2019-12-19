<template>
  <el-scrollbar style="height:100%;overflow-x:hidden;">
    <div class="app-container container" style="height:100%;">
      <el-row>
        <div v-html="topInfoContent" style="margin-left: 60px">
          {{ topInfoContent }}
        </div>
        <el-steps :active="3" simple style="border-top:3px solid #409EFF;border-radius: 0px;">
          <el-step title="填写报名基础信息" icon="el-icon-plus"/>
          <el-step title="确认修改报名信息" icon="el-icon-edit-outline"/>
          <el-step title="在线缴费" icon="el-icon-tickets"/>
          <el-step title="完成报名" icon="el-icon-check"/>
        </el-steps>
        <el-col :span="22" :offset="1" style="overflow: hidden;min-height: 400px;padding:30px 0px;">
          <div class="sign-success">
            <i class="el-icon-success"/> 学生信息已保存到系统
          </div>
          <el-row class="student-info-ct" style="margin-top:15px;">
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
                  <span>{{ studentInfo.sex=="1"?"男":"女" }}</span>
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
          <el-col :span="24" style="padding:13px 0px;font-size:14px;font-weight: bolder;margin-top:20px;">
            收费项目列表【注意：已选中的收费项目为必选项目】：
          </el-col>
          <el-table
            ref="ChargeItemsTable"
            :data="chargeItems"
            tooltip-effect="dark"
            style="width: 100%"
            fit
            border
            @selection-change="handleSelectionChange">
            <el-table-column :selectable="checkboxInit" align="center" type="selection" width="55"/>
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
        <el-col :span="24" style="padding: 20px;border-top: 1px solid #ddd;text-align: center;margin-top:20px;">
          <el-button v-loading="formSumbitLoading" type="primary" style="width:200px" @click.native="handPreEditStudent">
            <i class="el-icon-d-arrow-left" aria-hidden="true"/> 上一步
          </el-button>
          <el-button :disabled="!(PayMoneySum==0)" type="primary" style="width:200px" @click.native="handlePayCharge">
            <i class="el-icon-plus" aria-hidden="true"/> 确认缴费
          </el-button>
          <el-button :disabled="!(PayMoneySum > 0)" type="primary" style="width:200px" @click.native="handleNext">
            <i class="el-icon-plus" aria-hidden="true"/> 下一步
          </el-button>
        </el-col>
      </el-row>
      <el-dialog v-dialogDrag :visible.sync="dialogPayCodeVisible" width="700px">
        <div slot="title"><i class="fa fa-wechat"/>  使用微信进行扫码支付</div>
        <el-row>
          <el-col :span="14" style="line-height: 30px;font-size:13px;padding:10px;font-weight: 900;">
            <div>1. 打开微信，调用扫一扫，扫右边的支付二维码</div>
            <div>2. 支付的时候需要确认，支付给的账号名称是否以学校名称为名的账号</div>
            <div>3. 如果支付的账号不是以学校名称命名的请联系学校老师说明问题</div>
            <div>4. 支付完成需要刷新页面查看收费项目的已交金额是否变化，如已确认已支付成功，请不要多次支付，可能会有延时到账</div>
          </el-col>
          <el-col :span="10" style="text-align: center;padding:20px;">
            <vue-qr :text="payCodeUrl" :size="180" :margin="0"/>
            <div style="line-height: 40px;">【微信】扫码去支付</div>
          </el-col>
        </el-row>
        <div slot="footer" style="text-align: center" class="dialog-footer">
          <el-button style="width:200px;" type="primary" size="small" @click="dialogPayCodeVisible = false">关 闭</el-button>
        </div>
      </el-dialog>
    </div>
  </el-scrollbar>
</template>

<script>
import { getStudentInfo, getShowChargeItems, getStudentChargeItems } from '@/api/student'
import { getArticleByTitle } from '@/api/article'
import { getIp, getOpenId, SubMoneyByItems, getChargeImplStatus } from '@/api/charge'
import { ChargePay } from '@/utils/chargePay'
import { Loading } from 'element-ui'
import VueQr from 'vue-qr'
export default {
  components: {
    VueQr
  },
  data() {
    return {
      dialogPayCodeVisible: false,
      payCodeUrl: '',
      topInfoContent: '',
      labelWidth: '100px',
      studentInfo: {},
      chargeItems: [],
      multipleSelection: [],
      formSumbitLoading: false,
      showChargeItems: [],
      loadingInstance: null,
      ipAddress: null,
      fingerprint: null
    }
  },
  computed: {
    ReceivableMoneySum: function() {
      var sumMoney = 0
      this.multipleSelection.forEach(item => {
        sumMoney += parseFloat(item.ReceivableMoney)
      })
      return sumMoney
    },
    PayMoneySum: function() {
      var sumMoney = 0
      this.multipleSelection.forEach(item => {
        sumMoney += parseFloat(item.PayMoney)
      })
      return sumMoney
    },
    NeedPayMoney: function() {
      var needPayMoney = this.ReceivableMoneySum - this.PayMoneySum
      return needPayMoney
    }
  },
  created() {
    this.loadingInstance = Loading.service()
  },
  mounted() {
    //this.fetchGetIpAddress()
    this.fetchGetZhi()
    var student_id =  window.localStorage.getItem('student_id')
    this.fetchTopInfoContent()
    this.fetchStudentInfo(student_id)
    this.fetchStudentChargeItems(student_id)
  },
  methods: {
    /*    fetchGetIpAddress() {
      getIp().then(response => {
        this.ipAddress = response.data
      })
    },*/
    fetchGetZhi() {
      var _this = this
      var fp = new Fingerprint2()
      fp.get(function(result) {
        _this.fingerprint = result
      })
    },
    fetchTopInfoContent() {
      getArticleByTitle({ title: '在线交费页面顶部提示信息' }).then(response => {
        this.topInfoContent = response.data.content
      }).catch(error => {
        console.log(error)
      })
    },
    checkboxInit(row, index) {
      var isDisabled = true
      this.showChargeItems.forEach(item => {
        if (item.item_name === row.ItemName && (parseInt(item.is_disabled) === 1 || item.is_disabled === true)) {
          isDisabled = false
        }
      })
      return isDisabled
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fetchShowChargeItems() {
      getShowChargeItems().then(response => {
        this.showChargeItems = response.data
        this.showChargeItems.forEach(item => {
          this.chargeItems.forEach(chargeItem => {
            if (chargeItem.ItemName === item.item_name && (parseInt(item.is_selected) === 1 || item.is_selected === true)) {
              this.multipleSelection.push(chargeItem)
              this.$refs.ChargeItemsTable.toggleRowSelection(chargeItem)
            }
          })
        })
        this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
          this.loadingInstance.close()
        })
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
        this.$nextTick(() => {
          this.fetchShowChargeItems()
          //this.loadingInstance.close()
        })
      }).catch(error => {
        console.log(error)
      })
    },
    handPreEditStudent() {
      var student_id = window.localStorage.getItem('student_id')
      var pici_id = this.$route.query.pici_id
      var education = this.$route.query.education
      console.log(student_id + pici_id + education)
      this.$router.push({ path: '/edit', query: { student_id: student_id, pici_id: pici_id, education: education } })
    },
    handleNext() {
      var student_id = window.localStorage.getItem('student_id')
      this.$router.push({ path: '/finish', query: { student_id: student_id } })
    },
    handlePayCharge() {
      var openid = ''
      var student_id = window.localStorage.getItem('student_id')
      console.log(student_id)
      var itemsIds = []
      this.multipleSelection.forEach(item => {
        itemsIds.push(item.AutoID)
      })
      if (this.NeedPayMoney <= 0) {
        this.$alert('你选择的收费项目不需要交费', '提示', { type: 'warning' })
        return false
      }
      if (this.studentInfo.is_can_charge != "1") {
        this.$alert('后台尚未开通学生缴费，请联系老师处理！', '提示', { type: 'warning' })
        return false
      }
      openid =this.$route.query.openid
      console.log("openid:" + openid)

      var params = {
        studentId: student_id,
        shiji: this.NeedPayMoney,
        its: itemsIds.join(','),
        fingerprint: this.fingerprint,
        spbill_create_ip: this.ipAddress,
        istelphone: false,
        openid: openid
      }
      SubMoneyByItems(params).then(response => {
        var data = response.data
        if (ChargePay.IsWeiXin() == 'false') {
          data = $.parseJSON(data);
        } else {
          data = JSON.parse(data);
        }
        if (data.result !== '0') {
          _this.$vux.alert.show({ title: '温馨提示', content: data.msg })
        } else {
            var bankurl = data.msg.Bankurl
            var redirect = bankurl
            this.payCodeUrl = redirect
            this.dialogPayCodeVisible = true
        }
      }).catch(error => {
        console.log(error)
      })
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
