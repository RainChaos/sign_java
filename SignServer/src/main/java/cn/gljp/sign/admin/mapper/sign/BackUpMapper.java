package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.ArticleCategory;
import org.apache.ibatis.annotations.Param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface BackUpMapper {


    void add(HashMap<String, Object> backUp);

    List<Map<String, Object>> getBackUp(Map<String, Object> params);

    Long getBackUpCount(Map<String, Object> params);

    Integer select1();
}
