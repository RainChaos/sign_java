import Vue from 'vue'
import Router from 'vue-router'
import Layout from '../views/layout/Layout'
import TLayout from '../views/layout/TLayout'

Vue.use(Router)

export const constantRouterMap = [
  {
    path: '/',
    component: Layout,
    children: [{
        path: '/',
        name: 'Index',
        component: () => import('@/views/step/Index')
      }]
  },
  {
    path: '/login',
    name: 'TeacherLogin',
    component: () => import('@/views/operator/TLogin')
  },
  {
    path: '/',
    component: TLayout,
    children: [
      {
        path: '/t/zhuanyeCount',
        name: 'ZhuanyeCount',
        component: () => import('@/views/operator/zhuanyeCount')
      },
      {
        path: '/t/banjiCount',
        name: 'BanjiCount',
        component: () => import('@/views/operator/banjiCount')
      },
      {
        path: '/t/studentLists',
        name: 'StudentLists',
        component: () => import('@/views/operator/TStudentLists')
      },
      {
        path: '/t/studentBanji',
        name: 'studentBanji',
        component: () => import('@/views/operator/TStudentBanji')
      },
        {
            path: '/t/studentInfo',
            name: 'StudentInfo',
            component: () => import('@/views/operator/TStudentInfo')
        },
{
    path: '/t/TStudentTzbjInfo',
        name: 'TStudentTzbjInfo',
    component: () => import('@/views/operator/TStudentTzbjInfo')
}
    ]
  },

  {
    path: '/search',
    name: 'SearchStudent',
    component: () => import('@/views/step/SearchStudent')
  },
  {
    path: '/add',
    name: 'AddStudent',
    component: () => import('@/views/step/AddStudent'),
      meta: { content: 'user-scalable=no'}
  },
  {
    path: '/edit',
    name: 'EditStudent',
    component: () => import('@/views/step/EditStudent'),
      meta: { name: "viewport", content: 'user-scalable=no'}
  },
  {
    path: '/ldedit',
    name: 'LdEditStudent',
    component: () => import('@/views/step/LdEditStudent')
  },
  {
    path: '/pay',
    name: 'PayCharge',
    component: () => import('@/views/step/PayCharge'),
  },
    {
        path: '/prepay',
        name: 'PrePayCharge',
        component: () => import('@/views/step/PrePayCharge'),
    },
  {
    path: '/ruxuexuzhi',
    name: 'ruxuexuzhi',
    component: () => import('@/views/step/ruxuexuzhi')
  },
    {
        path: '/finish',
        name: 'SignFinish',
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
  },

    {
        path: '/getNotice',
        name: 'GetNotice',
        component: () => import('@/views/step/GetNotice')
    },
    {
        path: '/Notice',
        name: 'Notice',
        component: () => import('@/views/step/Notice')
    },
    {
        path: '/contactOnline',
        name: 'ContactOnline',
        component: () => import('@/views/step/ContactOnline')
    },
    {
        path: '/admissionInfo',
        name: 'AdmissionInfo',
        component: () => import('@/views/step/AdmissionInfo'),
        meta: { content: 'user-scalable=no'}
    }
]

export default new Router({
  mode: 'history', // 后端支持可开 /operator/
  base: '/phone',
  routes: constantRouterMap
})
