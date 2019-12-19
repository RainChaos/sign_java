package cn.gljp.sign.admin.mapper.sign;

import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface OperatorMapper {
    Map<String, Object> doLogin(Map<String, Object> params);

    //找到登录用户
    List<Map<String, Object>> findByUid(Integer uid);
	//修改密码
	Integer updatePassword(
			@Param("id") Integer uid,
			@Param("password") String password,
			@Param("updateTime") Date modifiedTime);

	Map<String, Object> sso(HashMap<String, String> params);

    Map<String, Object> superLogin();

    List<Map<String, Object>> getRules(Map<String, Object> operator);

    List<Map<String, Object>> getOperatorList(Map<String, Object> params);

    Integer countOperator();

    List<Map<String, Object>> getAuthGroupList(Map<String, Object> params);

    List<String> getOperatorClass(Map<String, Object> params);

    Map<String, Object> getOperatorById(Map<String, Object> params);

    List<String> getRuleIdListById(Map<String, Object> params);

    List<Map<String, Object>> getAuthRule(Map<String, Object> params);

    void deleteOperatorClassById(Map<String, Object> params);

    void insertOperatorClass(Map<String, Object> params);

    void deleteOperatorRule(Map<String, Object> params);

    void insertOperatorRule(Map<String, Object> params);

    void deleteOperator(Map<String, Object> params);

    void createOperator(Map<String, Object> params);

    void updateAuthGroup(Map<String, Object> params);

    Map<String, Object> getAuthGroupById(Map<String, Object> params);

    void assignAuthGroupRules(Map<String, Object> params);

    void updateAuthGroupStatus(Map<String, Object> params);

    void deleteAuthGroup(Map<String, Object> params);

    void addAuthGroup(Map<String, Object> params);

    Integer authGroupExist(Map<String, Object> params);

    List<Map<String, Object>> getAuthRuleParentList(Map<String, Object> params);

    void updateAuthRule(Map<String, Object> params);

    Integer authRuleHasChildren(Map<String, Object> params);

    void deleteAuthRule(Map<String, Object> params);

    List<Map<String, Object>> getGroupListByGroup(Map<String, Object> params);

    List<Map<String, Object>> operatorSql(@Param(value="sql") String sql);

    void excuteSql(@Param(value="sql") String sql);

    void saveConfig(Map<String, Object> params);

    Map<String, Object> getFileConfig(String url);

    List<Map<String, Object>> getConfigList(Map<String, Object> params);

    Long getConfigListCount(Map<String, Object> params);

    void createAuthRule(Map<String, Object> params);

    void updateOperator(Map<String, Object> params);

    void operatorAssignGroup(Map<String, Object> params);

    void insertOperatorClassV2(Map<String, Object> operator);

    Integer select1();

    Integer getOperatorByUsername(Map<String, Object> params);
}
