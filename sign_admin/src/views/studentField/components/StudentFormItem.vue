<template>
  <div>
    <el-row>
      <el-col :span="12">
        <el-form :model="formData" :rules="rules" size="small">
          <template v-for="(item) in formDataLists">
            <el-form-item v-if="item.data_type=='字符串'" :label-width="item.labelWidth" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input v-model="item.value" :style="{width:item.width}"/>
              <label class="form-label-text"> &nbsp;<i class="fa fa-warning" /> 23424</label>
            </el-form-item>
            <el-form-item v-if="item.data_type=='文本'" :label-width="item.labelWidth" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-input v-model="item.value" :style="{width:item.width}" type="textarea"/>
              <label class="form-label-text"> &nbsp;<i class="fa fa-warning" /> 23424</label>
            </el-form-item>
            <el-form-item v-if="item.data_type=='省市区'" :label-width="item.labelWidth" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader v-model="item.value" :options="regionOptions" :props="regionProps" :style="{width:item.width}" @active-item-change="handleItemChange"/>
              <label class="form-label-text"> &nbsp;<i class="fa fa-warning" /> 23424 </label>
            </el-form-item>
            <el-form-item v-if="item.data_type=='基础数据'" :label-width="item.labelWidth" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-select v-model="item.value" :style="{width:item.width}" placeholder="请选择类型">
                <el-option v-for="item2 in item.dataLists" :key="item2.value" :label="item2.text" :value="item2.value"/>
              </el-select>
            </el-form-item>
            <el-form-item v-if="item.data_type=='单位代码'" :label-width="item.labelWidth" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-cascader v-model="item.value" :options="unitOptions" :props="unitProps" :style="{width:item.width}"/>
              <label class="form-label-text"> &nbsp;<i class="fa fa-warning" />：23424</label>
            </el-form-item>
            <el-form-item v-if="item.data_type=='图片'" :label-width="item.labelWidth" :prop="item.field_name">
              <label slot="label">{{ item.describe }}:</label>
              <el-upload
                :on-preview="handlePreview"
                :on-remove="handleRemove"
                :name="item.field_name"
                v-model="item.value"
                :file-list="item.file"
                :data="{describe:item.describe,field_name:item.field_name}"
                :action="uploadImageUrl"
                :limit="item.limit"
                :multiple="item.multiple"
                :on-success="uploadImageSuccess"
                :style="{width:item.width}"
                list-type="picture">
                <el-button size="small" type="primary">点击上传</el-button>
                <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
              </el-upload>
            </el-form-item>
          </template>
        </el-form>
      </el-col>
      <el-col :span="12"/>
    </el-row>
  </div>
</template>

