package ru.leonchenko.graduationproject.utils;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.x509.X509V3CertificateGenerator;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.Date;

import java.util.UUID;

public class Utils {

    /**
     * @param keyPair - keyPair for certificate generation
     * @return - X509Certificate type certificate
     */
    @SneakyThrows
    public static X509Certificate generateCertificate(KeyPair keyPair) {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
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
     * Создание временного каталога, где мы и будем хранить все файлы до их архивации
     * @return путь временного каталога хранения файлов
     */
    public static String createTempDirectory() throws IOException {
        Path path = Paths.get(FileUtils.getTempDirectory().getAbsolutePath(), UUID.randomUUID().toString(), "temp");
        return Files.createDirectories(path).toFile().getAbsolutePath();
    }
}
