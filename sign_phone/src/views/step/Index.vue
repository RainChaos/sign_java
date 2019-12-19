<template>
  <div class="main-page"  :style='{"background-image":"url("+bgUrl+")"}'>
    <div class="top">
      <div class="top-info-ct" v-html="topInfoContent">
        &nbsp;{{topInfoContent}}
      </div>
    </div>
    <div class="content" style="padding-bottom:50px;">
      <div style="text-align: center;font-size:15px;font-weight: bolder">请选择报名类型</div>
      <div class="span-text">我是：</div>
      <div style="margin-top:10px;">
        <flexbox :gutter="10">
          <flexbox-item v-for="item in typeLists" :key="item.id">
            <div @click="handleStudentType(item.code)"
                 :class="{'typeItem':true, 'selectedItem':studentType==item.code}">{{item.name}}
            </div>
          </flexbox-item>
        </flexbox>
      </div>
      <div class="span-text">我要参加：</div>
      <div style="margin-top:10px;">
        <!--<flexbox :gutter="10">-->
        <div v-for="item in piciLists" :key="item.id" style="margin-bottom: 2px">
          <div v-if="item.baoming_status===1" @click="handleStudentPici(item.id,item.baoming_status)"
               :class="{'typeItem':true, 'selectedItem':studentPici==item.id}">{{item.remark}}
          </div>
          <div v-if="item.baoming_status===2" @click="handleStudentPici(item.id,item.baoming_status)"
               :class="{'typeItem':true, 'selectedItem':studentPici==item.id}">{{item.remark}}[未开始]
          </div>
          <div v-if="item.baoming_status===3" @click="handleStudentPici(item.id,item.baoming_status)"
               :class="{'typeItem':true, 'selectedItem':studentPici==item.id}">{{item.remark}}[已结束]
          </div>
        </div>
        <!--</flexbox>-->
      </div>
      <div style="margin-top:20px;">
        <x-button :disabled="baomingStatus!==1" type="primary" @click.native="handleAddStudent">下一步</x-button>
      </div>
      <div style="margin-top:20px;">
        <x-button type="default" @click.native="handleSearchStudent">我已报名，预缴费用</x-button>
        <x-button type="default" @click.native="getNotice">我已报名，获取通知书</x-button>
      </div>
    </div>
    <div v-transfer-dom>
      <x-dialog v-model="showTishi" class="dialog-demo">
        <div style="padding:10px;">
          <el-row style="text-align: center;font-size:18px;font-weight: bolder;color:#666;">
            填报前，您需要了解和准备
            <div @click="showTishi=false" style="position: absolute;top:2px;right: 10px;">
              <span class="el-icon-close"></span>
            </div>
          </el-row>
          <el-row style="text-align: left;font-size:13px;">
            <span style="font-weight: bolder">
              一、不申请劳动预备制，请先准备好学生的身份证号码；
            </span><br/>
            <span style="font-weight: bolder">
              二、申请劳动预备制，除身份证号码还需要符合申请劳动预备制的材料照片；
              <span style="color:#dd0000">（注意：各个专业名额有限，申请之后最终以学校审核通过为准）</span>
            </span><br/>
            1.什么是劳动预备制培训？<br/>
            国家针对中职学校学生一项优惠政策，符合条件的劳动预备制学员，在免学费的基础上，免实习费、免住宿费。<br/>
            2.符合什么条件才能申请？<br/>
            年满16周岁，应届初中生或高中生，广西户口。<br/>
            3.网上初审需提交什么材料？<br/>
            （1）身份证原件照片（正、反面）<br/>
            （2）应届初中毕业证原件照片、或应届高中毕业证原件照片<br/>
            （3）户口本原件第一页、户主页和学生本人页照片<br/>
          </el-row>
        </div>
        <div style="padding:0px 15px 15px 15px;">
          <x-button @click.native="handleAddStudent" type="primary">准备完毕，进入填报</x-button>
        </div>
      </x-dialog>
    </div>
  </div>
</template>

