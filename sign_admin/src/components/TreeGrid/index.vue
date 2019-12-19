<template>
  <div ref="tree" class="tree-wrap">
    <div class="tree">
      <!--表头-->
      <ul class="table-header" style="padding:0px;margin:0px;">
        <li
          v-for="(col, colIndex) in column"
          :key="colIndex"
          :style="[{
            width: col.width? col.width+'px': col.delete == true || col.operate == true? '50px': '',
            flex: col.width? 'none': col.delete == true || col.operate == true? 'none': 1,
            padding: col.operate == true || col.delete == true? '0px': '0px 10px',
            'min-width': col.operate == true || col.delete == true? '40px': '100px',
          }]"
          class="nowrap">
          <template v-if="col.operate == true">
            <i style="transform: translateX(-12px)" class="fa fa-cogs" aria-hidden="true"/>
          </template>
          <span v-else-if="col.delete == true">
            <button :disabled="deleteBtnDisable" title="删除所选项" class="btn-delete" @click="removeLine">
              <i v-if="deleteBtnDisable" class="fa fa-spin fa-spinner" aria-hidden="true"/>
              <i v-if="!deleteBtnDisable" class="fa fa-trash" aria-hidden="true"/>
            </button>
          </span>
          <span v-else>
            <span v-if="col.isTree">
              <button
                :disabled="refreshBtnDisable"
                title="刷新"
                class="btn-refresh"
                @click="refreshTable">
                <i
                  :class="{'fa-spin':refreshBtnDisable? true: false}"
                  class="fa fa-refresh"
                  aria-hidden="true"/>
              </button>
              &nbsp;
            </span>
            {{ col.name }}
          </span>
          <span class="w-resize" @mousedown="willDragStart(col, $event)"/>
        </li>
      </ul>
      <!--表体-->
      <ul v-if="data.length !== 0" class="table-body" style="margin:0px;padding:0px;">
        <div
          v-for="(rowItem) in data"
          v-show="rowItem.show"
          :key="rowItem.id"
          class="table-body-li-wrap"
          @mouseleave="rowMouseLeave(rowItem.id)"
          @mouseenter="rowMouseEnter(rowItem.id)">
          <div v-if="uploading['uploading'+rowItem.id] == true" class="uploading">
            <i class="fa fa-spin fa-spinner" aria-hidden="true"/>
          </div>
          <div style="display: flex;">
            <li
              v-for="(col, colIndex) in column"
              :class="{modify: rowItem.modify == true}"
              :key="colIndex"
              :style="[{
                width: col.width? col.width+'px': col.delete == true || col.operate == true? '50px': '',
                flex: col.width? 'none': col.delete == true || col.operate == true? 'none': 1,
                padding: col.operate == true? '0px': '0px 10px',
                'padding-left': col.isTree? 24 * rowItem.level+14+'px': col.operate == true? '0px': '14px',
                overflow: col.operate == true? 'visible': '',
                'min-width': col.operate == true || col.delete == true? '40px': '100px',
              }]"
              class="table-body-li nowrap">
              <!--树的伸缩按钮-->
              <template v-if="col.isTree && rowItem.children.length != 0">
                <span
                  v-show="rowItem.icon == false"
                  @click="expand(rowItem, true)">
                  <i class="fa fa-plus-square" aria-hidden="true"/>
                </span>
                <span
                  v-show="rowItem.icon == true"
                  @click="expand(rowItem, false)">
                  <i class="fa fa-minus-square" aria-hidden="true"/>
                </span>
              </template>
              <!--不可编辑-->
              <span v-if="!col.edit && !col.isTree && !col.operate && !col.delete && col.prop != 'id'"> {{ rowItem[col.prop] }} </span>
              <span v-if="!col.edit && !col.isTree && !col.operate && !col.delete && col.prop == 'id'"> {{ rowItem[col.prop] | timestamp__ }} </span>
              <!--可编辑-->
              <template v-if="col.edit">
                <span
                  v-show="!operateStatus['status'+rowItem.id]"
                  :title="rowItem[col.prop]"
                  style=" text-align: left;"
                  class="no-wrap">
                  {{ rowItem[col.prop] }}
                </span>
                <input
                  v-show="operateStatus['status'+rowItem.id]"
                  :style="{
                    width: col.isTree? 'auto': '100%'
                  }"
                  :value="lineValue['lineValue'+col.prop+rowItem.id]"
                  type="text"
                  @input="inputEdit(rowItem.id, col.prop, $event)" >
              </template>

              <!--操作按钮-->
              <template v-if="col.operate">
                <span class="hover-btn" @mouseover="mouseHover(rowItem.id, 1)" @mouseout="mouseHover(rowItem.id, 2)" >
                  <span class="hover-btn-span">
                    &nbsp;<i class="fa fa-pencil-square" aria-hidden="true"/>
                  </span>
                  <transition name="as">
                    <span v-show="hoverBtn['hoverBtn'+rowItem.id]" class="col-btn-group">
                      <span v-show="!operateStatus['status'+rowItem.id]">
                        <button class="btn-edit" @click="operateBtn(rowItem.id)">
                          <i class="fa fa-pencil" aria-hidden="true"/>&nbsp;编辑
                        </button>
                        <button class="btn-add" @click="addLine(rowItem.id, 1)">
                          <i class="fa fa-plus" aria-hidden="true"/>&nbsp;增加同级
                        </button>
                        <button class="btn-add" @click="addLine(rowItem.id, 2)">
                          <i class="fa fa-plus-circle" aria-hidden="true"/>&nbsp;增加下级
                        </button>
                        <button v-if="rowItem.modify" class="btn-upload" @click="upload(rowItem.id)">
                          <i class="fa fa-cloud-upload" aria-hidden="true"/>&nbsp;上传
                        </button>
                      </span>
                      <span v-show="!!operateStatus['status'+rowItem.id]">
                        <button class="btn-confirm" @click="saveBtn(rowItem.id)">
                          <i class="fa fa-check" aria-hidden="true"/>&nbsp;确认
                        </button>
                        <button class="btn-cancel" @click="cancel(rowItem.id)">
                          <i class="fa fa-times" aria-hidden="true"/>&nbsp;撤销
                        </button>
                      </span>
                    </span>
                  </transition>
                </span>
              </template>

              <!--删除-复选框-->
              <template v-if="col.delete">
                <span
                  :class="{active: checkboxControl['active'+rowItem.id]}"
                  class="checkbox-span"
                  @click="checkbox_change(rowItem.id)">
                  <i v-if="checkboxControl['active'+rowItem.id]" class="fa fa-check-circle" aria-hidden="true"/>
                  <i v-if="!checkboxControl['active'+rowItem.id]" class="fa fa-circle-o" aria-hidden="true"/>
                </span>
              </template>

              <!--准备上传的提示-->
              <template v-if="rowItem.modify && colIndex == 0">
                <span class="modify-pin">
                  <span class="modify-pin-title">记得上传</span>
                  <i class="fa fa-exclamation" aria-hidden="true"/>
                </span>
              </template>

            </li>
          </div>
        </div>
      </ul>
      <!--空-->
      <div v-if="data.length == 0" class="no-data" style="text-align: center;">
        <i class="fa fa-file-o" aria-hidden="true"/>&nbsp;&nbsp;数据为空
      </div>
    </div>
    <!--loading-->
    <div v-if="treeLoading && data.length != 0" class="tree-loading">
      <i class="fa fa-spinner fa-spin" aria-hidden="true"/>
    </div>
  </div>
