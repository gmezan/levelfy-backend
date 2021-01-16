package com.uc.backend.job;

import org.springframework.stereotype.Component;


@Component
public class ClaseDeactivationJob  {/*
    private static final Logger logger = LoggerFactory.getLogger(ClaseDeactivationJob.class);

    @Autowired
    ServiceRepository claseRepository;

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

*/

}
