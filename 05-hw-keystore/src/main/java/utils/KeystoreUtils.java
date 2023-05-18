package utils;

import lombok.SneakyThrows;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import javax.crypto.Cipher;
import java.math.BigInteger;
import java.security.*;
import java.security.cert.X509Certificate;
import java.util.Date;

import static enums.Constants.RSA;

public class KeystoreUtils {

    /**
     * @param keyPair - keyPair for certificate generation
     * @return - X509Certificate type certificate
     */
    @SneakyThrows
    public static X509Certificate generateCertificate(KeyPair keyPair) {
        X509V3CertificateGenerator cert = new X509V3CertificateGenerator();
        cert.setSerialNumber(BigInteger.valueOf(1));   //or generate a random number
        cert.setSubjectDN(new X509Principal("CN=localhost"));  //see examples to add O,OU etc
        cert.setIssuerDN(new X509Principal("CN=localhost")); //same since it is self-signed
        cert.setPublicKey(keyPair.getPublic());
        cert.setNotBefore(new Date());
        cert.setNotAfter(new Date());
        cert.setSignatureAlgorithm("SHA1WithRSA");
        PrivateKey signingKey = keyPair.getPrivate();
        return cert.generate(signingKey, "BC");
    }

    /**
     * @param data - algorithm for KeyFactory
     * @param publicKey - publicKey for encryption or decryption
     * @param privateKey - privateKey for encryption or decryption
     * @param isEncryptMode - encryption or decryption mode
     * @return - byte array of encrypted or decrypted data
     */
    @SneakyThrows
    public static byte[] messageCoDec(String data, PublicKey publicKey, Key privateKey, boolean isEncryptMode) {

        var cipher = Cipher.getInstance(RSA);

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
}
