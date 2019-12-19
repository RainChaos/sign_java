<template>
  <div class="main-page" style="">
    <x-header style="background-color:#0068b7;position:relative;">
      入学须知
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>
    <div class="">
      <el-row style="height:100%;background-color: #FFF;">
          <div  v-html="topInfoContent">
            &nbsp;{{topInfoContent}}
          </div>
      </el-row>
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
  import { getArticleByTitle } from '@/api/article'
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
  data () {
    return {
      topInfoContent:''
    }
  },
  computed: {

  },
  mounted(){
    getArticleByTitle({ title: '2019年春季学期新生入学须知' }).then(response => {
      this.topInfoContent = response.data.content
    }).catch(error => {
      console.log(error)
    })
  },
  methods:{

  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style >
  .sum-money{
    font-size:12px;
    font-weight: bolder;
    color:#666;
    background-color:#ececec;
    border:1px solid #409EFF;
    padding:5px;
    border-radius: 3px;
  }
  img{
    width: 100%;
  }
  .h-money{
    color:#dd0000;
    font-size:16px;
  }
</style>
