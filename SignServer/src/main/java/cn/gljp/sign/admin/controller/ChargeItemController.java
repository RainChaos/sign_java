package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.ChargeItem;
import cn.gljp.sign.admin.service.ChargeItemService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.gljp.sign.utils.TreeUtils;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class ChargeItemController {
    @Autowired
    private ChargeItemService chargeItemService;
    @Autowired
    private HttpServletRequest request;
    /*
    * 同步收费系统项目
    * */
    @RequestMapping("/chargeitem/syncChargeItem")
    public Result syncChargeItem() {
        chargeItemService.syncChargeItem();
        return new Result(StatusCode.OK, true,"同步成功","");
    }
    @RequestMapping("/chargeitem/getChargeItemList")
    public Result getChargeItemList() {
        List<ChargeItem> list=chargeItemService.getChargeItemList();
        return new Result(StatusCode.OK, true,"", list);

    }
    @RequestMapping("/chargeitem/insert")
    public Result insert(@RequestBody ChargeItem chargeItem) {
        request.setAttribute("params", JSONUtil.toJsonStr(chargeItem));
        try {
            chargeItem.setSort(1);
            chargeItemService.insertSelective(chargeItem);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败","添加失败");
        }
    }
    @RequestMapping("/chargeitem/update")
    public Result update(@RequestBody ChargeItem chargeItem) {
        request.setAttribute("params", JSONUtil.toJsonStr(chargeItem));
        try {
            chargeItemService.update(chargeItem);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/chargeitem/delete")
    public Result delete(@RequestBody ChargeItem chargeItem) {
        request.setAttribute("params", JSONUtil.toJsonStr(chargeItem));
        try {
            chargeItemService.delete(chargeItem);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
}
