package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.service.*;
//import cn.gljp.sign.utils.ExcelUtil;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@RestController
@ResponseBody
@RequestMapping("/admin")
@CrossOrigin
public class ExportController {
    @Autowired
    private StatisticsService statisticsService;
    @Autowired
    private ExportService exportService;
    @Autowired
    private HttpServletResponse response;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private DictService dictService;
    @Autowired
    private StudentService studentService;

    /*
     * 导出地区统计
     * */
    @GetMapping(value = "/studentCount/exportChengShiLists")
    public Result exportRegionStatistics(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<LinkedHashMap<String, Object>> regionStatistics = statisticsService.getRegionStatistics(params);
        String url = exportService.exportRegionStatistics(regionStatistics);
        return new Result(StatusCode.OK, true,"",url);
    }


    @GetMapping(value = "/studentCount/exportFieldNameStatistics")
    public Result exportFieldNameStatistics(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<LinkedHashMap<String, Object>> fieldNameStatistics = statisticsService.getFieldNameStatistics(params);
        String url = exportService.exportFieldNameStatistics(fieldNameStatistics, params);
        return new Result(StatusCode.OK, true,"",url);
    }

    @GetMapping(value = "/studentCount/exportZyLists")
    public Result exportMajorStatistics(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<LinkedHashMap<String, Object>> majorCount = null;
        if(params.get("type").toString().equals("3")){
            majorCount = statisticsService.getDeptCount(params);
        }else if(params.get("type").toString().equals("4")){
            majorCount = statisticsService.getMajorCount(params);
        }else {
            majorCount = statisticsService.getClassCount(params);
        }
        String url = exportService.exportMajorStatistics(majorCount, params);
        return new Result(StatusCode.OK, true,"",url);
    }

    /**
     * 导出学生信息
     * @param params
     * @return
     */
    @GetMapping(value = "/student/exportStudentLists")
    public Result exportStudentStatistics(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        params.put("is_export", "");
        List<Map<String, Object>> formConfigLists = dictService.getFormConfigLists(params);
        LinkedHashMap<String, Object> page = exportService.getStudentStatistics(params, formConfigLists);

        String url = exportService.exportStudentStatistics((List<LinkedHashMap<String, Object>>)page.get("data"), params, formConfigLists);

        return new Result(StatusCode.OK, true,"", url);
    }


}
