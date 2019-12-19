package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.DictionaryType;
import cn.gljp.sign.admin.entity.Region;
import cn.gljp.sign.admin.service.RegionService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class RegionController {
    @Autowired
    private RegionService regionService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/region/listByFatherCode")
    public Result listByFatherCode(@RequestParam(name="fatherCode")int fatherCode) {
        List<Region> list=regionService.listByFatherCode(fatherCode);
        return new Result(StatusCode.OK,true, "", list);
    }
    @RequestMapping("/region/setOrderSortAll")
    public Result setOrderSortAll(int type,int fatherCode) {
        try {
            regionService.setOrderSortAll(type,fatherCode);
            return new Result(StatusCode.OK, true,"重新排序成功","重新排序成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "重新排序失败","重新排序失败");
        }
    }
    @RequestMapping("/region/insert")
    public Result insert(@RequestBody Region region) {
        request.setAttribute("params", JSONUtil.toJsonStr(region));
        try {
            regionService.insert(region);
            return new Result(StatusCode.OK, true,"添加成功","添加成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "添加失败,请查看行政代码是否重复！","添加失败,请查看行政代码是否重复！");
        }
    }
    @RequestMapping("/region/update")
    public Result update(@RequestBody Region region) {
        request.setAttribute("params", JSONUtil.toJsonStr(region));
        try {
            regionService.update(region);
            return new Result(StatusCode.OK, true,"修改成功","修改成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","修改失败");
        }
    }
    @RequestMapping("/region/delete")
    public Result delete(@RequestBody Region region) {
        request.setAttribute("params", JSONUtil.toJsonStr(region));
        try {
            regionService.delete(region);
            return new Result(StatusCode.OK, true,"删除成功","删除成功");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "删除失败","删除失败");
        }
    }

    @RequestMapping("/region/get")
    public Result getRegion() {
        List<Map<String, Object>> list = regionService.getRegion();
        return new Result(StatusCode.OK,true, "", list);
    }

}
