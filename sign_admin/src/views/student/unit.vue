<template>
  <div class="app-container" style="height: 100%;">
    <el-row style="height: 100%;">
      <el-col :span="5" style="border:1px solid #dddddd;height:100%;">
        <div style="padding:10px;border-bottom:1px solid #dddddd">
          学生单位结构
        </div>
        <el-scrollbar style="height: calc(100% - 50px);">
          <el-tree
            ref="treeBox"
            :data="schoolData"
            :props="defaultProps"
            :highlight-current="true"
            :default-expand-all="false"
            :default-expanded-keys="expandedUnitCode"
            :expand-on-click-node="false"
            :check-strictly="true"
            :indent="10"
            :load="loadNode"
            node-key="unit_code"
            @current-change="currentChange"/>
        </el-scrollbar>
      </el-col>
      <el-col :span="19" style="height:100%;padding-left:10px;">
        <el-row style="border:1px solid #dddddd;height:100%;">
          <el-col :span="24" style="border-bottom: 1px solid #dddddd;padding: 10px;">
            <el-button :loading="sysnBtnLoading" type="primary" size="small" @click="sysnCharges">
              <i class="el-icon-refresh" aria-hidden="true"/>
              同步数据
            </el-button>
            <el-button :disabled="addNextUnitDisabled" type="primary" size="small" style="width: 120px" @click="addNextUnit">
              <i class="el-icon-plus" aria-hidden="true"/>
              添加下级单位
            </el-button>
            <el-button :disabled="cancelAddNextUnitDisabled" type="primary" size="small" style="width: 120px" @click="cancelAddNextUnit">
              <i class="fa fa-reply-all" aria-hidden="true"/>
              取消
            </el-button>
            <el-button :disabled="saveUnitDisabled" :loading="saveUnitBtnLoading" type="primary" size="small" style="width: 120px" @click="saveUnitData">
              <i class="el-icon-edit" aria-hidden="true"/>
              保存
            </el-button>
            <el-button :disabled="deleteUnitDisabled" :loading="deleteUnitBtnLoading" type="danger" size="small" style="width: 120px" @click="deleteUnit">
              <i class="el-icon-delete" aria-hidden="true"/>  删除
            </el-button>
          </el-col>
          <el-col :span="24">
            <el-alert
              :closable="false"
              title="需要操作单位结构需要先选中左边的单位数据，不同层次的单位结构需要填写不同的信息，变成不可编辑的字段是不用到的，显示的是默认值"
              type="warning"
              show-icon />
          </el-col>
          <el-col :span="16" style="padding-top:20px;">

            <el-form ref="form" :model="formData" size="small" label-width="100px">
              <el-form-item >
                <label slot="label">单位代码</label>
                <el-input v-model="formData.unit_code" style="min-width:300px;" disabled/>
              </el-form-item>
              <el-form-item >
                <label slot="label">单位名称</label>
                <el-input v-model="formData.unit_name" style="min-width:300px;"/>
              </el-form-item>
              <el-form-item >
                <label slot="label">自定义代码</label>
                <el-input v-model="formData.unit_self_code" style="min-width:300px;" disabled/>
              </el-form-item>
              <el-form-item >
                <label slot="label">学期</label>
                <el-select v-model="formData.unit_term" placeholder="请选择学期" style="width:300px;">
                  <el-option v-for="item in stTermLists" :key="item.code" :label="item.name" :value="item.code"/>
                </el-select>
              </el-form-item>
              <el-form-item >
                <label slot="label">类型</label>
                <el-select v-model="formData.unit_type" placeholder="请选择学期" style="width:300px;">
                  <el-option v-for="item in stTypeLists" :key="item.code" :label="item.name" :value="item.code"/>
                </el-select>
              </el-form-item>
              <el-form-item >
                <label slot="label">入学时间</label>
                <el-select v-model="formData.unit_entrance_date" placeholder="请选择学期" style="width:300px;">
                  <el-option v-for="item in stRxsjLists" :key="item.code" :label="item.name" :value="item.code"/>
                </el-select>
              </el-form-item>
              <el-form-item >
                <label slot="label">状态</label>
                <el-select ref="unitStatus" v-model="formData.unit_status" placeholder="请选择学期" style="width:300px;">
                  <el-option v-for="item in stZxztLists" :key="item.code" :label="item.name" :value="item.code"/>
                </el-select>
              </el-form-item>
              <el-form-item >
                <label slot="label">管理体制</label>
                <el-select v-model="formData.unit_manage_system" placeholder="请选择学期" style="width:300px;">
                  <el-option v-for="item in stGltzLists" :key="item.code" :label="item.name" :value="item.code"/>
                </el-select>
              </el-form-item>
              <el-form-item >
                <label slot="label">排序</label>
                <el-input-number v-model="formData.unit_sort" style="min-width:300px;"/>
              </el-form-item>

            </el-form>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-dialog :visible.sync="importDialogVisible" :show-close="true" title="同步数据进度" width="30%">
      <div><el-progress :text-inside="true" :stroke-width="18" :percentage="percentage"/></div>
    </el-dialog>
  </div>
