//保证对token的操作函数

import Cookies from 'js-cookie'
import store from '@/store'

const TokenKey = 'Admin-Sign-Token'
 //path : auth
const authUrl = {
  "/main":"/main",
  "/system":"/system",
  "/system/operator":"/system/operator",
  "/system/authRule":"/system/authRule",
  "/system/authGroup":"/system/authGroup",
  "/article":"/article",
  "/article/articleCategory":"/article/articleCategory",
  "/article/articleList":"/article/articleList",
  "/article/editArticle":"/article/articleList",
  "/system/config":"/system/config",
  "/batch":"/batch",
  "/batch/batch":"/batch/batch",
  "/batch/selectUnit":"/batch/selectUnit",
  "/batch/selectUnitClass":"/batch/selectUnitClass",
  "/student":"/student",
  "/setting":"/setting",
  "/fenban":"/fenban",
  "/tzs":"/tzs",
  "/tongji":"/tongji",
  "/basedata":"/basedata",
  "/student/addStudent":"/student/addStudent",
  "/student/import":"/student/import",
  "/student/studentLists":"/student/studentLists",
  "/setting/unit":"/setting/unit",
  "/setting/setStudentGrid":"/setting/setStudentGrid",
  "/setting/setStudentForm":"/setting/setStudentForm",
  "/setting/studentSearchField":"/setting/studentSearchField",
  "/setting/stuField":"/setting/stuField",
  "/setting/showChargeItem":"/setting/showChargeItem",
  "/fenban/autoFenban":"/fenban/autoFenban",
  "/fenban/fenbanIndex":"/fenban/fenbanIndex",
  "/tzs/tzsField":"/tzs/tzsField",
  "/tzs/tzsmb":"/tzs/tzsmb",
  "/tzs/printTzs":"/tzs/printTzs",
  "/basedata/dictionaryType":"/basedata/dictionaryType",
  "/studentCount/unitCount":"/studentCount/unitCount",
  "/basedata/dictionary":"/basedata/dictionary",
  "/basedata/region":"/basedata/region",
  "/basedata/middleSchool":"/basedata/middleSchool",
  "/system/setconfig":"/system/setconfig",
  "/studentCount/distCount":"/studentCount/distCount",
  "/student/yingxin":"/student/yingxin",
  "/student/dorLists":"/student/dorLists",
  "/studentCount/zsrCount":"/studentCount/zsrCount",
  "/student/ldCheckLists":"/student/ldCheckLists",
  "/setting/gridSearchField":"/setting/gridSearchField",
  "/logs/actionlog":"/logs/actionlog",
  "/logs/studentLog":"/logs/studentLog",
  "/system/backupList":"/system/backupList",
  "/fenban/xueji":"/fenban/xueji",
  "/student/ldCheckStudent":"/student/ldCheckLists",
  "/student/editStudent":"/student/studentLists",
  "/system/assignOperatorRules":"/system/operator",
  "/system/assignGroupRules":"/system/authGroup",
  "/system/configForm":"/system/config"
}

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

export function setCurrentUrl(url){
  return Cookies.set("CurrentUrl", url)
}

export function getCurrentUrl() {
  let url = window.location.pathname
  let path = url.replace('/operator', '')
  return authUrl[path]
}
