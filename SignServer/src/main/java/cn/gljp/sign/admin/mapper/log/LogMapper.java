package cn.gljp.sign.admin.mapper.log;

import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.ArticleCategory;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface LogMapper {


    List<Map<String, Object>> getActionLog(Map<String, Object> params);

    Long getActionLogCount(Map<String, Object> params);

    void addActionLog(Map<String, Object> params);

    List<Map<String, Object>> getStudentLog(Map<String, Object> params);

    Long getStudentLogCount(Map<String, Object> params);

    void addStudentLog(Map<String, Object> params);

    Integer select1();
}
