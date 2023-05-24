package ru.leonchenko.graduationproject;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

@SpringBootTest
class GraduationProjectApplicationTests {

	@Test
	void contextLoads() {
	}


		@Test
		public void givenRsaKeyPair_whenEncryptAndDecryptString_thenCompareResults() throws Exception {
			KeyPairGenerator generator = KeyPairGenerator.getInstance("RSA");
			generator.initialize(2048);
			KeyPair pair = generator.generateKeyPair();
			PrivateKey privateKey = pair.getPrivate();
			PublicKey publicKey = pair.getPublic();

			String secretMessage = "Baeldung secret message";
			Cipher encryptCipher = Cipher.getInstance("RSA");
			encryptCipher.init(Cipher.ENCRYPT_MODE, publicKey);
			byte[] secretMessageBytes = secretMessage.getBytes(StandardCharsets.UTF_8);
			System.out.println(secretMessage.length());
			byte[] encryptedMessageBytes = encryptCipher.doFinal(secretMessageBytes);
			System.out.println(encryptedMessageBytes.length);

			Cipher decryptCipher = Cipher.getInstance("RSA");
			decryptCipher.init(Cipher.DECRYPT_MODE, privateKey);
			byte[] decryptedMessageBytes = decryptCipher.doFinal(encryptedMessageBytes);
			String decryptedMessage = new String(decryptedMessageBytes, StandardCharsets.UTF_8);

			Assertions.assertEquals(secretMessage, decryptedMessage);
		}


	}
