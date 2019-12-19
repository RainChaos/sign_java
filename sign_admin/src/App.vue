<!--//主页面的渲染载体，只需包含一个router-view元素即可，不同路由对应的组件都会在这个页面进行渲染展示。-->
<template>
<div id="app">
  <!--//vue组件切换时使用过渡(transition)-->
  <transition name="fade-transform" mode="out-in">
    <!--当组件在keep-alive内被切换时
    组件的activated、deactivated这两个生命周期钩子函数会被执行
    被包裹在keep-alive中的组件的状态将会被保留，
    例如我们将某个列表类组件内容滑动到第100条位置，
    那么我们在切换到一个组件后再次切换回到该组件，
    该组件的位置状态依旧会保持在第100条列表处
-->
    <keep-alive >
      <router-view v-if="$route.meta.keepAlive"/>
    </keep-alive>
  </transition>
  <transition name="fade-transform" mode="out-in">
    <router-view v-if="!$route.meta.keepAlive"/>
  </transition>
</div>
</template>

<script>
export default {
  // name 属性，组件的全局唯一ID  类型：string  限制：只有作为组件选项时起作用。
  name: 'App'
}
</script>
<style>
#nprogress .bar {
  background: lightskyblue !important;
}

#nprogress .peg {
  box-shadow: 0 0 10px lightskyblue, 0 0 5px lightskyblue;
  opacity: 1.0;
}


*::-webkit-scrollbar {
  width: 7px;
  height: 7px;
  background-color: transparent;
}

*::-webkit-scrollbar-track {
  background-color: white;
}

*::-webkit-scrollbar-thumb {
  background-color: #DDDEE0;
  border-radius: 3px;
}

.scrollbarHide::-webkit-scrollbar {
  display: none
}

.scrollbarShow::-webkit-scrollbar {
  display: block
}
.table-fixed {
  .el-table__fixed-right {
  height: 100% !important;
  }
}

body .el-table th.gutter{
    display: table-cell!important;
}
</style>
