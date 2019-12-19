package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.mapper.charge.AdminMapper;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import cn.gljp.sign.admin.mapper.sign.StudentMapper;
import cn.gljp.sign.utils.JwtUtil;
import cn.gljp.sign.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private ConfigService configService;

    @Autowired
    private BatchService batchService;

    public Map<String, Object> doLogin(@RequestBody Map<String, Object> params) {
        Map<String, Object> operator = new HashMap<>();

        if (params.get("username").equals("admin") && params.get("password").equals("guilin@123")) {
            //超管
            operator = operatorMapper.superLogin();
            operator.put("supper", 1);

        } else {
            //普通登录
            String password = new Md5Util().MD5(params.get("password").toString());
            params.put("password", password);

            operator = operatorMapper.doLogin(params);
            operator.put("supper", 0);
        }


        //生成token
        String token = new JwtUtil().tokenGenerate(operator);
        operator.put("token", token);

        Config config = configService.getConfig();
        operator.put("pici_id", config.getCurrentPici());

        return operator;
    }


    public Map<String, Object> getInfo(Map<String, Object> params) throws Exception {
        String token = params.get("token").toString();
        Map<String, Object> operator = new JwtUtil().tokenCheck(token);

        Map<String, Object> info = new HashMap<>();

        List<String> rules = getRules(operator);
        info.put("rules", rules);

//        Config config = configService.getConfig();
        Map<String, Object> configMap = configService.getConfigMap();
        info.put("pici_id", configMap.get("CURRENT_PICI"));
        info.put("classLength", configMap.get("SCHOOL_STRUCTURE"));
        info.put("webSiteName", configMap.get("WEB_SITE_NAME"));
        info.put("logoUrl", configMap.get("LOGOIMG"));
        info.put("username", operator.get("username"));

        return info;
    }

    public List<String> getRules(Map<String, Object> operator) {
        List<String> list = new LinkedList<>();
        //权限菜单
        List<Map<String, Object>> rules = operatorMapper.getRules(operator);
        for (Map<String, Object> rule : rules) {
            String url = rule.get("url").toString();
            list.add(url);
        }
        return list;
    }

    public void updatePici(Map<String, Object> params) {
        batchService.updateBatch(params);
    }

    public void createPici(Map<String, Object> params) {
        batchService.createBatch(params);
    }


}
