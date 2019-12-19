export function PrefixInteger(num, n) {
  return (Array(n).join(0) + num).slice(-n)
}

export function GetNewUnitSelfCode(pselfcode, snum) {
  return pselfcode + PrefixInteger(snum + 1, 2)
}

export function ReadIDCard(pluginId) {
  console.log(pluginId)
  var CVR_IDCard = document.getElementById(pluginId)
  var ret = eval('(' + CVR_IDCard.connect() + ')')
  if (ret.resultFlag === 0) {
    var result = eval('(' + CVR_IDCard.readCert() + ')')
    return result
  } else {
    return ret
  }
}

