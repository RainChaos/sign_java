package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.service.*;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/admin")
@CrossOrigin
public class LogController {
    @Autowired
    private LogService logService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping("/actionLog/lists")
    public Result getActionLog(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = logService.getActionLog(params);
        Long actionLogCount = logService.getActionLogCount(params);
        PageResult<Map<String, Object>> data = new PageResult<>(actionLogCount, list);
        return new Result(StatusCode.OK, true,"", data);
    }

    @RequestMapping("/studentLog/lists")
    public Result getStudentLog(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = logService.getStudentLog(params);
        Long studentLogCount = logService.getStudentLogCount(params);
        PageResult<Map<String, Object>> data = new PageResult<>(studentLogCount, list);
        return new Result(StatusCode.OK, true,"", data);
    }

}
