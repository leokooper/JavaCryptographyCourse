package ru.leonchenko.graduationproject.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@Getter @Setter
@ToString
public class CipherRq {
    private String randomType;
    private String password;
    private String keyWord;
}
