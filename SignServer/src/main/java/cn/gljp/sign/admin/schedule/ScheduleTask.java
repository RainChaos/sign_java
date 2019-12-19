package cn.gljp.sign.admin.schedule;

import cn.gljp.sign.admin.service.BackUpService;
import cn.gljp.sign.admin.service.LogService;
import cn.gljp.sign.admin.service.OperatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Configuration
@EnableScheduling
public class ScheduleTask {
    @Value("${signDataSourceBackUp}")
    private String signDataSourceBackUp;
    @Autowired
    private BackUpService backUpService;
    @Autowired
    private LogService logService;

    /**
     * 零点备份
     * @throws IOException
     */
    @Scheduled(cron = "0 0 0 * * ? ")
    private void mysqlBackup() throws IOException {
        System.out.println("执行数据库备份");
        backUpService.signBackUp(signDataSourceBackUp);
    }

    @Scheduled(cron = "0 0 0/5 * * ? ")
    private void select1(){
        logService.select1();
        backUpService.select1();
    }
}
