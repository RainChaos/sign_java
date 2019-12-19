package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.MiddleSchool;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface MiddleSchoolMapper {
    Long listSchoolByRegionCount(String regionCode);
    List<MiddleSchool> listSchoolByRegion(@Param("pageIndex")int index,@Param("pageSize") int limit,@Param("regionCode") String regionCode);
    void insert(MiddleSchool middleSchool);
    void update(MiddleSchool middleSchool);
    void delete(MiddleSchool middleSchool);
    void setOrderSortAll(String regionCode);

    List<MiddleSchool> listSchoolByRegionNoPage(@Param("regionCode") String regionCode);
}
