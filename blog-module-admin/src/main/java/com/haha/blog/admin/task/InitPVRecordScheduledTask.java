package com.haha.blog.admin.task;

import com.haha.blog.common.domain.dos.StatisticsArticlePvDO;
import com.haha.blog.common.mapper.StatisticsArticlePvMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitPVRecordScheduledTask {

    private final StatisticsArticlePvMapper articlePvMapper;

    @Scheduled(cron = "0 0 23 * * ?") // 每天晚间 23 点执行
    public void execute() {
        // 定时任务执行的业务逻辑
        log.info("==> 开始执行创建明日 PV 访问量记录表定时任务");
        try{
            LocalDate currDate = LocalDate.now();
            LocalDate tomorrowDate = currDate.plusDays(1);
            StatisticsArticlePvDO articlePvDO = new StatisticsArticlePvDO()
                    .setPvDate(tomorrowDate)
                    .setPvCount(0L);
            articlePvMapper.insert(articlePvDO);
        }catch (Exception e){
            log.error("创建明日 PV 访问量记录表定时任务异常",e);
        }
        log.info("==> 结束执行创建明日 PV 访问量记录表定时任务");
    }

}
