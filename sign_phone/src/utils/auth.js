import Cookies from 'js-cookie'
import store from '@/store'

const TokenKey = 'Home-Sign-Token'
export function setCurrentUrl(url){
  return Cookies.set("CurrentUrl", url)
}
export function getCurrentUrl() {
  return Cookies.get("CurrentUrl")
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

export function getQueryVariable(variable)
{
  var query = window.location.search.substring(1);
  var vars = query.split("&");
  for (var i=0;i<vars.length;i++) {
    var pair = vars[i].split("=");
    if(pair[0] == variable){return pair[1];}
  }
  return(false);
}
