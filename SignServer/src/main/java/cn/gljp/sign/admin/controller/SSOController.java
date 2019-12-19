package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.service.AdminService;
import cn.gljp.sign.admin.service.SSOService;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
@ResponseBody
public class SSOController {
    @Autowired
    private AdminService adminService;

    @Autowired
    private SSOService ssoService;

    /**
     * 登录
     * @param params
     * @return
     */
    @RequestMapping(value = "/sso", method = RequestMethod.POST)
    public Result doLogin(@RequestBody Map<String, Object> params) throws Exception {
        Map<String, Object> operator = new HashMap<>();
        try {
            operator = ssoService.sso(params);
        }catch (Exception e){
            throw new Exception("错误：请通过账号密码登录");
        }

        return new Result(StatusCode.OK, true, "登录成功", operator);
    }
}
