import Vue from 'vue'
import Router from 'vue-router'
Vue.use(Router)
import Layout from '../views/layout/Layout'
export const constantRouterMap = [
  { path: '/login', component: () => import('@/views/login/index'), hidden: true },
  { path: '/sso', component: () => import('@/views/sso/index'), hidden: true },
  { path: '/404', component: () => import('@/views/404'), hidden: true },
  {
    path: '/',
    component: Layout,
    hidden: false,
    children: [{
      path: 'main',
      name: 'Main',
      hidden: false,
      component: () => import('@/views/main/index'),
      meta: { title: '系统首页', icon: 'index', hidden: false,keepAlive:true }
    },
    {
      hidden: true,
      path: '/redirect/:path*',
      name: 'Redirect',
      component: () => import('@/views/redirect/index'),
      meta: { title: '重新跳转', hidden: true }
    }
    ]
  },
  {
    path: '/batch',
    component: Layout,
    hidden: false,
    redirect: 'noredirect',
    meta: { title: '批次管理', icon: 'paramManagement', hidden: false },
    children: [
      {
        path: 'batch',
        name: 'Batch',
        hidden: false,
        component: () => import('@/views/batch/batch'),
        meta: { title: '报名批次管理', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'selectUnit',
        name: 'SelectUnit',
        hidden: false,
        component: () => import('@/views/batch/selectUnit'),
        meta: { title: '可选单位专业', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'selectUnitClass',
        name: 'SelectUnitClass',
        hidden: false,
        component: () => import('@/views/batch/selectUnitClass'),
        meta: { title: '可选单位班级', icon: 'circle', hidden: false,keepAlive:true }
      }
    ]
  },
  {
    path: '/student',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '学生管理', icon: 'student', hidden: false },
    children: [
      {
        path: 'yingxin',
        name: 'Yingxin',
        hidden: false,
        component: () => import('@/views/student/yingxin'),
        meta: { title: '现场迎新', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'studentLists',
        name: 'StudentLists',
        hidden: false,
        component: () => import('@/views/student/studentLists'),
        meta: { title: '学生信息', icon: 'circle', hidden: false, keepAlive:true }
      },
      {
        path: 'ldCheckLists',
        name: 'LdCheckLists',
        hidden: false,
        component: () => import('@/views/student/LdCheckLists'),
        meta: { title: '预备制审核', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'dorLists',
        name: 'dorLists',
        hidden: false,
        component: () => import('@/views/student/dorLists'),
        meta: { title: '入住公寓', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'addStudent',
        name: 'addStudent',
        hidden: false,
        component: () => import('@/views/student/addStudent'),
        meta: { title: '新增学生', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'editStudent',
        name: 'editStudent',
        hidden: true,
        component: () => import('@/views/student/editStudent'),
        meta: { title: '编辑学生', icon: 'circle', hidden: true,keepAlive:true }
      },
      {
        path: 'ldCheckStudent',
        name: 'LdCheckStudent',
        hidden: true,
        component: () => import('@/views/student/LdCheckStudent'),
        meta: { title: '审核劳动预备制', icon: 'circle', hidden: true,keepAlive:true }
      },
      {
        path: 'import',
        name: 'Import',
        hidden: false,
        component: () => import('@/views/student/import'),
        meta: { title: '导入学生', icon: 'circle', hidden: false ,keepAlive:true}
      }
    ]
  },
  {
    path: '/setting',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '基础设置', icon: 'setting', hidden: false },
    children: [
      {
        path: 'unit',
        name: 'Unit',
        hidden: false,
        component: () => import('@/views/student/unit'),
        meta: { title: '单位管理', icon: 'circle', hidden: false,keepAlive:true}
      },
      {
        path: 'studentSearchField',
        name: 'StudentSearchField',
        hidden: false,
        component: () => import('@/views/studentField/studentSearchField'),
        meta: { title: '查询条件', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'gridSearchField',
        name: 'gridSearchField',
        hidden: false,
        component: () => import('@/views/studentField/gridSearchField'),
        meta: { title: '表格查询', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'setStudentGrid',
        name: 'setStudentGrid',
        hidden: false,
        component: () => import('@/views/studentField/setStudentGrid'),
        meta: { title: '表格字段', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'setStudentForm',
        name: 'SetStudentForm',
        hidden: false,
        component: () => import('@/views/studentField/setStudentForm'),
        meta: { title: '表单设计', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'stuField',
        name: 'StuField',
        hidden: false,
        component: () => import('@/views/studentField/studentField'),
        meta: { title: '字段管理', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'showChargeItem',
        name: 'showChargeItem',
        hidden: false,
        component: () => import('@/views/studentField/showChargeItem'),
        meta: { title: '收费项目', icon: 'circle', hidden: false ,keepAlive:true}
      }
    ]
  },
  {
    path: '/fenban',
    component: Layout,
    hidden: false,
    redirect: 'noredirect',
    meta: { title: '分班管理', icon: 'fenban', hidden: false },
    children: [
      {
        path: 'autoFenban',
        name: 'AutoFenban',
        hidden: false,
        component: () => import('@/views/fenban/autoFenban'),
        meta: { title: '一键分班', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'fenbanIndex',
        name: 'FenbanIndex',
        hidden: false,
        component: () => import('@/views/fenban/index'),
        meta: { title: '分班管理', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'xueji',
        name: 'Xueji',
        hidden: false,
        component: () => import('@/views/fenban/xueji'),
        meta: { title: '学籍异动', icon: 'circle', hidden: false ,keepAlive:true}
      }
    ]
  },
  {
    path: '/tzs',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '通知书管理', icon: 'tzs', hidden: false },
    children: [
      {
        path: 'tzsField',
        name: 'tzsField',
        hidden: false,
        component: () => import('@/views/tzs/tzsField'),
        meta: { title: '打印项管理', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'tzsmb',
        name: 'Tzsmb',
        hidden: false,
        component: () => import('@/views/tzs/tzsmb'),
        meta: { title: '通知书模板', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'printTzs',
        name: 'PrintTzs',
        hidden: false,
        component: () => import('@/views/tzs/printTzs'),
        meta: { title: '打印通知书', icon: 'circle', hidden: false ,keepAlive:true}
      }
    ]
  },
  {
    path: '/studentCount',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '统计管理', icon: 'count', hidden: false },
    children: [
      {
        path: 'unitCount',
        name: 'unitCount',
        hidden: false,
        component: () => import('@/views/studentCount/unitCount'),
        meta: { title: '单位统计', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'distCount',
        name: 'DistCount',
        hidden: false,
        component: () => import('@/views/studentCount/distCount'),
        meta: { title: '地区统计', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'zsrCount',
        name: 'ZsrCount',
        hidden: false,
        component: () => import('@/views/studentCount/zsrCount'),
        meta: { title: '招生人统计', icon: 'circle', hidden: false ,keepAlive:true}
      }
    ]
  },
  {
    path: '/basedata',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '基础数据', icon: 'data', hidden: false },
    children: [
      {
        path: 'dictionaryType',
        name: 'DictionaryType',
        hidden: false,
        component: () => import('@/views/basedata/dictionaryType'),
        meta: { title: '数据分类', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'dictionary',
        name: 'Dictionary',
        hidden: false,
        component: () => import('@/views/basedata/dictionary'),
        meta: { title: '标准数据', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'region',
        name: 'Region',
        hidden: false,
        component: () => import('@/views/basedata/region'),
        meta: { title: '省份市区', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'middleSchool',
        name: 'MiddleSchool',
        hidden: false,
        component: () => import('@/views/basedata/middleSchool'),
        meta: { title: '中学数据', icon: 'circle', hidden: false,keepAlive:true }
      }
    ]
  },
  {
    path: '/system',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '系统管理', icon: 'settings', hidden: false },
    children: [
      {
        path: 'operator',
        name: 'Operator',
        hidden: false,
        component: () => import('@/views/operator/index'),
        meta: { title: '用户管理', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'assignOperatorRules',
        name: 'AssignOperatorRules',
        hidden: true,
        component: () => import('@/views/operator/assignOperatorRules'),
        meta: { title: '用户权限设置', icon: 'circle', hidden: true ,keepAlive:true}
      },
      {
        path: 'authGroup',
        name: 'AuthGroup',
        hidden: false,
        component: () => import('@/views/authGroup/index'),
        meta: { title: '用户角色', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'assignGroupRules',
        name: 'AssignGroupRules',
        hidden: true,
        component: () => import('@/views/authGroup/assignGroupRules'),
        meta: { title: '角色权限设置', icon: 'circle', hidden: true ,keepAlive:true}
      },
      {
        path: 'authRule',
        name: 'AuthRule',
        hidden: false,
        component: () => import('@/views/authRule/authRule'),
        meta: { title: '权限管理', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        hidden: false,
        path: 'setconfig',
        name: 'SetConfig',
        component: () => import('@/views/config/setconfig'),
        meta: { title: '参数设置', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'config',
        name: 'Config',
        hidden: false,
        component: () => import('@/views/config/index'),
        meta: { title: '参数管理', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'configForm',
        name: 'ConfigForm',
        hidden: true,
        component: () => import('@/views/config/form'),
        meta: { title: '新增配置项', icon: 'circle', hidden: true,keepAlive:true }
      },
      {
        path: 'backupList',
        name: 'backupList',
        hidden: false,
        component: () => import('@/views/backup/backupList'),
        meta: { title: '数据库备份', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'restoreList',
        name: 'restoreList',
        hidden: false,
        component: () => import('@/views/backup/restoreList'),
        meta: { title: '数据库还原', icon: 'circle', hidden: false,keepAlive:true }
      }
    ]
  },
  {
    path: '/article',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '文章管理', icon: 'article', hidden: false },
    children: [
      {
        path: 'createArticle',
        name: 'CreateArticle',
        hidden: false,
        component: () => import('@/views/article/articleForm'),
        meta: { title: '创建文章', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'editArticle',
        name: 'EditArticle',
        hidden: true,
        component: () => import('@/views/article/articleForm'),
        meta: { title: '编辑文章', icon: 'circle', hidden: true ,keepAlive:true}
      },
      {
        path: 'articleCategory',
        name: 'ArticleCategory',
        hidden: false,
        component: () => import('@/views/article/articleCategory'),
        meta: { title: '文章分类', icon: 'circle', hidden: false ,keepAlive:true}
      },
      {
        path: 'articleList',
        name: 'ArticleList',
        hidden: false,
        component: () => import('@/views/article/index'),
        meta: { title: '文章列表', icon: 'circle', hidden: false ,keepAlive:true}
      }
    ]
  },
  {
    path: '/logs',
    component: Layout,
    redirect: 'noredirect',
    hidden: false,
    meta: { title: '系统日志', icon: 'log', hidden: false },
    children: [
      {
        path: 'actionlog',
        name: 'ActionLog',
        hidden: false,
        component: () => import('@/views/logs/actionlog'),
        meta: { title: '操作日志', icon: 'circle', hidden: false,keepAlive:true }
      },
      {
        path: 'studentLog',
        name: 'StudentLog',
        hidden: false,
        component: () => import('@/views/logs/studentLog'),
        meta: { title: '学生日志', icon: 'circle', hidden: false,keepAlive:true }
      }
    ]
  },
  { path: '*', redirect: '/404', hidden: true }
]

export default new Router({
  mode: 'history', // 后端支持可开 /operator/
  base: '/operator',
  routes: constantRouterMap,

})
