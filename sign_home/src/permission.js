import router from './router'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { getWebSiteName } from '@/api/main'
router.beforeEach((to, from, next) => {
  NProgress.start()
  next()
})

router.afterEach(() => {
  getWebSiteName().then(response => {
    document.title = response.data + '报名平台'
  })
  NProgress.done() // 结束Progress
})
