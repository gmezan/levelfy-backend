package com.uc.backend.enums;

public enum  PaymentMethodName {
    YAPE, PLIN, LUKITA, DEPOSITO_BCP, DEPOSITO_BBVA;

    public String toLocalString() {
        if (this.equals(YAPE)) {
            return "YAPE";
        }
        else if (this.equals(PLIN)) {
            return "PLIN";
        }
        else if (this.equals(LUKITA)) {
            return "LUKITA";
        }
        else if (this.equals(DEPOSITO_BBVA)) {
            return "DEPOSITO A CUENTA BBVA";
        }
        else if (this.equals(DEPOSITO_BCP)) {
            return "DEPOSITO A CUENTA BCP";
        }
        else return "OTRO METODO";
    }
}

