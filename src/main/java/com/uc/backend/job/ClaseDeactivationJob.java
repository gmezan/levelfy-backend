package com.uc.backend.job;

import com.uc.backend.entity.Clase;
import com.uc.backend.repository.ClaseRepository;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;


@Component
public class ClaseDeactivationJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ClaseDeactivationJob.class);

    @Autowired
    ClaseRepository claseRepository;

    //Desactivación
    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        Clase clase = claseRepository.findById(jobDataMap.getInt("idclase")).orElse(null);

        if (clase!=null){
            clase.setDisponible(Boolean.FALSE);
            claseRepository.save(clase);
        }

    }



}
