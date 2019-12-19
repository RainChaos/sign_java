package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.BaseFile;
import cn.gljp.sign.admin.mapper.sign.FieldMapper;
import cn.gljp.sign.admin.mapper.sign.StudentMapper;
import cn.gljp.sign.utils.FileUtil;
import cn.gljp.sign.utils.Utils;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

@Service
public class ExportService {
    @Value("${web.upload-path}")
    private String baseUrl;
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
    @Autowired
    private FieldMapper fieldMapper;

    public String fileName(){
        int hashcode = UUID.randomUUID().toString().replace("-", "").toUpperCase() .hashCode();
        int dir1 = hashcode & 0xf; //得到名为1到16的下及文件夹
        int dir2 = (hashcode & 0xf0) >> 4; //得到名为1到16的下下及文件夹
        String url = File.separator + "excel" + File.separator + dir1 + File.separator + dir2 + File.separator + System.currentTimeMillis() + ".xlsx";
        return url;
    }

    public void title(BigExcelWriter writer){
        writer.addHeaderAlias("count", "总数");
        writer.addHeaderAlias("nan_count", "男生");
        writer.addHeaderAlias("nv_count", "女生");
        writer.addHeaderAlias("pay_count", "已缴费");
        writer.addHeaderAlias("pay_nan_count", "已缴费男生");
        writer.addHeaderAlias("pay_nv_count", "已缴费女生");
        writer.addHeaderAlias("register_count", "已报到");
        writer.addHeaderAlias("register_nan_count", "已报到男生");
        writer.addHeaderAlias("register_nv_count", "已报到女生");
    }

    public void majorTitle(BigExcelWriter writer){
        writer.addHeaderAlias("student_count", "总数");
        writer.addHeaderAlias("student_nan_count", "男生");
        writer.addHeaderAlias("student_nv_count", "女生");
        writer.addHeaderAlias("student_pay_count", "已缴费");
        writer.addHeaderAlias("student_nan_pay_count", "已缴费男生");
        writer.addHeaderAlias("student_nv_pay_count", "已缴费女生");
        writer.addHeaderAlias("student_register_count", "已报到");
        writer.addHeaderAlias("student_nan_register_count", "已报到男生");
        writer.addHeaderAlias("student_nv_register_count", "已报到女生");
    }

    public String exportRegionStatistics(List<LinkedHashMap<String, Object>> regionStatistics) {
        String fileName = fileName();
        BigExcelWriter writer = ExcelUtil.getBigWriter(baseUrl + fileName);
        writer.addHeaderAlias("provinceName", "省份");
        writer.addHeaderAlias("cityName", "城市");
        title(writer);
        writer.write(regionStatistics, true);
        writer.close();

        return fileName;
    }

    public String exportFieldNameStatistics(List<LinkedHashMap<String, Object>> fieldNameStatistics, Map<String, Object> params) {
        String fileName = fileName();
        BigExcelWriter writer = ExcelUtil.getBigWriter(baseUrl + fileName);
        writer.addHeaderAlias("code", params.get("fieldName").toString());
        title(writer);
        writer.write(fieldNameStatistics, true);
        writer.close();

        return fileName;
    }

    public String exportMajorStatistics(List<LinkedHashMap<String, Object>> majorCount, Map<String, Object> params) {
        for (LinkedHashMap<String, Object> item : majorCount) {
            item.remove("unit_code");
        }
        String fileName = fileName();
        BigExcelWriter writer = ExcelUtil.getBigWriter(baseUrl + fileName);
        writer.addHeaderAlias("school", "校区");
        writer.addHeaderAlias("unit_name", "院系/专业");
//        writer.addHeaderAlias("unit_code", "编码");
        majorTitle(writer);
        writer.write(majorCount, true);
        writer.close();

        return fileName;
    }

    public String exportStudentStatistics(List<LinkedHashMap<String, Object>> data, Map<String, Object> params, List<Map<String, Object>> formConfigLists) {
        Map<String, Object> fields = new HashMap<>();
//        List<Map<String, Object>> allField = fieldMapper.getAllField();
        String fileName = fileName();
        BigExcelWriter writer = ExcelUtil.getBigWriter(baseUrl + fileName);
        for (Map<String, Object> field : formConfigLists) {
//            fields.put(field.get("field_name").toString(), field.get("describe").toString());
//            if((boolean)field.get("is_export"))
            writer.addHeaderAlias(field.get("field_name").toString(), field.get("describe").toString());
        }

        writer.write(data, true);
        writer.close();

        return fileName;
    }

    public LinkedHashMap<String, Object> getStudentStatistics(Map<String, Object> params, List<Map<String, Object>> formConfigLists) throws Exception {
        params.put("limit", 1000000);
//        Long studentListCount = studentService.getStudentListCount(params);
        Map<String, Object> operator = operatorService.loginCheck(request);


        LinkedHashMap<String, Object> page = studentService.getStudentList(params, formConfigLists, operator);
        return page;
    }
}
