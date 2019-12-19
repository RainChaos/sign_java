package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.BaseFile;
import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.mapper.sign.ConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ConfigService {
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private ConfigMapper configMapper;

    /**
     * 获取配置表信息
     * 缓存
     *
     * @return
     */
    public Config getConfig() {

        Config configCache = (Config) redisTemplate.opsForValue().get("config");
        if (configCache != null) {
            return configCache;
        }

        Config conf = new Config();
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Object>> config = configMapper.getConfig();
        for (Map<String, Object> item : config) {
            map.put(item.get("name").toString(), item.get("value"));
        }
        conf.setAdmissionsProfile(map.get("ADMISSIONS_PROFILE").toString());
        conf.setWebSiteAddress(map.get("WEBSITE_ADDRESS").toString());
        conf.setLOGOIMG(map.get("LOGOIMG").toString());
        conf.setBGIMG(map.get("BGIMG").toString());
        conf.setExpandedUnitCode(map.get("EXPANDED_UNIT_CODE").toString());
        conf.setBankCode(map.get("BANKCODE").toString());
        conf.setChargeAddress(map.get("CHARGEONLINEINTERFACEB2C").toString());
        conf.setLastSnoBumber(Integer.parseInt(map.get("STUDENT_SNO_NUMBER").toString()));
        conf.setLimitClassType(map.get("LIMIT_CLASS_NUMBER").toString());
        conf.setCurrentGrade(map.get("CURRENT_GRADE").toString());
        conf.setCurrentPici(Integer.parseInt(map.get("CURRENT_PICI").toString()));
        conf.setSchoolStructure(Integer.parseInt(map.get("SCHOOL_STRUCTURE").toString()));
        conf.setWebSiteName(map.get("WEB_SITE_NAME").toString());
        conf.setWebSiteClose(map.get("WEB_SITE_CLOSE").toString());
        conf.setConfigGroupList(map.get("CONFIG_GROUP_LIST").toString());

        System.err.println("first");
        redisTemplate.opsForValue().set("config", conf);
        return conf;
    }

    /**
     * 获取配置表信息
     * 缓存
     *
     * @return
     */
    public Map<String, Object> getConfigMap() {
        Map<String, Object> configMapCache = (Map<String, Object>) redisTemplate.opsForValue().get("configMap");
        if (configMapCache != null) {
            return configMapCache;
        }

        Config conf = new Config();
        Map<String, Object> map = new HashMap<>();

        List<Map<String, Object>> config = configMapper.getConfig();
        for (Map<String, Object> item : config) {
            map.put(item.get("name").toString(), item.get("value"));
        }
        System.out.println("configMap");
        redisTemplate.opsForValue().set("configMap", map);
        return map;
    }

    public Integer getSchoolStructureLength() {
        Config conf = getConfig();
        return conf.getSchoolStructure();
    }

    public Integer getCurrentPici() {
        Config conf = getConfig();
        return conf.getCurrentPici();
    }

    public String getExpandedUnitCode() {
        Config conf = getConfig();
        return conf.getExpandedUnitCode();
    }

    public String getChargeAddress() {
        Config conf = getConfig();
        return conf.getChargeAddress();
    }

    public String getBankCode() {
        Config conf = getConfig();
        return conf.getBankCode();
    }

    public String getAdmissionsProfile() {
        Config config = getConfig();
        return config.getAdmissionsProfile();
    }

    public String getWebSiteAddress() {
        Config config = getConfig();
        return config.getWebSiteAddress();
    }

    public String getLOGOIMG() {
        Config config = getConfig();
        return config.getLOGOIMG();
    }

    public String getBGIMG() {
        Config config = getConfig();
        return config.getBGIMG();
    }

    public String getWebSiteName() {
        return configMapper.getWebSiteName();
    }

    public void insertBaseFileSelective(BaseFile baseFile) {
        configMapper.insertBaseFileSelective(baseFile);
    }

    public Map<String, Object> getConfigData(Map<String, Object> params) {
        Map<String, Object> map = configMapper.getConfigData(params);
        return map;
    }

    /**
     * 更新配置信息
     * 更新缓存
     *
     * @param params
     */
    public void updateConfigData(Map<String, Object> params) {
        configMapper.updateConfigData(params);
        updateCache();
    }

    /**
     * 创建配置信息
     * 更新缓存
     *
     * @param params
     */
    public void createConfigData(Map<String, Object> params) {
        configMapper.createConfigData(params);
        updateCache();
    }

    /**
     * 删除配置信息
     * 更新缓存
     *
     * @param params
     */
    public void deleteConfigData(Map<String, Object> params) {
        configMapper.deleteConfigData(params);
        updateCache();
    }


    public List<Map<String, Object>> getQQ() {
        return configMapper.getQQ();
    }

    /**
     * 更新缓存
     */
    public void updateCache() {
        redisTemplate.delete("config");
        redisTemplate.delete("configMap");
        getConfig();
        getConfigMap();
    }
}
