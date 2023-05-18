import lombok.SneakyThrows;

import java.io.FileInputStream;
import java.security.*;
import java.util.Scanner;

import static enums.Constants.*;
import static enums.Path.ROOT_PATH;
import static utils.KeystoreUtils.messageCoDec;

public class MainPlus {

    @SneakyThrows
    public static void main(String[] args) {

        var scanner = new Scanner(System.in);

        System.out.println("Введите имя файла хранилища (keystore)");
        String keystoreName = scanner.next();

        System.out.println("Имя ключа шифрования");
        String keyAlias = scanner.next();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(new FileInputStream(ROOT_PATH.getValue() + keystoreName + ".jks"), KEY_PASSWORD.toCharArray());

        Key privateKey = keyStore.getKey(keyAlias, KEY_PASSWORD.toCharArray());
        PublicKey publicKey = keyStore.getCertificate(keyAlias).getPublicKey();

        var decodedMessage = messageCoDec("java", publicKey, privateKey, false);
        System.out.println(new String(decodedMessage));
    }
}
