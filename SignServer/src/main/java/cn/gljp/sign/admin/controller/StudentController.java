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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@ResponseBody
@RequestMapping("/admin")
@CrossOrigin
public class StudentController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    private AdminService adminService;

    @Autowired
    private SchoolStructureService schoolStructureService;

    @Autowired
    private BatchService batchService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private DictService dictService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private OperatorService operatorService;

    @Autowired
    private LogService logService;

    /*
     * 自动分班
     * */
    @RequestMapping(value = "/student/autoFenBan")
    public Result autoFenBan(@RequestBody Map<String, Object> params) throws Exception {
        String token = request.getHeader("Admin-Sign-Token");
        params.put("token",token);
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        try {
            //如果班级区分是否劳动预备制
            if ("false".equals(params.get("is_ldybz").toString())){
                 studentService.autoFenBan(params);
            }else{
                //如果班级区分劳动预备制
                studentService.autoFenBanByLDYBZ(params);
            }
            return new Result(StatusCode.OK, true, "自动分班成功","");
        }catch (Exception e){
            throw new Exception( "错误：自动分班失败");
        }
    }
    /*
     * 修改学籍状态
     * */
    @RequestMapping(value = "/student/changeStudentStatus")
    public Result changeStudentStatus(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        try {
            String [] ids=params.get("student_ids").toString().split(",");
            for (String id:ids){
                Map<String,Object> map=new HashMap<>();
                map.put("status",params.get("status"));
                map.put("remark",params.get("remark"));
                map.put("student_id",id);
                //修改学生状态和备注
                studentService.changeStudentStatus(map);
            }
            return new Result(StatusCode.OK, true, "修改成功","");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "修改失败","");
        }
    }
    /*
     * 调整班级
     * */
    @RequestMapping(value = "/student/changeUnitCode")
    public Result changeUnitCode(@RequestBody Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        try {
            String [] ids=params.get("student_ids").toString().split(",");
            for (String id:ids){
                Map<String,Object> map=new HashMap<>();
                map.put("zbyy",params.get("remark"));
                map.put("targetClass",params.get("unit_code"));
                map.put("student_id",id);
                //修改报名系统的学生单位代码和转班备注
                studentService.modifyStudentClass2(map);
                //调整班级时修改收费系统的学生单位代码和收费项目
                studentService.changeChargeItemsByStudentIdAndUnitCode(params.get("unit_code").toString(),id);
            }
            return new Result(StatusCode.OK, true, "调整成功","");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "调整失败","");
        }
    }
    /*
     * 后台学生获取收费项目
     * */
    @GetMapping(value = "/student/getStudentChargeItems")
    public Result getStudentChargeItems(@RequestParam Map<String, Object> map){
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        try {
            Map<String, Object> stuMap=studentService.getStudentInfoBySno(map);
            map.put("student_id",stuMap.get("id"));
            List<Map<String, Object>> studentChargeItemList=studentService.getStudentChargeItems(map);
            return new Result(StatusCode.OK, true, "获取学生收费项目信息成功",studentChargeItemList);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "获取学生收费项目信息失败","");
        }

    }
    /**
     * 获取学生列表
     * @param params
     * @return
     */
    @RequestMapping(value = "/student/lists")
    public Result getStudentList(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        if (params.get("pici_id")==null){
            params.put("pici_id",configService.getCurrentPici());
        }
        Map<String, Object> operator = operatorService.loginCheck(request);
        HashMap<String, Object> result = new HashMap<>();
        List<Map<String, Object>> formConfigLists = dictService.getFormConfigLists(params);
        LinkedHashMap<String, Object> page = studentService.getStudentList(params, formConfigLists, operator);

        result.put("data", page.get("data"));
        result.put("columns", formConfigLists);
        result.put("total", page.get("total"));

        return new Result(StatusCode.OK, true, "", result);
    }


    @RequestMapping(value = "/student/getSearchField")
    public Result getSearchField(@RequestParam Map<String, Object> params){
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> field = studentService.getSearchField(params);

        return new Result(StatusCode.OK, true, "", field);
    }

    @RequestMapping(value = "/student/getStudentInfo")
    public Result getStudentInfo(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        Map<String, Object> student = studentService.getStudentInfoById(params);

        return new Result(StatusCode.OK, true, "", student);
    }
    @RequestMapping(value = "/student/deleteStudentInfo")
    public Result deleteStudentInfo(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> student = studentService.getStudentInfoById(params);
        //删除学生数据
        studentService.deleteStudentInfo(params);
        request.setAttribute("old_data", JSONUtil.toJsonStr(student));
        request.setAttribute("new_data", JSONUtil.toJsonStr(student));
        logService.addStudentLog(request);
        return new Result(StatusCode.OK, true, "删除成功", "");
    }

    @RequestMapping(value = "/student/getEditFormConfigLists")
    public Result getEditFormConfigList(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> data = studentService.getEditFormConfigList(params);


        return new Result(StatusCode.OK, true, "", data);
    }
    /*
     * 后台管理端编辑学生信息
     * */
    @PostMapping(value = "/student/editStudentInfo")
    public Result editStudentInfo(@RequestBody Map<String, Object> map) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        Map<String, Object> old_data = studentService.getStudentInfoById(map);

        //修改学生到收费系统和报名系统
        Map<String, Object> studentInfo = studentService.editStudentInfo(map);

        request.setAttribute("old_data", JSONUtil.toJsonStr(old_data));
        request.setAttribute("new_data", JSONUtil.toJsonStr(studentInfo));
        logService.addStudentLog(request);

        return new Result(StatusCode.OK, true, "编辑学生信息成功","");

    }
    /*
     * 修改学生是否到校报到
     * */
    @PostMapping(value = "/student/setRegister")
    public Result setRegister(@RequestBody Map<String, Object> map){
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        Map<String, Object> old_data = studentService.getStudentInfoById(map);

        studentService.setRegister(map);

        Map<String, Object> new_data = studentService.getStudentInfoById(map);
        request.setAttribute("old_data", JSONUtil.toJsonStr(old_data));
        request.setAttribute("new_data", JSONUtil.toJsonStr(new_data));
        logService.addStudentLog(request);
        return new Result(StatusCode.OK, true, "操作成功","");

    }
    /*
     * 后台编辑预备制
     * */
    @PostMapping(value = "/student/editStudentInfoForLDYBZ")
    public Result editStudentInfoForLDYBZ(@RequestBody Map<String, Object> map){
        request.setAttribute("params", JSONUtil.toJsonStr(map));
        Map<String, Object> old_data = studentService.getStudentInfoById(map);
        map.remove("unit_code");


        studentService.editStudentInfoForLDYBZ(map);

        Map<String, Object> new_data = studentService.getStudentInfoById(map);
        request.setAttribute("old_data", JSONUtil.toJsonStr(old_data));
        request.setAttribute("new_data", JSONUtil.toJsonStr(new_data));
        logService.addStudentLog(request);
        return new Result(StatusCode.OK, true, "申请劳动预备制成功","");

    }
    /*
     * 后台管理端新增学生
     * */
    @PostMapping(value = "/student/addStudentInfo")
    public Result addStudent(@RequestBody Map<String, Object> map) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(map));

            //格式校验，去掉前后空格
            Map<String, Object> dataFormat=studentService.dataFormat(map);
            //获取判断学生是否存在的唯一的字段
            List<String> unitqueFieldList=studentService.getUnitqueFieldList(dataFormat);
            //根据唯一字段判断学生是否存在
            Boolean isHaveStudent=false;
            if (unitqueFieldList.size()>0){
                isHaveStudent=studentService.isHaveStudent(dataFormat,unitqueFieldList);
            }
            if (isHaveStudent){
                String stuName=dataFormat.get("name").toString();
                return new Result(StatusCode.ERROR, false, stuName+"同学，您已报名，请用姓名身份证号登录", "");
            }
            //获取配置数据
            Config config = configService.getConfig();
            //根据专业代码和批次获取单位数据
            Map<String, Object> majorUnit=schoolStructureService.queryUnitMajor(dataFormat);
            //获取学生班级单位，并且判断人数是否达到限制
            String classCode=studentService.getStudentClassCode(config,dataFormat,majorUnit);
            dataFormat.put("unit_code",classCode);
            //根据班级代码生成学号
            String sno=studentService.getNewSID(classCode,config);
            dataFormat.put("sno",sno);
            //添加学生到收费系统和报名系统
            Map<String, Object> studentInfo = studentService.addStudentInfo(dataFormat);

            request.setAttribute("old_data", JSONUtil.toJsonStr(studentInfo));
            request.setAttribute("new_data", JSONUtil.toJsonStr(studentInfo));
            logService.addStudentLog(request);

            return new Result(StatusCode.OK, true, "添加学生成功", studentInfo);


    }
}
