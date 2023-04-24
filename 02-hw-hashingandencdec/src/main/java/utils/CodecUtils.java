package utils;

import enums.CodecMode;
import org.bouncycastle.util.encoders.Hex;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;

public class CodecUtils {

    /**
     * @param data - входные данные для расчета hash-суммы
     * @return - hash-сумма в формате SHA-256
     */
    public static String getSha256Hash(String data) throws NoSuchAlgorithmException {
        var messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(data.getBytes());

        byte[] hash = messageDigest.digest(data.getBytes(UTF_8));
        return new String(Hex.encode(hash));
    }

    /**
     * @param message - входное сообщение для кодирования/декодирования
     * @param key - ключ шифрования
     * @param mode - режим кодирование/декодирование
     * @return - в режиме ENCODE возвращает зашифрованное сообщение в формате Base64, в режиме DECODE взвращает расшифрованное сообщение
     */
    public static String messageAesCodec(String message, Key key, CodecMode mode) throws InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchPaddingException, NoSuchAlgorithmException {

        var ivspec = new IvParameterSpec(new byte[16]);

        var secretKey = new SecretKeySpec(key.getEncoded(), "AES");
        var cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        return switch (mode) {
            case ENCODE -> {
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivspec);
                yield Base64.getEncoder().encodeToString(cipher.doFinal(message.getBytes(StandardCharsets.UTF_8)));
            }
            case DECODE -> {
                cipher.init(Cipher.DECRYPT_MODE, secretKey, ivspec);
                yield new String(cipher.doFinal(Base64.getDecoder().decode(message)));
            }
        };
    }

    /**
     * @param hash - эталонная hash-сумма с которой нужно сравнить
     * @param data - данные для вычисления новой эталонной hash-суммы
     * @return - результат сравнения эталонной и новой расчитанной hash-суммы
     */
    public static String validateHash(String hash, String data) throws NoSuchAlgorithmException {
        var newHash = getSha256Hash(data);
        if (newHash.equals(hash)){
            return "Hash-сумма валидна!";
        } else {
            return "Hash-сумма не валидна!";
        }
    }
}
