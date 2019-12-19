package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.service.OfferService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class OfferController {
    @Autowired
    private OfferService offerService;
    @Autowired
    private HttpServletRequest request;

    @RequestMapping(value = "/printTemplate/lists")
    public Result getPrintTemplateList(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> data = offerService.getPrintTemplate(params);
        return new Result(StatusCode.OK, true, "打印模板获取成功", data);
    }


    @RequestMapping(value = "/student/printTzs")
    public Result printOffer(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        String data = offerService.getPrint(params);
        return new Result(StatusCode.OK, true, "打印通知书成功", data);
    }

    @RequestMapping(value = "/student/setIsPrintTzs", method = RequestMethod.POST)
    public Result setIsPrint(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        offerService.setIsPrint(params);
        return new Result(StatusCode.OK, true, "设置打印成功", "");
    }


    @RequestMapping(value = "/printTemplate/update", method = RequestMethod.POST)
    public Result updatePrintTemplate(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        offerService.updatePrintTemplate(params);
        return new Result(StatusCode.OK, true, "打印模板修改成功", "");
    }


    @RequestMapping(value = "/printTemplate/create", method = RequestMethod.POST)
    public Result createPrintTemplate(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        offerService.createPrintTemplate(params);
        return new Result(StatusCode.OK, true, "打印模板添加成功", "");
    }

    @RequestMapping(value = "/printTemplate/delete", method = RequestMethod.POST)
    public Result deletePrintTemplate(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        offerService.deletePrintTemplate(params);
        return new Result(StatusCode.OK, true, "打印模板删除成功", "");
    }

    @RequestMapping(value = "/printAttribute/lists")
    public Result getPrintAttribute(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> printAttribute = offerService.getPrintAttribute();
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", printAttribute);
        return new Result(StatusCode.OK, true, "打印模板参数获取成功", data);
    }

    @RequestMapping(value = "/printAttribute/update", method = RequestMethod.POST)
    public Result updatePrintTemplateAttr(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        offerService.updatePrintTemplateAttr(params);
        return new Result(StatusCode.OK, true, "打印模板参数修改成功", "");
    }


    @RequestMapping(value = "/printAttribute/delete", method = RequestMethod.POST)
    public Result deletePrintTemplateAttr(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        offerService.deletePrintTemplateAttr(params);
        return new Result(StatusCode.OK, true, "打印模板参数删除成功", "");
    }

    @RequestMapping(value = "/printAttribute/create", method = RequestMethod.POST)
    public Result createPrintTemplateAttr(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        offerService.createPrintTemplateAttr(params);
        return new Result(StatusCode.OK, true, "打印模板参数添加成功", "");
    }
}
