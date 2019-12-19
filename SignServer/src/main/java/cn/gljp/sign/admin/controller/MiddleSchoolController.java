package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.MiddleSchool;
import cn.gljp.sign.admin.service.MiddleSchoolService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class MiddleSchoolController {
    @Autowired
    private MiddleSchoolService middleSchoolService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/middleSchool/listSchoolByRegion")
    public Result listSchoolByRegion(String regionCode, @RequestParam(defaultValue = "1") int page,
                                     @RequestParam(defaultValue = "20") int limit ) {
        int index=(page-1)*limit;
        return middleSchoolService.listSchoolByRegion(index,limit,regionCode);
    }
    @RequestMapping("/middleSchool/listSchoolByRegionNoPage")
    public Result listSchoolByRegionNoPage(String regionCode) {
        return middleSchoolService.listSchoolByRegionNoPage(regionCode);
    }
   @RequestMapping("/middleSchool/setOrderSortAll")
    public Result setOrderSortAll(String regionCode) {
        try {
            middleSchoolService.setOrderSortAll(regionCode);
            return new Result(StatusCode.OK, true,"重新排序成功","重新排序成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "重新排序失败","重新排序失败");
        }
    }
    @RequestMapping("/middleSchool/insert")
    public Result insert(@RequestBody MiddleSchool middleSchool) {
        request.setAttribute("params", JSONUtil.toJsonStr(middleSchool));
        try {
            middleSchoolService.insert(middleSchool);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败,请查看行政代码是否重复！","添加失败,请查看行政代码是否重复！");
        }
    }
    @RequestMapping("/middleSchool/update")
    public Result update(@RequestBody MiddleSchool middleSchool) {
        request.setAttribute("params", JSONUtil.toJsonStr(middleSchool));
        try {
            middleSchoolService.update(middleSchool);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/middleSchool/delete")
    public Result delete(@RequestBody MiddleSchool middleSchool) {
        request.setAttribute("params", JSONUtil.toJsonStr(middleSchool));
        try {
            middleSchoolService.delete(middleSchool);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }
}
