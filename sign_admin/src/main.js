import Vue from 'vue'

import 'babel-polyfill'  //转码器,可以将ES6代码转为ES5代码
import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui' //饿了么前端团队推出的一款基于Vue.js 2.0 的桌面端UI框架

import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

/*
Sass是成熟、稳定、强大的CSS预处理器，
而SCSS是Sass3版本当中引入的新语法特性，
完全兼容CSS3的同时继承了Sass强大的动态功能。
CSS书写代码规模较大的Web应用时，容易造成选择器、
层叠的复杂度过高，因此推荐通过SASS预处理器进行CSS的开发
，SASS提供的变量、嵌套、混合、继承等特性，让CSS的书写更加有趣与程式化。
 */
import '@/styles/index.scss' // global css

// import 'font-awesome/css/font-awesome.min.css'
import App from './App'
import router from './router'
import store from './store'

import '@/icons' // icon
import '@/permission' // permission control

Vue.use(ElementUI, { locale })

import './utils/directives.js'


Vue.config.productionTip = false
/*render: h => h(App) 是下面内容的缩写：
render: function (createElement) {
  return createElement(App);
}
进一步缩写为(ES6 语法)：
render (createElement) {
  return createElement(App);
}
再进一步缩写为：
render (h){
   return h(App);
}
按照 ES6 箭头函数的写法，就得到了：
render: h => h(App);

*/
new Vue({
  el: '#app',
  router,
  store,
  render: h => h(App)
})
