<template>
  <div class="main-page" style="">
    <x-header  style="background-color:#0068b7; z-index:999999">
      1. 基本信息填写
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>
    <el-row id="stepN" style="background: #fbf9fe;margin:0px 10px 5px 10px;padding:5px;z-index:999999">
      <step-nar :step-number="stepNumber"></step-nar>
    </el-row>

    <!--<scroller lock-x scrollbar-y-->
              <!--height="100%"-->


              <!--ref="scroller"-->
              <!--style="font-size:13px;color:#fff;"-->
              <!--&gt;-->
      <!--<div style="overflow: auto;background:#fff;margin:0px 5px;padding-bottom: 50px">-->


    <div style="height: calc(100% - 100px);overflow: scroll;touch-action: pan-y;-webkit-overflow-scrolling: touch;">
      <div class="form-content" style="height: auto">
        <div class="weixtishi"><i class="el-icon-warning"/> 提示：带红色星号(*)为必填项</div>

        <el-form
            ref="AddStudentInfoForm"
            :show-message="false"
            :inline-message="false"
            :status-icon="true"
            :model="formData"
            :rules="rules"
            label-position="left">

          <el-form-item label="批次：" style="margin-bottom: 0px;width: 100%;">
            <el-select v-model="piciValue" disabled  placeholder="请选择季节批次" required  style="width:100%">
              <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
          </el-form-item>
          <template v-for="(item) in formDataLists">
            <el-form-item
                v-if="item.data_type=='字符串'"
                :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input @blur.prevent="inputLoseFocus" v-model="item.value" :disabled="item.is_disabed==1" :placeholder="'请填写'+item.describe" style="width:100%"/>
            </el-form-item>
            <el-form-item
                v-if="item.data_type=='文本'"
                :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input @blur.prevent="inputLoseFocus" v-model="item.value" style="width:100%" :placeholder="'请填写'+item.describe" type="textarea"/>
            </el-form-item>
            <el-form-item v-if="item.data_type=='省市区'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader
                  v-if="item.event_name !=null &&item.event_name.length>0"
                  v-model="item.value"
                  :disabled="item.is_disabed==1"
                  :options="regionOptions"
                  :props="regionProps"
                  style="width:100%"
                  :placeholder="'请选择'+item.describe"
                  @active-item-change="handleItemChange"
                  @change="item.event_name"
                  @click="this.blur()"/>
              <el-cascader
                  v-else-if="item.event_name==null||item.event_name.length==0"
                  v-model="item.value"
                  :disabled="item.is_disabed==1"
                  :options="regionOptions"
                  :props="regionProps"
                  style="width:100%"
                  :placeholder="'请选择'+item.describe"
                  @active-item-change="handleItemChange"/>
            </el-form-item>
            <el-form-item v-if="item.data_type=='标准数据'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-select v-model="item.value" :disabled="item.is_disabed==1" style="width:100%" :placeholder="'请选择'+item.describe" >
                <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value" />
              </el-select>
            </el-form-item>
            <el-form-item v-if="item.data_type=='单位结构'"  :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader
                  v-model="item.value"
                  :disabled="item.is_disabed==1"
                  :options="unitOptions"
                  :props="unitProps"
                  :placeholder="'请选择'+item.describe"
                  style="width:100%"/>
            </el-form-item>

            <div v-if="item.data_type=='毕业中学'">

              <el-form-item>
                <label slot="label">{{ item.describe }}:</label>
                <el-select
                        v-model="item.value"
                        :placeholder="'请选择'+item.describe"
                        style="width:100%"
                        allow-create>
                  <el-option v-for="middle in middleSchoolLists" :key="middle.schoolName" :label="middle.schoolName" :value="middle.schoolName"/>
                </el-select>
              </el-form-item>
              <el-form-item   :prop="item.field_name">
                <el-input @blur.prevent="inputLoseFocus" v-model="item.value" :disabled="item.is_disabed==1" style="width:100%" placeholder="手动填写毕业中学" />
              </el-form-item>

            </div>

            <el-form-item v-if="item.data_type=='图片'" :prop="item.field_name" style="margin-top:10px;">
              <uploader
                  :images="item.file"
                  :name="fileName"
                  :max="uploadImage.max"
                  :upload-url="uploadUrl"
                  :handle-click="uploadImage.handleClick"
                  :show-header="uploadImage.showHeader"
                  :readonly="uploadImage.readonly"
                  :auto-upload="uploadImage.autoUpload"
                  :show-tip="uploadImage.showTip"
                  :params="{describe:item.describe,name:item.field_name}"
                  size="normal"
                  :title="item.describe"
                  @preview="previewMethod"
                  @add-image="addImageMethod"
                  @remove-image="removeImageMethod"
              ></uploader>
            </el-form-item>
          </template>
        </el-form>
      </div>
      <div style="margin-top:0px;padding:10px;">
        <x-button type="primary" @click.native="handleSaveStudent">提 交</x-button>
        <x-button type="default" @click.native="handleSearchSttem.file" >预缴费用</x-button>

      </div>
    </div>
    <div v-transfer-dom>
      <x-dialog v-model="dialogShowImageVisible" class="dialog-demo">
        <div class="img-box">
          <img :src="ShowImage.url" style="max-width:100%">
        </div>
        <div style="padding:10px">
          <x-button type="primary" @click.native="dialogShowImageVisible=false">关 闭</x-button>
        </div>
      </x-dialog>
    </div>
      <!--</div>-->
    <!--</scroller>-->
  </div>
