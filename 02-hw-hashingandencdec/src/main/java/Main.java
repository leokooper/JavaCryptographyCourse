import enums.CodecMode;

import javax.crypto.*;
import java.security.*;
import java.util.Locale;
import java.util.Scanner;

import static utils.CodecUtils.*;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, InvalidAlgorithmParameterException {

        System.out.println("Введите данные для вычисления hash-суммы, кодирования и декодирования");
        var scanner = new Scanner(System.in);
        var data = scanner.nextLine();

        var dataHash = getSha256Hash(data);
        System.out.println("Hash-сумма алгоритма SHA-256: " + dataHash.toUpperCase(Locale.ROOT));

        var keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        var sk = keyGen.generateKey();

        var encMessage = messageAesCodec(data, sk, CodecMode.ENCODE);
        System.out.println("Зашивроанное сообщение, кодированное в Base64: " + encMessage);

        var decMessage = messageAesCodec(encMessage, sk, CodecMode.DECODE);
        System.out.println("Расшифрованное сообщение: " + decMessage);

        System.out.println("Результат валидации hash-суммы дешифрованного сообщения: " + validateHash(dataHash, decMessage));
    }
}
