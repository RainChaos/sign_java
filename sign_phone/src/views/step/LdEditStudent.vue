<template>
  <div class="main-page">
    <x-header style="background-color:#0068b7;">
      2-1. 申请劳动预备制
      <router-link slot="right" :to="{path:'/?openid=' + $route.query.openid}">退出</router-link>
    </x-header>
    <div style="height: calc(100% - 50px);overflow: auto">
      <div class="form-content" style="height: auto">
        <div class="weixtishi"><i class="el-icon-warning"/> 学生基础信息</div>
        <el-form
            label-position="left"
            inline size="mini"
            class="student-info">
          <el-row>
            <el-col :span="24">
              <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="院系：">
                <span>{{ studentInfo.unit_code_yuanxi }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="24">
              <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="专业：">
                <span>{{ studentInfo.unit_code_zhuanye }}</span>
              </el-form-item>
            </el-col>
          </el-row>

          <el-row style="border-top:1px solid #ddd;">
            <el-col :span="12">
              <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="姓名：">
                <span>{{ studentInfo.name }}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item :label-width="InfoLabelWidth" label="学号：" class="show-student-info">
                <span>{{ studentInfo.sno }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="性别：">
                <span>{{ studentInfo.sex=="1"?"男":"女"}}</span>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="学历：">
                <span>{{ studentInfo.education }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row>
            <el-col :span="12">
              <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="类型：">
                <span>{{ studentInfo.student_type }}</span>
              </el-form-item>
            </el-col>
          </el-row>
          <el-row style="padding:5px;border-top:1px solid #ddd;background: #cf9236;margin:0px -10px;color:#fff;" v-if="studentInfo.is_fuhe_ldybz==1">
            <el-col :span="24" style="text-align: center;font-size:13px;">
              申请状态：
              <span v-if="studentInfo.ldybz_check=='未申请'">未提交申请</span>
              <span v-if="studentInfo.ldybz_check=='已提交，未审核'">已提交申请,请等待老师进行审核</span>
              <span v-if="studentInfo.ldybz_check=='审核通过'">审核通过</span>
              <span v-if="studentInfo.ldybz_check=='审核不通过'">审核未通过,原因：{{studentInfo.ldybz_remark}}</span>
            </el-col>
          </el-row>
        </el-form>
        <div class="weixtishi"><i class="el-icon-warning"/> 提示：带红色星号(*)为必填项</div>
        <el-form
            ref="EditStudentInfoForm"
            :show-message="false"
            :inline-message="false"
            :status-icon="true"
            :model="formData"
            :rules="rules"
            label-position="left"
            size="small">
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
              <el-input @blur.prevent="inputLoseFocus" v-model="item.value" :disabled="item.is_disabed==1" style="width:100%" :placeholder="'请填写'+item.describe" type="textarea"/>
            </el-form-item>
            <el-form-item v-if="item.data_type=='省市区'" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader
                      v-if="item.event_name&&item.event_name.length>0"
                      v-model="item.value"
                      :disabled="item.is_disabed==1"
                      :options="regionOptions"
                      :props="regionProps"
                      style="width:100%"
                      :placeholder="'请选择'+item.describe"
                      @active-item-change="handleItemChange"
                      @change="item.event_name"/>
              <el-cascader
                      v-else-if="item.event_name.length==0"
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
              <el-select v-model="item.value" :disabled="item.is_disabed==1" style="width:100%" :placeholder="'请选择'+item.describe">
                <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value"/>
              </el-select>
            </el-form-item>
            <el-form-item v-if="item.data_type=='单位结构'"  :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <!--<el-cascader-->
                      <!--v-model="item.value"-->
                      <!--:disabled="item.is_disabed==1"-->
                      <!--:options="unitOptions"-->
                      <!--:props="unitProps"-->
                      <!--:placeholder="'请选择'+item.describe"-->
                      <!--style="width:100%"-->
                      <!--filterable/>-->
              <el-input disabled v-model="item.value"  :placeholder="'请填写'+item.describe" style="width:100%" type="textarea"/>
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
                  :readonly="studentInfo.ldybz_check!=='未申请' && studentInfo.ldybz_check!=='审核通过'"
                  :auto-upload="uploadImage.autoUpload"
                  :show-tip="uploadImage.showTip"
                  :params="{describe:item.describe, field_name:item.field_name}"
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
      <div style="margin-top:0px;padding:10px;text-align: center">
        <!--<x-button type="primary" @click.native="handleSaveStudent">-->
          <!--提交申请-->
        <!--</x-button>-->
        <x-button v-if="studentInfo.ldybz_check=='未申请'" type="primary" @click.native="handleSaveStudent">
          提交申请
        </x-button>
        <x-button  type="default" @click.native="handleFanhui">
          返回
        </x-button>
      </div>
      <el-row style="margin:10px;padding:5px;border-top:1px solid #ddd;background: #cf9236;color:#fff;text-align: center;font-size:13px">
        申请状态：
        <span v-if="studentInfo.ldybz_check=='未申请'">未提交申请</span>
        <span v-if="studentInfo.ldybz_check=='已提交，未审核'">已提交申请,请等待老师进行审核</span>
        <span v-if="studentInfo.ldybz_check=='审核通过'">审核通过</span>
        <span v-if="studentInfo.ldybz_check=='审核不通过'">审核未通过,原因：{{studentInfo.ldybz_remark}}</span>
      </el-row>
    </div>
    <div v-transfer-dom>
      <x-dialog v-model="dialogShowImageVisible" class="dialog-demo">
        <div class="img-box"><img :src="ShowImage.url" style="max-width:100%"></div>
        <div style="padding:10px">
          <x-button type="primary" @click.native="dialogShowImageVisible=false">关 闭</x-button>
        </div>
      </x-dialog>
    </div>
  </div>
</template>

<script>
  import { XHeader,XInput,Selector,PopupPicker,Group,Flexbox, FlexboxItem,XButton } from 'vux'
  import Uploader from 'vux-uploader'
  import { XDialog , TransferDomDirective as TransferDom } from 'vux'
  import { getRegionLists } from '@/api/region'
  import { getSelectUnitLists } from '@/api/selectUnit'
  import { editStudentInfoForLDYBZ, getStudentInfo, getEditFormConfigLists } from '@/api/student'
  import { formValidater } from '@/utils/formValidate'
  import { getAllMiddleSchoolLists } from '@/api/middleSchool'
  import { getArticleByTitle } from '@/api/article'
  import { deleteFile } from '@/api/file'
  import { Loading } from 'element-ui';
  export default {
    directives: {
      TransferDom
    },
    components: {
      XHeader,
      XInput,
      Selector,
      PopupPicker,
      Group,
      Flexbox,
      FlexboxItem,
      XButton,
      XDialog,
      Uploader
    },
    data() {
      return {
        fileName:'photo',
        isSaveStudentInfo: true,
        InfoLabelWidth:'55px',
        labelWidth: '100px',
        studentInfo: {},
        formSumbitLoading: false,
        formDataLists: [],
        middleSchoolLists: [],
        rules: { },
        regionOptions: [],
        regionProps: {
          value: 'code',
          label: 'fullName',
          children: 'children'
        },
        uploadUrl:process.env.BASE_API + '/index/student/phoneUploadImage',
        uploadImage: {
          max: 1,
          handleClick: false,
          showHeader: true,
          readonly: false,
          autoUpload: true,
          showTip: false
        },

        unitOptions: [],
        unitProps: {
          value: 'id',
          label: 'unit_name',
          children: 'children'
        },
        ShowImage: {
          title: '',
          url: ''
        },
        dialogShowImageVisible: false
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
    mounted() {
      var student_id = window.localStorage.getItem('student_id')
      this.fetchStudentInfo(student_id)
      this.fetchUnitLists()
      this.fetchShengFenLists()
      this.fetchFormFieldLists(student_id)
    },
    methods: {
      previewMethod(data){
        this.ShowImage.url = data.url
        this.dialogShowImageVisible = true
      },
      addImageMethod(){},
      removeImageMethod(data){
        var url = data.url
        deleteFile({ url: url }).then(response => {
          // console.log(response.data)
        })
      },
      fetchStudentInfo(student_id) {
        getStudentInfo({ student_id: student_id }).then(response => {
          this.studentInfo = response.data
        }).catch(error => {
          console.log(error)
        })
      },
      handleNext() {
        var student_id = window.localStorage.getItem('student_id')
        this.$router.push({ path: '/pay', query: { student_id: student_id,openid: this.$route.query.openid }})
      },
      fetchFormFieldLists(student_id) {
        var pici_id = this.$route.query.pici_id
        this.formSumbitLoading = true
        const loadingInstance = Loading.service({});
        getEditFormConfigLists({ form_name: '申请劳动预备信息表单', student_id: student_id,pici_id:pici_id }).then(response => {
          this.formDataLists = response.data
            console.log(this.formDataLists)
          var rules = {}

          this.formDataLists.forEach((item, index) => {
              if( item.field_name === 'zsr') {
                  item.is_disabed = 1
              }

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
                      } else {
                          this.formDataLists[index].value = []
                      }
                  }
                  this.fetchRegionLists(item.value)
              }
              // if (item.data_type === '单位结构') {
              //     if (typeof value === 'string') {
              //         if (value.length > 0) {
              //             this.formDataLists[index].value = JSON.parse(value)
              //         } else {
              //             this.formDataLists[index].value = []
              //         }
              //     }
              // }
            if (item.data_type === '图片') {
                console.log(item)
              if (this.formDataLists[index].file.length>0){
                this.formDataLists[index].file[0].url=process.env.BASE_API+this.formDataLists[index].file[0].url
              }
            }
          })
          this.rules = rules
          this.formSumbitLoading = false
          this.$nextTick(() => { // 以服务的方式调用的 Loading 需要异步关闭
            loadingInstance.close();
          });
        }).catch(error => {
          console.log(error)
            // loadingInstance.close();
        })
      },
      fetchUnitLists() {
        getSelectUnitLists().then(response => {
          var UnitLists = JSON.parse(JSON.stringify(response.data))
          this.unitOptions = UnitLists
        })
      },
      setMiddleSchool(region) {
        var region_code = region[region.length - 1]
        getAllMiddleSchoolLists({ regionCode: region_code }).then(response => {
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
      handleFanhui(){
        var _this = this
        var student_id = _this.$route.query.student_id
        _this.$router.push({ path: '/edit', query: { student_id: student_id , openid: _this.$route.query.openid}})
      },
      handleSaveStudent() {
        var _this = this
        this.$refs.EditStudentInfoForm.validate((valid, errors) => {
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
            formdata["ldybz_shenqing"] = '是'
            formdata["ldybz_check"] = '已提交，未审核'
            for(var o in formdata ){
              if(formdata[o] == '' || formdata[o] == null){
                this.$vux.alert.show({
                  title: '错误提示',
                  content: '请先完善所有信息再提交'
                })
                return false;
              }
            }
            editStudentInfoForLDYBZ(formdata).then(response => {
              this.$vux.alert.show({
                title: '温馨提示',
                content: '保存信息成功',
                onHide(){
                  _this.fetchStudentInfo(formdata['id'])
                }
              })
              this.formSumbitLoading = false
              this.isSaveStudentInfo = false
            }).catch(error => {
              this.formSumbitLoading = false
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
            this.formSumbitLoading = false
          }
        })
      }
    }
  }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
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
  }
</style>
