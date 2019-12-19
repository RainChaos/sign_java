/**
 * @desc 基于ElementUI Form rules 进行使用, 参考官方文档: http://element-cn.eleme.io/#/zh-CN/component/form [自定义校验规则]
 * @example  rules: {
 *  ip: [
 *    {required: true, message: '请输入矩阵IP', trigger: 'blur'},
 *    {validator: this.$rules.ipAddress, trigger: 'blur'}
 *  ]
 * }
 *
 */
export const formValidater = {

  /**
   * 校验 包括中文字、英文字母、数字和下划线
   * 登录账号校验
   */
  validateAccount(rule, value, callback) {
    const acount = /^(?!_)(?!.*?_$)[a-zA-Z0-9_\u4e00-\u9fa5]+$/
    if (value && (!(acount).test(value))) {
      callback(new Error('账号不符合规范'))
    } else {
      callback()
    }
  },

  /**
   * IP地址校验
   */
  ipAddress(rule, value, callback) {
    if (value && value === '...') {
      callback(new Error('请输入IP地址'))
    }
    if (value && !(/((25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))\.){3}(25[0-5]|2[0-4]\d|((1\d{2})|([1-9]?\d)))/).test(value)) {
      callback(new Error('IP地址不符合规范'))
    } else {
      callback()
    }
  },

  /**
   * 手机号码校验
   */
  phoneNumber(rule, value, callback) {
    if (value && (!(/^[1][3456789]\d{9}$/).test(value) || !(/^[1-9]\d*$/).test(value) || value.length !== 11)) {
      callback(new Error('手机号码不符合规范'))
    } else {
      callback()
    }
  },

  /**
   * 电话号码校验
   */
  telephoneNumber(rule, value, callback) {
    if (value && (!(/^((0\d{2,3}-\d{7,8})|(1[3456789]\d{9}))$/).test(value))) {
      callback(new Error('电话号码不符合规范'))
    } else {
      callback()
    }
  },

  /**
   * 邮箱校验
   */
  emailValue(rule, value, callback) {
    const temp = /^[\w.\-]+@(?:[a-z0-9]+(?:-[a-z0-9]+)*\.)+[a-z]{2,3}$/
    // const tempOne = /^[A-Za-zd]+([-_.][A-Za-zd]+)*@([A-Za-zd]+[-.])+[A-Za-zd]{2,5}$/
    if (value && (!(temp).test(value))) {
      callback(new Error('邮箱格式不符合规范'))
    } else {
      callback()
    }
  },

  /**
   * 正整数校验
   */
  integerP(rule, value, callback) {
    if (value && !(/^[1-9]\d*$/).test(value)) {
      callback(new Error('只能填写正整数'))
    } else {
      callback()
    }
  },

  /**
   * 验证身份证号码
   * @param rule
   * @param value
   * @param callback
   */
  checkdIdCard(rule, value, callback) {
    var cityArr = {
      11: '北京',
      12: '天津',
      13: '河北',
      14: '山西',
      15: '内蒙古',
      21: '辽宁',
      22: '吉林',
      23: '黑龙江',
      31: '上海',
      32: '江苏',
      33: '浙江',
      34: '安徽',
      35: '福建',
      36: '江西',
      37: '山东',
      41: '河南',
      42: '湖北',
      43: '湖南',
      44: '广东',
      45: '广西',
      46: '海南',
      50: '重庆',
      51: '四川',
      52: '贵州',
      53: '云南',
      54: '西藏',
      61: '陕西',
      62: '甘肃',
      63: '青海',
      64: '宁夏',
      65: '新疆'
    }
    //, 71: "台湾", 81: "香港", 82: "澳门", 91: "国外"
    const reg = /(^[A-Z][1,2]\d{8})$|(^\d{15}$)|(^\d{17}([0-9]|X)$)/i
    if (!reg.test(value)) {
      callback(new Error('身份证格式不正确'))
    }
    if(value.length == 10){

    }else{
      if (cityArr[parseInt(value.substr(0, 2))] == null) {
        callback(new Error('身份证包含非法地区'))
      }
      const birthDay = value.substr(6, 4) + '-' + Number(value.substr(10, 2)) + '-' + Number(value.substr(12, 2))

      const d = new Date(birthDay.replace(/-/g, '/'))
      const nowdata = new Date()
      if (birthDay != (d.getFullYear() + '-' + (d.getMonth() + 1) + '-' + d.getDate())) {
        callback(new Error('身份证包含非法出生日期'))
      }
      if (d > nowdata) {
        callback(new Error('身份证包含非法出生日期'))
      }
    }


    callback()
  },
  /**
   * 负整数校验
   */
  integerN(rule, value, callback) {
    if (value && !(/^-[1-9]\d*$/).test(value)) {
      callback(new Error('只能填写负整数'))
    } else {
      callback()
    }
  },

  /**
   * 英文字符校验
   */
  enText(rule, value, callback) {
    if (value && !(/^[A-Za-z]+$/).test(value)) {
      callback(new Error('只能填写英文字符'))
    } else {
      callback()
    }
  },
  /**
   * 中文字符英文字符校验
   */
  ChEnText(rule, value, callback) {
    if (value && !(/^[A-Za-z0-9]+$/).test(value)) {
      callback(new Error('只能填写数字和英文字符'))
    } else {
      callback()
    }
  },
  /**
   * 中文字符校验
   */
  cnText(rule, value, callback) {
    if (value && (/[^\u4e00-\u9fa5]/).test(value)) {
      callback(new Error('只能填写中文字符'))
    } else {
      callback()
    }
  },
  /**
   * 只能输入英文或者数字
   */
  enOrnunText(rule, value, callback) {
    if (value && !(/^[A-Za-z0-9]+$/).test(value)) {
      callback(new Error('只能填写英文或者数字'))
    } else {
      callback()
    }
  },
  /**
   * 20位数字国标编码校验，且为正整数
   */
  validateDeviceNo(rule, value, callback) {
    if (value && !(/^[1-9]\d*$/).test(value)) {
      callback(new Error('只能填写正整数'))
    } else {
      if (!/^[0-9]{20}$/.test(value)) {
        callback(new Error('请输入20位数字的编码！'))
      } else {
        callback()
      }
    }
  },
  /**
   *校验电脑Mac地址
   *以xx-xx-xx-xx-xx-xx的形式输入（xx为16进制数字）
   */
  validateMac(rule, value, callback) {
    const temp = /[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}-[A-Fa-f0-9]{2}/
    if (!temp.test(value)) {
      callback(new Error('请输入xx-xx-xx-xx-xx-xx形式的MAC地址！'))
    } else {
      callback()
    }
  },
  /**
   * 校验地址代码或者分组代码
   */
  validateCode(rule, value, callback) {
    const num = /^[1-9]\d*$/
    if (value && !(num).test(value)) {
      callback(new Error('只能填写正整数'))
    } else {
      const codeLen = value.toString().length
      console.log(codeLen)
      if (codeLen > 0 && codeLen % 3 !== 0) {
        callback(new Error('输入的长度必须是3的倍数'))
      } else if (codeLen > 18) {
        callback(new Error('输入的长度不能超过18位,请重新输入'))
      } else {
        callback()
      }
    }
  },

  /**
   * 校验字符长度
   */
  validateLength(rule, value, callback) {
    const codeLen = value.toString().length
    if (codeLen > 18) {
      callback(new Error('输入的长度不能超过20位,请重新输入'))
    }
  },

  /**
   * 数字 ,两位
   */
  validateTwoNum(rule, value, callback) {
    const temp = /^[1-9]{2}$/
    if (!temp.test(value)) {
      callback(new Error('请输入两位正整数！'))
    } else {
      callback()
    }
  },

  /**
   * 校验经度是否符合规范
   *
   */
  checkLongitude(rule, value, callback) {
    const longrg = /^(\-|\+)?(((\d|[1-9]\d|1[0-7]\d|0{1,3})\.\d{0,6})|(\d|[1-9]\d|1[0-7]\d|0{1,3})|180\.0{0,6}|180)$/
    if (!longrg.test(value)) {
      callback(new Error('经度整数部分为0-180,小数部分为0到6位!'))
    } else {
      callback()
    }
  },

  /**
   * 校验纬度是否符合规范
   *
   */
  checkLatitude(rule, value, callback) {
    var latreg = /^(\-|\+)?([0-8]?\d{1}\.\d{0,6}|90\.0{0,6}|[0-8]?\d{1}|90)$/
    if (!latreg.test(value)) {
      callback(new Error('纬度整数部分为0-90,小数部分为0到6位!'))
    } else {
      callback()
    }
  }
}
