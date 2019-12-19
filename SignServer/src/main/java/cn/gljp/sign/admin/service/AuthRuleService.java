package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.mapper.sign.AuthRuleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AuthRuleService {
    @Autowired
    private AuthRuleMapper authRuleMapper;

    /**
     * 接口权限判断
     * @return
     */
    public boolean authRuleCheck(Map<String, Object> params){
        Integer count = authRuleMapper.getAuthRuleByUrlAndOperatorId(params);
        return count > 0 ? true : false;
    }

    public Integer getPhoneTeacherRuleId(){
        return authRuleMapper.getPhoneTeacherRuleId();
    }

    public boolean getPhoneTeacherAuthRuleUrlCount(Map<String, Object> params){
        Integer count = authRuleMapper.getPhoneTeacherAuthRuleUrlCount(params);
        return count > 0 ? true : false;
    }

    public List<String> getPhoneTeacherRuleUrl(){
        return  authRuleMapper.getPhoneTeacherRuleUrl();
    }

    public List<Map<String, Object>> getAuthRuleInterface(Map<String, Object> params) {
        return authRuleMapper.getAuthRuleInterfaceById(params);
    }

    public void addAuthRuleInterface(Map<String, Object> params) {
        authRuleMapper.addAuthRuleInterface(params);
    }

    public void deleteAuthRuleInterface(Map<String, Object> params) {
        authRuleMapper.deleteAuthRuleInterface(params);
    }

    public void updateAuthRuleInterface(Map<String, Object> params) {
        authRuleMapper.updateAuthRuleInterface(params);

    }
}
