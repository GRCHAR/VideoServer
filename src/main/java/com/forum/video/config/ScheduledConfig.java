package com.forum.video.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.util.concurrent.*;

/**
 * @author genghaoran
 */
@Configuration
public class ScheduledConfig implements SchedulingConfigurer {



    private final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(2, r -> new Thread(r, "schedule"));


    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.setScheduler(executor);
    }



}
