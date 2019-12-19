<template>
  <div :ref="ContentId" style="width:600px;margin: 10px auto;overflow: hidden;font-size: 14px;color:#000;">
    <div style="width:100%;overflow: hidden;">
      <div style="position:absolute;"><vue-barcode :value="StudentInfo.sno" :options="barcode_option" tag="svg" /></div>
      <div style="float:left;line-height: 68px;text-align: center;width:100%;">
        <font face="宋体" style="letter-spacing: 2px;font-size:20px;font-weight:bold;">报到通知单</font>
      </div>
    </div>
    <div style="line-height: 20px;width:100%;;overflow: hidden;">
      &nbsp; &nbsp; &nbsp;<span style="font-weight:bolder;">{{ StudentInfo.name }}</span> 同学，你已成功报名了我校（
      <span style="font-weight:bolder;">{{ SchoolName }}</span>）的&nbsp;
      <span style="font-weight:bolder;">{{ StudentInfo.unit_code_yuanxi }}</span>-
      <span style="font-weight:bolder;">{{ StudentInfo.unit_code.split('>')[1] }}</span>&nbsp;专业，
      基本信息如下：
    </div>
    <table border="1" style="width:100%;border: 1px solid #000; border-collapse: collapse;letter-spacing: 1px;margin-top:10px;">
      <tr>
        <td>学号：</td>
        <td> {{ StudentInfo.sno }}</td>
        <td>姓名：</td>
        <td>{{ StudentInfo.name }}</td>
      </tr>
      <tr>
        <td>身份证号码：</td>
        <td>{{ StudentInfo.idcode }}</td>
        <td>电话号码：</td>
        <td>{{ StudentInfo.phone }}</td>
      </tr>
      <tr>
        <td>专业：</td>
        <td>{{ StudentInfo.unit_code.split('>')[1] }} </td>
        <td>班级：</td>
        <td>{{ StudentInfo.unit_code.split('>')[2] }}</td>
      </tr>
      <tr>
        <td>住宿状态：</td>
        <td colspan="3">{{ StudentInfo.dor_status }} </td>
      </tr>
      <tr>
        <td>宿舍：</td>
        <td v-if="StudentInfo.dor_path!=null && StudentInfo.dor_path!=''" colspan="3">
          {{ StudentInfo.dor_bed_number }}
        </td>
        <td v-else colspan="3"></td>
      </tr>
      <tr>
        <td>班主任：</td>
        <td>{{ StudentInfo.class_teacher }} </td>
        <td>班主任电话：</td>
        <td>{{ StudentInfo.teacher_phone }}</td>
      </tr>
      <tr>
        <td>备注：</td>
        <td colspan="3">{{ StudentInfo.remark }}</td>
      </tr>
    </table>
    <table border="1" class="gridtable" style="width:100%;border: 1px solid #000; border-collapse: collapse;margin-top:10px;">
      <tr>
        <th>收费项目</th>
        <th>应收(元)</th>
        <th>已交(元)</th>
      </tr>
      <tr v-for="item in ChargeItems">
        <td>{{ item.ItemName }}</td>
        <td>{{ item.ReceivableMoney }}</td>
        <td>{{ item.PayMoney }}</td>
      </tr>
      <tr>
        <td align="right">合计：</td>
        <td colspan="2">{{ ReceivableMoneySum.toFixed(2) }}</td>
      </tr>
      <tr>
        <td align="right">已缴费：</td>
        <td colspan="2">{{ PayMoneySum.toFixed(2) }}</td>
      </tr>
    </table>
    <div style="text-align:right;margin-top:15px;width:100%;">
      宿管员签字（盖章）：___________现场收款人签字：___________
    </div>
    <div style="text-align:right;margin-top:10px;width:100%;overflow: hidden;">
      报名时间：<span id="bmshijian" class="bmshijian">{{ StudentInfo.create_time }}</span>
    </div>
  </div>
</template>

<script>
import VueBarcode from '@xkeshi/vue-barcode'
export default {
  name: 'Tongzhidan',
  components: {
    VueBarcode
  },
  props: {
    ContentId: {
      type: String,
      default: 'PrintTzs'
    },
    StudentInfo: {
      type: Object,
      default: {}
    },
    ChargeItems: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      SchoolName: '',
      barcode_option: {
        displayValue: true, // 是否默认显示条形码数据
        textPosition: 'bottom', // 条形码数据显示的位置
        background: '#fff', // 条形码背景颜色
        valid: function(valid) {
          console.log(valid)
        },
        width: '2px', height: '30px', fontSize: '18px' // 字体大小
      }
    }
  },
  computed: {
    ReceivableMoneySum: function() {
      var sumMoney = 0
      this.ChargeItems.forEach(item => {
        sumMoney += parseFloat(item.ReceivableMoney)
      })
      return sumMoney
    },
    PayMoneySum: function() {
      var sumMoney = 0
      this.ChargeItems.forEach(item => {
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
    this.SchoolName = this.$store.getters.web_site_name
  }
}
</script>
