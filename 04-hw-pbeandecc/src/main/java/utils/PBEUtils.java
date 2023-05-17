package utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.security.spec.KeySpec;

import static enums.Algorithm.AES;
import static enums.Path.ROOT_PATH;
import static enums.SignatureAlgorithm.PBKDF_2_WITH_HMAC_SHA_256;

public class PBEUtils {

    /**
     * @param password - password for generating AES-256 key
     */
    @SneakyThrows
    public static void generateAndSaveAesKey(String password){

        var SALT = "salt";
        var ITERATION_COUNT = 65536;
        var KEY_LENGTH = 256;

        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), SALT.getBytes(), ITERATION_COUNT, KEY_LENGTH);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(PBKDF_2_WITH_HMAC_SHA_256.getValue());
        SecretKey aesKey = new SecretKeySpec(keyFactory.generateSecret(keySpec).getEncoded(), AES.name());

        FileUtils.writeByteArrayToFile(new File(ROOT_PATH.getValue() + "/aes.key"), aesKey.getEncoded());
    }

    /**
     * @param algorithm - algorithm for KeyFactory
     */
    @SneakyThrows
    public static SecretKey loadAesKeys(String algorithm){

        var encodedAesKey = FileUtils.readFileToByteArray(new File(ROOT_PATH.getValue() + "/aes.key"));

        return new SecretKeySpec(encodedAesKey, algorithm);
    }

    /**
     * @param data - algorithm for KeyFactory
     * @param secretKey - SecretKey for encryption or decryption
     * @param isEncryptMode - encryption or decryption mode
     * @return - byte array of encrypted or decrypted data
     */
    @SneakyThrows
    public static byte[] messageCoDec(String data, SecretKey secretKey, boolean isEncryptMode) {

        var cipher = Cipher.getInstance(AES.name());

        var dataToEncrypt = data.getBytes();
        byte[] fluidData;

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        fluidData = cipher.doFinal(dataToEncrypt);

        if (!isEncryptMode) {
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            fluidData = cipher.doFinal(fluidData);
        }

        return fluidData;
    }
}
