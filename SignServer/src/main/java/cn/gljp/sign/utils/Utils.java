package cn.gljp.sign.utils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

    public void pageParams(Map<String, Object> params){
        try {
            int page = Integer.parseInt(params.get("page").toString());
            int limit = Integer.parseInt(params.get("limit").toString());
            page = (page - 1) * limit;
            params.put("page", page);
            params.put("limit", limit);
        }catch (Exception e){
            params.put("page", 0);
            params.put("limit", 15);
        }
    }

    /**
     *
     * @param map1 原数据
     * @param map2 需要比较的数据
     * @return
     */
    public static  boolean mapCompar(Map<String,Object> map1,Map<String, Object> map2) {
        boolean isChange = false;
        for (Map.Entry<String, Object> entry1 : map1.entrySet()) {
            String m1value =  entry1.getValue() == null ? "" : entry1.getValue().toString();
            String m2value = map2.get(entry1.getKey()) == null ? "" : map2.get(entry1.getKey()).toString();
            if (!m1value.equals(m2value)) {
                isChange = true;
            }
        }
        return isChange;
    }

    public String getClientIp(HttpServletRequest request)
    {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.trim() == "" || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        // 多个路由时，取第一个非unknown的ip
        final String[] arr = ip.split(",");
        for (final String str : arr) {
            if (!"unknown".equalsIgnoreCase(str)) {
                ip = str;
                break;
            }
        }
        return ip;
    }
}
