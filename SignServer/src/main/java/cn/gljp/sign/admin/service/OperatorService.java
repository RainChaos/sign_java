package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import cn.gljp.sign.admin.mapper.sign.SchoolMapper;
import cn.gljp.sign.utils.JwtUtil;
import cn.gljp.sign.utils.Md5Util;
import cn.gljp.sign.utils.Utils;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

@Service
public class OperatorService {
    @Autowired
    private SchoolStructureService schoolStructureService;

    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private SchoolMapper schoolMapper;

    @Autowired
    private ConfigService configService;

    @Value("${domain}")
    private String domain;

	public void select1(){
		operatorMapper.select1();
	}

	public void changePassword(Integer uid, String oldPassword, String newPassword)
			throws Exception {
		// 使用uid查询用户数据
		List<Map<String, Object>> user=operatorMapper.findByUid(uid);
		// 判断返回结果是否为null
		if(user ==null) {
			throw new Exception("错误：修改密码异常！用户数据不存在");
		}

		// 比较和查询到的用户密码是否不一致
		String password = new Md5Util().MD5(oldPassword);
		if(!password.equals(user.get(0).get("password"))) {

			throw new Exception("错误：修改密码异常！原始密码错误");
		}

		// 调用updatePassword()更新密码
		String md5NewPassword = new Md5Util().MD5(newPassword);
		Integer row=operatorMapper.updatePassword(uid, md5NewPassword,new Date());
		// 判断受影响的行数是否不为1
		if(!row.equals(1)) {
			// 是：UpdateException
			throw new Exception("错误：修改密码异常！请联系管理员");
		}

	}

    public Map<String, Object> loginCheck(HttpServletRequest request) throws Exception {
        Map<String, Object> map = new HashMap<>();
        String token = request.getHeader("Admin-Sign-Token");
        if(token != null){
            try{
                Map<String, Object> operator = new JwtUtil().tokenCheck(token);
                return operator;
            }catch (Exception e){
                throw new Exception("201");
            }
        }else{
            throw new Exception("202");
        }
    }

    public List<Map<String, Object>> getOperatorList(Map<String, Object> params) {
        new Utils().pageParams(params);
        return operatorMapper.getOperatorList(params);
    }

    public Integer countOperator(){
        return operatorMapper.countOperator();
    }

    public List<Map<String, Object>> getAuthGroupList(Map<String, Object> params) {
        List<Map<String, Object>> list = operatorMapper.getAuthGroupList(params);
        return list;
    }

    /**
     * 管理员需要用到整体组织架构
     * @param params
     * @return
     */
//    @Cacheable(cacheNames  = "OperatorServiceGetSchoolStructureList")
    public List<Map<String, Object>> getSchoolStructureList(Map<String, Object> params) {
        //没有专业的系部，将会删除
        boolean operator_id = params.get("operator_id") == null ? false : true;

        List<String> operatorClass = getOperatorClass(params);

        params.put("farther_code", "001");
        List<Map<String, Object>> schoolStructure = schoolMapper.getSchoolStructure2(params);//校区
        System.out.println(schoolStructure);
        for (int i = schoolStructure.size() - 1; i >= 0; i--) {
            params.put("farther_code", schoolStructure.get(i).get("unit_code").toString());
            List<Map<String, Object>> school = schoolMapper.getSchoolStructure2(params);//系
            if(school != null && school.size() > 0){
                schoolStructure.get(i).put("children", school);
                for (int j = school.size() - 1; j >= 0; j--) {
                    params.put("farther_code", school.get(j).get("unit_code").toString());
                    List<Map<String, Object>> dept = schoolMapper.getSchoolStructure2(params);//专业
                    if(dept != null && dept.size() > 0){
                        school.get(j).put("children", dept);
                        for(int k = dept.size() - 1; k >= 0; k--){
                            params.put("farther_code", dept.get(k).get("unit_code").toString());
                            List<Map<String, Object>> grade = schoolMapper.getSchoolStructure2(params);//年级
                            if(grade != null && grade.size() > 0){
                                dept.get(k).put("children", grade);
                                for(int l = grade.size() - 1; l >= 0; l--){
                                    params.put("farther_code", grade.get(l).get("unit_code").toString());
                                    List<Map<String, Object>> clazz = schoolMapper.getSchoolStructure2(params);//班级
                                    if(clazz != null && clazz.size() > 0){
                                        grade.get(l).put("children", clazz);
                                        if(operator_id)
                                            operatorClassRuleCheck(clazz, operatorClass);
                                    }
                                    if(clazz.size() == 0 && operator_id == true)
                                        grade.remove(l);
                                }
                            }
                            if(grade.size() == 0 && operator_id == true)
                                dept.remove(k);
                        }
                    }
                    if(dept.size() == 0 && operator_id == true)
                        school.remove(j);
                }
            }
            if(school.size() == 0 && operator_id == true)
                schoolStructure.remove(i);
        }
        return schoolStructure;
    }
    /*
    * 获取年级下的班级数据
    * */
    public List<Map<String, Object>> getClassList(Map<String, Object> params) {
        List<Map<String, Object>> clazz = schoolMapper.getSchoolStructure2(params);
        return clazz;
    }
    /**
     * 检查管理员班级权限
     *
     */
    public void operatorClassRuleCheck(List<Map<String, Object>> clazz, List<String> operatorClass){
        for(int i = clazz.size() - 1; i >=0; i--){
            if(!operatorClass.contains(clazz.get(i).get("unit_code").toString())){
                clazz.remove(i);
            }
        }
    }

