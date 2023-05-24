package ru.leonchenko.graduationproject.model;

import lombok.*;

@Getter @Builder
public class CipherRs {
    private String keystoreType;
    private String keyName;
    private String cipherWord;
    private String signature;
    private String keystorePath;
}
