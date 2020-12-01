package com.uc.backend.job;

import com.uc.backend.entity.ClaseEnroll;
import com.uc.backend.repository.ClaseEnrollRepository;
import com.uc.backend.service.CustomEmailService;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.uc.backend.utils.CustomConstants.SERVICIO_ASESORIA_PAQUETE;
import static com.uc.backend.utils.CustomConstants.SERVICIO_ASESORIA_PERSONALIZADA;

public class ClaseEnrollDeactivationJob extends QuartzJobBean {
    private static final Logger logger = LoggerFactory.getLogger(ClaseEnrollDeactivationJob.class);

    @Autowired
    ClaseEnrollRepository claseEnrollRepository;

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        logger.info("Executing Job with key {}", jobExecutionContext.getJobDetail().getKey());

        List<ClaseEnroll> claseEnrollsAsesPaq = new ArrayList<ClaseEnroll>(){{
                claseEnrollRepository.findClaseEnrollsByActiveIsTrueAndClase_DisponibleIsFalseAndClase_Servicio(SERVICIO_ASESORIA_PAQUETE).
                        forEach(claseEnroll -> {
                            claseEnroll.setActive(Boolean.FALSE);
                            add(claseEnroll);
                        });
        }};

        List<ClaseEnroll> claseEnrollsAsesPer = new ArrayList<ClaseEnroll>(){{
            claseEnrollRepository.findClaseEnrollsByActiveIsTrueAndClase_Servicio(SERVICIO_ASESORIA_PERSONALIZADA)
                    .forEach(claseEnroll -> {
                        if (claseEnroll.getFinasesoria().plusHours(1).isBefore(LocalDateTime.now().minusHours(5)))
                        {
                            claseEnroll.setActive(Boolean.FALSE);
                            add(claseEnroll);
                        }
                    });
        }};

        if (!claseEnrollsAsesPer.isEmpty())
            claseEnrollRepository.saveAll(claseEnrollsAsesPer);

        if (!claseEnrollsAsesPaq.isEmpty())
            claseEnrollRepository.saveAll(claseEnrollsAsesPaq);

    }
}
