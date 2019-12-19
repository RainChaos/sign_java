package cn.gljp.sign.index.controller;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.mapper.sign.OfferMapper;
import cn.gljp.sign.admin.service.*;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.core.img.ImgUtil;
import cn.hutool.core.io.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.xml.sax.SAXException;

import javax.servlet.http.HttpServletRequest;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController("StudentControllerV2")
@ResponseBody
@RequestMapping("/index")
@CrossOrigin
public class StudentController {

    @Autowired
    private StudentService studentService;
    @Autowired
    private SchoolStructureService schoolStructureService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private BatchService batchService;
    @Autowired
    private OfferService offerService;
    @Autowired
    private OfferMapper offerMapper;
    /*
     * 电脑端报名根据学生姓名和身份证号查询信息
     * */
    @RequestMapping(value = "/student/searchStudentInfo")
    public Result searchStudentInfo(@RequestParam Map<String, Object> map){
        try {
            //格式校验，去掉前后空格
            Map<String, Object> dataFormat=studentService.dataFormat(map);
            String name=dataFormat.get("name").toString();
            String idcode=dataFormat.get("idcode").toString();
            if (name==null||name==""){
                return new Result(StatusCode.ERROR, false, "姓名不能为空","");
            }
            if (idcode==null||idcode==""){
                return new Result(StatusCode.ERROR, false, "身份证号不能为空","");
            }
            //修改学生到收费系统和报名系统
            List<Map<String, Object>> studentInfo = studentService.searchStudentInfo(dataFormat);
            if (studentInfo.size()==0){
                return new Result(StatusCode.ERROR, false, "不存在该学生,请先报名后在查询学生信息","不存在该学生,请先报名后在查询学生信息");
            }
            if (studentInfo.size()>1){
                return new Result(StatusCode.ERROR, false, "学生信息重复，请联系学校老师进行处理","学生信息重复，请联系学校老师进行处理");
            }

            return new Result(StatusCode.OK, true, "查询学生信息成功",studentInfo.get(0));
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "查询学生信息失败","");
        }
    }
    /*
     * 根据学生ID获取班级列表
     * */
    @RequestMapping(value = "/student/getSameMajorClass")
    public Result getSameMajorClass(@RequestParam Map<String, Object> map){
        try {
            //根据学生ID获取班级ID
            String unitCode=studentService.getUnitCodeByID(map);
            //根据班级ID前15位获取该班级所属专业年级的所有班级
            List<Map<String, Object>> data=studentService.getClassListByNID(unitCode.substring(0,15));
            return new Result(StatusCode.OK, true, "获取调整班级列表成功",data);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "获取调整班级列表失败","");
        }

    }
    /*
     * 根据学生ID和要转换的班级ID转班级
     * */
    @RequestMapping(value = "/student/modifyStudentClass")
    public Result modifyStudentClass(@RequestParam Map<String, Object> map){
        try {
            studentService.modifyStudentClass(map);
            return new Result(StatusCode.OK, true, "调整班级成功","");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "调整班级失败","");
        }

    }
    /*
     * 电脑端报名获取学生编辑信息表单
     * */
    @RequestMapping(value = "/student/getEditFormConfigLists")
    public Result getEditFormConfigLists(@RequestParam Map<String, Object> map){
        try {
            List<Map<String, Object>> data=studentService.getEditFormConfigList(map);
            return new Result(StatusCode.OK, true, "获取学生编辑信息成功",data);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "获取学生编辑信息失败","");
        }

    }
    /*
     * 电脑端报名根据学生ID获取学生收费项目信息
     * */
    @GetMapping(value = "/student/getStudentChargeItems")
    public Result getStudentChargeItems(@RequestParam Map<String, Object> map){
        try {
            List<Map<String, Object>> studentChargeItemList=studentService.getStudentChargeItems(map);
            return new Result(StatusCode.OK, true, "获取学生收费项目信息成功",studentChargeItemList);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "获取学生收费项目信息失败","");
        }

    }
    /*
     * 电脑端报名根据学生ID获取学生收费项目信息
     * */
    @GetMapping(value = "/student/getShowChargeItems")
    public Result getShowChargeItems(){
        try {
            List<Map<String, Object>> showChargeItemList=studentService.getShowChargeItems();
            return new Result(StatusCode.OK, true, "获取收费项目信息成功",showChargeItemList);
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "获取收费项目信息失败","");
        }

    }
    /*
     * 电脑端报名根据编辑学生信息
     * */
    @PostMapping(value = "/student/editStudentInfo")
    public Result editStudentInfo(@RequestBody Map<String, Object> map){
        try {
            //修改学生到收费系统和报名系统
            Map<String, Object> studentInfo = studentService.editStudentInfo(map);
            return new Result(StatusCode.OK, true, "编辑学生信息成功","");
        }catch (Exception e){
            return new Result(StatusCode.ERROR, false, "编辑学生信息失败","");
        }
    }
    /*
     * 电脑端报名根据编辑学生信息
     * */
    @PostMapping(value = "/student/editStudentInfoForLDYBZ")
    public Result editStudentInfoForLDYBZ(@RequestBody Map<String, Object> map){
//        try {
            Map<String, Object> stuMap=studentService.getStudentInfoById(map);
            map.remove("student_id");
            map.put("sno",stuMap.get("sno"));
            map.remove("unit_code");
             studentService.editStudentInfoForLDYBZ(map);
            return new Result(StatusCode.OK, true, "申请劳动预备制成功","");
//        }catch (Exception e){
//            return new Result(StatusCode.ERROR, false, "申请劳动预备制失败","");
//        }
    }
    /*
    * 电脑端报名根据学生ID获取学生信息
    * */
    @GetMapping(value = "/student/getStudentInfo")
    public Result getStudentInfo(@RequestParam Map<String, Object> map) throws Exception {
            Map<String, Object> stuMap=studentService.getStudentInfoById(map);
            if (stuMap.size()==0||stuMap==null){
                return new Result(StatusCode.ERROR, false, "学生信息不存在","");
            }
            //获取学生缴费数据
            List<Map<String, Object>> studentChargeItemList=studentService.getStudentChargeItems(map);
            Double totalMoney= 0.00;
            for (Map<String, Object> chargeItem:studentChargeItemList){
                totalMoney+=Double.parseDouble(chargeItem.get("PayMoney").toString());
            }
            //如果已缴费，设置缴费状态
            if (totalMoney>0){
                Map<String,Object> chargeStatus=new HashMap<>();
                chargeStatus.put("id",map.get("id"));
                chargeStatus.put("is_pay","是");
                studentService.editStudentChargeStatus(chargeStatus);
            }
            //获取批次信息
            Map<String, Object> piciMap=batchService.getPiciInfoById(stuMap.get("pici_id").toString());
            //获取批次结束时间
            String is_end_charge=piciMap.get("is_end_charge").toString();
            //获取批次是否结束缴费
            String end_charge_time=piciMap.get("end_charge_time").toString();
            //根据班级代码和批次获取单位数据
            Map<String, Object> paramMap=new HashMap<>();
            paramMap.put("pici_id",stuMap.get("pici_id"));
            paramMap.put("unit_code",stuMap.get("unit_code").toString());
            Map<String, Object> classUnit=schoolStructureService.queryUnitClass(paramMap);
            stuMap.put("unit_code_banji",classUnit.get("unit_name"));
            //获取班级是否允许缴费
            String is_can_charge=classUnit.get("is_can_charge").toString();
            // 批次没结束收费， 单个班级可以交费，那么才可以交费
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            if (is_end_charge.equals("false")&&sdf.parse(end_charge_time).getTime()>new Date().getTime()&&is_can_charge.equals("true")){
                stuMap.put("is_can_charge",1);
            }else {
                stuMap.put("is_can_charge",0);
            }
            //根据专业代码和批次获取单位数据
            paramMap.put("unit_code",stuMap.get("unit_code").toString().substring(0,12));
            Map<String, Object> majorUnit=schoolStructureService.queryUnitMajor(paramMap);
            //是否是劳动预备制专业
            String is_ldybz=majorUnit.get("is_ldybz").toString();
            //劳动预备制招生人数
            String ldybz_number=majorUnit.get("ldybz_number").toString();
            //获取专业下劳动预备制人数
            Integer ldybzCount=studentService.getLdybzCount(majorUnit);
            if (stuMap.get("education")!=null){
                // 判断是否符合劳动预备制学员
                Boolean condition1 = ldybzCount < Integer.parseInt(ldybz_number); //申请的人数是否小于限定的人数
                Boolean condition2 = stuMap.get("education").toString().equals("初中生")||stuMap.get("education").toString().equals("高中生");
                Boolean condition3 = stuMap.get("student_type").toString().equals("应届生")&& stuMap.get("idcode").toString().startsWith("450");
                //如果专业属于劳动预备制并且学生符合三个条件，即可申请为劳动预备制学生
                if(is_ldybz.equals("true")&&condition1&&condition2&&condition3){
                    stuMap.put("is_fuhe_ldybz",1);
                }else {
                    stuMap.put("is_fuhe_ldybz",0);
                }
            }else {
                stuMap.put("is_fuhe_ldybz",0);
            }

            //根据院系代码获取单位数据
            paramMap.put("unit_code",stuMap.get("unit_code").toString().substring(0,9));
            Map<String, Object> yuanxiUnit=schoolStructureService.queryMajor(paramMap);
            stuMap.put("unit_code_yuanxi",yuanxiUnit.get("unit_name"));
            stuMap.put("unit_code_zhuanye",majorUnit.get("unit_name"));
            return new Result(StatusCode.OK, true, "获取学生信息成功",stuMap);

    }
    /*
    * 电脑端报名添加学生
    * */
    @PostMapping(value = "/student/addStudentInfo")
    public Result addStudent(@RequestBody Map<String, Object> map) throws Exception {
         //格式校验，去掉前后空格
         Map<String, Object> dataFormat=studentService.dataFormat(map);
         //获取判断学生是否存在的唯一的字段
         List<String> unitqueFieldList=studentService.getUnitqueFieldList(dataFormat);
         //根据唯一字段判断学生是否存在
         Boolean isHaveStudent=studentService.isHaveStudent(dataFormat,unitqueFieldList);
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
         return new Result(StatusCode.OK, true, "添加学生成功", studentInfo);
    }

    @GetMapping(value = "/student/getNotice")
    public Result getElectronicOffer(@RequestParam Map<String, Object> map) throws Exception {
        Map<String, Object> dataFormat = studentService.dataFormat(map);
        Map<String, Object> user = studentService.searchStudentInfoV2(dataFormat);
        List<Map<String, Object>> printAttr = offerMapper.getPrintAttr(map);
        System.out.println(printAttr);

        studentService.offerHtmlGenerate(printAttr, user);
        String url = studentService.getElectronicOffer(user);

        return new Result(StatusCode.OK, true, "", url);

    }
}
