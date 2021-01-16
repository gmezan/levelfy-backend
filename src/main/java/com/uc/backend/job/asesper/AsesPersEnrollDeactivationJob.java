package com.uc.backend.job.asesper;

import org.springframework.stereotype.Component;

@Component
public class AsesPersEnrollDeactivationJob   {/*
    private static final Logger logger = LoggerFactory.getLogger(AsesPersEnrollDeactivationJob.class);

    @Autowired
    EnrollmentRepository claseEnrollRepository;

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
*/}
