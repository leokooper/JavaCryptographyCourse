package utils;

import java.nio.CharBuffer;
import java.security.SecureRandom;
import java.util.List;
import java.util.SplittableRandom;

public class Utils {
    /**
     * @param name - used for seed
     * @param securityType - security type name
     * @param list - list from which to select
     * @return selected parameter from {@code list}
     */
    public static String getSecurityAnswer(String name, String securityType, List<String> list) {

        int basicSeed = CharBuffer.wrap(name.toCharArray())
                .chars()
                .sum();

        int basicRnd = new SplittableRandom(basicSeed)
                .nextInt(list.size());

        int secRnd = new SecureRandom(name.getBytes())
                .nextInt(list.size());

        return switch (securityType.toUpperCase()) {
            case "BASIC" -> list.get(basicRnd);
            case "SECURED" -> list.get(secRnd);
            default -> "Выберите в следующий раз Basic или Secured";
        };
    }
}
