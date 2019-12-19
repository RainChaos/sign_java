package cn.gljp.sign.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.context.annotation.Bean;

import java.util.Date;
import java.util.Map;

public class JwtUtil {
    final long time = 1000*60*60*8;

    /**
     * 登录生成token
     * @param operator
     * @return
     */
    public String tokenGenerate(Map<String, Object> operator){
        long now = System.currentTimeMillis();
        long exp = now + time;

        JwtBuilder builder = Jwts.builder().claim("operator", operator)  //自定义申明
                .setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "cngljp")  //头部信息: 指定编码 和 盐
                .setExpiration(new Date(exp));

        return builder.compact();
    }

    /**
     * token解析,返回operator
     * @param token
     * @throws Exception
     */
    public static Map<String, Object> tokenCheck(String token) throws Exception {
        try {
            Claims claims = Jwts.parser().setSigningKey("cngljp").parseClaimsJws(token).getBody();
            Map<String, Object> operator = (Map<String, Object>)claims.get("operator");
            return operator;
        }catch (Exception e){
            throw new Exception("错误：50008");
        }

    }

    public static void main(String[] args) throws Exception {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJvcGVyYXRvciI6eyJwaG9uZSI6IjE1MDc3MzA0OTY4IiwiZGVmYXVsdF91cmwiOiIvbWFpbiIsInJ1bGVzIjpbeyJ1cmwiOiIvbWFpbiJ9LHsidXJsIjoiL3N5c3RlbSJ9LHsidXJsIjoiL3N5c3RlbS9vcGVyYXRvciJ9LHsidXJsIjoiL3N5c3RlbS9hdXRoR3JvdXAifSx7InVybCI6Ii9zeXN0ZW0vYXV0aFJ1bGUifSx7InVybCI6Ii9zeXN0ZW0vY29uZmlnIn0seyJ1cmwiOiIvc3lzdGVtL3NldGNvbmZpZyJ9LHsidXJsIjoiL3N5c3RlbS9iYWNrdXBMaXN0In0seyJ1cmwiOiIvc3lzdGVtL3Jlc3RvcmVMaXN0In0seyJ1cmwiOiIvYXJ0aWNsZSJ9LHsidXJsIjoiL2FydGljbGUvYXJ0aWNsZUNhdGVnb3J5In0seyJ1cmwiOiIvYXJ0aWNsZS9hcnRpY2xlTGlzdCJ9LHsidXJsIjoiL2JhdGNoIn0seyJ1cmwiOiIvYmF0Y2gvYmF0Y2gifSx7InVybCI6Ii9iYXRjaC9zZWxlY3RVbml0In0seyJ1cmwiOiIvYmF0Y2gvc2VsZWN0VW5pdENsYXNzIn0seyJ1cmwiOiIvc3R1ZGVudCJ9LHsidXJsIjoiL3N0dWRlbnQvYWRkU3R1ZGVudCJ9LHsidXJsIjoiL3N0dWRlbnQvaW1wb3J0In0seyJ1cmwiOiIvc3R1ZGVudC9zdHVkZW50TGlzdHMifSx7InVybCI6Ii9zdHVkZW50L3lpbmd4aW4ifSx7InVybCI6Ii9zdHVkZW50L2Rvckxpc3RzIn0seyJ1cmwiOiIvc3R1ZGVudC9sZENoZWNrTGlzdHMifSx7InVybCI6IuWFgeiuuOS_ruaUueaLm-eUn-iAgeW4iCJ9LHsidXJsIjoi6K6-572u5YWl5L2PIn0seyJ1cmwiOiLorr7nva7nprvlrr8ifSx7InVybCI6Iuiuvue9ruWkluWuvyJ9LHsidXJsIjoiL3NldHRpbmcifSx7InVybCI6Ii9zZXR0aW5nL3VuaXQifSx7InVybCI6Ii9zZXR0aW5nL3NldFN0dWRlbnRHcmlkIn0seyJ1cmwiOiIvc2V0dGluZy9zZXRTdHVkZW50Rm9ybSJ9LHsidXJsIjoiL3NldHRpbmcvc3R1ZGVudFNlYXJjaEZpZWxkIn0seyJ1cmwiOiIvc2V0dGluZy9zdHVGaWVsZCJ9LHsidXJsIjoiL3NldHRpbmcvc2hvd0NoYXJnZUl0ZW0ifSx7InVybCI6Ii9zZXR0aW5nL2dyaWRTZWFyY2hGaWVsZCJ9LHsidXJsIjoiL2ZlbmJhbiJ9LHsidXJsIjoiL2ZlbmJhbi9hdXRvRmVuYmFuIn0seyJ1cmwiOiIvZmVuYmFuL2ZlbmJhbkluZGV4In0seyJ1cmwiOiIvZmVuYmFuL3h1ZWppIn0seyJ1cmwiOiIvdHpzIn0seyJ1cmwiOiIvdHpzL3R6c0ZpZWxkIn0seyJ1cmwiOiIvdHpzL3R6c21iIn0seyJ1cmwiOiIvdHpzL3ByaW50VHpzIn0seyJ1cmwiOiIvdG9uZ2ppIn0seyJ1cmwiOiIvc3R1ZGVudENvdW50L3VuaXRDb3VudCJ9LHsidXJsIjoiL3N0dWRlbnRDb3VudC9kaXN0Q291bnQifSx7InVybCI6Ii9zdHVkZW50Q291bnQvenNyQ291bnQifSx7InVybCI6Ii9iYXNlZGF0YSJ9LHsidXJsIjoiL2Jhc2VkYXRhL2RpY3Rpb25hcnlUeXBlIn0seyJ1cmwiOiIvYmFzZWRhdGEvZGljdGlvbmFyeSJ9LHsidXJsIjoiL2Jhc2VkYXRhL3JlZ2lvbiJ9LHsidXJsIjoiL2Jhc2VkYXRhL21pZGRsZVNjaG9vbCJ9LHsidXJsIjoi57O757uf5pel5b-XIn0seyJ1cmwiOiIvbG9ncy9hY3Rpb25sb2cifSx7InVybCI6Ii9sb2dzL3N0dWRlbnRMb2cifV0sImlkIjoxLCJzdXBwZXIiOjEsInVzZXJuYW1lIjoiYWRtaW4iLCJyZWFsbmFtZSI6Iui2hee6p-euoeeQhuWRmCIsInN0YXR1cyI6MSwiaXNfb25lIjpmYWxzZX0sImlhdCI6MTU2NTI1MTI0MiwiZXhwIjoxNTY1MjgwMDQyfQ.0mS46OQcg4_ZuVntUuQ_3zMq_-s0dbhcBxJkV6Pu7DY";
//        Claims claims = new JwtUtil().tokenCheck(token);
//        Map<String, Object> operator = (Map<String, Object>)claims.get("operator");
//        System.out.println(operator);
    }
}
