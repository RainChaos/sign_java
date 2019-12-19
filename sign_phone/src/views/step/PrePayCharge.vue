<template>
  <div class="main-page">
    <x-header style="background-color:#0068b7;">
      3. 在线交费
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>
    <el-row id="stepN" style="background: #fbf9fe;margin:0px 10px 5px 10px;padding:5px;">
      <step-nar :step-number="stepNumber"></step-nar>
    </el-row>
    <div class="form-content" style="height: calc(100% - 100px);overflow: auto">
      <div class="weixtishi"><i class="el-icon-warning"/> 学生基础信息</div>
      <el-form label-position="left" inline size="mini" class="student-info">
        <el-row>
          <el-col :span="14">
            <el-form-item :label-width="labelWidth" label="学号：">
              <span>{{ studentInfo.sno }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label-width="labelWidth" label="姓名：">
              <span>{{ studentInfo.name }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="14">
            <el-form-item :label-width="labelWidth" label="院系：">
              <span>{{ studentInfo.unit_code_yuanxi }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item :label-width="labelWidth" label="性别：">
              <span>{{ studentInfo.sex=="1"?"男":"女" }}</span>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="24">
            <el-form-item :label-width="labelWidth" label="专业：">
              <span>{{ studentInfo.unit_code_zhuanye }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
      <div class="weixtishi"><i class="el-icon-warning"/> 收费项目</div>
      <el-table
          ref="ChargeItemsTable"
          :data="chargeItems"
          tooltip-effect="dark"
          fit
          border
          style="margin-top:10px; cursor: pointer"
          @select="handleSelectionSelect"
          @row-click="handleSelectionClick"
          @selection-change="handleSelectionChange">
        <el-table-column :selectable="checkboxInit" align="center" type="selection"  />
        <el-table-column label="收费项目">
          <template slot-scope="scope">{{ scope.row.ItemName }}</template>
        </el-table-column>
        <el-table-column label="应收金额">
          <template slot-scope="scope">{{ scope.row.ReceivableMoney }}</template>
        </el-table-column>
        <el-table-column label="已交金额">
          <template slot-scope="scope">{{ scope.row.PayMoney }}</template>
        </el-table-column>
      </el-table>
      <el-row class="sum-money" style="margin-top:20px">
        <el-row>
          合计应收金额: <span class="h-money">{{ ReceivableMoneySum.toFixed(2) }}</span> 元
        </el-row>
        <el-row>
          合计已交金额: <span class="h-money">{{ PayMoneySum.toFixed(2) }}</span> 元
        </el-row>
        <el-row>
          合计需交金额: <span class="h-money">{{ NeedPayMoney.toFixed(2) }}</span>元
        </el-row>
      </el-row>
      <div style="margin-top:20px;">
        <x-button v-if="studentInfo.is_can_charge == 1"  :gradients="['#1AAD19', '#1AAD19']" @click.native="handleChargeStatus">支 付</x-button>
        <x-button v-if="studentInfo.is_can_charge != 1" :disabled="true" type="default"  @click.native="handlePayCharge">支付通道已关闭</x-button>
        <x-button  type="default" @click.native="handlePre">上一步</x-button>
        <x-button  type="default" @click.native="handleNext">下一步</x-button>
      </div>
    </div>
    <div v-show="PayFormShow">
      <form method="post" :action="PayParams.PostUrl">
        <input type="hidden" name="biz_content"  :value="PayParams.BizContent"/>
        <input id="PaySubmit" type="submit" value="提交">
      </form>
    </div>

    <div >
      <x-dialog v-model="rxxzVisible" class="" style="width: 100%;" hide-on-blur>
        <i class="el-icon-circle-close" style="float: right; padding:3px 3px 0 0" @click="rxxzVisible=false"></i>
        <div class="img-box"style="padding:20px 20px 20px 20px">
          <!--<img src="../../../static/admission.jpg" style="max-width:120%">-->

          <x-button type="primary" @click.native="ruxuexuzhi">2019年入学须知（秋季）</x-button>
        </div>
        <!--<div style="padding:20px">-->
          <!--<x-button type="primary" @click.native="rxxzVisible=false">关 闭</x-button>-->
        <!--</div>-->
      </x-dialog>
    </div>
  </div>
</template>

<script>
  import { ViewBox,XHeader,Flexbox, FlexboxItem,XButton, XDialog} from 'vux'
  import { getStudentInfo, getShowChargeItems, getStudentChargeItems} from '@/api/student'
  import { getIp, getOpenId, SubMoneyByItems, getChargeImplStatus } from '@/api/charge'
  import { ChargePay } from '@/utils/chargePay'
  import { getQueryVariable } from '@/utils/auth'
  import StepNar from './components/StepNar'
  import FastClick from 'fastclick'
  import axios from 'axios/index'
export default {
  components: {
    ViewBox,
    Flexbox,
    FlexboxItem,
    XButton,
    XHeader,
    StepNar,
      XDialog
  },
  inject: ['reload'],
  data () {
    return {
      piciValue:1,
      rxxzVisible:true,
      stepNumber: 2,
      PayFormShow:false,
      labelWidth:'55px',
      studentInfo: {},
      chargeItems: [],
      multipleSelection: [],
      formSumbitLoading: false,
      showChargeItems: [],
      ipAddress: '',
      fingerprint: '',
      PayParams:{
        PostUrl:'',
        BizContent: ''
      }
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

    created(){

      // this.reload()
      // window.location.reload()
    try{FastClick.attach(document.html)}catch (e) {

    }
      this.next()

    // this.piciValue = this.$route.query.pici_id
    // this.fetchGetIpAddress()
    // this.fetchGetZhi()
    // var student_id = window.localStorage.getItem('student_id')
    // this.fetchStudentInfo(student_id)
    // this.fetchStudentChargeItems(student_id)
  },
  methods:{
    next(){
      var student_id = window.localStorage.getItem('student_id')
      var pici_id=this.$route.query.pici_id
      this.$router.push({ path: '/pay', query: { student_id: student_id ,pici_id:pici_id, openid: this.$route.query.openid}})
    },
      handleSelectionSelect(val){
          console.log(val)
      },
      handleSelectionClick(val){
          // this.$refs.ChargeItemsTable.toggleRowSelection(val)
          // console.log(val)
      },
    fetchGetIpAddress() {
      getIp().then(response => {
        this.ipAddress = response.data
      })
    },
    fetchGetZhi() {
      var _this = this
      var fp = new Fingerprint2()
      fp.get(function(result) {
        _this.fingerprint = result
      })
    },
    checkboxInit(row, index) {
      var isDisabled = true
      this.showChargeItems.forEach(item => {
        if (item.item_name === row.ItemName && (parseInt(item.is_disabled) === 1||item.is_disabled===true)) {
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
            if (chargeItem.ItemName === item.item_name && (parseInt(item.is_selected) === 1||item.is_selected===true)) {
              this.multipleSelection.push(chargeItem)
              this.$refs.ChargeItemsTable.toggleRowSelection(chargeItem)
            }
          })
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
        console.log(this.chargeItems)
        /*for(var i = 0; i < this.chargeItems.length; i++){
          if(this.chargeItems[i].ItemName == '住宿费' && this.studentInfo.dor_status != '入住'){
            this.chargeItems[i].ReceivableMoney = 0
          }
        }*/
        this.$nextTick(() => {
          this.fetchShowChargeItems()
        })
      }).catch(error => {
        console.log(error)
      })
    },
    handlePre() {
      var student_id = window.localStorage.getItem('student_id')
      var pici_id =this.$route.query.pici_id
      this.$router.push({ path: '/edit', query: { student_id: student_id  ,pici_id:pici_id ,openid: this.$route.query.openid}})
    },
    handleNext() {
      var student_id = window.localStorage.getItem('student_id')
      var pici_id =this.$route.query.pici_id
      this.$router.push({ path: '/finish', query: { student_id: student_id ,pici_id:pici_id, openid: this.$route.query.openid}})
    },
      ruxuexuzhi(){
          this.$router.push({ path: '/ruxuexuzhi', query: {openid: this.$route.query.openid}})
      },
    async handleChargeStatus(){
        // getChargeImplStatus().then(
        //     response => {
        //         var data = response.data
        //
        //         data = JSON.parse(data)
        //         console.log(data)
        //         if(data.status !== 'success'){
        //             alert("收费系统维护，请稍后再试。")
        //             return false
        //         }else{
        //
        //         }
        //     }
        // ).catch(
        //     error => {
        //         return false
        //     }
        // )
      this.handlePayCharge();
    }  ,
    handlePayCharge() {
        var openid = ''
      var _this = this
      var student_id = window.localStorage.getItem('student_id')
      var itemsIds = []
      this.multipleSelection.forEach(item => {
        itemsIds.push(item.AutoID)
      })
      if (this.NeedPayMoney <= 0) {
        this.$vux.alert.show({ title: '温馨提示', content: '你选择的收费项目不需要交费' })
        return false
      }
      if (this.studentInfo.is_can_charge!="1"){
        this.$alert('后台尚未开通学生缴费，请联系老师处理！', '提示', { type: 'warning' })
        return false
      }
      if(ChargePay.IsWeiXin() == 'false'){
        console.log("IsWeiXin:" + ChargePay.IsWeiXin())
        console.log("openid:" + openid)

      }else{

          openid = this.$route.query.openid
          console.log("openid:" + openid)

                  var params = {
                    studentId: student_id,
                    shiji: this.NeedPayMoney,
                    its: itemsIds.join(','),
                    fingerprint: this.fingerprint,
                    spbill_create_ip: this.ipAddress,
                    istelphone: ChargePay.IsWeiXin() ?  "2": "true",
                    openid: openid
                  }


                  SubMoneyByItems(params).then(response => {
                    var data = response.data
                    console.log(data)

                    // data = JSON.parse(data)
                    if (ChargePay.IsWeiXin() == 'false') {
                      data = $.parseJSON(data);
                    } else {
                      // data = data.replace(/\\\"/g, '\"');
                      data = JSON.parse(data);
                    }

                    if (data.result !== '0') {
                      _this.$vux.alert.show({ title: '温馨提示', content: data.msg })
                    } else {
                      // console.log(data.msg.Bankurl)
                      // var bankurl = data.msg.Bankurl
                      // bankurl = bankurl.replace('/\/g','\"')
                      var bankurl = JSON.stringify(data.msg.Bankurl)
                      bankurl = bankurl.replace(/\\\\\\/g, '')
                      bankurl = bankurl.substr(1,bankurl.length-2)
                      console.log(bankurl)
                      ChargePay.callpay(bankurl, "/phone/finish?student_id="+student_id+"&openid=" + openid)
                    }
                  }).catch(error => {
                    console.log(error)
                  })

      }


    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .sum-money{
    font-size:12px;
    font-weight: bolder;
    color:#666;
    background-color:#ececec;
    border:1px solid #409EFF;
    padding:5px;
    border-radius: 3px;
  }
  .h-money{
    color:#dd0000;
    font-size:16px;
  }
</style>
