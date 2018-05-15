package eu.dozd.mongo.crypto;

/**
 * Created by Gernot Pansy <gernot.pansy@ut11.net> on 15.05.18.
 */
public interface CryptoProvider {

    /**
     * Encrypts the provided value.
     *
     * @param value the value
     * @return encrypted value data
     */
    byte[] encrypt(Object value);

    /**
     * Decrypt the provided data.
     *
     * @param data AES encrypted value
     * @return decrypted value
     */
    Object decrypt(byte[] data);
}