</template>

<script>

export default {
  name: 'TreeGrid',
  filters: {
    timestamp__: function(val) {
      if ((val + '').indexOf('__timestamp__') !== -1) {
        return ' - '
      }
      return val
    }
  },
  props: {
    columns: { require: true, type: Array },
    rowdata: { require: true, type: Array },
    needUpdate: { require: true, default: false },
    treeLoading: { default: false }
  },
  data() {
    return {
      column: [],
      data: [],
      // 每行数据对象的格式
      data_format: null,
      // 移入移出-控制
      operate: {},
      hoverBtn: {},
      // 每行的编辑状态
      operateStatus: {},
      // 每行的上传loading
      uploading: {},
      // 控制输入框的值
      lineValue: {},
      // 复选框控制
      checkboxControl: {},
      // 待删除id集合
      deleteIdArr: [],
      // 表头-删除btn
      deleteBtnDisable: false,
      // 表头-刷新btn
      refreshBtnDisable: false
    }
  },
  watch: {
    needUpdate() {
      this.init()
    }
  },
  created() {
    this.column = [...this.columns]
  },
  methods: {
    // 刷新表格
    refreshTable() {
      this.refreshBtnDisable = true
      this.deleteBtnDisable = true
      this.$emit('refreshTable')
    },
    // 初始化
    init() {
      if (this.rowdata.length === 0) return
      this.refreshBtnDisable = false
      this.deleteBtnDisable = false
      // 整理data
      this.data = combineData(cleanData([...this.rowdata]))
      // 得到其中的数据对象格式
      const data_format = { ...this.data[0] }
      for (const key in data_format) {
        if (data_format.hasOwnProperty(key)) {
          data_format[key] = ''
        }
      }
      this.data_format = data_format
      // 初始化行
      this.initValue()
    },
    // 初始化行各单元格控制
    initValue() {
      const row = [...this.data]
      const col = [...this.column]
      for (let i = 0; i < row.length; i++) {
        // 初始化checkbox
        this.$set(this.checkboxControl, 'active' + row[i].id, false)
        for (let j = 0; j < col.length; j++) {
          if (col[j].edit === true) {
            // 初始化单元格input
            this.$set(this.lineValue, 'lineValue' + col[j].prop + row[i].id, row[i][col[j].prop])
          }
        }
      }
    },
    // 行的移入、移出
    rowMouseEnter(id) {
      this.$set(this.operate, 'operate' + id, true)
    },
    rowMouseLeave(id) {
      this.$set(this.operate, 'operate' + id, false)
    },
    // 操作列的移入移出
    mouseHover(rowid, index) {
      if (index === 1) {
        this.$set(this.hoverBtn, 'hoverBtn' + rowid, true)
      } else if (index === 2) {
        this.$set(this.hoverBtn, 'hoverBtn' + rowid, false)
      }
    },
    // 表头列-伸缩宽度
    willDragStart(col, e) {
      if (col.prop === undefined) return
      if (col.width === undefined) return
      this.startClientX = e.clientX
      this.colItem = { ...col }
      const _this = this
      const _width = col.width
      window.removeEventListener('mousemove', dragMove)
      window.removeEventListener('mouseup', dragUp)
      window.addEventListener('mousemove', dragMove)
      window.addEventListener('mouseup', dragUp)
      function dragMove(ev) {
        _this.moveClientX = ev.clientX
        const dis = _this.moveClientX - _this.startClientX
        const _column = [..._this.column]
        for (let i = 0; i < _column.length; i++) {
          if (_column[i].prop === col.prop) {
            _column[i].width = _width * 1 + dis * 1
            break
          }
        }
        _this.column = _column
      }
      function dragUp() {
        window.removeEventListener('mousemove', dragMove)
        window.removeEventListener('mouseup', dragUp)
      }
    },
    // 相关行的折叠与展开
    expand(rowItem, bool) {
      const d = [...this.data]
      let lastLine = null
      for (let i = 0; i < d.length; i++) {
        if (d[i].id === rowItem.id) {
          d[i].icon = bool
          let temp_level = Number.MAX_SAFE_INTEGER
          let k = i
          const clickLevel = d[i].level
          while (123456) {
            k++
            lastLine = k
            if (d[k] === undefined) break
            if (bool === false) {
              if (d[k].level > clickLevel) {
                d[k].show = false
              } else {
                break
              }
            } else {
              if (d[k].level > clickLevel) {
                if (d[k].level > temp_level) {
                  continue
                } else if (d[k].level === temp_level) {
                  temp_level = Number.MAX_SAFE_INTEGER
                }
                if (d[k].icon === false) {
                  temp_level = d[k].level
                }
                d[k].show = true
              } else {
                break
              }
            }
          }
        }
      }
      this.data = d
      return [lastLine, d]
    },
    // 行的输入事件
    inputEdit(rowItemId, colProp, e) {
      const d = [...this.data]
      const col = [...this.column]
      for (let i = 0; i < d.length; i++) {
        if (d[i].id === rowItemId) {
          d[i].editing = true
          break
        }
      }
      this.data = d
      this.$set(this.lineValue, 'lineValue' + colProp + rowItemId, e.target.value)
    },
    // btn-编辑
    operateBtn(rowItemId) {
      // 打开编辑状态
      this.$set(this.operateStatus, 'status' + rowItemId, true)
    },
    // btn-确认
    saveBtn(rowItemId) {
      const d = [...this.data]
      const col = [...this.column]
      for (let i = 0; i < d.length; i++) {
        if (d[i].id === rowItemId) {
          if (d[i].editing === true) {
            d[i].modify = true
          }
          for (let j = 0; j < col.length; j++) {
            if (col[j].edit === true) {
              d[i][col[j].prop] = this.lineValue['lineValue' + col[j].prop + rowItemId]
            }
          }
          break
        }
      }
      this.data = d
      // 关闭编辑状态
      this.$set(this.operateStatus, 'status' + rowItemId, false)
    },
    // btn-撤销
    cancel(rowItemId) {
      const d = [...this.data]
      const col = [...this.column]
      for (let i = 0; i < d.length; i++) {
        if (d[i].id === rowItemId) {
          for (let j = 0; j < col.length; j++) {
            if (col[j].edit === true) {
              this.lineValue['lineValue' + col[j].prop + rowItemId] = d[i][col[j].prop]
            }
          }
          break
        }
      }
      // 关闭编辑状态
      this.$set(this.operateStatus, 'status' + rowItemId, false)
    },
    // btn-上传
    upload(rowItemId) {
      const d = [...this.data]
      for (let i = 0; i < d.length; i++) {
        if (d[i].id === rowItemId) {
          // 显示loading
          this.$set(this.uploading, 'uploading' + rowItemId, true)
          this.$emit('uploadmodify', [d[i], success.bind(this), faild.bind(this)])
        }
      }
      this.rowMouseLeave(rowItemId)
      function success() {
        const d = [...this.data]
        const col = [...this.column]
        for (let i = 0; i < d.length; i++) {
          if (d[i].id === rowItemId) {
            d[i].modify = false
            d[i].editing = false
            break
          }
        }
        this.data = d
        this.$set(this.uploading, 'uploading' + rowItemId, false)
        this.$message.success('上传成功')
      }
      function faild() {
        this.$set(this.uploading, 'uploading' + rowItemId, false)
        this.$message.success('上传失败，请重试')
      }
    },
    // btn-同下级的增加
    addLine(rowItemId, indexed) {
      if (indexed === 1) {
        // 1、增加同级
        const d = [...this.data]
        let rowItem = null
        for (let i = 0; i < d.length; i++) {
          if (d[i].id === rowItemId) {
            rowItem = d[i]
          }
        }
        const [lastLine, nowData] = this.expand(rowItem, false)
        const newId = Date.now() + '__timestamp__'
        // 增加项
        const addItem = {
          ...this.data_format,
          ...{
            id: newId,
            level: rowItem.level,
            parentid: rowItem.parentid,

            modify: true,
            show: true,

            children: []// 长度为0，就没有+/-
          }
        }
        // 增加项的父层
        const _parentItem = d.filter(item => item.id === addItem.parentid)[0]
        if (_parentItem !== undefined) {
          // 增加项的复选框状态
          this.$set(this.checkboxControl, 'active' + addItem.id, this.checkboxControl['active' + _parentItem.id])
          // 增加项的复选框状态为选中，则加入待删集合
          if (this.checkboxControl['active' + addItem.id]) {
            this.deleteIdArr.push(addItem.id)
          }
        }
        nowData.splice(lastLine, 0, addItem)
        this.$set(this.operateStatus, 'status' + newId, true) // 打开编辑状态
        this.data = nowData
      } else if (indexed === 2) {
        // 2、增加下级
        const d = [...this.data]
        let rowItem = null
        let rowIndex = null
        for (let i = 0; i < d.length; i++) {
          if (d[i].id === rowItemId) {
            d[i].children = [true]
            rowItem = d[i]
            rowItem.icon = true
            rowIndex = i
          }
        }
        const newId = Date.now() + '__timestamp__;'
        const [lastLine, nowData] = this.expand(rowItem, true)
        const addItem = {
          ...this.data_format,
          ...{
            id: newId,
            level: Number.parseInt(rowItem.level) + 1,
            parentid: rowItem.id,

            modify: true,
            show: true,

            children: []// 长度为0，就没有+/-
          }
        }
        // 增加项的父层
        const _parentItem = d.filter(item => item.id === addItem.parentid)[0]
        // 增加项的复选框状态
        this.$set(this.checkboxControl, 'active' + addItem.id, this.checkboxControl['active' + _parentItem.id])
        // 增加项的复选框状态为选中，则加入待删集合
        if (this.checkboxControl['active' + addItem.id]) {
          this.deleteIdArr.push(addItem.id)
        }
        nowData.splice(Number.parseInt(rowIndex) + 1, 0, addItem)
        this.$set(this.operateStatus, 'status' + newId, true) // 打开编辑状态
        this.data = nowData
      }
    },
    // 复选框切换
    checkbox_change(rowItemId) {
      if (this.deleteBtnDisable) return
      const old_bool = this.checkboxControl['active' + rowItemId]
      this.$set(this.checkboxControl, 'active' + rowItemId, !old_bool)
      const now_bool = !old_bool
      if (now_bool) {
        // 选中的规则
        const d = [...this.data]
        for (let i = 0; i < d.length; i++) {
          if (d[i].id === rowItemId) {
            let k = i
            const clickLevel = d[i].level
            while (123456) {
              k++
              if (d[k] === undefined) break
              if (d[k].level > clickLevel) {
                this.$set(this.checkboxControl, 'active' + d[k].id, true)
              } else {
                break
              }
            }
            break
          }
        }
      } else {
        // 不选中的规则
        const d = [...this.data]
        for (let i = 0; i < d.length; i++) {
          if (d[i].id === rowItemId) {
            let k = i
            const clickLevel = d[i].level
            this.$set(this.checkboxControl, 'active' + d[i].parentid, false)
            while (123456) {
              k++
              if (d[k] === undefined) break
              if (d[k].level > clickLevel) {
                this.$set(this.checkboxControl, 'active' + d[k].id, false)
              } else {
                break
              }
            }
            repeatFn.call(this, d[i])
            function repeatFn(item) {
              this.$set(this.checkboxControl, 'active' + item.parentid, false)
              if (item.parentid === item.id) {
                return
              }
              for (let i = 0; i < d.length; i++) {
                if (d[i].id === item.parentid) {
                  repeatFn.call(this, d[i])
                }
              }
            }
            break
          }
        }
      }
      // 更新待删除集合：deleteIdArr
      this.deleteIdArr = []
      for (const key in this.checkboxControl) {
        if (this.checkboxControl.hasOwnProperty(key)) {
          if (this.checkboxControl[key] === true) {
            this.deleteIdArr.push(key.split('active')[1])
          }
        }
      }
    },
    // btn-删除
    removeLine() {
      const deleteIdArr = [...this.deleteIdArr]
      if (deleteIdArr.length === 0) return
      this.deleteBtnDisable = true
      for (let j = 0; j < deleteIdArr.length; j++) {
        this.$set(this.uploading, 'uploading' + deleteIdArr[j], true)
      }
      this.$emit('uploaddelete', [deleteIdArr, success.bind(this), faild.bind(this)])
      // 成功的回调
      function success() {
        const d = [...this.data]
        const deleteIdArr = [...this.deleteIdArr]
        deleteIdArr.map(item => {
          item = item + ''
          return item
        })
        for (let j = 0; j < deleteIdArr.length; j++) {
          this.$set(this.uploading, 'uploading' + deleteIdArr[j], false)
        }
        for (const [key, value] of Object.entries(this.checkboxControl)) {
          deleteIdArr.forEach(id => {
            if (key.split('active')[1] === id) {
              delete this.checkboxControl[key]
            }
          })
        }
        const _d = d.filter(item => {
          for (let i = 0; i < deleteIdArr.length; i++) {
            if (deleteIdArr[i] + '' === item.id + '') {
              return false
            }
          }
          return true
        })
        // 修正icon是否显示
        for (let i = 0; i < _d.length; i++) {
          if (_d[i + 1] !== undefined) {
            if (_d[i + 1].level <= _d[i].level) {
              delete _d[i].icon
              _d[i].children = []
            }
          } else if (_d[i + 1] === undefined) {
            delete _d[i].icon
            _d[i].children = []
          }
        }
        this.deleteIdArr = []
        this.deleteBtnDisable = false
        this.data = _d
        this.$message.success('删除成功')
      }
      // 失败的回调
      function faild() {
        for (let j = 0; j < this.deleteIdArr.length; j++) {
          this.$set(this.uploading, 'uploading' + this.deleteIdArr[j], false)
        }
        this.deleteBtnDisable = false
        this.$message.error('删除失败，请重试！')
      }
    }
  }
}

