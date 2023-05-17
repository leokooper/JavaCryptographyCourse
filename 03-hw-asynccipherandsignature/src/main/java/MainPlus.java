import java.util.Scanner;

import static utils.AsyncAndSignUtils.*;

public class MainPlus {

    public static void main(String[] args)  {

        var scanner = new Scanner(System.in);

        System.out.println("Введите информацию которую нужно зашифровать");
        String data = scanner.next();

        System.out.println("Введите размер RSA-ключа шифрования");
        String keySize = scanner.next();

        //1. Генерация и сохранения ключей в файлы
        generateAndSaveRsaKeys(keySize);

        //2. Считывание ключей из файла
        var keyPair = loadRsaKeys("RSA");

        //3. Зашифрованное сообщение
        var encodedMessage = messageCoDec(data, keyPair, true);

        //4. Подпись зашифрованного сообщения
        signAndVerify(keyPair, encodedMessage, true);

        //5. Расшифрованное сообщение
        var decodedMessage = messageCoDec(data, keyPair, false);

        System.out.println("Расшифрованное сообщение: " + new String(decodedMessage));

        //Проверка подписи зашифрованного сообщения
        signAndVerify(keyPair, encodedMessage, false);
    }
}
