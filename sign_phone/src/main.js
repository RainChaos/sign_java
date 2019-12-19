// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import VueRouter from 'vue-router'
import App from './App'
import { ToastPlugin, LoadingPlugin, WechatPlugin, AlertPlugin,ConfirmPlugin } from 'vux'

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/zh-CN' // lang i18n

import router from './router'
import './styles/element-ui.css'
import './styles/index.css'
import store from './store'


// FastClick.prototype.focus = function (targetElement) {
//     let length;
//     if (targetElement.setSelectionRange && targetElement.type.indexOf('date') !== 0 && targetElement.type !== 'time' && targetElement.type !== 'month') {
//         // console.log(targetElement)
//         length = targetElement.value.length;
//         targetElement.focus();
//         targetElement.setSelectionRange(length, length);
//     } else {
//         // console.log(targetElement)
//         targetElement.focus();
//     }
// };

Vue.use(VueRouter)
Vue.use(ToastPlugin)
Vue.use(LoadingPlugin)
Vue.use(AlertPlugin)
Vue.use(ConfirmPlugin)
Vue.use(WechatPlugin)

Vue.use(ElementUI, { locale })
Vue.prototype.$devicePixelRatio = 2
// FastClick.attach(document.body)
Vue.config.productionTip = false
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app-box')

//main.js
// router.afterEach((to, from)=>{
//
//   var reloaded = window.localStorage.getItem('reloaded') || '';
//   console.log(reloaded)
//   if(reloaded == ''&&to.name == 'PayCharge'){
//     alert('刷新此页面')
//     // console.log('reload')
//     // window.localStorage.setItem('reloaded','yes');
//     // window.location.reload();
//
//   }
// })
