package cn.gljp.sign.admin.controller;


import cn.gljp.sign.admin.entity.BaseFile;
import cn.gljp.sign.admin.service.*;
import cn.gljp.sign.ueditor.ActionEnter;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

import static java.awt.SystemColor.info;

@RestController
@CrossOrigin   //支持跨域
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private RedisTemplate redisTemplate;
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
    private OperatorService operatorService;
    @Autowired
    private UploadService uploadService;
    @Autowired
    private AuthRuleService authRuleService;

    //    @Autowired
//    private UpdateService updateService;
    /*
     * 初始化系统
     * */
    @RequestMapping(value = "/update")
    public Result initSystem() throws Exception {
//        try {
////            updateService.initDb();
//        } catch (SQLException e) {
//            throw new Exception("错误：初始化系统失败");
//        }
        return new Result(StatusCode.OK, true, "初始化系统成功", "初始化系统成功");
    }

    @RequestMapping(value = "/config/getExpandedUnitCode")
    public Result getExpandedUnitCode() {
        String expandedUnitCode = configService.getExpandedUnitCode();
        return new Result(StatusCode.OK, true, expandedUnitCode, expandedUnitCode);
    }

    @RequestMapping(value = "/upload")
    public void config(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("application/json");

        String rootPath = request.getSession().getServletContext().getRealPath("/");
        try {
            String exec = new ActionEnter(request, rootPath).exec();
            PrintWriter writer = response.getWriter();
            writer.write(exec);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 登录
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/login/dologin", method = RequestMethod.POST)
    public Result doLogin(@RequestBody Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map<String, Object> operator = adminService.doLogin(params);
        return new Result(StatusCode.OK, true, "登录成功", operator);
    }



    /**
     * 登录后获取相关信息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/operator/getInfo")
    public Result getInfo(@RequestParam Map<String, Object> params) throws Exception {
        Map<String, Object> info = adminService.getInfo(params);
        return new Result(StatusCode.OK, true, "", info);
    }

    /**
     * 获取班级长度
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/config/getSchoolStructureLength")
    public Result getSchoolStructureLength(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Integer length = configService.getSchoolStructureLength();
        return new Result(StatusCode.OK, true, "", length);
    }

    /**
     * 获取当前批次
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/config/getCurrentPici")
    public Result getCurrentPici(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Integer pici = configService.getCurrentPici();
        return new Result(StatusCode.OK, true, "", pici);
    }

    /**
     * 获取系统名称
     */
    @RequestMapping(value = "/config/getWebSiteName")
    public Result getWebSiteName() {
        String webSiteName = configService.getWebSiteName();
        return new Result(StatusCode.OK, true, "", webSiteName);
    }

    /**
     * 获取批次列表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/pici/lists")
    public Result getPiciList(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        PageResult<Map<String, Object>> result = new PageResult<>((long) batchService.getBatchCount(), batchService.getBatch(params));
        return new Result(StatusCode.OK, true, "", result);


    }


    /**
     * 创建批次
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/pici/create", method = RequestMethod.POST)
    public Result createPici(@RequestBody Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        adminService.createPici(params);
        return new Result(StatusCode.OK, true, "", "");
    }

    /**
     * 更新批次信息
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/pici/update", method = RequestMethod.POST)
    public Result updatePici(@RequestBody Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        adminService.updatePici(params);
        return new Result(StatusCode.OK, true, "", "");
    }

    /**
     * 删除批次
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/pici/delete", method = RequestMethod.POST)
    public Result deletePici(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        batchService.currentBatchStudentCount(params);
        batchService.deleteBatch(params);
        return new Result(StatusCode.OK, true, "", "");
    }


    /**
     * 获取配置数据
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/main/getConfigData")
    public Result getConfigData(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        PageResult<Map<String, Object>> result = new PageResult<>((long) batchService.getBatchCount(), batchService.getBatch(params));
        return new Result(StatusCode.OK, true, "", result);
    }

    /**
     * 快速选择可选专业和班级
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/schoolStructure/addSelectUnit")
    public Result addSelectUnit(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        schoolStructureService.addSelectUnit(params);
        return new Result(StatusCode.OK, true, "快速选择可选专业成功", "");
    }

    /**
     * 获取架构——到专业
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/schoolStructure/getStructureOnlyYxZyLists")
    public Result getStructureOnlyYxZyLists(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map data = new HashMap<String, Object>();

//        Object schoolStructureCache = redisTemplate.opsForValue().get("SchoolStructure");
//        List<String> checkedListCache = (List<String>) redisTemplate.opsForValue().get("checkedList");
//        if(schoolStructureCache != null && checkedListCache != null){
//            data.put("schoolStructure", schoolStructureCache);
//            data.put("checkedList", checkedListCache);
//            return new Result(StatusCode.OK, true, "", data);
//        }

        List<String> checkedList = new LinkedList<>();
        Object schoolStructure = schoolStructureService.getSchoolStructure(params, checkedList, false, true);
        data.put("schoolStructure", schoolStructure);
        data.put("checkedList", checkedList);

//        redisTemplate.opsForValue().set("SchoolStructure", schoolStructure);
//        redisTemplate.opsForValue().set("checkedList", checkedList);

        return new Result(StatusCode.OK, true, "", data);
    }

    @RequestMapping(value = "/schoolStructure/getListsLimitByOperatorClass")
    public Result getListsLimitByOperatorClass(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Object id = params.get("operator_id");
        if (id == null) {
            return new Result(StatusCode.ERROR, false, "", "");
        }
        Map data = new HashMap<String, Object>();
        List<String> checkedList = new LinkedList<>();
        Object schoolStructure = operatorService.getSchoolStructureList(params);
        data.put("schoolStructure", schoolStructure);
        data.put("checkedList", checkedList);

        return new Result(StatusCode.OK, true, "", data);
    }

    /**
     * 获取当前批次已选择的专业
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/lists")
    public Result getSelectUnit(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        Map data = new HashMap<String, Object>();
        //为true时，会使得前端按钮不可选，只可选择报名专业
        boolean disable = true;
        if (null == params.get("farther_code")) {
            //新增学生页面，获取专业时不传递该参数
            params.put("farther_code", "001");
            //不禁用每一个选项，令其可选
            disable = false;

            //新增学生时，可选专业缓存
//            Object selectUnitTrueCache = redisTemplate.opsForValue().get("selectUnitTrue");
//            if(selectUnitTrueCache != null){
//                data.put("schoolStructure", selectUnitTrueCache);
//                return new Result(StatusCode.OK, true, "", data);
//            }
        }

        List<String> checkedList = new LinkedList<>();
        Object schoolStructure = schoolStructureService.getSchoolStructure(params, checkedList, true, disable);
        data.put("schoolStructure", schoolStructure);
        data.put("checkedList", checkedList);

//        if(!disable){
//            redisTemplate.opsForValue().set("selectUnitTrue", schoolStructure);
//        }

        return new Result(StatusCode.OK, true, "", data);
    }

    /**
     * 获取当前批次已选择的班级
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnitClass/lists")
    public Result getSelectUnitClass(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        List<Map<String, Object>> selectUnitClass = schoolStructureService.getSelectUnitClass(params);

        return new Result(StatusCode.OK, true, "", selectUnitClass);
    }

    /**
     * 获取当前批次当前学历下的班级数量
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/getSelectUnitCount")
    public Result getSelectUnitCount(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        int selectUnitCount = schoolStructureService.getSelectUnitCount(params);

        return new Result(StatusCode.OK, true, "", selectUnitCount);
    }

    /**
     * 添加专业
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/addSelectUnitFromStructure", method = RequestMethod.POST)
    public Result addSelectUnitFromStructure(@RequestBody Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        schoolStructureService.addSelectUnitFromStructure(params);
        schoolStructureService.updateCache();

        return new Result(StatusCode.OK, true, "", "");
    }

    /**
     * 删除专业
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/deleteSelectUnitFromStructure", method = RequestMethod.POST)
    public Result deleteSelectUnitFromStructure(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        schoolStructureService.deleteSelectUnitFromStructure(params);
        schoolStructureService.updateCache();

        return new Result(StatusCode.OK, true, "", "");
    }

    /**
     * 更新专业
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/update", method = RequestMethod.POST)
    public Result updateSelectUnit(@RequestBody Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        schoolStructureService.updateSelectUnit(params);

        schoolStructureService.updateCache();

        return new Result(StatusCode.OK, true, "", "");
    }

    /**
     * 更新班级
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnitClass/update", method = RequestMethod.POST)
    public Result updateSelectUnitClass(@RequestBody Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        schoolStructureService.updateSelectUnitClass(params);

        return new Result(StatusCode.OK, true, "", "");
    }

    /**
     * 删除班级
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnitClass/delete", method = RequestMethod.POST)
    public Result deleteSelectUnitClass(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        schoolStructureService.deleteSelectUnitClass(params);

        return new Result(StatusCode.OK, true, "", "");
    }

    /**
     * 获取班级列表
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/schoolStructure/getClassLists")
    public Result getClassLists(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        List<Map<String, Object>> classLists = schoolStructureService.getClassLists(params);

        return new Result(StatusCode.OK, true, "", classLists);
    }

    /**
     * 班级创建
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnitClass/create", method = RequestMethod.POST)
    public Result createSelectUnitClass(@RequestBody Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        schoolStructureService.createSelectUnitClass(params);

        return new Result(StatusCode.OK, true, "", "");
    }


    /**
     * 获取表单字段配置
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/student/getAddFormConfigLists")
    public Result getAddFormConfigLists(@RequestParam Map<String, Object> params) {
        request.setAttribute("params", JSONUtil.toJsonStr(params));

        List<Map<String, Object>> list = dictService.getAddFormConfigLists(params);

        return new Result(StatusCode.OK, true, "", list);
    }

    /**
     * 获取学历数据，供专业信息编辑时调用
     *
     * @return
     */
    @RequestMapping(value = "/student/education/get")
    public Result getStudentEducation() {
        List<Map<String, Object>> list = dictService.getStudentEducation();
        return new Result(StatusCode.OK, true, "", list);
    }

    /**
     * 可选单位专业删除操作
     *
     * @param params
     * @return
     */
    @RequestMapping(value = "/selectUnit/delete", method = RequestMethod.POST)
    public Result deleteSelectUnit(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        schoolStructureService.deleteSelectUnit(params);
        schoolStructureService.updateCache();
        return new Result(StatusCode.OK, true, "", "");
    }


    private String baseUrl;

    @Value("${web.upload-path}") //注解从配置文件读取值的用法 D:upload
    public void setBaseUrl(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    @RequestMapping(value = "/file/uploadImage")
    public Result uploadImage(@RequestParam("photo") MultipartFile file, BaseFile baseFile) throws Exception {
        uploadService.imgFileCheck(file, baseFile);
        uploadService.uploadImage(file, baseFile, baseUrl);
        configService.insertBaseFileSelective(baseFile);
        return new Result(StatusCode.OK, true, "上传成功", baseFile);
    }
    @RequestMapping(value = "/file/backGroundImage")
    public Result backGroundImage(@RequestParam("photo") MultipartFile file, BaseFile baseFile) throws Exception {
        uploadService.imgFileCheck(file, baseFile);
        uploadService.backGroundImage(file, baseFile, baseUrl);
        configService.insertBaseFileSelective(baseFile);
        return new Result(StatusCode.OK, true, "上传成功", baseFile);
    }

    @RequestMapping(value = "/authRule/getAuthRuleInterface")
    public Result getAuthRuleInterface(@RequestParam Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        List<Map<String, Object>> authRuleInterface = authRuleService.getAuthRuleInterface(params);
        return new Result(StatusCode.OK, true, "获取功能接口列表", authRuleInterface);
    }

    @RequestMapping(value = "/authRule/addAuthRuleInterface", method = RequestMethod.POST)
    public Result addAuthRuleInterface(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        authRuleService.addAuthRuleInterface(params);
        return new Result(StatusCode.OK, true, "新增功能接口", "");
    }

    @RequestMapping(value = "/authRule/deleteAuthRuleInterface", method = RequestMethod.POST)
    public Result deleteAuthRuleInterface(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        authRuleService.deleteAuthRuleInterface(params);
        return new Result(StatusCode.OK, true, "删除功能接口", "");
    }

    @RequestMapping(value = "/authRule/updateAuthRuleInterface", method = RequestMethod.POST)
    public Result updateAuthRuleInterface(@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        authRuleService.updateAuthRuleInterface(params);
        return new Result(StatusCode.OK, true, "更新功能接口", "");
    }
}
