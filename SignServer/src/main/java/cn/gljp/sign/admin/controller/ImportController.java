package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.service.ConfigService;
import cn.gljp.sign.admin.service.ImportService;
import cn.gljp.sign.admin.service.SchoolStructureService;
import cn.gljp.sign.admin.service.StudentService;
import cn.gljp.sign.utils.FileUtil;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@RestController
@ResponseBody
@RequestMapping("/admin")
@CrossOrigin
public class ImportController {
    @Autowired
    private ImportService importService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private ConfigService configService;
    @Autowired
    private SchoolStructureService schoolStructureService;
    @Autowired
    private HttpServletRequest request;

    @Value("${importStudentUrl}")
    private String importStudentUrl;
    @RequestMapping("/importStudent/uploadExcel")
    public Result importStudent(MultipartFile file) throws Exception {
        String url = importService.excelImport(file, importStudentUrl);
        return new Result(StatusCode.OK, true,"", url);
    }

    @RequestMapping("/importStudent/importStudentData")
    public Result importStudentData (@RequestBody Map<String, Object> params) throws Exception {
        request.setAttribute("params", JSONUtil.toJsonStr(params));
        String excelFilePath = params.get("excelFilePath") == null ? "" : params.get("excelFilePath").toString();
        String pici = params.get("pici") == null ? "" : params.get("pici").toString();
        if(excelFilePath.equals("")){
            throw new Exception("错误：excel未上传");
        }
        if(pici.equals("")){
            throw new Exception("错误：批次未选择");
        }
        List<Map<String,Object>> studentList = importService.studentDataGenerate(excelFilePath);
        System.out.println("studentList"+studentList);

        LinkedList<String> errorList = new LinkedList<>();

        for (Map<String, Object> stu: studentList) {
            try {
                stu.put("pici_id", pici);
                //获取判断学生是否存在的唯一的字段
                List<String> unitqueFieldList = studentService.getUnitqueFieldList(stu);
                //根据唯一字段判断学生是否存在
                Boolean isHaveStudent = false;
                if (unitqueFieldList.size()>0){
                    isHaveStudent = studentService.isHaveStudent(stu,unitqueFieldList);
                }
                if (isHaveStudent){
                    String msg = stu.get("name") + "未导入：" + "当前批次下该学生已报名";
                    errorList.add(msg);
                    continue;
                }
                //获取配置数据
                Config config = configService.getConfig();
                //根据专业代码和批次获取单位数据
                Map<String, Object> majorUnit = schoolStructureService.queryUnitMajor(stu);
                if(majorUnit == null){
                    String msg = stu.get("name") + "未导入：" + "当前批次下未查询到该专业，请重新核对专业名称";
                    errorList.add(msg);
                    continue;
                }
                //获取学生班级单位，并且判断人数是否达到限制
                String classCode = studentService.getStudentClassCode(config, stu, majorUnit);
                stu.put("unit_code",classCode);
                //根据班级代码生成学号
                String sno = studentService.getNewSID(classCode,config);
                stu.put("sno",sno);
                studentService.addStudentInfo(stu);
            }catch (Exception e){
                String msg = stu.get("name") + "未导入：" + e.getMessage();
                errorList.add(msg);
                continue;
            }
        }

        return new Result(StatusCode.OK, true,"", errorList);
    }
}
