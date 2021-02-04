package com.uc.backend.enums;

public enum EvaluationName {
    PC1, PC2, PC3, PC4, PC5, ExamenParcial, ExamenFinal, Examen1, Examen2, Examen3, Examen4,
    ExamenEspecial, Libre;

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
            case ExamenParcial:
                return "Examen Parcial";
            case ExamenFinal:
                return "Examen Final";
            case Libre:
                return "Libre";
            case Examen1:
                return "Examen 1";
            case Examen2:
                return "Examen 2";
            case Examen3:
                return "Examen 3";
            case Examen4:
                return "Examen 4";
            case ExamenEspecial:
                return "Examen Especial";

        }
        return "";

    }

}
