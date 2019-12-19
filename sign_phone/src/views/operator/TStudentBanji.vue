<template>
    <div class="main-page">
        <op-header web-title="2019秋季招生数据实时监控"></op-header>
        <scroller lock-x scrollbar-y
                  height="100%"
                  use-pulldown
                  :pulldown-config="pulldownDefaultConfig"
                  ref="scroller"
                  style="font-size:13px;color:#fff;"
                  @on-pulldown-loading="refresh">
            <div style="overflow: auto;background:#fff;margin:0px 5px;padding-bottom: 50px">
                <el-row style="margin-bottom: 5px;padding:0px 10px;">
                    <el-form :model="params" size="mini">
                        <el-form-item  style="margin-bottom: 0px;margin-top:5px;">
                            <el-select v-model="params.unit_code" placeholder="请选择班级" style="width:100%">
                                <el-option v-for="item in banjiLists" :key="item.unit_code" :label="item.banji" :value="item.unit_code"/>
                            </el-select>
                        </el-form-item>
                        <el-form-item style="margin-bottom: 0px;margin-top:5px;" >
                            <el-input v-model="params.keyword" placeholder="姓名/学号/身份证号码"/>
                        </el-form-item>
                        <el-form-item style="margin-bottom: 0px;margin-top:5px;" >
                            <el-button type="primary" size="small" style="width:100%;" @click.native="fetchData">查询</el-button>
                        </el-form-item>
                    </el-form>
                </el-row>
                <el-table
                    ref="ChargeItemsTable"
                    :data="studentLists"
                    fit
                    border
                    size="mini">
                    <el-table-column fixed="left" label="姓名" width="70">
                        <template slot-scope="scope">{{ scope.row.name }}</template>
                    </el-table-column>
                    <el-table-column label="学号" width="110">
                        <template slot-scope="scope">{{ scope.row.sno }}</template>
                    </el-table-column>
                    <el-table-column label="交费" width="60" align="center">
                        <template slot-scope="scope">
                            <i v-if="scope.row.is_pay===0||scope.row.is_pay==='否'||scope.row.is_pay===false" class="el-icon-close" style="color:firebrick" />
                            <i v-if="scope.row.is_pay===1||scope.row.is_pay==='是'||scope.row.is_pay===true" class="el-icon-check" style="color:forestgreen" />
                        </template>
                    </el-table-column>
                    <el-table-column label="报到" width="60" align="center">
                        <template slot-scope="scope">
                            <i v-if="scope.row.is_register===0||scope.row.is_register==='否'||scope.row.is_register===false" class="el-icon-close" style="color:firebrick" />
                            <i v-if="scope.row.is_register===1||scope.row.is_register==='是'||scope.row.is_register===true" class="el-icon-check" style="color:forestgreen" />
                        </template>
                    </el-table-column>
                    <el-table-column fixed="right" align="center" label="操作" width="50">
                        <template slot-scope="scope">
                            <el-button size="mini" type="text" plain @click="handleTzbj(scope.$index, scope.row)">调班</el-button>
                        </template>
                    </el-table-column>
                </el-table>
                <el-row v-if="pagination.total>0" style="text-align: center;margin-top:8px;">
                    <el-pagination
                        :current-page="pagination.current_page"
                        :page-sizes="[15,30, 50, 100]"
                        :page-size="pagination.pre_page"
                        :total="pagination.total"
                        background
                        layout="prev, pager, next,total"
                        @size-change="handleSizeChange"
                        @current-change="handleCurrentChange"/>
                </el-row>
            </div>
        </scroller>
    </div>
</template>

<script>
    import { XHeader,Flexbox, FlexboxItem, Scroller, Divider,XButton } from 'vux'
    import { Loading } from 'element-ui'
    import { countBanjiLists,getBanjiLists } from '@/api/studentCount'
    import { getStudentLists } from '@/api/student'
    import  OpHeader from './components/OpHeader'
    import store from '@/store'
    const pulldownDefaultConfig = {
        content: '下拉刷新',
        height: 40,
        autoRefresh: false,
        downContent: '下拉刷新',
        upContent: '释放后刷新',
        loadingContent: '正在刷新...',
        clsPrefix: 'xs-plugin-pulldown-'
    }
    export default {
        components: {
            XButton,
            Scroller,
            Divider,
            XHeader,
            Flexbox,
            FlexboxItem,
            OpHeader
        },
        data () {
            return {
                pulldownDefaultConfig: pulldownDefaultConfig,
                studentLists: [],
                banjiLists: [],
                loadingInstance: null,
                pagination: {
                    current_page: 0,
                    pre_page: 15,
                    total: 0
                },
                params: {
                    limit: 15,
                    page: 1,
                    unit_code:'001',
                    is_width_dor: true,
                    conditions: {},
                    keyword:'',
                    form_name: '维护学生列表',
                    formName: '维护学生列表'
                }
            }
        },
        mounted(){
            this.fetchBanjiLists();
            this.fetchData();
        },
        methods: {
            refresh() {
                this.fetchData()
            },
            fetchBanjiLists() {
              countBanjiLists().then(response => {
                    var lists = response.data
                    var banjiLists = []
                    banjiLists.push({unit_code:'001',banji: '全部'})
                    lists.forEach(item=>{
                        var d = {unit_code:item.unit_code,banji:item.unit_name}
                        banjiLists.push(d)
                    })
                    this.banjiLists = banjiLists
                })
            },
            fetchData() {
                return new Promise((resolve, reject) => {
                    this.loadingInstance = Loading.service({});
                    var _this = this
                    getStudentLists(this.params).then(response => {
                        _this.studentLists = response.data.data
                        this.pagination.current_page = response.data.current_page
                        this.pagination.total = response.data.total
                        _this.$nextTick(() => {
                            _this.$refs.scroller.donePulldown()
                            _this.$refs.scroller.reset({top: 0})
                            this.loadingInstance.close();
                        });
                        resolve(response)
                    }).catch(error => {
                        reject(error)
                    })
                })
            },
            handleSizeChange(val) {
                this.pagination.pre_page = val
                this.params.limit = this.pagination.pre_page
                this.params.page = 1
                this.fetchData()
            },
            handleCurrentChange(val) {
                this.params.limit = this.pagination.pre_page
                this.params.page = val
                this.fetchData()
            },
            handleTzbj(index, row){
                this.$router.push({ path: '/t/TStudentTzbjInfo', query: { student_id: row.id  }})
            }
        }
    }
</script>

<style scoped>
    .title-ct{
        padding-left:10px;
        line-height: 30px;
        background: #0068b7;
        color:#fff;
        font-size:13px;
    }
    .count-item{
        padding:10px;
        font-size:13px;
    }
    .count-number{
        font-weight: bolder;font-size:16px;
    }
</style>
