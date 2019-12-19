<template>
  <div class="main-page" style="height:100%;overflow: auto">
    <x-header style="background-color:#0068b7;">
      在线咨询
      <!--<router-link slot="right" :to="{path:'/'}">返回</router-link>-->
    </x-header>

    <div class="contact" v-for="item in qqList">
      <div>
        <el-row>
          <el-col :span="10">
            <a v-bind:href="['http://wpa.qq.com/msgrd?v=3&uin='+item.qq+ '&site=qq&menu=yes']" target='_blank'>
                  <el-button type="plain" size="mini">
                    <img src='../../../static/tabbar/qq.png' border='0'  style="width:60px; height: 60px;"/><p>咨询</p>
                  </el-button>
            </a>
          </el-col>
          {{item.teacher}}
          <br>
          {{item.remark}}
        </el-row>
      </div>
    </div>

  </div>
</template>

<script>
  import { XHeader,ViewBox,Flexbox, FlexboxItem,XButton, XDialog,TransferDomDirective as TransferDom } from 'vux'
  import { Loading } from 'element-ui';
  import { getQQ } from '@/api/student'
export default {

  components: {
    ViewBox,
    Flexbox,
    FlexboxItem,
    XButton,
    XDialog,
      XHeader
  },
  name: 'ContactOnline',
  data () {
    return {
      qqList: [

      ]
    }
  },
  mounted(){
    this.getQQ()
  },
  methods:{
    getQQ(){
        getQQ().then(response => {
            console.log(response)
            this.qqList = response.data
        }).catch(error => {
            this.formSumbitLoading = false
        })
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .main-page{
    background-image:url("../../../static/bg.png");
  }
  .top{
    padding:80px 15px 15px 15px;
  }
  .top-info-ct{
    border-radius: 5px;
    min-height:80px;
    padding:10px;
    font-size:11px;
    background-color: #fff;
    font-weight: 500;
  }
  .content{
    padding:10px 20px;
    overflow: hidden;
  }
  .span-text{
    font-size:16px;
    color:#666;
    padding-left:20px;
    margin-top:10px;
  }
  .typeItem{
    cursor:pointer;
    color:#257be8;
    padding:5px;
    text-align: center;
    border:1px solid #257be8;
  }
  .selectedItem{
    background:#257be8;
    color:#FFF;
  }
  .contact{
    padding: 10px;
    margin: 15px;
    height: 120px;
    border-radius: 10px;
    background: rgba(255,255,255,0.99);

  }
  a{
    text-decoration: none;
    color: #E6A23C;
  }
</style>
