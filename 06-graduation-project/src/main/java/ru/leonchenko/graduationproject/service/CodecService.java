package ru.leonchenko.graduationproject.service;

import java.security.Key;
import java.security.KeyPair;
import java.security.KeyStore;
import java.security.PublicKey;
import java.util.List;

public interface CodecService {

    /**
     * @param input - used for seed
     * @param securityType - security type name
     * @param list - list from which to select
     * @return type of keystore
     */
    String getRandomKeystore(String input, String securityType, List<String> list);

    /**
     * @param tempDir - full path to temp directory
     * @param keystoreType - type of keystore
     * @param password - password for keystore
     * @return keystore with keys
     */
    KeyStore generateKeystoreWithKeys(String tempDir, String keystoreType, String password);

    /**
     * @param data - algorithm for KeyFactory
     * @param publicKey - publicKey for encryption
     * @return byte array of encrypted data
     */
    byte[] dataCoder(String data, PublicKey publicKey);

    /**
     * @param data - algorithm for KeyFactory
     * @param privateKey - private key for decryption
     * @return - byte array of decrypted data
     */
    byte[] dateDecoder(byte[] data, Key privateKey);

    /**
     * @param keyPair - keyPair for signification
     * @param encryptedText - encrypted text data
     * @return digital signature byte array
     */
    byte[] signWithSignature(KeyPair keyPair, byte[] encryptedText);

    /**
     * @param keyPair - keyPair for signification
     * @param encryptedText - encrypted text data
     * @param digitalSignature - digital signature byte array
     * @return sign verification flag
     */
    String verifySignature(KeyPair keyPair, byte[] encryptedText, byte[] digitalSignature);

    /**
     * @param keystorePath - full path to keystore
     * @param password - keystore password
     * @return KeyStore - JKS or JCEKS keystore
     */
    KeyStore loadKeystore(String keystorePath, String password);
}
