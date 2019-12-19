package cn.gljp.sign.admin.mapper.charge;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ChargeStudentMapper {

    int addChargeStudentInfo(@Param("map") Map<String, Object> chargeStuMap);

    Map<String, Object> getChargeItemByID(@Param("itemID") String itemID);

    void addStudentChargeItem(@Param("map") Map<String, Object> stuChargeItem);

    void editChargeStudentInfo(@Param("map") Map<String, Object> chargeStuMap,@Param("studentid") String sno);

    List<Map<String, Object>> getStudentChargeItems(Map<String, Object> map);

    List<Map<String, Object>> getStudentChargeById(@Param(value="ids") String ids);

    void modifyStudentClass(Map<String, Object> map);

    List<Map<String, Object>> getChargeItems();

    void deleteStudentInfo(Map<String, Object> params);
}
