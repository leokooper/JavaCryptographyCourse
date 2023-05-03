import org.junit.Test;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import static enums.CodecMode.*;
import static org.junit.Assert.*;
import static utils.CodecUtils.*;

public class CodecUtilsTest {

    @Test
    public void should_return_proper_hash() throws NoSuchAlgorithmException {

        var data = "Hello!";
        var hash = "1efaf60f34e6d5fe7b052a7516fcef2cbd12054a576cad4265f483a6c6fa26d8";

        var calculatedHash = getSha256Hash(data);

        assertNotNull(calculatedHash);
        assertEquals(hash, calculatedHash);
    }

    @Test
    public void should_return_valid_calculate_hash_message() throws NoSuchAlgorithmException {

        var hash = "1efaf60f34e6d5fe7b052a7516fcef2cbd12054a576cad4265f483a6c6fa26d8";
        var data = "Hello!";

        var result = validateHash(hash, data);

        assertNotNull(result);
        assertEquals(result, "Hash-сумма валидна!");
    }

    @Test
    public void should_return_invalid_calculate_hash_message() throws NoSuchAlgorithmException {

        var hash = "1efaf60f34e6d5fe7b052a7516fcef2cbd12054a576cad4265f483a6c6fa26d7";
        var data = "Hello!";

        var result = validateHash(hash, data);

        assertNotNull(result);
        assertEquals(result, "Hash-сумма не валидна!");
    }

    @Test
    public void should_proper_encode_input_data() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {

        var data = "Hello!";
        var sk = new SecretKeySpec("thisIsSecretKey!".getBytes(StandardCharsets.UTF_8), "AES");
        var encodedMessage = "p4kWVgnM6mahXG+RbTqxJg==";

        var result = messageAesCodec(data, sk, ENCODE);

        assertNotNull(result);
        assertEquals(encodedMessage, result);
    }

    @Test
    public void should_proper_decode_input_data() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, IllegalBlockSizeException, NoSuchPaddingException, BadPaddingException, InvalidKeyException {

        var messageToDecode = "p4kWVgnM6mahXG+RbTqxJg==";
        var sk = new SecretKeySpec("thisIsSecretKey!".getBytes(StandardCharsets.UTF_8), "AES");
        var data = "Hello!";

        var result = messageAesCodec(messageToDecode, sk, DECODE);

        assertNotNull(result);
        assertEquals(data, result);
    }
}