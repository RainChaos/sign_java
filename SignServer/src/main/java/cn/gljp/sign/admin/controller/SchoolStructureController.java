package cn.gljp.sign.admin.controller;


import cn.gljp.sign.admin.service.SchoolStructureService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin/schoolStructure")
@CrossOrigin
public class SchoolStructureController {

    @Autowired
    private SchoolStructureService schoolStructureService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 根据批次和学历获取院系和专业
     * @param map
     * @return
     */
    @RequestMapping("/getSelectUnitList")
    public Result getSelectUnitList(@RequestParam Map<String, Object> map){
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        List<Map<String, Object>> structure =   schoolStructureService.getSelectUnitList(map);
        return new Result(StatusCode.OK, true, "", structure);
    }

    @RequestMapping("/SyncCharges")
    public Result synStructure(){
        //同步单位架构
        schoolStructureService.synStructure();
        return new Result(StatusCode.OK, true, "同步成功", "");
    }
    /**
     * 根据年级UnitID获取该年级下班级的数量
     * @param map
     * @return
     */
    @RequestMapping("/getChildsCount")
    public Result getChildsCount(@RequestBody Map<String, Object> map){
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        Integer count=schoolStructureService.getChildsCount(map);
        return new Result(StatusCode.OK, true, "", count);
    }
    /**
     * 根据年级UnitID生成新建班级的UnitID
     * @param map
     * @return
     */
    @RequestMapping("/getNextUnitCode")
    public Result getNextUnitCode(@RequestParam Map<String, Object> map){
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        String code=schoolStructureService.getNextUnitCode(map);
        return new Result(StatusCode.OK, true, "",code);
    }
    /**
     * 删除班级
     * @param map
     * @return
     */
    @RequestMapping("/delete")
    public Result delete(@RequestBody Map<String, Object> map){
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        Integer count=schoolStructureService.getClassCountByUnitID(map);
        if (count>0){
            return new Result(StatusCode.ERROR, false, "该单位下还有学生，不能删除","");
        }
        //删除报名系统班级
        schoolStructureService.deleteSchoolStructure(map);
        //删除收费系统班级
        schoolStructureService.deleteChargeSchoolStructure(map);
        return new Result(StatusCode.OK, true, "删除成功","");
    }
    /**
     * 保存新创建的班级
     * @param map
     * @return
     */
    @RequestMapping("/saveUnit")
    public Result saveUnit(@RequestBody Map<String, Object> map) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        schoolStructureService.saveUnit(map);

        return new Result(StatusCode.OK, true, "操作成功","");
    }

}
