package cn.gljp.sign.admin.mapper.charge;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SchoolStructureMapper {
    void updateUnitNameByUnitCode(Map<String, Object> params);

    void updateClassNameByClassCode(Map<String, Object> params);

    List<Map<String, Object>> getStuCountByUnitCode(@Param("classCode") String classCode);

    List<Map<String, Object>> getChargeItemListByUnitID(Map<String, Object> chargeStuMap);

    Integer getChildsCount(Map<String, Object> map);

    Map<String, Object> getNextUnitCode(Map<String, Object> map);

    Map<String, Object> queryUnitClass(Map<String, Object> map);

    void updateSchoolStructure(Map<String, Object> map);

    void addSchoolStructure(Map<String, Object> map);

    void addUnitChargeItem(Map<String, Object> classChargeItem);

    Integer getClassCountByUnitID(Map<String, Object> map);

    void deleteChargeSchoolStructure(Map<String, Object> map);

    void deleteUnitChargeItem(Map<String, Object> map);

    List<Map<String, Object>> getSchoolStructureList();
}
