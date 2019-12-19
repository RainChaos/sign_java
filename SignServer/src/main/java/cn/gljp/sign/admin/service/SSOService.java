package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.mapper.sign.OperatorMapper;
import cn.gljp.sign.utils.JwtUtil;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SSOService {
    @Autowired
    private OperatorMapper operatorMapper;

    @Autowired
    private ConfigService configService;

    /**
     * 用于单点登录
     * @param params
     * @return
     * @throws Exception
     */
    public Map<String, Object> sso(Map<String, Object> params) throws Exception {
        String token = params.get("token").toString();
        String appid = params.get("appId").toString();

        String userId = login(token, appid);

        if("".equals(userId)){
            throw new Exception("错误：登录失败");
        }

//        String userId = "admin";

        HashMap<String, String> data = new HashMap<>();
        data.put("username", userId);
        Map<String, Object> operator = operatorMapper.sso(data);

        //生成token
        String signToken = new JwtUtil().tokenGenerate(operator);
        operator.put("token", signToken);

        Config config = configService.getConfig();
        operator.put("pici_id", config.getCurrentPici());

        return operator;
    }

    /**
     * 请求数据中心接口
     * @param token
     * @param appid
     * @return
     */
    public String login(String token, String appid){
        String ip = "http://gledu.guetcloud.com";
        String api = "/CasWeb/ServerValidate";
        String url = ip + api + "?token=" + token + "&appid=" + appid;

        System.out.println(url);

        CloseableHttpClient client = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String result = "";

        HttpPost httpPost = new HttpPost(url);
        RequestConfig build = RequestConfig.custom().setConnectTimeout(2000).setSocketTimeout(2000).build();

        try {
            response = client.execute(httpPost);
            int statusCode = response.getStatusLine().getStatusCode();
            if(statusCode != HttpStatus.SC_OK){
                httpPost.abort();
                throw new Exception("Fail to connect:" + statusCode);
            }
            result = EntityUtils.toString(response.getEntity());
            JSONObject operator = JSONObject.parseObject(result);
            Object userId = operator.get("userId");
            if(userId != null && !"".equals(userId.toString())){
                //认证成功
                return userId.toString();
            }else{
                return "";
            }
        } catch (Exception e) {
        }
        return "";
    }


}
