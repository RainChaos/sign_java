package cn.gljp.sign.init;

import cn.gljp.sign.admin.entity.ChargeItem;
import cn.gljp.sign.admin.entity.Config;
import cn.gljp.sign.admin.service.ChargeItemService;
import cn.gljp.sign.admin.service.ConfigService;
import cn.gljp.sign.admin.service.LogService;
//import cn.gljp.sign.admin.service.UpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Init implements ApplicationRunner {
    @Autowired
    private ConfigService configService;
    @Autowired
    private LogService logService;
//    @Autowired
//    private UpdateService updateService;
    @Autowired
    private ChargeItemService chargeItemService;
    @Override
    public void run(ApplicationArguments args) throws Exception {
//        updateService.initDb();
//        updateService.initTable();
        //同步收费项目
       List<ChargeItem> chargeItemList = chargeItemService.getChargeItemList();
        if (chargeItemList.size()==0){
            chargeItemService.syncChargeItem();
        }
        Config config = configService.getConfig();
        logService.addStudentLogExecute();
        configService.getConfigMap();
        System.out.println(config);

    }
}
