package cn.gljp.sign.admin.mapper.sign;

import cn.gljp.sign.admin.entity.Article;
import cn.gljp.sign.admin.entity.ArticleCategory;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AuthRuleMapper {
    Integer getAuthRuleByUrlAndOperatorId(Map<String, Object> params);

    Integer getPhoneTeacherRuleId();

    Integer getPhoneTeacherAuthRuleUrlCount(Map<String, Object> params);

    List<String> getPhoneTeacherRuleUrl();

    List<Map<String, Object>> getAuthRuleInterfaceById(Map<String, Object> params);

    void addAuthRuleInterface(Map<String, Object> params);

    void deleteAuthRuleInterface(Map<String, Object> params);

    void updateAuthRuleInterface(Map<String, Object> params);
}
