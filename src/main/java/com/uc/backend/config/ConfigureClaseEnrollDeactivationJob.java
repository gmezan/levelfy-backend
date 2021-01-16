package com.uc.backend.config;

import com.uc.backend.job.ClaseEnrollDeactivationJob;
//import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureClaseEnrollDeactivationJob {
/*
    @Bean
    public JobDetail jobADetails() {
        return JobBuilder.newJob(ClaseEnrollDeactivationJob.class).withIdentity("daily-deactivation-clase")
                .storeDurably().build();
    }


    @Bean
    public Trigger jobATrigger(JobDetail jobADetails) {

        return TriggerBuilder.newTrigger().forJob(jobADetails)
                .withIdentity("TriggerA")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 0/12 1/1 * ? *")) // Cada 12h desde las 00:00 (UTC)
                .build();
    }
*/
}
