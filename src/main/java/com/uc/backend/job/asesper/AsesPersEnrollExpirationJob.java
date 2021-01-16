package com.uc.backend.job.asesper;

import org.springframework.stereotype.Component;

@Component
public class AsesPersEnrollExpirationJob  {/*
    private static final Logger logger = LoggerFactory.getLogger(AsesPersEnrollExpirationJob.class);

    @Autowired
    EnrollmentRepository claseEnrollRepository;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();

        ClaseEnroll claseEnroll = claseEnrollRepository.findById(jobDataMap.getInt("idClaseEnroll")).orElse(null);

        if (claseEnroll!=null && !claseEnroll.getPagado()){
            claseEnrollRepository.delete(claseEnroll);
        }
    }*/
}