function cleanData(data) {
  let data2 = [...data]
  let levelLength = 0
  const clean = []
  if (data2.length === 0) return []
  data2.forEach(item => {
    if (item.level > levelLength) {
      levelLength = item.level
    }
  })
  data2 = data2.map(item => {
    item.children = []
    item.show = true
    return item
  })
  for (let i = 0; i <= levelLength; i++) {
    clean[i] = []
    data2.forEach(item => {
      if (item.level === i) {
        clean[i].push(item)
      }
    })
  }
  for (let i = clean.length - 1; i >= 0; i--) {
    for (let j = 0; j < clean[i].length; j++) {
      addToChildren(clean[i][j], i)
    }
  }
  return clean[0]
  function addToChildren(obj, index) {
    if (index !== 0) {
      const _i = index - 1
      for (let i = 0; i < clean[_i].length; i++) {
        if (clean[_i][i].id === obj.parentid) {
          clean[_i][i].icon = true// 初始化全部展开
          clean[_i][i].children.push(obj)
        }
      }
    }
  }
}

function combineData(cleanData) {
  const clean = [...cleanData]
  const res = []
  for (let i = 0; i < clean.length; i++) {
    res.push(clean[i])
    expandToArray(clean[i])
  }
  return res
  function expandToArray(whenClean) {
    if (whenClean.children.length !== 0) {
      for (let j = 0; j < whenClean.children.length; j++) {
        res.push(whenClean.children[j])
        expandToArray(whenClean.children[j])
      }
    }
  }
}

