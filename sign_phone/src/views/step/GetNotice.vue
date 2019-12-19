<template>
  <div class="main-page">
    <x-header style="background-color:#0068b7;">
      获取通知书
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>

      <div class="form-content" >
        <el-row style="background-color: #FFF;" v-if="mainShow">
          <el-col :span="24" style="padding-top:0px;">
            <el-form ref="SearchStudentForm"
                     :show-message="false"
                     :rules="searchRules"
                     :model="search"
                     size="small"
                     label-position="left">
              <el-form-item  label="姓名：" prop="search_name">
                <el-input v-model="search.search_name" placeholder="请输入学生姓名" style="width:100%" auto-complete="off"/>
              </el-form-item>
              <el-form-item  label="身份证号码：" prop="search_idcode">
                <el-input v-model="search.search_idcode"  placeholder="请输入学生身份证号码"  style="width:100%" auto-complete="off"/>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col :span="24" style="margin-top:20px;">
            <x-button type="primary" @click.native="getNotice">获取通知书</x-button>
          </el-col>

          <el-col :span="24" style="margin-top:20px;">
            <x-button type="primary" @click.native="getSnoBarcode">获取学号条形码</x-button>
          </el-col>

          <el-col :span="24" style="margin-top:20px; text-align: center" v-if="sno != null && sno != ''">
          <vue-barcode :value="sno" :options="barcode_option" tag="canvas" @click.native="BarcodeClick"/>
          </el-col>

        </el-row>

        <el-row style="text-align: center; " v-if="codeShow">
          <el-col :span="24" style="margin-top:160px; text-align: center">
          <vue-barcode class="rotate" :value="sno" :options="barcode_option2" tag="canvas" @click.native="BarcodeClick2"/>
          </el-col>
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
  import { getElectronicNotice } from '@/api/electronicNotice';
  import VueBarcode from '@xkeshi/vue-barcode';
  export default {
    components: {
      XHeader,
      XInput,
      Selector,
      PopupPicker,
      Group,
      Flexbox,
      FlexboxItem,
      XButton,
        VueBarcode
    },
    name: 'SearchStudent',
    data () {
      return {
          mainShow: true,
          codeShow: false,
          sno: '',
          barcode_option: {
              displayValue: true, // 是否默认显示条形码数据
              textPosition: 'bottom', // 条形码数据显示的位置
              background: '#fff', // 条形码背景颜色
              valid: function(valid) {
                  console.log(valid)
              },
              width: '2px', height: '30px', fontSize: '14px', // 字体大小
              format: 'CODE128'
          },
          barcode_option2: {
              displayValue: true, // 是否默认显示条形码数据
              textPosition: 'bottom', // 条形码数据显示的位置
              background: '#fff', // 条形码背景颜色
              valid: function(valid) {
                  console.log(valid)
              },
              width: '3px', height: '80px', fontSize: '14px', // 字体大小
              format: 'CODE128'
          },
        search: {
          search_name: '黄娟',
          search_idcode: '450923200302125962'
        },
        formSearchLoading: false,
        searchRules: {
          search_name: [{
            required: true,
            message: '学生姓名不能为空',
            trigger: 'blur'
          }],
          search_idcode: [
            { required: true, message: '学生身份证号码不能为空', trigger: 'blur' },
            { validator: formValidater.checkdIdCard, trigger: 'blur' }
          ]
        }
      }
    },
    mounted(){
      let loadingInstance = Loading.service({});
      this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
        loadingInstance.close();
      });
    },
    methods: {
        BarcodeClick(){
          this.mainShow = false
            this.codeShow = true
        },
        BarcodeClick2(){
            this.mainShow = true
            this.codeShow = false
        },
        getSnoBarcode(){
            this.formSearchLoading = true
            var _this =  this
            this.$refs.SearchStudentForm.validate((valid,errors) => {
                if (valid) {
                    var data = JSON.parse(JSON.stringify(this.search))
                    // console.log(data);
                    searchStudentInfo({ name: data.search_name, idcode: data.search_idcode }).then(response => {
                        var student_id = response.data.id
                        var name = response.data.name
                        var sno = response.data.sno
                        var message = '查询到学生【姓名：' + name + ',学号：' + sno + '】'

                        this.$vux.alert.show({
                            title: '成功提示',
                            content: message,
                            onHide () {
                                _this.sno = sno
                            }
                        })
                        this.formSearchLoading = false
                    }).catch(error => {
                        this.formSearchLoading = false
                    })
                } else {
                    var errorHtml = []
                    for (var k in errors) {
                        errorHtml.push(errors[k][0].message)
                    }
                    this.$vux.alert.show({
                        title: '错误提示',
                        content: errorHtml.join('<br />')
                    })
                    this.formSearchLoading = false
                }
            })
        },
        getNotice() {
        this.formSearchLoading = true
        var _this =  this
        this.$refs.SearchStudentForm.validate((valid,errors) => {
          if (valid) {
            var data = JSON.parse(JSON.stringify(this.search))
              // console.log(data);
            // searchStudentInfo({ name: data.search_name, idcode: data.search_idcode }).then(response => {
            //   var student_id = response.data.id
            //   var name = response.data.name
            //   var sno = response.data.sno
            //   var message = '查询到学生【姓名：' + name + ',学号：' + sno + '】'
            //
            //   this.$vux.alert.show({
            //     title: '成功提示',
            //     content: message,
            //     onHide () {
            //       // _this.$router.push({ path: '/edit', query: { student_id: student_id }})
            //
            //     }
            //   })
            //   this.formSearchLoading = false
            // }).catch(error => {
            //   this.formSearchLoading = false
            // })
              getElectronicNotice({ name: data.search_name, idcode: data.search_idcode }).then(reponse=>{

                  window.location.href = process.env.BASE_API + '/notice/' + reponse.data
                  this.formSearchLoading = false
              }).catch(error=>{
                  console.log( error )
                  this.formSearchLoading = false
              })
          } else {
            var errorHtml = []
            for (var k in errors) {
              errorHtml.push(errors[k][0].message)
            }
            this.$vux.alert.show({
              title: '错误提示',
              content: errorHtml.join('<br />')
            })
            this.formSearchLoading = false
          }
        })
      },

      getElectronicNotice(sno) {
          // alert(this.studentInfo.sno);

      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  .rotate{
    transform:rotate(90deg);
    -ms-transform:rotate(90deg); /* Internet Explorer */
    -moz-transform:rotate(90deg); /* Firefox */
    -webkit-transform:rotate(90deg); /* Safari 和 Chrome */
    -o-transform:rotate(90deg); /* Opera */
  }
</style>
