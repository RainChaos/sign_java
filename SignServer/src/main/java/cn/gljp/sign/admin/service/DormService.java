package cn.gljp.sign.admin.service;


import cn.gljp.sign.utils.Md5Util;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DormService {
    @Autowired
    private ConfigService configService;
    @Autowired
    private StudentService studentService;


    /**
     * 设置入住状态
     * @param params
     */
    @Value("${dormKey}")
    private String dormKey;
    public void setDorm(Map<String, Object> params, int code) throws Exception {

        Map<String, Object> configMap = configService.getConfigMap();
        if(configMap.get("ISUSEDOR") != null && configMap.get("ISUSEDOR").toString().equals("1")){
            if(configMap.containsKey("DORAPIURL")){
                String dorApiUrl = configMap.get("DORAPIURL").toString();
                String[] studentIds = params.get("studentIds").toString().split(",");
                List<String> sno = studentService.getSnoById(studentIds);
                dormStatus(dorApiUrl, code, sno);
            }else{
                throw new Exception("错误：公寓系统接口未配置");
            }
        }else{
            throw new Exception("错误：公寓系统接口未开启");
        }
    }

    public void dormStatus(String dorApiUrl, int code, List<String> sno) throws Exception {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();
        String date = now.format(dateTimeFormatter);

        String dor_status = "";
        String dor_path = "";
        String dor_bed_number = "";

        if(code == 1){
            dorApiUrl += "Dorm/Backend.php/YX/Distribution/autoCheckIn";//入住
            dor_status = "已分配";
        }else if(code == 2){
            dorApiUrl += "Dorm/Backend.php/YX/Dorm/checkOut";//外宿、离宿
            dor_status = "未分配";
        }

        for(String id : sno){
            Map<String, Object> data = new HashMap<>();
            data.put("sid", id);
            data.put("dor_status", dor_status);

            boolean studentDormStatus = getStudentDormStatus(id);
            if(code == 2 && !studentDormStatus){
                studentService.setStudentDorm(data);
                return;
            }


            //签名
            String sign = date + id + dormKey;
            data.put("sign", Md5Util.MD5(sign));
            JSONObject jsonObject = dormRequest(dorApiUrl, data);

            if(jsonObject.get("res_code").toString().equals("0")){//请求成功
                if(code == 1){
                    JSONObject res = JSONUtil.parseObj(jsonObject.get("data").toString());
                    dor_path = res.get("room_path").toString();//宿舍
                    dor_bed_number = res.get("bed_path").toString();//带床位
                }
                data.put("dor_path", dor_path);
                data.put("dor_bed_number", dor_bed_number);

                studentService.setStudentDorm(data);
            }else{
                throw new Exception("错误：公寓系统——" + jsonObject.get("res_msg"));
            }
        }
    }

    public JSONObject dormRequest(String url, Map<String, Object> data){
        String result = HttpUtil.post(url, data);
        JSONObject res = JSONUtil.parseObj(result);
        return res;
    }

    /**
     * 查询住宿状态
     * @return
     */
    public boolean getStudentDormStatus(String sno) throws Exception {
        Map<String, Object> params = new HashMap<>();
        params.put("sid", sno);
        Map<String, Object> configMap = configService.getConfigMap();
        String dorApiUrl = configMap.get("DORAPIURL").toString();
        dorApiUrl += "Dorm/Backend.php/YX/Dorm/bedOfStudent";

        String result = HttpUtil.post(dorApiUrl, params);

        JSONObject res = JSONUtil.parseObj(result);
        if(res.get("res_code").toString().equals("0")){//请求成功
            JSONObject data = JSONUtil.parseObj(res.get("data").toString());
            params.put("dor_status", "已分配");
            params.put("dor_path", data.get("bed_path").toString());
            params.put("dor_bed_number", data.get("bed_path").toString());

            if(data.get("bed_path").toString().equals("")){
                params.put("dor_status", "未分配");
                studentService.setStudentDorm(params);
                return false;
            }else{
                studentService.setStudentDorm(params);
            }
        }else{
            throw new Exception("错误：公寓系统——" + res.get("res_msg"));
        }

        return true;
    }

}
