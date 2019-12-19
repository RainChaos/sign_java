<template>
  <div class="">
    <x-header style="background-color:#0068b7; position:relative; top: 0px;">
      入学须知
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>

      <div class="">
        <el-row style="height:100%;background-color: #FFF;">
          <!--<el-col :span="24" style="padding-top:0px;">
            <img src="../../../static/admission.jpg" width="100%" alt="">
          </el-col>
          <el-col :span="24" style="padding-top:0px;">
            <img src="../../../static/charge.png" width="100%" alt="">
          </el-col>-->
          <div  v-html="topInfoContent">
            &nbsp;{{topInfoContent}}
          </div>
        </el-row>

      </div>
  </div>
</template>

<script>
  import { XHeader,XInput,Selector,PopupPicker,Group,Flexbox, FlexboxItem,XButton } from 'vux'
  import { searchStudentInfo } from '@/api/student'
  import { formValidater } from '@/utils/formValidate'
  import { getArticleByTitle } from '@/api/article'
  import { Loading } from 'element-ui';
  import { getElectronicNotice } from '@/api/electronicNotice'
  export default {
    components: {
      XHeader,
      XInput,
      Selector,
      PopupPicker,
      Group,
      Flexbox,
      FlexboxItem,
      XButton
    },
    name: 'SearchStudent',
    data () {
      return {
        topInfoContent:''
      }
    },
    mounted(){
      getArticleByTitle({ title: '2019年春季学期新生入学须知' }).then(response => {
        this.topInfoContent = response.data.content
      }).catch(error => {
        console.log(error)
      })
    },
    methods: {

    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style >
  img{
    width: 100%;
  }
</style>
