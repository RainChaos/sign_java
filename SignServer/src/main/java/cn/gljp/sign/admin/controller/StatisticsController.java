package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.service.ConfigService;
import cn.gljp.sign.admin.service.StatisticsService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/admin")
@ResponseBody
@CrossOrigin
public class StatisticsController {
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 获取学生统计
     * 报名人数、报道人数、报道男生、报道女生、缴费人数
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/main/getCountStudent")
    public Result getStudentCount(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> data = statisticsService.getStudentCount(params);
        return new Result(StatusCode.OK, true, "", data);
    }
    /**
     * 获取班级人数统计
     * 总数，男生，女生
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/main/getCountClassLists")
    public Result getCountClassLists(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        if (params.get("pici_id")==null){
            params.put("pici_id",configService.getCurrentPici());
        }
        List<LinkedHashMap<String, Object>> data = statisticsService.getClassCount(params);
        return new Result(StatusCode.OK, true, "", data);
    }

    /**
     * 获取专业人数统计
     * 总数，男生，女生
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/main/getCountZyLists")
    public Result getMajorCount(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        if (params.get("pici_id")==null){
            params.put("pici_id",configService.getCurrentPici());
        }
        List<LinkedHashMap<String, Object>> data = statisticsService.getMajorCount(params);
        return new Result(StatusCode.OK, true, "", data);
    }

    /**
     * 获取系人数统计
     * 总数，男生，女生
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/main/getCountXiLists")
    public Result getDeptCount(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        if (params.get("pici_id")==null){
            params.put("pici_id",configService.getCurrentPici());
        }
        List<LinkedHashMap<String, Object>> data = statisticsService.getDeptCount(params);
        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/studentCount/countZyLists")
    public Result getMajorStatistics(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<LinkedHashMap<String, Object>> data = statisticsService.getDeptCount(params);
        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/getDataTypeIsSSQ")
    public Result getDataTypeIsSSQ() throws Exception {
        List<Map<String, Object>> data = statisticsService.getDataTypeIsSSQ();
        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/studentCount/countChengshiLists")
    public Result getRegionStatistics(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<LinkedHashMap<String, Object>> data = statisticsService.getRegionStatistics(params);
        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/studentCount/countFieldNameList")
    public Result getFieldNameStatistics(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<LinkedHashMap<String, Object>> data = statisticsService.getFieldNameStatistics(params);
        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/statistics/fieldName/config/get")
    public Result getFieldNameStatisticsConfig() throws Exception {
        List<LinkedHashMap<String, Object>> data = statisticsService.getFieldNameStatisticsConfig();
        return new Result(StatusCode.OK, true, "", data);
    }



}
