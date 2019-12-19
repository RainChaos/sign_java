package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Region;
import cn.gljp.sign.admin.mapper.sign.RegionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class RegionService {
    @Autowired
    private RegionMapper regionMapper;

    public List<Region> listByFatherCode(int fatherCode) {
        return regionMapper.listByFatherCode(fatherCode);
    }

    public void setOrderSortAll(int type, int fatherCode) {
        List<Region> regionList= regionMapper.listRegionByTypeAndFatherCode(type,fatherCode);
        int i=1;
        for (Region r: regionList) {
            r.setSort(i);
            regionMapper.update(r);
            i++;
        }
    }

    public void insert(Region region) {
        regionMapper.insert(region);
    }

    public void update(Region region) {
        regionMapper.update(region);
    }

    public void delete(Region region) {
        regionMapper.delete(region);
    }

    public List<Map<String, Object>> getRegion() {
        List<Map<String, Object>> regionList = new ArrayList<>();

        //获取省
        List<Map<String, Object>> provinces = regionMapper.getProvince();
        for (Map<String, Object> province : provinces) {

            //获取市
            List<Map<String, Object>> cities = regionMapper.getCity(province);
            if(cities.size() > 0){
                for (Map<String, Object> city : cities) {
                    //获取区
                    List<Map<String, Object>> areas = regionMapper.getArea(city);
                    if(areas.size() > 0){
                        city.put("children", areas);
                    }
                }
                province.put("children", cities);
            }
            regionList.add(province);

        }

        return regionList;
    }

    public String getCodeByAllName(String province, String city, String area){
        String code = regionMapper.getCodeByAllName(province, city, area);
        return code;
    }
}
