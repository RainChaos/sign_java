<template>
    <div class="main-page">
        <op-header :web-title="studentInfo.name+'学生信息'"></op-header>
        <div style="height: calc(100% - 50px);overflow: auto">
            <div class="form-content" style="height: auto">
                <div class="weixtishi"><i class="el-icon-warning"/> 学生基础信息</div>
                <el-form
                    label-position="left"
                    inline size="mini"
                    class="student-info">
                    <el-row>
                        <el-col :span="24">
                            <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="专业：">
                                <span>{{ studentInfo.unit_code_zhuanye }}</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="14">
                            <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="学号：">
                                <span>{{ studentInfo.sno }}</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="10">
                            <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="姓名：">
                                <span>{{ studentInfo.name }}</span>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="14">
                            <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="院系：">
                                <span>{{ studentInfo.unit_code_yuanxi }}</span>
                            </el-form-item>
                        </el-col>
                        <el-col :span="10">
                            <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="性别：">
                                <span>{{ studentInfo.sex=="1"?"男":"女"}}</span>
                            </el-form-item>
                        </el-col>
                    </el-row>

                    <el-row>
                        <el-col :span="14">
                            <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="交费：">
                                <i v-if="studentInfo.is_pay===0||studentInfo.is_pay==='否'||studentInfo.is_pay===false" class="el-icon-close" style="color:firebrick" />
                                <i v-if="studentInfo.is_pay===1||studentInfo.is_pay==='是'||studentInfo.is_pay===true" class="el-icon-check" style="color:forestgreen" />
                            </el-form-item>
                        </el-col>
                        <el-col :span="10">
                            <el-form-item class="studentInfo" :label-width="InfoLabelWidth" label="报到：">
                                <i v-if="studentInfo.is_register===0||studentInfo.is_register==='否'||studentInfo.is_register===false" class="el-icon-close" style="color:firebrick" />
                                <i v-if="studentInfo.is_register===1||studentInfo.is_register==='是'||studentInfo.is_register===true" class="el-icon-check" style="color:forestgreen" />
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
                <el-row class="sum-money" style="margin-top:10px">
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
                <div style="margin-top:0px;padding:10px;">
                    <el-button v-if="studentInfo.is_register == '是'" :disabled="Object.keys(studentInfo).length === 0" style="width:100%;margin-top:5px" type="info" @click.native="handleCancel">
                        <i class="fa fa-mail-reply-all " aria-hidden="true"/> 取消报到
                    </el-button>
                    <el-button v-if="studentInfo.is_register == '否'"
                               :disabled="Object.keys(studentInfo).length === 0"
                               style="width: 100%;margin-top:5px" type="primary" @click.native="handleSumbit">
                        <i class="el-icon-check" aria-hidden="true"/> 确认到校
                    </el-button>
                </div>
                <div class="weixtishi"><i class="el-icon-warning"/> 学生详细信息</div>
                <el-form
                    ref="EditStudentInfoForm"
                    :show-message="false"
                    :inline-message="false"
                    :status-icon="true"
                    :model="formData"
                    :rules="rules"
                    label-position="left"
                    size="mini">
                    <el-form-item label="批次：" style="margin-bottom: 0px;width: 100%;">
                        <el-select v-model="piciValue" disabled placeholder="请选择季节批次" style="width:100%">
                            <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
                        </el-select>
                    </el-form-item>
                    <template v-for="(item) in formDataLists">
                        <el-form-item
                            v-if="item.data_type=='字符串'"
                            :prop="item.field_name">
                            <label slot="label">{{ item.describe }}:</label>
                            <el-input v-model="item.value" :disabled="item.is_disabed==1" :placeholder="'请填写'+item.describe" style="width:100%"/>
                        </el-form-item>
                        <el-form-item
                            v-if="item.data_type=='文本'"
                            :prop="item.field_name">
                            <label slot="label">{{ item.describe }}:</label>
                            <el-input v-model="item.value" :disabled="item.is_disabed==1" style="width:100%" :placeholder="'请填写'+item.describe" type="textarea"/>
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
                            <el-cascader
                                v-model="item.value"
                                :disabled="item.is_disabed==1"
                                :options="unitOptions"
                                :props="unitProps"
                                :placeholder="'请选择'+item.describe"
                                style="width:100%"
                                filterable/>
                        </el-form-item>

                        <el-form-item v-if="item.data_type=='毕业中学'"  :prop="item.field_name">
                            <label slot="label">{{ item.describe }}:</label>
                            <el-select
                                v-model="item.value"
                                :placeholder="'请选择'+item.describe"
                                style="width:100%"
                                filterable
                                allow-create>
                                <el-option v-for="middle in middleSchoolLists" :key="middle.school_name" :label="middle.school_name" :value="middle.school_name"/>
                            </el-select>
                        </el-form-item>

                        <el-form-item v-if="item.data_type=='图片'" :prop="item.field_name" style="margin-top:10px;">
                            <label slot="label">{{ item.describe }}:</label>
                            <el-upload
                                :on-preview="handlePreview"
                                :on-remove="handleRemove"
                                :name="item.field_name"
                                v-model="item.value"
                                :file-list="item.file"
                                :data="{describe:item.describe,name:item.field_name}"
                                :action="uploadImageUrl"
                                :limit="item.limit"
                                :multiple="item.multiple"
                                :on-success="uploadImageSuccess"
                                style="width:100%"
                                list-type="picture">
                                <el-button size="small" type="primary">点击上传</el-button>
                                <div slot="tip" class="el-upload__tip label-right-ct"><i class="el-icon-warning"/> 只能上传jpg/png文件，且不超过500kb</div>
                            </el-upload>
                        </el-form-item>
                    </template>
                </el-form>
            </div>
            <div style="margin-top:0px;padding:10px;">
                <x-button type="primary" @click.native="handleSaveStudent">
                    保存信息
                </x-button>
            </div>
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
    import { XInput,Selector,PopupPicker,Group,Flexbox, FlexboxItem,XButton } from 'vux'
    import { XDialog , TransferDomDirective as TransferDom } from 'vux'
    import { getPiciLists } from '@/api/studentPici'
    import { getRegionLists } from '@/api/region'
    import { getSelectUnitLists } from '@/api/selectUnit'
    import { editStudentInfo, getStudentInfo, getEditFormConfigLists,setRegister,getStudentChargeItems } from '@/api/student'
    import { formValidater } from '@/utils/formValidate'
    import { getAllMiddleSchoolLists } from '@/api/middleSchool'
    import { getArticleByTitle } from '@/api/article'
    import { deleteFile } from '@/api/file'
    import { Loading } from 'element-ui';
    import  OpHeader  from './components/OpHeader'
    export default {
        directives: {
            TransferDom
        },
        components: {
            XInput,
            Selector,
            PopupPicker,
            Group,
            Flexbox,
            FlexboxItem,
            XButton,
            XDialog,
            OpHeader
        },
        data() {
            return {
                piciValue:null,
                piciLists:[],
                isSaveStudentInfo: true,
                InfoLabelWidth:'55px',
                labelWidth: '100px',
                studentInfo: {},
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
                    value: 'id',
                    label: 'unit_name',
                    children: 'children'
                },
                ShowImage: {
                    title: '',
                    url: ''
                },
                dialogShowImageVisible: false,
                chargeItems: []
            }
        },
        computed: {
            formData: function() {
                var formData = {}
                this.formDataLists.forEach((item) => {
                    formData[item.field_name] = item.value
                })
                return formData
            },
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
                return needPayMoney
            }
        },
        mounted() {
            var student_id = window.localStorage.getItem('student_id')
            this.fetchStudentInfo(student_id)
            this.fetchStudentChargeItems(student_id)
            this.fetchPiciLists()
            this.fetchUnitLists()
            this.fetchShengFenLists()
            this.fetchFormFieldLists(student_id)
            this.fetchStudentInfo(student_id)
        },
        methods: {
            fetchPiciLists(){
                getPiciLists().then(reponse=>{
                    this.piciLists = reponse.data.data
                    console.log(JSON.parse(JSON.stringify(this.piciLists)))
                }).catch(error=>{
                    console.log( error )
                })
            },
            fetchStudentInfo(student_id) {
                getStudentInfo({ student_id: student_id }).then(response => {
                    this.studentInfo = response.data
                    this.piciValue = response.data.pici_id
                }).catch(error => {
                    console.log(error)
                })
            },
            fetchStudentChargeItems(student_id) {
                getStudentChargeItems({ student_id: student_id }).then(response => {
                    this.chargeItems = response.data
                }).catch(error => {
                    console.log(error)
                })
            },
            fetchFormFieldLists(student_id) {
                this.formSumbitLoading = true
                const loadingInstance = Loading.service({});
                getEditFormConfigLists({ form_name: '手机学生完善信息表单', student_id: student_id }).then(response => {
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
                                } else {
                                    this.formDataLists[index].value = []
                                }
                            }
                            this.fetchRegionLists(item.value)
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
                        if (item.data_type === '图片') {
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
                })
            },
            fetchUnitLists() {
                getSelectUnitLists().then(response => {
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
            var name=""
            if (data.response===undefined){
              name=data.field_name
            }else{
              name=data.response.data.name
            }

            this.formDataLists.forEach((item,index) => {
              if (item.field_name===name){
                this.formDataLists[index].file=[]
                this.formData[item.field_name] =""
              }
            })
          },
          handlePreview(file) {
            var data = JSON.parse(JSON.stringify(file))
            this.ShowImage.title = data.name
            this.ShowImage.url = data.url
            this.dialogShowImageVisible = true
          },
          studentDataFormat(){
            this.formDataLists.forEach((item) => {
              console.log(item.field_name + '——' + item.value);
              this.formData[item.field_name] = item.value
            })
          },
          uploadImageSuccess(response, file, fileList) {
            fileList[0].url = process.env.BASE_API+response.data.url
            this.formDataLists.forEach((item, index) => {
              if (item.field_name === response.data.name) {
                this.formDataLists[index].value = response.data.url
                this.formDataLists[index].file = fileList
                this.studentDataFormat()
              }
            })
          },
            handleSaveStudent() {
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
                        formdata["pici_id"] = this.piciValue
                        editStudentInfo(formdata).then(response => {
                            this.$vux.alert.show({
                                title: '温馨提示',
                                content: '保存信息成功'
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
            },
            handleSumbit() {
                var _this = this
                _this.$vux.confirm.show({
                    title:'提示',
                    content:'是否确认【' + this.studentInfo.name + '】已到校报到?',
                    onConfirm () {
                        setRegister({ student_id: _this.studentInfo['id'], is_register: '是' }).then(response => {
                            _this.$vux.alert.show({
                                title: '提示',
                                content:'确认成功',
                                onHide () {
                                    _this.fetchStudentInfo(_this.studentInfo['id'])
                                }
                            })
                        })
                    }
                })
            },
            handleCancel() {
                var _this = this
                _this.$vux.confirm.show({
                    title:'提示',
                    content:'取消【' + this.studentInfo.name + '】 到校报到, 是否继续?',
                    onConfirm () {
                        setRegister({ student_id: _this.studentInfo['id'], is_register: '否' }).then(response => {
                            _this.$vux.alert.show({
                                title: '提示',
                                content:'取消成功',
                                onHide () {
                                    _this.fetchStudentInfo(_this.studentInfo['id'])
                                }
                            })
                        })
                    }
                })
            },
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
