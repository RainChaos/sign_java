package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.BaseFile;

import java.util.List;
import java.util.Map;

public interface ConfigMapper {
    List<Map<String, Object>> getConfig();

    String getWebSiteName();

    void insertBaseFileSelective(BaseFile baseFile);

    Map<String, Object> getConfigData(Map<String, Object> params);

    void updateConfigData(Map<String, Object> params);

    void createConfigData(Map<String, Object> params);

    void deleteConfigData(Map<String, Object> params);

    List<Map<String, Object>> getQQ();
}