</template>

<script>
  import { XHeader,XInput,Selector,PopupPicker,Group,Flexbox, FlexboxItem,XButton,Scroller } from 'vux'
  import Uploader from 'vux-uploader'
  import { XDialog , TransferDomDirective as TransferDom } from 'vux'
  import { getPiciLists } from '@/api/studentPici'
  import { getRegionLists } from '@/api/region'
  import { getSelectUnitLists } from '@/api/selectUnit'
  import { addStudentInfo, searchStudentInfo, getFormConfigLists } from '@/api/student'
  import { formValidater } from '@/utils/formValidate'
  import { getAllMiddleSchoolLists } from '@/api/middleSchool'
  import { getArticleByTitle } from '@/api/article'
  import { deleteFile } from '@/api/file'
  import { Loading } from 'element-ui';
  import StepNar from './components/StepNar'
  import FastClick from 'fastclick'
export default {
  directives: {
    TransferDom
  },
  components: {
      Scroller,
    XHeader,
    XInput,
    Selector,
    PopupPicker,
    Group,
    Flexbox,
    FlexboxItem,
    XButton,
    XDialog,
    StepNar,
    Uploader
  },
  name: 'AddStudent',
  data () {
    return {
      fileName:'photo',
      stepNumber: 0,
      piciValue: 3,
      education:"初中生",
      piciLists: [],
      topInfoContent: '',
      formSumbitLoading: false,
      uploadImageUrl: process.env.BASE_API + '/index/file/uploadImage',
      formDataLists: [],
      middleSchoolLists: [],
      rules: { },
      regionOptions: [],
      regionProps: {
        value: 'code',
        label: 'fullName',
        children: 'children'
      },
      unitOptions: [],
      unitProps: {
        value: 'unit_code',
        label: 'unit_name',
        children: 'children'
      },
      ShowImage: {
        title: '',
        url: ''
      },
      dialogShowImageVisible: false,

      uploadUrl:process.env.BASE_API + '/index/student/phoneUploadImage',
      uploadImage: {
        max: 1,
        handleClick: false,
        showHeader: true,
        readonly: false,
        autoUpload: true,
        showTip: false
      }
    }
  },
  computed: {
    formData: function() {
      var formData = {}
      this.formDataLists.forEach((item) => {
        if(item.data_type=="图片" && item.file.length > 0){
          var url=item.file[0].url
          formData[item.field_name] = url.substring(url.indexOf("Upload")-1)
          // console.log(item.field_name+"----"+ url.substring(url.indexOf("Upload")-1))

        }else{
          formData[item.field_name] = item.value
          // console.log(item.field_name+"--"+item.value)
        }
      })
      return formData
    }
  },
  created() {
    FastClick.attach(document.body)
      window.addEventListener("touchmove",this.touchmove)
    this.piciValue = parseInt(this.$route.query.pici)
    this.education = this.$route.query.type
    this.fetchPiciLists()
    this.fetchUnitLists(this.piciValue,this.education)
    this.fetchShengFenLists()
    this.fetchFormFieldLists()

  },
  methods: {

      touchmove(evt){
          var inputs = document.getElementsByTagName('input');
          for(var i = 0; i < inputs.length; i++){
              inputs[i].blur()
          }
      },
      // 解决调起手机软键盘页面被顶到底部再关闭软键盘页面底部留白的问题
      inputLoseFocus() {
          setTimeout(() => {
              window.scrollTo(0, 0);
          }, 0);
      },
    previewMethod(data){
      this.ShowImage.url = data.url
      this.dialogShowImageVisible = true
    },
    addImageMethod(){
    },
    removeImageMethod(data){
      var url = data.url
      // alert("22222222222222")
      deleteFile({ url: url }).then(response => {
        // console.log(response.data)
      })
    },
    fetchPiciLists(){
      getPiciLists().then(reponse=>{
        this.piciLists = reponse.data.data
      }).catch(error=>{
        console.log( error )
      })
    },
    fetchFormFieldLists() {
      const loadingInstance = Loading.service({});
      getFormConfigLists({ form_name: '手机学生报名信息表单' ,pici_id:this.piciValue}).then(response => {
        this.formDataLists = response.data
        var rules = {}
        this.formDataLists.forEach((item, index) => {
          // 判断验证规则
          if (item.rules !== '' && item.rules !== null) {
            var ruleList = JSON.parse(item.rules)
            for (let i = 0; i < ruleList.length; i++) {
              if (ruleList[i].validator !== undefined) {
                if (ruleList[i].validator === 'formValidater.checkdIdCard') { ruleList[i].validator = formValidater.checkdIdCard }
                if (ruleList[i].validator === 'formValidater.phoneNumber') { ruleList[i].validator = formValidater.phoneNumber }
                if (ruleList[i].validator === 'formValidater.telephoneNumber') { ruleList[i].validator = formValidater.telephoneNumber }
              }
            }
            rules[item.field_name] = ruleList
          }
          var value = item.value
          var event_name = item.event_name

          if (event_name === 'setMiddleSchool') {
            this.formDataLists[index].event_name = this.setMiddleSchool
          } else {
            this.formDataLists[index].event_name = ''
          }
          // 判断是否存在事件
          if (item.data_type === '省市区') {
            if (typeof value === 'string') {
              if (value.length > 0) {
                this.formDataLists[index].value = JSON.parse(value)
                this.fetchRegionLists(item.value)
              } else {
                this.formDataLists[index].value = []
              }
            }
          }
          if (item.data_type === '单位结构') {
            if (typeof value === 'string') {
              if (value.length > 0) {
                this.formDataLists[index].value = JSON.parse(value)
              } else {
                this.formDataLists[index].value = []
              }
            }
          }
          if(item.field_name=="education"){
            this.formDataLists[index].value = this.$route.query.type
          }
        })
        this.rules = rules
        // console.log(rules)
        this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
          loadingInstance.close();
        });
      })
    },
    fetchUnitLists(pici_id,education) {
      getSelectUnitLists({ pici_id:pici_id,education:education }).then(response => {
        var UnitLists = JSON.parse(JSON.stringify(response.data))
        this.unitOptions = UnitLists
      })
    },
    setMiddleSchool(region) {
      var regionCode = region[region.length - 1]
      getAllMiddleSchoolLists({ regionCode: regionCode }).then(response => {
        this.middleSchoolLists = response.data
      }).catch(() => {})
    },
    fetchShengFenLists() {
      getRegionLists({ fatherCode: 100000 }).then(response => {
        var ShengFenLists = JSON.parse(JSON.stringify(response.data))
        ShengFenLists.forEach((item, index) => {
          ShengFenLists[index]['children'] = []
        })
        this.regionOptions = ShengFenLists
      })
    },
    fetchRegionLists(value) {
      var shengfen_code = (value !== null) ? value[0] : null
      var chengshi_code = (value !== null) ? value[1] : null
      var ShengFenIndex = 0
      var ChengShiIndex = 0
      // 获取省份信息
      this.regionOptions.forEach((item, index) => {
        if (this.regionOptions[index].code === shengfen_code) {
          ShengFenIndex = index
        }
      })
      // 获取城市信息
      if (value !== null && value.length > 0) {
        getRegionLists({ fatherCode: shengfen_code }).then(response => {
          var ChengShiLists = JSON.parse(JSON.stringify(response.data))
          ChengShiLists.forEach((item, index) => {
            if (ChengShiLists[index]['children'] === undefined) {
              ChengShiLists[index]['children'] = []
            }
            if (ChengShiLists[index].code === chengshi_code) {
              ChengShiIndex = index
            }
          })
          if (this.regionOptions[ShengFenIndex]['children'].length === 0) { this.regionOptions[ShengFenIndex]['children'] = ChengShiLists }
          // 获取地区信息
          if (this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'].length === 0) {
            getRegionLists({ fatherCode: chengshi_code }).then(response => {
              var DiquLists = JSON.parse(JSON.stringify(response.data))
              this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'] = DiquLists
            })
          }
        })
      }
    },
    handleItemChange(val) {
      var length = val.length
      var shengfen_code = val[0]
      var ShengFenIndex = 0
      var ChengShiIndex = 0
      var regionOptions = JSON.parse(JSON.stringify(this.regionOptions))
      // 点击省份
      if (length === 1) {
        for (let i = 0; i < regionOptions.length; i++) {
          if (regionOptions[i].code === shengfen_code) {
            ShengFenIndex = i
            break
          }
        }
        if (this.regionOptions[ShengFenIndex]['children'].length === 0) {
          getRegionLists({ fatherCode: shengfen_code }).then(response => {
            var regionLists = JSON.parse(JSON.stringify(response.data))
            for (let i = 0; i < regionLists.length; i++) {
              if (regionLists[i].type !== 3) {
                regionLists[i]['children'] = []
              }
            }
            this.regionOptions[ShengFenIndex]['children'] = regionLists
          })
        }
      }

      if (length === 2) {
        const chengshi_code = val[1]
        for (let i = 0; i < regionOptions.length; i++) {
          if (regionOptions[i].code === shengfen_code) {
            ShengFenIndex = i
            break
          }
        }
        for (let i = 0; i < regionOptions[ShengFenIndex]['children'].length; i++) {
          if (regionOptions[ShengFenIndex]['children'][i].code === chengshi_code) {
            ChengShiIndex = i
            break
          }
        }
        if (this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'].length === 0) {
          getRegionLists({ fatherCode: chengshi_code }).then(response => {
            var regionLists = JSON.parse(JSON.stringify(response.data))
            for (let i = 0; i < regionLists.length; i++) {
              if (regionLists[i].type !== 3) {
                regionLists[i]['children'] = []
              }
            }
            this.regionOptions[ShengFenIndex]['children'][ChengShiIndex]['children'] = regionLists
          })
        }
      }
    },
    handleRemove(file, fileList) {
      var data = JSON.parse(JSON.stringify(file))
      var url = data.url
      deleteFile({ url: url }).then(response => {
        // console.log(response.data)
      })
    },
    handlePreview(file) {
      var data = JSON.parse(JSON.stringify(file))
      this.ShowImage.title = data.name
      this.ShowImage.url = data.url
      this.dialogShowImageVisible = true
    },
    uploadImageSuccess(response, file, fileList) {
      fileList[0].url = response.data.url
      this.formDataLists.forEach((item, index) => {
        if (item.field_name === response.data.field_name) {
          this.formDataLists[index].value = response.data.value
          this.formDataLists[index].file = fileList
        }
      })
    },
    handleSaveStudent() {
      var _this = this
      this.$refs.AddStudentInfoForm.validate((valid,errors) => {
        if (valid) {
          var formdata = JSON.parse(JSON.stringify(this.formData))
          // 如果id是0则为新增用户，新增用户密码不能为空
          this.formSumbitLoading = true

          for (var k in formdata) {
            var value = formdata[k]
            if (typeof value === 'object') {
              if (value !== null && value.length > 0) { formdata[k] = value[value.length - 1] }
              if (value !== null && value.length === 0) { formdata[k] = null }
            }
          }
          formdata["pici_id"] = this.piciValue
          // console.log(JSON.stringify(formdata));

          addStudentInfo(formdata).then(response => {
            this.formSumbitLoading = false
            this.$vux.alert.show({
              title: '温馨提示',
              content: '信息提交成功',
              onHide () {
                window.localStorage.setItem('student_id', response.data.id);
                _this.$router.push({ path: '/edit', query: { pici_id: _this.piciValue, openid: _this.$route.query.openid }})
              }
            })
          })
        }else{
          var errorHtml = []
          for (var k in errors) {
            errorHtml.push(errors[k][0].message)
          }
          this.$vux.alert.show({
            title: '错误提示',
            content: errorHtml.join('<br />')
          })
          this.formSumbitLoading = false
        }
      })
    },
    handleSearchStudent(){
      this.$router.push({ path: '/search',query:{openid: this.$route.query.openid}})
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style lang="less" scoped>
  .form-content{
    min-height: calc(100% - 150px);
  }
  .dialog-demo {
    .weui-dialog{
      border-radius: 8px;
      padding-bottom: 8px;
    }
    .dialog-title {
      line-height: 30px;
      color: #666;
    }
    .img-box {
      height: 350px;
      overflow: hidden;
    }
    .vux-close {
      margin-top: 8px;
      margin-bottom: 8px;
    }
  }
  select:hover{
    display: none !important;
  }
  opetion:hover{
    display: inline-block !important;;
  }
</style>
