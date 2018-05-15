package eu.dozd.mongo.crypto;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.nio.*;
import java.security.*;
import java.util.*;
import java.util.concurrent.locks.*;

/**
 * Created by Gernot Pansy <gernot.pansy@ut11.net> on 15.05.18.
 */
public class AesCbcCryptoProvider implements CryptoProvider {

    private static final String DEFAULT_ALGORITHM = "AES";
    private static final int DEFAULT_SALT_SIZE = 16;

    private SecretKeySpec key;
    private Cipher encryptCipher;
    private Cipher decryptCipher;
    private ReentrantLock encryptionLock;
    private ReentrantLock decryptionLock;
    private SecureRandom secureRandom;

    /**
     * Creates a new AES/CBC crypto provider with an secret key.
     *
     * @param secret a 128, 192 or 256 bytes long secret key
     */
    public AesCbcCryptoProvider(byte[] secret) {

        key = new SecretKeySpec(secret, DEFAULT_ALGORITHM);

        try {
            encryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            decryptCipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        } catch (Exception e) {
            throw new RuntimeException("AES/CBC cipher couldn't be initialized", e);
        }

        encryptionLock = new ReentrantLock();
        decryptionLock = new ReentrantLock();
        secureRandom = new SecureRandom();
    }

    @Override
    public byte[] encrypt(Object value) {
        this.encryptionLock.lock();
        try {
            byte[] salt = generateSalt();
            byte[] data;
            try {
                encryptCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(salt));
                data = encryptCipher.doFinal(DataSerializerUtil.serialize(value));
            } catch (Exception e) {
                throw new RuntimeException("AES/CBC cipher failed to encrypt value", e);
            }
            ByteBuffer result = ByteBuffer.allocate(salt.length + data.length);
            result.put(salt);
            result.put(data);
            return result.array();
        } finally{
            this.encryptionLock.unlock();
        }
    }

    @Override
    public Object decrypt(byte[] data) {
        this.decryptionLock.lock();
        try {
            Object value;
            try {
                byte[] salt = Arrays.copyOfRange(data, 0, DEFAULT_SALT_SIZE);
                decryptCipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(salt));
                value = DataSerializerUtil.deserialize(
                        decryptCipher.doFinal(Arrays.copyOfRange(data, DEFAULT_SALT_SIZE, data.length))
                );
            } catch (Exception e) {
                throw new RuntimeException("AES/CBC cipher failed to decrypt data", e);
            }
            return value;
        } finally{
            this.decryptionLock.unlock();
        }
    }

    private byte[] generateSalt() {
        byte[] salt = new byte[DEFAULT_SALT_SIZE];
        secureRandom.nextBytes(salt);
        return salt;
    }


}
