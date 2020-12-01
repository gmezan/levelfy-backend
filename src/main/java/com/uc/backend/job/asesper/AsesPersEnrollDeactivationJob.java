package com.uc.backend.job.asesper;

import com.uc.backend.entity.ClaseEnroll;
import com.uc.backend.repository.ClaseEnrollRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

@Component
public class AsesPersEnrollDeactivationJob  extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(AsesPersEnrollDeactivationJob.class);

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    //Desactivación pasada la fecha de la asesoría
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        ClaseEnroll claseEnroll = claseEnrollRepository.findById(jobDataMap.getInt("idClaseEnroll")).orElse(null);

        if (claseEnroll!=null){
            claseEnroll.setActive(false);
            claseEnrollRepository.save(claseEnroll);
        }

    }
}
