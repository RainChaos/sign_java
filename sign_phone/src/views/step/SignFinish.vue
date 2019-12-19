<template>
  <div class="main-page">
    <x-header style="background-color:#0068b7;">
      4. 报名成功
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>
    <el-row id="stepN" style="background: #fbf9fe;margin:0px 10px 5px 10px;padding:5px;">
      <step-nar :step-number="stepNumber"></step-nar>
    </el-row>
    <div class="form-content" style="height: calc(100% - 150px);overflow: scroll;touch-action: pan-y;-webkit-overflow-scrolling: touch;">
      <div v-if="!showSuccessMsg" class="sign-not-success">
        <i class="el-icon-info"/>  你还没交费用,还需交费用 {{ NeedPayMoney }} 元
      </div>
      <div v-if="showSuccessMsg" class="sign-success" style="text-align: center">
        <i class="el-icon-success"/>  支付成功<br>实付款：￥{{PayMoneySum}}
      </div>
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
              <span>{{  studentInfo.sex=="1"?"男":"女" }}</span>
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
          style="margin-top:10px;"
          size="mini">
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
        <x-button v-if="NeedPayMoney>0" type="warn" @click.native="handPrePayCharge">
          <i class="el-icon-d-arrow-left" aria-hidden="true"/> 去交费
        </x-button>
        <x-button  type="default" @click.native="handlePreEdit">完善信息</x-button>
        <x-button  type="default" @click.native="getElectronicNotice">获取通知书</x-button>
        <x-button  type="default" @click.native="handleNext">退 出</x-button>
      </div>
    </div>
  </div>
</template>

<script>
  import { ViewBox,XHeader,Flexbox, FlexboxItem,XButton } from 'vux'
  import { Icon } from 'vux'
  import { getStudentInfo, getShowChargeItems, getStudentChargeItems } from '@/api/student'
  import StepNar from './components/StepNar'
  import { getElectronicNotice } from '@/api/electronicNotice'
  export default {
    components: {
      ViewBox,
      Flexbox,
      FlexboxItem,
      XButton,
      XHeader,
      Icon,
      StepNar
    },
    data () {
      return {
        stepNumber: 3,
        showSuccessMsg: false,
        labelWidth:'55px',
        studentInfo: {},
        chargeItems: [],
        multipleSelection: [],
        formSumbitLoading: false,
        showChargeItems: [],
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
          if(this.PayMoneySum > 0){
              this.showSuccessMsg = true
          }
        return needPayMoney
      }
    },
    mounted(){
      var student_id = window.localStorage.getItem('student_id')
      this.fetchStudentInfo(student_id)
      this.fetchStudentChargeItems(student_id)
    },
    methods:{
      checkboxInit(row, index) {
        var isDisabled = true
        this.showChargeItems.forEach(item => {
          if (item.item_name === row.ItemName && parseInt(item.is_disabled) === 1) {
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
              if (chargeItem.ItemName === item.item_name && parseInt(item.is_selected) === 1) {
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
          // console.log(JSON.stringify(response.data))
          this.chargeItems = response.data
          this.$nextTick(() => {
            this.fetchShowChargeItems()
          })
        }).catch(error => {
          console.log(error)
        })
      },

      handleNext() {
        var student_id = window.localStorage.getItem('student_id')
        this.$router.push({ path: '/', query:{openid: this.$route.query.openid}})
      },
      handlePreEdit() {
        var student_id = window.localStorage.getItem('student_id')
        var pici_id =this.$route.query.pici_id
        this.$router.push({ path: '/edit', query: { pici_id:pici_id, openid: this.$route.query.openid}})
      },
      handPrePayCharge() {
        var student_id = window.localStorage.getItem('student_id')
        var pici_id =this.$route.query.pici_id
        this.$router.push({ path: '/pay', query: { pici_id:pici_id, openid: this.$route.query.openid }})
      },
      getElectronicNotice() {
        // alert(this.studentInfo.sno);
          getElectronicNotice({sid : this.studentInfo.sno,name:this.studentInfo.name,idcode:this.studentInfo.idcode}).then(reponse=>{
              // console.log(data.url)
            window.location.href = process.env.BASE_API + '/notice/' + reponse.data
          }).catch(error=>{
              console.log( error )
          })
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
