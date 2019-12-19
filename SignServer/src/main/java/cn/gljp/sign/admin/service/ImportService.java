package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.BaseFile;
import cn.gljp.sign.admin.mapper.sign.StudentMapper;
import cn.gljp.sign.utils.FileUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.util.*;

@Service
public class ImportService {
    @Autowired
    private FieldService fieldService;
    @Autowired
    private SchoolStructureService schoolStructureService;
    @Autowired
    private RegionService regionService;

    /**
     * 写入文件
     * @param file
     * @param importStudentUrl
     * @return
     */
    public String excelImport(MultipartFile file, String importStudentUrl) {
        ArrayList<Object> upload = FileUtil.upload(new MultipartFile[]{file}, importStudentUrl);
        System.out.println(upload);
        Map <String, Object> map = (HashMap<String, Object>)upload.get(0);
        return importStudentUrl + map.get("url");
    }

    /**
     * 导入学生数据生成
     * @param url
     * @return
     */
    public List<Map<String, Object>> studentDataGenerate(String url) {
        //读取excel
        ExcelReader reader = ExcelUtil.getReader(url);
        List<Map<String,Object>> stuList = reader.readAll();
        //获取字段列表
        List<Map<String,Object>> fieldList = fieldService.getField();
        Map<String, Object> field = new HashMap<>();
        Map<String, Object> type = new HashMap<>();
        fieldService.fieldNameAndTypeFormat(fieldList, field, type);

        //封装的学生数据
        LinkedList<Map<String,Object>> stuLinkedList = new LinkedList<>();

        for (Map<String,Object> stu : stuList) {
            Map<String, Object> student = new HashMap<>();
            for(String key : stu.keySet()){
                String fieldName = field.get(key).toString().trim();
                String fieldValue = stu.get(key) == null ? "" : stu.get(key).toString();

                if(type.get(fieldName).equals("单位结构")){
                    String majorCode = schoolStructureService.getMajorCodeByName(fieldValue);
                    fieldValue =  majorCode;
                }
                if(type.get(fieldName).equals("省市区")){
                    String[] region = fieldValue.split("，");
                    if (region.length == 3) {
                        String code = regionService.getCodeByAllName(region[0], region[1], region[2]);
                        fieldValue =  code;
                    }
                }
                student.put(fieldName, fieldValue.trim());
            }
            stuLinkedList.add(student);
        }

        return stuLinkedList;
    }

}
