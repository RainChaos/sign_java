export const ChargePay = {
  IsWeiXin() {
    var ua = window.navigator.userAgent.toLowerCase()
    if (ua.match(/MicroMessenger/i) === 'micromessenger') {
      return true
    } else {
      return false
    }
  },
  callpay(wxJsApiParam, redirect) {
    if (typeof WeixinJSBridge === 'undefined') {
      if (document.addEventListener) {
        document.addEventListener('WeixinJSBridgeReady', this.jsApiCall, false)
      } else if (document.attachEvent) {
        document.attachEvent('WeixinJSBridgeReady', this.jsApiCall)
        document.attachEvent('onWeixinJSBridgeReady', this.jsApiCall)
      }
    } else {
      this.jsApiCall(wxJsApiParam, redirect)
    }
  },
  // 公众号支付 调用微信JS api 支付
  jsApiCall(wxJsApiParam, redirect) {
    WeixinJSBridge.invoke(
      'getBrandWCPayRequest',
      wxJsApiParam, // josn串
      function(res) {
        WeixinJSBridge.log(res.err_msg)
        if (res.err_msg === 'get_brand_wcpay_request:ok') {
          window.location.href = redirect
        }
      }
    )
  }
}
