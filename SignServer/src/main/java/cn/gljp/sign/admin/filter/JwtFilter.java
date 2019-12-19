package cn.gljp.sign.admin.filter;

import cn.gljp.sign.admin.service.AuthRuleService;
import cn.gljp.sign.admin.service.LogService;
import cn.gljp.sign.utils.JwtUtil;
import cn.gljp.sign.utils.Utils;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class JwtFilter extends HandlerInterceptorAdapter {

    @Autowired
    private AuthRuleService authRuleService;
    @Autowired
    private LogService logService;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!request.getMethod().equals("OPTIONS")){
            try {
                String token = request.getHeader("Admin-Sign-Token");
                String currentUrl = request.getHeader("Current-Url");
                String requestURI = request.getRequestURI().replace("/SignServer", "");
                Map<String, Object> operator = new JwtUtil().tokenCheck(token); //token解析,返回operator
                String operatorId = operator.get("id").toString();
                HashMap<String, Object> params = new HashMap<>();
                request.setAttribute("username", operator.get("username").toString());
                params.put("url", currentUrl);
                params.put("authRuleUrl", requestURI);
                params.put("OperatorId", Integer.parseInt(operatorId));
                if(currentUrl.equals("teacher")){
                    List<String> phoneTeacherRuleUrl = authRuleService.getPhoneTeacherRuleUrl();
                    if(phoneTeacherRuleUrl.contains(requestURI)){
                        return true;
                    }
                }
                if (requestURI.equals("/admin/operator/updatePassword")){
                    return true;
                }
                boolean hasRule = authRuleService.authRuleCheck(params);
                if(!hasRule){
                    request.getRequestDispatcher("/authRule/error").forward(request, response);
                    return false;
                }
            }catch (Exception e){
                e.printStackTrace();
                request.getRequestDispatcher("/login/error").forward(request, response);
                return false;
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(!request.getMethod().equals("OPTIONS")){
            String clientIp = new Utils().getClientIp(request);
            String requestURI = request.getRequestURI().replace("/SignServer", "");

            HashMap<String, Object> params = new HashMap<>();
            params.put("username", request.getAttribute("username"));
            params.put("ip", clientIp);
            params.put("url", requestURI);
            params.put("describe", request.getAttribute("params"));

            logService.addActionLog(params);
        }
    }
}
