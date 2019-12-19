<template>
  <div id="app" style="height: 100%">
    <router-view></router-view>
  </div>
</template>

<script>
    import fixedInput from './utils/fixedInput'
export default {
  name: 'app',
    mixins: [fixedInput],
    updated() {
        // 解决ios输入框弹出的页面样式问题
        document.querySelectorAll("input").forEach(item => {
            item.onblur = this.temporaryRepair;
        });
        document.querySelectorAll("select").forEach(item => {
            item.onchange = this.temporaryRepair;
        });
        document.querySelectorAll("textarea").forEach(item => {
            item.onblur = this.temporaryRepair;
        });
    },
}
</script>

<style lang="less">
@import '~vux/src/styles/reset.less';
html{
  height: 100%;
  overflow-x: hidden;
}
body {
  height: 100% !important;
  background-color: #fbf9fe;
  overflow-x: hidden;
}
  #app{
    height:100%;
  }
</style>
