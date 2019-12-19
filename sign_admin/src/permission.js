import router from './router'
import store from './store'
import NProgress from 'nprogress' // Progress 进度条
import 'nprogress/nprogress.css'// Progress 进度条样式
import { Message } from 'element-ui'
import { getToken } from '@/utils/auth' // 验权

const whiteList = ['/login','/sso'] // 不重定向白名单

router.beforeEach((to, from, next) => {
  NProgress.start()
  if (getToken()) {
    if (to.path === '/login') {
      next({ path: '/' })
      NProgress.done() // if current page is main will not trigger	afterEach hook, so manually handle it
    } else {
      if (store.getters.rules.length === 0) {
        store.dispatch('GetInfo').then(res => { // 拉取用户信息
          store.dispatch('StructureLength').then(res2 => {
            store.dispatch('CurrentPici').then(res3 => {
              var routes = router.options.routes
              routes.forEach(one => {
                if (one.children) {
                  var onepath = one.path === '/' ? '' : one.path
                  var isHidden = 0
                  one.children.forEach(two => {
                    var twopath = onepath + '/' + two.path
                    if (store.getters.rules.indexOf(twopath) === -1 && !two.hidden) {
                      two.hidden = true
                    }
                    if (two.hidden === true) {
                      isHidden++
                    }
                  })
                  one.hidden = isHidden === one.children.length
                }
              })
              if (to.path === '/') {
                next({ path: '/main' })
              } else {
                next({ ...to, replace: true })
              }
            }).catch(error => { console.log(error) })
          }).catch(error => { console.log(error) })
        }).catch((err) => {
          store.dispatch('FedLogOut').then(() => {
            Message.error(err || '验证失败，请重新登陆')
            next({ path: '/' })
          }).catch(error => { console.error(error) })
        })
      } else {
        if (to.path === '/') {
          next({ path: '/main' })
        } else {
          next()
        }
      }
    }
  } else {
    if (whiteList.indexOf(to.path) !== -1) {
      next()
    } else {
      next(`/login?redirect=${to.path}`) // 否则全部重定向到登录页
      NProgress.done()
    }
  }
})

router.afterEach(() => {
  NProgress.done() // 结束Progress
})
