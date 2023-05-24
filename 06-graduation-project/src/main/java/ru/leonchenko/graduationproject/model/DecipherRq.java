package ru.leonchenko.graduationproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class DecipherRq {
    private String keystorePath;
    private String keystorePassword;
    private String codedWord;
    private String sign;
    private String keyAlias;
}
