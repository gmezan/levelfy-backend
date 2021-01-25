package com.uc.backend.util;

import com.uc.backend.entity.Paquete;

import java.util.HashMap;

public abstract class CustomConstants {
    // HashMaps:
    public final static HashMap<Integer, String> DAYS = new HashMap<>();
    public final static HashMap<Integer, String> EVALUATIONS = new HashMap<>();
    //public final static HashMap<Integer, Integer> TIEMPOCLASE = new HashMap<>();
    public final static HashMap<String, String> UNIVERSITIES = new HashMap<>();
    public final static HashMap<String, String> SERVICES = new HashMap<>();
    public final static HashMap<Integer, String> ROLES = new HashMap<>();
    public final static HashMap<Integer, Paquete> PAQUETES = new HashMap<>();

    public final static String SERVICIO_ASESORIA_PERSONALIZADA = "ASES_PER";
    public final static String SERVICIO_ASESORIA_PAQUETE = "ASES_PAQ";
    public final static String SERVICIO_SELF_PACED = "SELF_P";
    /// Amazon S3


    public final static String BUCKET_NAME = "myuniversityclass";

    public final static String FOLDER_NAME = "foro";

    public final static String FILE_NAME = "probando.jpeg";

    public final static String FILE_PATH = "C:\\\\Users\\\\Dino Lopez\\\\Downloads\\\\CASO BCP (1) (1) (1).pdf";

    public final static String BUCKET_FILE_PATH = "foro/Invoice 1May.pdf";

    public final static String LOCAL_DOWNLOAD_PATH = "C:\\\\Users\\\\Dino Lopez\\\\Downloads\\\\Invoice_May_new.pdf";

    public static final String SUFFIX = "/";
    public final static String AMAZON_KEY_S3="AKIAVFA75GPQ36NIXBUH";
    public final static String AMAZON_SEC_S3="1nbJ9iFRZbvc7wUNqWa4BFhmREpNwqAT8YQbe3l8";
    public final static int PRECIO_BASE_ASES_PER_PUCP = 30;
    public final static int PRECIO_BASE_ASES_PAQ_PUCP = 30;
    public final static int DIA_DOMINGO=7;
    public final static int DIA_LUNES=1;
    public final static int DIA_MARTES=2;
    public final static int DIA_MIERCOLES=3;
    public final static int DIA_JUEVES=4;
    public final static int DIA_VIERNES=5;
    public final static int DIA_SABADO=6;

    public final static int EVALUACION_PC1 = 1;
    public final static int EVALUACION_PC2 = 2;
    public final static int EVALUACION_PC3= 3;
    public final static int EVALUACION_PC4 = 4;
    public final static int EVALUACION_PC5 = 5;
    public final static int EVALUACION_EX_PARCIAL = 6;
    public final static int EVALUACION_EX_FINAL = 7;
    public final static int EVALUACION_EX_1 = 8;
    public final static int EVALUACION_EX_2 = 9;
    public final static int EVALUACION_EX_3 = 10;
    public final static int EVALUACION_EX_4 = 11;
    public final static int EVALUACION_EX_ESPECIAL = 12;

    public static final int MINUTES_EXPIRATION_TIME_FOR_ASES_PER=240;

    public static String[] METODOS_DE_PAGO = new String[]{"Yape", "Plin", "Lukita", "Depósito a cuenta BCP", "Depósito a cuenta BBVA"};

    public static final String[] ADMINS = new String[]{"gustavomeza27@gmail.com","josue.12lch@gmail.com"};

    static{
        // Table DIA
        DAYS.put(1,"Lunes");
        DAYS.put(2,"Martes");
        DAYS.put(3,"Miércoles");
        DAYS.put(4,"Jueves");
        DAYS.put(5,"Viernes");
        DAYS.put(6,"Sábado");
        DAYS.put(7,"Domingo");

        // Table Evaluación
        EVALUATIONS.put(1,"PC1");
        EVALUATIONS.put(2,"PC2");
        EVALUATIONS.put(3,"PC3");
        EVALUATIONS.put(4,"PC4");
        EVALUATIONS.put(5,"PC5");
        EVALUATIONS.put(6,"Examen Parcial");
        EVALUATIONS.put(7,"Examen Final");
        EVALUATIONS.put(8,"Examen 1");
        EVALUATIONS.put(9,"Examen 2");
        EVALUATIONS.put(10,"Examen 3");
        EVALUATIONS.put(11,"Examen 4");
        EVALUATIONS.put(12,"Examen Especial");
        EVALUATIONS.put(13,"Libre");

        //Table UNIVERSIDAD
        UNIVERSITIES.put("PUCP","Pontificia Universidad Católica del Perú");
        UNIVERSITIES.put("UPC","Universidad Peruana de Ciencias Aplicadas");
        UNIVERSITIES.put("UL","Universidad de Lima");
        UNIVERSITIES.put("UPCH","Universidad Peruana Cayetano Heredia");
        UNIVERSITIES.put("LIBRE","Libre");

        //Table Servicio
        SERVICES.put("ASES_PER","Asesoría personalizada");
        SERVICES.put("ASES_PAQ","Paquete de asesorías");
        SERVICES.put("SELF_P","Aprende a tu ritmo");

        //Table ROLS
        ROLES.put(1,"client");
        ROLES.put(2,"teach");
        ROLES.put(3,"admin");

        //Table TIEMPOCLASE (tiempo de suscripción)
        //TIEMPOCLASE.put(1,7); //una semana
        //TIEMPOCLASE.put(2,30); // un mes
        //TIEMPOCLASE.put(3,60); //dos meses

        //PAQUETES
        //---- ASESORIAS PERSONALIZADAS
        PAQUETES.put(1, new Paquete("Asesoria personalizada",1, SERVICIO_ASESORIA_PERSONALIZADA));
        //---- ASESORIAS PAQUETES
        PAQUETES.put(2, new Paquete("Paquete de asesorías 2",2, SERVICIO_ASESORIA_PAQUETE));
        PAQUETES.put(3, new Paquete("Paquete de asesorías 3",3, SERVICIO_ASESORIA_PAQUETE));
        PAQUETES.put(4, new Paquete("Paquete de asesorías 4",4, SERVICIO_ASESORIA_PAQUETE));
        //---- SELF PACED
        PAQUETES.put(5, new Paquete("Clase aprende a tu ritmo",0, SERVICIO_SELF_PACED));


    }


}
