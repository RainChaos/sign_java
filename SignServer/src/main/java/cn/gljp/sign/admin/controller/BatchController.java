package cn.gljp.sign.admin.controller;

import cn.gljp.sign.admin.service.BatchService;
import cn.gljp.sign.utils.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/batch")
public class BatchController {
    @Autowired
    private BatchService batchService;

    /**
     * 获取批次
     * @return
     */
//    @RequestMapping("/get")
//    public Result getBatch(){
//        List<Map<String, Object>> batch = batchService.getBatch();
//        return new Result().success(true, batch);
//    }
}