</script>

<style lang="scss" scoped>
*{ box-sizing: border-box !important; }
.nowrap{
    overflow: hidden;
    white-space: nowrap;
    text-overflow: ellipsis;
}
.tree-wrap{
    position: relative;
    font-family: Helvetica Neue,Helvetica,PingFang SC,Hiragino Sans GB,Microsoft YaHei,SimSun,sans-serif;
    .tree-loading{
        width: 100%;
        height: 100%;
        position: absolute;
        top: 0px;
        left: 0px;
        z-index: 10010;
        background-color: rgba(255, 255, 255, .77);
        .fa{
            font-size: 28px;
            display: block;
            text-align: center;
            margin-top: 52px;
        }
    }
}
.tree{
    .no-data{
        padding-top: 16px;
        padding-bottom: 16px;
        border: 1px solid #d8dcdf;
        color: #6e5f49;
    }
    .table-header{
        display: flex;
        width: 100%;
        li{
            padding: 0px 10px;
            background-color: #F8F8F9;
            border-right: 1px solid #ececec;
            border-top: 1px solid #ececec;
            border-bottom: 1px solid #ececec;
            color: #909399;
            font-weight: bolder;
            text-indent: 12px;
            font-size:13px;
            min-width: 90px;
            height: 30px;
            line-height: 30px;
            position: relative;
        }
        li:first-child{
          border-left:1px solid #ececec;
        }
        li:last-child{
            border-right:1px solid #ececec;
        }
        .w-resize{
            cursor: w-resize;
            position: absolute;
            right: 0px;
            top: 0px;
            height: 32px;
            width: 8px;
            background: transparent;
            z-index: 101;
        }
    }
    .table-body-li-wrap{
        position: relative;
        .uploading{
            position: absolute;
            height: 32px;
            width: 100%;
            background: rgba(255, 255, 255, .77);
            text-align: left;
            z-index: 1000;
            line-height: 32px;
            font-size: 13px;
            padding-left: 50px;
        }
        &:hover{
            background-color: #ebf3f0;
            .table-body-li{
                color: #965757;
            }
        }
    }
    .table-body{
        display: block;
        .table-body-li{
            background-color: rgba(255,255,255,.3);
            border-right: 1px solid #ececec;
            border-bottom: 1px solid #ececec;
            color: #454545;
            font-size: 13px;
            height: 28px;
            line-height: 28px;
            text-align: left;
            list-style-type:none;
            &:nth-of-type(1){
                position: relative;
                overflow: visible;
            }
            &.modify{
                background: #f7f4ea;
                color: #E3431C;
                .hover-btn-span{
                    color: #E3431C !important;
                }
            }
            .fa{
                cursor: pointer;
                &:hover{
                   // color: #359899;
                }
            }
            input{
                max-width: 100%;
                margin: 0px;
                height: 24px;
                border: 1px solid #aabdbe;
                text-indent: 8px;
                &:focus{
                    outline: none;
                    -moz-outline: none;
                    color: #E3431C;
                    border: 1px solid #E3431C;
                }
            }
            span.checkbox-span{
                width: 18px;
                height: 18px;
                margin-top: 7px;
                cursor: pointer;
                border-radius: 50%;
                display: inline-block;
                position: relative;
                &.active{
                    .fa{
                        color: #409EFF;
                    }
                }
                .fa{
                    position: absolute;
                    font-size: 18px;
                    &:hover{
                        color: #409EFF;
                    }
                    left: 1px;
                    top: 1px;
                }
            }
            .hover-btn{
                position: relative;
                display: inline-block;
                width: 100%;
                .hover-btn-span{
                    display: inline-block;
                    width: 100%;
                    text-align: center;
                    font-size: 18px;
                    // background: #eef1f6;
                    border: none;
                    color: #3C9AFC;
                    &:hover{
                        background: #eef1f6;
                    }
                }
              // #ecf5ff
                .col-btn-group{
                    position: absolute;
                    left: calc(100% - 1px);
                    top: -2px;
                    z-index: 99;
                    background: #ececec;
                    padding-left: 10px;
                    padding-right: 8px;
                    border: 2px solid #3C9AFC;
                    &:after{
                        position: absolute;
                        content: ' ';
                        width: 12px;
                        height: 12px;
                        background: #ececec;
                        z-index: 98;
                        left: -7px;
                        top: 10px;
                        transform: rotate(45deg);
                        border: 2px solid #3C9AFC;
                        border-right: none;
                        border-top: none;
                    }
                }
            }
            .modify-pin{
                position: absolute;
                right: 0;
                bottom: calc(100% - 20px);
                perspective: 150px;
                animation: tip 5.2s infinite ease-in-out;
                transform-origin: center 22px;
                .modify-pin-title{
                    display: block;
                    height: 16px;
                    font-size: 10px;
                    line-height: 16px;
                    text-align: center;
                    background: #e76e50;
                    width: 54px;
                    color: #fff;
                }
                .fa{
                    display: block;
                    text-align: center;
                    font-size: 10px;
                    color: #E3431C;
                }
            }

        }

        .table-body-li:first-child{
            border-left: 1px solid #ececec;
        }
    }
    button{
        border: none;
        height: 18px;
        cursor: pointer;
        font-size: 12px;
        padding-left: 6px;
        padding-right: 6px;
        &:hover,&:active,&:focus{
            outline: none;
            -moz-outline: none;
        }
        &.btn-delete{
            transform: translateX(-6px);
            background-color: #F56C6C;
            color: #fff;
            font-size: 15px;
            padding-left: 8px;
            padding-right: 8px;
            height: 20px;
            border: none;
            &:hover{
              background: #f78989;
              border-color: #f78989;
              color: #fff;
            }
            &[disabled]{
                background-color: #f3be91;
            }
        }
        &.btn-refresh{
            transform: translateX(-6px);
            background-color: #1dd545;
            color: #fff;
            font-size: 15px;
            padding-left: 8px;
            padding-right: 8px;
            height: 20px;
            border: none;
            &:hover{
                background-color: #7ff699;
            }
            &[disabled]{
                background-color: #a0eeb1;
            }
        }
        &.btn-edit{
            background-color: #2a75a9;
            color: #eef1f6;
            &:hover{
                background-color: #55a4db;
            }
        }
        &.btn-add{
            background-color: #4f911d;
            color: #eef1f6;
            &:hover{
                background-color: #74ba3e;
            }
        }
        &.btn-upload{
            background-color: #59ca1f;
            color: #eef1f6;
            &:hover{
                background-color: #6ddb35;
            }
        }
        &.btn-confirm{
            background-color: #26a10f;
            color: #eef1f6;
            &:hover{
                background-color: #46d72b;
            }
        }
        &.btn-cancel{
            background-color: #a99d0d;
            color: #eef1f6;
            &:hover{
                background-color: #cec120;
            }
        }
    }

}

@keyframes tip{
    22%{ transform: rotateZ(0deg); }
    24%{ transform: rotateZ(-4deg); }
    26%{ transform: rotateZ(4deg); }
    28%{ transform: rotateZ(0deg); }

    68%{ transform: translateY(0px); }
    71%{ transform: translateY(-3px); }
    74%{ transform: translateY(0px); }
    77%{ transform: translateY(-3px); }
    80%{ transform: translateY(0px); }
}
.as-enter-active, .as-leave-active {
  transition: all .2s
}
.as-enter, .as-leave-to {
  opacity: 0;
  transform: translateX(16px);
}
</style>

