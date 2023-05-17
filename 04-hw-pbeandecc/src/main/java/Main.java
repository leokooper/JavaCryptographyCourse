import javax.crypto.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

import static enums.Algorithm.AES;
import static utils.PBEUtils.*;

public class Main {

    public static void main(String[] args) {

        var scanner = new Scanner(System.in);

        System.out.println("Введите информацию которую нужно зашифровать");
        String data = scanner.next();

        System.out.println("Введите пароль шифрования");
        String password = scanner.next();

        //1. Генерация и сохранения ключей в файлы
        generateAndSaveAesKey(password);

        //2. Загружаем сгенерированный ключ
        SecretKey key = loadAesKeys(AES.name());

        //3. Шифруем и расшивровываем сообщение
        var decMessage = new String(messageCoDec(data, key, true));
        var encMessage = new String(messageCoDec(data, key, false));

        System.out.printf("Зашифрованное сообщение: %s%n" +
                "Расшифрованное сообщение: %s", decMessage, encMessage);


    }
}
