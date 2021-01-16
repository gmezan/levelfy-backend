package com.uc.backend.job;

public class ClaseEnrollDeactivationJob { /*
    private static final Logger logger = LoggerFactory.getLogger(ClaseEnrollDeactivationJob.class);

    @Autowired
    EnrollmentRepository claseEnrollRepository;

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

    }*/
}
