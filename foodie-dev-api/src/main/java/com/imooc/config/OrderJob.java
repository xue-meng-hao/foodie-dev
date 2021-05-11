package com.imooc.config;

import com.imooc.utils.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OrderJob {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderJob.class);

    @Scheduled(cron = "0/3 * * * * ?")
    public void autoCloseOrder() {
        LOGGER.info("执行定时任务,当前时间为：" + DateUtil.getCurrentDateString(DateUtil.DATETIME_PATTERN));
    }
}
