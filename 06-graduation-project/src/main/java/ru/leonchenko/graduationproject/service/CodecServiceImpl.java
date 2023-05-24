package ru.leonchenko.graduationproject.service;

import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.List;
import java.util.SplittableRandom;

import static ru.leonchenko.graduationproject.enums.Constants.KEY_ALIAS;
import static ru.leonchenko.graduationproject.utils.Utils.generateCertificate;

@Service
public class CodecServiceImpl implements CodecService {

    @SneakyThrows
    public String getRandomKeystore(String input, String securityType, List<String> list) {

        int basicSeed = CharBuffer.wrap(input.toCharArray())
                .chars()
                .sum();

        int basicRnd = new SplittableRandom(basicSeed)
                .nextInt(list.size());

        int secRnd = new SecureRandom(input.getBytes())
                .nextInt(list.size());

        return switch (securityType.toUpperCase()) {
            case "BASIC" -> list.get(basicRnd);
            case "SECURE" -> list.get(secRnd);
            default -> "";
        };
    }

    @SneakyThrows
    public KeyStore generateKeystoreWithKeys(String tempDir, String keystoreType, String password) {

        KeyStore keyStore = KeyStore.getInstance(keystoreType);
        keyStore.load(null, null);

        var generator = KeyPairGenerator.getInstance("RSA");
        generator.initialize(Integer.parseInt("2048"));
        var keyPair = generator.generateKeyPair();

        var certificate = generateCertificate(keyPair);
        X509Certificate[] certChain = new X509Certificate[1];
        certChain[0] = certificate;

        keyStore.setKeyEntry(KEY_ALIAS, keyPair.getPrivate(), password.toCharArray(), certChain);
        keyStore.store(new FileOutputStream(tempDir + "keystore." + keystoreType.toLowerCase()), password.toCharArray());

        System.out.println(tempDir);

        return keyStore;
    }

    @SneakyThrows
    public byte[] dataCoder(String data, PublicKey publicKey) {

        var cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        var dataBytes = data.getBytes(StandardCharsets.UTF_8);
        System.out.println(dataBytes.length);
        var encryptedDataBytes = cipher.doFinal(dataBytes);

        System.out.println(encryptedDataBytes.length);

        return encryptedDataBytes;
    }

    @SneakyThrows
    public byte[] dateDecoder(byte[] data, Key privateKey) {

        Cipher decryptCipher = Cipher.getInstance("RSA");
        decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);

        var decryptedMessageBytes = decryptCipher.doFinal(data);

        return decryptedMessageBytes;
    }

    @SneakyThrows
    public byte[] signWithSignature(KeyPair keyPair, byte[] encryptedText) {

        var sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(keyPair.getPrivate());
        sign.update(encryptedText);

        var digitalSignature = sign.sign();

        System.out.println("Cообщение успешно подписано!");

        return digitalSignature;
    }

    @SneakyThrows
    public String verifySignature(KeyPair keyPair, byte[] encryptedText, byte[] digitalSignature) {

        var sign = Signature.getInstance("SHA256withRSA");
        sign.initSign(keyPair.getPrivate());
        sign.update(encryptedText);

        sign.initVerify(keyPair.getPublic());
        sign.update(encryptedText);
        var isSignatureCorrect = sign.verify(digitalSignature);
        var state = isSignatureCorrect ? "Подпись прошла проверку!" : "Подпись не прошла проверку!";

        return state;
    }

    @SneakyThrows
    public KeyStore loadKeystore(String keystorePath, String password) {

        var extension = FilenameUtils.getExtension(keystorePath).toUpperCase();
        KeyStore keyStore = KeyStore.getInstance(extension);
        keyStore.load(new FileInputStream(keystorePath), password.toCharArray());

        return keyStore;
    }
}
