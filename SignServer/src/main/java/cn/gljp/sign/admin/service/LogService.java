package cn.gljp.sign.admin.service;

import cn.gljp.sign.admin.mapper.log.LogMapper;
import cn.gljp.sign.utils.Utils;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
public class LogService {
    private static LinkedList<Map<String, Object>> studentLogList = new LinkedList<>();

    private static ScheduledExecutorService pool = Executors.newScheduledThreadPool(1);

    LogService() {
        addStudentLogExecute();
    }

    @Autowired
    private LogMapper logMapper;

    public void select1() {
        logMapper.select1();
    }

    public List<Map<String, Object>> getActionLog(Map<String, Object> params) {
        new Utils().pageParams(params);
        List<Map<String, Object>> list = logMapper.getActionLog(params);
        return list;
    }

    public Long getActionLogCount(Map<String, Object> params) {
        return logMapper.getActionLogCount(params);
    }

    public void addActionLog(Map<String, Object> params) {
        logMapper.addActionLog(params);
    }

    public List<Map<String, Object>> getStudentLog(Map<String, Object> params) {
        new Utils().pageParams(params);
        List<Map<String, Object>> list = logMapper.getStudentLog(params);
        return list;
    }

    public Long getStudentLogCount(Map<String, Object> params) {
        return logMapper.getStudentLogCount(params);
    }

    public void addStudentLog(HttpServletRequest request) {
        try {
            String clientIp = new Utils().getClientIp(request);
            String requestURI = request.getRequestURI().replace("/SignServer", "");
            Object old_data = request.getAttribute("old_data");
            Object new_data = request.getAttribute("new_data");

            Map<String, Object> params = new HashMap<>();
            params.put("ip", clientIp);
            params.put("url", requestURI);
            params.put("username", request.getAttribute("username"));
            params.put("old_data", request.getAttribute("old_data"));
            params.put("new_data", request.getAttribute("new_data"));
            params.put("describe", request.getAttribute("params"));
            String student = request.getAttribute("new_data").toString();
            JSONObject jsonObject = JSONUtil.parseObj(student);
            Object id = jsonObject.get("id");
            Object sno = jsonObject.get("sno");
            Object name = jsonObject.get("name");
            Object idcode = jsonObject.get("idcode");

            params.put("student_id", id.toString());
            params.put("student_sno", sno.toString());
            params.put("student_name", name.toString());
            params.put("student_idcode", idcode.toString());

            studentLogList.add(params);
        } catch (Exception e) {

        }
    }

    public void addStudentLogExecute() {
        pool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                if (studentLogList.size() > 0) {
                    System.out.println("执行任务");
                    Map<String, Object> params = studentLogList.poll();
                    logMapper.addStudentLog(params);
                } else {
                }
            }
        }, 0, 50, TimeUnit.MILLISECONDS);
    }

}
