package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.service.OperatorService;
import cn.gljp.sign.utils.JwtUtil;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@ResponseBody
@RequestMapping("/admin")
public class OperatorController {
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private HttpServletRequest request;

    /**
     * 获取管理台用户
     * @param params
     * @return
     */
    @RequestMapping("/operator/lists")
    public Result getOperatorList(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> result = new HashMap<>();
        Integer count = operatorService.countOperator();
        List<Map<String, Object>> list = operatorService.getOperatorList(params);
        result.put("total", count);
        result.put("data", list);
        return new Result(StatusCode.OK, true, "管理用户列表获取成功", result);
    }

    /**
     * 修改密码*/
    @RequestMapping("/operator/updatePassword")
    public Result changePassword(
    		@RequestBody  Map<String,Object> param) throws Exception {
	    String token = request.getHeader("Admin-Sign-Token");
	    Map<String, Object> operator = new JwtUtil().tokenCheck(token);

	    operatorService.changePassword(Integer.parseInt(operator.get("id").toString()),param.get("old_password").toString() ,param.get("new_password").toString());
	    return new Result(StatusCode.OK, true, "修改密码成功", "");
    }



    /**
     * 获取分组信息
     * @param params
     * @return
     */
    @RequestMapping("/authGroup/lists")
    public Result getAuthGroupList(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = operatorService.getAuthGroupList(params);
        return new Result(StatusCode.OK, true, "角色组获取成功", list);
    }

    /**
     * 组织架构
     * @param params
     * @return
     */
    @RequestMapping("/schoolStructure/getlists")
    public Result getSchoolStructureList(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = operatorService.getSchoolStructureList(params);
        return new Result(StatusCode.OK, true, "组织架构获取成功", list);
    } /**
     * 年级下班级数据
     * @param params
     * @return
     */
    @RequestMapping("/schoolStructure/getClassList")
    public Result getClassList(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = operatorService.getClassList(params);
        return new Result(StatusCode.OK, true, "班级数据获取成功", list);
    }

    /**
     * 获取用户的班级权限
     * @param params
     * @return
     */
    @RequestMapping("/operator/getOperatorInfo")
    public Result getOperatorClass(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> operator = operatorService.getOperatorById(params);
        List<String> operatorClass = operatorService.getOperatorClass(params);
        operator.put("unit_class", operatorClass);
        return new Result(StatusCode.OK, true, "管理用户信息获取成功", operator);
    }

    @RequestMapping(value="/operator/getAuthRulesTree", method = RequestMethod.POST)
    public Result getAuthRulesTree(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = operatorService.getAuthRulesTree(params);
        return new Result(StatusCode.OK, true, "管理用户权限获取成功", list);
    }


    @RequestMapping(value="/operator/assignRules", method = RequestMethod.POST)
    public Result assignRules(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.assignRules(params);
        return new Result(StatusCode.OK, true, "管理用户权限添加成功", "");
    }

    @RequestMapping(value="/operator/delete", method = RequestMethod.POST)
    public Result deleteOperator(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.deleteOperator(params);
        return new Result(StatusCode.OK, true, "删除用户成功", "");
    }

    @RequestMapping(value="/operator/create", method = RequestMethod.POST)
    public Result createOperator(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.createOperator(params);
        return new Result(StatusCode.OK, true, "创建管理用户成功", "");
    }

    @RequestMapping(value="/authGroup/update", method = RequestMethod.POST)
    public Result updateAuthGroup(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.updateAuthGroup(params);
        return new Result(StatusCode.OK, true, "更新角色组成功", "");
    }

    @RequestMapping(value = "/authGroup/getAuthRulesTree", method = RequestMethod.POST)
    public Result getAuthGroupRulesTree(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = operatorService.getAuthRulesTree(params);
        return new Result(StatusCode.OK, true, "获取角色组权限成功", list);
    }

    @RequestMapping(value = "/authGroup/assignRules", method = RequestMethod.POST)
    public Result assignAuthGroupRules(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.assignAuthGroupRules(params);
        return new Result(StatusCode.OK, true, "角色组权限添加成功", "");
    }

    @RequestMapping(value = "/authGroup/updateStatus", method = RequestMethod.POST)
    public Result updateAuthGroupStatus(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.updateAuthGroupStatus(params);
        return new Result(StatusCode.OK, true, "角色组状态修改成功", "");
    }

