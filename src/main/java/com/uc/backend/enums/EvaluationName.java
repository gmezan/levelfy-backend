package com.uc.backend.enums;

public enum EvaluationName {
    PC1, PC2, PC3, PC4, PC5, EXAM_PARCIAL, EXAM_FINAL, EXAM_1, EXAM_2, EXAM_3, EXAM_4,
    EXAM_ESPECIAL, OPEN;

    public String toCustomString() {
        switch (this) {
            case PC1:
                return "PC1";
            case PC2:
                return "PC2";
            case PC3:
                return "PC3";
            case PC4:
                return "PC4";
            case PC5:
                return "PC5";
            case EXAM_PARCIAL:
                return "Examen Parcial";
            case EXAM_FINAL:
                return "Examen Final";
            case OPEN:
                return "Libre";
            case EXAM_1:
                return "Examen 1";
            case EXAM_2:
                return "Examen 2";
            case EXAM_3:
                return "Examen 3";
            case EXAM_4:
                return "Examen 4";
            case EXAM_ESPECIAL:
                return "Examen Especial";

        }
        return "";

    }

}
