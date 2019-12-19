<template>
  <div style="width:100%;height:100%;">
    <div v-if="IsMenuShow" style="width:100%;height:100%;">
      <top-navbar/>
      <div :class="classObj" class="app-wrapper" style="height: calc(100% - 63px);overflow:hidden;">
        <div v-if="device==='mobile'&&sidebar.opened" class="drawer-bg" @click="handleClickOutside"/>
        <sidebar class="sidebar-container"/>
        <div class="main-container">
          <navbar/>
          <tags-view/>
          <app-main/>
        </div>
      </div>
    </div>
    <app-main v-if="!IsMenuShow"/>
  </div>
</template>
<!--导入页面组成部分，侧边菜单，导航栏，主页面。
混入具有响应式功能的基类，即可根据浏览器窗口大小来判断是否展示侧边菜单。-->
<script>
  import { TopNavbar, Navbar, Sidebar, AppMain, TagsView } from './components'
  import ResizeMixin from './mixin/ResizeHandler'

  export default {
    name: 'Layout',
    components: {
      TopNavbar,
      Navbar,
      Sidebar,
      AppMain,
      TagsView
    },
    mixins: [ResizeMixin],
    computed: {
      IsMenuShow() {
        var sl = this.$route.query.sl
        return sl === undefined || sl === 'false'
      },
      sidebar() {
        return this.$store.state.app.sidebar
      },
      device() {
        return this.$store.state.app.device
      },
      classObj() {
        return {
          hideSidebar: !this.sidebar.opened,
          openSidebar: this.sidebar.opened,
          withoutAnimation: this.sidebar.withoutAnimation,
          mobile: this.device === 'mobile'
        }
      }
    },
    methods: {
      handleClickOutside() {
        this.$store.dispatch('CloseSideBar', { withoutAnimation: true })
      }
    }
  }
</script>
<!--类似面向对象的继承机制，定义一个基类，继承自基类，可以复用代码-->
<style rel="stylesheet/scss" lang="scss" scoped>
  @import "src/styles/mixin.scss";

  .app-wrapper {
    @include clearfix;
    position: relative;
    height: 100%;
    width: 100%;

    &.mobile.openSidebar {
      position: fixed;
      top: 0;
    }
  }

  .drawer-bg {
    background: #000;
    opacity: 0.3;
    width: 100%;
    top: 0;
    height: 100%;
    position: absolute;
    z-index: 999;
  }
</style>
