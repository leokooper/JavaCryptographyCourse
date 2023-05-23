import lombok.SneakyThrows;

import java.io.FileOutputStream;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Scanner;

import static enums.Constants.*;
import static enums.Path.ROOT_PATH;
import static utils.KeystoreUtils.generateCertificate;
import static utils.KeystoreUtils.messageCoDec;

public class Main {

    @SneakyThrows
    public static void main(String[] args) {

        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

        var scanner = new Scanner(System.in);

        System.out.println("Введите имя файла хранилища (keystore)");
        String keystoreName = scanner.next();

        System.out.println("Введите длину ключа RSA");
        String keySize = scanner.next();

        KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(null, null);

        var generator = KeyPairGenerator.getInstance(RSA);
        generator.initialize(Integer.parseInt(keySize));
        var keyPair = generator.generateKeyPair();

        var certificate = generateCertificate(keyPair);
        X509Certificate[] certChain = new X509Certificate[1];
        certChain[0] = certificate;

        keyStore.setKeyEntry(KEY_ALIAS, keyPair.getPrivate(), KEY_PASSWORD.toCharArray(), certChain);
        keyStore.store(new FileOutputStream(ROOT_PATH.getValue() + keystoreName + ".jks"), KEY_PASSWORD.toCharArray());

        Key privateKey = keyStore.getKey(KEY_ALIAS, KEY_PASSWORD.toCharArray());
        PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();

        var encodedMessage = messageCoDec("java", publicKey, privateKey, true);
        System.out.println(new String(encodedMessage));
    }
}
