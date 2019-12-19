package cn.gljp.sign.admin.mapper.sign;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {
    List<LinkedHashMap<String, Object>> getStudentList(Map<String, Object> params);

    String stuInfoConvert(HashMap<String, Object> map);

    Long getStudentListCount(Map<String, Object> params);

    void addStudentInfo(@Param("map") Map<String, Object> map);

    List<Map<String, Object>> getStudentField(Map<String, Object> params);

    Integer isHaveStudent(@Param("map") Map<String, Object> searchMap);

    List<String> getUnitqueFieldList(@Param("map") Map<String, Object> map);

    void addSignStudentInfo(@Param("map") Map<String, Object> map);

    Map<String, Object> getStudentInfoById(Map<String, Object> params);

    void deleteStuImage(@Param("url")String url,@Param("field_name") String field_name);

    void editSignStudentInfo(@Param("map") Map<String, Object> dataFormat);

    Map<String, Object> getStudentInfoBySno(Map<String, Object> map);

    List<Map<String, Object>> searchStudentInfo(Map<String, Object> dataFormat);

    Integer getLdybzCount(Map<String, Object> majorUnit);

    void modifyStudentClass(Map<String, Object> map);

    void setRegister(Map<String, Object> map);

    void changeStudentStatus(Map<String, Object> map);

    List<Map<String, Object>> getStudentListBySearch(Map<String, Object> params);

    void changeUnitCodeByID(Map<String, Object> stuMap);

    List<String> getSnoById(String[] ids);

    void setStudentDorm(Map<String, Object> params);

    void deleteStudentInfo(Map<String, Object> params);
}
