<template>
  <div v-if="!item.hidden && item.children.length" class="menu-wrapper">
    <!--// 如果只有一个子菜单，就展示该菜单项即可，例如form和external link-->
    <template v-if="hasOneShowingChild(item.children,item) && (!onlyOneChild.children||onlyOneChild.noShowingChildren)&&!item.alwaysShow">
      <a :href="onlyOneChild.path" target="_blank" @click="clickLink(onlyOneChild.path,$event)">
        <el-menu-item :index="resolvePath(onlyOneChild.path)" :class="{'submenu-title-noDropdown':!isNest}">
          <svg-icon v-if="item.children[0].meta&&item.children[0].meta.icon" :icon-class="item.children[0].meta.icon"/>
          <item v-if="onlyOneChild.meta" :title="onlyOneChild.meta.title" />
        </el-menu-item>
      </a>
    </template>
    <!--// 如果有多个子菜单，如example和nested，先展示父菜单-->
    <el-submenu v-else :index="item.name||item.path">
     <!-- // 展示父菜单，这里的slot=title是element-ui里定义的具名slot-->
      <template slot="title">
        <svg-icon style="" v-if="item.meta&&item.meta.icon" :icon-class="item.meta.icon"/>
        <item v-if="item.meta" :title="item.meta.title" />
      </template>
      <template v-for="child in item.children" v-if="!child.hidden">
       <!-- // 如果有子菜单，则递归展示sidebar-item-->
        <sidebar-item
          v-if="child.children && child.children.length>0"
          :is-nest="true"
          :item="child"
          :key="child.path"
          :base-path="resolvePath(child.path)"
          class="nest-menu" />
      <!--  // 否则展示为一个普通链接即可-->
        <a v-else :href="child.path" :key="child.name" target="_blank" @click="clickLink(child.path,$event)">
          <el-menu-item :index="resolvePath(child.path)">
            <svg-icon style="width: 8px" v-if="child.meta&&child.meta.icon" :icon-class="child.meta.icon"/>
            <item v-if="child.meta" :title="child.meta.title" />
          </el-menu-item>
        </a>
      </template>
    </el-submenu>
  </div>
</template>

<script>
import path from 'path'
import { validateURL } from '@/utils/validate'
import Item from './Item'

export default {
  name: 'SidebarItem',
  components: { Item },
  props: {
    // route object
    item: {
      type: Object,
      required: true
    },
    isNest: {
      type: Boolean,
      default: false
    },
    basePath: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      onlyOneChild: null
    }
  },
  methods: {
    hasOneShowingChild(children, parent) {
      const showingChildren = children.filter(item => {
        if (item.hidden) {
          return false
        } else {
          //   set(will be used if only has one showing child)
          this.onlyOneChild = item
          return true
        }
      })

      // When there is only one child router, the child router is displayed by default
      if (showingChildren.length === 1) {
        return true
      }

      // Show parent if there are no child router to display
      if (showingChildren.length === 0) {
        this.onlyOneChild = { ... parent, path: '', noShowingChildren: true }
        return true
      }

      return false
    },
    resolvePath(routePath) {
      return path.resolve(this.basePath, routePath)
    },
    isExternalLink(routePath) {
      return validateURL(routePath)
    },
    clickLink(routePath, e) {
      if (!this.isExternalLink(routePath)) {
        e.preventDefault()
        const path = this.resolvePath(routePath)
        this.$router.push(path)
      }
    }
  }
}
</script>
