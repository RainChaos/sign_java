<template>
  <el-row>
    <el-table :data="serverInfoData" border style="width: 100%">
      <el-table-column prop="name" label="服务器参数" width="400"/>
      <el-table-column prop="value" label="参数值"/>
    </el-table>
  </el-row>
</template>
<script>
import { getServerInfo } from '@/api/main'
export default {
  name: 'ServerInfo',
  data() {
    return {
      serverInfoData: []
    }
  },
  mounted: function() {
    this.fetchServerInfo()
  },
  methods: {
    fetchServerInfo() {
      getServerInfo().then(response => {
        var data = response.data
        this.serverInfoData = data
      }).catch(error => {
        console.log(error)
      })
    }
  }
}
</script>