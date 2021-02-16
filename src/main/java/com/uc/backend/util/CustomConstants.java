package com.uc.backend.util;

import com.uc.backend.entity.Service;
import com.uc.backend.enums.LevelfyServiceType;
import com.uc.backend.enums.UniversityName;
import com.uc.backend.util.prices.AsesPaqPriceDocument;
import com.uc.backend.util.prices.AsesPerPriceDocument;
import com.uc.backend.util.prices.MarPriceDocument;
import com.uc.backend.util.prices.ServicePriceDocument;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public abstract class CustomConstants {
    // HashMaps:
    public final static HashMap<UniversityName, String> UNIVERSITIES = new HashMap<>();
    public final static HashMap<UniversityName, HashMap<LevelfyServiceType, List<ServicePriceDocument>>> PRICES = new HashMap<>();

    /// Amazon S3
    public final static String BUCKET_NAME = "myuniversityclass";
    public final static String FOLDER_NAME = "foro";
    public final static String FILE_NAME = "probando.jpeg";


    public static final String SUFFIX = "/";
    public final static String AMAZON_KEY_S3="AKIAVFA75GPQ36NIXBUH";
    public final static String AMAZON_SEC_S3="1nbJ9iFRZbvc7wUNqWa4BFhmREpNwqAT8YQbe3l8";


    public static final int MINUTES_EXPIRATION_TIME_FOR_ASES_PER=240;

    public static String[] METODOS_DE_PAGO = new String[]{"Yape", "Plin", "Lukita", "Depósito a cuenta BCP", "Depósito a cuenta BBVA"};

    public static final String[] ADMINS = new String[]{"gustavomeza27@gmail.com","josue.12lch@gmail.com"};

    static{

        //Table UNIVERSIDAD
        UNIVERSITIES.put(UniversityName.PUCP,"Pontificia Universidad Católica del Perú");
        UNIVERSITIES.put(UniversityName.UPC, "Universidad Peruana de Ciencias Aplicadas");
        UNIVERSITIES.put(UniversityName.UL, "Universidad de Lima");
        UNIVERSITIES.put(UniversityName.UPCH, "Universidad Peruana Cayetano Heredia");
        UNIVERSITIES.put(UniversityName.OPEN,"Libre");



        PRICES.put(UniversityName.PUCP, new HashMap<LevelfyServiceType, List<ServicePriceDocument>>(){{
                put(LevelfyServiceType.ASES_PAQ, new ArrayList<ServicePriceDocument>(){{
                    add(new AsesPaqPriceDocument(BigDecimal.valueOf(80), 7, ServicePriceDocument.LevelfyGeneralServiceType.EXAM));
                    add(new AsesPaqPriceDocument(BigDecimal.valueOf(40), 5, ServicePriceDocument.LevelfyGeneralServiceType.PC));
                }});
                put(LevelfyServiceType.MAR, new ArrayList<ServicePriceDocument>(){{
                    add(new MarPriceDocument(BigDecimal.valueOf(30), 3));
                }});
                put(LevelfyServiceType.ASES_PER, new ArrayList<ServicePriceDocument>(){{
                    add(new AsesPerPriceDocument(BigDecimal.valueOf(24), 1));
                    add(new AsesPerPriceDocument(BigDecimal.valueOf(19), 2));
                    add(new AsesPerPriceDocument(BigDecimal.valueOf(16), 3));
                    add(new AsesPerPriceDocument(BigDecimal.valueOf(14), 4));
                    add(new AsesPerPriceDocument(BigDecimal.valueOf(12), 5));
                }});
            }}
        );
        PRICES.put(UniversityName.UL, new HashMap<LevelfyServiceType, List<ServicePriceDocument>>(){{
                    put(LevelfyServiceType.ASES_PAQ, new ArrayList<ServicePriceDocument>(){{
                        add(new AsesPaqPriceDocument(BigDecimal.valueOf(80), 7, ServicePriceDocument.LevelfyGeneralServiceType.EXAM));
                        add(new AsesPaqPriceDocument(BigDecimal.valueOf(40), 5, ServicePriceDocument.LevelfyGeneralServiceType.PC));
                    }});
                    put(LevelfyServiceType.MAR, new ArrayList<ServicePriceDocument>(){{
                        add(new MarPriceDocument(BigDecimal.valueOf(30), 3));
                    }});
                    put(LevelfyServiceType.ASES_PER, new ArrayList<ServicePriceDocument>(){{
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(24), 1));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(19), 2));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(16), 3));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(14), 4));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(12), 5));
                    }});
                }}
        );
        PRICES.put(UniversityName.UDEP, new HashMap<LevelfyServiceType, List<ServicePriceDocument>>(){{
                    put(LevelfyServiceType.ASES_PAQ, new ArrayList<ServicePriceDocument>(){{
                        add(new AsesPaqPriceDocument(BigDecimal.valueOf(80), 7, ServicePriceDocument.LevelfyGeneralServiceType.EXAM));
                        add(new AsesPaqPriceDocument(BigDecimal.valueOf(40), 5, ServicePriceDocument.LevelfyGeneralServiceType.PC));
                    }});
                    put(LevelfyServiceType.MAR, new ArrayList<ServicePriceDocument>(){{
                        add(new MarPriceDocument(BigDecimal.valueOf(30), 3));
                    }});
                    put(LevelfyServiceType.ASES_PER, new ArrayList<ServicePriceDocument>(){{
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(24), 1));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(19), 2));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(16), 3));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(14), 4));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(12), 5));
                    }});
                }}
        );
        PRICES.put(UniversityName.UPCH, new HashMap<LevelfyServiceType, List<ServicePriceDocument>>(){{
                    put(LevelfyServiceType.ASES_PAQ, new ArrayList<ServicePriceDocument>(){{
                        add(new AsesPaqPriceDocument(BigDecimal.valueOf(80), 7, ServicePriceDocument.LevelfyGeneralServiceType.EXAM));
                        add(new AsesPaqPriceDocument(BigDecimal.valueOf(40), 5, ServicePriceDocument.LevelfyGeneralServiceType.PC));
                    }});
                    put(LevelfyServiceType.MAR, new ArrayList<ServicePriceDocument>(){{
                        add(new MarPriceDocument(BigDecimal.valueOf(30), 3));
                    }});
                    put(LevelfyServiceType.ASES_PER, new ArrayList<ServicePriceDocument>(){{
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(24), 1));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(19), 2));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(16), 3));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(14), 4));
                        add(new AsesPerPriceDocument(BigDecimal.valueOf(12), 5));
                    }});
                }}
        );


    }


}
