const getters = {
  //做了函数映射，函数直接返回相应的数据。
  //sidebar 函数名    :后面是方法体
  sidebar: state => state.app.sidebar,
  device: state => state.app.device,
  structureLength: state => state.app.structureLength,
  currentPici: state => state.app.currentPici,
  web_site_name: state => state.user.web_site_name,
  logo_image_url: state => state.user.logo_image_url,
  token: state => state.user.token,
  username: state => state.user.username,
  rules: state => state.user.rules,
  visitedViews: state => state.tagsView.visitedViews,
  cachedViews: state => state.tagsView.cachedViews
}
export default getters