    /**
     * 获取当前登录用户，所拥有可查看权限的班级
     * @param params
     * @return
     */
    public List<String> getOperatorClass(Map<String, Object> params){
        List<String> operatorClass = operatorMapper.getOperatorClass(params);
        return operatorClass;
    }

    /**
     * 根据id查询管理员用户
     * @param params
     * @return
     */
    public Map<String, Object> getOperatorById(Map<String, Object> params){
        return operatorMapper.getOperatorById(params);
    }

    /**
     * 获取此管理员的权限
     * @param params
     * @return
     */
    public List<Map<String, Object>> getAuthRulesTree(Map<String, Object> params) {

        List<Map<String, Object>> authRuleTree = new LinkedList<>();
        List<String> ruleIdList = new ArrayList<>();
        Map<String, Object> authGroup = new HashMap<>();

        List<Map<String, Object>> authRule = operatorMapper.getAuthRule(params);


        if(params.get("group_id") != null){
            authGroup = operatorMapper.getAuthGroupById(params);//获取权限组权限信息
            String[] rules = authGroup.get("rules").toString().split(",");
            for(String rule : rules){
                ruleIdList.add(rule);
            }
        }else{
            ruleIdList = operatorMapper.getRuleIdListById(params);//管理员权限信息
        }

        for(Map<String, Object> item : authRule){
            if(params.get("pageRule") == null){
                if(ruleIdList.contains(item.get("id").toString())){
                    item.put("checked", true);
                }else{
                    item.put("checked", false);
                }
            }
            if("0".equals(item.get("father_id").toString())){//获取父节点
                authRuleTree.add(item);
            }
        }

        for(Map<String, Object> item : authRuleTree){
            List<Map<String, Object>> children = new LinkedList<>();
            String id = item.get("id").toString();
            for(Map<String, Object> rule : authRule){

                if(rule.get("father_id").toString().equals(id)){
                    children.add(rule);
                }
            }
            if(children.size() > 0)
                item.put("children", children);
        }

        return authRuleTree;
    }

    /**
     * 赋予权限
     * @param params
     */
    @Transactional
    public void assignRules(Map<String, Object> params){
        String[] operator_classes = params.get("operator_class").toString().split(",");
        String[] rules = params.get("rules").toString().split(",");

        params.put("operator_class", operator_classes);
        params.put("rules", rules);

        operatorMapper.deleteOperatorClassById(params);
        operatorMapper.insertOperatorClass(params);
        operatorMapper.deleteOperatorRule(params);
        operatorMapper.insertOperatorRule(params);
    }


    /**
     * 删除管理用户
     * @param params
     */
    public void deleteOperator(Map<String, Object> params) {
        operatorMapper.deleteOperator(params);
    }

    /**
     * 创建管理用户
     * @param params
     */
    public void createOperator(Map<String, Object> params) {
        String password = params.get("password").toString();
        params.put("password", Md5Util.MD5(password));

        operatorMapper.createOperator(params);
    }

    /**
     * 更新角色组信息
     * @param params
     */
    public void updateAuthGroup(Map<String, Object> params) {
        operatorMapper.updateAuthGroup(params);
    }

    /**
     * 角色组权限赋予
     * @param params
     */
    public void assignAuthGroupRules(Map<String, Object> params) {
        operatorMapper.assignAuthGroupRules(params);
    }

    /**
     * 更新角色组状态
     * @param params
     */
    public void updateAuthGroupStatus(Map<String, Object> params) {
        operatorMapper.updateAuthGroupStatus(params);
    }

    /**
     * 删除角色组
     * @param params
     */
    public void deleteAuthGroup(Map<String, Object> params) {
        operatorMapper.deleteAuthGroup(params);
    }

