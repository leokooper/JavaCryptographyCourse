import enums.Prediction;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static utils.Utils.getSecurityAnswer;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        String name = scanner.next();

        String securityType = scanner.next();

        List<String> list = Stream.of(Prediction.values())
                .map(Prediction::getValue)
                .collect(Collectors.toList());

        String securityAnswer = getSecurityAnswer(name, securityType, list);

        System.out.println("Привет: " + name + "! " + securityAnswer);
    }
}
