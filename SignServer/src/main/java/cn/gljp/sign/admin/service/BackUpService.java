package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.mapper.sign.AuthRuleMapper;
import cn.gljp.sign.admin.mapper.sign.BackUpMapper;
import cn.gljp.sign.utils.Utils;
import cn.hutool.crypto.SecureUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BackUpService {
    @Autowired
    private BackUpMapper backUpMapper;

    public void select1(){
        backUpMapper.select1();
    }

    public void signBackUp(String signDataSourceBackUp) throws IOException {
        long time = new Date().getTime();

        String name = time   + ".sql";
        name = SecureUtil.sha1(name) + ".sql";
        String cmd = signDataSourceBackUp + name;

        Process exec = Runtime.getRuntime().exec(cmd);

        HashMap<String, Object> backUp = new HashMap<>();
        backUp.put("name", name);
        backUpMapper.add(backUp);
    }

    public List<Map<String, Object>> getBackUp(Map<String, Object> params) {
        new Utils().pageParams(params);
        List<Map<String, Object>> list = backUpMapper.getBackUp(params);
        return list;
    }

    public long getBackUpCount(Map<String, Object> params) {
        return backUpMapper.getBackUpCount(params);
    }
}