    public void addAuthGroup(Map<String, Object> params) throws Exception {
        Integer count = operatorMapper.authGroupExist(params);
        if(count > 0){
            throw new Exception("错误：角色名称重复");
        }
        operatorMapper.addAuthGroup(params);
    }

    public List<Map<String, Object>> getAuthRuleParentList(Map<String, Object> params) {
        List<Map<String, Object>> list = operatorMapper.getAuthRuleParentList(params);
        return list;
    }

    public void updateAuthRule(Map<String, Object> params) {
        operatorMapper.updateAuthRule(params);
    }

    public void deleteAuthRule(Map<String, Object> params) throws Exception {
        Integer count = operatorMapper.authRuleHasChildren(params);
        if(count > 0)
            throw new Exception("错误：此分类下有子菜单");
        operatorMapper.deleteAuthRule(params);
    }

    public List<Map<String, Object>> getGroupLists(Map<String, Object> params) {
        List<Map<String, Object>> list = operatorMapper.getGroupListByGroup(params);
        for(Map<String, Object> item : list) {
            if(item.get("type").toString().equals("5")){

                List<Map<String, Object>> extra = operatorMapper.operatorSql(item.get("extra").toString());
                item.put("extra", extra);
            }else if(item.get("type").toString().equals("6")){
                Map<String, Object> fileConfig = operatorMapper.getFileConfig(item.get("value").toString());
                if(fileConfig != null){
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", fileConfig.get("name"));
                    data.put("url",  fileConfig.get("url"));
                    item.put("extra", "");
                    ArrayList<Object> file = new ArrayList<>();
                    file.add(data);
                    item.put("file", file);
                }else{
                    HashMap<String, Object> data = new HashMap<>();
                    data.put("name", "");
                    data.put("url",  "");
                    item.put("extra", "");
                    ArrayList<Object> file = new ArrayList<>();
                    file.add(data);
                    item.put("file", file);
                }
            }else{
                JSONArray extra = JSONArray.parseArray(item.get("extra").toString());
                if(extra == null)
                    extra = new JSONArray();
                item.put("extra", extra);
            }
        }
        return list;
    }

    public JSONArray getConfigGroup() {
        Config config = configService.getConfig();
        JSONArray data = JSONArray.parseArray(config.getConfigGroupList());
        return data;
    }
    @Transactional
    public void saveConfig(Map<String, Object> params) {
        for(Map.Entry<String, Object> item : params.entrySet()){
            Map<String, Object> param = new HashMap<>();
            param.put("name", item.getKey());
            param.put("value", item.getValue());
            operatorMapper.saveConfig(param);
        }
        configService.updateCache();
    }

    public Map<String, Object> getConfigList(Map<String, Object> params) {
        new Utils().pageParams(params);
        HashMap<String, Object> data = new HashMap<>();
        Long total = operatorMapper.getConfigListCount(params);
        List<Map<String, Object>> list = operatorMapper.getConfigList(params);
        data.put("total", total);
        data.put("data", list);

        return data;
    }

    public void createAuthRule(Map<String, Object> params) {
        operatorMapper.createAuthRule(params);
    }

    public void updateOperator(Map<String, Object> params) {
        if(params.get("password") != null && !"".equals(params.get("password").toString())){
            String password = new Md5Util().MD5(params.get("password").toString());
            params.put("password", password);
        }
        operatorMapper.updateOperator(params);
    }
    @Transactional
    public void operatorAssignGroup(Map<String, Object> params) {
        params.put("group_id", params.get("auth_group_id"));
        //更新base_auth_operator_group表的group_id
        operatorMapper.operatorAssignGroup(params);
        //获取group_id的权限
        Map<String, Object> group = operatorMapper.getAuthGroupById(params);
        String rules = group.get("rules").toString();
        String[] rulesAttr = rules.split(",");
        params.put("rules", rulesAttr);
        //删除原有权限
        operatorMapper.deleteOperatorRule(params);
        //增加权限
        operatorMapper.insertOperatorRule(params);

    }

    public JSONArray getConfigType() {
        Map<String, Object> configMap = configService.getConfigMap();
        String configTypeList = configMap.get("CONFIG_TYPE_LIST").toString();
        JSONArray data = JSONArray.parseArray(configTypeList);
        return data;
    }

    public Map<String, Object> getConfigData(Map<String, Object> params) {
        Map<String, Object> map = configService.getConfigData(params);
        return map;
    }

    public void updateConfigData(Map<String, Object> params) {
        configService.updateConfigData(params);
    }

    public void createConfigData(Map<String, Object> params) {
        configService.createConfigData(params);
    }

    public void deleteConfigData(Map<String, Object> params) {
        configService.deleteConfigData(params);
    }
}