<script>
import { getRegionLists } from '@/api/region'
import { getSelectUnitLists } from '@/api/selectUnit'
import { formValidater } from '@/utils/formValidate'
export default {
  data() {
    return {
      uploadImageUrl: process.env.BASE_API + '/admin/file/uploadImage',
      formDataLists: [
        {
          id: 1,
          data_type: '字符串',
          describe: '姓名',
          field_name: 'name',
          width: '400px',
          is_must_input: true,
          value: null,
          labelWidth: '100px',
          sort: 1
        },
        {
          id: 2,
          describe: '家庭地址',
          data_type: '省市区',
          field_name: 'region',
          labelWidth: '100px',
          width: '400px',
          is_must_input: true,
          value: [450000, 450300, 450323],
          sort: 2
        },
        {
          id: 2,
          describe: '户口本地址',
          data_type: '省市区',
          field_name: 'hkb_region',
          labelWidth: '100px',
          width: '400px',
          validate: 'idcode_validate',
          is_must_input: true,
          value: null,
          sort: 2
        },
        {
          id: 2,
          describe: '报读专业',
          data_type: '单位代码',
          field_name: 'unit_code',
          labelWidth: '100px',
          width: '400px',
          validate: 'idcode_validate',
          is_must_input: true,
          value: null,
          sort: 2
        },
        {
          id: 2,
          describe: '详细地址',
          data_type: '字符串',
          field_name: 'address',
          labelWidth: '100px',
          width: '400px',
          validate: 'idcode_validate',
          is_must_input: true,
          value: null,
          sort: 2
        },
        {
          id: 3,
          describe: '身份证',
          data_type: '字符串',
          field_name: 'idcard',
          labelWidth: '100px',
          width: '400px',
          validate: 'idcode_validate',
          is_must_input: true,
          value: null,
          sort: 2
        },
        {
          id: 4,
          describe: '备注',
          data_type: '文本',
          field_name: 'remark',
          labelWidth: '100px',
          width: '400px',
          validate: 'idcode_validate',
          is_must_input: true,
          value: null,
          sort: 2
        },
        {
          id: 4,
          describe: '分数条',
          data_type: '图片',
          field_name: 'photo',
          labelWidth: '100px',
          width: '400px',
          limit: 1,
          file: [],
          multiple: false,
          validate: 'idcode_validate',
          is_must_input: true,
          value: null,
          sort: 2
        },
        {
          id: 4,
          describe: '请假条',
          data_type: '图片',
          field_name: 'photo2',
          labelWidth: '100px',
          width: '400px',
          limit: 1,
          file: [],
          multiple: false,
          validate: 'idcode_validate',
          is_must_input: true,
          value: null,
          sort: 2
        },
        {
          id: 4,
          describe: '民族',
          data_type: '基础数据',
          field_name: 'mz',
          labelWidth: '100px',
          width: '400px',
          validate: '',
          is_must_input: true,
          value: '汉族',
          sort: 2,
          dataLists: [
            {
              text: '汉族',
              value: '汉族'
            },
            {
              text: '苗族',
              value: '苗族'
            }
          ]
        }
      ],
      rules: {
        name: [
          { required: true, message: '请输入名字', trigger: 'blur' }
        ],
        idcard: [
          { required: true, message: '请输入身份证号码', trigger: 'blur' },
          { validator: formValidater.checkdIdCard, trigger: 'blur' }
        ]
      },
      regionOptions: [],
      regionProps: {
        value: 'code',
        label: 'full_name',
        children: 'children'
      },
      unitOptions: [],
      unitProps: {
        value: 'unit_code',
        label: 'unit_name',
        children: 'children'
      }
    }
  },
  computed: {
    formData: function() {
      var formData = {}
      this.formDataLists.forEach((item) => {
        formData[item.field_name] = item.value
      })
      return formData
    }
  },
  mounted() {
    this.formDataLists.forEach((item) => {
      if (item.data_type === '省市区') {
        this.fetchRegionLists(item.value)
      }
    })
    this.fetchUnitLists()
  },
  methods: {
    fetchUnitLists() {
      getSelectUnitLists().then(response => {
        var UnitLists = JSON.parse(JSON.stringify(response.data))
        console.log('UnitLists', UnitLists)
        this.unitOptions = UnitLists
      })
    },
    fetchRegionLists(value) {
      var shengfen_code = (value !== null) ? value[0] : null
      var chengshi_code = (value !== null) ? value[1] : null
      var ShengFenIndex = 0
      var ChengShiIndex = 0
      // 获取省份信息
      getRegionLists({ fatherCode: 100000 }).then(response => {
        var ShengFenLists = JSON.parse(JSON.stringify(response.data))
        ShengFenLists.forEach((item, index) => {
          ShengFenLists[index]['children'] = []
          if (ShengFenLists[index].code === shengfen_code) {
            ShengFenIndex = index
          }
        })
        if (value == null) {
          this.regionOptions = ShengFenLists
        } else {
          // 获取城市信息
          getRegionLists({ fatherCode: shengfen_code }).then(response => {
            var ChengShiLists = JSON.parse(JSON.stringify(response.data))
            ChengShiLists.forEach((item, index) => {
              ChengShiLists[index]['children'] = []
              if (ChengShiLists[index].code === chengshi_code) {
                ChengShiIndex = index
              }
            })
            ShengFenLists[ShengFenIndex]['children'] = ChengShiLists
            // 获取地区信息
            getRegionLists({ fatherCode: chengshi_code }).then(response => {
              var DiquLists = JSON.parse(JSON.stringify(response.data))
              ShengFenLists[ShengFenIndex]['children'][ChengShiIndex]['children'] = DiquLists
              this.regionOptions = ShengFenLists
            })
          })
        }
      })
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
      console.log(file, fileList)
    },
    handlePreview(file) {
      console.log(file)
    },
    uploadImageSuccess(response, file, fileList) {
      console.log(response.data)
      console.log(file)
      console.log(fileList)
      fileList[0].url = response.data.url
      this.formDataLists.forEach((item, index) => {
        if (item.field_name === response.data.field_name) {
          this.formDataLists[index].value = response.data.value
          this.formDataLists[index].file = fileList
        }
      })
      console.log('formDataLists', JSON.parse(JSON.stringify(this.formDataLists)))
    }
  }
}
</script>

<style scoped>
  .form-label-text{
    color:#dcac6c;
  }
</style>
