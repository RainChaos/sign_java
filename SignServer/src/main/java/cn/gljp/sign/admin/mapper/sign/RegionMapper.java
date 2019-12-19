package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.Region;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RegionMapper {
    List<Region> listByFatherCode(int fatherCode);
    List<Region> listRegionByTypeAndFatherCode(@Param("type")int type,@Param("fatherCode") int fatherCode);
    void update(Region r);
    void insert(Region region);
    void delete(Region region);

    List<Map<String, Object>> getProvince();

    List<Map<String, Object>> getCity(Map<String, Object> map);

    List<Map<String, Object>> getArea(Map<String, Object> map);

    String getCodeByAllName(String province, String city, String area);
}
