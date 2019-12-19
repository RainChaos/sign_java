package cn.gljp.sign.admin.mapper.sign;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper
public interface StatisticsMapper {

    /**
     * 获取首页学生统计
     * @param params
     * @return
     */
    Map<String, Object> getStudentCount(Map<String, Object> params);

    /**
     * 获取专业统计
     * @param params
     * @return
     */
    List<LinkedHashMap<String, Object>> getMajorCount(Map<String, Object> params);

    /**
     * 获取系部统计
     * @param params
     * @return
     */
    List<LinkedHashMap<String, Object>> getDeptCount(Map<String, Object> params);

    /**
     * 查询省市区字段
     * @return
     */
    List<Map<String, Object>> getDataTypeIsSSQ();

    /**
     * 获取该省市区人数统计
     * @param params
     * @return
     */
    LinkedHashMap<String, Object> getSSQCount(Map<String, Object> params);

    /**
     * 获取省级编码
     * @param params
     * @return
     */
    List<LinkedHashMap<String, Object>> getProvinceList(Map<String, Object> params);

    /**
     * 获取该省市区为空人数统计
     * @param params
     * @return
     */
    LinkedHashMap<String, Object> getSSQEmptyCount(Map<String, Object> params);

    List<LinkedHashMap<String, Object>> getCityList(Map<String, Object> params);

    List<LinkedHashMap<String, Object>> getFieldNameList(Map<String, Object> params);

    LinkedHashMap<String, Object> getFieldNameCount(Map<String, Object> params);

    LinkedHashMap<String, Object> getFieldNameEmptyCount(Map<String, Object> params);

    List<LinkedHashMap<String, Object>> getFieldNameStatisticsConfig();

    List<LinkedHashMap<String, Object>> getClassCount(Map<String, Object> params);
}
