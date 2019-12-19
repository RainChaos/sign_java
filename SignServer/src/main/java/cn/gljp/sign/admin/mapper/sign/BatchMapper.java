package cn.gljp.sign.admin.mapper.sign;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface BatchMapper {
    List<Map<String, Object>> getBatch(Map<String, Object> params);

    Integer getBatchCount();

    void updateBatch(Map<String, Object> params);

    void createBatch(Map<String, Object> params);

    Integer currentBatchStudentCount(Map<String, Object> params);

    void deleteBatch(Map<String, Object> params);

    Map<String, Object> getPiciInfoById(@Param("id") String pici_id);

    Integer getBatchCountV2();

    List<Map<String, Object>> getBatchV2(Map<String, Object> params);
}
