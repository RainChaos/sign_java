<template>
  <el-row :gutter="15" class="panel-group">
    <el-col :span="4" class="card-panel-col">
      <div class="card-panel" style="line-height:30px;font-size:16px;padding:20px;font-weight: bolder">
        <el-form :inline-message="true" size="mini">
          <el-form-item class="studentInfo" label-width="60px" label="年级：" style="margin-bottom: 8px;">
            <el-input v-model="configData.grade_name" disabled/>
          </el-form-item>
          <el-form-item label="批次：" label-width="60px" style="margin-bottom: 0px;width: 100%;">
            <el-select v-model="piciValue" placeholder="请选择季节批次" @change="handleChangePici">
              <el-option v-for="item in piciLists" :key="item.id" :label="item.name" :value="item.id"/>
            </el-select>
          </el-form-item>
        </el-form>
      </div>
    </el-col>
    <el-col :span="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">报名人数</div>
          <count-to :start-val="0" :end-val="studentCountInfo.StudentCount" :duration="2600" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :span="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">已报到人数</div>
          <count-to :start-val="0" :end-val="studentCountInfo.RegisterCount" :duration="2600" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :span="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">已报到男生人数</div>
          <count-to :start-val="0" :end-val="studentCountInfo.RegisterNanCount" :duration="2600" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :span="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-people">
          <svg-icon icon-class="peoples" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">已报到女生人数</div>
          <count-to :start-val="0" :end-val="studentCountInfo.RegisterNvCount" :duration="2600" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
    <el-col :span="4" class="card-panel-col">
      <div class="card-panel">
        <div class="card-panel-icon-wrapper icon-money">
          <svg-icon icon-class="money" class-name="card-panel-icon" />
        </div>
        <div class="card-panel-description">
          <div class="card-panel-text">已缴费人数</div>
          <count-to :start-val="0" :end-val="studentCountInfo.StudentPayCount" :duration="2600" class="card-panel-num"/>
        </div>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import CountTo from 'vue-count-to'
import { getCountStudent, getConfigData } from '@/api/main'
import { getPiciLists } from '@/api/pici'
export default {
  name: 'StudentCount',
  components: {
    CountTo
  },
  data() {
    return {
      studentCountInfo: {
        StudentCount: 0,
        RegisterCount: 0,
        RegisterNanCount: 0,
        RegisterNvCount: 0,
        StudentPayCount: 0
      },
      configData: {
        pici_name: '',
        grade_name: ''
      },
      piciValue: 0,
      piciLists: []
    }
  },
  mounted: function() {
    var _this = this
    this.fetchPiciData()
    this.fetchConfigData()
    this.fetchStudentCount()
    setTimeout(function() {
      _this.piciValue = _this.$store.getters.currentPici
    }, '1000')
  },
  methods: {
    handleChangePici() {
      this.fetchStudentCount()
    },
    fetchPiciData() {
      getPiciLists().then(response => {
        this.piciLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchStudentCount() {
      getCountStudent({ pici_id: this.piciValue }).then(response => {
        var data = response.data
        this.studentCountInfo = data
      }).catch(error => {
        console.log(error)
      })
    },
    fetchConfigData() {
      getConfigData().then(response => {
        var data = response.data
        this.configData = data
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .panel-group {
    margin-top: 0px;
  .card-panel-col{
    margin-bottom: 15px;
  }
  .card-panel {
    height: 108px;
    font-size: 12px;
    position: relative;
    overflow: hidden;
    color: #666;
    background: #fff;
    box-shadow: 4px 4px 40px rgba(0, 0, 0, .05);
    border-color: rgba(0, 0, 0, .05);
  .card-panel-icon-wrapper {
    color: #fff;
  }
  .icon-people {
    background: #40c9c6;
  }
  .icon-message {
    background: #36a3f7;
  }
  .icon-money {
    background: #f4516c;
  }
  .icon-shopping {
    background: #34bfa3
  }
  .card-panel-icon-wrapper {
    float: left;
    margin: 14px 0 0 14px;
    padding: 16px;
    transition: all 0.38s ease-out;
    border-radius: 6px;
  }
  .card-panel-icon {
    float: left;
    font-size: 48px;
  }
  .card-panel-description {
    float: right;
    font-weight: bold;
    margin: 26px;
    margin-left: 0px;
  .card-panel-text {
    line-height: 18px;
    color: rgba(0, 0, 0, 0.45);
    font-size: 16px;
    margin-bottom: 12px;
  }
  .card-panel-num {
    font-size: 20px;
  }
  }
  }
  }
</style>
