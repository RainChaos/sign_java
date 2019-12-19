package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.mapper.sign.FieldMapper;
import cn.gljp.sign.admin.mapper.sign.StatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class StatisticsService {
    @Autowired
    private StatisticsMapper statisticsMapper;

    @Autowired
    private FieldMapper fieldMapper;

    public Map<String, Object> getStudentCount(Map<String, Object> params) {
        Map<String, Object> data = statisticsMapper.getStudentCount(params);
        return data;
    }

    public List<LinkedHashMap<String, Object>> getMajorCount(Map<String, Object> params) {
        List<LinkedHashMap<String, Object>> data = statisticsMapper.getMajorCount(params);
        return data;
    }

    public List<LinkedHashMap<String, Object>> getDeptCount(Map<String, Object> params) {
        List<LinkedHashMap<String, Object>> data = statisticsMapper.getDeptCount(params);
        return data;
    }

    /**
     * 查询为省市区的字段
     * @return
     */
    public List<Map<String, Object>> getDataTypeIsSSQ(){
        return statisticsMapper.getDataTypeIsSSQ();
    }

    /**
     * 按所在省统计
     * 参数应包含id，通过id查询base_student_field表field_name字段的值
     * @param params
     * @return
     */
    public List<LinkedHashMap<String, Object>> getRegionStatistics(Map<String, Object> params) {
        List<LinkedHashMap<String, Object>> data = new LinkedList<>();
        Integer ssqType = Integer.parseInt(params.get("ssqType").toString());

        List<LinkedHashMap<String, Object>> regionCodeList = null;

        switch (ssqType){
            case 1:
                regionCodeList = statisticsMapper.getProvinceList(params);
                break;
            case 2:
                regionCodeList = statisticsMapper.getCityList(params);
                break;
        }

        //非空code查询
        for(Map<String, Object> province : regionCodeList){
            int code = Integer.parseInt(province.get("code").toString());
            params.put("code", code);
            LinkedHashMap<String, Object> ssqCount = new LinkedHashMap<>();
            ssqCount.put("provinceName", province.get("provinceName"));
            if(ssqType == 2)
                ssqCount.put("cityName", province.get("cityName"));
            ssqCount.putAll(statisticsMapper.getSSQCount(params));
            data.add(ssqCount);
        }

        //空code查询
        LinkedHashMap<String, Object> ssqEmptyCount = new LinkedHashMap<>();
        ssqEmptyCount.put("provinceName", "其他");
        if(ssqType == 2)
            ssqEmptyCount.put("cityName", "");
        ssqEmptyCount.putAll(statisticsMapper.getSSQEmptyCount(params));

        data.add(ssqEmptyCount);

        return data;
    }

    /**
     * 获取指定字段统计
     * @param params
     * @return
     */
    public List<LinkedHashMap<String, Object>> getFieldNameStatistics(Map<String, Object> params) {
        List<LinkedHashMap<String, Object>> fieldNameList = statisticsMapper.getFieldNameList(params);

        for (LinkedHashMap<String, Object> item : fieldNameList) {
            params.put("code", item.get(params.get("field_name")));
            item.put("code", item.get(params.get("field_name")));
            item.remove(params.get("field_name").toString());
            LinkedHashMap<String, Object> fieldNameCount = statisticsMapper.getFieldNameCount(params);
            item.putAll(fieldNameCount);
        }

        LinkedHashMap<String, Object> fieldNameEmptyCount = new LinkedHashMap<>();
        fieldNameEmptyCount.put("code", "其他");
        fieldNameEmptyCount.putAll(statisticsMapper.getFieldNameEmptyCount(params));

        fieldNameList.add(fieldNameEmptyCount);
        return fieldNameList;
    }

    /**
     * 获取已配置的统计字段
     * @return
     */
    public List<LinkedHashMap<String, Object>> getFieldNameStatisticsConfig() {
        List<LinkedHashMap<String, Object>> data = statisticsMapper.getFieldNameStatisticsConfig();
        return data;
    }

    public List<LinkedHashMap<String, Object>> getClassCount(Map<String, Object> params) {
        return statisticsMapper.getClassCount(params);
    }
}
