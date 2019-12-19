import Cookies from 'js-cookie'
import store from '@/store'

const TokenKey = 'Admin-Sign-Token'

export function getToken() {
  return Cookies.get(TokenKey)
}

export function setToken(token) {
  return Cookies.set(TokenKey, token)
}

export function removeToken() {
  return Cookies.remove(TokenKey)
}

export function vilidateBtn(url) {
  return store.getters.rules.indexOf(url) > -1
}
