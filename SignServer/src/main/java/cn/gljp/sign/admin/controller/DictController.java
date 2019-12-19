package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.Dictionary;
import cn.gljp.sign.admin.entity.DictionaryType;
import cn.gljp.sign.admin.service.DictService;
import cn.gljp.sign.utils.StatusCode;
import cn.gljp.sign.utils.TreeUtils;
import cn.gljp.sign.utils.Result;
import cn.hutool.json.JSONUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class DictController {
    @Autowired
    private DictService dictService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/dict/getDictionaryTypeList")
    public Result getDictionaryTree() {
       List<DictionaryType> list=dictService.getDictionaryTypeList();
       list= TreeUtils.listToTree(list);
       return new Result(StatusCode.OK, true,"", list);
    }
    @RequestMapping("/dict/listForPid")
    public Result listForPid(@RequestParam(defaultValue = "0")int pid) {
        List<DictionaryType> list=dictService.listForPid(pid);
        return new Result(StatusCode.OK,true, "", list);
    }
    @RequestMapping("/dict/insert")
    public Result insert(@RequestBody DictionaryType dictionaryType) {
        request.setAttribute("params", JSONUtil.toJsonStr(dictionaryType));
        try {
            dictService.insert(dictionaryType);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败","添加失败");
        }
    }
    @RequestMapping("/dict/update")
    public Result update(@RequestBody DictionaryType dictionaryType) {
        request.setAttribute("params", JSONUtil.toJsonStr(dictionaryType));
        try {
            dictService.update(dictionaryType);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/dict/delete")
    public Result delete(@RequestBody DictionaryType dictionaryType) {
        request.setAttribute("params", JSONUtil.toJsonStr(dictionaryType));
        try {
            dictService.delete(dictionaryType);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
    @RequestMapping("/dictionary/listDictionaryByType")
    public Result listDictionary(String type, @RequestParam(defaultValue = "1") int page,
                                 @RequestParam(defaultValue = "20") int limit ) {
        int index=(page-1)*limit;
        return dictService.listDictionaryByType(index,limit,type);
    }
    @RequestMapping("/dictionary/insert")
    public Result dictionaryInsert(@RequestBody Dictionary dictionary) {
        request.setAttribute("params", JSONUtil.toJsonStr(dictionary));
        try {
            dictService.dictionaryInsert(dictionary);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败","添加失败");
        }
    }
    @RequestMapping("/dictionary/update")
    public Result dictionaryUpdate(@RequestBody Dictionary dictionary) {
        request.setAttribute("params", JSONUtil.toJsonStr(dictionary));
        dictService.dictionaryUpdate(dictionary);
        return new Result(StatusCode.OK, true,"修改成功","修改成功");

    }
    @RequestMapping("/dictionary/updateStatus")
    public Result updateStatus(@RequestBody Dictionary dictionary) {
        request.setAttribute("params", JSONUtil.toJsonStr(dictionary));
        try {
            dictService.updateStatus(dictionary);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/dictionary/delete")
    public Result dictionaryDelete(@RequestBody Map<Object,String> ids) {
        try {
            String idStr=ids.get("ids");
            List list=Arrays.asList(idStr.split(","));
            dictService.dictionaryDelete(list);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
    @RequestMapping("/dictionary/setOrderSortAll")
    public Result setOrderSortAll(@RequestParam(name="type",required = false) String type) {
        try {
            dictService.setOrderSortAll(type);
            return new Result(StatusCode.OK, true,"调整成功","调整成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "调整失败","调整失败");
        }

    }

    @RequestMapping("/dictionary/setDefault")
    public Result setDefault(@RequestBody Map<Object,String> params) {
        dictService.setDefault(params);
        return new Result(StatusCode.OK, true,"设置默认成功","");

    }
}
