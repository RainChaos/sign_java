package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.MiddleSchool;
import cn.gljp.sign.admin.entity.Region;
import cn.gljp.sign.admin.mapper.sign.MiddleSchoolMapper;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MiddleSchoolService {
    @Autowired
    private MiddleSchoolMapper middleSchoolMapper;

    public Result listSchoolByRegion(int index, int limit, String regionCode) {
        Long total=middleSchoolMapper.listSchoolByRegionCount(regionCode);
        List<MiddleSchool> list = middleSchoolMapper.listSchoolByRegion(index,limit,regionCode);
        PageResult<MiddleSchool> pageList=new PageResult<MiddleSchool>(total,list);
        return  new Result(StatusCode.OK,true, "", pageList);
    }

    public void insert(MiddleSchool middleSchool) {
        middleSchoolMapper.insert(middleSchool);
    }

    public void update(MiddleSchool middleSchool) {
        middleSchoolMapper.update(middleSchool);
    }

    public void delete(MiddleSchool middleSchool) {
        middleSchoolMapper.delete(middleSchool);
    }

    public void setOrderSortAll(String regionCode) {
        middleSchoolMapper.setOrderSortAll(regionCode);
    }

    public Result listSchoolByRegionNoPage(String regionCode) {
        List<MiddleSchool> list = middleSchoolMapper.listSchoolByRegionNoPage(regionCode);
        return  new Result(StatusCode.OK,true, "", list);
    }
}
