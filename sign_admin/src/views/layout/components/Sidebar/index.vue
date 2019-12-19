<!--// 侧边菜单栏，根据路由文件传递相关属性给组件sidebarItem来进行渲染-->
<template>
  <el-scrollbar wrap-class="scrollbar-wrapper" style="height: calc(100% - 63px)">
    <el-menu
      :show-timeout="0"
      :default-active="$route.path"
      :collapse="isCollapse"
      :unique-opened="false"
      class="sidebar-menu-ct"
      mode="vertical"
      background-color="#EEEEEE"
      text-color="#444444"
      active-text-color="#444444">
      <sidebar-item v-for="route in routes" :key="route.name" :item="route" :base-path="route.path" />

    </el-menu>
  </el-scrollbar>
</template>

<script>
import { mapGetters } from 'vuex'
import SidebarItem from './SidebarItem'

export default {
  components: { SidebarItem },
  computed: {
    ...mapGetters([
      'sidebar'
    ]),
    routes() {
      return this.$router.options.routes
    },
    isCollapse() {
      return !this.sidebar.opened
    }
  }
}
</script>
