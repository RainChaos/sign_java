package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.service.DormService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/admin")
@CrossOrigin
public class DormController {
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private DormService dormService;

    /**
     * 设置入住
     * 调用住宿接口
     * @param params
     * @return
     */
    @RequestMapping(value = "/dor/inBedAuto")
    public Result setInBed(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        dormService.setDorm(params, 1);

        return new Result(StatusCode.OK, true, "设置入住成功","");
    }

    @RequestMapping(value = "/dor/outBed")
    public Result setOutBed(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        dormService.setDorm(params, 2);

        return new Result(StatusCode.OK, true, "设置离宿成功","");
    }
}
