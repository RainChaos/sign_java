package cn.gljp.sign.admin.controller;

import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class AuthRuleController {

    @RequestMapping("/authRule/error")
    public Result authRuleError() {
        return new Result(StatusCode.ERROR, false, "没有权限", "");
    }

    @RequestMapping("/login/error")
    public Result loginError() {
        return new Result(50014, false, "", "");
    }
}