    @RequestMapping(value = "/authGroup/delete", method = RequestMethod.POST)
    public Result deleteAuthGroupStatus(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.deleteAuthGroup(params);
        return new Result(StatusCode.OK, true, "角色组删除成功", "");
    }

    @RequestMapping(value = "/authGroup/create", method = RequestMethod.POST)
    public Result addAuthGroupStatus(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.addAuthGroup(params);
        return new Result(StatusCode.OK, true, "角色组新增成功", "");
    }

    @RequestMapping(value = "/authRule/lists")
    public Result getAuthRuleList(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        params.put("pageRule", "");
        List<Map<String, Object>> list = operatorService.getAuthRulesTree(params);
        return new Result(StatusCode.OK, true, "页面权限列表获取成功", list);
    }

    @RequestMapping(value = "/authRule/parent/list")
    public Result getAuthRuleParentList(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> list = operatorService.getAuthRuleParentList(params);
        return new Result(StatusCode.OK, true, "页面权限父级列表获取成功", list);
    }

    @RequestMapping(value = "/authRule/update", method = RequestMethod.POST)
    public Result updateAuthRule(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.updateAuthRule(params);
        return new Result(StatusCode.OK, true, "页面权限更新成功", "");
    }

    @RequestMapping(value = "/authRule/create", method = RequestMethod.POST)
    public Result createAuthRule(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.createAuthRule(params);
        return new Result(StatusCode.OK, true, "页面权限新增成功", "");
    }

    @RequestMapping(value = "/authRule/delete", method = RequestMethod.POST)
    public Result deleteAuthRule(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.deleteAuthRule(params);
        return new Result(StatusCode.OK, true, "页面权限删除成功", "");
    }

    @RequestMapping(value = "/config/getConfigGroup", method = RequestMethod.POST)
    public Result getConfigGroup() throws Exception {
        JSONArray data = operatorService.getConfigGroup();
        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/config/getConfigType", method = RequestMethod.POST)
    public Result getConfigType() throws Exception {
        JSONArray data = operatorService.getConfigType();
        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/config/getGroupLists", method = RequestMethod.POST)
    public Result getGroupLists(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        HashMap<String, Object> data = new HashMap<>();

        List<Map<String, Object>> list = operatorService.getGroupLists(params);

        data.put("title", "基本配置");
        data.put("data", list);

        return new Result(StatusCode.OK, true, "系统参数配置获取成功", data);
    }

    @RequestMapping(value = "/config/saveConfig", method = RequestMethod.POST)
    public Result saveConfig(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.saveConfig(params);
        return new Result(StatusCode.OK, true, "系统参数配置修改成功", "");
    }

    @RequestMapping(value = "/config/lists")
    public Result getConfigList(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> data = operatorService.getConfigList(params);
        return new Result(StatusCode.OK, true, "系统参数配置获取成功", data);
    }


    @RequestMapping(value = "/operator/update", method = RequestMethod.POST)
    public Result updateOperator(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.updateOperator(params);
        return new Result(StatusCode.OK, true, "更新管理员信息成功", "");
    }

    @RequestMapping(value = "/operator/assignGroup", method = RequestMethod.POST)
    public Result OperatorAssignGroup(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.operatorAssignGroup(params);
        return new Result(StatusCode.OK, true, "设置管理员角色成功", "");
    }

    @RequestMapping(value = "/config/getData", method = RequestMethod.POST)
    public Result getConfigData(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> configData = operatorService.getConfigData(params);
        return new Result(StatusCode.OK, true, "获取参数配置详情成功", configData);
    }


    @RequestMapping(value = "/Config/update", method = RequestMethod.POST)
    public Result updateConfigData(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.updateConfigData(params);
        return new Result(StatusCode.OK, true, "修改参数配置详情成功", "");
    }

    @RequestMapping(value = "config/create", method = RequestMethod.POST)
    public Result createConfigData(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.createConfigData(params);
        return new Result(StatusCode.OK, true, "新增参数配置成功", "");
    }

    @RequestMapping(value = "config/delete", method = RequestMethod.POST)
    public Result deleteConfigData(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        operatorService.deleteConfigData(params);
        return new Result(StatusCode.OK, true, "删除参数配置成功", "");
    }
}