<script>
    import {ViewBox, Flexbox, FlexboxItem, XButton, XDialog, TransferDomDirective as TransferDom} from 'vux'
    import {getArticleByTitle} from '@/api/article'
    import {getPiciLists} from '@/api/studentPici'
    import {getSelectUnitCount} from '@/api/selectUnit'
    import {getListsByCode} from '@/api/dictionary'
    import {getBGIMG} from '@/api/main'

    export default {
        directives: {
            TransferDom
        },
        components: {
            ViewBox,
            Flexbox,
            FlexboxItem,
            XButton,
            XDialog
        },
        name: 'Index',
        data() {
            return {
                showTishi: false,
                baomingStatus: 1,
                topInfoContent: '',
                studentType: '',
                studentPici: 0,
                ldybz: false,
                piciLists: [],
                typeLists: [],
                openid: '',
                bgUrl: '',
                note: {
                    backgroundImage: "bgUrl",
                    backgroundRepeat:"no-repeat",
                }

            }
        },
        mounted() {
            this.fetchTypeLists()
            this.fetchPiciLists()
            this.fetchTopInfoContent()
            this.getOpenId()
            this.getBGIMG()
        },
        methods: {
            getBGIMG(){
                getBGIMG().then(reponse => {
                    console.log(reponse.data)
                    this.bgUrl = process.env.BASE_API+reponse.data
                }).catch(error => {
                    console.log(error)
                })
            },
            getOpenId() {
                this.openid = this.$route.query.openid
            },
            fetchPiciLists() {
                getPiciLists().then(reponse => {
                    this.piciLists = reponse.data.data
                }).catch(error => {
                    console.log(error)
                })
            },
            fetchTypeLists() {
                getListsByCode({type: 'BEDUCATION'}).then(reponse => {
                    this.typeLists = reponse.data.data
                }).catch(error => {
                    console.log(error)
                })
            },
            fetchTopInfoContent() {
                getArticleByTitle({title: '手机端首页提示信息'}).then(response => {
                    this.topInfoContent = response.data.content
                }).catch(error => {
                    console.log(error)
                })
            },
            handleStudentType(name) {
                this.studentType = name
            },
            handleStudentPici(name, status) {
                this.baomingStatus = status
                this.studentPici = name
            },
            handleShowTishi() {
                this.showTishi = true
            },
            handleAddStudent() {
                this.showTishi = false
                var type = this.studentType
                var pici = this.studentPici
                var ldybz = this.ldybz === true ? 1 : 0

                if (type === '') {
                    this.$vux.alert.show({
                        title: '错误提示',
                        content: '请先选择学生类型'
                    })
                    return
                }
                if (pici === 0) {
                    this.$vux.alert.show({
                        title: '错误提示',
                        content: '请先选择报名季节'
                    })
                    return
                }
                if (type !== '' && pici !== 0) {
                    getSelectUnitCount({pici_id: pici, education: type}).then(response => {
                        var count = parseInt(response.data)
                        if (count > 0) {
                            this.$router.push({path: '/add', query: {type: type, pici: pici, openid: this.openid}})
                        } else {
                            this.$vux.alert.show({
                                title: '温馨提示',
                                content: '所选类型还没开放专业报名'
                            })
                        }
                    })
                }
            },
            handleSearchStudent() {
                this.$router.push({path: '/search', query: {openid: this.openid}})
            },
            getNotice() {
                this.$router.push({path: '/getNotice', query: {openid: this.openid}})
            },
        }
    }
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
  /*.main-page{*/
  /*  background-image:url("../../../static/bg.png");*/
  /*}*/
  .top {
    padding: 80px 15px 15px 15px;
  }

  .top-info-ct {
    border-radius: 5px;
    min-height: 80px;
    padding: 10px;
    font-size: 11px;
    background-color: #fff;
    font-weight: 500;
  }

  .content {
    padding: 10px 20px;
    overflow: hidden;
  }

  .span-text {
    font-size: 16px;
    color: #666;
    padding-left: 20px;
    margin-top: 10px;
  }

  .typeItem {
    cursor: pointer;
    color: #257be8;
    padding: 5px;
    text-align: center;
    border: 1px solid #257be8;
  }

  .selectedItem {
    background: #257be8;
    color: #FFF;
  }
</style>
