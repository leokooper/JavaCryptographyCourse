package ru.leonchenko.graduationproject.enums;

public enum Keystore {

    JKS("JKS"),
    JCEKS("JCEKS");

    private final String value;

    Keystore(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
