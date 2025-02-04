package com.hasandag.banking.loanapi.enums;

public enum NumberOfInstallments {

    SIX(6),
    NINE(9),
    TWELVE(12),
    TWENTY_FOUR(24);

    private final int value;

    NumberOfInstallments(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
