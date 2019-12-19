import Layout from '../views/layout/Layout'
export const OperatorRouter = {
  path: '/batch',
  component: Layout,
  hidden: false,
  redirect: 'noredirect',
  meta: { title: '批次管理', icon: 'fa fa-user fa-fw', hidden: false },
  children: [
    {
      path: 'batch',
      name: 'Batch',
      hidden: false,
      component: () => import('@/views/batch/batch'),
      meta: { title: '报名批次管理', icon: 'fa fa-circle fa-fw', hidden: false }
    },
    {
      path: 'selectUnit',
      name: 'SelectUnit',
      hidden: false,
      component: () => import('@/views/batch/selectUnit'),
      meta: { title: '可选单位专业', icon: 'fa fa-circle fa-fw', hidden: false }
    },
    {
      path: 'selectUnitClass',
      name: 'SelectUnitClass',
      hidden: false,
      component: () => import('@/views/batch/selectUnitClass'),
      meta: { title: '可选单位班级', icon: 'fa fa-circle fa-fw', hidden: false }
    }
  ]
}
