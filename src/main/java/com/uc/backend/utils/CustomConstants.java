package com.uc.backend.utils;

import com.example.universityclass.entity.Paquete;

import java.util.HashMap;

public abstract class CustomConstants {
    // HashMaps:
    public final static HashMap<Integer, String> DIA = new HashMap<>();
    public final static HashMap<Integer, String> EVALUACION = new HashMap<>();
    public final static HashMap<Integer, Integer> TIEMPOCLASE = new HashMap<>();
    public final static HashMap<String, String> UNIVERSIDAD = new HashMap<>();
    public final static HashMap<String, String> SERVICIO = new HashMap<>();
    public final static HashMap<Integer, String> ROLS = new HashMap<>();
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
        DIA.put(1,"Lunes");
        DIA.put(2,"Martes");
        DIA.put(3,"Miércoles");
        DIA.put(4,"Jueves");
        DIA.put(5,"Viernes");
        DIA.put(6,"Sábado");
        DIA.put(7,"Domingo");

        // Table Evaluación
        EVALUACION.put(1,"PC1");
        EVALUACION.put(2,"PC2");
        EVALUACION.put(3,"PC3");
        EVALUACION.put(4,"PC4");
        EVALUACION.put(5,"PC5");
        EVALUACION.put(6,"Examen Parcial");
        EVALUACION.put(7,"Examen Final");
        EVALUACION.put(8,"Examen 1");
        EVALUACION.put(9,"Examen 2");
        EVALUACION.put(10,"Examen 3");
        EVALUACION.put(11,"Examen 4");
        EVALUACION.put(12,"Examen Especial");
        EVALUACION.put(13,"Libre");

        //Table UNIVERSIDAD
        UNIVERSIDAD.put("PUCP","Pontificia Universidad Católica del Perú");
        UNIVERSIDAD.put("UPC","Universidad Peruana de Ciencias Aplicadas");
        UNIVERSIDAD.put("UL","Universidad de Lima");
        UNIVERSIDAD.put("UPCH","Universidad Peruana Cayetano Heredia");
        UNIVERSIDAD.put("LIBRE","Libre");

        //Table Servicio
        SERVICIO.put("ASES_PER","Asesoría personalizada");
        SERVICIO.put("ASES_PAQ","Paquete de asesorías");
        SERVICIO.put("SELF_P","Aprende a tu ritmo");

        //Table ROLS
        ROLS.put(1,"estudiante");
        ROLS.put(2,"asesor");
        ROLS.put(3,"admin");

        //Table TIEMPOCLASE (tiempo de suscripción)
        TIEMPOCLASE.put(1,7); //una semana
        TIEMPOCLASE.put(2,30); // un mes
        TIEMPOCLASE.put(3,60); //dos meses

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
