package cn.gljp.sign.admin.mapper.sign;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SchoolMapper {
    List<Map<String, Object>> getSchoolStructure(Map<String, Object> map);

    List<Map<String, Object>> getSchoolStructure2(Map<String, Object> map);

    Map<String, Object> getMajorExtendField(Map<String, Object> map);

    List<Map<String, Object>> getSelectUnitClass(Map<String, Object> map);
    Map<String, Object> queryUnitClass(Map<String, Object> paramMap);
    List<Map<String, Object>> queryUnitClassV2(Map<String, Object> paramMap);

    Map<String, Object> queryMajor(Map<String, Object> params);

    Map<String, Object> queryUnitMajor(Map<String, Object> params);

    void addUnitMajor(Map<String, Object> unitMajor);

    void deleteUnitMajor(Map<String, Object> params);

    void updateUnitMajor(Map<String, Object> params);

    void updateUnitClass(Map<String, Object> params);

    void deleteSelectUnitClass(Map<String, Object> params);

    void deleteSelectUnitClassV2(Map<String, Object> params);

    List<Map<String, Object>> getClassLists(Map<String, Object> params);

    void createSelectUnitClass(Map<String, Object> params);

    Integer classExist(Map<String, Object> params);

    void deleteSelectUnitClassWhenDeleteSelectUnit(Map<String, Object> params);

    int getSelectUnitCount(Map<String, Object> params);

    List<Map<String, Object>> getSelectUnitList(Map<String, Object> map);

    List<Map<String, Object>> getSelectDept(Map<String, Object> map);

    Integer classHasStudent(Map<String, Object> params);

    Integer unitHasStudent(Map<String, Object> params);

    void deleteSelectUnit(Map<String, Object> params);

    Integer unitHasStudentV2(Map<String, Object> params);
    Map<String, Object> getUnitByUnitCode(String unitCode);

    Map<String, Object> isHaveGrade(@Param("gradeLength") Integer gradeLength,@Param("currentGrade") String currentGrade,@Param("unitCode") String unitCode);

    List<Map<String, Object>> getClassListByMajor(Map<String, Object> majorUnit);

    Integer getStudentCountByClassCode(@Param("classCode") String classCode,@Param("type") Integer type);

    String getUnitCodeByID(Map<String, Object> map);

    List<Map<String, Object>> getClassListByNID(@Param("nid") String nid);

    void addSchoolStructure(Map<String, Object> map);

    void updateSchoolStructure(Map<String, Object> map);

    void deleteSchoolStructure(Map<String, Object> map);

    void deleteSelectUnitClassByUnitID(Map<String, Object> map);

    Map<String, Object> queryMajor2(Map<String, Object> signSchoolStructure);

    void updateSelectUnitBySelfCode(Map<String, Object> signSchoolStructure);

    void updateSelectUnitClassBySelfCode(Map<String, Object> signSchoolStructure);

    void updateStudentByUnitCode(Map<String, Object> signSchoolStructure);

    String getMajorCodeByName(String name);

    void updateSchoolStructure2(Map<String, Object> signSchoolStructure);

    Map<String, Object> queryUnitClassV3(@Param("nianjiCode")String nianjiCode,@Param("banjiName") String banjiName);

    String getClassUnitItemByNID(@Param("nid")String nianjiCode);
}
