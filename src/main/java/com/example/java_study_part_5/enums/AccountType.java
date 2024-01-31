package com.example.java_study_part_5.enums;

public enum AccountType {
    Client("Клиентский"),
    InnerBank("Внутрибанковский");

    public final String label;

    AccountType(String label) {
        this.label = label;

    }
}
