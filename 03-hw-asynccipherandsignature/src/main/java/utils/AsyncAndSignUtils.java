package utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import java.io.File;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import static enums.Algorithm.RSA;
import static enums.Path.ROOT_PATH;
import static enums.SignatureAlgorithm.SHA_256_WITH_RSA;

public class AsyncAndSignUtils {

    /**
     * @param keySize - RSA key size
     */
    @SneakyThrows
    public static void generateAndSaveRsaKeys(String keySize){

        var generator = KeyPairGenerator.getInstance(RSA.name());
        generator.initialize(Integer.parseInt(keySize));

        var keyPair = generator.generateKeyPair();

        var publicKey = keyPair.getPublic();
        var privateKey = keyPair.getPrivate();

        var publicKeyEncoded = new X509EncodedKeySpec(publicKey.getEncoded());
        var privateKeyEncoded = new PKCS8EncodedKeySpec(privateKey.getEncoded());

        FileUtils.writeByteArrayToFile(new File(ROOT_PATH.getValue() + "/public.key"), publicKeyEncoded.getEncoded());
        FileUtils.writeByteArrayToFile(new File(ROOT_PATH.getValue() + "/private.key"), privateKeyEncoded.getEncoded());
    }


    /**
     * @param algorithm - algorithm for KeyFactory
     * @return - RSA KeyPair object
     */
    @SneakyThrows
    public static KeyPair loadRsaKeys(String algorithm){

        var encodedPublicKey = FileUtils.readFileToByteArray(new File(ROOT_PATH.getValue() + "/public.key"));
        var encodedPrivateKey = FileUtils.readFileToByteArray(new File(ROOT_PATH.getValue() + "/private.key"));
                
        var keyFactory = KeyFactory.getInstance(algorithm);

        var publicKeySpec = new X509EncodedKeySpec(encodedPublicKey);
        var publicKey = keyFactory.generatePublic(publicKeySpec);

        var privateKeySpec = new PKCS8EncodedKeySpec(encodedPrivateKey);
        var privateKey = keyFactory.generatePrivate(privateKeySpec);

        return new KeyPair(publicKey, privateKey);
    }

    /**
     * @param data - algorithm for KeyFactory
     * @param keyPair - keyPair for encryption or decryption
     * @param isEncryptMode - encryption or decryption mode
     * @return - byte array of encrypted or decrypted data
     */
    @SneakyThrows
    public static byte[] messageCoDec(String data, KeyPair keyPair, boolean isEncryptMode) {

        var cipher = Cipher.getInstance(RSA.name());

        var publicKey = keyPair.getPublic();
        var privateKey = keyPair.getPrivate();

        var dataToEncrypt = data.getBytes();
        byte[] fluidData;

        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        fluidData = cipher.doFinal(dataToEncrypt);

        if (!isEncryptMode) {
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            fluidData = cipher.doFinal(fluidData);
        }

        return fluidData;
    }

    /**
     * @param keyPair - keyPair for signification
     * @param encryptedText - encrypted text data
     * @param isSingMode - mode to choose signification mode or verification mode
     */
    @SneakyThrows
    public static void signAndVerify(KeyPair keyPair, byte[] encryptedText, boolean isSingMode) {

        var sign = Signature.getInstance(SHA_256_WITH_RSA.getValue());
        sign.initSign(keyPair.getPrivate());
        sign.update(encryptedText);

        var digitalSignature = sign.sign();

        if (isSingMode) {
            System.out.println("Cообщение успешно подписано!");
        } else {
            sign.initVerify(keyPair.getPublic());
            sign.update(encryptedText);
            var isSignatureCorrect = sign.verify(digitalSignature);
            var state = isSignatureCorrect ? "Sign is ok!" : "Sign is not ok!";
            System.out.println(state);
        }
    }
}