</template>

<script>
import { getStructureLists,getClassList, saveUnit, deleteUnit, SyncCharges, getChildsCount, getNextUnitCode } from '@/api/unit'
import { getExpandedUnitCode } from '@/api/config'
import store from '@/store'
import { GetNewUnitSelfCode } from '@/utils/student'
import { getUseDictionaryLists } from '@/api/dictionary'

import {setCurrentUrl, getCurrentUrl} from '@/utils/auth.js'

const ayncSchoolStructureResData = {
  current_page: 0,
  last_page: 0,
  per_page: 100,
  total: 0,
  finish: false
}
export default {
  name:'Unit',
  data() {
    return {
      defaultCode:'',
      schoolData: [],
      expandedUnitCode: ['001'],
      stTermLists: [],
      stTypeLists: [],
      stRxsjLists: [],
      stZxztLists: [],
      stGltzLists: [],
      addNextUnitTag: false,
      formData: {
        unit_code: '',
        unit_self_code: '',
        farther_code: '',
        unit_name: '',
        unit_type: '',
        unit_manage_system: '',
        unit_entrance_date: '',
        unit_term: '',
        unit_status: '',
        unit_sort: ''
      },
      TreeData: [],
      selectUnit: {},
      defaultProps: {
        children: 'children',
        label: 'unit_name'
      },
      sysnBtnLoading: false,
      saveUnitBtnLoading: false,
      deleteUnitBtnLoading: false,
      importDialogVisible: false,
      sysnChargesLoading: true,
      ayncSchoolStructureRes: Object.assign({}, ayncSchoolStructureResData),
      addNextUnitDisabled: true,
      cancelAddNextUnitDisabled: true,
      saveUnitDisabled: true,
      deleteUnitDisabled: true
    }
  },
  computed: {

    // 同步收费系统单位数据进度条
    percentage: function() {
      const total = parseInt(this.ayncSchoolStructureRes.total)
      const finish = this.ayncSchoolStructureRes.finish
      if (total === 0) {
        return 0
      }
      if (finish) {
        return 100
      }
      const current_page = parseInt(this.ayncSchoolStructureRes.current_page)
      const per_page = parseInt(this.ayncSchoolStructureRes.per_page)
      return parseInt(((current_page * per_page) / total) * 100)
    }
  },
  activated(){
    setCurrentUrl(this.$route.path)
    this.fetchDictionaryData()
  },
  mounted() {
    setCurrentUrl(this.$route.path)
    this.fetchDictionaryData()
    getStructureLists().then(response => {
      const data = response.data
      this.schoolData = data
      console.log(data[0])
      data.forEach((item) => {
        console.log(item["unit_code"])
         if (item["unit_code"]===this.defaultCode){
           this.formData=item
         }
      })
    }).catch(error => {
      console.log(error)
    })
  },

  methods: {

    fetchDictionaryData() {
      getUseDictionaryLists({ type: 'STTERM' }).then(response => {
        this.stTermLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
      getUseDictionaryLists({ type: 'XSLBDM' }).then(response => {
        this.stTypeLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
      getUseDictionaryLists({ type: 'RXSJDM' }).then(response => {
        this.stRxsjLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
      getUseDictionaryLists({ type: 'ZXZTDM' }).then(response => {
        this.stZxztLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
      getUseDictionaryLists({ type: 'GLTZDM' }).then(response => {
        this.stGltzLists = response.data.data
      }).catch(error => {
        console.log(error)
      })
      getExpandedUnitCode().then(response => {
        this.expandedUnitCode.push(response.data)
        this.defaultCode=response.data
      }).catch(error => {
        console.log(error)
      })
    },

    // 设置当前结构数据
    currentChange(data, node) {
      this.addNextUnitTag = false
      const unitData = JSON.parse(JSON.stringify(data))
      this.formData = unitData
      // 获取单位最长度
      const unitLength = store.getters.structureLength

      if (unitData.unit_code.length === unitLength - 3) {
        this.addNextUnitDisabled = false
      } else {
        this.addNextUnitDisabled = true
      }

      if (unitData.unit_code.length === unitLength) {
        this.saveUnitDisabled = false
        this.deleteUnitDisabled = false
      } else {
        this.saveUnitDisabled = true
        this.deleteUnitDisabled = true
      }
    },

    // 同步收费系统的单位结构数据
    sysnCharges() {
      SyncCharges().then(response => {
        var data = response.data
        this.$alert('同步成功', '成功提示', { type: 'success' })
        setCurrentUrl(this.$route.path)
        this.fetchDictionaryData()
        getStructureLists().then(response => {
          const data = response.data
          this.schoolData = data
          console.log(data[0])
          data.forEach((item) => {
            console.log(item["unit_code"])
            if (item["unit_code"]===this.defaultCode){
              this.formData=item
            }
          })
        }).catch(error => {
          console.log(error)
        })
      }).catch(error => {
        this.sysnBtnLoading = false
        console.log(error)
      })
    },

    // 添加下级单位
    addNextUnit() {
      this.addNextUnitDisabled = true
      this.saveUnitDisabled = false
      this.cancelAddNextUnitDisabled = false
      this.addNextUnitTag = true
      const unitLength = store.getters.structureLength
      const node = this.$refs.treeBox.getCurrentNode()
      const data = JSON.parse(JSON.stringify(node))
      if (data.unit_term===''||data.unit_term===null){
        this.stTermLists.forEach((term) => {
          if(term.code.startsWith(data.unit_name.substr(0,4))){
            data.unit_term=term.code
          }
        })
      }
      const unitCodeLength = data.unit_code.length
      var unit_name = ''

      if (unitLength === 18) {
        switch (unitCodeLength) {
          case 3 : unit_name = '新分院名称'; break
          case 6 : unit_name = '新院系名称'; break
          case 9 : unit_name = '新专业名称'; break
          case 12 : unit_name = '新年级名称'; break
          case 15 : unit_name = '新班级名称'; break
        }
      }
      if (unitLength === 15) {
        switch (unitCodeLength) {
          case 3 : unit_name = '新院系名称'; break
          case 6 : unit_name = '新专业名称'; break
          case 9 : unit_name = '新年级名称'; break
          case 12 : unit_name = '新班级名称'; break
        }
      }

      if (unitLength === 12) {
        switch (unitCodeLength) {
          case 3 : unit_name = '新专业名称'; break
          case 6 : unit_name = '新年级名称'; break
          case 9 : unit_name = '新班级名称'; break
        }
      }

      if (unitLength === 9) {
        switch (unitCodeLength) {
          case 3 : unit_name = '新年级名称'; break
          case 6 : unit_name = '新班级名称'; break
        }
      }
      if (unitLength === 6) {
        switch (unitCodeLength) {
          case 3 : unit_name = '新班级名称'; break
        }
      }

      var formData = {
        unit_code: '',
        unit_self_code: '',
        farther_code: data.unit_code,
        unit_name: unit_name,
        unit_type: data.unit_type,
        unit_manage_system: data.unit_manage_system,
        unit_entrance_date: data.unit_entrance_date,
        unit_term: data.unit_term,
        unit_status: data.unit_status,
        unit_sort: 1
      }
      this.formData = formData

      getChildsCount({ farther_code: data.unit_code }).then(response => {
        var childsCount = response.data
        var unit_self_code = GetNewUnitSelfCode(data.unit_self_code, childsCount)
        this.formData.unit_self_code = unit_self_code
      })

      getNextUnitCode({ farther_code: data.unit_code }).then(response => {
        var unit_code = response.data
        this.formData.unit_code = unit_code
      })

      if (unitLength > 12) {
        if (unitCodeLength === (unitLength - 9) || unitCodeLength === (unitLength - 6)) {
          this.$refs.unitStatus.disabled = true
        }
      }
    },

    // 取消添加下级单位
    cancelAddNextUnit() {
      this.addNextUnitDisabled = false
      this.saveUnitDisabled = true
      this.cancelAddNextUnitDisabled = true
      this.addNextUnitTag = false
      const node = this.$refs.treeBox.getCurrentNmaxode()
      const data = JSON.parse(JSON.stringify(node))
      this.formData = data
    },

    saveUnitData() {
      this.saveUnitBtnLoading = true
      for(var o in this.formData ){
        if(this.formData[o] == '' || this.formData[o] == null){
          this.$alert('请先完善信息后提交', '错误提示', { type: 'error' })
          this.saveUnitBtnLoading = false
          return false;
        }
      }
      return new Promise((resolve, reject) => {
        saveUnit(this.formData).then(response => {
          this.$alert('保存成功', '成功提示', { type: 'success' })
          this.saveUnitBtnLoading = false
          const node = this.$refs.treeBox.getCurrentNode()
          var keys = this.$refs.treeBox.getCurrentKey()
          const data = JSON.parse(JSON.stringify(node))
          const selectNode = this.$refs.treeBox.getNode(data.unit_code)
          var farther_code = data.unit_code
          // 如果是修改则更新数据，如果是添加则追加节点数据
          if (this.addNextUnitTag === false) {
            keys = data.farther_code
            farther_code = data.farther_code
            getClassList({ farther_code: farther_code }).then(response => {
              const newdata = response.data
              console.log(newdata)
              this.$refs.treeBox.updateKeyChildren(keys, newdata)
              this.$refs.treeBox.setCurrentKey(data.unit_code)
            }).catch(error => {
              console.log(error)
            })
          } else {
            this.$refs.treeBox.append(this.formData, selectNode)
          }
          resolve(response)
        }).catch(error => {
          this.saveUnitBtnLoading = false
          reject(error)
        })
      })
    },
    deleteUnit() {

        this.deleteUnitBtnLoading = true
        const node = this.$refs.treeBox.getCurrentNode()
        const data = JSON.parse(JSON.stringify(node))
        return new Promise((resolve, reject) => {
          deleteUnit({ unit_code: data.unit_code }).then(response => {
            const selectNode = this.$refs.treeBox.getNode(data.farther_code)
            this.$refs.treeBox.setCurrentKey(data.farther_code)
            this.formData = JSON.parse(JSON.stringify(selectNode.data))
            this.$alert('删除成功', '成功提示', { type: 'success' })
            this.$refs.treeBox.remove(data)
            this.deleteUnitDisabled = true
            this.deleteUnitBtnLoading = false
            resolve(response)
          }).catch(error => {
            this.deleteUnitBtnLoading = false
            reject(error)
          })
        })

    }
  }
}
</script>

<style scoped>

</style>
