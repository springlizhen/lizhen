package com.bjpowernode.p2p.timer;

import com.bjpowernode.p2p.service.loan.IncomeRecordService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * ClassName：TimerManager
 * Package:com.bjpowernode.p2p.timer
 * Description:
 *
 * @date:2019/3/1 11:40
 * @author:guoxin@bjpowernode.com
 */
@Component
public class TimerManager {
    Logger logger = LogManager.getLogger(TimerManager.class);

    @Autowired
    private IncomeRecordService incomeRecordService;
    //@Scheduled(cron = "0/5 * * * * ?")
    public void generateIncomeplan(){
        logger.info("----------收益开始---------");
        incomeRecordService.generateIncomeplan();
        logger.info("-------收益结束----------");


    }
    @Scheduled(cron = "0/5 * * * * ?")//每隔五秒执行一次
    public void generateIncomeBack(){
        logger.info("-----收益返还开始-----");
        incomeRecordService.generateIncomeBack();
        logger.info("-----收益返还结束----------");

    }

}
