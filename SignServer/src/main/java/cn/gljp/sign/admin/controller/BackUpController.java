package cn.gljp.sign.admin.controller;


import cn.gljp.sign.admin.entity.ChargeItem;
import cn.gljp.sign.admin.service.BackUpService;
import cn.gljp.sign.utils.FileUtil;
import cn.gljp.sign.utils.PageResult;
import cn.gljp.sign.utils.Result;
import cn.gljp.sign.utils.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/admin")
public class BackUpController {
    @Value("${signDataSourceBackUp}")
    private String signDataSourceBackUp;
    @Autowired
    private BackUpService backUpService;
    @Autowired
    private HttpServletResponse response;

    @RequestMapping("/sign/backUp")
    public Result signBackUp() throws IOException {
        backUpService.signBackUp(signDataSourceBackUp);
        return new Result(StatusCode.OK, true,"", "");
    }

    @RequestMapping("/backUp/get")
    public Result getBackUp(@RequestParam Map<String, Object> params) throws IOException {
        List<Map<String, Object>> data = backUpService.getBackUp(params);
        long count = backUpService.getBackUpCount(params);
        PageResult<Map<String, Object>> result = new PageResult<>(count, data);
        return new Result(StatusCode.OK, true,"", result);
    }

    @Value("${signDataSourceBackUpUrl}")
    private String signDataSourceBackUpUrl;
    @RequestMapping("/backup")
    public void downloadBackup(@RequestBody Map<String, Object> params) throws IOException {
        File file = new File(signDataSourceBackUpUrl + params.get("name").toString());
        FileUtil.responseTo(file, response);
    }
}
