import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
import Layout from '../views/layout/Layout'
export const constantRouterMap = [
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '/',
    component: Layout,
    children: [
      {
        path: '/',
        name: 'SearchStudent',
        component: () => import('@/views/step/SearchStudent')
      },
      {
        path: '/add',
        name: 'AddStudent',
        component: () => import('@/views/step/AddStudent')
      },
      {
        path: '/edit',
        name: 'EditStudent',
        component: () => import('@/views/step/EditStudent')
      },
      {
        path: '/ldedit',
        name: 'LdEditStudent',
        component: () => import('@/views/step/LdEditStudent')
      },
      {
        path: '/pay',
        name: 'PayCharge',
        component: () => import('@/views/step/PayCharge')
      },
      {
        path: '/finish',
        name: 'Finish',
        component: () => import('@/views/step/SignFinish')
      },
      {
        path: '/jianjie/lists',
        name: 'JianJieLists',
        component: () => import('@/views/jianjie/Lists')
      },
      {
        path: '/jianjie/detail',
        name: 'JianJieDetail',
        component: () => import('@/views/jianjie/Detail')
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  mode: 'history', // 后端支持可开
  base: '/home/',
  routes: constantRouterMap
})
