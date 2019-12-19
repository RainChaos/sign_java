<template>
  <div style="width:100%;height:100%;">
    <app-header/>
    <app-main/>
    <app-footer />
  </div>
</template>

<script>
import { AppHeader, AppMain, AppFooter } from './components'

export default {
  name: 'Layout',
  components: {
    AppHeader,
    AppMain,
    AppFooter
  },
  computed: {
  },
  methods: {

  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
