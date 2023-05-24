package ru.leonchenko.graduationproject.model;

import lombok.*;

@Getter @Builder
public class DecipherRs {
    private String decodedWord;
    private String isSigned;
}
